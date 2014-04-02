package org.akah.makeit.controllers

import org.akah.makeit.services.GeneralService
import org.akah.makeit.services.UserService
import org.akah.makeit.domain.*

class UserController {

    //TODO: check with teammates if doLogin and doLogout are useful here
    //static allowedMethods = [doLogout: "POST", doLogin: "POST", login: "POST", create:"POST"]

    def beforeInterceptor = [action: this.&isUserLoggedIn, except: ['logout','myAccount','delete']]

    /**
     * Method intercepting others that allows a user if he's not logged in
     * (used to control that a logged user can't create an account while
     * still logged in)
     * Only used in user class. If you are looking for a authentication
     * method see the auth method. In InvolvedController
     */
    private isUserLoggedIn(){
        if(GeneralService.userIsLoggedIn(session.user)) {
            flash.message = "app.already.logged"
            redirect(url:GeneralService.redirectToCaller(session))
            return false
        }
    }

    /**
     * Redirects to login page. It should redirect to account page if the user is logged in
     * not implemented yet.
     */
    def index() {
        redirect(action:'login')
    }

    /**
     * Shows the current user account without passing it's ID
     * as a get parameter like scaffolding method does
     * Enables editing
     */
    def myAccount(){
        def user = session.user
        if (user){
            if (params.edit == 'enabled') { // if edit mode
                if (request.method == 'POST') { // if edit sent trough post then save it
                    edit(params)
                    return true
                } else { // else just render the edit page
                    render(view: 'edit', model:[userInstance: user])
                    return true
                }
            } else { // if edit mode is not enabled then render the show view
                render(view: 'show', model:[userInstance: user])
                return true
            }
        } else { // if user is not logged in then redirect him to the caller
            flash.message = "app.login.first"
            redirect(url:GeneralService.redirectToCaller(session))
            return false
        }
    }

    /**
     * Controls the creation page and is able to save a user
     * @return
     */
    def create(){
        def user = new User(params)
        if (request.method == 'GET'){ // if params are not passed then we just render the page
            render(view: "create", model:[userInstance: user])
            return true
        } else { // else we try to save the user
            if (UserService.checkPasswords(params["password"],params["confirm"])) { // the password is ok we try to save the user
                user.hashedPassword = GeneralService.encrypt(params["password"]) // encrypt password

                if (UserService.save(user)){
                    flash.message="user.created"
                    redirect(action:'login')
                    return true
                } else {
                    flash.error="user.creation.failed"
                    redirect(view: "create", model:[userInstance: user]) // redirect to creation
                    return false
                }
            } else {
                flash.error="user.password.checking.failed"
                render(view:"create", model:[userInstance: session.user]) // renders the view with the previous given information
            }
        }
    }

    /**
     * Private function called to edit a user's profile (only does the redirection
     * and message sending, real eddition is done in a specific service)
     * @param params
     * @return
     */
    private boolean edit(params){
        User user = session.user
        String currentPassword = params["current"]
        String newPassword = params["password"]

        if (currentPassword){ // If current password is given we check it
            if (UserService.checkEditPassword(user.hashedPassword, currentPassword)){
                if (!UserService.checkPasswords(newPassword, params["confirm"])){
                    flash.error = "user.password.checking.failed"
                    render(action:'myAccount', params: [edit:'enabled'], model:[userInstance: session.user]) // renders the view with the previous given information
                    return false
                } else user.hashedPassword = GeneralService.encrypt(newPassword)
            } else {
                flash.error = "user.invalid.password"
                render(action:'myAccount', params: [edit:'enabled'], model:[userInstance: session.user]) // renders the view with the previous given information
                return false
            }
        } else if (params['password']){ // Current password is undefined but the new one is
            flash.error = "user.edit.nopassword.error" // you can't change the password without the old one
            redirect(action:'myAccount', params: [edit:'enabled'])
            return false
        }
        user.properties = params

       if (UserService.editUser(user)){
            flash.message = "user.edited"
            session.user = user // update session
            redirect(action:'myAccount') // show user account
            return true
        } else {
            flash.error = "user.edit.failed"
            redirect(action:'myAccount', params: [edit:'enabled']) // continue editing
            return false
        }
    }

    /**
     * Controls the deleting of a user
     * @return
     */
    def delete(){
        UserService.delete(session.user)

        redirect(uri:'/')
        return true
    }

    /**
     * Displays login page and allows to log in
     */
    def login(){
        if (UserService.checkLoginAttempts(session)){
            if (!params['email'] || request.method == 'GET'){ // if params are not passed then we just render the page
                render(view:"login")
                return false
            } else {
                def user = User.findWhere(email:params['email'], hashedPassword:GeneralService.encrypt(params['password']))

                if (user){
                    session.user = user
                    flash.message = "app.login.success"
                    session.loginAttempts = null
                    redirect(url:GeneralService.redirectToCaller(session))
                    return true
                }
                else {
                    UserService.recordLoginFailure(session)
                    flash.error = "app.login.error"
                    redirect(action:'login')
                    return false
                }
            }
        } else {
            // TODO externalize string
            flash.error="Too many login attempts. Please wait."
            redirect(action:'login')
            return false
        }
    }

    /**
     * Logs a user out by cleaning the session attribute
     */
    def logout(){
        if (UserService.logUserOut(session)){
            flash.message = "app.logout.success"
            redirect(uri:'/')
        } else {
            flash.message = "app.logout.failed"
            redirect(uri:'/')
        }
    }
}
