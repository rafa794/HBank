package com.hiberus.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import java.util.*;

@RestController
@RequestMapping("/api")
public class BankController {
    @Autowired
    private BankService bankService;
    private final List<Worker> createdWorkers = new ArrayList<>();

    @PostMapping("/workers")
    public ResponseEntity<String> addWorker(@RequestBody Worker worker) {
        try {
            bankService.addWorker(worker);
            return ResponseEntity.status(HttpStatus.CREATED).body("Trabajador agregado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al agregar el trabajador");
        }
    }


    @DeleteMapping("/workers")
    public ResponseEntity<String> removeWorker(@RequestBody Map<String, String> requestBody) {
        String firstName = requestBody.get("firstName");
        String lastName = requestBody.get("lastName");

        if (firstName == null || lastName == null) {
            return ResponseEntity.badRequest().body("Se requiere 'firstName' y 'lastName' en el JSON.");
        }

        boolean removed = bankService.removeWorker(firstName, lastName);

        if (removed) {
            String message = "El trabajador '" + firstName + " " + lastName + "' se ha eliminado correctamente.";
            return ResponseEntity.ok(message);
        } else {
            String errorMessage = "No se encontró al trabajador '" + firstName + " " + lastName + "'.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }


    @PutMapping("/workers/salary/{firstName}/{lastName}/{amount}")
    public ResponseEntity<String> increaseSalary(
            @PathVariable String firstName,
            @PathVariable String lastName,
            @PathVariable double amount,
            @RequestBody(required = false) Map<String, Object> requestBody) {
        // Verificamos si los valores se proporcionan en el cuerpo del JSON
        if (requestBody != null && requestBody.containsKey("firstName") && requestBody.containsKey("lastName") && requestBody.containsKey("amount")) {
            firstName = (String) requestBody.get("firstName");
            lastName = (String) requestBody.get("lastName");
            amount = (Double) requestBody.get("amount");
        }


        bankService.increaseSalary(firstName, lastName, amount);

        return ResponseEntity.ok("Salario actualizado con éxito");
    }

    @PutMapping("/workers/salary")
    public ResponseEntity<String> increaseSalaryWithJson(@RequestBody Map<String, Object> requestBody) {
        // Verificamos si los valores se proporcionan en el cuerpo del JSON
        if (requestBody != null && requestBody.containsKey("firstName") && requestBody.containsKey("lastName") && requestBody.containsKey("amount")) {
            String firstName = (String) requestBody.get("firstName");
            String lastName = (String) requestBody.get("lastName");
            // Convertimos el valor de amount a Double
            Double amount = Double.valueOf(requestBody.get("amount").toString());


            bankService.increaseSalary(firstName, lastName, amount);

            return ResponseEntity.ok("Salario actualizado con éxito");
        } else {
            return ResponseEntity.badRequest().body("Se requieren los campos 'firstName', 'lastName' y 'amount' en el JSON.");
        }
    }

    @PostMapping("/payments")
    public ResponseEntity<Object> paySalary(@RequestBody PaymentRequest paymentRequest, @RequestHeader("Authorization") String authorization) {
        if (!"Gandalf".equals(authorization)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso no autorizado");
        }

        boolean isPaid = bankService.paySalary(paymentRequest.getFirstName(), paymentRequest.getLastName(), paymentRequest.getSalary());

        if (isPaid) {
            return ResponseEntity.ok("Nómina pagada");
        } else {
            return ResponseEntity.badRequest().body("No se pudo pagar la nómina");
        }
    }

    @PostMapping("/transfers")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request) {
        Worker sender = bankService.getWorker(request.getSenderFirstName(), request.getSenderLastName());
        Worker receiver = bankService.getWorker(request.getReceiverFirstName(), request.getReceiverLastName());

        if (sender == null || receiver == null) {
            return ResponseEntity.badRequest().body("Trabajadores no encontrados");
        }

        String failReason = null; // Variable para almacenar la razón del fallo

        if (request.getAmount() % 10 != 0) {
            // La cantidad no es múltiplo de 10
            failReason = "La cantidad debe ser múltiplo de 10";
        } else if (receiver.isAntonio()) {
            // Transferencias de/para 'Antonios' no están permitidas
            failReason = "Transferencias de/para 'Antonios' no están permitidas";
        } else if (sender.getBalance() < request.getAmount()) {
            // Saldo insuficiente en la cuenta del remitente
            failReason = "Saldo insuficiente en la cuenta del remitente";
        } else {
            // Realizar la transferencia exitosa aquí
            boolean transferSuccess = bankService.transfer(sender, receiver, request.getAmount());

            if (!transferSuccess) {
                failReason = "Error al realizar la transferencia";
                System.out.println("La transferencia ha fallado en el control " + failReason);
            }
        }

        // Registramos la transferencia como exitosa solo si es exitosa
        if (failReason == null) {

            Transfer transfer = new Transfer(sender, receiver, request.getAmount(), new Date(), null);

            // Agregamos la transferencia exitosa solo a la lista de transferencias exitosas
            bankService.addSuccessfulTransfer(transfer);

            String message = "Transferencia realizada con éxito";
            return ResponseEntity.ok(message);
        } else {
            // Si hay una razón de falla, regresa una respuesta de error
            return ResponseEntity.badRequest().body("La transferencia ha fallado: " + failReason);
        }
    }


    @GetMapping("/workers")
    public ResponseEntity<List<Worker>> getAllWorkers() {
        List<Worker> workers = bankService.getAllWorkers();
        return ResponseEntity.ok(workers);
    }

    @GetMapping("/workers/{firstName}/{lastName}")
    public ResponseEntity<?> getWorker(@PathVariable(required = false) String firstName, @PathVariable(required = false) String lastName, @RequestBody(required = false) Map<String, String> requestBody) {
        if ((firstName == null || lastName == null) && (requestBody == null || !requestBody.containsKey("firstName") || !requestBody.containsKey("lastName"))) {
            return ResponseEntity.badRequest().body("Debes proporcionar 'firstName' y 'lastName' en la URL o en el cuerpo JSON.");
        }

        String workerFirstName = firstName != null ? firstName : requestBody.get("firstName");
        String workerLastName = lastName != null ? lastName : requestBody.get("lastName");

        Worker worker = bankService.getWorker(workerFirstName, workerLastName);

        if (worker != null) {
            return ResponseEntity.ok(worker);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/created-workers")
    public ResponseEntity<List<Worker>> getCreatedWorkers() {
        return ResponseEntity.ok(createdWorkers);
    }


    @GetMapping("/payments")
    public List<Payment> getAllPayments() {
        return bankService.getAllPayments();
    }

    @GetMapping("/failed-transfers")
    public ResponseEntity<List<FailedTransferResponse>> getFailedTransfers() {
        List<Transfer> failedTransfers = bankService.getFailedTransfers();
        System.out.println("Cantidad de transferencias fallidas: " + failedTransfers.size());
        List<FailedTransferResponse> responseList = new ArrayList<>();

        for (Transfer transfer : failedTransfers) {
            FailedTransferResponse response = new FailedTransferResponse();
            response.setType("Transferencia fallida");
            response.setDate(transfer.getDate());
            response.setAmount(transfer.getAmount());
            response.setMessage(transfer.getFailReason());

            // Buscamos o creamos el objeto Worker correspondiente
            Worker sender = bankService.getWorker(transfer.getSender().getFirstName(), transfer.getSender().getLastName());
            Worker receiver = bankService.getWorker(transfer.getReceiver().getFirstName(), transfer.getReceiver().getLastName());

            response.setSender(sender);
            response.setReceiver(receiver);

            responseList.add(response);
        }

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/payments-and-transfers")
    public ResponseEntity<List<PaymentAndTransferResponse>> getAllPaymentsAndTransfers() {
        List<Payment> payments = bankService.getAllPayments();
        List<Transfer> transfers = bankService.getAllTransfers();
        List<PaymentAndTransferResponse> responseList = new ArrayList<>();

        // Agregamos los pagos de nómina
        for (Payment payment : payments) {
            PaymentAndTransferResponse response = new PaymentAndTransferResponse();
            response.setType("Pago de nómina");
            response.setDate(payment.getPaymentDate());
            response.setAmount(payment.getNetAmount());
            response.setMessage("Nómina pagada");
            response.setReceiverName(payment.getWorker().getFullName());
            responseList.add(response);
        }

        // Agregamos todas las transferencias, ya sean exitosas o fallidas
        for (Transfer transfer : transfers) {
            PaymentAndTransferResponse response = new PaymentAndTransferResponse();
            response.setType("Transferencia");
            response.setDate(transfer.getDate());
            response.setAmount(transfer.getAmount());
            response.setMessage(transfer.getMessage());
            response.setSenderName(transfer.getSender().getFullName());
            response.setReceiverName(transfer.getReceiver().getFullName());
            responseList.add(response);
        }

        return ResponseEntity.ok(responseList);
    }
    @GetMapping("/successful-transfers")
    public ResponseEntity<List<Map<String, Object>>> getSuccessfulTransfers() {
        List<Transfer> successfulTransfers = bankService.getAllSuccessfulTransfers();
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (Transfer transfer : successfulTransfers) {
            Map<String, Object> response = new HashMap<>();
            response.put("senderName", transfer.getSender().getFullName());
            response.put("receiverName", transfer.getReceiver().getFullName());
            response.put("amount", transfer.getAmount());
            response.put("date", transfer.getDate());
            responseList.add(response);
        }

        return ResponseEntity.ok(responseList);
    }
}
