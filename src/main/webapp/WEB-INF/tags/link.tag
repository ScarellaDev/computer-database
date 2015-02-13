<%@ tag body-content="scriptless" %>
<%@ attribute name="target" required="true" %>
<%@ attribute name="page" required="false" %>
<%@ attribute name="size" required="false" %>
<%@ attribute name="search" required="false" %>
<%@ attribute name="sort" required="false" %>
<%@ attribute name="direction" required="false" %>
<%@ attribute name="classes" required="false" %>
<jsp:doBody var="body" />

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${pageScope.page == null}">
	<c:set var="page" value="0"/>
</c:if>

<c:if test="${pageScope.size == null }">
	<c:set var="size" value="10"/>
</c:if>

<c:if test="${pageScope.search != null }">
	<c:set var="search" value="&search=${search}"/>
</c:if>

<c:if test="${pageScope.sort != null }">
	<c:set var="sort" value="&sort=${sort}"/>
	<c:if test="${pageScope.direction != null }">
		<c:set var="sort" value="${pageScope.sort},${pageScope.direction}"/>
	</c:if>
</c:if>

<c:if test="${pageScope.direction != null }">
	<c:set var="direction" value="&direction=${direction}"/>
</c:if>

<a class="${classes}" href="${pageScope.target}?page=${pageScope.page}&size=${pageScope.size}${pageScope.search}${pageScope.sort}">${body}</a>

