package org.akah.makeit.tests.unit.domain

import org.akah.makeit.domain.Role
import spock.lang.Specification

class RoleSpec extends Specification {
    def "test role enum"(){
        setup:
            def owner = Role.OWNER
            def guest = Role.GUEST
            def driver = Role.DRIVER

        expect:
            owner.toString() == "owner"
            guest.toString() == "guest"
            driver.toString() == "driver"

            owner.getKey() == "OWNER"
            guest.getKey() == "GUEST"
            driver.getKey() == "DRIVER"
    }
}
