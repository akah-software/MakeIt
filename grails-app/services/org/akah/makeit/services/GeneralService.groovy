package org.akah.makeit.services

import org.akah.makeit.domain.User

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class GeneralService {

    static transactional = true

    /**
     * Redirection variable
     */
    public static final REDIRECT_CONTROLLER = "RDirController"
    /**
     * Redirection variable
     */
    public static final REDIRECT_ACTION = "RDirAction"

    /**
     * Returns current time
     * @return current times
     */
    def static getTime(){
        TimeZone reference = TimeZone.getTimeZone("GMT")
        Calendar myCal = Calendar.getInstance(reference)
        TimeZone.setDefault(reference);
        return myCal.getTime()
    }

    /**
     * Encrypts a passwords using MD5
     * @param String password the password to encrypt
     * @return hashedPassword
     * @throws NoSuchAlgorithmException if the JVM doesn't implement the MD5 Algorithm
     */
    public static String encrypt(password) throws NoSuchAlgorithmException{
        StringBuilder hashString = new StringBuilder();
        byte[] uniqueKey = password.getBytes();
        byte[] hash = MessageDigest.getInstance("MD5").digest(uniqueKey);

        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(hash[i]);
            if (hex.length() == 1) {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length() - 1));
            }
            else hashString.append(hex.substring(hex.length() - 2));
        }
        return hashString.toString();
    }

    /**
     * Checks if a user is logged in and retruns true if yes
     * @param user the user
     * @return true if the user is logged in
     */
    public static boolean userIsLoggedIn(User user){
        return (user != null)
    }

    /**
     * Authenticate a user and set a redirection if the user is not logged in
     * @param controller the redirection target
     * @param action the redirection target
     * @return true if authenticate succeed
     */
    public static boolean authenticateWithRedirection(session ,String controller, String action){
        if(!userIsLoggedIn(session.user)) {
            session[REDIRECT_CONTROLLER] = controller
            session[REDIRECT_ACTION] = action
            return false
        } else return true
    }

    /**
     * Redirection method, sends the path to redirect to as a string
     * @return the redirection string
     */
    public static String redirectToCaller(session){
        // Variable "rdirXXX" used for redirection to caller, each page should pass it to login
        String controller = session[REDIRECT_CONTROLLER]
        String action = session[REDIRECT_ACTION]
        session[REDIRECT_CONTROLLER] = null
        session[REDIRECT_ACTION] = null

        if (!controller || !action) {
            return "/"
        }else{
            return "/"+controller+"/"+action+"/"
        }
    }
}
