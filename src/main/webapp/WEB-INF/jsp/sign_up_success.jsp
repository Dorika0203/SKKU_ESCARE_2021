<%@ page language="java" contentType="text/html; charset=utf-8"
pageEncoding="utf-8" isELIgnored="false"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>

<html>
  <head>
    <link
      href="https://fonts.googleapis.com/css?family=Nunito+Sans:400,400i,700,900&display=swap"
      rel="stylesheet"
    />
    <title></title>
  </head>
  <style>
    body {
      text-align: center;
      padding: 40px 0;
      background: #ebf0f5;
    }
    h1 {
      color: #88b04b;
      font-family: "Nunito Sans", "Helvetica Neue", sans-serif;
      font-weight: 900;
      font-size: 40px;
      margin-bottom: 10px;
    }
    p {
      color: #404f5e;
      font-family: "Nunito Sans", "Helvetica Neue", sans-serif;
      font-size: 20px;
      margin: 0;
    }
    i {
      color: #9abc66;
      font-size: 100px;
      line-height: 200px;
      margin-left: -15px;
    }
    .card {
      background: white;
      padding: 60px;
      border-radius: 4px;
      box-shadow: 0 2px 3px #c8d0d8;
      display: inline-block;
      margin: 0 auto;
    }
  </style>
  <body>
    <div class="card">
      <div
        style="
          border-radius: 200px;
          height: 200px;
          width: 200px;
          background: #f8faf5;
          margin: 0 auto;
        "
      >
        <i class="checkmark">✓</i>
      </div>
      <h1>Sign up success!</h1>
      <p>Now you have a new account for Demo Bank.</p>
      <br />
      <button
        title="Return to home page"
        target="_self"
        id="home_button"
      >Home</button>

    </div>
    <script>
      var homeButton = document.getElementById("home_button");
      homeButton.addEventListener("click", init)

      function init(e) {
        e.preventDefault()
        localStorage.setItem("public-key", '<%= request.getAttribute("public-key")%>')
        localStorage.setItem("private-key", '<%= request.getAttribute("private-key")%>')
        console.log(localStorage.getItem("public-key") + " " + localStorage.getItem("private-key"))
        location.href = "https://localhost:8080/home"
      }
    </script>
  </body>
</html>
