<%@ page import="org.akah.makeit.services.EventService" %>
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
    <h1>Event details</h1>

    <g:if test="${flash.message}">
        <div class="alert alert-info"><g:message code="${flash.message}"/></div>
    </g:if>
    <g:if test="${flash.error}">
        <div class="alert alert-danger"><g:message code="${flash.error}"/></div>
    </g:if>

    <div class="form-group">

        <g:if test="${eventInstance?.title}">
            <label for="title"> <g:message code="event.title.label" default="Title" />
            </label>
            <input class="form-control" id="title" type="text" value="${eventInstance.title}"  disabled>
        </g:if>

        <g:if test="${eventInstance?.address}">
            <label for="address"> <g:message code="event.address.label" default="Address" />
            </label>
            <input class="form-control" id="address" type="text" value="${eventInstance.address}"  disabled>
        </g:if>

        <g:if test="${eventInstance?.dateBeginEvent}">
            <label for="dateBeginEvent"> <g:message code="event.begin.label" default="From" />
            </label>

            <input class="form-control" id="dateBeginEvent" type="datetime" value="<g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${eventInstance.dateBeginEvent}"/>"  disabled>
        </g:if>

        <g:if test="${eventInstance?.dateEndEvent}">
            <label for="dateEndEvent"> <g:message code="event.until.label" default="To" />
            </label>
            <input class="form-control" id="dateEndEvent" type="datetime" value="<g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${eventInstance.dateEndEvent}"/>"  disabled>
        </g:if>

        <g:if test="${eventInstance?.description}">
            <label for="dateEndEvent"> <g:message code="event.description.label" default="Description" />
            </label>
            <input class="form-control" id="description" type="text" value="${eventInstance.description}"  disabled>
        </g:if>

        <g:if test="${eventInstance?.privacy}">
            <label for="privacy"> <g:message code="event.type.label" default="Privacy" />
            </label>
            <input class="form-control" id="privacy" type="datetime" value="${eventInstance.privacy}"  disabled>
        </g:if>
    </div>
    <br />
    <g:if test="${EventService.isUserAllowedToEdit(session.user, eventInstance)}">
        <g:form url="[action:'delete', params:[id:eventInstance.id]]" method="DELETE">
            <fieldset class="buttons">
                <g:link class="btn btn-primary" action="edit" params="[id:eventInstance.id]">
                    <g:message code="default.button.edit.label" default="Edit" />
                </g:link>
                <g:actionSubmit
                        class="btn btn-danger" action="delete"
                        value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                        onclick="return confirm('You are about to delete your event. Are you sure?');" />
            </fieldset>
        </g:form>
        </div>
    </g:if>
    <g:elseif test="${isUserInvolved}">
        <g:link class="btn btn-danger" controller="event" action="unInvolve" id="${eventInstance.id}">Disengage</g:link>
    </g:elseif>
    <g:else>
        <g:link class="btn btn-primary" controller="event" action="getInvolved" id="${eventInstance.id}">Involve</g:link>
    </g:else>
</div>
<!-- /.row -->
</div>
<!-- /.container -->
</body>
</html>