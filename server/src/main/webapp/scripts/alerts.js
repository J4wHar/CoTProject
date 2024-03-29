const alertsEndpoint = 'https://api.waspsecurity.tech/waspsecurity-1.0-SNAPSHOT/api/alerts';

function fetchAlerts() {
    const accessToken = localStorage.getItem('accessToken');
    fetch(alertsEndpoint, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log(data[0]);
            displayAlerts(data);
        })
        .catch(error => {
            console.error('Error fetching alerts:', error);
        });
}

function displayAlerts(alerts) {
    const alertsContainer = document.getElementById('alertsContainer');
    alertsContainer.innerHTML = ''; // Clear previous alerts

    if (alerts.length === 0) {
        alertsContainer.innerHTML = '<p>No alerts at the moment.</p>';
    } else {
        const ul = document.createElement('ul');
        alerts.forEach(alert => {
            const li = document.createElement('li');
            li.textContent = "An abnormal behavior was detected at " + alert.timestamp;
            ul.appendChild(li);
        });
        alertsContainer.appendChild(ul);
    }
}

// Fetch alerts every 5 seconds
setInterval(fetchAlerts, 5000);

// Initial fetch when the page loads
fetchAlerts();

function logout() {
    localStorage.clear();
    window.location.href = "signin.html";
}