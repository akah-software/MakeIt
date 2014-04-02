<%@ page import="org.akah.makeit.domain.Car" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'car.label', default: 'Car')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-car" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-car" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list car">
			
				<g:if test="${carInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="car.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${carInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${carInstance?.capacity}">
				<li class="fieldcontain">
					<span id="capacity-label" class="property-label"><g:message code="car.capacity.label" default="Capacity" /></span>
					
						<span class="property-value" aria-labelledby="capacity-label"><g:fieldValue bean="${carInstance}" field="capacity"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${carInstance?.availableSeats}">
				<li class="fieldcontain">
					<span id="availableSeats-label" class="property-label"><g:message code="car.availableSeats.label" default="Available Seats" /></span>
					
						<span class="property-value" aria-labelledby="availableSeats-label"><g:fieldValue bean="${carInstance}" field="availableSeats"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${carInstance?.user}">
				<li class="fieldcontain">
					<span id="user-label" class="property-label"><g:message code="car.user.label" default="User" /></span>
					
						<span class="property-value" aria-labelledby="user-label"><g:link controller="user" action="show" id="${carInstance?.user?.id}">${carInstance?.user?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:carInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${carInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
