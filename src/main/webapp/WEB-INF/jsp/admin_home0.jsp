<%@ page language="java" contentType="text/html; charset=utf-8"
pageEncoding="utf-8" isELIgnored="false" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <link href="\css\main.css" rel="stylesheet" />
    <title>Super Admin Home Page</title>
  </head>
  <body>
    <h1>Admin List</h1>
    <button id="create_admin", type="button"> 관리자 추가 </button>
    <button id="modify_admin", type="button"> 관리자 정보 수정 </button>
    <button id="remove_admin", type="button"> 관리자 제거 </button>
    <p></p>
    <table border="1", id="adminListTable">
      <th>인덱스</th>
      <th>아이디</th>
      <th>이름</th>
      <th>관리자 전화 번호</th>
      <th>관리자 등급</th>
    </table>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script>
      var adminListInfo = `<%=request.getAttribute("adminListInfo")%>`;
      var adminList = JSON.parse(adminListInfo);
      var adminListTable = document.getElementById('adminListTable');

      for(i=0; i<adminList.length; i++)
      {
        let ith_admin = adminList[i]

        let tr = document.createElement('tr');
        let td_index = document.createElement('td');
        let td_id = document.createElement('td');
        let td_name = document.createElement('td');
        let td_number = document.createElement('td');
        let td_level = document.createElement('td');

        td_index.append(i);
        td_id.append(ith_admin.id);
        td_name.append(ith_admin.name);
        td_number.append(ith_admin.number);
        td_level.append(ith_admin.level);
        
        tr.appendChild(td_index);
        tr.appendChild(td_id);
        tr.appendChild(td_name);
        tr.appendChild(td_number);
        tr.appendChild(td_level);

        adminListTable.appendChild(tr);
      }

      $('#create_admin').click(function() {

          let id = prompt('ID?', '');
          let pw = prompt('PW?', '');
          let name = prompt('name?', '');
          let number = prompt('phone number?', '');
          let adminLevel = prompt('admin level?', '');

          $.ajax({
            type: "POST",
            url: "adminPage/manageAdmin/create",
            data: {
              id: id,
              pw: pw,
              name: name,
              number: number,
              adminLevel: adminLevel
            },
            success: function(result) {
              switch(result) {
                case 0:
                  alert("관리자 생성 성공");
                  break;
                case 1:
                  alert("ID 중복");
                  break;
                default:
                  alert("알수 없는 오류");
                  break;
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
