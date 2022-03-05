package com.Taylor.cmpe275Group.orderRecord;

import com.Taylor.cmpe275Group.AppUser.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class OrderRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String seller;

    @Column()
    private String buyer;

    @Column()
    private Float bitcoinAmount;

    @Column()
    private Float bitcoinPrice;

    @Column()
    private String currency;

    @Column()
    private LocalDateTime startDateTime;

    @Column()
    private LocalDateTime  endDateTime;

    @Column()
    private String status;

    @Column()
    private String type;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AppUser> appUsers = new ArrayList<>();

    public OrderRecord(String seller, String buyer, Float bitcoinAmount, Float bitcoinPrice, String currency, LocalDateTime startDateTime, LocalDateTime endDateTime, String status, String type) {
        this.seller = seller;
        this.buyer = buyer;
        this.bitcoinAmount = bitcoinAmount;
        this.bitcoinPrice = bitcoinPrice;
        this.currency = currency;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.status = status;
        this.type = type;
    }
}
