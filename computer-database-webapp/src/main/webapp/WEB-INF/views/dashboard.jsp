<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.excilys.computerdatabase.domain.*"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<jsp:include page="includes/header.jsp" />

	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>	
	<script>var alertMessage="<spring:message code='alert.message' javaScriptEscape='true'/>"</script>
	<script src="js/dashboard.js"></script>

	<section id="main">
		<div class="container">
			<h1 id="homeTitle">${page.totalElements} <spring:message code="title-dashboard"/></h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" action="#" method="GET" class="form-inline">
						<input type="hidden" name="size" value="${page.size}">
						<input type="hidden" name="page" value="0">
						<input type="search" id="search" name="search"
						class="form-control" placeholder="<spring:message code="search-placeholder"/>" /> <input
						type="submit" id="searchsubmit" value="<spring:message code="button-filter"/>"
						class="btn btn-primary" />
					</form>
				</div>
				<div class="pull-right">
					<a class="btn btn-success" id="addcomputer" href="addcomputer"><spring:message code="button-add"/></a>
					<a class="btn btn-danger" id="deletecomputer" href="#"
					onclick="$.fn.toggleEditMode();">
						<spring:message code="button-delete"/>
					</a>
				</div>
			</div>
		</div>
		
		<form id="deleteForm" action="deletecomputer" method="POST">
			<input type="hidden" name="selection" value="">
		</form>
		
		<div class="container" style="margin-top: 10px;">
			<c:if test="${not empty message}"><div id="message" class="alert alert-success text-center"><c:out value="${message}"/></div></c:if>
			<c:if test="${not empty errormessage}"><div id="errormessage" class="alert alert-danger text-center"><c:out value="${errormessage}"/></div></c:if>
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<!-- Variable declarations for passing labels as parameters -->
						<!-- Table header for Computer Name -->
						<th class="editMode" style="width: 60px; height: 22px;"><input
						type="checkbox" id="selectall" /> <span
						style="vertical-align: top;"> - <a href="#"
						id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
						class="fa fa-trash-o fa-lg"></i>
						</a>
						</span></th>
						<%String[][] columns = {{"name","computer-name"}, {"introduced", "computer-introduced"},
						{"discontinued", "computer-discontinued"}, {"company.name","computer-company"}};
						pageContext.setAttribute("columns", columns);%>
						<c:forEach items="${columns}" var="col">
							<c:choose>
								<c:when test="${col[0].equals(sort) && direction.equalsIgnoreCase(\"ASC\") }">
									<th><h:link target="dashboard" size="${page.size}" search="${search}" sort="${col[0]}" direction="desc"><spring:message code="${col[1]}"/></h:link></th>
								</c:when>
								<c:otherwise>
									<th><h:link target="dashboard" size="${page.size}" search="${search}" sort="${col[0]}" direction="asc"><spring:message code="${col[1]}"/></h:link></th>
								</c:otherwise>
							</c:choose>
						</c:forEach>		
					</tr>
				</thead>
				<!-- Browse attribute computers -->
				<tbody id="results">
					<c:forEach items="${page.content}" var="computer">
						<tr>
							<td class="editMode"><input type="checkbox" name="cb" class="cb" value="${computer.id}"></td>
							<td><a href="editcomputer?id=${computer.id}"><c:out value="${computer.name}"/></a></td>
							<td>${computer.introduced}</td>
							<td>${computer.discontinued}</td>
							<td>${computer.company.name}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</section>
	
	<footer class="navbar-fixed-bottom">
		<div class="container text-center">
			<h:pagination target="dashboard" pageNumber="${page.number}" totalPages="${page.totalPages}" size="${page.size}" search="${search}" sort="${sort}" direction="${direction}"/>
		</div>
	</footer>
	
	</body>
</html>