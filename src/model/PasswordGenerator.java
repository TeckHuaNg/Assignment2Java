/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 *
 * @author Bearuang
 */
public class PasswordGenerator {
    
    /**
     * This will create a random salt consisting of 16 bytes
     */
    public static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom rng = SecureRandom.getInstanceStrong();
        byte[] salt = new byte[16];
        rng.nextBytes(salt);
        return salt;
    }
    
    public static String hashPw(String pw, byte[] salt)
    {
        String hashedPw = null;
        
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            
            byte[] hashed = md.digest(pw.getBytes());
            
            StringBuilder sb = new StringBuilder();
            
            for(int i=0; i<hashed.length; i++)
            {
                sb.append(String.format("%02x", hashed[i]));
            }
            hashedPw = sb.toString();
        }
        catch(NoSuchAlgorithmException e)
        {
            System.err.println(e);
        }
        
        return hashedPw;
    }
    
    
}
