<%@ page language="java" contentType="text/html; charset=utf-8"
pageEncoding="utf-8" isELIgnored="false"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>My Board</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bulma@0.9.2/css/bulma.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" integrity="sha512-iBBXm8fW90+nuLcSKlbmrPcLa0OT92xO1BIsZ+ywDWZCvqsWgccV3gFoRBv0z+8dLJgyAHIhR35VZc2oM/gI1w==" crossorigin="anonymous" />
</head>

<body>
  <nav class="navbar is-transparent">
    <div class="navbar-brand">
      <a class="navbar-item" id="home_butt" href="hello">
        데모은행
      </a>
      <!-- <script>
        let temp = getElementById("home_butt");
        temp.addEventListener('click', ()=>{
          location.href="connect"
        })
      </script> -->
    </div>

    <div class="navbar-end">
      <div class="navbar-item">
        <div class="buttons">
          <button class="button is-primary" id="show-sign-up">회원 가입</button>
          <button class="button is-primary" id="show-sign-in">로그인</button>
          <button class="button is-primary" id="sign-out">로그 아웃</button>
        </div>
      </div>
    </div>
  </nav>

  <section class="section">
    <div class="container">
      <div class="columns">

        <div class="column is-two-fifth">
          <form id='sign-in-form' class="box">
            
            <div class="field">
              <label class="label">아이디</label>
              <div class="control">
                <input class="input" type="email" placeholder="e.g. alex@example.com">
              </div>
            </div>

            <div class="field">
              <label class="label">비밀번호</label>
              <div class="control">
                <input class="input" type="password" placeholder="********">
              </div>
            </div>

            <button class="button is-primary" id="sign-in">로그인</button>
          </form>

          <form id='sign-up-form' class="box" style=>
            
            <div class="field">
              <label class="label">아이디</label>
              <div class="control">
                <input class="input" type="email" placeholder="e.g. alex@example.com">
              </div>
            </div>

            <div class="field">
              <label class="label">비밀번호</label>
              <div class="control">
                <input class="input" type="password" placeholder="********">
              </div>
            </div>

            <div class="field">
              <label class="label">성</label>
              <div class="control">
                <input class="input" placeholder="Kim">
              </div>
            </div>

            <div class="field">
              <label class="label">이름</label>
              <div class="control">
                <input class="input" placeholder="Doyeol">
              </div>
            </div>

            <div class="field">
              <label class="label">전화번호</label>
              <div class="control">
                <input class="input" pattern='010-[0-9]{4}-[0-9]{4}' placeholder="010-1234-5678" title="Please enter KR tel type : 010-1234-5678">
              </div>
            </div>

            <button class="button is-primary" id="sign-in">회원 가입</button>
          </form>
        </div>

        <div class="column">
          <img class="is-square" src="home_page_img.png" alt="home_page_img">
        </div>

      </div>
    </div>
  </section>
</body>
<script>
  let form_up = document.getElementById('sign-up-form');
  let form_in = document.getElementById('sign-in-form');
  let button1 = document.getElementById('show-sign-up');
  let button2 = document.getElementById('show-sign-in');
  form_up.style.display = 'block';
  form_in.style.display = 'none';

  function navButtonListener() {
    switch (event.target.id) {
      case 'show-sign-up':
      form_up.style.display = 'block';
      form_in.style.display = 'none';
        break;
      case 'show-sign-in':
      form_up.style.display = 'none';
      form_in.style.display = 'block';
    }
  }
  button1.addEventListener('click', navButtonListener);
  button2.addEventListener('click', navButtonListener);
</script>

</html>