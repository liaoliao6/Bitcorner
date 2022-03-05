package com.Taylor.cmpe275Group.transRecord;

import com.Taylor.cmpe275Group.AppUser.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class TransRecord {
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
    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDateTime;

    @Column()
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime  endDateTime;

    @Column()
    private String type;

    public TransRecord(String seller, String buyer, Float bitcoinAmount, Float bitcoinPrice, String currency, LocalDateTime startDateTime, LocalDateTime endDateTime, String type) {
        this.seller = seller;
        this.buyer = buyer;
        this.bitcoinAmount = bitcoinAmount;
        this.bitcoinPrice = bitcoinPrice;
        this.currency = currency;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.type = type;
    }
}
