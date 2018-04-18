/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.PasswordGenerator;
import model.User;

/**
 * FXML Controller class
 *
 * @author Bearuang
 */
public class RegisterController implements Initializable {

    @FXML private TextField userNameTextField;
    @FXML private TextField pwTextField;
    @FXML private TextField confirmPwTextField;
    @FXML private Label errorMsg;
    
    
    private Predicate<String> checkLength;
    private Predicate<String> checkSpecialChar;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        checkLength = (pw)-> pw.length() > 6;
        checkSpecialChar = (pw)-> pw.matches("[A-Za-z0-9]+");
        errorMsg.setText("");
    } 
       
    /**
     * This method checks the password for requirements
     */
    public void checkPw()
    {
        if(checkLength.test(pwTextField.getText()) == false || checkSpecialChar.test(pwTextField.getText()) == true)
            throw new IllegalArgumentException("Your password must be more than 6 characters and contains at least one special character");        
    }
    
    /**
     * This method compares both password and check if they are same
     */
    public void comparePw()
    {
        Predicate<String> comparePw = pw -> pw.equals(confirmPwTextField.getText());
        if(comparePw.test(pwTextField.getText()) == false)
             throw new IllegalArgumentException("Both password must match");   
    }
    
    public void registerButtonClicked(ActionEvent event)
    {
        try
        {
            checkPw();
            comparePw();
            User newUser = new User(PasswordGenerator.getSalt(), userNameTextField.getText(),pwTextField.getText());
          
            errorMsg.setText("");
            newUser.insertIntoDB();
            System.out.print(newUser.getSalt());
            SceneChanger sc = new SceneChanger();
            sc.changeScenes(event, "Login.fxml", "Login");
        }
        catch(Exception e)
        {
            errorMsg.setText(e.getMessage());
        }
        
    }
}
