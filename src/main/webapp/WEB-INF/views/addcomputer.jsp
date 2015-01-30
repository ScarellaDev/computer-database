<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="com.excilys.computerdatabase.domain.*"%>
<%@ page import="java.util.*" %>
<jsp:include page="includes/header.jsp" />
<script type="text/javascript" src="js/jquery.min.js"></script>	
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script src="js/formValidation.js"></script>

<style>
	.error{
		color: red;
	}
</style>

    <section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <h1>Add Computer</h1>
                    
                    <form:form id="form" action="addcomputer" method="POST" commandName="computerDto">
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName">Computer name</label>
                            	<form:input path="name" type="text" class="form-control" id="name" placeholder="Computer name" required="required"/>
                            	<form:errors path="name" cssClass="error"/>
                            </div>
                            <div class="form-group">
                                <label for="introduced">Introduced date</label>
                                <form:input path="introduced" type="date" class="form-control" id="introduced" placeholder="yyyy-MM-dd"/>
                            	<form:errors path="introduced" cssClass="error"/>
                            </div>
                            <div class="form-group">
                                <label for="discontinued">Discontinued date</label>
                                <form:input path="discontinued" class="form-control" id="discontinued" placeholder="yyyy-MM-dd"/>
                            	<form:errors path="discontinued" cssClass="error"/>
                            </div>
                            <div class="form-group">
                                <label for="companyId">Company</label>
                                <form:select path="companyId" class="form-control" id="companyId">
                                    <option value="0">--</option>
                                    <c:forEach items="${companies}" var="company">
                                   	<option value="${company.id}"><c:out value="${company.name}"/></option>
                                   	</c:forEach>
                                </form:select>
                                <form:errors path="companyId" cssClass="error"/>
                            </div>                  
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="Add" class="btn btn-primary">
                            or
                            <a href="dashboard" class="btn btn-default">Cancel</a>
                        </div>
                    </form:form>
                    
                </div>
            </div>
        </div>
    </section>
</body>
</html>