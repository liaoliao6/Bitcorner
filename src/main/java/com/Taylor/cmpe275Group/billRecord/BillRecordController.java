package com.Taylor.cmpe275Group.billRecord;

import com.Taylor.cmpe275Group.AppUser.AppUser;
import com.Taylor.cmpe275Group.AppUser.AppUserRepository;
import com.Taylor.cmpe275Group.AppUser.bankAccount.BankAccount;
import com.Taylor.cmpe275Group.Exception.MissingParameterException;
import com.Taylor.cmpe275Group.market.Market;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BillRecordController {

    @Autowired
    BillRecordRepository billRecordRepository;

    @Autowired
    BillService billService;

    @Autowired
    AppUserRepository appUserRepository;

    @PostMapping(path = "/bill",
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> createBill(
            @RequestBody BillRecord billRecord) {
        try {
            if (billRecord == null)
                throw new MissingParameterException("Missing billRecord");

            String sender = billRecord.getSender();
            String receiver = billRecord.getReceiver();
            String description = billRecord.getDescription();
            Float balance = billRecord.getBalance();
            String currency = billRecord.getCurrency();
            String dDate = billRecord.getDueDate().toString()+ "T23:59:59";
            LocalDateTime dueDate =  LocalDateTime.parse(dDate);

            System.out.println(sender + " " + receiver + " " + description+ " " + balance+ " " +currency+ " " +dueDate.toString()+ " " +LocalDateTime.now().toString());


            if(sender == null || sender.isEmpty()) {
                throw new MissingParameterException("Missing sender");
            }
            if(receiver == null || receiver.isEmpty()) {
                throw new MissingParameterException("Missing receiver");
            }
            if(description == null || description.isEmpty()){
                throw new MissingParameterException("Missing Description");
            }
            if(balance == null || balance <= 0) {
                throw new MissingParameterException("Missing Balance or Balance is smaller/equal to 0");
            }
            if(currency == null || currency.isEmpty()) {
                throw new MissingParameterException("Missing Currency");
            }
            if(dueDate.isBefore(LocalDateTime.now()) || dueDate == null){
                throw new MissingParameterException("Due date is invalid");
            }

            System.out.println(sender + " " + receiver + " " + description+ " " + balance+ " " +currency+ " " +dueDate.toString()+ " " +LocalDateTime.now().toString());
            //adding bill info to sender and receiver
            BillRecord newRecord = new BillRecord(sender, receiver, description, balance, currency, dueDate.toString(), LocalDateTime.now().toString(), null, "Waiting");
            AppUser senderUser = appUserRepository.findByEmail(sender);
            AppUser receiverUser = appUserRepository.findByEmail(receiver);
            newRecord.getAppUsers().add(senderUser);
            newRecord.getAppUsers().add(receiverUser);

            System.out.println();
            billRecordRepository.save(newRecord);

            billService.sendBillByEmail(sender, receiver, balance, currency, dueDate);

            return ResponseEntity.ok().body(newRecord);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MissingParameterException(e.getMessage());
        }
    }

    @PutMapping(path = "/payBill/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> updateBill(
            @PathVariable(value = "id") Long id) {
        try {
            BillRecord currentRecord = billRecordRepository.findById(id).get();
            if (currentRecord == null)
                throw new MissingParameterException("This bill does not exist");

            //paying bill
            String currency = currentRecord.getCurrency();
            Float balance = currentRecord.getBalance();
            AppUser receiverUser = appUserRepository.findByEmail(currentRecord.getReceiver());
            AppUser senderUser = appUserRepository.findByEmail(currentRecord.getSender());
            Boolean paid = billService.PayForBill(senderUser, receiverUser, currency, balance);
            if(!paid){
                throw new MissingParameterException("Insufficient Balance");
            }

            currentRecord.setStatus("Paid");
            billRecordRepository.save(currentRecord);
            //TODO: send email for bill notification
            billService.sendBillByEmailForPaying(currentRecord.getSender(), currentRecord.getReceiver());
            return ResponseEntity.ok().body(currentRecord);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping(path = "/rejectBill/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> rejectBill(
            @PathVariable(value = "id") Long id) {
        try {
            BillRecord currentRecord = billRecordRepository.findById(id).get();
            if (currentRecord == null)
                throw new MissingParameterException("This bill does not exist");


            currentRecord.setStatus("Rejected");
            billRecordRepository.save(currentRecord);
            //TODO: send email for bill notification
            billService.sendBillByEmailForRejecting(currentRecord.getSender(), currentRecord.getReceiver());
            return ResponseEntity.ok().body(currentRecord);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping(path = "/cancelBill/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> cancelBill(
            @PathVariable(value = "id") Long id) {
        try {
            BillRecord currentRecord = billRecordRepository.findById(id).get();
            if (currentRecord == null)
                throw new MissingParameterException("This bill does not exist");


            currentRecord.setStatus("Cancelled");
            billRecordRepository.save(currentRecord);
            //TODO: send email for bill notification
            billService.sendBillByEmailForCancelling(currentRecord.getSender(), currentRecord.getReceiver());
            return ResponseEntity.ok().body(currentRecord);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping(path = "/bill/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> editBill(
            @RequestBody BillRecord billRecord,
            @PathVariable(value = "id") Long id) {
        try {
            BillRecord currentRecord = billRecordRepository.findById(id).get();
            if (currentRecord == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This bill does not exist");
            String description = billRecord.getDescription();
            Float balance = billRecord.getBalance();
            String currency = billRecord.getCurrency();
            String dDate = billRecord.getDueDate()+"T23:59:59";
            LocalDateTime dueDate = LocalDateTime.parse(dDate);



            if(description != null && !description.isEmpty()) {
                currentRecord.setDescription(description);
            }
            if(balance != null) {
                currentRecord.setBalance(balance);
            }
            if(currency != null && !currency.isEmpty()) {
                currentRecord.setCurrency(currency);
            }
            if(dueDate != null) {
                currentRecord.setDueDate(dueDate.toString());
            }

            
            billRecordRepository.save(currentRecord);
            //TODO: send email for bill notification
            return ResponseEntity.ok().body(currentRecord);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(path = "/bill/receiver/{email}",
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<BillRecord> getReceiverBillInfo(@PathVariable(value = "email") String email) {

        List<BillRecord> billRecords = billRecordRepository.findAll();
        List<BillRecord> resBillRecords = new ArrayList<>();
        for(BillRecord billRecord: billRecords){
            if(billRecord.getReceiver().equals(email))
                resBillRecords.add(billRecord);
        }
        return resBillRecords;
    }

    @GetMapping(path = "/bill/sender/{email}",
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<BillRecord> getSenderBillInfo(@PathVariable(value = "email") String email) {

        List<BillRecord> billRecords = billRecordRepository.findAll();
        List<BillRecord> resBillRecords = new ArrayList<>();
        for(BillRecord billRecord: billRecords){
            if(billRecord.getSender().equals(email))
                resBillRecords.add(billRecord);
        }
        return resBillRecords;
    }


}
