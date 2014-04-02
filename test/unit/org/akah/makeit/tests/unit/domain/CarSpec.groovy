package org.akah.makeit.tests.unit.domain

import grails.test.mixin.TestFor
import org.akah.makeit.domain.Car
import org.akah.makeit.domain.User
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Car)
class CarSpec extends Specification {

    def "car constraints"() {
        setup:
        def user = new User(loginName: "login", firstName: "firstName", secondName: "secondName", email: "email@email.com", hashedPassword: "password")
        def car = new Car(owner: user, name: name, capacity: capacity, availableSeats: availableSeats)
        car.validate()

        expect:
        car.hasErrors() == !valid

        where:
        name    | capacity    | availableSeats  | valid
        "Car"   | 2           | 0               | true
        "Car"   | 2           | 1               | true
        "Car"   | 2           | 2               | false
        "Car"   | 7           | 6               | true
        "Car"   | 8           | 6               | false
        ""      | 2           | 1               | false
        null    | 2           | 1               | false
        "Car"   | null        | 1               | false
        "Car"   | 2           | null            | false
    }

    def "toString test"() {
        setup:
        def user = new User(loginName: "login", firstName: "firstName", secondName: "secondName", email: "email@email.com", hashedPassword: "password")
        def car = new Car(owner: user, name: name, capacity: 2, availableSeats: 1)
        car.validate()

        expect:
        car.toString() == valid

        where:
        name                        | valid
        "THis"                      | "THis"
        "mUst"                      | "mUst"
        "priNT"                     | "priNT"
        "CoReCtLy"                  | "CoReCtLy"
    }
}
