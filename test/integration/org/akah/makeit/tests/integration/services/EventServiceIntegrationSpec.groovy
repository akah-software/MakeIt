package org.akah.makeit.tests.integration.services

import org.akah.makeit.domain.*;
import org.akah.makeit.services.EventService
import spock.lang.Specification;


/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */

class EventServiceIntegrationSpec extends Specification {

    def userTest
    def userTest2
    def userTest3

    def eventTest
    def eventTest2
    def eventTest3
    def eventTest4

    def setup() {
        userTest = new User(loginName: "login", firstName: "fname", secondName: "sname", email: "ben@gmail.com", hashedPassword: "whocares").save()
        userTest2 = new User(loginName: "logindeux", firstName: "fnamedeux", secondName: "snamedeux", email: "bendeux@gmail.com", hashedPassword: "whocaresdeux").save()
        userTest3 = new User(loginName: "logintrois", firstName: "fnametrois", secondName: "snametrois", email: "bentrois@gmail.com", hashedPassword: "whocarestrois").save()

        eventTest = new Event(title: "titre", address: "address", dateBeginEvent: new Date() +1, dateEndEvent: new Date() +2, description: "description", privacy: Type.PUBLIC).save()
        eventTest2 = new Event(title: "titredeux", address: "addressdeux", dateBeginEvent: new Date() +1, dateEndEvent: new Date() +2, description: "descriptiondeux", privacy: Type.PRIVATE).save()
        eventTest3 = new Event(title: "titretrois", address: "addresstrois", dateBeginEvent: new Date() +1, dateEndEvent: new Date() +2, description: "descriptiontrois", privacy: Type.PUBLIC).save()
        eventTest4 = new Event(title: "titrequatre", address: "addressquatre", dateBeginEvent: new Date() +1, dateEndEvent: new Date() +2, description: "descriptionquatre", privacy: Type.PRIVATE).save()

        // One owns One, Two owns Two, Three owns Three and is involved in One and Two
        new Involved(role: Role.OWNER, user:userTest, event: eventTest).save()
        new Involved(role: Role.OWNER, user:userTest2, event: eventTest2).save()
        new Involved(role: Role.OWNER, user:userTest3, event: eventTest3).save()
        new Involved(role: Role.GUEST, user:userTest3, event: eventTest2).save()
        new Involved(role: Role.GUEST, user:userTest3, event: eventTest).save()
        new Involved(role: Role.OWNER, user:userTest3, event: eventTest4).save()
    }

    def "it should allow a user to see a public event"() {
        given:
        def owner = new User(loginName: "login", firstName: "firstName", secondName: "secondName",
                email: "email@email.com", hashedPassword: "password")
        def event = new Event(title: "title", address: "address", dateBeginEvent: new Date() +1,
                dateEndEvent: new Date() +2, description: "description", owner: owner, privacy: Type.PUBLIC)

        and:
        def isAllowed = EventService.isUserAllowedToSee(owner, event)
        def isAllowed2 = EventService.isUserAllowedToSee(userTest,eventTest2)

        expect:
        isAllowed
        !isAllowed2
    }

    def "test that a user is allowed to see an event"(){
        expect:
        EventService.isUserAllowedToSee(userTest,eventTest) == userOneSelfEvent
        EventService.isUserAllowedToSee(userTest,eventTest2) == userOneEventTwo
        EventService.isUserAllowedToSee(userTest2,eventTest2) == userTwoSelfEvent
        EventService.isUserAllowedToSee(userTest2,eventTest) == userTwoEventOne
        EventService.isUserAllowedToSee(userTest2,null) == f1
        EventService.isUserAllowedToSee(null,eventTest) == f2

        where:
        userOneSelfEvent | userOneEventTwo | userTwoSelfEvent | userTwoEventOne | f1    | f2
        true             | false           | true             | true            | false | false
    }

    def "test that a user is allowed to edit an event"(){
        expect:
        EventService.isUserAllowedToEdit(userTest,eventTest) == userOneSelfEvent
        EventService.isUserAllowedToEdit(userTest,eventTest2) == userOneEventTwo
        EventService.isUserAllowedToEdit(userTest2,eventTest2) == userTwoSelfEvent
        EventService.isUserAllowedToEdit(userTest2,eventTest) == userTwoEventOne

        where:
        userOneSelfEvent | userOneEventTwo | userTwoSelfEvent | userTwoEventOne
        true             | false           | true             | false
    }

    def "tests that a user can find his events"(){
        setup:
        ArrayList<Event> eventList = new ArrayList<Event>()
        eventList.add(eventTest3)
        eventList.add(eventTest4)
        eventList

        expect:
        EventService.findUsersEvents(userTest3).sort() == eventList.sort()
    }

    def "tests that a user can find events where he is involved"(){
        setup:
        ArrayList<Event> eventList = new ArrayList<Event>() // for user three
        ArrayList<Event> eventList2 = new ArrayList<Event>()// for user two
        eventList.add(eventTest)
        eventList.add(eventTest2)
        eventList.add(eventTest3)
        eventList.add(eventTest4)

        eventList2.add(eventTest2)

        expect:
        EventService.findWhereUserIsInvolved(userTest3).sort() == eventList.sort()
        EventService.findWhereUserIsInvolved(userTest2).sort() == eventList2.sort()
    }

    def "test that we can get the list of public events"(){
        setup:
        ArrayList<Event> eventList = new ArrayList<Event>() // for user three
        eventList.add(eventTest)
        eventList.add(eventTest3)

        expect:
        EventService.findPublicEvents().sort() == eventList.sort()
    }


    def "test that events can be saved"(){
        setup:
        Event e =  new Event(title: "ttle", address: "ddrs", dateBeginEvent: new Date() +1, dateEndEvent: new Date() +2, description: "descpt", privacy: Type.PUBLIC)
        Event e2 =  new Event(title: "", address: "", dateBeginEvent: new Date(), dateEndEvent: new Date() +2, description: "", privacy: Type.PUBLIC)

        expect:
        EventService.saveEvent(e,userTest) == true
        EventService.saveEvent(e2,userTest) == false
    }

    def "test that an event (and it's involved entities) can be deleted"(){
        expect:
        EventService.removeEvent(eventTest.id,userTest) == true
        EventService.removeEvent(eventTest.id,userTest) == false
        EventService.removeEvent(eventTest2.id,userTest) == false
        // TODO: test thet the involved are deleted
    }

    def "test that events can be updated"(){
        setup:
        eventTest.privacy=Type.PRIVATE

        expect:
        EventService.updateEvent(eventTest) == true
    }
}