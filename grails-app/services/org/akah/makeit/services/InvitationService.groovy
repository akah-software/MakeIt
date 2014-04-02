package org.akah.makeit.services

import grails.transaction.Transactional
import jline.internal.Log
import org.apache.commons.logging.LogFactory
import org.akah.makeit.domain.*

@Transactional
class InvitationService {

    private static final log = LogFactory.getLog('grails.app.' + InvitationService.class.name)

    public static isUserAllowedToInvite(User userToCheck,Event eventToCheck){
        Log.info("find involved : "+userToCheck.loginName+" "+Involved.findAllByEventAndUserAndRole(eventToCheck,userToCheck,Role.GUEST))
        return (!Involved.findAllByEventAndUserAndRole(eventToCheck,userToCheck,Role.GUEST).isEmpty() ||
                !Involved.findAllByEventAndUserAndRole(eventToCheck,userToCheck,Role.OWNER).isEmpty() )
    }

    public static invite(Invitation invitation) {
        if(Involved.findAllByUserAndEvent(invitation.sendTo,invitation.event).isEmpty()
            && InvitationService.isUserAllowedToInvite(invitation.sendBy , invitation.event) ) {
            InvolvedService.involve(invitation.sendTo,invitation.event,Role.GUEST)
            return true
        }
        else {
            return false
        }
    }
}
