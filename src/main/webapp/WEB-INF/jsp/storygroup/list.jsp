<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
    <h1><spring:message code="label.storygroup.list.page.title"/></h1>
    <div>
        <a href="/storygroup/add" id="add-button" class="btn btn-primary"><spring:message code="label.add.storygroup.link"/></a>
    </div>
    <div id="storygroup-list" class="page-content">
        <c:choose>
            <c:when test="${empty storygroups}">
                <p><spring:message code="label.storygroup.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ storygroups}" var="storygroup">
                    <div class="well well-small">
                        <a href="/storygroup/${storygroup.id}"><c:out value="${storygroup.title}"/></a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>