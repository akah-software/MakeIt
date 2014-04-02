<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="layout" content="main"/>
    <title>MakeIt</title>
</head>
<body>
<div class="container">
    <hr>
    <div class="row">
        <h1>My Account > Edit</h1>

        <g:if test="${flash.message}">
            <div class="alert alert-info"><g:message code="${flash.message}"/></div>
        </g:if>
        <g:if test="${flash.error}">
            <div class="alert alert-danger"><g:message code="${flash.error}"/></div>
        </g:if>

        <g:form action="myAccount" params="[edit:'enabled']" method="POST" class="form-group">
            <div class="fieldcontain">
                <label>
                    <g:message code="user.loginName.label" default="Login Name" />
                </label>
                <input class="form-control" id="loginName" type="text" value="${userInstance.loginName}"  disabled>
            </div>
            <g:render template="form"/>
            <div class="fieldcontain">
                <label for="current">
                    <g:message code="user.edit.currentPassword.label" default="Current password" />
                </label>
                <g:field type="password" class="form-control" name="current" value=""/>
            </div>

            <div class="fieldcontain ${hasErrors(bean: userInstance, field: 'password', 'error')}">
                <label for="password">
                    <g:message code="user.newPassword.label" default="New Password" />
                </label>
                <g:field type="password" class="form-control" name="password" value=""/>
            </div>

            <div class="fieldcontain ${hasErrors(bean: userInstance, field: 'confirm', 'error')}">
                <label for="confirm">
                    <g:message code="user.confirm.label" default="Confirm password" />
                </label>
                <g:field type="password" class="form-control" name="confirm" value=""/>
            </div>

            <br />
            <div class="buttons">
                <input type="submit" class="btn btn-primary" value="Edit"/>
            </div>
        </g:form>
    </div>
    <!-- /.row -->
</div>
<!-- /.container -->
</body>
</html>
