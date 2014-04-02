package org.akah.makeit.services

import grails.transaction.Transactional
import groovy.time.TimeCategory
import org.apache.commons.logging.LogFactory
import org.akah.makeit.domain.Car
import org.akah.makeit.domain.Involved
import org.akah.makeit.domain.Role
import org.akah.makeit.domain.User

@Transactional
class UserService {

    public static final REDIRECT_CONTROLLER = "RDirController"
    public static final REDIRECT_ACTION = "RDirAction"
    public static final USER = "user"
    public static final LOGIN_ATTEMPTS = "loginAttempts"
    public static final LAST_LOGIN_ATTEMPT = "lastLoginAttempt"

    /**
     * Logger
     */
    private static final log = LogFactory.getLog('grails.app.' + UserService.class.name)

    /**
     * Saves a user and saves it in the database. The user is immediately persisted
     * so it can be used immediately for logging in the app.
     *
     * @return true if success false if process failed
     */
    public static boolean save(User user){
        if (user.save(flush: true)) { // call saving service
            log.info("New user created: "+user)
            return true
        } else {
            return false
        }
    }

    /**
     * Check constrains concerning new and old passwords when
     * an edit is called
     * @param params
     * @return
     */
    public static boolean checkEditPassword(String userHashedPassword,String password){
        return (GeneralService.encrypt(password) == userHashedPassword)
    }

    /**
     * Checks the constraints on the passwords. Passwords must match and have
     * at least 5 characters
     * @param password  String password one
     * @param confirm  String confirmation password
     * @return true if the passwords are valid
     */
    public static boolean checkPasswords(password, confirm){
        if (!password || !confirm) {
            return false
        } else if (password != confirm) {
            return false
        } else if (password.size() < 5) {
            return false
        } else {
            return true
        }
    }

    public static boolean editUser(user){
            user = user.merge(flush: true) // explanations below
            // merge is similar in function to the save
            // for details on merge function
            // @see http://grails.org/doc/2.2.1/ref/Domain%20Classes/merge.html

            if (user) { // call saving service
                log.info("User edited: "+user)
                return true
            } else {
                return false
            }
    }

    /**
     * Deletes a user and all his involved objects, his events and his cars
     * @param user the user to remove
     * @return
     */
    public static boolean delete(User user, session){
        def involvedList = Involved.findAllWhere(user:user)
        def carList = Car.findAllWhere(owner:user)

        // get list of where the user is involved and remove them
        for (Involved involved:involvedList) {
            // if the user is owner of an event, delete it
            if (involved.role == Role.OWNER) { // if you own an event this event is deleted too
                def eventToDelete = involved.event
                involved.delete() // must delete involved before event because it references event
                eventToDelete.delete(flush:true)
            } else {
                // if you don't own the event just delete the involvement
                involved.delete(flush:true)
            }
        }
        // if the user owns a car, delete it
        for (Car car:carList){
            car.delete(flush:true)
        }
        user.delete(flush: true)
        return logUserOut(session)
    }

    /**
     * Logs a user out, invalidates the session
     * @return
     */
    public static boolean logUserOut(session){
        // IntelliJ can't resolve but this exists in the implementations
       // def session = RequestContextHolder.currentRequestAttributes().getSession()

        if (session[USER] == null) {
            return false
        } else {
            // old fashioned habit of nulling attributes before invalidating
            session[REDIRECT_CONTROLLER] = null
            session[REDIRECT_ACTION] = null
            session[USER] = null
            //session.invalidate() // cleans the session
            return true
        }
    }

    /**
     * Checks remaining attempts on user login
     * @return true if the user can login false if not
     */
    public static boolean checkLoginAttempts(session){
        // IntelliJ can't resolve but this exists in the implementations
        //def session = RequestContextHolder.currentRequestAttributes().getSession()

        if (!session[LOGIN_ATTEMPTS]){
            session[LOGIN_ATTEMPTS] = 1
        } else if (session[LOGIN_ATTEMPTS] >= 5){

            def nextLoginPossibility
            use (TimeCategory) {
                nextLoginPossibility = session[LAST_LOGIN_ATTEMPT] + 5.minutes
            }

            def currentTime = GeneralService.getTime()

            if (currentTime.before(nextLoginPossibility)){
                return false
            } else {
                session[LOGIN_ATTEMPTS]  = 1
                return true
            }
        } else {
            return true
        }
    }

    /**
     * Records a failed login attempt
     * @return
     */
    public static recordLoginFailure(session){
        //def session = RequestContextHolder.currentRequestAttributes().getSession()
        session[LOGIN_ATTEMPTS]++
        session[LAST_LOGIN_ATTEMPT] = GeneralService.getTime()
    }

}

