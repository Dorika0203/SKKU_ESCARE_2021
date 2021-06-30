<%@ page language="java" contentType="text/html; charset=utf-8"
pageEncoding="utf-8" isELIgnored="false" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>My Board</title>
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bulma@0.9.2/css/bulma.min.css"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"
      integrity="sha512-iBBXm8fW90+nuLcSKlbmrPcLa0OT92xO1BIsZ+ywDWZCvqsWgccV3gFoRBv0z+8dLJgyAHIhR35VZc2oM/gI1w=="
      crossorigin="anonymous"
    />
  </head>

  <body>
    <nav class="navbar is-transparent">
      <div class="navbar-end">
        <div class="navbar-item">
          <div class="buttons">
            <button class="button is-primary" id="show-sign-up">
              회원 가입
            </button>
            <button class="button is-primary" id="show-sign-in">로그인</button>
          </div>
        </div>
      </div>
    </nav>

    <section class="section">
      <div class="container">
        <div class="columns">
          <div class="column is-two-fifth">
            <form id="sign-in-form" class="box" action="signin" method="post">
              <div class="field">
                <label class="label">E-MAIL</label>
                <div class="control">
                  <input
                    class="input"
                    type="email"
                    placeholder="e.g. alex@example.com"
                    name="ID_IN"
                  />
                </div>
              </div>

              <div class="field">
                <label class="label">PASSWORD</label>
                <div class="control">
                  <input
                    class="input"
                    type="password"
                    placeholder="********"
                    name="PW_IN"
                  />
                </div>
              </div>

              <button class="button is-primary" id="login">Login</button>
            </form>

            <form id="sign-up-form" class="box" action="signup" method="post">
              <div class="field">
                <label class="label">E-MAIL</label>
                <div class="control">
                  <input
                    class="input"
                    type="email"
                    placeholder="e.g. alex@example.com"
                    name="ID"
                  />
                </div>
              </div>

              <div class="field">
                <label class="label">PASSWORD</label>
                <div class="control">
                  <input
                    class="input"
                    type="password"
                    placeholder="********"
                    name="PW"
                  />
                </div>
              </div>

              <div class="field">
                <label class="label">NAME</label>
                <div class="control">
                  <input class="input" placeholder="Doyeol" name="firstName" />
                </div>

                <div class="control">
                  <input class="input" placeholder="Kim" name="lastName" />
                </div>
              </div>

              <div class="field">
                <label class="label">PHONE NUMBER</label>
                <div class="control">
                  <input
                    class="input"
                    pattern="010-[0-9]{4}-[0-9]{4}"
                    placeholder="010-1234-5678"
                    title="Please enter KR tel type : 010-1234-5678"
                    name="phoneNumber"
                  />
                </div>
              </div>

              <button class="button is-primary" id="sign-in">Join</button>
            </form>
          </div>

          <div class="column">
            <img
              class="is-square"
              src="image/home_page_logo.gif"
              alt="home_page_img"
            />
          </div>
        </div>
      </div>
    </section>
  </body>
  <script type="text/javascript" src="js/home_page.js"></script>
</html>
