package org.akah.makeit.tests.integration.controller

import org.akah.makeit.controllers.UserController
import org.akah.makeit.domain.User
import org.akah.makeit.services.GeneralService
import spock.lang.Specification

/**
 * Created by Ben on 09/02/14.
 */
class UserControllerIntegrationSpec extends Specification {

    UserController controller
    def setup(){
        controller = new UserController()
    }
    def "it should persist a new user"() {
        given:
        controller.params.loginName = "user1"
        controller.params.firstName = "toto"
        controller.params.secondName = "toto"
        controller.params.email = "login@univ.com"
        controller.params.password = "password"
        controller.params.confirm = "password"

        and:
        controller.create()

        expect:
        User.findByLoginName("user1")
    }

    def "it should not persist a user with a wrong password"() {
        given:
        controller.params.loginName = "user1"
        controller.params.firstName = "toto"
        controller.params.secondName = "toto"
        controller.params.email = "login@univ.com"
        controller.params.password = "password"
        controller.params.confirm = "pazzword"

        and:
        controller.create()

        expect:
        !User.findByLoginName("user1")
    }

    def "it should not persist a user with a short password"() {
        given:
        controller.params.loginName = "user1"
        controller.params.firstName = "toto"
        controller.params.secondName = "toto"
        controller.params.email = "login@univ.com"
        controller.params.password = "pass"
        controller.params.confirm = "pass"

        and:
        controller.create()

        expect:
        !User.findByLoginName("user1")
    }

    def "it should not persist a user with a blank password and a blank confirm"() {
        given:
        controller.params.loginName = "user1"
        controller.params.firstName = "toto"
        controller.params.secondName = "toto"
        controller.params.email = "login@univ.com"

        and:
        controller.create()

        expect:
        !User.findByLoginName("user1")
    }

    def "it should not persist a user with a blank password"() {
        given:
        controller.params.loginName = "user1"
        controller.params.firstName = "toto"
        controller.params.secondName = "toto"
        controller.params.email = "login@univ.com"
        controller.params.password = "password"

        and:
        controller.create()

        expect:
        !User.findByLoginName("user1")
    }

    def "it should not persist a user with a blank confirm"() {
        given:
        controller.params.loginName = "user1"
        controller.params.firstName = "toto"
        controller.params.secondName = "toto"
        controller.params.email = "login@univ.com"
        controller.params.confirm = "password"

        and:
        controller.create()

        expect:
        !User.findByLoginName("user1")
    }

    def "it should not successfully log in"() {
        given:
        controller.params.email = "login@univ.com"
        controller.params.password = "password"

        and:
        controller.login()

        expect:
        controller.session.user == null
    }

    def "it should successfully log in"() {
        given:
        def password = GeneralService.encrypt("password")
        def user = new User(loginName: "user3", firstName: "Toto", secondName: "toto", email: "login@univ.com", hashedPassword: password)
        user.save(flush: true)
        controller.params.email = "login@univ.com"
        controller.params.password = "password"

        and:
        controller.login()

        expect:
        controller.session.user != null
    }

    def "it should log out"() {
        given:
        controller.session.user = new User(loginName: "user3", firstName: "Toto", secondName: "toto", email: "login@univ.com", hashedPassword: "password")

        and:
        controller.logout()

        expect:
        controller.session.user == null
    }

    def "it should confirm the user is already logged"() {
        given:
        controller.session.user = new User(loginName: "user3", firstName: "Toto", secondName: "toto", email: "login@univ.com", hashedPassword: GeneralService.encrypt("password"))

        and:
        controller.isUserLoggedIn()

        expect:
        GeneralService.userIsLoggedIn(controller.session.user) == true
    }

    def "it should edit a user password"() {
        given:
        def user = new User(loginName: "user3", firstName: "Toto", secondName: "toto", email: "login@univ.com", hashedPassword: GeneralService.encrypt("password"))
        user.save(flush: true)
        controller.session.user = user
        controller.params.current = "password"
        controller.params.password = "pazzword"
        controller.params.confirm = "pazzword"

        and:
        controller.edit(controller.params)

        expect:
        User.findByLoginName("user3").hashedPassword == GeneralService.encrypt("pazzword")
    }

    def "it should not edit a user password because of missing confirm param"() {
        given:
        def user = new User(loginName: "user3", firstName: "Toto", secondName: "toto", email: "login@univ.com", hashedPassword: GeneralService.encrypt("password"))
        user.save(flush: true)
        controller.session.user = user
        controller.params.current = "password"
        controller.params.password = "pazzword"

        and:
        controller.edit(controller.params)

        expect:
        User.findByLoginName("user3").hashedPassword != GeneralService.encrypt("pazzword")
    }

    def "it should not edit a user password because of missing current param"() {
        given:
        def user = new User(loginName: "user3", firstName: "Toto", secondName: "toto", email: "login@univ.com", hashedPassword: GeneralService.encrypt("password"))
        user.save(flush: true)
        controller.session.user = user
        controller.params.password = "pazzword"

        and:
        controller.edit(controller.params)

        expect:
        User.findByLoginName("user3").hashedPassword == GeneralService.encrypt("password")
    }

    def "it should not edit a user password because of wrong current param"() {
        given:
        def user = new User(loginName: "user3", firstName: "Toto", secondName: "toto", email: "login@univ.com", hashedPassword: GeneralService.encrypt("password"))
        user.save(flush: true)
        controller.session.user = user
        controller.params.current = "pazzword"

        and:
        controller.edit(controller.params)

        expect:
        User.findByLoginName("user3").hashedPassword == GeneralService.encrypt("password")
    }
}
