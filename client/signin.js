singInEndpoint = 'http://localhost:8080/waspsecurity-1.0-SNAPSHOT/api/oauth2/token';

function prepareData() {
    const form = document.getElementById('signInForm');
    const formData = new FormData(form);
    var object = {};
    formData.forEach(function (value, key) {
        object[key] = value;
    });
    object['grandType'] = 'PASSWORD'
    object['refreshToken'] = "empty"
    return JSON.stringify(object);
}

function saveTokenToLocalStorage(token) {
    localStorage.setItem('accessToken', token);
}

function signIn() {


    fetch(singInEndpoint, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: prepareData(),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Signup successful:', data);
            saveTokenToLocalStorage(data.accessToken);
            //redirect to alert page
            window.location.href = 'alerts.html';
        })
        .catch(error => {
            console.error('Error during signup:', error);
        });
}

