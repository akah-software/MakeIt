package org.akah.makeit.tests.unit.controllers

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.akah.makeit.controllers.UserController
import org.akah.makeit.domain.User
import org.akah.makeit.services.GeneralService
import org.akah.makeit.services.UserService
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(UserController)
@Mock([User])
class UserControllerSpec extends Specification {

    def "it should not have any session"() {
        setup:
        controller.index()

        expect:
        controller.session.user == null
    }

    def "it should redirect on login page"() {
        setup:
        controller.myAccount()

        expect:
        controller.getFlash().message == "app.login.first"
    }

    def "it should persist a new user"() {
        given:
        GroovyMock(UserService, global:true)
        GroovyMock(GeneralService, global:true)
        controller.request.method="POST"

        and:
        1 * UserService.checkPasswords(_,_) >> true
        1 * UserService.save(_) >> true
        1 * GeneralService.encrypt(_) >> "ramdomPassword"
        controller.create()

        expect:
        controller.getFlash().message == "user.created"
        response.redirectedUrl == "/user/login"
    }

    def "it should not persist a user with a wrong password"() {
        given:
        GroovyMock(UserService, global:true)
        GroovyMock(GeneralService, global:true)
        controller.request.method="POST"

        and:
        1 * UserService.checkPasswords(_,_) >> true
        1 * UserService.save(_) >> false
        1 * GeneralService.encrypt(_) >> "ramdomPassword"
        controller.create()

        expect:
        controller.getFlash().error == "user.creation.failed"
        response.redirectedUrl == "/user/create"
        // model.userInstance != null not in the specs
    }

    def "it should not persist a user with  wrong constraints"() {
        given:
        GroovyMock(UserService, global:true)
        controller.request.method="POST"
        controller.session.user= "blabla"

        and:
        1 * UserService.checkPasswords(_,_) >> false
        controller.create()

        expect:
        controller.getFlash().error == "user.password.checking.failed"
        view == "/user/create"
        // model.userInstance != null not in the specs
    }

    @Unroll
    def "it should log in properly"() {
        given:
        GroovyMock(UserService, global:true)
        GroovyMock(GeneralService, global:true)
        GroovyMock(User, global:true)
        request.method = requestMethod

        and:
        (0..1) * UserService.checkLoginAttempts(_) >> canLogin
        (0..1) * User.findByEmailAndHashedPassword(_,_) >> userResult
        (0..1) * GeneralService.redirectToCaller(_) >> "/user/create"
        controller.login()

        expect:
        controller.getFlash().error == errorMessage
        controller.getFlash().message == message
        view == viewResult
        response.redirectedUrl == redirect

        where:
        requestMethod | canLogin | userResult | errorMessage                            | message               | viewResult      | redirect
        "POST"        | false    | null       | "Too many login attempts. Please wait." | null                  | null            | "/user/login"
        "POST"        | true     | null       | "app.login.error"                       | null                  | null            | "/user/login"
        "GET"         | true     | null       | null                                    | null                  | "/user/login"   | null
        "POST"        | true     | "random"   | null                                    | "app.login.success"   | null            | "/user/create"
    }

    @Unroll
    def "it should log out"() {
        given:
        GroovyMock(UserService, global:true)
        controller.session.user = new User(loginName: "user3", firstName: "Toto", secondName: "toto", email: "login@univ.com", hashedPassword: "password")

        and:
        1 * UserService.logUserOut(_) >> returnLogout

        controller.logout()

        expect:
        controller.getFlash().message == message
        response.redirectedUrl == "/"

        where:
        message              | returnLogout
        "app.logout.success" | true
        "app.logout.failed"  | false
    }

    @Unroll
    def "it should confirm the user is already logged"() {
        given:
        GroovyMock(GeneralService, global:true)
        controller.session.user = new User(loginName: "user3", firstName: "Toto", secondName: "toto", email: "login@univ.com", hashedPassword: "password")

        and:
        1 * GeneralService.userIsLoggedIn(_) >> returnLogout
        (0..1) * GeneralService.redirectToCaller(_) >> "/"
        controller.isUserLoggedIn()

        expect:
        controller.getFlash().message == message
        response.redirectedUrl == redirect

        where:
        message              | returnLogout | redirect
        "app.already.logged" | true         | "/"
        null                 | false        | null
    }

    @Unroll
    def "it should edit a user password"() {
        given:
        User user = Mock()
        GroovyMock(GeneralService, global:true)
        GroovyMock(UserService, global:true)
        controller.session.user = user
        controller.params.current = currentPassword
        controller.params.password = password

        and:
        (0..1) * UserService.checkEditPassword(_,_) >> checkEditPassword
        (0..1) * UserService.checkPasswords(_,_) >> checkPassword
        (0..1) * GeneralService.encrypt(_) >> "ramdomPassword"
        (0..1) * UserService.editUser(_) >> editResult
        controller.edit(controller.params)

        expect:
        controller.getFlash().error == errorMessage
        controller.getFlash().message == message
        view == viewResult
        response.redirectedUrl == redirect

        where:
        checkEditPassword | checkPassword | editResult | currentPassword | password   | errorMessage                    | message        | viewResult          | redirect
        true              | true          | true       | "whatever"      | "whatever" | null                            | "user.edited"  | null                | "/user/myAccount"
        true              | true          | true       | null            | null       | null                            | "user.edited"  | null                | "/user/myAccount"
        true              | true          | false      | "whatever"      | "whatever" | "user.edit.failed"              | null           | null                | "/user/myAccount?edit=enabled"
        true              | true          | false      | null            | null       | "user.edit.failed"              | null           | null                | "/user/myAccount?edit=enabled"
        false             | true          | true       | "whatever"      | "whatever" | "user.invalid.password"         | null           | "/user/myAccount"   | null
        true              | false         | true       | "whatever"      | "whatever" | "user.password.checking.failed" | null           | "/user/myAccount"   | null
        true              | true          | true       | null            | "whatever" | "user.edit.nopassword.error"    | null           | null                | "/user/myAccount?edit=enabled"

    }
}
