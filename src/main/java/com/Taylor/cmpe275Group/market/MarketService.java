package com.Taylor.cmpe275Group.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MarketService {

    @Autowired
    MarketRepository marketRepository;

    public Market updateMarketPrice(Market newMarket){
        Market market = marketRepository.findById((long)1).get();

        Float LAPofUSD = newMarket.getLAPofUSD();
        if(LAPofUSD != null){
            market.setLAPofUSD(LAPofUSD);
        }
        Float LBPofUSD = newMarket.getLBPofUSD();
        if(LBPofUSD != null){
            market.setLBPofUSD(LBPofUSD);
        }
        Float LTPofUSD = newMarket.getLTPofUSD();
        if(LTPofUSD != null){
            market.setLTPofUSD(LTPofUSD);
        }

        Float LAPofEUR = newMarket.getLAPofEUR();
        if(LAPofEUR != null){
            market.setLAPofEUR(LAPofEUR);
        }
        Float LBPofEUR = newMarket.getLBPofEUR();
        if(LBPofEUR != null){
            market.setLBPofEUR(LBPofEUR);
        }
        Float LTPofEUR= newMarket.getLTPofEUR();
        if(LTPofEUR != null){
            market.setLTPofEUR(LTPofEUR);
        }

        Float LAPofGBP = newMarket.getLAPofGBP();
        if(LAPofGBP != null){
            market.setLAPofGBP(LAPofGBP);
        }
        Float LBPofGBP = newMarket.getLBPofGBP();
        if(LBPofGBP != null){
            market.setLBPofGBP(LBPofGBP);
        }
        Float LTPofGBP = newMarket.getLTPofGBP();
        if(LTPofGBP != null){
            market.setLTPofGBP(LTPofGBP);
        }

        Float LAPofINR = newMarket.getLAPofINR();
        if(LAPofINR != null){
            market.setLAPofINR(LAPofINR);
        }
        Float LBPofINR = newMarket.getLBPofINR();
        if(LBPofINR != null){
            market.setLBPofINR(LBPofINR);
        }
        Float LTPofINR = newMarket.getLTPofINR();
        if(LTPofINR != null){
            market.setLTPofINR(LTPofINR);
        }

        Float LAPofRMB = newMarket.getLAPofRMB();
        if(LAPofRMB != null){
            market.setLAPofRMB(LAPofRMB);
        }
        Float LBPofRMB = newMarket.getLBPofRMB();
        if(LBPofRMB != null){
            market.setLBPofRMB(LBPofRMB);
        }
        Float LTPofRMB = newMarket.getLTPofRMB();
        if(LTPofRMB != null){
            market.setLTPofRMB(LTPofRMB);
        }

        marketRepository.save(market);
        return market;
    }

    public Float getLAP(String currency){
        Market market = marketRepository.findById((long)1).get();
        if(currency.equals("USD")){
            return market.getLAPofUSD();
        }
        if(currency.equals("EUR")){
            return market.getLAPofEUR();
        }
        if(currency.equals("GBP")){
            return market.getLAPofGBP();
        }
        if(currency.equals("INR")){
            return market.getLAPofINR();
        }
        if(currency.equals("RMB")){
            return market.getLAPofRMB();
        }
        return null;
    }

    public void updateLAP(String currency, Float LAP){
        Market market = marketRepository.findById((long)1).get();
        if(currency.equals("USD")){
            market.setLAPofUSD(LAP);
        }
        if(currency.equals("EUR")){
            market.setLAPofEUR(LAP);
        }
        if(currency.equals("GBP")){
            market.setLAPofGBP(LAP);
        }
        if(currency.equals("INR")){
            market.setLAPofINR(LAP);
        }
        if(currency.equals("RMB")){
            market.setLAPofRMB(LAP);
        }
        marketRepository.save(market);
    }

    public Float getLBP(String currency){
        Market market = marketRepository.findById((long)1).get();
        if(currency.equals("USD")){
            return market.getLBPofUSD();
        }
        if(currency.equals("EUR")){
            return market.getLBPofEUR();
        }
        if(currency.equals("GBP")){
            return market.getLBPofGBP();
        }
        if(currency.equals("INR")){
            return market.getLBPofINR();
        }
        if(currency.equals("RMB")){
            return market.getLBPofRMB();
        }
        return null;
    }

    public void updateLBP(String currency, Float LBP){
        Market market = marketRepository.findById((long)1).get();
        if(currency.equals("USD")){
            market.setLBPofUSD(LBP);
        }
        if(currency.equals("EUR")){
            market.setLBPofEUR(LBP);
        }
        if(currency.equals("GBP")){
            market.setLBPofGBP(LBP);
        }
        if(currency.equals("INR")){
            market.setLBPofINR(LBP);
        }
        if(currency.equals("RMB")){
            market.setLBPofRMB(LBP);
        }
        marketRepository.save(market);
    }

    public Float getLTP(String currency){
        Market market = marketRepository.findById((long)1).get();
        Float LTP;
        Float LBP;
        if(currency.equals("USD")){
            LTP = market.getLTPofUSD();
            LBP = market.getLBPofUSD();
            if(LTP > LBP){
                market.setLTPofUSD(LBP);
            }
            marketRepository.save(market);
            return market.getLTPofUSD();
        }
        if(currency.equals("EUR")){
            LTP = market.getLTPofEUR();
            LBP = market.getLBPofEUR();
            if(LTP > LBP){
                market.setLTPofEUR(LBP);
            }
            marketRepository.save(market);
            return market.getLTPofEUR();
        }
        if(currency.equals("GBP")){
            LTP = market.getLTPofGBP();
            LBP = market.getLBPofGBP();
            if(LTP > LBP){
                market.setLTPofGBP(LBP);
            }
            marketRepository.save(market);
            return market.getLTPofGBP();
        }
        if(currency.equals("INR")){
            LTP = market.getLTPofINR();
            LBP = market.getLBPofINR();
            if(LTP > LBP){
                market.setLTPofINR(LBP);
            }
            marketRepository.save(market);
            return market.getLTPofINR();
        }
        if(currency.equals("RMB")){
            LTP = market.getLTPofRMB();
            LBP = market.getLBPofRMB();
            if(LTP > LBP){
                market.setLTPofRMB(LBP);
            }
            marketRepository.save(market);
            return market.getLTPofRMB();
        }
        return null;
    }

    public void updateLTP(String currency, Float LTP){
        Market market = marketRepository.findById((long)1).get();
        if(currency.equals("USD")){
            market.setLTPofUSD(LTP);
        }
        if(currency.equals("EUR")){
            market.setLTPofEUR(LTP);
        }
        if(currency.equals("GBP")){
            market.setLTPofGBP(LTP);
        }
        if(currency.equals("INR")){
            market.setLTPofINR(LTP);
        }
        if(currency.equals("RMB")){
            market.setLTPofRMB(LTP);
        }
        marketRepository.save(market);
    }
}
