<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="layout" content="main"/>
    <title>MakeIt - Invite people to ${eventInstance}</title>
</head>

<body>
    <div class="container">
        <hr>
        <div class="row">
            <h1>Event > Invite to ${eventInstance}</h1>

            <g:if test="${flash.message}">
                <div class="alert alert-info"><g:message code="${flash.message}"/></div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="alert alert-danger"><g:message code="${flash.error}"/></div>
            </g:if>

            <g:form action="invite" id="${eventInstance?.id}" method="POST" class="form-group">
                <div class="fieldcontain">
                    <label>
                        <g:message code="invitation.friend.label" default="Friend" />
                    </label>
                    <input class="form-control" id="title" type="text" name="friend" />
                </div>
                <br />
                <div class="buttons">
                    <input type="submit" class="btn btn btn-primary" value="Invite"/>
                </div>
            </g:form>
        </div>
    </div>
</body>
</html>