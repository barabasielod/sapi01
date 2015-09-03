<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/storygroup.view.js"></script>
</head>
<body>
    <div id="storygroup-id" class="hidden">${storygroup.id}</div>
    <h1><spring:message code="label.storygroup.view.page.title"/></h1>
    <div class="well page-content">
        <h2 id="storygroup-title"><c:out value="${storygroup.title}"/></h2>
        <div>
            <p><c:out value="${storygroup.description}"/></p>
        </div>
        <div class="action-buttons">
            <a href="/storygroup/update/${storygroup.id}" class="btn btn-primary"><spring:message code="label.update.storygroup.link"/></a>
            <a id="delete-story-link" class="btn btn-primary"><spring:message code="label.delete.storygroup.link"/></a>
        <a href="/storygroup/story/" class="btn btn-primary"><spring:message code="label.story.add"/></a>
        </div>
    </div>
    <script id="template-delete-storygroup-confirmation-dialog" type="text/x-handlebars-template">
        <div id="delete-storygroup-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.storygroup.delete.dialog.title"/></h3>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.storygroup.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-storygroup-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-storygroup-button" href="#" class="btn btn-primary"><spring:message code="label.delete.storygroup.button"/></a>
            </div>
        </div>
    </script>
</body>
</html>