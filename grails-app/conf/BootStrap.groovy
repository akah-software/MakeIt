import grails.util.Environment
import org.akah.makeit.domain.Event
import org.akah.makeit.domain.Involved
import org.akah.makeit.domain.Role
import org.akah.makeit.domain.Type
import org.akah.makeit.domain.User
import org.akah.makeit.services.GeneralService

class BootStrap {

    def init = { servletContext ->
        if (Environment.current == Environment.TEST) {
            println "Test environment"
            //new BootStrapTest().init()
        } else if (Environment.current == Environment.DEVELOPMENT){
            def user1 = new User(loginName: "user1", firstName: "firstName", secondName: "secondName", email: "user1@email.com", hashedPassword: GeneralService.encrypt("password")).save(failOnError: true)
            def event1 = new Event(title: "AngularJS Conference", address: "118 route de narbonne 31400 Toulouse", dateBeginEvent: new Date() +1,
                    dateEndEvent: new Date() +2, description: "A description", privacy: Type.PUBLIC).save(failOnError: true)
            new Involved(user: user1, event: event1, role: Role.OWNER, car: null, meetingPoint: null).save(failOnError: true)

            def user2 = new User(loginName: "user2", firstName: "firstName", secondName: "secondName", email: "user2@email.com", hashedPassword: GeneralService.encrypt("password")).save(failOnError: true)
            def event2 = new Event(title: "Forum", address: "118 route de narbonne 31400 Toulouse", dateBeginEvent: new Date() +5,
                    dateEndEvent: new Date() +6, description: "First session of beloved teaching unit", privacy: Type.PUBLIC).save(failOnError: true)
            new Involved(user: user2, event: event2, role: Role.OWNER, car: null, meetingPoint: null).save(failOnError: true)

            def event3 = new Event(title: "IVVQ oral", address: "118 route de narbonne 31400 Toulouse", dateBeginEvent: new Date() +5,
                    dateEndEvent: new Date() +6, description: "MakeIt consecration", privacy: Type.PUBLIC).save(failOnError: true)
            new Involved(user: user2, event: event3, role: Role.OWNER, car: null, meetingPoint: null).save(failOnError: true)

            new Involved(user: user1, event: event2, role: Role.GUEST, car: null, meetingPoint: null).save(failOnError: true)
        }
    }
    def setEventServiceEnvironnement(){
        def userTest = new User(loginName: "login", firstName: "fname", secondName: "sname", email: "ben@gmail.com", hashedPassword: "whocares").save()
        def userTest2 = new User(loginName: "logindeux", firstName: "fnamedeux", secondName: "snamedeux", email: "bendeux@gmail.com", hashedPassword: "whocaresdeux").save()
        def eventTest = new Event(title: "titre", address: "address", dateBeginEvent: new Date() +1, dateEndEvent: new Date() +2, description: "description", privacy: type).save()
        def eventTest2 = new Event(title: "titredeux", address: "addressdeux", dateBeginEvent: new Date() +1, dateEndEvent: new Date() +2, description: "descriptiondeux", privacy: type2).save()

        new Involved(role: invRole, user:userTest, event: eventTest).save()
        new Involved(role: invRole2, user:userTest2, event: eventTest2).save()
    }
    def destroy = {
    }
}
