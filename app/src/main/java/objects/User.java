package objects;

import java.text.DecimalFormat;

public class User {
    public String email;
    public String userID;
    public float balance;

    public User(){

    }

   public User(String email, String userID){
       this.email = email;
       this.userID = userID;
   }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getUserID(){
        return userID;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
