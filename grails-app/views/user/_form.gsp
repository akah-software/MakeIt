<%@ page import="org.akah.makeit.domain.User" %>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'firstName', 'error')} required">
    <label for="firstName">
        <g:message code="user.firstName.label" default="First Name" />
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="firstName" class="form-control" maxlength="20" pattern="${userInstance.constraints.firstName.matches}" required="" value="${userInstance?.firstName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'secondName', 'error')} required">
    <label for="secondName">
        <g:message code="user.secondName.label" default="Second Name" />
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="secondName"  class="form-control" maxlength="20" pattern="${userInstance.constraints.secondName.matches}" required="" value="${userInstance?.secondName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: userInstance, field: 'email', 'error')} required">
    <label for="email">
        <g:message code="user.email.label" default="Email" />
        <span class="required-indicator">*</span>
    </label>
    <g:field type="email" name="email" class="form-control"  pattern="${userInstance.constraints.email.matches}" required="" value="${userInstance?.email}"/>
</div>


