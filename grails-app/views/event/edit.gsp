<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="layout" content="main"/>
    <title>MakeIt - Edit Event</title>
</head>
<body>
<div class="container">
    <hr>
    <div class="row">
        <h1>Event > Edit</h1>

        <g:if test="${flash.message}">
            <div class="alert alert-info"><g:message code="${flash.message}"/></div>
        </g:if>
        <g:if test="${flash.error}">
            <div class="alert alert-danger"><g:message code="${flash.error}"/></div>
        </g:if>

        <g:form action="edit" id="${eventInstance?.id}" method="POST" class="form-group">
            <div class="fieldcontain">
                <label>
                    <g:message code="event.title.label" default="Title" />
                </label>
                <input class="form-control" id="title" type="text" value="${eventInstance?.title}"  disabled>
            </div>

            <g:render template="form"/>
            <br />
            <div class="buttons">
                <input type="submit" class="btn btn btn-primary" value="Edit"/>
            </div>
        </g:form>
    </div>
    <!-- /.row -->
</div>
<!-- /.container -->
</body>
</html>
