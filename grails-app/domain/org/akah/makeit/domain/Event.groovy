package org.akah.makeit.domain

import groovy.transform.EqualsAndHashCode


@EqualsAndHashCode
class Event {

    /**
     * The name of the event
     */
    String title
    /**
     * The address where it takes place
     */
    String address
    /**
     * The date when the event begins
     */
    Date dateBeginEvent
    /**
     * The ending date of the event
     */
    Date dateEndEvent
    /**
     * Small text describing the event
     */
    String description
    /**
     * Type of the event: Private, public ...
     */
    Type privacy

    @Override
    String toString() {
        return title
    }

    static constraints = {
        title(size:3..50, blank:false, nullable:false, matches: "^[a-zA-Z][a-zA-Z ]+", unique: true)
        address(size: 2..100, blank: false,nullable:false, matches: "^[a-zA-Z0-9][a-zA-Z0-9 ]+")
        dateBeginEvent(nullable:false, min: new Date())
        description(size: 2..300, blank: false, matches: "^[a-zA-Z0-9][a-zA-Z0-9 ]+")
        privacy(nullable: false, blank: false)
        dateEndEvent(nullable:false, min: new Date(), validator:{
            value, reference ->
                return value.after(reference.dateBeginEvent) // ensure that the event end after it begins
        })
    }
}
