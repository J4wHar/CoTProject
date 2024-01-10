const endpoint = 'http://localhost:8080/waspsecurity-1.0-SNAPSHOT/api/localization';
function getCurrentLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            // Success callback
            function (position) {
                const latitude = position.coords.latitude;
                const longitude = position.coords.longitude;
                const coordinates = { latitude, longitude };
                console.log(coordinates)
                // Send coordinates to your endpoint
                sendCoordinates(coordinates);
            },
            // Error callback
            function (error) {
                console.error('Error getting location:', error.message);
            }
        );
    } else {
        console.error('Geolocation is not supported by this browser.');
    }
}

// Function to send coordinates to an endpoint
function sendCoordinates(coordinates) {
    const accessToken = localStorage.getItem('accessToken');
    const email = localStorage.getItem('email')

    coordinates.add(email);

    // Make a POST request to the endpoint
    fetch(endpoint, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${accessToken}`,
        },
        body: JSON.stringify(coordinates),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to send coordinates.');
            }
            console.log('Coordinates sent successfully.');
        })
        .catch(error => {
            console.error('Error sending coordinates:', error.message);
        });
}

setInterval(getCurrentLocation, 5000);

// Call the function to get and send coordinates
getCurrentLocation();
