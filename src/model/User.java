/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Bearuang
 */
public class User {
    private byte[]salt;
    private String username, pw;

    public User(byte[] salt, String username, String pw) {
        this.salt = new byte[16];
        setSalt(salt);
        setUsername(username);
        setPw(pw);
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = PasswordGenerator.hashPw(pw, salt);
    }
    
    /**
     * This method will write the instance of the User into the database
     */
    public void insertIntoDB() throws SQLException
    {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        
        try
        {
            //1. Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://sql.computerstudi.es/gc200348264", "gc200348264", "kS9h4EJ3");
            
            //2. Create a String that holds the query with ? as user inputs
            String sql = "INSERT INTO user (username, salt, password)"
                    + "VALUES (?,?,?)";
                    
            //3. prepare the query
            preparedStatement = conn.prepareStatement(sql);
                
            //4. Bind the values to the parameters
            preparedStatement.setString(1, username);
            preparedStatement.setBlob(2, new javax.sql.rowset.serial.SerialBlob(salt));
            preparedStatement.setString(3, pw);
            
            
            preparedStatement.executeUpdate();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            if (preparedStatement != null)
                preparedStatement.close();
            
            if (conn != null)
                conn.close();
        }
    }
    
}
