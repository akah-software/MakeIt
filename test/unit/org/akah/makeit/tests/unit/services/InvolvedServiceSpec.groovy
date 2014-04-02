package org.akah.makeit.tests.unit.services

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.akah.makeit.domain.Car
import org.akah.makeit.domain.Event
import org.akah.makeit.domain.Involved
import org.akah.makeit.domain.Role
import org.akah.makeit.domain.User
import org.akah.makeit.services.InvolvedService
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(InvolvedService)
@Mock([User, Event, Involved, Car])
class InvolvedServiceSpec extends Specification {

    @Unroll
    def "it should check constraints properly"(){
        given:
        User user = Mock()
        Involved involve = Mock()
        Car car = Mock()

        // here the problem is that The mocked object returns allways the default value
        // so even for dirver false you must specify one that won't be null ...
        and:
        1 * involve.getRole() >> role
        (0..1) * involve.getCar() >> car
        (0..1) * car.getOwner() >> user
        (0..1) * involve.getUser() >> null

        expect:
        InvolvedService.checkConstraints(involve) == expectedResult

        where:
        role            | expectedResult
        Role.GUEST      | true
        Role.OWNER      | false
        Role.DRIVER     | false
    }

    def "it should check driver constraints properly"(){
        given:
        User user = Mock()
        Involved involve2 = Mock()
        Car car = Mock()

        and:
        1 * involve2.getRole() >> Role.DRIVER
        1 * involve2.getCar() >> car
        1 * involve2.getUser() >> user
        1 * car.getOwner() >> user

        expect:
        InvolvedService.checkConstraints(involve2) == true
    }

    def "it should not save a user that does not fit constraints"() {
        given: "A mocked invovled"
        Involved involved = Mock() // a good user is sent

        and: "That is supposed no to save correctly"
        1 * involved.save(_) >> null

        expect:
        !InvolvedService.saveInvolved(involved)
    }

    def "it should persist an involved"() {
        given: "A mocked invovled"
        Involved involved = Mock() // a good user is sent

        and: "That is supposed no to save correctly"
        1 * involved.save(_) >> involved

        expect:
        InvolvedService.saveInvolved(involved)
    }

    @Unroll
    def "it should involve a user"() {
        given: "A mocked invovled"
        Involved involved = Mock() // a good user is sent

        and:
        (0..1) * involved.save(_) >> saveReturn
        1 * involved.getRole() >> role

        expect:
        InvolvedService.involve(involved) == expectedResult

        where:
        role            | saveReturn            | expectedResult
        Role.GUEST      | new Involved()        | true
        Role.OWNER      | new Involved()        | false
        Role.GUEST      | null                  | false
        Role.OWNER      | null                  | false
    }

    def "it should remove an involvement"() {
        given: "A global mocked invovled"
        GroovyMock(Involved, global: true)
        Involved involved = Mock() // a good user is sent

        and:
        (0..1) * involved.delete() >> true
        (0..1) * Involved.findByUserAndEvent(_,_) >> involved

        expect:
        InvolvedService.removeInvolvement(1,new User())
    }
}