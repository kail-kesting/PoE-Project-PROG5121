/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poe.part1;

/**
 *
 * @author Student
 */
public class Registration {
    
    String userName;
    String passWord;
    String cellPhone;
     
    public boolean checkUserName(String username) { //start of username method
        int length = username.length();
        return username.contains("@") && length <= 6;
    } //end of username method
    
    public boolean checkPasswordComplexity(String password) { //start of password method
        return password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[a-z].*")
                && password.matches(".*\\d.*")
                && password.matches(".*[^a-zA-Z0-9].*"); //end of password method
    }
    
    public boolean checkCellPhoneNumber(String cellphone) { //start of cellphone method
        return cellphone.matches(".*\\+27[0-9]{12}$") || cellphone.matches(".*^0[0-9]{12}") || cellphone.length() ==12;
    } //end of cellphone method
    
    public String registerUser(String username,
            String password,
            String cellphone) {
        
        if (checkUserName(username)
                && checkPasswordComplexity(password)
                && checkCellPhoneNumber(cellphone)) {
            
            this.userName = username;
            this.passWord = password;
            this.cellPhone = cellphone;
            
            return "Registration was successful";
        } else {
            return "Registration was unsuccessful";
        }
    }
    public boolean registerStatus(String username, String password, String cellphone) {
        return checkUserName(username) && 
                checkPasswordComplexity(password) &&
                checkCellPhoneNumber (cellphone);
    }
    
} //end of class
