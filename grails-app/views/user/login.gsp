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
        <h1>Login</h1>
        <p>Do you already have an account ?</p>
        <g:if test="${flash.message}">
            <div class="alert alert-info"><g:message code="${flash.message}"/></div>
        </g:if>
        <g:if test="${flash.error}">
            <div class="alert alert-danger"><g:message code="${flash.error}"/></div>
        </g:if>
        <g:form action="login" method="POST" class="form-group">
            <div class="fieldcontain ${hasErrors(bean: userInstance, field: 'email', 'error')} required">
                <label for="email"> <g:message code="user.email.label" default="Email" />
                    <span class="required-indicator">*</span>
                </label>
                <g:field type="email" class="form-control" name="email" required=""
                         value="${userInstance?.email}" />
            </div>
            <div class="fieldcontain ${hasErrors(bean: userInstance, field: 'password', 'error')} required">
                <label for="password"> <g:message code="user.password.label" default="Password" />
                    <span class="required-indicator">*</span>
                </label>
                <g:field type="password" class="form-control" name="password" required="" value="${userInstance?.password}" />
            </div>

            </fieldset><br>
            <div class="buttons">
                <button type="submit" class="btn btn-default">Login</button>
            </div>
        </g:form><br>
        <p>
            You do not have an account ? <g:link controller="user" action="create">Create</g:link> one!
        </p>
    </div>
    <!-- /.row -->
</div>
<!-- /.container -->
</body>
</html>
