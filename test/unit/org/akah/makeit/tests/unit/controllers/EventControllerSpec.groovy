package org.akah.makeit.tests.unit.controllers

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.akah.makeit.controllers.EventController
import org.akah.makeit.domain.Car
import org.akah.makeit.domain.Event
import org.akah.makeit.domain.Involved
import org.akah.makeit.domain.Type
import org.akah.makeit.domain.User
import org.akah.makeit.services.EventService
import org.akah.makeit.services.GeneralService
import org.akah.makeit.services.InvolvedService
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(EventController)
@Mock([User, Event, Involved, Car])
class EventControllerSpec extends Specification {
    def "Test auth"() {
        given:
        GroovyMock(GeneralService, global: true)

        and:
        1 * GeneralService.authenticateWithRedirection(_,_,_) >> variable

        expect:
        controller.auth() == resultat

        where:
        variable | resultat
        true    | true
        false   | false
    }

    @Unroll
    def "delete an event" () {
        given:
        GroovyMock(EventService, global: true)

        and:
        1 * EventService.removeEvent(_,_) >> input

        expect:
        controller.delete() == resultat
        controller.getFlash().message == message
        response.redirectedUrl == expectedUrl

        where:
        input | resultat | message                  | expectedUrl
        true  | true     | "event.deleted"          | "/event/list?filter=my"
        false | false    | "cannot.delete.event"    | "/event/list"

    }

    void "Test the index action returns the correct model"() {

        when: "The index action is executed"
        controller.index()

        then: "Redirection to list action"
        response.redirectedUrl == '/event/list'
    }

    void "Test the create action returns the correct model"() {

        when: "The create action is executed"
        controller.create()

        then: "The view is correctly displayed"
        model.eventInstance != null
        view == "/event/create"
    }

    @Unroll
    def "it should create a new event"() {
        given:
        GroovyMock(EventService, global: true)
        request.method = method

        and:
        (0..1) * EventService.saveEvent(_,_) >> result
        controller.create()

        expect:
        controller.getFlash().message == expectedMessage
        controller.getFlash().error == expectedError
        response.redirectedUrl == redirect // testing redirect
        view == redirectView // testing render
        (model.eventInstance == null) == modelReturn

        where:
        method | result  | expectedMessage  | expectedError             | redirect        | modelReturn | redirectView
        "POST" | true    | "event.created"  | null                      | "/event/list"   | true        | null
        "POST" | false   | null             | "event.creation.failed"   | null            | false       | "/event/create"
        "GET"  | false   | null             | null                      | null            | false       | "/event/create"
    }

    @Unroll
    def "it should redirect the user to the correct view"() {
        given:
        GroovyMock(EventService, global: true)
        controller.params.filter = filter

        and:
        controller.list()

        expect:
        view == path
        params == parameters

        where:
        filter      | path              | parameters
        "my"        | "/event/list"     | [filter:"my"]
        "involved"  | "/event/list"     | [filter:"involved"]
        "public"    | "/event/list"     | [filter:"public"]
        null        | "/event/list"     | [filter:null]
    }

    @Unroll
    def "it should edit an event"() {
        given:
        GroovyMock(EventService, global: true)
        GroovyMock(Event, global: true)
        Event event = Mock()
        request.method = method

        and:
        1 * EventService.isUserAllowedToEdit(_,_) >> isAllowed
        1 * Event.get(_) >> event
        (0..1) * event.getId(_) >> 1
        (0..1) * EventService.updateEvent(_) >> isUpdated
        controller.edit()

        expect:
        controller.getFlash().message == expectedMessage
        controller.getFlash().error == expectedError
        response.redirectedUrl == redirect // testing redirect
        view == redirectView
        (model.eventInstance == null) == modelReturn

        where:
        method | isUpdated | isAllowed |  expectedMessage   | expectedError              | redirectView       | modelReturn | redirect
        "POST" | true      | true      |  "event.edited"    | null                       | null               | true        | "/event/show"
        "POST" | false     | true      |  null              | "event.edition.failed"     | "/event/edit"      | false       | null
        "GET"  | true      | true      |  null              | null                       | "/event/edit"      | false       | null
        "POST" | true      | false     |  null              | "event.access.not.allowed" | null               | true        | "/event/list"
        "GET"  | true      | false     |  null              | "event.access.not.allowed" | null               | true        | "/event/list"
    }

    @Unroll
    def "Testing show an event" () {
        given:
        GroovyMock(EventService, global: true)
        GroovyMock(Event, global: true)
        Event event = Mock()

        and:
        response.reset()
        1 * EventService.isUserAllowedToSee(_,_) >> isAllowed
        1 * Event.get(_) >> event
        (0..1) * EventService.isUserGuest(_,_) >> true
        controller.show()

        expect:
        view == redirectView
        response.redirectedUrl == redirect // testing redirect
        (model.eventInstance == null) != modelEventReturn
        (model.isUserInvolved == null) != modelUserReturn
        flash.error == error

        where:
        isAllowed   | redirectView  | modelEventReturn | modelUserReturn | redirect      | error
        true        | "/event/show" | true             | true            | null          | null
        false       | null          | false            | false           | "/event/list" | "event.access.not.allowed"

    }

    @Unroll
    def "it should uninvolve a user"(){
        given:
        GroovyMock(InvolvedService , global:true)

        and:
        1 * InvolvedService.removeInvolvement(_,_) >> input
        controller.unInvolve()

        expect:
        response.reset()
        flash.message == expectedMessage
        flash.error == expactedError


        where:
        input 	| expectedMessage 		| expactedError
        true	| "uninvolve.success"	| null
        false	| null					| "uninvolve.failed"
    }


    @Unroll
    def "it should involve a user"(){
        given:
        GroovyMock(Event, global:true)
        GroovyMock(Car, global:true)
        GroovyMock(InvolvedService , global:true)
        Event event = Mock()

        controller.params.role = role
        controller.params.car = car

        and:
        1 * Event.get(_) >> event
        (0..1) * Car.get(_) >> new Car()
        1 * event.getPrivacy() >> privacy
        (0..1) * event.getId() >> 1
        (0..1) * InvolvedService.involve(_,_,_,_) >> true
        controller.getInvolved()

        expect:
        flash.message == expectedMessage
        flash.error == expactedError
        response.redirectedUrl == redirect

        where:
        role 		| car 		| privacy 		| expectedMessage 	 | expactedError 	 | redirect
        "GUEST" 	| 1         | Type.PUBLIC	| "involved.success" | null				 | "/event/show/1"
        "OWNER"	    | 1         | Type.PUBLIC	| "involved.success" | null				 | "/event/show/1"
        null		| 1         | Type.PUBLIC	| "involved.success" | null				 | "/event/show/1"
        null		| null		| Type.PUBLIC	| "involved.success" | null				 | "/event/show/1"
        "GUEST"	    | 1         | Type.PRIVATE	| null				 | "involved.failed" | "/event/list"
        "OWNER"		| 1         | Type.PRIVATE	| null				 | "involved.failed" | "/event/list"
        null		| 1         | Type.PRIVATE	| null				 | "involved.failed" | "/event/list"
        null		| null		| Type.PRIVATE	| null				 | "involved.failed" | "/event/list"
    }

    def "it should not involve a user"(){
        given:
        GroovyMock(Event, global:true)
        GroovyMock(InvolvedService , global:true)

        params.role = null
        params.car = null

        and:
        1 * Event.get(_) >> null
        controller.getInvolved()

        expect:
        flash.message == null
        flash.error == "involved.failed"
        response.redirectedUrl == "/event/list"
    }
}
