package com.Taylor.cmpe275Group.AppUser;

import com.Taylor.cmpe275Group.AppUser.bankAccount.BankAccount;
import com.Taylor.cmpe275Group.billRecord.BillRecord;
import com.Taylor.cmpe275Group.orderRecord.OrderRecord;
import com.Taylor.cmpe275Group.transRecord.TransRecord;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Setter
@EqualsAndHashCode
@NoArgsConstructor

@Entity
public class AppUser {


    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize =  1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    private String nickName;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;
    private Boolean locked = false;
    private Boolean enabled = false;

    @Embedded
    private BankAccount bankAccount;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TransRecord> transRecords = new ArrayList<>();

//    @OneToMany(
//            cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY
//    )
//    @JsonIgnoreProperties("reports")
//    private List<OrderRecord> orderRecords = new ArrayList<>();


//    @ManyToMany(
//            cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY
//    )
//    @JoinTable(name = "appUser_billRecord",
//        joinColumns = {@JoinColumn(name = "appUser_id")},
//        inverseJoinColumns = {@JoinColumn(name="billRecord_id")})
//    @JsonIgnoreProperties("reports")
//    private List<BillRecord> billRecords = new ArrayList<>();

    public AppUser(String nickName, String email, String password, AppUserRole appUserRole) {
        //Boolean locked, Boolean enabled TODO
        this.nickName = nickName;
        this.email = email;
        this.password = password;
        this.appUserRole = appUserRole;
    }


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(appUserRole.name());
//        return Collections.singletonList(authority);
//    }

    public String getEmail(){return email;}
//    @Override
    public String getPassword() {
        return password;
    }


    public String getNickName() {
        return nickName;
    }

//    @Override
//    public String getUsername(){
//        return email;
//    }


    public BankAccount getBankAccount(){return bankAccount;}

    public void setBankAccount(BankAccount bankAccount){this.bankAccount = bankAccount;}

    public void setTransRecords(List<TransRecord> transRecords) {
        this.transRecords = transRecords;
    }

    public List<TransRecord> getTransRecords() {
        return transRecords;
    }

    //    public List<BillRecord> getBillRecords() {
//        return billRecords;
//    }

    //(String bankName, String country, Long accountNumber, String ownerNaturalName,
    //                       String address, String primaryCurrency, Float balanceOfUSD, Float balanceOfEUR,
    //                       Float balanceOfGBP, Float balanceOfINR, Float balanceOfRMB, Float balanceOfBitcoin)
    public void updateBankAccount(BankAccount bankAccount) {
        BankAccount old = this.bankAccount;

       // BankAccount newB = new BankAccount();

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
        setBankAccount(newB);
    }



//    public boolean validate(AppUser usr){
//
//    }
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return !locked;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return enabled;
//    }
}
