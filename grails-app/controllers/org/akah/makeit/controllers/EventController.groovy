package org.akah.makeit.controllers

import grails.transaction.Transactional
import org.akah.makeit.domain.*
import org.akah.makeit.services.EventService
import org.akah.makeit.services.GeneralService
import org.akah.makeit.services.InvolvedService

@Transactional(readOnly = true)
class EventController {
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
    }

    def index() {
        redirect(action:'list')
    }

    /**
     * Controller for create view. Creates a user.
     * @return
     */
    def create() {
        Event event = new Event(params)
        if (request.method == 'GET'){ // if params are not passed then we just render the page
            render (view:"create", model: [eventInstance: event])
            return true
        } else { // else we try to save the user
            if (EventService.saveEvent(event,session.user)){ // whe creating an event, automatically involve the user in it as owner
                flash.message = "event.created"
                redirect (action:"list")
            } else {
                flash.error = "event.creation.failed"
                render (view:"create", model: [eventInstance: event])
                return false
            }
        }
    }

    /**
     * Controller for delete action, removes an event
     * @return
     */
    def delete(){
        if (EventService.removeEvent(params["id"], session.user)){
            flash.message="event.deleted"
            redirect (action:"list", params:[filter:"my"])
            return true
        } else {
            flash.message="cannot.delete.event"
            redirect (action:"list")
        }
    }

    /**
     * Shows an event or the list of user's event or the list of events he's
     * involved in.
     */
    def list(){ //TODO implement a smart way to get all the events for a given user (count must be limited)
        def filter = params.filter
        def user = session.user

        if (filter == "my"){ // show all my events
            render(view: "list",model:[eventList:EventService.findUsersEvents(user)])
            return true
        } else if (filter == "involved"){ // show events i'm involved in
            render(view: "list",model:[eventList:EventService.findWhereUserIsInvolved(user)])
            return true
        } else {// shows all public events
            render(view: "list",model:[eventList:EventService.findPublicEvents()])
            return true
        }
    }

    /**
     * Shows an event if the user is allowed to see it
     * @return
     */
    def show(){
        def event = Event.find { id == params.id }
        if (EventService.isUserAllowedToSee(session.user, event)) {
            render(view:"show",model:[eventInstance:event, isUserInvolved:EventService.isUserGuest(session.user, event)])
        } else {
            flash.error="event.access.not.allowed"
            redirect(view:"list")
            return false
        }
    }

    def edit(){
        def event = Event.get(params.id)
        if (event != null && EventService.isUserAllowedToEdit(session.user,event)){
            if (request.method == 'GET'){ // if params are not passed then we just render the page
                render (view:"edit", model:[eventInstance: event])
                return true
            } else { // else we try to save the user
                event.properties=params
                if (EventService.updateEvent(event)) {
                    flash.message="event.edited"
                    redirect (action:"show", params:[id:event.id])
                    return true
                } else {
                    flash.error="event.edition.failed"
                    render (view:"edit", model: [eventInstance: event])
                    return false
                }
            }
        } else {
            flash.error="event.access.not.allowed"
            redirect(view:"list")
            return false
        }
    }

    /**
     * You can simply have a involved button as guest and involve as driver button
     * there is no need for a form.
     * Redirection must be initialized before calling this method
     * Id and role must be passed trough the request
     */
    def getInvolved(){
        def event = Event.get(params.id)
        def role

        if (!params.role){
            role = Role.GUEST
        }else {
            role = Role.valueOf( params.role )
        }

        def car
        if (params.car != null){
            car = Car.find { id == params.car }
        } else {
            car = null
        }

        if (event != null && event.privacy != Type.PRIVATE){
            InvolvedService.involve(session.user, event, role, car)
            flash.message="involved.success"
            redirect (action:"show", params:[id:event.id])
            return true
        } else { // Redirection must be initialized before calling this method
            flash.error="involved.failed"
            redirect (action:"list")
            return false
        }
    }

    def unInvolve(){
        if (InvolvedService.removeInvolvement(params.id,session.user)){
            flash.message="uninvolve.success"
            redirect (action:"list")
            return true
        }else {
            flash.error="uninvolve.failed"
            redirect (action:"list")
            return false
        }
    }
}
