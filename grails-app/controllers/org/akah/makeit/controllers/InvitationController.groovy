package org.akah.makeit.controllers

import org.apache.commons.logging.LogFactory
import org.akah.makeit.domain.Event
import org.akah.makeit.domain.Invitation
import org.akah.makeit.domain.User
import org.akah.makeit.services.InvitationService
import org.akah.makeit.services.GeneralService

class InvitationController {

    /**
     * The logger is used only to log actions that will be needed in final
     * software use. Do not use it for tests.
     */
    private static final log = LogFactory.getLog('grails.app.' + InvitationController.class.name)
    //static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    def beforeInterceptor = [action: this.&auth, except:['index']]

    /**
     * Check if the user is authorized to access this page.
     * Redirects to login page
     * see https://docs.google.com/a/master-developpement-logiciel.fr/document/d/1NKsf-LJWN3GpCMUg1_BSeHDDokl31yEmV1j_uBtbCHk/edit
     */
    def auth() {
        if (!GeneralService.authenticateWithRedirection(session,controllerName,actionName)){
            flash.message = "app.login.first"
            redirect(controller: 'user', action: 'login')
            return false
        }
        flash.message = "login.ok"
    }

    def index() {
        redirect(action:'invite')
    }

    def invite = {
        def event = Event.find { id == params.id }
        if( event != null && InvitationService.isUserAllowedToInvite(session.user,event)) {
            if (request.method == 'GET'){ // if params are not passed then we just render the page
                render (view:"invite", model:[eventInstance: event])
                return true
            } else { // else we try to invite the user
                def sendTo = User.findByLoginName(params.friend)
                def invitation = new Invitation(sendBy: session.user, sendTo: sendTo, event: event)
                invitation.save(flush:true)
                log.info("New Invitation created: " + invitation.toString())
                flash.message = "invitation.sent"
                redirect (controller:"Event" , action:"list")
            }
        }
        else {
            flash.error="event.access.not.allowed"
            redirect(controller:"Event" , view:"list")
            return false
        }
    }
}
