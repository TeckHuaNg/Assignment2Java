/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.PasswordGenerator;
import model.Profile;
import model.User;

/**
 * FXML Controller class
 *
 * @author Bearuang
 */
public class LoginController implements Initializable {

    @FXML private TextField userNameTextField;
    @FXML private TextField passwordTextField;
    @FXML private Label errormsg;
    
    
    private byte salt[] = null;
    private String pw = null;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errormsg.setText("");
    }    
    
    /**
     * This method will get the user's from database
     */
    public void getUserInfo() throws SQLException
    {
        String user = userNameTextField.getText();
        
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try{
            //1. connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://sql.computerstudi.es/gc200348264", "gc200348264", "kS9h4EJ3");
            //2.  create a statement object
            statement = conn.createStatement();
            
            //3.  create the SQL query
            resultSet = statement.executeQuery("SELECT * FROM user WHERE userName = '" + user +"'");
            
            
            //4.  create volunteer objects from each record
            while (resultSet.next())
            {
                Blob blob = resultSet.getBlob("salt");
                int blobLength = (int) blob.length();
                salt = blob.getBytes(1, blobLength);
               
                pw = resultSet.getString("password");
            }
              
        } catch (Exception e)
        {
            this.errormsg.setText(e.getMessage());
        }
        finally
        {
            if (conn != null)
                conn.close();
            if(statement != null)
                statement.close();
            if(resultSet != null)
                resultSet.close();
        }
    }
    
    /**
     * This method compare the password entered
     */
    public boolean checkPw()
    {
        Predicate<String> pwChecker = (password)-> password.equals(pw);
        String pw = PasswordGenerator.hashPw(passwordTextField.getText(), salt);
        if(pwChecker.test(pw) == false)
            throw new IllegalArgumentException("Either password or username is invalid");
        else
            return true;
    }
    
    public void loginButtonPushed(ActionEvent event) throws SQLException, IOException
    {
        try
        {
            getUserInfo();
            if(checkPw() == true)
            {
                SceneChanger sc = new SceneChanger();
                sc.changeScenes(event, "ProfileList.fxml", "All Contacts");
            }
        }
        catch(IllegalArgumentException e)
        {
            errormsg.setText(e.getMessage());
        }
        
    }
    
    public void registerButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "Register.fxml", "New User");
    }
}
