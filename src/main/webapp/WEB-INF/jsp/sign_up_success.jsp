<%@ page language="java" contentType="text/html; charset=utf-8"
pageEncoding="utf-8" isELIgnored="false"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core"%>
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
              <a href="mypage" title="Return to home page" target="_self"
                >| My page |</a
              >
            </div>
          </div>
        </div>
      </div>
    </div>

    <script>
      var homeButton = document.getElementById("home_button");
      homeButton.addEventListener("click", init);

      function init(e) {
        e.preventDefault();
        localStorage.setItem(
          "public-key",
          '<%= request.getAttribute("public-key")%>'
        );
        localStorage.setItem(
          "private-key",
          '<%= request.getAttribute("private-key")%>'
        );
        console.log(
          localStorage.getItem("public-key") +
            " " +
            localStorage.getItem("private-key")
        );
        location.href = "https://localhost:8080/home";
      }
    </script>
  </body>
</html>
