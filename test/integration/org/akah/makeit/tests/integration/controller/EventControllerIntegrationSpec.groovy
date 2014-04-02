package org.akah.makeit.tests.integration.controller

import org.akah.makeit.controllers.EventController
import org.akah.makeit.domain.Event
import org.akah.makeit.domain.Involved
import org.akah.makeit.domain.Role
import org.akah.makeit.domain.Type
import org.akah.makeit.domain.User
import org.akah.makeit.services.GeneralService
import spock.lang.Specification

/**
 * Created by Ben on 09/02/14.
 */
class EventControllerIntegrationSpec  extends Specification{

    EventController controller
    def setup(){
        controller = new EventController()
    }

    def "it should create a new event"() {
        given:
        def user = new User(loginName: "user3", firstName: "Toto", secondName: "toto", email: "login@univ.com", hashedPassword: GeneralService.encrypt("password"))
        user.save(flush: true)

        controller.session.user = user
        controller.params.title = "title"
        controller.params.address = "address"
        controller.params.dateBeginEvent = new Date() + 1
        controller.params.dateEndEvent = new Date() + 2
        controller.params.description = "description"
        controller.params.privacy = Type.PUBLIC

        and:
        controller.create()

        expect:
        Event.findByTitle("title")
    }

    /*@Unroll
    def "it should return the correct model"() {
        given:
        def user = new User(loginName: "user3", firstName: "Toto", secondName: "toto", email: "login@univ.com", hashedPassword: GeneralService.encrypt("password"))
        user.save(flush: true)
        controller.session.user = user
        controller.params.filter = filter

        and:
        controller.list()

        expect:
        model.eventList == eventList

        where:
        filter      | eventList
        "my"        | EventService.findUsersEvents(user)
        "involved"  | EventService.findWhereUserIsInvolved(user)
        "public"    | EventService.findPublicEvents()
        null        | EventService.findPublicEvents()
    }*/

    def "delete an know event" () {
        given:
        def dUser = new User(loginName: "Login42", firstName: "Myname", secondName: "Boomer", email: "mail@cortte.com", hashedPassword: GeneralService.encrypt("passwordindechifrable"))
        dUser.save(flush: true)

        def dEvent = new Event(title: "title", address: "address", dateBeginEvent: new Date() +1,
                dateEndEvent: new Date() +2, description: "description", privacy: Type.PUBLIC)
        dEvent.save(flush: true)

        def dInvolved = new Involved(role: Role.OWNER ,event: dEvent,car: null, user: dUser)
        dInvolved.save(flush: true)

        controller.session.user = dUser
        controller.params.id = dEvent.getId()

        and:
        controller.delete()

        expect:
        !Event.get(dEvent.getId())
    }
}
