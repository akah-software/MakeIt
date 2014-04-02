package org.akah.makeit.tests.unit.services

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.akah.makeit.domain.Car
import org.akah.makeit.domain.Event
import org.akah.makeit.domain.Involved
import org.akah.makeit.domain.Role
import org.akah.makeit.domain.Type
import org.akah.makeit.domain.User
import org.akah.makeit.services.GeneralService
import org.akah.makeit.services.UserService
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(UserService)
@Mock([User, Event, Involved, Car])
class UserServiceSpec extends Specification {
    public static final REDIRECT_CONTROLLER = "RDirController"
    public static final REDIRECT_ACTION = "RDirAction"
    public static final USER = "user"
    public static final LOGIN_ATTEMPTS = "loginAttempts"
    public static final LAST_LOGIN_ATTEMPT = "lastLoginAttempt"

    def "it should confirm that the edited password is equals to the current one"() {
        given:
        GroovyMock(GeneralService, global:true)

        and: "We try to know if two calls of password hashing returns the same value"
        2 * GeneralService.encrypt(_) >> "password"

        expect: "Both passwords are equals"
        UserService.checkEditPassword("password", "password")
        !UserService.checkEditPassword("fdgfd", "pass")
    }

    @Unroll
    def "it should expect a non null password param to ensure password checking"() {
        setup:
        def success = UserService.checkPasswords(password, confirm)

        expect:
        success == expectedResult

        where:
        password    | confirm       | expectedResult
        "password"  | "password"    | true
        null        | "password"    | false
        "password"  | null          | false
        "pazzword"  | "password"    | false
        "pass"      | "password"    | false
        "password"  | "pass"        | false
        "pass"      | "pass"        | false
    }

    def "it should not save a user that does not fit constraints"() {
        given: "A mocked user"
        User user = Mock() // a good user is sent

        and: "That is supposed to save badly"
        1 * user.save(_) >> null

        expect: "We fail"
        !UserService.save(user)
    }

    def "it should  save a user that fits constraints"() {
        given: "A mocked user"
        User user = Mock() // a good user is sent

        and: "That is supposed to save correctly"
        1 * user.save(_) >> user

        expect: "We success"
        UserService.save(user)
    }

    def "it should not merge a persisted user if changes break constraints"() {
        given: "A mocked user"
        User user = Mock() // a good user is sent

        and: "That is supposed to merge badly"
        1 * user.merge(_) >> null

        expect: "We fail"
        !UserService.editUser(user)
    }

    def "it should successfully merge a persisted user"() {
        given: "A mocked user"
        User user = Mock() // a good user is sent

        and: "That is supposed to merge correctly"
        1 * user.merge(_) >> user

        expect: "We success"
        UserService.editUser(user)
    }

    def "it should delete a user without relations"() {
        given: "A mocked user"
        User user = Mock() // a good user is sent
        GroovyMock(Involved, global: true)
        GroovyMock(Car, global: true)

        def session = new HashMap<String,String>()
        session[REDIRECT_CONTROLLER] = null
        session[REDIRECT_ACTION] = null
        session[USER] = user

        and: "That is supposed to merge correctly"
        1 * user.delete(_) >> null // does not matter as long as the exception is not thrown
        1 * Involved.findAllWhere(_) >> []
        1 * Car.findAllWhere(_) >> []

        expect: "We success"
        UserService.delete(user, session)
    }

    def "it should delete a user and all his references"() {
        given: "A mocked user"
        User user = Mock() // a good user is sent
        GroovyMock(Involved, global: true)
        GroovyMock(Car, global: true)
        Involved involved = Mock()
        Car car = Mock()

        def session = new HashMap<String,String>()
        session[REDIRECT_CONTROLLER] = null
        session[REDIRECT_ACTION] = null
        session[USER] = user

        and: "That is supposed to merge correctly"
        1 * user.delete(_) >> null // does not matter as long as the exception is not thrown
        1 * Involved.findAllWhere(_) >> [involved]
        1 * involved.delete(_) >> null
        1 * Car.findAllWhere(_) >> [car]
        1 * car.delete(_) >> null

        expect: "We success"
        UserService.delete(user, session)
    }

    def "it should increment login failure"() {
        given:
        def session = new HashMap<String,String>()
        session[LOGIN_ATTEMPTS] = 2

        and:
        UserService.recordLoginFailure(session)

        expect:
        session[LOGIN_ATTEMPTS] == 3
    }

    def "it should avoid to retry a connection"() {
        given:
        def session = new HashMap<String, String>()
        session[LAST_LOGIN_ATTEMPT] = new Date()
        session[LOGIN_ATTEMPTS] = 5

        and:
        def success = UserService.checkLoginAttempts(session)

        expect:
        !success
    }

    def "it should allow to retry a connection"() {
        given:
        def session = new HashMap<String, String>()
        session[LAST_LOGIN_ATTEMPT] = new Date() - 1
        session[LOGIN_ATTEMPTS] = 5

        and:
        def success = UserService.checkLoginAttempts(session)

        expect:
        success
    }

    @Unroll
    def "it should log the user out"() {
        setup:
        def session = new HashMap<String, String>()
        session[REDIRECT_CONTROLLER] = ctrl
        session[REDIRECT_ACTION] = action
        session[USER] = user

        expect:
        UserService.logUserOut(session) == expectedResult
        session[REDIRECT_CONTROLLER] == ctrlResult
        session[REDIRECT_ACTION] == actionResult
        session[USER] == null

        where:
        ctrl   | action   | user   | expectedResult | ctrlResult  | actionResult
        "ctrl" | "action" | "toto" | true           | null          | null
        null   | "action" | "toto" | true           | null          | null
        ""     | null     | "toto" | true           | null          | null
        "ctrl" | "action" | null   | false          | "ctrl"        | "action"
    }
}