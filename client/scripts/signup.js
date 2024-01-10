// script.js
const singUpEndpoint = 'http://localhost:8080/waspsecurity-1.0-SNAPSHOT/api/users/signup';

function prepareData() {
    const currentDate = new Date().toISOString();

    const firstNameInput = document.getElementById("first-name-input");
    const lastNameInput = document.getElementById("last-name-input");
    const emailInput = document.getElementById("email-input");
    const passwordInput = document.getElementById("password-input");
    const phoneInput = document.getElementById("phone-input");


    var object = {};
    object["email"] = emailInput.value;
    object["firstName"] = firstNameInput.value;
    object["lastName"] = lastNameInput.value;
    object["phoneNumber"] = phoneInput.value;
    object["password"] = passwordInput.value;
    object["created_on"] = currentDate;
    object["archived"] = false;
    object['roles'] = [1];

    console.log(object);
    return JSON.stringify(object);
}

function signUp() {

    fetch(singUpEndpoint, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: prepareData()
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            window.location.href = "alerts.html";
            return response.json();
        })
        .then(data => {
            console.log('Signup successful:', data);
        })
        .catch(error => {
            console.error('Error during signup:', error);
        });
}
