<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="layout" content="main"/>
    <title>MakeIt - Create Event</title>
</head>
<body>
<div class="container">
    <hr>
    <div class="row">
        <div id="create-user" role="main" class="form-group">
            <h1>Create Event</h1>
            <g:if test="${flash.message}">
                <div class="alert alert-info"><g:message code="${flash.message}"/></div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="alert alert-danger"><g:message code="${flash.error}"/></div>
            </g:if>

            <g:form action="create" method="POST">
                <div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'title', 'error')} required">
                    <label for="title">
                        <g:message code="event.title.label" default="Title" />
                        <span class="required-indicator">*</span>
                    </label>
                    <g:textField name="title" maxlength="50" pattern="${eventInstance.constraints.title.matches}" class="form-control" required="" value="${eventInstance?.title}"/>
                </div>

                <g:render template="form"/><br>
                <br />
                <div class="buttons">
                    <input type="submit" class="btn btn-primary" value="Create"/>
                </div>
            </g:form>
        </div>
    </div>
    <!-- /.row -->
</div>
<!-- /.container -->
</body>
</html>
