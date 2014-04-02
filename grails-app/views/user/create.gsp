<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="layout" content="main"/>
    <title>MakeIt - Create Account</title>
</head>
<body>
<div class="container">
    <hr>
    <div class="row">
        <div id="create-user" role="main" class="form-group">
            <h1>Create Account</h1>
            <g:if test="${flash.message}">
                <div class="alert alert-info"><g:message code="${flash.message}"/></div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="alert alert-danger"><g:message code="${flash.error}"/></div>
            </g:if>

            <g:form action="create" method="POST">
                <div class="fieldcontain ${hasErrors(bean: userInstance, field: 'loginName', 'error')} required">
                    <label for="loginName">
                        <g:message code="user.loginName.label" default="Login Name" />
                        <span class="required-indicator">*</span>
                    </label>
                    <g:textField name="loginName" class="form-control" maxlength="20" pattern="${userInstance.constraints.loginName.matches}" required="" value="${userInstance?.loginName}"/>
                </div>

                <g:render template="form"/><br>

                <div class="fieldcontain ${hasErrors(bean: userInstance, field: 'password', 'error')} required">
                    <label for="password">
                        <g:message code="user.password.label" default="Password" />
                        <span class="required-indicator">*</span>
                    </label>
                    <g:field type="password" class="form-control" name="password" required="" value=""/>
                </div>

                <div class="fieldcontain ${hasErrors(bean: userInstance, field: 'confirm', 'error')} required">
                    <label for="confirm">
                        <g:message code="user.confirm.label" default="Confirm password" />
                        <span class="required-indicator">*</span>
                    </label>
                    <g:field type="password" class="form-control" name="confirm" required="" value=""/>
                </div>
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
