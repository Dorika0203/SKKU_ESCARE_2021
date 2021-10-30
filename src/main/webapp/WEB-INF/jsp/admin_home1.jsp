<%@ page language="java" contentType="text/html; charset=utf-8"
pageEncoding="utf-8" isELIgnored="false" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <link href="\css\main.css" rel="stylesheet" />
    <title>Super user Home Page</title>
  </head>
  <body>
    <h1>User List</h1>
    <button id="modify_user", type="button"> 사용자 정보 수정 </button>
    <button id="remove_user", type="button"> 사용자 제거 </button>
    <p></p>
    <table border="1", id="userListTable">
      <th>인덱스</th>
      <th>아이디</th>
      <th>성씨</th>
      <th>이름</th>
      <th>사용자 전화 번호</th>
      <th>사용자 회원 가입 시기</th>
    </table>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script>
      var userListInfo = `<%=request.getAttribute("userListInfo")%>`;
      var userList = JSON.parse(userListInfo);
      var userListTable = document.getElementById('userListTable');

      for(i=0; i<userList.length; i++)
      {
        let ith_user = userList[i]

        let tr = document.createElement('tr');
        let td_index = document.createElement('td');
        let td_id = document.createElement('td');
        let td_firstName = document.createElement('td');
        let td_lastName = document.createElement('td');
        let td_phoneNumber = document.createElement('td');
        let td_issuedTime = document.createElement('td');

        td_index.append(i);
        td_id.append(ith_user.id);
        td_firstName.append(ith_user.firstName);
        td_lastName.append(ith_user.lastName);
        td_phoneNumber.append(ith_user.phoneNumber);
        td_issuedTime.append(ith_user.issuedTime);
        
        tr.appendChild(td_index);
        tr.appendChild(td_id);
        tr.appendChild(td_firstName);
        tr.appendChild(td_lastName);
        tr.appendChild(td_phoneNumber);
        tr.appendChild(td_issuedTime);

        userListTable.appendChild(tr);
      }

        // 사용자 제거
        $('#remove_user').click(function() {
          // let id = prompt('ID?', '');
          // $.ajax({
          //   type: "POST",
          //   url: "manageuser/remove",
          //   data: {
          //     id: id,
          //   },
          //   success: function(result) {
          //     switch(result) {
          //       case 0:
          //         alert("관리자 제거 성공");
          //         break;
          //       case 1:
          //         alert("ID 존재하지 않음");
          //         break;
          //       case 2:
          //         alert("권한 없음 혹은 세션 없음");
          //         break;
          //       default:
          //         alert("알 수 없는 오류");
          //         break;
          //     }
          //     location.reload();
          //   },
          //   error:function(request,status,error) {
          //     alert("서버 오류");
          //     location.reload();
          //   }
          // });
        })
    </script>
  </body>
</html>
