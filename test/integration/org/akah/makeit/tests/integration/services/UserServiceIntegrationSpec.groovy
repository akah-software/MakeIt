package org.akah.makeit.tests.integration.services

import org.akah.makeit.domain.*
import org.akah.makeit.services.UserService
import spock.lang.Specification

/**
 * Created by Ben on 11/02/14.
 **/
class UserServiceIntegrationSpec extends Specification{
    public static final REDIRECT_CONTROLLER = "RDirController"
    public static final REDIRECT_ACTION = "RDirAction"
    public static final USER = "user"

    def userTest
    def userTest2

    def setup() {
        userTest = new User(loginName: "login", firstName: "fname", secondName: "sname", email: "ben@gmail.com", hashedPassword: "whocares").save()
        userTest2 = new User(loginName: "login2", firstName: "fname2", secondName: "sname2", email: "ben2@gmail.com", hashedPassword: "whocares2")
    }

    def "bad user not saved"() {
        given: "A user without params"

        and: "We try to persist this user"
        UserService.save(userTest2)

        expect: "A failure"
        User.findByLoginName("login2") == null
    }

    def "good user saved"() {
        given:
        userTest = new User(loginName: "user3", firstName: "Toto", secondName: "toto", email: "login@univ.com", hashedPassword: "password").save()
        UserService.save(userTest)

        expect: "A success"
        User.findByLoginName("login") != null
    }

    def "bad user not edited"() {
        given:
        userTest.email = "mail"
        userTest.discard() // otherwise grails saves it imediatly !!

        and:
        UserService.editUser(userTest)

        expect:
        User.findByLoginName("login").email == "ben@gmail.com"
    }

    def "good user edited"() {
        given:
        userTest.email = "user3@univ-tlse3.fr"
        userTest.discard() // otherwise grails saves it imediatly !!

        and:
        UserService.editUser(userTest)

        expect:
        User.findByLoginName("login").email == "user3@univ-tlse3.fr"
    }

    def "delete without relations"() {
        given:

        def session = new HashMap<String,String>()
        session[REDIRECT_CONTROLLER] = null
        session[REDIRECT_ACTION] = null
        session[USER] = userTest

        and:
        UserService.delete(userTest, session)

        expect:
        User.findByLoginName("login") == null
    }

    def "delete with relations"() {
        given:
        def event = new Event(title: "title", address: "address", dateBeginEvent: new Date() +1,
                dateEndEvent: new Date() +2, description: "description", privacy: Type.PUBLIC)
        event.save(flush: true)
        def involved = new Involved(role: Role.OWNER, user: userTest, event: event)
        involved.save(flush: true)
        def car = new Car(owner: userTest, name: "myCar", capacity: 2, availableSeats: 1)
        car.save(flush: true)

        def session = new HashMap<String,String>()
        session[REDIRECT_CONTROLLER] = null
        session[REDIRECT_ACTION] = null
        session[USER] = userTest

        and:
        UserService.delete(userTest, session)

        expect:
        User.findByLoginName("login") == null
    }
}
