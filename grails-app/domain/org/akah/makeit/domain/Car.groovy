package org.akah.makeit.domain

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class Car {
    String name
    int capacity // include driver and passengers
    int availableSeats
    User owner

    @Override
    String toString() {
        return this.name
    }

    static constraints = {
        owner(blank: false, nullable: false)
        name(blank: false, nullable: false)
        capacity(range: 2..7, blank: false, nullable: false)
        availableSeats(validator:{
            value, reference ->
                return value < reference.capacity //ensure that the number of available seats is lesser than the car capacity
        })
    }
}
