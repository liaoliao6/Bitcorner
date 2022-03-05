package com.Taylor.cmpe275Group.AppUser.bankAccount;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class BankAccount {
    @Column()
    private String bankName;

    @Column()
    private String country;

    @Column()
    private Long accountNumber;

    @Column()
    private String ownerNaturalName;

    @Column()
    private String address;

    @Column()
    private String primaryCurrency;

    @Column()
    private Float balanceOfUSD;

    @Column()
    private Float balanceOfEUR;

    @Column()
    private Float balanceOfGBP;

    @Column()
    private Float balanceOfINR;

    @Column()
    private Float balanceOfRMB;

    @Column()
    private Float balanceOfBitcoin;
//
//    public BankAccount(String bankName, String country, Long accountNumber, String ownerNaturalName,
//                       String address, String primaryCurrency, Float balanceOfUSD, Float balanceOfEUR,
//                       Float balanceOfGBP, Float balanceOfINR, Float balanceOfRMB, Float balanceOfBitcoin) {
//        this.bankName = bankName;
//        this.country = country;
//        this.accountNumber = accountNumber;
//        this.ownerNaturalName = ownerNaturalName;
//        this.address = address;
//        this.primaryCurrency = primaryCurrency;
//        this.balanceOfUSD = balanceOfUSD;
//        this.balanceOfEUR = balanceOfEUR;
//        this.balanceOfGBP = balanceOfGBP;
//        this.balanceOfINR = balanceOfINR;
//        this.balanceOfRMB = balanceOfRMB;
//        this.balanceOfBitcoin = balanceOfBitcoin;
//    }



}
