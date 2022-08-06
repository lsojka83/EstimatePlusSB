<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <meta name="description" content=""/>
    <meta name="author" content=""/>
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

                        </div>

                        <div class="card shadow-lg border-0 rounded-lg mt-5">
                            <div class="card-body">
                                ${message}
                            </div>

                        </div>

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

        </main>
    </div>


    <%@ include file="/WEB-INF/jspf/common-footer.jspf" %>
