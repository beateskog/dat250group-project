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
    const apiUrl = "http://localhost:8080/random-question";

    fetch(apiUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
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
            questionElement.innerText = 'Error fetching the question!';
        });
}

function sendResults() {
    const apiUrl = "http://localhost:8080/iotvotes"; 
    const data = {
        pollId: currentPollId,
        yesVotes: yesCount,
        noVotes: noCount

    };

    fetch(apiUrl, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(data => {
        console.log('Success:', data);
        alert('Data sent successfully!'); // You can replace this with a better feedback mechanism
    })
    .catch((error) => {
        console.error('Error:', error);
        alert('Error sending data!'); // You can replace this with a better feedback mechanism
    });
}

function resetCounts() {
    yesCount = 0;
    noCount = 0;
    document.getElementById('yesCount').innerText = yesCount;
    document.getElementById('noCount').innerText = noCount;
}

window.onload = fetchQuestion;