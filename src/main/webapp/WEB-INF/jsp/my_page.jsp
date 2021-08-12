<%@ page language="java" contentType="text/html; charset=utf-8"
pageEncoding="utf-8" isELIgnored="false" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta http-equiv="Content-Language" content="en" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Demo Bank</title>
    <meta
      name="viewport"
      content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, shrink-to-fit=no"
    />
    <meta
      name="description"
      content="This is an example dashboard created using build-in elements and components."
    />
    <meta name="msapplication-tap-highlight" content="no" />

    <link href="\css\main.css" rel="stylesheet" />
    <link rel="shortcut icon" type="image⁄x-icon" href="\image\logo-only.png" />
  </head>
  <body>
    <div
      class="
        app-container app-theme-white
        body-tabs-shadow
        fixed-sidebar fixed-header
      "
    >
      <div class="app-header header-shadow">
        <div class="app-header__logo">
          <div class="header__pane ml-auto">
            <div>
              <a href="mypage">
                <img src="\image\logo.png" />
              </a>
            </div>
          </div>
        </div>

        <div class="app-header__content">
          <div class="app-header-left">
            <div class="widget-content p-0">
              <div class="widget-content-wrapper">
                <a href="transferpage"> | transfer </a>
              </div>
              <div class="widget-content-wrapper">
                <a onclick="createAccount()"> | account </a>
<%--                <a href="createaccount"> | account </a>--%>
              </div>
              <div class="widget-content-wrapper">
                <a href="reissuance"> | reissuance </a>
              </div>
            </div>
          </div>
          <div class="app-header-right">
            <div class="header-btn-lg pr-0">
              <div class="widget-content p-0">
                <div class="widget-content-wrapper">
                  <div class="widget-content-left">
                    <img
                      width="42"
                      class="rounded-circle"
                      src="\image\user.png"
                      alt=""
                    />
                  </div>
                  <div class="widget-content-left ml-3 header-user-info">
                    <div class="widget-subheading">
                      <span id="counter"></span>
                      <input
                        type="button"
                        class="btn p-1 show-toastr-example"
                        value="연장"
                        onclick="counter_reset()"
                      />
                    </div>
                  </div>

                  <div class="widget-content-right header-user-info ml-3">
                    <button
                      type="button"
                      class="
                        btn-shadow
                        p-1
                        btn btn-warning btn-sm
                        show-toastr-example
                      "
                    >
                      <a href="logout"> Logout </a>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="app-main">
        <div style="margin: auto; width: 1000px">
          <div class="app-main__inner">
            <div class="app-page-title">
              <div class="page-title-wrapper">
                <div class="page-title-heading">
                  <!--은행 로고-->
                  <div>
                    Welcome to your account page!
                    <div class="page-title-subheading">
                      i'm your private manager.
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-md col-xl">
                <div class="main-card mb-3 card">
                  <div class="card-body">
                    <!-- select_list -->
                    <ul class="nav nav-tabs nav-justified" role="tablist" id='select_list'></ul>
                    <div class="tab-content" id='select_panel'>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <script
      src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
      integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
      crossorigin="anonymous"
    ></script>

    <script
      src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"
      integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4"
      crossorigin="anonymous"
    ></script>

    <script
      src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"
      integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1"
      crossorigin="anonymous"
    ></script>

    <script type="text/javascript" src="js/my_page.js"></script>
  </body>
</html>

<script>
  counter_init();
  var myData = `<%=request.getAttribute("myAccountsData")%>`;
  var accounts = JSON.parse(myData);
  var i;
  var select_list = document.getElementById('select_list');
  var select_panel = document.getElementById('select_panel');
  for (i=0; i<accounts.length; i++)
  {
    // select list creation
    var newList = document.createElement("li");
    newList.setAttribute("class", "nav-item");
    var temp = document.createElement('a');
    temp.setAttribute('data-toggle', 'tab');
    temp.setAttribute('href', '#tab-page1-'+i);
    temp.setAttribute('class', 'nav-link');
    temp.append('계좌별칭 '+i);
    newList.appendChild(temp);
    select_list.appendChild(newList);


    var div1 = document.createElement('div');
    div1.setAttribute('class', 'tab-pane fade active show');
    div1.setAttribute('id', 'tab-page1-'+i);
    div1.setAttribute('role', 'tabpanel');

    // account description
    var div2 = document.createElement('div');
    div2.setAttribute('class', 'card mb-3 widget-content bg-midnight-bloom');
    var div3 = document.createElement('div');
    div3.setAttribute('class', 'widget-content-wrapper text-white');
    var div4 = document.createElement('div');
    div4.setAttribute('class', 'widget-content-left');
    var div5 = document.createElement('div');
    div5.setAttribute('class', 'widget-heading');
    div5.append('계좌별칭 '+i);
    var div6 = document.createElement('div');
    div6.append(accounts[i].accountID);
    div6.setAttribute('class', 'widget-subheading');
    var div7 = document.createElement('div');
    div7.setAttribute('class', 'widget-content-right');
    var div8 = document.createElement('div');
    div8.setAttribute('class', 'widget-numbers text-white');
    var span1 = document.createElement('span');
    span1.append(accounts[i].balance);
    div8.appendChild(span1);
    div7.appendChild(div8);
    div4.appendChild(div5);
    div4.appendChild(div6);
    div3.appendChild(div4);
    div3.appendChild(div7);
    div2.appendChild(div3);
    div1.appendChild(div2);


    // account transfer log description
    var div9 = document.createElement('div');
    div9.setAttribute('class', 'mb-3 card');
    var div10 = document.createElement('div');
    div10.setAttribute('class', 'card-header-tab card-header-tab-animation card-header');
    var div11 = document.createElement('div');
    div11.setAttribute('class', 'card-header-title');
    var i1 = document.createElement('i');
    i1.setAttribute('class', 'header-icon lnr-apartment icon-gradient bg-love-kiss');
    div11.appendChild(i1);
    div11.append('거래내역');
    div10.appendChild(div11);
    div9.appendChild(div10);

    var div12 = document.createElement('div');
    div12.setAttribute('class', 'card-body');
    var h6_1 = document.createElement('h6');
    h6_1.setAttribute('class', 'text-muted text-uppercase font-size-md opacity-5 font-weight-normal');
    h6_1.append('입출금 내역');
    div12.appendChild(h6_1);

    var div13 = document.createElement('div');
    div13.setAttribute('class', 'cscroll-area-sm');
    var div14 = document.createElement('div');
    div14.setAttribute('class', 'scrollbar-container');
    var ul1 = document.createElement('ul');
    ul1.setAttribute('class', 'rm-list-borders rm-list-borders-scroll list-group list-group-flush');
    
    var tempLog = accounts[i].transferLog;
    var j;
    for(j=0; j<tempLog.length; j++)
    {
      console.log(tempLog[j]);
      var TsendTo = tempLog[j].sendTo;
      var Tgold = tempLog[j].gold;
      var Ttime = tempLog[j].time;
      var Tresult = tempLog[j].result;

      var date = new Date(Ttime*1000);

      var year = date.getFullYear();
      var month = date.getMonth() + 1;
      var day = date.getDay() + 1;
      var hour = date.getHours();
      var minute = date.getMinutes();
      var second = date.getSeconds();

      var TtimeStr = year + '/' + month + '/' + day + "  " + hour + ':' + minute + ':' + second;

      var li1 = document.createElement('li');
      li1.setAttribute('class', 'list-group-item');

      var div15 = document.createElement('div');
      div15.setAttribute('class', 'widget-content p-0');
      var div16 = document.createElement('div');
      div16.setAttribute('class', 'widget-content-wrapper');
      var div17 = document.createElement('div');
      div17.setAttribute('class', 'widget-content-left mr-3');
      div17.append('|');
      var div18 = document.createElement('div');
      div18.setAttribute('class', 'widget-content-left');
      var div19 = document.createElement('div');
      div19.setAttribute('class', 'widget-heading');
      div19.append(TsendTo);
      var div20 = document.createElement('div');
      div20.setAttribute('class', 'widget-subheading');
      div20.append(TtimeStr);
      var div21 = document.createElement('div');
      div21.setAttribute('class', 'widget-content-right');
      var div22 = document.createElement('div');
      div22.setAttribute('class', 'font-size-xlg text-muted');
      var span2 = document.createElement('span');
      span2.append(Math.abs(Tgold));
      var small1 = document.createElement('small');
      small1.setAttribute('class', 'opacity-5 pr-1');
      small1.append('원');
      var small2 = document.createElement('small');
      if(Tgold > 0)
      {
        small2.setAttribute('class', 'text-danger pl-2');
        small2.append('출금');
      }
      else
      {
        small2.setAttribute('class', 'text-success pl-2');
        small2.append('입금');
      }
      div22.appendChild(span2);
      div22.appendChild(small1);
      div22.appendChild(small2);
      div21.appendChild(div22);
      div18.appendChild(div19);
      div18.appendChild(div20);
      div16.appendChild(div17);
      div16.appendChild(div18);
      div16.appendChild(div21);
      div15.appendChild(div16);
      li1.appendChild(div15);

      ul1.appendChild(li1);
    }
    div14.appendChild(ul1);
    div13.appendChild(div14);
    div12.appendChild(div13);
    div9.appendChild(div12);
    div1.appendChild(div9);

    select_panel.appendChild(div1);
  }
</script>
