package poe.part1;

/**
 *
 * @author Student
 */
public class Registration {
    
    String userName;
    String passWord;
    String cellPhone;
     
    // Checks that username contains an underscore (_) and is no more than 5 characters long
    public boolean checkUserName(String username) { 
        return username != null && username.contains("_") && username.length() <= 5;
    } 
    
    // Checks password complexity (>=8 chars, uppercase, lowercase, digit, special char)
    public boolean checkPasswordComplexity(String password) { 
        return password != null 
                && password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[a-z].*")
                && password.matches(".*\\d.*")
                && password.matches(".*[^a-zA-Z0-9].*"); 
    }
    
    // Checks that cellphone contains international code (+27) or starts with 0 and has 10 digits total
    public boolean checkCellPhoneNumber(String cellphone) { 
        return cellphone != null && (cellphone.matches("^\\+27[0-9]{9}$") || cellphone.matches("^0[0-9]{9}$"));
    } 
    
    // Registers user if all conditions are met and returns expected message
    public String registerUser(String username, String password, String cellphone) {
        
        if (checkUserName(username)
                && checkPasswordComplexity(password)
                && checkCellPhoneNumber(cellphone)) {
            
            this.userName = username;
            this.passWord = password;
            this.cellPhone = cellphone;
            
            return "User registered successfully.";
        } else {
            return "Registration was unsuccessful";
        }
    }

    public boolean registerStatus(String username, String password, String cellphone) {
        return checkUserName(username) && 
                checkPasswordComplexity(password) &&
                checkCellPhoneNumber(cellphone);
    }
    
} // end of class