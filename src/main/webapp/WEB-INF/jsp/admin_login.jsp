<%@ page language="java" contentType="text/html; charset=utf-8"
pageEncoding="utf-8" isELIgnored="false" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <link href="\css\main.css" rel="stylesheet" />
    <title>Admin Login</title>
  </head>
  <body>
    <h1>LOGIN INFO PLZ</h1>
      <h5>ID</h5><input type="text" id="id" size="20"><br>
      <h5>PW</h5><input type="password" id="pw" size="20">
      <button id="loginButton" type="button">LOGIN</button>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script>
      $('#loginButton').click(function() {
          let id = $("#id").val();
          let pw = $("#pw").val();
          $.ajax({
            type: "POST",
            url: "adminPage",
            data: {
              id: id,
              pw: pw
            },
            success: function(result) {
              switch(result) {
                case 0:
                  alert("로그인 성공 !");
                  break;
                case 1:
                  alert("아이디가 존재하지 않습니다. 수퍼 관리자에게 문의하세요. ");
                  break;
                case 2:
                  alert("비밀 번호가 틀렸습니다.");
                  break;
                default:
                  alert("something wrong...");
              }
              location.reload();
            },
            error:function(request,status,error) {
              alert("HTTP ERROR...");
              alert("code = "+ request.status + " message = " + request.responseText + " error = " + error);
            }
          });
        })
    </script>
  </body>
</html>
