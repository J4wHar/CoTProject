// script.js
const singUpEndpoint = 'http://localhost:8080/waspsecurity-1.0-SNAPSHOT/api/users/signup';

function prepareData() {
    const currentDate = new Date().toISOString();
    const form = document.getElementById('signupForm');
    const formData = new FormData(form);
    formData.append('archived', false);
    formData.append('created_on', currentDate);

    const rolesArray = Array.from(formData.getAll('roles')).map(Number);

    formData.delete('roles')

    var object = {};
    formData.forEach(function (value, key) {
        object[key] = value;
    });
    object['roles'] = rolesArray;
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
            return response.json();
        })
        .then(data => {
            console.log('Signup successful:', data);
        })
        .catch(error => {
            console.error('Error during signup:', error);
        });
}
