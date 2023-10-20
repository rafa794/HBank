package com.hiberus.bank;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class BankService {
    private List<Worker> workers = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();
    private List<Transfer> transfers = new ArrayList<>();
    private List<Transfer> successfulTransfers = new ArrayList<>();

    public void addWorker(Worker worker) {
        workers.add(worker);
    }

    public boolean removeWorker(String firstName, String lastName) {
        Iterator<Worker> iterator = workers.iterator();
        while (iterator.hasNext()) {
            Worker worker = iterator.next();
            if (worker.getFirstName().equals(firstName) && worker.getLastName().equals(lastName)) {
                iterator.remove();
                return true; // Trabajador eliminado con éxito.
            }
        }
        return false; // Trabajador no encontrado.
    }

    public void increaseSalary(String firstName, String lastName, double amount) {
        workers.forEach(worker -> {
            if (worker.getFirstName().equals(firstName) && worker.getLastName().equals(lastName)) {
                double currentSalary = worker.getSalary();
                worker.setSalary(currentSalary + amount);
            }
        });
    }

    public boolean paySalary(String firstName, String lastName, double salary) {
        Worker worker = getWorker(firstName, lastName);

        if (worker == null) {
            return false; // Trabajador no encontado.
        }

        // Chequeamos s el trabajador es Antonio (EE1)
        if (worker.isAntonio()) {
            System.out.println("El trabajador es Antonio, no se paga la nómina.");
            return false;
        }

        // Actualizamos el balance
        double currentBalance = worker.getBalance();
        worker.setBalance(currentBalance + salary);
        payments.add(new Payment(worker, new Date(), salary));

        // Debugging
        System.out.println("Before update: " + currentBalance);

        // Debugging
        System.out.println("paySalary called for: " + worker.getFirstName() + " " + worker.getLastName());

        System.out.println("Salario: " + salary);

        System.out.println("Balance actualizado: " + worker.getBalance());

        return true;
    }

    public boolean transfer(Worker sender, Worker receiver, double amount) {
        boolean isTransferSuccessful = false;
        String failReason = null;

        if (amount % 10 != 0) {
            System.out.println("Transfer marked as failed: Amount not multiple of 10");
            failReason = "La cantidad debe ser múltiplo de 10";
        } else if (sender.isAntonio() || receiver.isAntonio()) {
            failReason = "Transferencias de/para 'Antonios' no están permitidas";
        } else if (sender.getBalance() < amount) {
            failReason = "Saldo insuficiente en la cuenta del remitente";
        } else {
            Date currentDate = new Date();

            Transfer transfer = new Transfer(sender, receiver, amount, currentDate, null);

            sender.setBalance(sender.getBalance() - amount);
            receiver.setBalance(receiver.getBalance() + amount);

            isTransferSuccessful = true;
        }

        if (!isTransferSuccessful) {
            Transfer failedTransfer = new Transfer(sender, receiver, amount, new Date(), failReason);
            failedTransfer.markAsFailed(failReason);
            transfers.add(failedTransfer);
            System.out.println("Transferencia fallida agregada a failedTransfers: " + failedTransfer);
        } else {
            System.out.println("Transferencia exitosa: " + sender.getFullName() + " a " + receiver.getFullName() + " - Monto: " + amount);
        }

        return isTransferSuccessful;
    }


    public String generateTransferId(Worker sender, Worker receiver, double amount, Date currentDate) {

        return sender.getFirstName() + "_" + sender.getLastName() + "_" + receiver.getFirstName() + "_" + receiver.getLastName() + "_" + amount + "_" + currentDate.getTime();
    }
    private boolean isTransferAlreadyAdded(String transferId) {
        for (Transfer transfer : transfers) {
            // Verificamos si la transferencia ya existe
            if (transfer.getId().equals(transferId)) {
                return true;
            }
        }
        return false;
    }


    public List<Worker> getAllWorkers() {
        return workers;
    }

    public Worker getWorker(String firstName, String lastName) {
        for (Worker worker : workers) {
            if (worker.getFirstName().equals(firstName) && worker.getLastName().equals(lastName)) {
                return worker;
            }
        }
        return null; // Trabajador no encontrado
    }

    public List<Payment> getAllPayments() {
        return payments;
    }

    public List<Transfer> getAllTransfers() {
        return transfers;
    }

    public List<Transfer> getFailedTransfers() {
        List<Transfer> failedTransfers = new ArrayList<>();
        for (Transfer transfer : transfers) {
            if (!transfer.isSuccessful()) {
                failedTransfers.add(transfer);
                System.out.println("Transferencia fallida detectada:");
                System.out.println("Fecha: " + transfer.getDate());
                System.out.println("Monto: " + transfer.getAmount());
                System.out.println("Razón de falla: " + transfer.getFailReason());
            }
        }
        return failedTransfers;
    }

    public void addSuccessfulTransfer(Transfer transfer) {
        successfulTransfers.add(transfer);
    }

    public List<Transfer> getAllSuccessfulTransfers() {
        return successfulTransfers;
    }

    public void addFailedTransfer(Transfer transfer) {
        transfer.markAsFailed("Saldo insuficiente en la cuenta del remitente"); // Marcar la transferencia como fallida
        transfers.add(transfer); // Agregar la transferencia fallida a la lista de transferencias
    }
}
