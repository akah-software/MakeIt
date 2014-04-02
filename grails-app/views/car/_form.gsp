<%@ page import="thatnight.Car" %>



<div class="fieldcontain ${hasErrors(bean: carInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="car.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${carInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: carInstance, field: 'capacity', 'error')} required">
	<label for="capacity">
		<g:message code="car.capacity.label" default="Capacity" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="capacity" from="${2..7}" class="range" required="" value="${fieldValue(bean: carInstance, field: 'capacity')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: carInstance, field: 'availableSeats', 'error')} required">
	<label for="availableSeats">
		<g:message code="car.availableSeats.label" default="Available Seats" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="availableSeats" type="number" value="${carInstance.availableSeats}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: carInstance, field: 'user', 'error')} required">
	<label for="user">
		<g:message code="car.user.label" default="User" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="user" name="user.id" from="${thatnight.User.list()}" optionKey="id" required="" value="${carInstance?.user?.id}" class="many-to-one"/>
</div>

