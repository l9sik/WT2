package com.poluectov.criticine.webapp.model.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordVerifier {

    public static String createPasswordHash(String password) {
        // Hash a password with a randomly generated salt
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String password, String storedHash) {
        // Check if the input password matches the stored hash
        return BCrypt.checkpw(password, storedHash);
    }




}
