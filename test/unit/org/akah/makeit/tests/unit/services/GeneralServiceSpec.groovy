package org.akah.makeit.tests.unit.services

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.akah.makeit.domain.User
import org.akah.makeit.services.GeneralService
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(GeneralService)
@Mock([User])
class GeneralServiceSpec extends Specification {

    def "test getTime"(){
        setup:
            println "Time: "+GeneralService.getTime()
            Date dateToTest = GeneralService.getTime()
            Date compareDate = Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTime()

        expect:
            dateToTest.before(compareDate) || dateToTest.equals(compareDate) // 1st time is generated few milliseconds before the second
            compareDate.before(dateToTest+1) // but this is not really a lot ...
    }

    def "test encrypt"(){
        expect:
            GeneralService.encrypt(passOne) == GeneralService.encrypt(passTwo)
            GeneralService.encrypt(passOne) != GeneralService.encrypt(passTwoBis)
        where:
            passOne         | passTwo       | passTwoBis
            "Pa55w0rd"      | "Pa55w0rd"    | "pasxword"
            "password"      | "password"    | "Pa56w0rd"
            "123456"        | "123456"      | "123656"
    }

    def "test that a user is logged in"(){
        setup:
        def userTest = new User(loginName: "login", firstName: "fname", secondName: "sname", email: "ben@gmail.com", hashedPassword: "whocares")
        expect:
        GeneralService.userIsLoggedIn(userTest) == true
        GeneralService.userIsLoggedIn(null) == false
    }

    def "test redirection with authentication"(){
        setup:
            def userTest = new User(loginName: "login", firstName: "fname", secondName: "sname", email: "ben@gmail.com", hashedPassword: "whocares").save()
            // simulate session attribute
            def session = new HashMap<String,String>();
            def session2 = new HashMap<String,String>();
            session.user = userTest
            session2.user = null

        expect:
            GeneralService.authenticateWithRedirection(session,"titi","toto") == true
            GeneralService.authenticateWithRedirection(session2,"titi","toto") == false
    }

    def "test redirect to caller"(){
        setup:
            def session = new HashMap<String,String>();
            session[GeneralService.REDIRECT_CONTROLLER] = ctrl
            session[GeneralService.REDIRECT_ACTION] = act

        expect:
            GeneralService.redirectToCaller(session) == result
            session[GeneralService.REDIRECT_CONTROLLER] == null
            session[GeneralService.REDIRECT_ACTION] == null

        where:
            ctrl       | act    | result
            "toto"     | "titi" | "/toto/titi/"
            null       | null   | "/"
    }
}
