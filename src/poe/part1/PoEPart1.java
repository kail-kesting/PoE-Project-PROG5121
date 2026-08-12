/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package poe.part1;

import java.util.Scanner;

/**
 *
 * @author Student
 */
public class PoEPart1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Login register = new Login(); 
        Scanner scan = new Scanner (System.in);
        
        System.out.println("==== Register ====");
        
        //Username input and checker
        
        System.out.println("Please Enter Username");
        String name = scan.nextLine(); 
        
        System.out.println("Enter Password");
        String password = scan.nextLine();
        
        System.out.println("Enter cellphone number");
        String cellphone = scan.nextLine();
        System.out.println(register.registerUser(name, password, cellphone));
        
        if (!register.registerStatus(name, password, cellphone)) {
            return;
        }
        System.out.println("==== Login ====");
        System.out.println("Please Enter Username");
        String Name = scan.nextLine(); 
        
        System.out.println("Enter Password");
        String Password = scan.nextLine();
        
        System.out.println("Enter cellphone number");
        String Cellphone = scan.nextLine();
        
       
    } //end of main
    
} //end of class
