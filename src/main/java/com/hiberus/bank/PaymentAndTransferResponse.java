package com.hiberus.bank;
import java.util.Date;

public class PaymentAndTransferResponse {
    private String type;
    private Date date;
    private double amount;
    private String message;
    private String senderName;
    private String receiverName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderName() { // Cambiamos el nombre del método y el tipo de retorno
        return senderName;
    }

    public void setSenderName(String senderName) { // Cambiamos el nombre del método y el tipo del argumento
        this.senderName = senderName;
    }

    public String getReceiverName() { // Cambiamos el nombre del método y el tipo de retorno
        return receiverName;
    }

    public void setReceiverName(String receiverName) { // Cambiamos el nombre del método y el tipo del argumento
        this.receiverName = receiverName;
    }
}
