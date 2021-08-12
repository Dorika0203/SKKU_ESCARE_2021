<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta http-equiv="Content-Language" content="en"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Demo Bank</title>
    <meta
            name="viewport"
            content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, shrink-to-fit=no"
    />
    <meta
            name="description"
            content="This is an example dashboard created using build-in elements and components."
    />
    <meta name="msapplication-tap-highlight" content="no"/>

    <link href="\css\main.css" rel="stylesheet"/>
    <link rel="shortcut icon" type="image⁄x-icon" href="\image\logo-only.png"/>
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
                        <img src="\image\logo.png"/>
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
                                    <span id="counter1"></span>
                                    <input
                                            type="button"
                                            class="btn p-1 show-toastr-example"
                                            value="🔄"
                                            onclick="counter_reset()"
                                    />
                                </div>
                            </div>

                            <div class="widget-content-right header-user-info ml-3">
                                <span id="counter"> </span> 후 자동로그아웃
                                <input
                                        type="button"
                                        value="연장"
                                        onclick="counter_reset()"
                                />
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
                                Welcome to your transfer page!
                                <div class="page-title-subheading">
                                    You can only transfer to demo bank account.
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md col-xl">
                        <div class="main-card mb-3 card">
                            <div class="card-body">
                                <ul class="nav nav-tabs nav-justified" role="tablist" id='select_list'></ul>
                                <div class="tab-content" id='select_panel'></div>
                                <div class="mb-3 card">
                                    <div
                                            class="
                      card-header-tab
                      card-header-tab-animation
                      card-header
                    "
                                    >
                                        <div class="card-header-title">| 송금</div>
                                    </div>
                                    <div class="card-body">
                                        <div class="md-form form-group w-50 mb-3">
                                            <input
                                                    type="password"
                                                    class="form-control"
                                                    aria-label="Text input with dropdown button"
                                                    placeholder="비밀번호"
                                                    id="password"
                                            />
                                        </div>
                                        <div class="input-group">
                                            <%--                                            <div class="input-group mb-3">--%>
                                            <%--                                                <input--%>
                                            <%--                                                        type="text"--%>
                                            <%--                                                        class="form-control"--%>
                                            <%--                                                        aria-label="Text input with dropdown button"--%>
                                            <%--                                                        placeholder="보내는 계좌번호 입력"--%>
                                            <%--                                                        id="remitter-account"--%>
                                            <%--                                                />--%>
                                            <%--                                            </div>--%>
                                            <div class="input-group-mb3">
                                                <select
                                                        class="form-control"
                                                        id="remitter-account">
                                                </select>
                                            </div>
                                            <div class="input-group mb-3">
                                                <input
                                                        type="text"
                                                        class="form-control"
                                                        aria-label="Text input with dropdown button"
                                                        placeholder="받는 계좌번호 입력"
                                                        id="receiver-account"
                                                />
                                            </div>
                                            <div class="input-group mb-3">
                                                <input
                                                        type="text"
                                                        class="form-control"
                                                        aria-label="Text input with dropdown button"
                                                        placeholder="금액"
                                                        id="transfer-amount"
                                                />
                                                <button
                                                        class="btn btn-outline-secondary"
                                                        type="submit"
                                                        id="send"
                                                >
                                                    송금
                                                </button>
                                            </div>
                                        </div>
                                    </div>
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
<script src="node-forge/dist/forge.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
    let accountJSONData = `<%=request.getAttribute("myAccountsData")%>`;
    let accounts = JSON.parse(accountJSONData);
    var i;
    var select_list = document.getElementById('select_list');
    var select_panel = document.getElementById('select_panel');
    var remitter_account = document.getElementById('remitter-account');
    for (i = 0; i < accounts.length; i++) {
        // select list creation
        var newList = document.createElement("li");
        newList.setAttribute("class", "nav-item");
        var temp = document.createElement('a');
        temp.setAttribute('data-toggle', 'tab');
        temp.setAttribute('href', '#tab-page1-' + i);
        temp.setAttribute('class', 'nav-link');
        temp.append('계좌별칭 ' + i);
        newList.appendChild(temp);
        select_list.appendChild(newList);


        var div1 = document.createElement('div');
        div1.setAttribute('class', 'tab-pane fade');
        div1.setAttribute('id', 'tab-page1-' + i);
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
        div5.append('계좌별칭 ' + i);
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
        select_panel.appendChild(div1);

        var option = document.createElement("option");
        option.setAttribute("value", accounts[i].accountID);
        option.append("계좌별칭 " + i + " | " + "계좌번호: " + accounts[i].accountID);
        remitter_account.appendChild(option);
    }
</script>
<script>
    //PKI element
    let pki = forge.pki
    let keyStorage = JSON.parse(localStorage.getItem('<%= request.getAttribute("loginClientID") %>'))
    let publicKey = Object.values(keyStorage)[0];
    let pbeEncryptedPrivateKey = Object.values(keyStorage)[1]
    let base64Salt = Object.values(keyStorage)[2]
    let salt = window.atob(base64Salt)
    $(function () {
        $('#send').click(function () {
            let password = $("#password").val();
            let pbeKey = forge.pkcs5.pbkdf2(password, salt, 20, 16);
            let privateKey = pki.decryptRsaPrivateKey(pbeEncryptedPrivateKey, pbeKey)
            let remitterAccount = $("#remitter-account").val()
            let recieverAccount = $("#receiver-account").val()
            let transferAmount = $("#transfer-amount").val()
            let timestampSecond = Math.floor(+new Date() / 1000);
            let transferData = recieverAccount + " " + remitterAccount + " " + transferAmount + " " + timestampSecond
            //signature
            let md = forge.md.sha1.create();
            md.update(transferData, 'utf8');
            let signature = privateKey.sign(md);
            let base64Signature = window.btoa(signature)
            $.ajax({
                type: "POST",
                url: "transferpage/transfer",
                data: {
                    transferData: transferData,
                    signature: base64Signature,
                    publicKey: publicKey
                }, // parameters
                success: function (result) {
                    switch (result) {
                        case 0:
                            alert("error")
                            break
                        case 1:
                            alert("account or transfer amount format is incorrect")
                            break
                        case 2:
                            alert("account doesn't exists!")
                            break
                        case 3:
                            alert("your balance is less than transfer amount")
                            break
                        case 4:
                            alert("success!")
                            location.replace("mypage")
                    }
                },
                error: function (result) {
                    alert("transfer failed")
                }
            });
        })
    });
</script>
</body>
</html>

<script>
    counter_init();
</script>
