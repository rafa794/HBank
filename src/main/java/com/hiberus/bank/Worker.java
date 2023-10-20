package com.hiberus.bank;

public class Worker {
    private String firstName;
    private String lastName;

    private double salary;
    private double balance;
    private boolean isAntonio;

    public Worker(String firstName, String lastName, double salary){
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.balance = 0;
    }

    public Worker() {
        // Constructor predeterminado Jackson
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public double getSalary(){
        return salary;
    }

    public double getBalance(){
        return balance;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setSalary(double salary){
        this.salary = salary;
    }

    public void setBalance(double balance){
        this.balance = balance;
    }

    public boolean isAntonio() {
        return firstName.equals("Antonio");
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
