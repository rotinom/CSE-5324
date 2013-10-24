package com.mytutor.session;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import android.util.Base64;


/**
 * Computes a salted hash of a password for storing in a database
 * @author rotinom
 *
 */
public class Password {
    // The higher the number of iterations the more 
    // expensive computing the hash is for us
    // and also for a brute force attack.
    private static final int iterations = 10;
    
    // Length of the salt
    private static final int salt_length = 32;
    
    // Length of the key
    private static final int desiredKeyLen = 256;
    
    // Delimiter for the salt and password
    private static final String delimiter = ",";
    
    /**
     * Generate a random salt
     * @return
     */
    public static String salt() {
    	try {
    		byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(salt_length);
    		return Base64.encodeToString(salt,  Base64.DEFAULT);
    	}
    	catch(Exception e){
    		return "";
    	}
    }
    
    /**
     * Compute a salted SHA1 hash given a plaintext password
     * 
     * @param password The password to encrypt
     * @param salt The salt to use with the password
     * @return String representing the salt + hashed password delimited by a '$'
     * @throws Exception
     */
    public static String hash(String password, String salt) {
        // store the salt with the password
    	byte[] bsalt = Base64.decode(salt, Base64.DEFAULT);
        return salt + delimiter + hash_(password, bsalt);
    }

    /** Checks whether given plaintext password corresponds 
        to a stored salted hash of the password. */
    
    
    /**
     * 
     * @param password
     * @param stored_hash
     * @return
     * @throws Exception
     */
    public static boolean authenticate(String password, String stored_hash) throws Exception{
        // String[] saltAndPass = stored_hash.split("\\$");
    	String[] saltAndPass = stored_hash.split(delimiter);
        if (saltAndPass.length != 2){
            return false;
        }
        String hashOfInput = hash_(password, Base64.decode(saltAndPass[0], Base64.DEFAULT));
        return hashOfInput.equals(saltAndPass[1]);
    }

    
    // using PBKDF2 from Sun, an alternative is https://github.com/wg/scrypt
    // cf. http://www.unlimitednovelty.com/2012/03/dont-use-bcrypt.html
    private static String hash_(String password, byte[] salt) {
    	
    	// Verify against null/empty passwords
        if (password == null || password.length() == 0){
            throw new IllegalArgumentException("Empty passwords are not supported.");
        }
        

		try {
	        // Get our key factory and generate a key
	        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		
	        SecretKey key = f.generateSecret(
				new PBEKeySpec(
						password.toCharArray(), 
						salt, 
						iterations, 
						desiredKeyLen
				)
			);
			
			return Base64.encodeToString(key.getEncoded(), Base64.DEFAULT);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "INVALID_HASH_VALUE";
    }
}