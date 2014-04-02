package org.akah.makeit.tests.unit.domain

import org.akah.makeit.domain.Type
import spock.lang.Specification

/**
 * Created by Ben on 30/01/14.
 */

class TypeSpec extends Specification {
    def "test role enum"(){
        setup:
        def privateType = Type.PRIVATE
        def publicType = Type.PUBLIC


        expect:
        privateType.toString() == "Private"
        publicType.toString() == "Public"


        privateType.getKey() == "PRIVATE"
        publicType.getKey() == "PUBLIC"

    }
}
