<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Language" content="en">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Demo Bank</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, shrink-to-fit=no" />
    <meta name="description" content="This is an example dashboard created using build-in elements and components.">
    <meta name="msapplication-tap-highlight" content="no">
    <!--
    =========================================================
    * ArchitectUI HTML Theme Dashboard - v1.0.0
    =========================================================
    * Product Page: https://dashboardpack.com
    * Copyright 2019 DashboardPack (https://dashboardpack.com)
    * Licensed under MIT (https://github.com/DashboardPack/architectui-html-theme-free/blob/master/LICENSE)
    =========================================================
    * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
    -->
<link href="SKKU_ESCARE_2021\src\main\resources\static\css\main.css" rel="stylesheet">
<link rel="shortcut icon" type="image⁄x-icon" href="SKKU_ESCARE_2021\src\main\resources\static\image\logo.png">

</head>
<body>
    <div class="app-container app-theme-white body-tabs-shadow fixed-sidebar fixed-header">
        <div class="app-header header-shadow">
            <div class="app-header__logo">
                
                <div class="header__pane ml-auto">
                    <div>
                        <a href="SKKU_ESCARE_2021\src\main\webapp\WEB-INF\jsp\my_page.jsp">
                            <img src="SKKU_ESCARE_2021\src\main\resources\static\image\logo.png">
                        </a>
                        
                    </div>
                </div>
            </div>
               
            <div class="app-header__content">
                <div class="app-header-left">
                    
                    <ul class="header-menu nav">
                        <li class="btn-group nav-item">
                            <a href="SKKU_ESCARE_2021\src\main\webapp\WEB-INF\jsp\transfer_page.jsp">
                                *송금
                            </a>
                            
                        </li>
                        <li class="btn-group nav-item">
                            <a href="javascript:void(0);" class="nav-link">
                                *공인인증서 재발급
                            </a>
                        </li>
                        
                    </ul>        
                </div>
                <div class="app-header-right">
                    <div class="header-btn-lg pr-0">
                        <div class="widget-content p-0">
                            <div class="widget-content-wrapper">
                                <div class="widget-content-left">
                                    <img width="42" class="rounded-circle" src="SKKU_ESCARE_2021\src\main\resources\static\image\logo.png" alt="">
                                </div>
                                <div class="widget-content-left  ml-3 header-user-info">
                                    <div class="widget-heading">
                                        Hyejin Yoo
                                    </div>
                                    <div class="widget-subheading">
                                        hyejin1234
                                    </div>
                                </div>
                                <div class="widget-content-right header-user-info ml-3">
                                    <button type="button" class="btn-shadow p-1 btn btn-warning btn-sm show-toastr-example">
                                        Logout
                                    </button>
                                </div>
                            </div> 
                        </div>
                    </div>        
                </div>
            </div>
        </div>        

        <div class="app-main">   
                <div style="margin: auto; width: 1000px;">
                    <div class="app-main__inner">
                        <div class="app-page-title">
                            <div class="page-title-wrapper">
                                <div class="page-title-heading">
                                    <img width="42" class="rounded-circle" src="SKKU_ESCARE_2021\src\main\resources\static\image\logo.png" alt="">
                                    <div>Demo Bank Header
                                        <div class="page-title-subheading">
                                            discription
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>            
                        <div class="row">
                            <div class="col-md col-xl">
                                <div class="main-card mb-3 card">
                                    <div class="card-body"><h5 class="card-title">Tab Header</h5>
                                        <ul class="nav nav-tabs nav-justified" role="tablist">
                                            <li class="nav-item"><a data-toggle="tab" href="#tab-page1-0" class="active nav-link">Tab 1</a></li>
                                            <li class="nav-item"><a data-toggle="tab" href="#tab-page1-1" class="nav-link">Tab 2</a></li>
                                            <li class="nav-item"><a data-toggle="tab" href="#tab-page1-2" class="nav-link">Tab 3</a></li>
                                        </ul>
                                        <div class="tab-content">
                                            <div class="tab-pane active" id="tab-page1-0" role="tabpanel">
                                                
                                                <div class="card mb-3 widget-content bg-midnight-bloom">
                                                    <div class="widget-content-wrapper text-white">
                                                        <div class="widget-content-left">
                                                            <div class="widget-heading">계좌별칭</div>
                                                            <div class="widget-subheading">123-4567-8987-65</div>
                                                        </div>
                                                        <div class="widget-content-right">
                                                            <div class="widget-numbers text-white"><span>999,999 원</span></div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="mb-3 card">
                                                    <div class="card-header-tab card-header-tab-animation card-header">
                                                        <div class="card-header-title">
                                                            <i class="header-icon lnr-apartment icon-gradient bg-love-kiss"> </i>
                                                            거래 내역
                                                        </div>
                                                    </div>
                                                    <div class="card-body">
                                                        <div class="tab-content">
                                                            <div class="tab-pane fade show active" id="tabs-eg-77">   
                                                                <h6 class="text-muted text-uppercase font-size-md opacity-5 font-weight-normal">입출금 내역</h6>
                                                                <div class="scroll-area-sm">
                                                                    <div class="scrollbar-container">
                                                                        <ul class="rm-list-borders rm-list-borders-scroll list-group list-group-flush">
                                                                            <li class="list-group-item">
                                                                                <div class="widget-content p-0">
                                                                                    <div class="widget-content-wrapper">
                                                                                        <div class="widget-content-left mr-3">
                                                                                            |
                                                                                        </div>
                                                                                        <div class="widget-content-left">
                                                                                            <div class="widget-heading">N_코레일유통</div>
                                                                                            <div class="widget-subheading">2021.06.22 17:59:37</div>
                                                                                        </div>
                                                                                        <div class="widget-content-right">
                                                                                            <div class="font-size-xlg text-muted">
                                                                                                <span>8,000</span>
                                                                                                <small class="opacity-5 pr-1">원</small>
                                                                                                <small class="text-danger pl-2">
                                                                                                    출금
                                                                                                </small>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </li>
                                                                            <li class="list-group-item">
                                                                                <div class="widget-content p-0">
                                                                                    <div class="widget-content-wrapper">
                                                                                        <div class="widget-content-left mr-3">
                                                                                            |
                                                                                        </div>
                                                                                        <div class="widget-content-left">
                                                                                            <div class="widget-heading">홍길동</div>
                                                                                            <div class="widget-subheading">2021.06.22 10:42:46</div>
                                                                                        </div>
                                                                                        <div class="widget-content-right">
                                                                                            <div class="font-size-xlg text-muted">
                                                                                                <span>15,900</span>
                                                                                                <small class="opacity-5 pr-1">원</small>
                                                                                                <small class="text-success pl-2">
                                                                                                    입금
                                                                                                </small>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </li>
                                                                            <li class="list-group-item">
                                                                                <div class="widget-content p-0">
                                                                                    <div class="widget-content-wrapper">
                                                                                        <div class="widget-content-left mr-3">
                                                                                            |
                                                                                        </div>
                                                                                        <div class="widget-content-left">
                                                                                            <div class="widget-heading">N_세븐일레븐</div>
                                                                                            <div class="widget-subheading">2021.06.20 23:02:11</div>
                                                                                        </div>
                                                                                        <div class="widget-content-right">
                                                                                            <div class="font-size-xlg text-muted">
                                                                                                <span>11,400</span>
                                                                                                <small class="opacity-5 pr-1">원</small>
                                                                                                <small class="text-danger pl-2">
                                                                                                    출금
                                                                                                </small>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </li>
                                                                            <li class="list-group-item">
                                                                                <div class="widget-content p-0">
                                                                                    <div class="widget-content-wrapper">
                                                                                        <div class="widget-content-left mr-3">
                                                                                            |
                                                                                        </div>
                                                                                        <div class="widget-content-left">
                                                                                            <div class="widget-heading">Apple</div>
                                                                                            <div class="widget-subheading">2021.06.19 20:27:16</div>
                                                                                        </div>
                                                                                        <div class="widget-content-right">
                                                                                            <div class="font-size-xlg text-muted">
                                                                                                <span>2,500</span>
                                                                                                <small class="opacity-5 pr-1">원</small>
                                                                                                <small class="text-danger pl-2">
                                                                                                    출금
                                                                                                </small>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </li>
                                                                            <li class="list-group-item">
                                                                                <div class="widget-content p-0">
                                                                                    <div class="widget-content-wrapper">
                                                                                        <div class="widget-content-left mr-3">
                                                                                            |
                                                                                        </div>
                                                                                        <div class="widget-content-left">
                                                                                            <div class="widget-heading">이호창</div>
                                                                                            <div class="widget-subheading">2021.05.22 17:59:37</div>
                                                                                        </div>
                                                                                        <div class="widget-content-right">
                                                                                            <div class="font-size-xlg text-muted">
                                                                                                <span>251,100</span>
                                                                                                <small class="opacity-5 pr-1">원</small>
                                                                                                <small class="text-success pl-2">
                                                                                                    입금
                                                                                                </small>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </li>
                                                                        </ul>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="tab-pane" id="tab-page1-1" role="tabpanel">
                                                <div class="card mb-3 widget-content bg-midnight-bloom">
                                                    <div class="widget-content-wrapper text-white">
                                                        <div class="widget-content-left">
                                                            <div class="widget-heading">계좌별칭2</div>
                                                            <div class="widget-subheading">123-4567-8987-65</div>
                                                        </div>
                                                        <div class="widget-content-right">
                                                            <div class="widget-numbers text-white"><span>888,888 원</span></div>
                                                        </div>
                                                    </div>
                                                </div>
                                                
                                                <div class="mb-3 card">
                                                    <div class="card-header-tab card-header-tab-animation card-header">
                                                        <div class="card-header-title">
                                                            <i class="header-icon lnr-apartment icon-gradient bg-love-kiss"> </i>
                                                            거래 내역
                                                        </div>
                                                    </div>
                                                    <div class="card-body">
                                                        <div class="tab-content">
                                                            <div class="tab-pane fade show active" id="tabs-eg-77">   
                                                                <h6 class="text-muted text-uppercase font-size-md opacity-5 font-weight-normal">입출금 내역</h6>
                                                                <div class="scroll-area-sm">
                                                                    <div class="scrollbar-container">
                                                                        <ul class="rm-list-borders rm-list-borders-scroll list-group list-group-flush">
                                                                            <li class="list-group-item">
                                                                                <div class="widget-content p-0">
                                                                                    <div class="widget-content-wrapper">
                                                                                        <div class="widget-content-left mr-3">
                                                                                            |
                                                                                        </div>
                                                                                        <div class="widget-content-left">
                                                                                            <div class="widget-heading">N_코레일유통</div>
                                                                                            <div class="widget-subheading">2021.06.22 17:59:37</div>
                                                                                        </div>
                                                                                        <div class="widget-content-right">
                                                                                            <div class="font-size-xlg text-muted">
                                                                                                <span>8,000</span>
                                                                                                <small class="opacity-5 pr-1">원</small>
                                                                                                <small class="text-danger pl-2">
                                                                                                    출금
                                                                                                </small>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </li>
                                                                            <li class="list-group-item">
                                                                                <div class="widget-content p-0">
                                                                                    <div class="widget-content-wrapper">
                                                                                        <div class="widget-content-left mr-3">
                                                                                            |
                                                                                        </div>
                                                                                        <div class="widget-content-left">
                                                                                            <div class="widget-heading">홍길동</div>
                                                                                            <div class="widget-subheading">2021.06.22 10:42:46</div>
                                                                                        </div>
                                                                                        <div class="widget-content-right">
                                                                                            <div class="font-size-xlg text-muted">
                                                                                                <span>15,900</span>
                                                                                                <small class="opacity-5 pr-1">원</small>
                                                                                                <small class="text-success pl-2">
                                                                                                    입금
                                                                                                </small>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </li>
                                                                            <li class="list-group-item">
                                                                                <div class="widget-content p-0">
                                                                                    <div class="widget-content-wrapper">
                                                                                        <div class="widget-content-left mr-3">
                                                                                            |
                                                                                        </div>
                                                                                        <div class="widget-content-left">
                                                                                            <div class="widget-heading">N_세븐일레븐</div>
                                                                                            <div class="widget-subheading">2021.06.20 23:02:11</div>
                                                                                        </div>
                                                                                        <div class="widget-content-right">
                                                                                            <div class="font-size-xlg text-muted">
                                                                                                <span>11,400</span>
                                                                                                <small class="opacity-5 pr-1">원</small>
                                                                                                <small class="text-danger pl-2">
                                                                                                    출금
                                                                                                </small>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </li>
                                                                            <li class="list-group-item">
                                                                                <div class="widget-content p-0">
                                                                                    <div class="widget-content-wrapper">
                                                                                        <div class="widget-content-left mr-3">
                                                                                            |
                                                                                        </div>
                                                                                        <div class="widget-content-left">
                                                                                            <div class="widget-heading">Apple</div>
                                                                                            <div class="widget-subheading">2021.06.19 20:27:16</div>
                                                                                        </div>
                                                                                        <div class="widget-content-right">
                                                                                            <div class="font-size-xlg text-muted">
                                                                                                <span>2,500</span>
                                                                                                <small class="opacity-5 pr-1">원</small>
                                                                                                <small class="text-danger pl-2">
                                                                                                    출금
                                                                                                </small>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </li>
                                                                            <li class="list-group-item">
                                                                                <div class="widget-content p-0">
                                                                                    <div class="widget-content-wrapper">
                                                                                        <div class="widget-content-left mr-3">
                                                                                            |
                                                                                        </div>
                                                                                        <div class="widget-content-left">
                                                                                            <div class="widget-heading">이호창</div>
                                                                                            <div class="widget-subheading">2021.05.22 17:59:37</div>
                                                                                        </div>
                                                                                        <div class="widget-content-right">
                                                                                            <div class="font-size-xlg text-muted">
                                                                                                <span>251,100</span>
                                                                                                <small class="opacity-5 pr-1">원</small>
                                                                                                <small class="text-success pl-2">
                                                                                                    입금
                                                                                                </small>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </li>
                                                                        </ul>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="tab-pane" id="tab-page1-2" role="tabpanel">
                                                <div class="card mb-3 widget-content bg-midnight-bloom">
                                                    <div class="widget-content-wrapper text-white">
                                                        <div class="widget-content-left">
                                                            <div class="widget-heading">계좌별칭3</div>
                                                            <div class="widget-subheading">123-4567-8987-65</div>
                                                        </div>
                                                        <div class="widget-content-right">
                                                            <div class="widget-numbers text-white"><span>777,777 원</span></div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="mb-3 card">
                                                    <div class="card-header-tab card-header-tab-animation card-header">
                                                        <div class="card-header-title">
                                                            <i class="header-icon lnr-apartment icon-gradient bg-love-kiss"> </i>
                                                            거래 내역
                                                        </div>
                                                    </div>
                                                    <div class="card-body">
                                                        <div class="tab-content">
                                                            <div class="tab-pane fade show active" id="tabs-eg-77">   
                                                                <h6 class="text-muted text-uppercase font-size-md opacity-5 font-weight-normal">입출금 내역</h6>
                                                                <div class="scroll-area-sm">
                                                                    <div class="scrollbar-container">
                                                                        <ul class="rm-list-borders rm-list-borders-scroll list-group list-group-flush">
                                                                            <li class="list-group-item">
                                                                                <div class="widget-content p-0">
                                                                                    <div class="widget-content-wrapper">
                                                                                        <div class="widget-content-left mr-3">
                                                                                            |
                                                                                        </div>
                                                                                        <div class="widget-content-left">
                                                                                            <div class="widget-heading">N_코레일유통</div>
                                                                                            <div class="widget-subheading">2021.06.22 17:59:37</div>
                                                                                        </div>
                                                                                        <div class="widget-content-right">
                                                                                            <div class="font-size-xlg text-muted">
                                                                                                <span>8,000</span>
                                                                                                <small class="opacity-5 pr-1">원</small>
                                                                                                <small class="text-danger pl-2">
                                                                                                    출금
                                                                                                </small>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </li>
                                                                            <li class="list-group-item">
                                                                                <div class="widget-content p-0">
                                                                                    <div class="widget-content-wrapper">
                                                                                        <div class="widget-content-left mr-3">
                                                                                            |
                                                                                        </div>
                                                                                        <div class="widget-content-left">
                                                                                            <div class="widget-heading">홍길동</div>
                                                                                            <div class="widget-subheading">2021.06.22 10:42:46</div>
                                                                                        </div>
                                                                                        <div class="widget-content-right">
                                                                                            <div class="font-size-xlg text-muted">
                                                                                                <span>15,900</span>
                                                                                                <small class="opacity-5 pr-1">원</small>
                                                                                                <small class="text-success pl-2">
                                                                                                    입금
                                                                                                </small>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </li>
                                                                            <li class="list-group-item">
                                                                                <div class="widget-content p-0">
                                                                                    <div class="widget-content-wrapper">
                                                                                        <div class="widget-content-left mr-3">
                                                                                            |
                                                                                        </div>
                                                                                        <div class="widget-content-left">
                                                                                            <div class="widget-heading">N_세븐일레븐</div>
                                                                                            <div class="widget-subheading">2021.06.20 23:02:11</div>
                                                                                        </div>
                                                                                        <div class="widget-content-right">
                                                                                            <div class="font-size-xlg text-muted">
                                                                                                <span>11,400</span>
                                                                                                <small class="opacity-5 pr-1">원</small>
                                                                                                <small class="text-danger pl-2">
                                                                                                    출금
                                                                                                </small>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </li>
                                                                            <li class="list-group-item">
                                                                                <div class="widget-content p-0">
                                                                                    <div class="widget-content-wrapper">
                                                                                        <div class="widget-content-left mr-3">
                                                                                            |
                                                                                        </div>
                                                                                        <div class="widget-content-left">
                                                                                            <div class="widget-heading">Apple</div>
                                                                                            <div class="widget-subheading">2021.06.19 20:27:16</div>
                                                                                        </div>
                                                                                        <div class="widget-content-right">
                                                                                            <div class="font-size-xlg text-muted">
                                                                                                <span>2,500</span>
                                                                                                <small class="opacity-5 pr-1">원</small>
                                                                                                <small class="text-danger pl-2">
                                                                                                    출금
                                                                                                </small>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </li>
                                                                            <li class="list-group-item">
                                                                                <div class="widget-content p-0">
                                                                                    <div class="widget-content-wrapper">
                                                                                        <div class="widget-content-left mr-3">
                                                                                            |
                                                                                        </div>
                                                                                        <div class="widget-content-left">
                                                                                            <div class="widget-heading">이호창</div>
                                                                                            <div class="widget-subheading">2021.05.22 17:59:37</div>
                                                                                        </div>
                                                                                        <div class="widget-content-right">
                                                                                            <div class="font-size-xlg text-muted">
                                                                                                <span>251,100</span>
                                                                                                <small class="opacity-5 pr-1">원</small>
                                                                                                <small class="text-success pl-2">
                                                                                                    입금
                                                                                                </small>
                                                                                            </div>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </li>
                                                                        </ul>
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
                        
                    </div>    
                </div>
               
        </div>
    </div>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
  
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js" integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4" crossorigin="anonymous"></script>
  
    
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js" integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1" crossorigin="anonymous"></script>

</body>
</html>