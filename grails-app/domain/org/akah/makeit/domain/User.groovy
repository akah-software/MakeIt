package org.akah.makeit.domain

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class User {

    /**
     * User login name, the one that's shown to everybody
     */
    String loginName
    /**
     * First name of the user
     */
    String firstName
    /**
     * User's second name
     */
    String secondName
    /**
     * User's email address
     */
    String email
    /**
     * User password (hashed)
     */
    String hashedPassword

    @Override
    String toString() {
        return loginName
    }

    static constraints = {
        loginName(size: 2..15, blank: false, matches: "[a-zA-Z0-9]+", unique: true)
        firstName(size: 2..20, blank: false, matches: "[a-zA-Z]+")
        secondName(size: 2..20, blank: false, matches: "[a-zA-Z]+")
        email(email:true, nullable:false, blank: false, unique: true)
        hashedPassword(nullable:false, blank: false)
    }

    // transient means that it won't be save in the database
    // static transients = ['password','confirm']
}
