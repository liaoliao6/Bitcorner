package com.Taylor.cmpe275Group.market;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Market {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//TODO: need to be delete later.......
    @Column()
    private Float marketSellingPrice;

    @Column()
    private Float marketBuyingPrice;
//TODO:.....................................

    @Column()
    private Float LAPofUSD;

    @Column()
    private Float LBPofUSD;

    @Column()
    private Float LTPofUSD;

    @Column()
    private Float LAPofEUR;

    @Column()
    private Float LBPofEUR;

    @Column()
    private Float LTPofEUR;

    @Column()
    private Float LAPofGBP;

    @Column()
    private Float LBPofGBP;

    @Column()
    private Float LTPofGBP;

    @Column()
    private Float LAPofINR;

    @Column()
    private Float LBPofINR;

    @Column()
    private Float LTPofINR;

    @Column()
    private Float LAPofRMB;

    @Column()
    private Float LBPofRMB;

    @Column()
    private Float LTPofRMB;

    public Market(Float marketSellingPrice, Float marketBuyingPrice, Float LAPofUSD, Float LBPofUSD, Float LTPofUSD, Float LAPofEUR, Float LBPofEUR, Float LTPofEUR, Float LAPofGBP, Float LBPofGBP, Float LTPofGBP, Float LAPofINR, Float LBPofINR, Float LTPofINR, Float LAPofRMB, Float LBPofRMB, Float LTPofRMB) {
        this.marketSellingPrice = marketSellingPrice;
        this.marketBuyingPrice = marketBuyingPrice;
        this.LAPofUSD = LAPofUSD;
        this.LBPofUSD = LBPofUSD;
        this.LTPofUSD = LTPofUSD;
        this.LAPofEUR = LAPofEUR;
        this.LBPofEUR = LBPofEUR;
        this.LTPofEUR = LTPofEUR;
        this.LAPofGBP = LAPofGBP;
        this.LBPofGBP = LBPofGBP;
        this.LTPofGBP = LTPofGBP;
        this.LAPofINR = LAPofINR;
        this.LBPofINR = LBPofINR;
        this.LTPofINR = LTPofINR;
        this.LAPofRMB = LAPofRMB;
        this.LBPofRMB = LBPofRMB;
        this.LTPofRMB = LTPofRMB;
    }

    public Market(Float sellingPrice, Float buyingPrice){
        marketSellingPrice = sellingPrice;
        marketBuyingPrice = buyingPrice;
    }

    public Float getMarketSellingPrice() {
        return marketSellingPrice;
    }

    public Float getMarketBuyingPrice() {
        return marketBuyingPrice;
    }
}
