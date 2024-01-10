const singInEndpoint = 'http://localhost:8080/waspsecurity-1.0-SNAPSHOT/api/oauth2/token';

function prepareData() {



    const emailInput = document.getElementById("email-input");
    const passwordInput = document.getElementById("password-input");

    let object = {};

    object["email"] = emailInput.value;
    object["password"] = passwordInput.value;

    object['grandType'] = 'PASSWORD';
    object['refreshToken'] = "empty";

    return JSON.stringify(object);
}

function saveDataToLocalStorage(data) {
    localStorage.setItem('accessToken', data.accessToken);
    localStorage.setItem('email', data.employeeId);
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
            saveDataToLocalStorage(data);
            //redirect to alert page
            window.location.href = 'alerts.html';
        })
        .catch(error => {
            console.error('Error during signup:', error);
        });
}
