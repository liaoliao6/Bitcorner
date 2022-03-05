package com.Taylor.cmpe275Group.billRecord;

import com.Taylor.cmpe275Group.AppUser.AppUser;
import com.Taylor.cmpe275Group.AppUser.AppUserRepository;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Transactional
public class BillService {
    @Autowired
    AppUserRepository appUserRepository;

    public void sendBillByEmail(String sender, String receiver, Float balance, String currency, LocalDateTime dueDate) throws IOException {
        Email from = new Email("zijianguan0204@gmail.com");
        Email to = new Email(receiver);
        String subject = "Bill Notification from Bitcornor";
        String mailContent = "Dear user, you have a bill request from "+ sender + " asking for " + balance + " " + currency + ". The due date is " + dueDate + ".";
        Content content = new Content("text/plain", mailContent);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();

        try{
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        }catch(IOException ex){
            throw ex;
        }
    }

    public void sendBillByEmailForPaying(String sender, String receiver) throws IOException {
        Email from = new Email("zijianguan0204@gmail.com");
        Email to = new Email(sender);
        String subject = "Bill Notification from Bitcornor";
        String mailContent = "Dear user, your have a bill from "+ receiver + " has been paid ";
        Content content = new Content("text/plain", mailContent);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();

        try{
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        }catch(IOException ex){
            throw ex;
        }
    }

    public void sendBillByEmailForCancelling(String sender, String receiver) throws IOException {
        Email from = new Email("zijianguan0204@gmail.com");
        Email to = new Email(receiver);
        String subject = "Bill Notification from Bitcornor";
        String mailContent = "Dear user, your have a bill from "+ sender + " has been cancelled ";
        Content content = new Content("text/plain", mailContent);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();

        try{
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        }catch(IOException ex){
            throw ex;
        }
    }

    public void sendBillByEmailForRejecting(String sender, String receiver) throws IOException {
        Email from = new Email("zijianguan0204@gmail.com");
        Email to = new Email(receiver);
        String subject = "Bill Notification from Bitcornor";
        String mailContent = "Dear user, your have a bill from "+ sender + " has been rejected ";
        Content content = new Content("text/plain", mailContent);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();

        try{
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        }catch(IOException ex){
            throw ex;
        }
    }

    public Boolean PayForBill(AppUser sender, AppUser receiver, String currency, Float balance){
        if(currency.equals("USD")){
            if(receiver.getBankAccount().getBalanceOfUSD() > balance){
                receiver.getBankAccount().setBalanceOfUSD(receiver.getBankAccount().getBalanceOfUSD()- balance);
                sender.getBankAccount().setBalanceOfUSD(sender.getBankAccount().getBalanceOfUSD()+ balance);
                appUserRepository.save(sender);
                appUserRepository.save(receiver);
            }else{
                return false;
            }
        }else if(currency.equals("EUR")){
            if(receiver.getBankAccount().getBalanceOfEUR() > balance){
                receiver.getBankAccount().setBalanceOfEUR(receiver.getBankAccount().getBalanceOfEUR()- balance);
                sender.getBankAccount().setBalanceOfEUR(sender.getBankAccount().getBalanceOfEUR()+ balance);
                appUserRepository.save(sender);
                appUserRepository.save(receiver);
            }else{
                return false;
            }
        }else if(currency.equals("GBP")){
            if(receiver.getBankAccount().getBalanceOfGBP() > balance){
                receiver.getBankAccount().setBalanceOfGBP(receiver.getBankAccount().getBalanceOfGBP()- balance);
                sender.getBankAccount().setBalanceOfGBP(sender.getBankAccount().getBalanceOfGBP()+ balance);
                appUserRepository.save(sender);
                appUserRepository.save(receiver);
            }else{
                return false;
            }
        }else if(currency.equals("INR")){
            if(receiver.getBankAccount().getBalanceOfINR() > balance){
                receiver.getBankAccount().setBalanceOfINR(receiver.getBankAccount().getBalanceOfINR()- balance);
                sender.getBankAccount().setBalanceOfINR(sender.getBankAccount().getBalanceOfINR()+ balance);
                appUserRepository.save(sender);
                appUserRepository.save(receiver);
            }else{
                return false;
            }
        }else if(currency.equals("RMB")){
            if(receiver.getBankAccount().getBalanceOfRMB() > balance){
                receiver.getBankAccount().setBalanceOfRMB(receiver.getBankAccount().getBalanceOfRMB()- balance);
                sender.getBankAccount().setBalanceOfRMB(sender.getBankAccount().getBalanceOfRMB()+ balance);
                appUserRepository.save(sender);
                appUserRepository.save(receiver);
            }else{
                return false;
            }
        }else if(currency.equals("Bitcoin")){
            if(receiver.getBankAccount().getBalanceOfBitcoin() > balance){
                receiver.getBankAccount().setBalanceOfBitcoin(receiver.getBankAccount().getBalanceOfBitcoin()- balance);
                sender.getBankAccount().setBalanceOfBitcoin(sender.getBankAccount().getBalanceOfBitcoin()+ balance);
                appUserRepository.save(sender);
                appUserRepository.save(receiver);
            }else{
                return false;
            }
        }
        return false;
    }
}
