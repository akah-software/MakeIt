<%@ page import="org.akah.makeit.services.EventService" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="layout" content="main"/>
        <title><g:message code="app.title"/> - <g:message code="event.list.title"/></title>
    </head>

    <body>
        <div class="container">
            <br /><br /><br />
            <h1><g:message code="event.list.title"/></h1>
            <g:link class="btn btn-primary" action="create">Create an event</g:link>
            <br/><br/>
            <g:if test="${flash.message}">
                <div class="alert alert-info"><g:message code="${flash.message}"/></div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="alert alert-danger"><g:message code="${flash.error}"/></div>
            </g:if>
            <div class="row text-center">
                <g:each in="${eventList}" status="i" var="eventInstance">
                    <div class="col-lg-3 col-md-6 hero-feature">
                        <div class="thumbnail">

                            <div class="caption">
                                <g:if test="${EventService.isUserAllowedToEdit(session.user, eventInstance)}">
                                    <div class="progress">
                                        <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
                                            OWNER
                                        </div>
                                    </div>
                                </g:if>
                                <g:elseif test="${EventService.isUserGuest(session.user, eventInstance)}">
                                    <div class="progress">
                                        <div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
                                            INVOLVED
                                        </div>
                                    </div>
                                </g:elseif>
                                <h3>${eventInstance.title}</h3>
                                <hr>

                                <p>${eventInstance.description}</p>
                                <p><b>Begin</b> : <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${eventInstance.dateBeginEvent}"/></p>
                                <p><b>End</b> : <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${eventInstance.dateEndEvent}"/></p>

                                <g:link class="btn btn-primary" action="show" id="${eventInstance.id}">+</g:link>
                            </div>
                        </div>
                    </div>
                </g:each>
            </div>
        </div>
    </body>
</html>
