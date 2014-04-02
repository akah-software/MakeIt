package org.akah.makeit.services

import grails.transaction.Transactional
import org.apache.commons.logging.LogFactory
import org.akah.makeit.domain.Event
import org.akah.makeit.domain.Involved
import org.akah.makeit.domain.Role
import org.akah.makeit.domain.User
import org.akah.makeit.domain.Car

@Transactional
class InvolvedService {

    private static final log = LogFactory.getLog('grails.app.' + InvolvedService.class.name)

    /**
     * Is not supposed to be used on an event creation so owner must be false
     * @param involved
     * @return
     */
    public static boolean checkConstraints(Involved involved){
        def role = involved.getRole()
        switch(role) {
            case Role.GUEST:
                return true
                break;
            case Role.DRIVER:
                return (involved.getCar().getOwner() == involved.getUser())
                break;
            case Role.OWNER:
                return false
                break;
        }
    }

    /**
     * Involves someone in an even.
     */
    public static boolean saveInvolved(Involved involved){ // V1
        if (!involved.save(flush:true)){
            return false
        } else {
            log.info("User "+involved.user+" involved in "+involved.event)
            return true
        }
    }

    public static boolean involve(Involved involved){
        if (checkConstraints(involved)){
            return saveInvolved(involved)
        }else {
            return false
        }
    }

    public static boolean removeInvolvement(eventId, User user){
        def event = Event.get(eventId)

        def involved = Involved.findByUserAndEvent(user, event)
        try{
            involved.delete(flush:true)
            return true
        } catch (Exception e){
            log.error(e.printStackTrace())
            return false
        }
    }

}
