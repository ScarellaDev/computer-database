<%@ tag body-content="scriptless" %>
<%@ attribute name="target" required="true" %>
<%@ attribute name="pageIndex" required="false" %>
<%@ attribute name="nbElementsPerPage" required="false" %>
<%@ attribute name="search" required="false" %>
<%@ attribute name="sort" required="false" %>
<%@ attribute name="order" required="false" %>
<%@ attribute name="classes" required="false" %>
<jsp:doBody var="body" />

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${pageScope.pageIndex == null}">
	<c:set var="pageIndex" value="1"/>
</c:if>
<c:if test="${pageScope.nbElementsPerPage == null }">
	<c:set var="nbElementsPerPage" value="10"/>
</c:if>
<c:if test="${pageScope.search != null }">
	<c:set var="search" value="&search=${search}"/>
</c:if>
<c:if test="${pageScope.sort != null }">
	<c:set var="sort" value="&sort=${sort}"/>
</c:if>
<c:if test="${pageScope.order != null }">
	<c:set var="order" value="&order=${order}"/>
</c:if>

<a class="${classes}" href="${pageScope.target}?pageIndex=${pageScope.pageIndex}&nbElementsPerPage=${pageScope.nbElementsPerPage}${pageScope.search}${pageScope.sort}${pageScope.order}">${body}</a>

