let formUp = document.getElementById('sign-up-form');
let formIn = document.getElementById('sign-in-form');
let signUpButton = document.getElementById('show-sign-up');
let signInButton = document.getElementById('show-sign-in');
let connectButton = document.getElementById('connect');
formUp.style.display = 'block';
formIn.style.display = 'none';

function navButtonListener() {
    switch (event.target.id) {
        case 'show-sign-up':
            formUp.style.display = 'block';
            formIn.style.display = 'none';
            break;
        case 'show-sign-in':
            formUp.style.display = 'none';
            formIn.style.display = 'block';
    }
}

function connectWithFortanixKMS() {
    location.href = "connect"
}

signUpButton.addEventListener('click', navButtonListener);
signInButton.addEventListener('click', navButtonListener);