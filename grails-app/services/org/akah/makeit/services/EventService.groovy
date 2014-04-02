package org.akah.makeit.services

import grails.transaction.Transactional
import org.apache.commons.logging.LogFactory
import org.akah.makeit.domain.*

@Transactional
class EventService {

    private static final log = LogFactory.getLog('grails.app.' + EventService.class.name)
    /**
     * Shall be used to check a single user for a single event. Not more.
     * There is an other method to give the list of every events a user
     * is involved in.
     * @param user the user
     * @param event the event
     * @return true if the user is allowed to see the event
     */
    public static isUserAllowedToSee(User userToCheck,Event eventToCheck){
        if (userToCheck == null || eventToCheck == null) {
            return false
        } else if (eventToCheck.privacy == Type.PUBLIC){
            return true
        } else if (Involved.findWhere(event:eventToCheck,user:userToCheck)){
            return true
        } else {
            return false
        }
    }

    public static isUserAllowedToEdit(User userToCheck,Event eventToCheck){
        return (Involved.findWhere(event:eventToCheck,user:userToCheck,role:Role.OWNER) != null)
    }

    public static isUserGuest(User userToCheck,Event eventToCheck){
        return (Involved.findWhere(event:eventToCheck,user:userToCheck,role:Role.GUEST) != null)
    }

    public static List<Event> findUsersEvents(userToCheck){
        ArrayList<Event> eventList = new ArrayList<Event>()
        def involvedList = Involved.findAllWhere( user:userToCheck,role:Role.OWNER)

        for (Involved involved:involvedList){
            eventList.add(involved.event)
        }
        return eventList
    }

    /**
     * Returns all events where a user is involved
     * @param user
     */
    public static List<Event> findWhereUserIsInvolved(User userToCheck){
        def involvedList = Involved.findAllWhere( user:userToCheck )
        def List<Event> eventList = new ArrayList<Event>()

        for (Involved involved:involvedList){
            eventList.add(involved.event)
        }
        return eventList
    }

    /**
     * Returns all events a user is allowed to see
     * @return
     */
    public static List<Event> findPublicEvents(){
        return Event.findAll{ privacy == Type.PUBLIC }
    }

    public static boolean removeEvent(eventId, user){
        def event = Event.find { id == eventId }

        if (EventService.isUserAllowedToEdit(user, event)) {
            def involvedList = Involved.findAllWhere(event:event)

            for (Involved involved:involvedList){
                involved.delete()
            }
            event.delete(flush: true)
            return true
        } else {
            return false
        }
    }

    public static boolean saveEvent(Event event, User user){
        if (!event.save(flush: true)) {
            return false
        } else {
            // saving involved
            new Involved(event:event,user:user,role:Role.OWNER).save(flush:true)

            log.info("New event created: "+event)
            return true
        }
    }

    /**
     * Updates an event
     * @param event
     * @return true if ok
     */
    public static boolean updateEvent(Event event){
        log.info("Event edited: "+event)
        event = event.merge(flush: true,failOnError: true)
        return (event != null)
    }
}
