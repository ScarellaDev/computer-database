<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.excilys.computerdatabase.domain.*"%>
<jsp:include page="includes/header.jsp" />
<script type="text/javascript" src="js/jquery.min.js"></script>	
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script src="js/formValidation.js"></script>

    <section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <div class="label label-default pull-right">
                        id: ${computer.id}
                    </div>
                    <h1>Edit Computer</h1>
					<p class="text-warning">${error.get("eId")}</p>
					<p class="text-warning">${error.get("eName")}</p>
                    <p class="text-warning">${error.get("eDateI")}</p>
                    <p class="text-warning">${error.get("eDateD")}</p>
                    <p class="text-warning">${error.get("eCompanyId")}</p>
                    <form id="form" action="editcomputer" method="POST">
                        <input type="hidden" name="id" value="${computer.id}"/>
                        <fieldset>
                            <div class="form-group">
                                <label for="name">Computer name</label>
                                <input type="text" class="form-control" id="name" name="name" placeholder="Computer name" value="${computer.name}" required="required">
                            </div>
                            <div class="form-group">
                                <label for="introduced">Introduced date</label>
                                <input type="date" class="form-control" id="introduced" name="introduced" placeholder="yyyy-MM-dd" value="${computer.introduced}">
                            </div>
                            <div class="form-group">
                                <label for="discontinued">Discontinued date</label>
                                <input type="date" class="form-control" id="discontinued" name="discontinued" placeholder="yyyy-MM-dd" value="${computer.discontinued}">
                            </div>
                            <div class="form-group">
                                <label for="companyId">Company</label>
                                <select class="form-control" id="companyId" name="companyId">
                                    <option value="0">--</option>
                                   	<c:forEach items="${companies}" var="company">
                                   	<c:if test="${company.id == computer.companyId}">
                                   	<option value="${company.id}" selected="selected">${company.name}</option>
                                   	</c:if>
                                   	<c:if test="${company.id != computer.companyId}"></c:if>
                                   	<option value="${company.id}"><c:out value="${company.name}"/></option>
                                   	</c:forEach>
                                </select>
                            </div>            
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="Edit" class="btn btn-primary">
                            or
                            <a href="dashboard" class="btn btn-default">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
</body>
</html>