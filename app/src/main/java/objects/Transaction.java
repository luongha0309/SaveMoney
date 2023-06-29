package objects;

import java.text.DecimalFormat;

public class Transaction {
    public double transAmount;
    public Boolean isPay;
    private String transNote;
    private String transDate;
    public String transID;
    private String userID;

    DecimalFormat f = new DecimalFormat("#,###");

    public Transaction(){

    }
    public Transaction(String transID,Double transAmount, Boolean isPay, String transNote ,
                       String transDate, String userID){
        this.transID = transID;
        this.transAmount = transAmount;
        this.isPay = isPay;
        this. transNote = transNote;
        this.transDate = transDate;
        this.userID = userID;
    }


    public Boolean getPay() {
        return isPay;
    }

    public Double getTransAmount() {
        return transAmount;
    }

    public String getTransDate() {
        return transDate;
    }

    public String getTransID() {
        return transID;
    }

    public String getTransNote() {
        return transNote;
    }


    public String getUserID() {
        return userID;
    }


    public void setPay(Boolean pay) {
        isPay = pay;
    }

    public void setTransAmount(double transAmount) {
        this.transAmount = transAmount;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public void setTransID(String transID) {
        this.transID = transID;
    }

    public void setTransNote(String transNote) {
        this.transNote = transNote;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}
