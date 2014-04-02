<%@ page import="org.akah.makeit.domain.Event" %>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'address', 'error')} required">
    <label for="address">
        <g:message code="event.address.label" default="Address" />
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="address" maxlength="50" pattern="${eventInstance.constraints.address.matches}" class="form-control" required="" value="${eventInstance?.address}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'dateBeginEvent', 'error')} required">
	<label for="dateBeginEvent">
        <g:message code="event.begin.label" default="From" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="dateBeginEvent" precision="minute" pattern="${eventInstance.constraints.dateBeginEvent}" class="form-control" value="${eventInstance?.dateBeginEvent}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'dateEndEvent', 'error')} required">
    <label for="dateBeginEvent">
        <g:message code="event.until.label" default="To" />
        <span class="required-indicator">*</span>
    </label>
    <g:datePicker name="dateEndEvent" precision="minute" pattern="${eventInstance.constraints.dateEndEvent}" class="form-control" value="${eventInstance?.dateEndEvent}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'description', 'error')} required">
	<label for="description">
        <g:message code="event.description.label" default="Describe you event" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="description" maxlength="300" pattern="${eventInstance.constraints.description.matches}" class="form-control" required="" value="${eventInstance?.description}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: eventInstance, field: 'privacy', 'error')} required">
	<label for="privacy">
        <g:message code="event.type.label" default="Select your event privacy" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="privacy" from="${org.akah.makeit.domain.Type?.values()}" keys="${org.akah.makeit.Type.values()*.name()}" class="form-control" required="" value="${eventInstance?.privacy?.name()}"/>
</div>


