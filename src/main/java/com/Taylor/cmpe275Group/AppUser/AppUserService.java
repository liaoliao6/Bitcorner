package com.Taylor.cmpe275Group.AppUser;
//
//import com.Taylor.cmpe275Group.registration.token.ConfirmationToken;
//import com.Taylor.cmpe275Group.registration.token.ConfirmationTokenService;
//import lombok.AllArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Service
//@AllArgsConstructor
//public class AppUserService implements UserDetailsService {
//
//    private final AppUserRepository appUserRepository;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//    private final ConfirmationTokenService confirmationTokenService;
//
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        return appUserRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("The user with email " + email + " not found"));
//    }
//
//    public String signUpUser(AppUser appUser){
//        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();
//        if(userExists){
//            //TODO: check of attributes are the same and if email not confirmed send another confirmation email
//            throw  new IllegalArgumentException("email altready Exist");
//        }
//
//        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
//        appUser.setPassword(encodedPassword);
//
//        appUserRepository.save(appUser);
//        String token = UUID.randomUUID().toString();
//        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),LocalDateTime.now().plusMinutes(15),appUser);
//        confirmationTokenService.saveConfirmationToken(confirmationToken);
//        // Send Email TODO
//
//        return "It works";
//    }
//
//    public int enableAppUser(String email) {
//        return appUserRepository.enableAppUser(email);
//    }
//
//
//}


import com.Taylor.cmpe275Group.AppUser.AppUserRepository;
import com.Taylor.cmpe275Group.AppUser.bankAccount.BankAccount;
import com.Taylor.cmpe275Group.market.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional

public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    MarketService marketService;

    public AppUser addUser(AppUser appUser) {
        //employee.setEmployeeCode(UUID.randomUUID().toString());
        return appUserRepository.save(appUser);
    }

    public List<AppUser> findAllUser() {
        return appUserRepository.findAll();
    }

    public AppUser updateUser(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    public AppUser createBank(String email, BankAccount bankAccount) {
        AppUser appUser = appUserRepository.findByEmail(email);


        appUser.setBankAccount(bankAccount);
        return appUserRepository.save(appUser);
    }


    public AppUser updateBank(String email, BankAccount bankAccount) {
        AppUser appUser = appUserRepository.findByEmail(email);
        BankAccount old = appUser.getBankAccount();
        BankAccount newB = new BankAccount(
                bankAccount.getBankName() == null ? old.getBankName() : bankAccount.getBankName(),
                bankAccount.getCountry() == null ? old.getCountry() : bankAccount.getCountry(),
                bankAccount.getAccountNumber() == null ? old.getAccountNumber() : bankAccount.getAccountNumber(),
                bankAccount.getOwnerNaturalName() == null ? old.getOwnerNaturalName() : bankAccount.getOwnerNaturalName(),
                bankAccount.getAddress() == null ? old.getAddress() : bankAccount.getAddress(),
                bankAccount.getPrimaryCurrency() == null ? old.getPrimaryCurrency() : bankAccount.getPrimaryCurrency(),
                bankAccount.getBalanceOfUSD() == null ? old.getBalanceOfUSD() : bankAccount.getBalanceOfUSD(),
                bankAccount.getBalanceOfEUR() == null ? old.getBalanceOfEUR() : bankAccount.getBalanceOfEUR(),
                bankAccount.getBalanceOfGBP() == null ? old.getBalanceOfGBP() : bankAccount.getBalanceOfGBP(),
                bankAccount.getBalanceOfINR() == null ? old.getBalanceOfINR() : bankAccount.getBalanceOfINR(),
                bankAccount.getBalanceOfRMB() == null ? old.getBalanceOfRMB() : bankAccount.getBalanceOfRMB(),
                bankAccount.getBalanceOfBitcoin() == null ? old.getBalanceOfBitcoin() : bankAccount.getBalanceOfBitcoin()
        );
        appUser.setBankAccount(newB);
        return appUserRepository.save(appUser);
    }

    public AppUser findUserByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    public void deleteUser(Long id){
        appUserRepository.deleteById(id);
    }



    public boolean payForMarketOrder(AppUser buyerUser, String currency, Float bitcoinAmount){
        BankAccount userBankAccount = buyerUser.getBankAccount();
        Float LTP;
        if(currency.equals("USD")){
            LTP = marketService.getLTP("USD");
            Float totalPrice = LTP*bitcoinAmount;
            if(userBankAccount.getBalanceOfUSD() < totalPrice){
                return false;
            }
            userBankAccount.setBalanceOfUSD(userBankAccount.getBalanceOfUSD()-bitcoinAmount*LTP);
            userBankAccount.setBalanceOfBitcoin(userBankAccount.getBalanceOfBitcoin()+bitcoinAmount);
            return true;
        }
        if(currency.equals("EUR")){
            LTP = marketService.getLTP("EUR");
            Float totalPrice = LTP*bitcoinAmount;
            if(userBankAccount.getBalanceOfUSD() < totalPrice){
                return false;
            }
            userBankAccount.setBalanceOfEUR(userBankAccount.getBalanceOfEUR()-bitcoinAmount*LTP);
            userBankAccount.setBalanceOfBitcoin(userBankAccount.getBalanceOfBitcoin()+bitcoinAmount);
            return true;
        }
        if(currency.equals("GBP")){
            LTP = marketService.getLTP("GBP");
            Float totalPrice = LTP*bitcoinAmount;
            if(userBankAccount.getBalanceOfGBP() < totalPrice){
                return false;
            }
            userBankAccount.setBalanceOfGBP(userBankAccount.getBalanceOfGBP()-bitcoinAmount*LTP);
            userBankAccount.setBalanceOfBitcoin(userBankAccount.getBalanceOfBitcoin()+bitcoinAmount);
            return true;
        }
        if(currency.equals("INR")){
            LTP = marketService.getLTP("INR");
            Float totalPrice = LTP*bitcoinAmount;
            if(userBankAccount.getBalanceOfINR() < totalPrice){
                return false;
            }
            userBankAccount.setBalanceOfINR(userBankAccount.getBalanceOfINR()-bitcoinAmount*LTP);
            userBankAccount.setBalanceOfBitcoin(userBankAccount.getBalanceOfBitcoin()+bitcoinAmount);
            return true;
        }
        if(currency.equals("RMB")){
            LTP = marketService.getLTP("RMB");
            Float totalPrice = LTP*bitcoinAmount;
            if(userBankAccount.getBalanceOfRMB() < totalPrice){
                return false;
            }
            userBankAccount.setBalanceOfRMB(userBankAccount.getBalanceOfRMB()-bitcoinAmount*LTP);
            userBankAccount.setBalanceOfBitcoin(userBankAccount.getBalanceOfBitcoin()+bitcoinAmount);
            return true;
        }
        return false;
    }

    public boolean payForLimitOrder(AppUser buyerUser, String currency, Float bitcoinPrice, Float bitcoinAmount){
        BankAccount userBankAccount = buyerUser.getBankAccount();
        Float totalPrice = bitcoinPrice*bitcoinAmount;
        if(currency.equals("USD")){
            if(userBankAccount.getBalanceOfUSD() < totalPrice){
                return false;
            }
            userBankAccount.setBalanceOfUSD(userBankAccount.getBalanceOfUSD()-bitcoinAmount*bitcoinPrice);
            return true;
        }
        if(currency.equals("EUR")){
            if(userBankAccount.getBalanceOfEUR() < totalPrice){
                return false;
            }
            userBankAccount.setBalanceOfEUR(userBankAccount.getBalanceOfEUR()-bitcoinAmount*bitcoinPrice);
            return true;
        }
        if(currency.equals("GBP")){
            if(userBankAccount.getBalanceOfGBP() < totalPrice){
                return false;
            }
            userBankAccount.setBalanceOfGBP(userBankAccount.getBalanceOfGBP()-bitcoinAmount*bitcoinPrice);
            return true;
        }
        if(currency.equals("INR")){
            if(userBankAccount.getBalanceOfINR() < totalPrice){
                return false;
            }
            userBankAccount.setBalanceOfINR(userBankAccount.getBalanceOfINR()-bitcoinAmount*bitcoinPrice);
            return true;
        }
        if(currency.equals("RMB")){
            if(userBankAccount.getBalanceOfRMB() < totalPrice){
                return false;
            }
            userBankAccount.setBalanceOfRMB(userBankAccount.getBalanceOfRMB()-bitcoinAmount*bitcoinPrice);
            return true;
        }
        return false;
    }

    public void gainFromLimitOrder(AppUser sellerUser, String currency, Float bitcoinPrice, Float bitcoinAmount){
        BankAccount userBankAccount = sellerUser.getBankAccount();
        Float totalPrice = bitcoinPrice*bitcoinAmount;
        if(currency.equals("USD")){
            userBankAccount.setBalanceOfUSD(userBankAccount.getBalanceOfUSD()+totalPrice);
        }
        if(currency.equals("EUR")){
            userBankAccount.setBalanceOfEUR(userBankAccount.getBalanceOfEUR()+totalPrice);
        }
        if(currency.equals("GBP")){
            userBankAccount.setBalanceOfGBP(userBankAccount.getBalanceOfGBP()+totalPrice);
        }
        if(currency.equals("INR")){
            userBankAccount.setBalanceOfINR(userBankAccount.getBalanceOfINR()+totalPrice);
        }
        if(currency.equals("RMB")){
            userBankAccount.setBalanceOfRMB(userBankAccount.getBalanceOfRMB()+totalPrice);
        }
    }
}
