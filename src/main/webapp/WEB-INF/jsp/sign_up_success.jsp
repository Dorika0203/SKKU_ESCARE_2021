<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<html>
<head>
    <link
            href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css"
            rel="stylesheet"
            id="bootstrap-css"
    />
    <script src="//netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
    <title>sign up success</title>
</head>

<body>
<div
        class="container"
        style="width: auto; max-width: 680px; padding: 0 15px; margin: auto"
>
    <div class="row">
        <div class="col-md-12">
            <div class="error-template">
                <div class="text-center">
                    <img
                            class="is-square"
                            src="image/check-animation.gif"
                            width="400"
                            alt="sign_in_success_img"
                    />
                    <h1>Sign up Success!</h1>
                    <p>Now you have a new account for Demo Bank.</p>
                    <a href="home" title="Return to home page" target="_self" id="home_button"
                    >| Home |</a
                    >
                </div>
            </div>
        </div>
    </div>
</div>
<script src="node-forge/dist/forge.min.js"></script>
<script>
    let javaPrivateKey = `-----BEGIN PRIVATE KEY-----` + `<%=request.getAttribute("privateKey")%>` + '-----END PRIVATE KEY-----'
    let publicKey = '-----BEGIN PUBLIC KEY-----' + `<%=request.getAttribute("publicKey")%>` + '-----END PUBLIC KEY-----'
    let pki = forge.pki
    let privateKey = pki.privateKeyFromPem(javaPrivateKey);
    let salt = forge.random.getBytesSync(128);
    let base64Salt = window.btoa(salt);
    let derivedKey = forge.pkcs5.pbkdf2(`<%=request.getAttribute("password")%>`, salt, 20, 16);
    let pbePrivateKey = pki.encryptRsaPrivateKey(privateKey, derivedKey);
    let homeButton = document.getElementById("home_button");
    homeButton.addEventListener("click", init)
    let user = JSON.stringify({
            public_key: publicKey,
            private_key: pbePrivateKey,
            salt: base64Salt
        }
    )
    console.log(`<%=request.getAttribute("password")%>`)
    console.log('original private key: ' + privateKey)
    console.log(JSON.stringify(privateKey))
    console.log('pbe key: ' + window.btoa(derivedKey))
    console.log('pbeEncryptedPrivateKey: ' + pem)

    function init(e) {
        e.preventDefault()
        localStorage.setItem(`<%=request.getAttribute("ID")%>`, user)
        console.log(localStorage.getItem(`<%=request.getAttribute("ID")%>`)[0] + " " + localStorage.getItem(`<%=request.getAttribute("ID")%>`)[1])
        location.href = "home"
    }
</script>
</body>
</html>
