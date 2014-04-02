package org.akah.makeit.tests.unit.domain

import grails.test.mixin.TestFor
import org.akah.makeit.domain.Event
import org.akah.makeit.domain.Type
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(Event)
class EventSpec extends Specification {

    @Unroll
    def "constraints test"() {
        setup:
        def event = new Event(title: title, address: address, dateBeginEvent: dateBeginEvent,
                dateEndEvent: dateEndEvent, description: description, privacy: Type.PUBLIC)

        event.validate()

        expect:
        event.hasErrors() == !valid

        where:
        title       | address       | dateBeginEvent        | dateEndEvent      | description         | valid
        "title"     | "address"     | new Date() +1         | new Date() +2     | "description"       | true
        "a"         | "address"     | new Date() +1         | new Date() +2     | "description"       | false
        "title"     | "a"           | new Date() +1         | new Date() +2     | "description"       | false
        "title"     | "address"     | new Date() +1         | new Date() +2     | "d"                 | false
        "title"     | "address"     | new Date()            | new Date() +2     | "description"       | false
        "title"     | "address"     | new Date() +1         | new Date()        | "description"       | false
        "title"     | "address"     | new Date() +2         | new Date() +1     | "description"       | false
    }

    def "toString test"() {
        setup:
        def event = new Event(title: title, address: address, dateBeginEvent: dateBeginEvent,
                dateEndEvent: dateEndEvent, description: description, privacy: Type.PUBLIC)

        event.validate()

        expect:
        event.toString() == valid

        where:
        title           | address       | dateBeginEvent        | dateEndEvent      | description         | valid
        "THis"          | "address"     | new Date() +1         | new Date() +2     | "description"       | "THis"
        "mUst"          | "address"     | new Date() +1         | new Date() +2     | "description"       | "mUst"
        "priNT"         | "address"     | new Date() +1         | new Date() +2     | "description"       | "priNT"
        "CoReCtLy"      | "address"     | new Date() +1         | new Date() +2     | "description"       | "CoReCtLy"
    }
}
