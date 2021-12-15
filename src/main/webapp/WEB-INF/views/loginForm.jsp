<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">
        <title>OAuth2</title>
        <link href="/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
        <link
            href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
            rel="stylesheet">
        <!-- Custom styles for this template-->
        <link href="/css/sb-admin-2.min.css" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.4.1.js"></script>
        <script>
            //로그인 버튼 클릭
            function swLogin() {
                if (login.id.value == "") {
                    alert("아이디를 입력하세요");
                    login.id.focus();
                    return;
                }

                if (login.pwd.value == "") {
                    alert("패스워드를 입력하세요");
                    login.pwd.focus();
                    return;
                }

                var formData = new FormData();
                formData.append("username", $("#id").val());  //text 타입 선택
                formData.append("password", $("#pwd").val());
                formData.append("grant_type", "password");
                formData.append("scope", "read");

                $.ajax({
                    type: 'POST',
                    url: 'http://localhost:8099/auth/oauth/token',
                    dataType: 'json',
                    crossDomain: true,
                    contentType: false,
                    processData: false,
                    async: false,
                    data: formData,
                    mode: 'no-cors',
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader("Authorization", "Basic " + btoa("oauth2-jwt-client" + ":" + "pass"));
                    },
                    success: function (data) {
                        //console.log(data);
                        $.ajax({
                            url: 'http://localhost:8088/user/profile',
                            type: 'GET',
                            contentType: false,
                            processData: false,
                            async: false,
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", data.token_type + "" + data.access_token);
                                xhr.setRequestHeader("scope", data.scope);
                            },
                            success: function (data) {
                                if (data != null) {
                                    //console.log(data);
                                    alert(data);
                                    return;
                                } else {
                                    alert("로그인 오류입니다.");
                                }
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                alert("잘못된 토큰입니다.");
                            }
                        });

                        $.ajax({
                            type: 'POST',
                            url: 'http://localhost:8077/login',
                            contentType: false,
                            processData: false,
                            async: false,
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", data.token_type + "" + data.access_token);
                                xhr.setRequestHeader("refreshToken", data.refresh_token);
                                xhr.setRequestHeader("scope", data.scope);
                            },
                            success: function (data) {
                                if (data != null) {
                                    window.location = "/main";
                                    /*
                                    var req = new XMLHttpRequest();
                                    req.open('GET', "/main", false);
                                    req.send(null);
                                    var headers = req.getAllResponseHeaders().toLowerCase();
                                    alert(headers);
                                    */
                                    return;
                                } else {
                                    alert("로그인 오류입니다.");
                                }
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                alert("잘못된 토큰입니다.");
                            }
                        });
                        /*
                        if (data.code == "9999") {
                            alert(data.message);
                            return;
                        } else {
                            // alert("로그인 되었습니다.");
                            window.location = "/main";
                        }*/
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        alert("아이디와 비밀번호를 확인해주세요.");
                        //console.log(jqXHR + "\n" + textStatus + "\n" + errorThrown);
                    }
                });
            }

            //아이디  ebter key 처리
            function enterkeyId() {
                if (window.event.keyCode == 13) {
                    if (login.id.value == "") {
                        alert("아이디를 입력하세요");
                        login.id.focus();
                        return;
                    } else {
                        login.pwd.focus();
                    }
                }
            }

            // 패스워드 ebter key 처리
            function enterkeyPwd() {
                if (window.event.keyCode == 13) {
                    swLogin();
                }
            }
        </script>
    </head>

    <body class="bg-gradient-primary">
        <div class="container">
            <!-- Outer Row -->
            <div class="row justify-content-center">
                <div class="col-xl-6 col-lg-6 col-md-9">
                    <div class="card o-hidden border-0 shadow-lg my-5">
                        <div class="card-body p-0">
                            <!-- Nested Row within Card Body -->
                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="p-5">
                                        <div class="text-center">
                                            <h1 class="h4 text-gray-900 mb-4">OAuth2</h1>
                                        </div>
                                        <form id="login" Method="POST">
                                            <div class="form-group">
                                                <input type="text" class="form-control form-control-user" id="id"
                                                    onkeyup="enterkeyId()" placeholder="아이디">
                                            </div>
                                            <div class="form-group">
                                                <input type="password" class="form-control form-control-user" id="pwd"
                                                    placeholder="패스워드" onkeyup="enterkeyPwd()">
                                            </div>
                                            <!--
                                            <div class="form-group">
                                                <div class="custom-control custom-checkbox small">
                                                    <input type="checkbox" class="custom-control-input" id="customCheck"
                                                        name="customCheck">
                                                    <label class="custom-control-label" for="customCheck">아이디 기억</label>
                                                </div>
                                            </div> -->
                                            <!--<label style="width: 100px; float: left">패스워드</label>
							<input type=password id="pwd" style="width: 100px;" onkeyup="enterkeyPwd()">
							<input type=button onclick="swLogin()" value="로그인">
							<br />
							<br />
							<input type="checkbox" id="chkId" name="chkId"> 아이디 기억-->
                                            <input type=button onclick="swLogin()"
                                                class="btn btn-primary btn-user btn-block" value="로그인">
                                            <hr>
                                        </form>
                                        <%-- <div class="text-center">
                                            <a class="small" href="forgot-password.html">Forgot Password?</a>
                                    </div>
                                    <div class="text-center">
                                        <a class="small" href="register.html">Create an Account!</a>
                                    </div>
                                    --%>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Bootstrap core JavaScript-->
        <script defer src="/vendor/jquery/jquery.min.js"></script>
        <script defer src="/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

        <!-- Core plugin JavaScript-->
        <script defer src="/vendor/jquery-easing/jquery.easing.min.js"></script>

        <!-- Custom scripts for all pages-->
        <script defer src="/js/sb-admin-2.min.js"></script>
    </body>

    </html>