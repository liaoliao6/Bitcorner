package com.Taylor.cmpe275Group.orderRecord;

import com.Taylor.cmpe275Group.AppUser.AppUser;
import com.Taylor.cmpe275Group.AppUser.AppUserRepository;
import com.Taylor.cmpe275Group.AppUser.AppUserService;
import com.Taylor.cmpe275Group.AppUser.bankAccount.BankAccount;
import com.Taylor.cmpe275Group.Exception.MissingParameterException;
import com.Taylor.cmpe275Group.market.Market;
import com.Taylor.cmpe275Group.market.MarketRepository;
import com.Taylor.cmpe275Group.market.MarketService;
import com.Taylor.cmpe275Group.transRecord.TransRecord;
import com.Taylor.cmpe275Group.transRecord.TransRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class OrderRecordController {

    @Autowired
    private OrderRecordRepository orderRecordRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private MarketService marketService;

    @Autowired
    private OrderRecordService orderRecordService;

    @Autowired
    private TransRecordRepository transRecordRepository;

    @PostMapping(path = "/orderOfSelling", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> createOrderofSelling(@RequestBody OrderRecord newRecord) {
        System.out.println(newRecord.getBitcoinAmount() + " i post a order of sell of bitcoinamount");
        Float bitcoinAmount = newRecord.getBitcoinAmount();
        Float bitcoinPrice = newRecord.getBitcoinPrice();
        String currency = newRecord.getCurrency();
        String type = newRecord.getType();
        String seller = newRecord.getSeller();

        try {
            if(bitcoinAmount == null || bitcoinAmount <= 0) {
                throw new MissingParameterException("Your selling bitcoin amount is less or equal to 0");
            }
            if(bitcoinPrice == null || bitcoinPrice <= 0) {
                throw new MissingParameterException("Your selling bitcoin price is less or equal to 0");
            }
            if(currency == null || currency.isEmpty()) {
                throw new MissingParameterException("Miss Currency");
            }
            if(type == null || type.isEmpty()) {
                throw new MissingParameterException("Missing  Order Type");
            }
            //check if user does not exist
            if(appUserRepository.findByEmail(seller) == null) {
                throw new MissingParameterException("Seller " + seller +" can not be found");
            }
            AppUser sellerUser = appUserRepository.findByEmail(seller);
            //check if it has sufficient found
            if(bitcoinAmount > sellerUser.getBankAccount().getBalanceOfBitcoin()){
                throw new MissingParameterException("You do not have sufficient bitcoin amount");
            }
            //check if there is any buying order matching the requirement
            Long highestPriceId = (long)-1;
            BankAccount userBankAccount = sellerUser.getBankAccount();
            userBankAccount.setBalanceOfBitcoin(userBankAccount.getBalanceOfBitcoin()-bitcoinAmount);
            if(type.equals("marketOrder")){
                Float marketBuyingPrice = orderRecordService.getMarketPrice(currency);
                if(currency.equals("USD")){
                    userBankAccount.setBalanceOfUSD(userBankAccount.getBalanceOfUSD()+bitcoinAmount*marketBuyingPrice-(Math.min(1, bitcoinAmount*marketBuyingPrice*(float)0.0001)));
                }
                if(currency.equals("EUR")){
                    userBankAccount.setBalanceOfEUR(userBankAccount.getBalanceOfEUR()+bitcoinAmount*marketBuyingPrice-(Math.min(1, bitcoinAmount*marketBuyingPrice*(float)0.0001)));
                }
                if(currency.equals("GBP")){
                    userBankAccount.setBalanceOfGBP(userBankAccount.getBalanceOfGBP()+bitcoinAmount*marketBuyingPrice-(Math.min(1, bitcoinAmount*marketBuyingPrice*(float)0.0001)));
                }
                if(currency.equals("INR")){
                    userBankAccount.setBalanceOfINR(userBankAccount.getBalanceOfINR()+bitcoinAmount*marketBuyingPrice-(Math.min(1, bitcoinAmount*marketBuyingPrice*(float)0.0001)));
                }
                if(currency.equals("RMB")){
                    userBankAccount.setBalanceOfRMB(userBankAccount.getBalanceOfRMB()+bitcoinAmount*marketBuyingPrice-(Math.min(1, bitcoinAmount*marketBuyingPrice*(float)0.0001)));
                }
                TransRecord transRecord = new TransRecord(seller, "MarketOwner", bitcoinAmount, marketBuyingPrice, currency, LocalDateTime.now(), LocalDateTime.now(),"marketOrder");
                sellerUser.getTransRecords().add(transRecord);
                appUserRepository.save(sellerUser);
                return ResponseEntity.ok().body(transRecord);

            }else{
                Float highestPrice = bitcoinPrice;
                for(OrderRecord order: orderRecordRepository.findAll()){
                    if(!order.getBuyer().equals("") && !order.getBuyer().equals(sellerUser.getEmail())&&order.getStatus().equals("Open")){
                        if(currency.equals(order.getCurrency()) && order.getBitcoinPrice() >= highestPrice){
                            System.out.println(order.getBitcoinPrice());
                            highestPrice = order.getBitcoinPrice();
                            highestPriceId = order.getId();
                        }
                    }
                }
                System.out.println("Highest price ID is " + highestPriceId);
                if(highestPriceId == -1){
                    //add the record
                    OrderRecord orderRecord = new OrderRecord(seller, "", bitcoinAmount, bitcoinPrice, currency, LocalDateTime.now(), LocalDateTime.now().minusDays(1), "Open", "limitOrder");
                    marketService.updateLAP(currency, bitcoinPrice);
                    orderRecordRepository.save(orderRecord);
                    appUserRepository.save(sellerUser);
                    return ResponseEntity.ok().body(orderRecord);
                }else{
                    System.out.println("In selling right place");
                    OrderRecord highestPriceOrder = orderRecordRepository.findById(highestPriceId).get();
                    AppUser buyerUser = appUserRepository.findByEmail(highestPriceOrder.getBuyer());
                    BankAccount buyerBankAccount = buyerUser.getBankAccount();
                    buyerBankAccount.setBalanceOfBitcoin(buyerBankAccount.getBalanceOfBitcoin()+highestPriceOrder.getBitcoinAmount());
                    if(currency.equals("USD")){
                        userBankAccount.setBalanceOfUSD(userBankAccount.getBalanceOfUSD()+bitcoinAmount*highestPrice);
                    }
                    if(currency.equals("EUR")){
                        userBankAccount.setBalanceOfEUR(userBankAccount.getBalanceOfEUR()+bitcoinAmount*highestPrice);
                    }
                    if(currency.equals("GBP")){
                        userBankAccount.setBalanceOfGBP(userBankAccount.getBalanceOfGBP()+bitcoinAmount*highestPrice);
                    }
                    if(currency.equals("INR")){
                        userBankAccount.setBalanceOfINR(userBankAccount.getBalanceOfINR()+bitcoinAmount*highestPrice);
                    }
                    if(currency.equals("RMB")){
                        userBankAccount.setBalanceOfRMB(userBankAccount.getBalanceOfRMB()+bitcoinAmount*highestPrice);
                    }

                    TransRecord sellerTransRecord = new TransRecord(seller, highestPriceOrder.getBuyer(), bitcoinAmount, highestPrice, currency, LocalDateTime.now(), LocalDateTime.now(), "limitOrder");
                    sellerUser.getTransRecords().add(sellerTransRecord);
                    if(highestPriceOrder.getBitcoinAmount() - bitcoinAmount <= 0){
                        TransRecord buyerTransRecord = new TransRecord(seller, highestPriceOrder.getBuyer(), highestPriceOrder.getBitcoinAmount(), highestPrice, currency, LocalDateTime.now(), LocalDateTime.now(),"limitOrder");
                        buyerUser.getTransRecords().add(buyerTransRecord);
                        highestPriceOrder.setBitcoinAmount((float)0);
                        highestPriceOrder.setStatus("Fulfilled");
                        highestPriceOrder.setEndDateTime(LocalDateTime.now());
                    }else{
                        highestPriceOrder.setBitcoinAmount(highestPriceOrder.getBitcoinAmount() - bitcoinAmount);
                        TransRecord buyerTransRecord = new TransRecord(seller, highestPriceOrder.getBuyer(), highestPriceOrder.getBitcoinAmount()-bitcoinAmount, highestPrice, currency, LocalDateTime.now(), LocalDateTime.now(),"limitOrder");
                        buyerUser.getTransRecords().add(buyerTransRecord);
                    }
                    orderRecordRepository.save(highestPriceOrder);
                    marketService.updateLAP(currency, highestPrice);
                    appUserRepository.save(sellerUser);
                    appUserRepository.save(buyerUser);
                    return new ResponseEntity<>(highestPriceOrder, HttpStatus.OK);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping(path = "/orderOfBuying", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> createOrderofBuying(
            @RequestBody OrderRecord newRecord){
        try {
            Float bitcoinAmount = newRecord.getBitcoinAmount();
            Float bitcoinPrice = newRecord.getBitcoinPrice();
            String currency = newRecord.getCurrency();
            String type = newRecord.getType();
            String buyer = newRecord.getBuyer();
            if(bitcoinAmount == null|| bitcoinAmount <= 0) {
                throw new MissingParameterException("Your buying bitcoin amount " + bitcoinAmount + " is less or equal to 0");
            }
            if(bitcoinPrice == null || bitcoinPrice <= 0) {
                throw new MissingParameterException("Your buying bitcoin price is less or equal to 0");
            }
            if(currency == null || currency.isEmpty()) {
                throw new MissingParameterException("Missing Currency");
            }
            if(type == null || type.isEmpty()) {
                throw new MissingParameterException("Missing  Order Type");
            }
            //check if user exist
            if(appUserRepository.findByEmail(buyer) == null) {
                throw new MissingParameterException("buyer cannot be found");
            }
            Market currentMarket = marketRepository.findById((long)1).get();
            AppUser buyerUser = appUserRepository.findByEmail(buyer);
            BankAccount userBankAccount = buyerUser.getBankAccount();

            //check if there is any selling order matching the requirement
            Long lowestPriceId = (long)-1;
            if(type.equals("marketOrder")){
                Float LTP = marketService.getLTP(currency);
                if(!appUserService.payForMarketOrder(buyerUser, currency, bitcoinAmount)){
                    throw new MissingParameterException("No sufficient balance on market order");
                }
                TransRecord transRecord = new TransRecord("MarketOwner", buyerUser.getEmail(), bitcoinAmount, LTP, currency, LocalDateTime.now(), LocalDateTime.now(), "marketOrder");
                buyerUser.getTransRecords().add(transRecord);
                appUserRepository.save(buyerUser);
                return ResponseEntity.ok().body(transRecord);
            } else{
                System.out.println("In the limitOrder");
                //check if there is any order matches the requirement
                Float lowestPrice = bitcoinPrice;
                for(OrderRecord order: orderRecordRepository.findAll()){
                    //TODO:need to double check the processing fee
                    if(!order.getSeller().equals("")&&!order.getSeller().equals(buyerUser.getEmail()) && order.getStatus().equals("Open")){
                        if(order.getCurrency().equals(currency)){
                            if(currency.equals("USD") && lowestPrice >= order.getBitcoinPrice()){
                                lowestPriceId = order.getId();
                                lowestPrice = order.getBitcoinPrice();
                            }
                            if(currency.equals("EUR") && lowestPrice >= order.getBitcoinPrice()){
                                lowestPriceId = order.getId();
                                lowestPrice = order.getBitcoinPrice();
                            }
                            if(currency.equals("GBP") && lowestPrice >= order.getBitcoinPrice()){
                                lowestPriceId = order.getId();
                                lowestPrice = order.getBitcoinPrice();
                            }
                            if(currency.equals("INR") && lowestPrice >= order.getBitcoinPrice()){
                                lowestPriceId = order.getId();
                                lowestPrice = order.getBitcoinPrice();
                            }
                            if(currency.equals("RMB") && lowestPrice >= order.getBitcoinPrice()){
                                lowestPriceId = order.getId();
                                lowestPrice = order.getBitcoinPrice();
                            }
                        }
                    }
                }
                System.out.println(lowestPriceId);
                System.out.println("Here on the step 1");
                //add the record

                if(lowestPriceId == -1){
                    System.out.println("No matched order, put the order on the marketboard");
                    if(!appUserService.payForLimitOrder(buyerUser, currency, bitcoinPrice, bitcoinAmount)){
                        throw new MissingParameterException("No sufficient balance on limit order");
                    }
                    OrderRecord orderRecord = new OrderRecord("", buyer, bitcoinAmount, bitcoinPrice, currency, LocalDateTime.now(), null, "Open", "limitOrder");

                    appUserRepository.save(buyerUser);
                    marketService.updateLBP(currency, bitcoinPrice);
                    orderRecordRepository.save(orderRecord);
                    return ResponseEntity.ok().body(orderRecord);
                }else{
                    //auto matches with lowest selling record
                    //update buyer balance
                    System.out.println("There is matched order");
                    OrderRecord lowestPriceOrder = orderRecordRepository.findById(lowestPriceId).get();
                    AppUser sellerUser = appUserRepository.findByEmail(lowestPriceOrder.getSeller());
                    if(!appUserService.payForLimitOrder(buyerUser, currency, bitcoinPrice, bitcoinAmount)){
                        throw new MissingParameterException("No sufficient balance on limit order");
                    }
                    TransRecord buyerTransRecord = new TransRecord(lowestPriceOrder.getSeller(), buyer, bitcoinAmount, lowestPrice, currency, LocalDateTime.now(), LocalDateTime.now(),"limitOrder");
                    buyerUser.getTransRecords().add(buyerTransRecord);
                    if(lowestPriceOrder.getBitcoinAmount() <= bitcoinAmount){
                        appUserService.gainFromLimitOrder(sellerUser, currency, lowestPrice, lowestPriceOrder.getBitcoinAmount());
                        TransRecord sellerTransRecord = new TransRecord(lowestPriceOrder.getSeller(), buyer, lowestPriceOrder.getBitcoinAmount(), lowestPrice, currency, LocalDateTime.now(), LocalDateTime.now(),"limitOrder");
                        sellerUser.getTransRecords().add(sellerTransRecord);
                        lowestPriceOrder.setBitcoinAmount((float)0);
                        lowestPriceOrder.setStatus("Fulfilled");
                        lowestPriceOrder.setEndDateTime(LocalDateTime.now());
                    }else{
                        lowestPriceOrder.setBitcoinAmount(lowestPriceOrder.getBitcoinAmount() - bitcoinAmount);
                        lowestPriceOrder.getAppUsers().add(sellerUser);
                        TransRecord sellerTransRecord = new TransRecord(lowestPriceOrder.getSeller(), buyer, lowestPriceOrder.getBitcoinAmount()-bitcoinAmount, lowestPrice, currency, LocalDateTime.now(), LocalDateTime.now(),"limitOrder");
                        sellerUser.getTransRecords().add(sellerTransRecord);
                        appUserService.gainFromLimitOrder(sellerUser, currency, lowestPrice, lowestPriceOrder.getBitcoinAmount());
                    }
                    userBankAccount.setBalanceOfBitcoin(userBankAccount.getBalanceOfBitcoin()+bitcoinAmount);

                    orderRecordRepository.save(lowestPriceOrder);
                    marketService.updateLBP(currency, lowestPrice);
                    appUserRepository.save(sellerUser);
                    appUserRepository.save(buyerUser);
                    return new ResponseEntity<>(lowestPriceOrder, HttpStatus.OK);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping(path = "/order/{id}}",
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getOpenOrders(
            @RequestBody OrderRecord orderRecord,
            @PathVariable(value = "id") Long id){
        try {
            OrderRecord currentOrderRecord = orderRecordRepository.findById((long)id).get();
            Float bitcoinAmount = orderRecord.getBitcoinAmount();
            Float bitcoinPrice = orderRecord.getBitcoinPrice();
            String currency = orderRecord.getCurrency();
            String status = orderRecord.getStatus();
            String seller = orderRecord.getSeller();
            String buyer = orderRecord.getBuyer();
            if(!seller.equals("") && !buyer.equals("")){
                throw new MissingParameterException("You can not change this order");
            }else if(!seller.equals("")){
                AppUser sellerUser = appUserRepository.findByEmail(seller);
                BankAccount userBankAccount = sellerUser.getBankAccount();
                userBankAccount.setBalanceOfBitcoin(userBankAccount.getBalanceOfBitcoin()+ currentOrderRecord.getBitcoinAmount()- orderRecord.getBitcoinAmount());
                currentOrderRecord.setBitcoinAmount(orderRecord.getBitcoinAmount());
                appUserRepository.save(sellerUser);
                orderRecordRepository.save(currentOrderRecord);
            }else if(!buyer.equals("")){
                AppUser buyerUser = appUserRepository.findByEmail(buyer);
                if(bitcoinAmount!= null && bitcoinPrice!= null && currency != null){
                    BankAccount userBankAccount = buyerUser.getBankAccount();
                    String currentCurrency = currentOrderRecord.getCurrency();
                    Float currentBitcoinAmount = currentOrderRecord.getBitcoinAmount();
                    Float currentBitcoinPrice = currentOrderRecord.getBitcoinPrice();
                    if(currentCurrency.equals("USD")){
                        userBankAccount.setBalanceOfUSD(userBankAccount.getBalanceOfUSD()+currentBitcoinAmount*currentBitcoinPrice);
                    }
                    if(currentCurrency.equals("EUR")){
                        userBankAccount.setBalanceOfEUR(userBankAccount.getBalanceOfEUR()+currentBitcoinAmount*currentBitcoinPrice);

                    }
                    if(currentCurrency.equals("GBP")){
                        userBankAccount.setBalanceOfGBP(userBankAccount.getBalanceOfGBP()+currentBitcoinAmount*currentBitcoinPrice);
                    }
                    if(currentCurrency.equals("INR")){
                        userBankAccount.setBalanceOfINR(userBankAccount.getBalanceOfINR()+currentBitcoinAmount*currentBitcoinPrice);
                    }
                    if(currentCurrency.equals("RMB")){
                        userBankAccount.setBalanceOfRMB(userBankAccount.getBalanceOfRMB()+currentBitcoinAmount*currentBitcoinPrice);
                    }

                    if(currency.equals("USD")){
                        userBankAccount.setBalanceOfUSD(userBankAccount.getBalanceOfUSD()-bitcoinAmount*currentBitcoinPrice);
                        currentOrderRecord.setCurrency("USD");
                        currentOrderRecord.setBitcoinPrice(bitcoinPrice);
                        currentOrderRecord.setBitcoinAmount(bitcoinAmount);
                    }
                    if(currency.equals("EUR")){
                        userBankAccount.setBalanceOfEUR(userBankAccount.getBalanceOfEUR()-bitcoinAmount*currentBitcoinPrice);
                        currentOrderRecord.setCurrency("EUR");
                        currentOrderRecord.setBitcoinPrice(bitcoinPrice);
                        currentOrderRecord.setBitcoinAmount(bitcoinAmount);
                    }
                    if(currency.equals("GBP")){
                        userBankAccount.setBalanceOfGBP(userBankAccount.getBalanceOfGBP()-bitcoinAmount*currentBitcoinPrice);
                        currentOrderRecord.setCurrency("GBP");
                        currentOrderRecord.setBitcoinPrice(bitcoinPrice);
                        currentOrderRecord.setBitcoinAmount(bitcoinAmount);
                    }
                    if(currency.equals("INR")){
                        userBankAccount.setBalanceOfINR(userBankAccount.getBalanceOfINR()-bitcoinAmount*currentBitcoinPrice);
                        currentOrderRecord.setCurrency("INR");
                        currentOrderRecord.setBitcoinPrice(bitcoinPrice);
                        currentOrderRecord.setBitcoinAmount(bitcoinAmount);
                    }
                    if(currency.equals("RMB")){
                        userBankAccount.setBalanceOfRMB(userBankAccount.getBalanceOfRMB()-bitcoinAmount*currentBitcoinPrice);
                        currentOrderRecord.setCurrency("RMB");
                        currentOrderRecord.setBitcoinPrice(bitcoinPrice);
                        currentOrderRecord.setBitcoinAmount(bitcoinAmount);
                    }
                    if(status.equals("Open") || status.equals("Cancelled"))
                        currentOrderRecord.setStatus(status);
                    else
                        throw new MissingParameterException("Invalid Status");
                    appUserRepository.save(buyerUser);
                    orderRecordRepository.save(currentOrderRecord);
                }else{
                    throw new MissingParameterException("Bitcoin Amount, Bitcoin Price and Currency have to be fulfilled");
                }
            }
            return ResponseEntity.ok().body(currentOrderRecord);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(path = "/orderOfUser/{email}",
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<OrderRecord> getOrderRecordsOfAppUser(
            @PathVariable(value = "email") String email) {

        AppUser existUser = appUserRepository.findByEmail(email);
        if(existUser == null) {
            return new ArrayList<>();
        }

        List<OrderRecord> orderRecords = orderRecordRepository.findAll();
        List<OrderRecord> userOrderRecords = new ArrayList<>();
        for(OrderRecord orderRecord : orderRecords){
            if(orderRecord.getSeller().equals(email)||orderRecord.getBuyer().equals(email))
                userOrderRecords.add(orderRecord);
            else{
                List<AppUser> listOfBuyer = orderRecord.getAppUsers();
                for(AppUser user: listOfBuyer){
                    if(user.getEmail().equals(email))
                        userOrderRecords.add(orderRecord);
                }
            }
        }
        return userOrderRecords;
    }


    @GetMapping(path = "/Order/open",
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> getOpenOrders(){
        try {
            List<OrderRecord> openOrders = orderRecordRepository.findAll().stream().filter((o) -> o.getStatus().equals("Open")).collect(Collectors.toList());
            return ResponseEntity.ok().body(openOrders);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
