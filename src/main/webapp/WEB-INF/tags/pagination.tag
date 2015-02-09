<%@ tag body-content="empty" %>
<%@ attribute name="target" required="true" %>
<%@ attribute name="pageIndex" required="true" type="java.lang.Integer"%>
<%@ attribute name="totalNbPages" required="true" type="java.lang.Integer" %>
<%@ attribute name="nbElementsPerPage" required="true" %>
<%@ attribute name="sort" required="true" %>
<%@ attribute name="order" required="true" %>
<%@ attribute name="search" required="true" %>


<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="h" tagdir="/WEB-INF/tags" %>

<ul class="pagination">
	<c:if test="${pageIndex > 1}">
		<li><h:link target="${target}" nbElementsPerPage="${nbElementsPerPage}" search="${search}" sort="${sort}" order="${order}"><spring:message code="pagination-first"/></h:link></li>
		<li><h:link target="${target}" pageIndex="${pageIndex-1}" nbElementsPerPage="${nbElementsPerPage}" search="${search}" sort="${sort}" order="${order}">&laquo;</h:link></li>
	</c:if>
	<c:if test="${pageIndex-2 > 0}">
		<li><h:link target="${target}" pageIndex="${pageIndex-2}" nbElementsPerPage="${nbElementsPerPage}" search="${search}" sort="${sort}" order="${order}">${pageIndex-2}</h:link></li>
	</c:if>
	<c:if test="${pageIndex-1 > 0}">
		<li><h:link target="${target}" pageIndex="${pageIndex-1}" nbElementsPerPage="${nbElementsPerPage}" search="${search}" sort="${sort}" order="${order}">${pageIndex-1}</h:link></li>
	</c:if>
	<c:forEach begin="${pageIndex}" end="${pageIndex+2}" var="i">
		<c:if test="${i <= totalNbPages}">
			<c:choose>
				<c:when test="${i == pageIndex}">
				<li class="disabled"><span>${i}</span></li>
				</c:when>
				<c:otherwise>
				<li><h:link target="${target}" pageIndex="${i}" nbElementsPerPage="${nbElementsPerPage}" search="${search}" sort="${sort}" order="${order}">${i}</h:link></li>
				</c:otherwise>
			</c:choose>
		</c:if>
	</c:forEach>
	<c:if test="${pageIndex < totalNbPages}">
		<li><h:link target="${target}" pageIndex="${pageIndex+1}" nbElementsPerPage="${nbElementsPerPage}" search="${search}" sort="${sort}" order="${order}">&raquo;</h:link></li>
		<li><h:link target="${target}" pageIndex="${totalNbPages}" nbElementsPerPage="${nbElementsPerPage}" search="${search}" sort="${sort}" order="${order}"><spring:message code="pagination-last"/></h:link></li>
	</c:if>
</ul>

<div class="btn-group btn-group-sm pull-right" role="group">
	<h:link target="${target}" nbElementsPerPage="10" search="${search}" sort="${sort}"  order="${order}" classes="btn btn-default">10</h:link>
	<h:link target="${target}" nbElementsPerPage="25" search="${search}" sort="${sort}"  order="${order}" classes="btn btn-default">25</h:link>
	<h:link target="${target}" nbElementsPerPage="50" search="${search}" sort="${sort}"  order="${order}" classes="btn btn-default">50</h:link>
	<h:link target="${target}" nbElementsPerPage="100" search="${search}" sort="${sort}"  order="${order}" classes="btn btn-default">100</h:link>
</div>