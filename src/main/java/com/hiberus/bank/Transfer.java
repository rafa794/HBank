package com.hiberus.bank;

import java.util.Date;

public class Transfer {
    private Worker sender;
    private Worker receiver;
    private double amount;
    private String id;
    private boolean successful;
    private String failReason;
    private Date date;

    public Transfer(Worker sender, Worker receiver, double amount, Date date, String failReason) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.successful = false; // Por defecto, se considera fallida
        this.date = date;
        this.failReason = failReason; // Inicializamos la razón de la falla con el valor proporcionado
        this.id = null; // Deja el ID en nulo y permite que BankService lo genere
    }

    public String getMessage() {
        if (successful) {
            return "Transferencia realizada con éxito";
        } else {
            return "No tienes saldo suficiente para realizar esta transferencia";
        }
    }

    public Worker getSender() {
        return sender;
    }

    public Worker getReceiver() {
        return receiver;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void markAsFailed(String reason) {
        this.successful = false; // Marcar la transferencia como fallida
        this.failReason = reason; // Establecer la razón del fallo
    }

    public Date getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }
}
