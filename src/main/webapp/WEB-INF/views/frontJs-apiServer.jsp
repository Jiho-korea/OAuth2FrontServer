<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">
        <title>Front(자바스크립트)-OAuth2 API</title>
        <link href="/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
        <link
            href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
            rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.4.1.js"></script>
        <script>
            function apiAjax(apiUrl) {
                var req = new XMLHttpRequest();
                req.open("GET", '/front/js', false);
                req.send(null);
                $.ajax({
                    url: apiUrl + '',
                    type: 'GET',
                    contentType: false,
                    processData: false,
                    async: true,
                    beforeSend: function (xhr) {
                        //console.log(req.getResponseHeader("authorization"));
                        //console.log(req.getResponseHeader("accesstoken"));
                        //console.log(req.getResponseHeader("refreshtoken"));
                        //console.log(req.getResponseHeader("scope"));
                        xhr.setRequestHeader("Authorization", req.getResponseHeader("authorization"));
                        xhr.setRequestHeader("accesstoken", req.getResponseHeader("accesstoken"));
                        xhr.setRequestHeader("refreshtoken", req.getResponseHeader("refreshtoken"));
                        xhr.setRequestHeader("scope", req.getResponseHeader("scope"));
                    },
                    success: function (data) {
                        if (data != null && data != "") {
                            console.log(data); // api 서버에서 받은 데이터

                            $('#apiResult').append(data['name'] + "님 반갑습니다! {Front(자바스크립트) - ApiServer 서버 연결}");
                            //alert(data);
                            return;
                        } else {
                            alert("인증이 유효하지 않습니다.");
                            swLogOut();
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        // 토큰 만료시 token refresh 처리
                        let formData = new FormData();
                        formData.append("grant_type", "refresh_token");
                        formData.append("refresh_token", req.getResponseHeader("refreshtoken")); // refresh 토큰 form-data에 입력
                        //console.log(formData);
                        $.ajax({
                            url: 'http://localhost:8099/auth/oauth/token',
                            type: 'POST',
                            contentType: false,
                            processData: false,
                            async: true,
                            data: formData,
                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic " + btoa("oauth2-jwt-client" + ":" + "pass"));
                            },
                            success: function (data) {
                                if (data != null && data != "") {
                                    //console.log(data); // OAuth2 서버의 토큰 refresh 결과
                                    //Front 서버의 세션 정보 업데이트 ajax 문
                                    $.ajax({
                                        url: '/front/js',
                                        type: 'POST',
                                        contentType: false,
                                        processData: false,
                                        async: true,
                                        data: formData,
                                        beforeSend: function (xhr) {
                                            xhr.setRequestHeader("tokenType", data.token_type);
                                            xhr.setRequestHeader("accessToken", data.access_token);
                                            xhr.setRequestHeader("refreshToken", data.refresh_token);
                                            xhr.setRequestHeader("scope", data.scope);
                                        },
                                        success: function (data) {
                                            if (data == "success") {
                                                // 세션 업데이트 성공 시
                                                console.log("refresh success!");
                                                apiAjax(apiUrl);
                                                return;
                                            } else {
                                                alert("서버 오류입니다.");
                                                swLogOut();
                                            }
                                        },
                                        error: function (jqXHR, textStatus, errorThrown) {
                                            alert("서버 오류입니다.");
                                            swLogOut();
                                        }
                                    });
                                    //apiAjax();
                                    return;
                                } else {
                                    alert("refresh 인증이 유효하지 않습니다.");
                                    swLogOut();
                                }
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                alert("refresh 인증이 유효하지 않습니다.");
                                swLogOut();
                            }
                        });
                    }
                });
            }

            document.addEventListener("DOMContentLoaded", function () {
                // apiAjax('http://localhost:8088/user/data'); // test Api
                apiAjax('http://localhost:8089/forexApi/report/ccr'); // board Api
            });
            function swLogOut() {
                $.ajax({
                    type: 'POST',
                    url: '/logout',
                    data: "json",
                    success: function (data) {
                        if (data != null) {
                            // alert("로그 아웃 처리 되었습니다.");
                            top.location = "/login";
                        } else {
                            alert("에러발생 관리자에게 문의 하세요");
                            return;
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        alert('로그 아웃 ajax 실패');
                        console.log(jqXHR, textStatus, errorThrown);
                    }
                });
            }
        </script>
    </head>

    <body>
        <p id="apiResult"></p>
        <br><br><br>

        <a href="/main">메인으로 이동</a><br><br>
        <a onclick="javascript:if (confirm('로그 아웃 하시겠습니까?')){swLogOut();}" style="cursor: pointer;">
            로그아웃
        </a>
        <!-- Bootstrap core JavaScript-->
        <script defer src="/vendor/jquery/jquery.min.js"></script>
        <script defer src="/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

        <!-- Core plugin JavaScript-->
        <script defer src="/vendor/jquery-easing/jquery.easing.min.js"></script>
    </body>

    </html>