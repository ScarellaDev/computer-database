<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="com.excilys.computerdatabase.domain.*"%>
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
                    <div class="label label-default pull-right">
                        id: ${computerDto.id}
                    </div>
                    <h1><spring:message code="title-edit"/></h1>

                    <form:form id="form" action="editcomputer" method="POST" commandName="computerDto">
                        <form:input type="hidden" path="id" value="${computerDto.id}"/>
                        <form:errors path="id" cssClass="error"/>
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName"><spring:message code="computer-name"/></label>
								<spring:message code="computer-name" var="placeholdername"/>
                                <form:input path="name" type="text" class="form-control" id="name"  placeholder="${placeholdername}" value="${computerDto.name}" required="required"/>
                            	<form:errors path="name" cssClass="error"/>
                            </div>
                            <div class="form-group">
                                <label for="introduced"><spring:message code="computer-introduced"/></label>
                                <spring:message code="date-placeholder" var="placeholderdate"/>
                                <form:input path="introduced" type="date" class="form-control" id="introduced" placeholder="${placeholderdate}" value="${computerDto.introduced}"/>
                            	<form:errors path="introduced" cssClass="error"/>
                            </div>
                            <div class="form-group">
                                <label for="discontinued"><spring:message code="computer-discontinued"/></label>
                                <form:input path="discontinued" type="date" class="form-control" id="discontinued" placeholder="${placeholderdate}" value="${computerDto.discontinued}"/>
                            	<form:errors path="discontinued" cssClass="error"/>
                            </div>
                            <div class="form-group">
                                <label for="company"><spring:message code="computer-company"/></label>
                                <form:select path="companyId" class="form-control" id="companyId">
                                    <option value="0">--</option>
                                   	<c:forEach items="${companies}" var="company">
                                   	<c:choose>
                                   		<c:when test="${company.id == computerDto.companyId}">
                                   			<option value="${company.id}" selected="selected">${company.name}</option>
                                 		</c:when>
                                   		<c:otherwise>
                                   			<option value="${company.id}"><c:out value="${company.name}"/></option>
                                   		</c:otherwise>
                                  	</c:choose>
                                   	</c:forEach>
                                </form:select>
                                <form:errors path="companyId" cssClass="error"/>
                            </div>            
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="<spring:message code="button-edit"/>" class="btn btn-primary">
                            <spring:message code="text-or"/>
                            <a href="dashboard" class="btn btn-default"><spring:message code="button-cancel"/></a>
                        </div>
                    </form:form>
                    
                </div>
            </div>
        </div>
    </section>
</body>
</html>