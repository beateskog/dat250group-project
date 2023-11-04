let yesCount = 0;
let noCount = 0;
let currentPollId;

function addVote(type) {
    if (type === 'yes') {
        yesCount++;
        document.getElementById('yesCount').innerText = yesCount;
    } else if (type === 'no') {
        noCount++;
        document.getElementById('noCount').innerText = noCount;
    }
}

function fetchQuestion() {
    const pollPin = document.getElementById('pollPin').value;
    const apiUrl = `http://localhost:8080/iot/${pollPin}`;

    fetch(apiUrl, {
        headers: {
            "X-API-KEY": "A1b2C3d4E5f6G7h8I9j0KlMnOpQrStUvWxYz01!2@3#4$5%6^7&8*9(0)"
        }})
        .then(response => {
            if (!response.ok) {
                return response.text().then(errorText => { throw new Error(errorText); });
            }
            return response.json();
        })
        .then(data => {
            const questionElement = document.getElementById('question');
            questionElement.innerText = data.question;
            currentPollId = data.pollId;
            console.log("Current Poll ID:", currentPollId);
        })
        .catch((error) => {
            console.error('Error fetching the question:', error);
            const questionElement = document.getElementById('question');
            questionElement.innerText = `Error fetching the question: ${error.message}`;
        });
}

function sendResults() {
    const apiUrl = "http://localhost:8080/iot/votes"; 
    const data = {
        pollId: currentPollId,
        yesVotes: yesCount,
        noVotes: noCount

    };

    fetch(apiUrl, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "X-API-KEY": "A1b2C3d4E5f6G7h8I9j0KlMnOpQrStUvWxYz01!2@3#4$5%6^7&8*9(0)"
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(data => {
        console.log('Success:', data);
        alert('Data sent successfully!');
        resetCounts();
        const questionElement = document.getElementById('question');
        questionElement.innerText = 'Enter a Poll Pin...';
    })
    .catch((error) => {
        console.error('Error:', error);
        alert('Error sending data!');
    });
}

function resetCounts() {
    yesCount = 0;
    noCount = 0;
    document.getElementById('yesCount').innerText = yesCount;
    document.getElementById('noCount').innerText = noCount;
}

