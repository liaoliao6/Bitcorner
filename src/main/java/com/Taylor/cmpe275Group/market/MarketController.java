package com.Taylor.cmpe275Group.market;
import com.Taylor.cmpe275Group.AppUser.AppUser;
import com.Taylor.cmpe275Group.orderRecord.OrderRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MarketController {

    @Autowired
    private MarketRepository marketRepository;


    @PostMapping(path = "/market", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Object> createMarket(
            @RequestBody Market newMarket) {
        try{
            Float marketSellingPrice = newMarket.getMarketSellingPrice();
            Float marketBuyingPrice = newMarket.getMarketBuyingPrice();

            Float LAPofUSD = newMarket.getLAPofUSD();
            Float LBPofUSD = newMarket.getLBPofUSD();
            Float LTPofUSD = newMarket.getLTPofUSD();

            Float LAPofEUR = newMarket.getLAPofEUR();
            Float LBPofEUR = newMarket.getLBPofEUR();
            Float LTPofEUR = newMarket.getLTPofEUR();

            Float LAPofGBP = newMarket.getLAPofGBP();
            Float LBPofGBP = newMarket.getLBPofGBP();
            Float LTPofGBP = newMarket.getLTPofGBP();

            Float LAPofINR = newMarket.getLAPofINR();
            Float LBPofINR = newMarket.getLBPofINR();
            Float LTPofINR = newMarket.getLTPofINR();

            Float LAPofRMB = newMarket.getLAPofRMB();
            Float LBPofRMB = newMarket.getLBPofRMB();
            Float LTPofRMB = newMarket.getLTPofRMB();
            if(marketSellingPrice == null || marketBuyingPrice == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing Required Parameter");
            }
            Market market = new Market(marketSellingPrice, marketBuyingPrice, LAPofUSD, LBPofUSD, LTPofUSD, LAPofEUR, LBPofEUR, LTPofEUR, LAPofGBP, LBPofGBP, LTPofGBP, LAPofINR, LBPofINR, LTPofINR, LAPofRMB, LBPofRMB, LTPofRMB);
            marketRepository.save(market);
            return ResponseEntity.ok().body(market);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request");
        }
    }

    @GetMapping(path = "/market",
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Market getMarketInfo() {

        Market market = marketRepository.findById((long)1).get();
        if(market == null) {
            return new Market();
        }
        return market;
    }
}
