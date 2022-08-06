<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <meta name="description" content="" />
    <meta name="author" content="" />
    <title>Login - Estimate+</title>
    <link href="<c:url value="/theme/css/styles.css"/>" rel="stylesheet" type="text/css">
    <script src="https://use.fontawesome.com/releases/v6.1.0/js/all.js" crossorigin="anonymous"></script>
</head>
<body class="bg-primary">
<div id="layoutAuthentication">
    <div id="layoutAuthentication_content">
        <main>
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-lg-5">
                        <div class="card shadow-lg border-0 rounded-lg mt-5">
                            <h1 class="mt-4 text-center">Estimate+</h1>
                            <%--                        <ol class="breadcrumb mb-4 text-center">--%>
                            <%--                            <li class="text-center breadcrumb-item active">Login to start</li>--%>
                            <%--                        </ol>--%>
                        </div>
                        <h2></h2>

                        <div class="card shadow-lg border-0 rounded-lg mt-5">
                            <div class="card-header"><h3 class="text-center font-weight-light my-4">Password reset form</h3></div>
                            <div class="card-body">
                                <form accept-charset="/resetpassword" method="post">
                                    <div class="form-floating mb-3">
                                        <input class="form-control" name="password" id="password" type="password" placeholder="password" />
                                        <label for="password">Password</label>
                                    </div>
                                    <div class="form-floating mb-3">
                                        <input class="form-control" name="password2" id="password2" type="password" placeholder="repeat password" />
                                        <label for="password2">Repeat password</label>
                                    </div>
                                    <c:if test="${not empty invalidPassword}">
                                        ${invalidPassword}
                                    </c:if>
                                    <div class="d-flex align-items-center justify-content-between mt-4 mb-0">
                                        <button name="button" value="send"  class="btn btn-primary">Send</button>
                                    </div>
                                </form>
                                <div class="card shadow-lg border-0 rounded-lg mt-5">
                                    <div class="card-body">
                                        <form method="get" action="/login">
                                            <div class="d-flex align-items-center justify-content-between mt-4 mb-0">
                                                <a href="\" class="btn btn-primary">Go to login page</a>
                                            </div>
                                        </form>
                                    </div>
                                </div>

                            </div>


                        </div>
                    </div>
                </div>
            </div>

        </main>
    </div>



    <%@ include file="/WEB-INF/jspf/common-footer.jspf" %>
