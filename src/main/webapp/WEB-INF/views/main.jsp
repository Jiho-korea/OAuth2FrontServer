<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">
        <title>메인페이지</title>
        <link href="/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
        <link
            href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
            rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.4.1.js"></script>
        <script>
            function parseHttpHeaders(httpHeaders) {
                return httpHeaders
                    .split("\n")
                    .map(x => x.split(/: */, 2))
                    .filter(x => x[0])
                    .reduce((ac, x) => {
                        ac[x[0]] = x[1];
                        return ac;
                    }, {});
            }

            document.addEventListener("DOMContentLoaded", function () {
                var req = new XMLHttpRequest();
                req.open("GET", '/main', false);
                req.send(null);
                var headers = parseHttpHeaders(req.getAllResponseHeaders());
                $.ajax({
                    url: 'http://localhost:8088/user/data',
                    type: 'GET',
                    contentType: false,
                    processData: false,
                    async: false,
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader("Authorization", headers.authorization);
                        xhr.setRequestHeader("scope", headers.scope);
                    },
                    success: function (data) {
                        if (data != null) {
                            console.log(data);
                            $('#content1').append(data['name'] + "님 반갑습니다! (자바스크립트 - ApiServer 서버 연결)");
                            //alert(data);
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
                    url: '/main',
                    success: function (data) {
                        if (data != null) {
                            console.log(data);
                            // alert("로그 아웃 처리 되었습니다.");
                            $('#content2').append(data['result']['name'] + "님 반갑습니다! (FrontServer - ApiServer 서버 연결)");
                        } else {
                            alert("에러발생 관리자에게 문의 하세요");
                            return;
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        alert("로그인 오류입니다.");
                        console.log(jqXHR, textStatus, errorThrown);
                    }
                });
            });
            function swLogOut() {
                if (confirm("로그 아웃 하시겠습니까?")) {
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
            }
        </script>
    </head>

    <body>
        <p id="content1"></p>

        <p id="content2"></p>
        <br><br><br>

        <a onclick="javascript:swLogOut();" style="cursor: pointer;">
            로그아웃
        </a>
        <!-- Bootstrap core JavaScript-->
        <script defer src="/vendor/jquery/jquery.min.js"></script>
        <script defer src="/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

        <!-- Core plugin JavaScript-->
        <script defer src="/vendor/jquery-easing/jquery.easing.min.js"></script>
    </body>

    </html>