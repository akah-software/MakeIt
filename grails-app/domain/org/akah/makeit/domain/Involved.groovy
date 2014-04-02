package org.akah.makeit.domain

import groovy.transform.EqualsAndHashCode


@EqualsAndHashCode
class Involved {
    Car car
    String meetingPoint
    Role role
    User user
    Event event

    @Override
    String toString() {
        return "User "+this.user+" is a "+this.getRole()+" in "+this.event+"."
    }

    static constraints = {
        user (nullable: false, blank:false)
        event (nullable: false, blank: false)
        car (nullable: true, blank: true) // car is nullable or blank when a user is coming with no car :)
        meetingPoint(nullable: true, blank: true)
        role(nullable : false, blank: false)
        // TODO : Conditional constraint according to ROLE (Role.DRIVER must have a non null car etc.)
    }

    static mapping = {
        role sqlType: 'varchar'
    }
}
