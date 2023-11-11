authenticateApiKey('A1b2C3d4E5f6G7h8I9j0KlMnOpQrStUvWxYz01!2@3#4$5%6^7&8*9(0)');
let socket;
function connectWebSocket(jwtToken) {
    socket = new WebSocket("ws://localhost:8080/iotWS");

    socket.onopen = function(e) {
      console.log("Connection established!");
      if (socket.readyState === WebSocket.OPEN) {
          socket.send(jwtToken); // Send JWT token as the first message
      }
    };

    socket.onclose = function(event) {
        console.log("WebSocket is closed now:", event.reason, event.code);
    };

    socket.onmessage = function(event) {
      console.log("Message from server: ", event.data);
      var message = JSON.parse(event.data);
      if (message.question) {
        var questionElement = document.getElementById("question");
        questionElement.innerText = message.question;
        currentPollId = message.pollId;
    } else if (message.type === "voteConfirmation") {
        console.log("Vote confirmed:", message.message);
    } else if (message.type === "authenticationSuccess") {
        console.log("Authentication successful:", message.message);
    } else if (message.type === "authenticationFailure") {
        console.log("Authentication failed:", message.message);
    } else {
        console.log("Unknown message type:", message.type);
    }
    };
      
    socket.onerror = function(error) {
        console.error("WebSocket Error: " + error.message);
    };
}

function authenticateApiKey(apiKey) {
    fetch('http://localhost:8080/iot/authenticate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({ 'apiKey': apiKey })
    })
    .then(response => response.text())
    .then(jwtToken => {
        jwt = jwtToken;
        connectWebSocket(jwtToken);
    })
    .catch(error => console.error('Authentication failed:', error));
}

let yesCount = 0;
let noCount = 0;
let currentPollId;

function addVote(type) {
    if (type === "yes"){
        yesCount++;
        document.getElementById("yesCount").innerText = yesCount;
    } else if (type === "no"){
        noCount++;
        document.getElementById("noCount").innerText = noCount;
    }
}

function sendResults() {
    const data = {
      type: "vote",
      pollId: currentPollId,
      yesVotes: yesCount,
      noVotes: noCount
    };
  
    if (socket.readyState === WebSocket.OPEN) {
      socket.send(JSON.stringify(data));
      resetCounts();
      const questionElement = document.getElementById("question");
      questionElement.innerText = "Enter a Poll Pin...";
    } else {
      alert('WebSocket connection is not open.');
    }
  }

function fetchQuestion() {
  const pollPin = document.getElementById('pollPin').value;
  const data = {
    type: "fetchQuestion",
    pollPin: pollPin
  };

  if (socket.readyState === WebSocket.OPEN) {
    socket.send(JSON.stringify(data));
  } else {
    alert('WebSocket connection is not open.');
  }
}
  
function resetCounts() {
    yesCount = 0;
    noCount = 0;
    document.getElementById("yesCount").innerText = yesCount;
    document.getElementById("noCount").innerText = noCount;
}
