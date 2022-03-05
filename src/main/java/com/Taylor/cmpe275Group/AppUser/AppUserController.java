package com.Taylor.cmpe275Group.AppUser;

import com.Taylor.cmpe275Group.AppUser.bankAccount.BankAccount;
import com.Taylor.cmpe275Group.Exception.MissingParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AppUserController {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AppUserService appUserService;

    @PostMapping(path = "/appUser2", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> createAppUser(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "nickName") String nickName,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "bankName") String bankName,
            @RequestParam(value = "country") String country,
            @RequestParam(value = "accountNumber") Long accountNumber,
            @RequestParam(value = "ownerNaturalName") String ownerNaturalName,
            @RequestParam(value = "address") String address,
            @RequestParam(value = "primaryCurrency") String primaryCurrency,
            @RequestParam(value = "balanceOfUSD", required = false) Float balanceOfUSD,
            @RequestParam(value = "balanceOfEUR", required = false) Float balanceOfEUR,
            @RequestParam(value = "balanceOfGBP", required = false) Float balanceOfGBP,
            @RequestParam(value = "balanceOfINR", required = false) Float balanceOfINR,
            @RequestParam(value = "balanceOfRMB", required = false) Float balanceOfRMB,
            @RequestParam(value = "balanceOfBitcoin", required = false) Float balanceOfBitcoin) {
        try{
            if(email == null || email.isEmpty()||nickName == null || nickName.isEmpty() || password == null || password.isEmpty()
            ||bankName == null || bankName.isEmpty()||country == null || country.isEmpty()||accountNumber == null || ownerNaturalName == null || ownerNaturalName.isEmpty()
            ||address == null || address.isEmpty()||primaryCurrency == null || primaryCurrency.isEmpty())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required parameters");

            if(appUserRepository.findByEmail(email) != null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
            }else if(appUserRepository.findByNickName(nickName) != null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NickName already exists");
            }
            AppUser appUser = new AppUser(nickName, email, password, AppUserRole.USER);


            BankAccount bankAccount = new BankAccount(bankName,country,accountNumber,ownerNaturalName,address,
                    primaryCurrency,balanceOfUSD==null?0:balanceOfUSD,balanceOfEUR==null?0:balanceOfEUR,
                    balanceOfGBP==null?0:balanceOfGBP,balanceOfINR==null?0:balanceOfINR,balanceOfRMB==null?0:balanceOfRMB,
                    balanceOfBitcoin==null?0:balanceOfBitcoin);
            appUser.setBankAccount(bankAccount);
            appUserRepository.save(appUser);
            return ResponseEntity.ok().body(appUser);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request");
        }
    }



    @PostMapping(path = "/appUser", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<AppUser> createAppUser(
           @RequestBody AppUser newAppUser) {
        try{
            String email = newAppUser.getEmail();
            String nickName = newAppUser.getNickName();
            String password = newAppUser.getPassword();
            BankAccount bank =  newAppUser.getBankAccount();
            String bankName = bank.getBankName();
            String country = bank.getCountry();
            Long accountNumber = bank.getAccountNumber();
            String ownerNaturalName = bank.getOwnerNaturalName();
            String address = bank.getAddress();
            String primaryCurrency = bank.getPrimaryCurrency();
            if(email == null || email.isEmpty()|| password == null || password.isEmpty()
                    ||bankName == null || bankName.isEmpty()||country == null || country.isEmpty()||accountNumber == null || ownerNaturalName == null || ownerNaturalName.isEmpty()
                    ||address == null || address.isEmpty()||primaryCurrency == null || primaryCurrency.isEmpty())
                //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required parameters");
                throw new MissingParameterException("Missing parameters");
            if(appUserRepository.findByEmail(email) != null){
                //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
                throw new MissingParameterException("Email Already exists");
            }
            else if(appUserRepository.findByNickName(nickName) != null){
                //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NickName already exists");
                throw new MissingParameterException("Nickname already exists");
            }
            if(nickName == null){
                nickName = email;
            }

            AppUser appUser = new AppUser(nickName, email, password, AppUserRole.USER);


            Float balanceOfUSD = bank.getBalanceOfUSD();
            Float balanceOfEUR = bank.getBalanceOfEUR();
            Float balanceOfRMB = bank.getBalanceOfRMB();
            Float balanceOfINR= bank.getBalanceOfINR();
            Float balanceOfGBP= bank.getBalanceOfGBP();
            Float balanceOfBitcoin= bank.getBalanceOfBitcoin();

            BankAccount bankAccount = new BankAccount(bankName,country,accountNumber,ownerNaturalName,address,
                    primaryCurrency,balanceOfUSD==null?0:balanceOfUSD,balanceOfEUR==null?0:balanceOfEUR,
                    balanceOfGBP==null?0:balanceOfGBP,balanceOfINR==null?0:balanceOfINR,balanceOfRMB==null?0:balanceOfRMB,
                    balanceOfBitcoin==null?0:balanceOfBitcoin);
            appUser.setBankAccount(bankAccount);
            appUserRepository.save(appUser);
            //return ResponseEntity.ok().body(appUser);
            return new ResponseEntity<>(appUser, HttpStatus.CREATED);
        }catch(Exception e){
            e.printStackTrace();
            return  new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }




    @PostMapping(path= "/appUser/bank/{email}")
    public ResponseEntity<AppUser> createBank(@RequestBody BankAccount bankAccount,  @PathVariable(value = "email") String email){
        String bankName = bankAccount.getBankName();
        String country = bankAccount.getCountry();
        Long accountNumber = bankAccount.getAccountNumber();
        String ownerNaturalName = bankAccount.getOwnerNaturalName();
        String address = bankAccount.getAddress();
        String primaryCurrency = bankAccount.getPrimaryCurrency();
        Float balanceOfUSD = bankAccount.getBalanceOfUSD();
        Float balanceOfEUR = bankAccount.getBalanceOfEUR();
        Float balanceOfGBP = bankAccount.getBalanceOfGBP();
        Float balanceOfINR = bankAccount.getBalanceOfINR();
        Float balanceOfRMB = bankAccount.getBalanceOfRMB();
        Float balanceOfBitcoin = bankAccount.getBalanceOfBitcoin();
        if(bankName == null || bankName.isEmpty()){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        if(country == null || country.isEmpty()){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        if(accountNumber == null){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        if(ownerNaturalName == null || ownerNaturalName.isEmpty()){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        if(address == null || address.isEmpty()){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        if(primaryCurrency == null || primaryCurrency.isEmpty()){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        balanceOfUSD = balanceOfUSD == null ? 0: balanceOfUSD;
        balanceOfEUR = balanceOfEUR == null ? 0: balanceOfEUR;
        balanceOfGBP = balanceOfGBP == null ? 0: balanceOfGBP;
        balanceOfINR = balanceOfINR == null ? 0: balanceOfINR;
        balanceOfRMB = balanceOfRMB == null ? 0: balanceOfRMB;
        balanceOfBitcoin = balanceOfBitcoin == null ? 0: balanceOfBitcoin;
        bankAccount = new BankAccount(bankName, country, accountNumber, ownerNaturalName, address, primaryCurrency, balanceOfUSD, balanceOfEUR, balanceOfGBP, balanceOfINR, balanceOfRMB, balanceOfBitcoin);
        AppUser appUser = appUserService.createBank(email, bankAccount);
        return new ResponseEntity<>(appUser,HttpStatus.CREATED);
    }

    @PutMapping(path= "/appUser/bank/{email}")
    public ResponseEntity<AppUser> updateBank(@RequestBody BankAccount bankAccount,  @PathVariable(value = "email") String email){
        AppUser appUser = appUserService.updateBank(email, bankAccount);
        return new ResponseEntity<>(appUser,HttpStatus.OK);
    }

    @GetMapping(path = "/appUser/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getEmployee(
            @PathVariable(value = "id") Long id) {
        try {
            AppUser appUser = appUserRepository.findById(id).get();
            if(appUser == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user does not exist");
            }
            return ResponseEntity.ok().body(appUser);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parameters");
        }
    }

    @GetMapping(path = "/appUser/bank/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getBankInfo(
            @PathVariable(value = "id") Long id) {
        try {
            AppUser appUser = appUserRepository.findById(id).get();
            if(appUser == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user does not exist");
            }
            return ResponseEntity.ok().body(appUser.getBankAccount());
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parameters");
        }
    }

    @GetMapping(path = "/appUser/email/{email}",
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getBankInfobyEmail(
            @PathVariable(value = "email") String email) {
        try {
            AppUser appUser = appUserRepository.findByEmail(email);
            if(appUser == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user does not exist");
            }
            return ResponseEntity.ok().body(appUser.getBankAccount());
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parameters");
        }
    }

    @PutMapping(path = "/appUser/bankBalance/{email}",
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> updateBankBalance(
            @RequestBody BankAccount bankAccount,
            @PathVariable(value = "email") String email){
        try{
            if(email == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required parameters");
            AppUser appUser = appUserRepository.findByEmail(email);

            if(appUser == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Such appUser does not exist");
            }

            BankAccount userBankAccount = appUser.getBankAccount();
            if(bankAccount.getBalanceOfUSD() != null){
                if(bankAccount.getBalanceOfUSD() + userBankAccount.getBalanceOfUSD() >= 0)
                    userBankAccount.setBalanceOfUSD(userBankAccount.getBalanceOfUSD() + (bankAccount.getBalanceOfUSD()==null?0:bankAccount.getBalanceOfUSD()));
                else
                    throw new MissingParameterException("Invalid balance");
            }
            if(bankAccount.getBalanceOfEUR() != null){
                if(bankAccount.getBalanceOfEUR() + userBankAccount.getBalanceOfEUR() >= 0)
                    userBankAccount.setBalanceOfEUR(userBankAccount.getBalanceOfEUR() + (bankAccount.getBalanceOfEUR()==null?0:bankAccount.getBalanceOfEUR()));
                else
                    throw new MissingParameterException("Invalid balance");
            }
            if(bankAccount.getBalanceOfGBP() != null){
                if(bankAccount.getBalanceOfGBP() + userBankAccount.getBalanceOfGBP() >= 0)
                    userBankAccount.setBalanceOfGBP(userBankAccount.getBalanceOfGBP() + (bankAccount.getBalanceOfGBP()==null?0:bankAccount.getBalanceOfGBP()));
                else
                    throw new MissingParameterException("Invalid balance");
            }
            if(bankAccount.getBalanceOfINR() != null){
                if(bankAccount.getBalanceOfINR() + userBankAccount.getBalanceOfINR() >= 0)
                    userBankAccount.setBalanceOfINR(userBankAccount.getBalanceOfINR() + (bankAccount.getBalanceOfINR()==null?0:bankAccount.getBalanceOfINR()));
                else
                    throw new MissingParameterException("Invalid balance");
            }
            if(bankAccount.getBalanceOfRMB() != null){
                if(bankAccount.getBalanceOfRMB() + userBankAccount.getBalanceOfRMB() >= 0)
                    userBankAccount.setBalanceOfRMB(userBankAccount.getBalanceOfRMB() + (bankAccount.getBalanceOfRMB()==null?0:bankAccount.getBalanceOfRMB()));
                else
                    throw new MissingParameterException("Invalid balance");
            }
            if(bankAccount.getBalanceOfBitcoin() != null){
                if(bankAccount.getBalanceOfBitcoin() + userBankAccount.getBalanceOfBitcoin() >= 0)
                    userBankAccount.setBalanceOfBitcoin(userBankAccount.getBalanceOfBitcoin() + (bankAccount.getBalanceOfBitcoin()==null?0:bankAccount.getBalanceOfBitcoin()));
                else
                    throw new MissingParameterException("Invalid balance");
            }

            appUser.setBankAccount(userBankAccount);
            appUserRepository.save(appUser);
            return ResponseEntity.ok().body(userBankAccount);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

       // @PutMapping(path = "/appUser/update",produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//        @PutMapping(path = '/appUser/update')
//        public ResponseEntity<BankAccount> updateBankAcount(@RequestBody BankAccount bankAccount) {
//
//            Optional<AppUser> existAppUser = appUserRepository.findById(id);
//            AppUser appUser = existAppUser.get();
//
//            if(!existAppUser.isPresent()){
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Such appUser does not exist");
//            }
//
//            appUser.setBankAccount(bankAccount);
//            return new ResponseEntity<>(updateBankAcount, HttpStatus.OK);
//        }
    }
}
