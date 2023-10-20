package com.hiberus.bank;
import java.util.Date;

public class FailedTransferResponse {
    private String type;
    private Date date;
    private double amount;
    private String message;
    private Worker sender;
    private Worker receiver;

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

    public Worker getSender() { // Cambiar de getSenderName() a getSender()
        return sender;
    }

    public void setSender(Worker sender) { // Cambiar de setSenderName() a setSender()
        this.sender = sender;
    }

    public Worker getReceiver() { // Cambiar de getReceiverName() a getReceiver()
        return receiver;
    }

    public void setReceiver(Worker receiver) { // Cambiar de setReceiverName() a setReceiver()
        this.receiver = receiver;
    }
}
