<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ include file="/WEB-INF/jspf/admin-header.jspf" %>


<main>
    <div class="container-fluid px-4">
        <div class="card mb-4">
            <div class="card-body">
                <span>Number of users: ${users.size()}</span>
            </div>
        </div>
        <div class="card mb-4">
            <div class="card-header">
                <i class="fas fa-table me-1"></i>
                User list
            </div>
            <div class="card-body">
                <table id="datatablesSimple">
                    <thead>
                    <tr>
                        <th>Id</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Is admin</th>
                        <th>No of estimates</th>
                        <th>No of pricelist item</th>
                        <th>Active</th>
                        <th>UUID</th>
                        <th>Sent Password Request</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="user" items="${users}">
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.userName}</td>
                            <td>${user.email}</td>
                            <td>${user.admin}</td>
                            <td>${user.estimates.size()}</td>
                            <td>${user.userPriceList.numberOfItems}</td>
                            <td>${user.active}</td>
                            <td>${user.uuid}</td>
                            <td>${user.sentResetRequest}</td>
                            <td>
                                <c:if test="${user.id != currentUser.id}">
                                    <a href="/admin/edituser?id=${user.id}"
                                       class="btn btn-warning">Edit</a>
                                    <a href="/admin/deleteuser?id=${user.id}"
                                       class="btn btn-danger"
                                       id="deleteLink" onclick="return confirm('Are you sure?')">Delete</a>
                                </c:if>

                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</main>
<%@ include file="/WEB-INF/jspf/common-footer.jspf" %>
