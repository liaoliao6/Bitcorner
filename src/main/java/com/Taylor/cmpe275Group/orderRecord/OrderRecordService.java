package com.Taylor.cmpe275Group.orderRecord;

import com.Taylor.cmpe275Group.market.Market;
import com.Taylor.cmpe275Group.market.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderRecordService {

    @Autowired
    MarketRepository marketRepository;

    public Float getMarketPrice(String currency){
        Float LTP;
        Float LAP;
        Market currentMarket = marketRepository.findById((long)1).get();
        if(currency.equals("USD")){
            LTP = currentMarket.getLTPofUSD();
            LAP = currentMarket.getLAPofUSD();
            if(LTP < LAP){
                currentMarket.setLTPofUSD(LAP);
            }
            marketRepository.save(currentMarket);
            return currentMarket.getLTPofUSD();
        }
        if(currency.equals("EUR")){
            LTP = currentMarket.getLTPofEUR();
            LAP = currentMarket.getLAPofEUR();
            if(LTP < LAP){
                currentMarket.setLTPofEUR(LAP);
            }
            marketRepository.save(currentMarket);
            return currentMarket.getLTPofEUR();
        }
        if(currency.equals("GBP")){
            LTP = currentMarket.getLTPofGBP();
            LAP = currentMarket.getLAPofGBP();
            if(LTP < LAP){
                currentMarket.setLTPofGBP(LAP);
            }
            marketRepository.save(currentMarket);
            return currentMarket.getLTPofGBP();
        }
        if(currency.equals("INR")){
            LTP = currentMarket.getLTPofINR();
            LAP = currentMarket.getLAPofINR();
            if(LTP < LAP){
                currentMarket.setLTPofINR(LAP);
            }
            marketRepository.save(currentMarket);
            return currentMarket.getLTPofINR();
        }
        if(currency.equals("RMB")){
            LTP = currentMarket.getLTPofRMB();
            LAP = currentMarket.getLAPofRMB();
            if(LTP < LAP){
                currentMarket.setLTPofRMB(LAP);
            }
            marketRepository.save(currentMarket);
            return currentMarket.getLTPofRMB();
        }
        return null;
    }
}
