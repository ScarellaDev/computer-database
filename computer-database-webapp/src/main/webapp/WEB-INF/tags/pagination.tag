<%@ tag body-content="empty" %>
<%@ attribute name="target" required="true" %>
<%@ attribute name="pageNumber" required="true" type="java.lang.Integer"%>
<%@ attribute name="totalPages" required="true" type="java.lang.Integer" %>
<%@ attribute name="size" required="true" %>
<%@ attribute name="sort" required="true" %>
<%@ attribute name="direction" required="true" %>
<%@ attribute name="search" required="true" %>


<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="h" tagdir="/WEB-INF/tags" %>

<ul class="pagination">
	<c:if test="${pageNumber > 0}">
		<li><h:link target="${target}" size="${size}" search="${search}" sort="${sort}" direction="${direction}"><spring:message code="pagination-first"/></h:link></li>
		<li><h:link target="${target}" page="${pageNumber-1}" size="${size}" search="${search}" sort="${sort}" direction="${direction}">&laquo;</h:link></li>
	</c:if>
	<c:if test="${pageNumber-1 > 0}">
		<li><h:link target="${target}" page="${pageNumber-2}" size="${size}" search="${search}" sort="${sort}" direction="${direction}">${pageNumber-1}</h:link></li>
	</c:if>
	<c:if test="${pageNumber > 0}">
		<li><h:link target="${target}" page="${pageNumber-1}" size="${size}" search="${search}" sort="${sort}" direction="${direction}">${pageNumber}</h:link></li>
	</c:if>
	<c:forEach begin="${pageNumber}" end="${pageNumber+2}" var="i">
		<c:if test="${i <= totalPages-1}">
			<c:choose>
				<c:when test="${i == pageNumber}">
				<li class="disabled"><span>${i+1}</span></li>
				</c:when>
				<c:otherwise>
				<li><h:link target="${target}" page="${i}" size="${size}" search="${search}" sort="${sort}" direction="${direction}">${i+1}</h:link></li>
				</c:otherwise>
			</c:choose>
		</c:if>
	</c:forEach>
	<c:if test="${pageNumber < totalPages-1}">
		<li><h:link target="${target}" page="${pageNumber+1}" size="${size}" search="${search}" sort="${sort}" direction="${direction}">&raquo;</h:link></li>
		<li><h:link target="${target}" page="${totalPages-1}" size="${size}" search="${search}" sort="${sort}" direction="${direction}"><spring:message code="pagination-last"/></h:link></li>
	</c:if>
</ul>

<div class="btn-group btn-group-sm pull-right" role="group">
	<h:link target="${target}" size="10" search="${search}" sort="${sort}"  direction="${direction}" classes="btn btn-default">10</h:link>
	<h:link target="${target}" size="25" search="${search}" sort="${sort}"  direction="${direction}" classes="btn btn-default">25</h:link>
	<h:link target="${target}" size="50" search="${search}" sort="${sort}"  direction="${direction}" classes="btn btn-default">50</h:link>
	<h:link target="${target}" size="100" search="${search}" sort="${sort}"  direction="${direction}" classes="btn btn-default">100</h:link>
</div>