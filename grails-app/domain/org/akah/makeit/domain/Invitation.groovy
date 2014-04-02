package org.akah.makeit.domain

class Invitation {
    /**
     * The sender of the invitation
     */
    User sendBy
    /**
     * The receiver of the invitation
     */
    User sendTo
    /**
     * The event where the senTo is invited
     */
    Event event

    @Override
    String toString() {
        return sendBy.getLoginName()+" invites "+sendTo.getLoginName()+" at the event : "+event.getTitle()
    }

    static constraints = {
        sendBy (nullable: false, blank:false)
        sendTo (nullable: false, blank:false)
        event (nullable: false, blank:false)
    }
}
