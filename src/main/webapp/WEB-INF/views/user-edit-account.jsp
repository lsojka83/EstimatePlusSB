
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>



<%@ include file="/WEB-INF/jspf/user-header.jspf" %>

<main>
    <div class="container-fluid px-4">
        <div id="layoutAuthentication">
            <div id="layoutAuthentication_content">
                <main>
                    <div class="container">
                        <div class="row justify-content-center">
                            <div class="col-lg-7">
                                <div class="card shadow-lg border-0 rounded-lg mt-5">
                                    <div class="card-header"><h3 class="text-center font-weight-light my-4">Edit
                                        Account</h3></div>
                                    <div class="card-body">

                                        <form:form method="post" modelAttribute="user">
                                                <div class="form-floating mb-3 mb-md-0">
                                                    <form:input path="userName" id="userName"
                                                                type="text" cssClass="form-control"></form:input>
                                                    <label for="userName">User name</label>
                                                    <form:errors path="userName"/>
                                                </div>
                                                <div class="form-floating mb-3">
                                                    <form:input path="email" id="email" type="text"  cssClass="form-control"></form:input>
                                                    <label for="email">Email</label>
                                                    <form:errors path="email"/>
                                                </div>
                                            <div class="form-floating mb-3 mb-md-0">
                                                <input type="password" name="password" placeholder="Enter new password"
                                                       id="password" class="form-control"/>
                                                <label for="password">Enter new password</label>
                                                <c:if test="${not empty invalidPassword}">
                                                    ${invalidPassword}
                                                </c:if>
                                            </div>
                                            <div class="form-floating mb-3 mb-md-0">
                                                <input type="password" name="password2" placeholder="Repeat new password"
                                                       id="password2" class="form-control"/>
                                                <label for="password2">Reapet new password</label>
                                            </div>
                                            <form:hidden path="admin"/>
                                            <form:hidden path="uuid"/>
                                            <form:hidden path="id"/>
                                            <form:hidden path="estimates"/>
                                            <form:hidden path="userPriceList"/>
                                            <form:hidden path="active"/>
                                            <form:hidden path="sentResetRequest"/>

                                        <div class="mt-4 mb-0">
                                            <div class="d-grid">
                                                <div class="btn btn-primary btn-block">
                                                    <button type="submit" element="button" class="btn btn-primary btn-block">Save</button>
                                                </div>
                                            </div>
                                            </form:form>

                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                </main>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/jspf/common-footer.jspf" %>

