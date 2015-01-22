<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.excilys.computerdatabase.domain.Page"%>
<ul class="pagination">
	<c:if test="${page.pageIndex != 1}">
		<li><a href="dashboard?pageIndex=1&nbElementsPerPage=${page.nbElementsPerPage}&search=${page.search}"
		aria-label="First">
		<span aria-hidden="true">First</span>
		</a></li>
		<li><a href="dashboard?pageIndex=${page.pageIndex-1}&nbElementsPerPage=${page.nbElementsPerPage}&search=${page.search}"
		aria-label="Previous">
		<span aria-hidden="true">&laquo;</span>
		</a></li>
	</c:if>
	<c:if test="${page.pageIndex-2 > 0}">
		<li><a href="dashboard?pageIndex=${page.pageIndex-2}&nbElementsPerPage=${page.nbElementsPerPage}&search=${page.search}">${page.pageIndex-2}</a></li>
	</c:if>
	<c:if test="${page.pageIndex-1 > 0}">
		<li><a href="dashboard?pageIndex=${page.pageIndex-1}&nbElementsPerPage=${page.nbElementsPerPage}&search=${page.search}">${page.pageIndex-1}</a></li>
	</c:if>
	<c:forEach begin="${page.pageIndex}" end="${page.pageIndex+2}" var="i">
		<c:if test="${i <= page.totalNbPages}">
			<c:choose>
				<c:when test="${i == page.pageIndex}">
					<li class="disabled"><span>${i}</span></li>
				</c:when>
				<c:otherwise>
					<li><a href="dashboard?pageIndex=${i}&nbElementsPerPage=${page.nbElementsPerPage}&search=${page.search}" >${i}</a></li>
				</c:otherwise>
			</c:choose>
		</c:if>
	</c:forEach>
	<c:if test="${page.pageIndex != page.totalNbPages}">
		<li><a href="dashboard?pageIndex=${page.pageIndex+1}&nbElementsPerPage=${page.nbElementsPerPage}&search=${page.search}"
		aria-label="Next">
		<span aria-hidden="true">&raquo;</span>
		</a></li>
		<li><a href="dashboard?pageIndex=${page.totalNbPages}&nbElementsPerPage=${page.nbElementsPerPage}&search=${page.search}"
		aria-label="Last">
		<span aria-hidden="true">Last</span>
		</a></li>
	</c:if>
</ul>

<div class="btn-group btn-group-sm pull-right" role="group">
	<a id="elements10" type="button" class="btn btn-default" href="dashboard?pageIndex=1&nbElementsPerPage=10&search=${page.search}">10</a>
	<a id="elements25" type="button" class="btn btn-default" href="dashboard?pageIndex=1&nbElementsPerPage=25&search=${page.search}">25</a>
	<a id="elements50" type="button" class="btn btn-default" href="dashboard?pageIndex=1&nbElementsPerPage=50&search=${page.search}">50</a>
	<a id="elements100" type="button" class="btn btn-default" href="dashboard?pageIndex=1&nbElementsPerPage=100&search=${page.search}">100</a>
</div>