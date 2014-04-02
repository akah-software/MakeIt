package org.akah.makeit.tests.unit.domain

import grails.test.mixin.TestFor
import org.akah.makeit.domain.Event
import org.akah.makeit.domain.Involved
import org.akah.makeit.domain.Role
import org.akah.makeit.domain.Type
import org.akah.makeit.domain.User
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(Involved)
class InvolvedSpec extends Specification {

    @Unroll
    def "toString test"() {
        setup:
        def owner = new User(loginName: lname, firstName: "firstName", secondName: "secondName",
                email: "email@email.com", hashedPassword: "password")
        def event = new Event(title: title, address: "address", dateBeginEvent: new Date() +1,
                dateEndEvent: new Date() +2, description: "description", privacy: Type.PUBLIC)
        def involved = new Involved(role: role,event: event,car: null, user: owner, meetingPoint: "here is the point")

        involved.validate()

        expect:
        involved.toString() == valid

        where:
        lname               | title                     | role                      | valid
        "Benjamin"          | "Soiree de fous"          | Role.OWNER                | "User Benjamin is a owner in Soiree de fous."
        "Laurent"           | "Soiree de malades"       | Role.GUEST                | "User Laurent is a guest in Soiree de malades."
        "Jordan"            | "Soiree de Timbres"       | Role.DRIVER               | "User Jordan is a driver in Soiree de Timbres."
        "Akhram"            | "Soiree tranquille"       | Role.GUEST                 | "User Akhram is a guest in Soiree tranquille."

    }
}
