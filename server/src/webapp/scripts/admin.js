const alertsEndpoint = 'http://localhost:8080/waspsecurity-1.0-SNAPSHOT/api/users';
const addUserEndpoint = 'http://localhost:8080/waspsecurity-1.0-SNAPSHOT/api/users/add-user';


function fetchUsers() {
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
            console.log("hello done")
            displayUsers(data);
        })
        .catch(error => {
            console.error('Error fetching alerts:', error);
        });
}

function displayUsers(users) {
    const usersContainer = document.getElementById('usersContainer');
    usersContainer.innerHTML = ''; // Clear previous user data

    if (users.length === 0) {
        usersContainer.innerHTML = '<p>No users at the moment.</p>';
    } else {
        const ul = document.createElement('ul');
        users.forEach(user => {
            const li = document.createElement('li');
            li.textContent = `Name: ${user.firstName} ${user.lastName}, Email: ${user.email}, Phone: ${user.phoneNumber}, Roles: ${user.roles.join(', ')}`;
            ul.appendChild(li);
        });
        usersContainer.appendChild(ul);
    }
}

function prepareData() {
    const currentDate = new Date().toISOString();

    const firstNameInput = document.getElementById("first-name-input");
    const lastNameInput = document.getElementById("last-name-input");
    const emailInput = document.getElementById("email-input");
    const passwordInput = document.getElementById("password-input");
    const phoneInput = document.getElementById("phone-input");
    const adminCheckbox = document.getElementById("admin-checkbox");
    const userCheckbox = document.getElementById("user-checkbox");

    var object = {};
    object["email"] = emailInput.value;
    object["firstName"] = firstNameInput.value;
    object["lastName"] = lastNameInput.value;
    object["phoneNumber"] = phoneInput.value;
    object["password"] = passwordInput.value;
    object["created_on"] = currentDate;
    object["archived"] = false;
    object['roles'] = [];
    if (adminCheckbox.checked) {
        object['roles'].push(0);
    }
    if (userCheckbox.checked) {
        object['roles'].push(1);
    }
    console.log(object);
    console.log(JSON.stringify(object))
    return JSON.stringify(object);
}

function addUser() {
    const accessToken = localStorage.getItem('accessToken');
    fetch(addUserEndpoint, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
        },
        body: prepareData()
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response;
        })
        .then(data => {
            console.log('user added:', data);
        })
        .catch(error => {
            console.error('Error during user adding:', error);
        });
}


function logout() {
    localStorage.clear();
    window.location.href = "signin.html";
}