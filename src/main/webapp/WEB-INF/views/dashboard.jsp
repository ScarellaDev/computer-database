<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.excilys.computerdatabase.domain.*"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<jsp:include page="includes/header.jsp" />

	<section id="main">
		<div class="container">
			<h1 id="homeTitle">${page.totalNbElements} Computers found</h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" action="#" method="GET" class="form-inline">
						<input type="search" id="search" name="search"
						class="form-control" placeholder="Search name" /> <input
						type="submit" id="searchsubmit" value="Filter by name"
						class="btn btn-primary" />
					</form>
				</div>
				<div class="pull-right">
					<a class="btn btn-success" id="addcomputer" href="addcomputer">Add</a>
					<a class="btn btn-danger" id="deletecomputer" href="#"
					onclick="$.fn.toggleEditMode();">Delete</a>
				</div>
			</div>
		</div>
		
		<form id="deleteForm" action="deletecomputer" method="POST">
			<input type="hidden" name="selection" value="">
		</form>
		
		<div class="container" style="margin-top: 10px;">
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
						<%String[][] columns = {{"1","Computer name"}, {"2", "Introduced date"},
						{"3", "Discontinued date"}, {"4","Company name"}};
						pageContext.setAttribute("columns", columns);%>
						<c:forEach items="${columns}" var="col">
							<c:choose>
								<c:when test="${col[0].equals(page.sort.toString()) && page.order.equals(\"ASC\") }">
									<th><h:link target="dashboard" pageIndex="${page.pageIndex}" nbElementsPerPage="${page.nbElementsPerPage}" search="${page.search}" sort="${col[0]}" order="desc">${col[1]}</h:link></th>
								</c:when>
								<c:when test="${col[0].equals(page.sort.toString()) && page.order.equals(\"DESC\") }">
									<th><h:link target="dashboard" pageIndex="${page.pageIndex}" nbElementsPerPage="${page.nbElementsPerPage}" search="${page.search}" sort="${col[0]}" order="asc">${col[1]}</h:link></th>
								</c:when>
								<c:otherwise>
									<th><h:link target="dashboard" pageIndex="${page.pageIndex}" nbElementsPerPage="${page.nbElementsPerPage}" search="${page.search}" sort="${col[0]}" order="asc">${col[1]}</h:link></th>
								</c:otherwise>
							</c:choose>
						</c:forEach>	
					</tr>
				</thead>
				<!-- Browse attribute computers -->
				<tbody id="results">
					<c:forEach items="${page.list}" var="computer">
						<tr>
							<td class="editMode"><input type="checkbox" name="cb" class="cb" value="${computer.id}"></td>
							<td><a href="editcomputer?id=${computer.id}"><c:out value="${computer.name}"/></a></td>
							<td>${computer.introduced}</td>
							<td>${computer.discontinued}</td>
							<td>${computer.companyName}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</section>
	
	<footer class="navbar-fixed-bottom">
		<div class="container text-center">
			<h:pagination target="dashboard" pageIndex="${page.pageIndex}" totalNbPages="${page.totalNbPages}" nbElementsPerPage="${page.nbElementsPerPage}" search="${page.search}" sort="${page.sort.toString()}" order="${page.order}"/>
		</div>
	</footer>
	
	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/dashboard.js"></script>
	
	</body>
</html>