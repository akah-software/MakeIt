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
        <h1>My account</h1>

        <g:if test="${flash.message}">
            <div class="alert alert-info"><g:message code="${flash.message}"/></div>
        </g:if>
        <g:if test="${flash.error}">
            <div class="alert alert-danger"><g:message code="${flash.error}"/></div>
        </g:if>

        <div class="form-group">

            <g:if test="${userInstance?.loginName}">
                <label for="loginName"> <g:message code="user.loginName.label" default="Login Name" />
                </label>
                <input class="form-control" id="loginName" type="text" value="${userInstance.loginName}"  disabled>
            </g:if>

            <g:if test="${userInstance?.firstName}">
                <label for="firstName"> <g:message code="user.firstName.label" default="Login Name" />
                </label>
                <input class="form-control" id="firstName" type="text" value="${userInstance.firstName}"  disabled>
            </g:if>

            <g:if test="${userInstance?.secondName}">
                <label for="secondName"> <g:message code="user.secondName.label" default="Login Name" />
                </label>
                <input class="form-control" id="secondName" type="text" value="${userInstance.secondName}"  disabled>
            </g:if>

            <g:if test="${userInstance?.email}">
                <label for="email"> <g:message code="user.email.label" default="Login Name" />
                </label>
                <input class="form-control" id="email" type="text" value="${userInstance.email}"  disabled>
            </g:if>
        </div>
        <br />

        <g:form action="delete" method="DELETE">
            <fieldset class="buttons">
                <g:link class="btn btn-primary" action="myAccount" params="[edit:'enabled']">
                    <g:message code="default.button.edit.label" default="Edit" />
                </g:link>
                <g:actionSubmit
                        class="btn btn-danger" action="delete"
                        value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                        onclick="return confirm('You are about to delete your account. Are you sure?');" />
            </fieldset>
        </g:form>
    </div>
    <!-- /.row -->
</div>
<!-- /.container -->
</body>
</html>