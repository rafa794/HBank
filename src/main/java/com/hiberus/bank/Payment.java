package com.hiberus.bank;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Payment {
    private Worker worker;
    private Date paymentDate;
    private double netAmount;

    public Payment(Worker worker, Date paymentDate, double netAmount){
        this.worker = worker;
        this.paymentDate = paymentDate;
        this.netAmount = netAmount;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    Date getPaymentDate(){
        return paymentDate;
    }

    double getNetAmount(){
        return netAmount;
    }

    Worker getWorker(){
        return worker;
    }
}
