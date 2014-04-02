package org.akah.makeit.tests.unit.domain

import grails.test.mixin.TestFor
import org.akah.makeit.domain.User
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(User)
class UserSpec extends Specification {

    @Unroll
    def "user constraints"() {
        setup:
        def user = new User(loginName: loginName, firstName: firstName, secondName: secondName, email: email, hashedPassword: hashedPassword)
        user.validate()

        expect:
        user.hasErrors() == !valid

        where:
        loginName               | firstName                 | secondName                | email             | hashedPassword    | valid
        "moreThan15Characters"  | "firstName"               | "secondName"              | "email@email.com" | "password"        | false
        "èèèè"                  | "firstName"               | "secondName"              | "email@email.com" | "password"        | false
        "a"                     | "firstName"               | "secondName"              | "email@email.com" | "password"        | false
        ""                      | "firstName"               | "secondName"              | "email@email.com" | "password"        | false
        null                    | "firstName"               | "secondName"              | "email@email.com" | "password"        | false
        "login"                 | "firstName"               | "secondName"              | "email@email.com" | "password"        | true
        "login"                 | "0firstName"              | "secondName"              | "email@email.com" | "password"        | false
        "login"                 | "moreThanTwentyCharacters"| "secondName"              | "email@email.com" | "password"        | false
        "login"                 | ""                        | "secondName"              | "email@email.com" | "password"        | false
        "login"                 | "a"                       | "secondName"              | "email@email.com" | "password"        | false
        "login"                 | "firstName"               | "0secondName"             | "email@email.com" | "password"        | false
        "login"                 | "firstName"               | "moreThanTwentyCharacters"| "email@email.com" | "password"        | false
        "login"                 | "firstName"               | ""                        | "email@email.com" | "password"        | false
        "login"                 | "firstName"               | "a"                       | "email@email.com" | "password"        | false
        "login"                 | "firstName"               | "secondName"              | "email"           | "password"        | false
        "login"                 | "firstName"               | "secondName"              | ""                | "password"        | false
        "login"                 | "firstName"               | "secondName"              | null              | "password"        | false
        "login"                 | "firstName"               | "secondName"              | "email@email.com" | ""                | false
        "login"                 | "firstName"               | "secondName"              | "email@email.com" | null              | false
    }

    def "user persistence"() {
        setup:
        def users = [
                new User(loginName: "user1", firstName: "firstName", secondName: "secondName", email: "user1@email.com", hashedPassword: "password"),
                new User(loginName: "user2", firstName: "firstName", secondName: "secondName", email: "user2@email.com", hashedPassword: "password")]
        users*.save(flush: true)

        expect:
        assertEquals 2, User.list().size()
    }

    def "user persistence with same loginName"() {
        setup:
        def users = [
                new User(loginName: "user3", firstName: "Toto", secondName: "toto", email: "toto@toto.com", hashedPassword: "password"),
                new User(loginName: "user3", firstName: "Oncle", secondName: "Picsou", email: "oncle@picsou.com", hashedPassword: "password")]
        users*.save(flush: true)
        expect:
        assertEquals 1, User.list().size()
    }

    def "user persistence with same email"() {
        setup:
        def users = [
                new User(loginName: "user4", firstName: "Jean", secondName: "Marc", email: "toto@toto.com", hashedPassword: "password"),
                new User(loginName: "user5", firstName: "Paul", secondName: "Pierre", email: "toto@toto.com", hashedPassword: "password")]
        users*.save(flush: true)
        expect:
        assertEquals 1, User.list().size()
    }
}
