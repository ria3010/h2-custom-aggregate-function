package org.h2.value;

import org.h2.api.ErrorCode;
import org.h2.message.DbException;

import java.io.Serializable;

public class Password implements Serializable {
    private String password ;

    public Password(String password) {
        System.out.println(" Inside the password constructor ");
        if(passwordValidator(password)){
            System.out.println(" Valid Password" );
            this.password = password;
        }
        else{
            System.out.println(" Invalid password exception thrown ");
            throw DbException.get(ErrorCode.INVALID_PASSWORD);
        }
    }

    private boolean passwordValidator(String password) {
        int letterCount = 0;
        int numberCount = 0;

        if (password.length() < 8) {
            System.out.println(" 28" );
            throw DbException.get(ErrorCode.INVALID_PASSWORD_LENGTH);
        }
        for (int i = 0; i < password.length(); i++) {

            char eachCharacter = password.charAt(i);

            if (checkIfNumber(eachCharacter)){
                numberCount++;
            }
            else if (checkIfLetter(eachCharacter)) {
                letterCount++;
            }
        }
        System.out.println(" 41" );
        if (letterCount<2){
            throw DbException.get(ErrorCode.INVALID_PASSWORD_CHAR);
        }
        if (numberCount<2){
            throw DbException.get(ErrorCode.INVALID_PASSWORD_NUMBER);
        }
        return (letterCount >= 2 && numberCount >= 2);
    }

    public static boolean checkIfLetter(char eachCharacter) {
        eachCharacter = Character.toUpperCase(eachCharacter);
        return (eachCharacter >= 'A' && eachCharacter <= 'Z');
    }


    public static boolean checkIfNumber(char eachCharacter) {

        return (eachCharacter >= '0' && eachCharacter <= '9');
    }

    @Override
    public String toString(){
        return password;
    }

}



