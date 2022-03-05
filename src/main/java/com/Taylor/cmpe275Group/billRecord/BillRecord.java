package com.Taylor.cmpe275Group.billRecord;

import com.Taylor.cmpe275Group.AppUser.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Setter
@Getter
@Entity
public class BillRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String sender;

    @Column()
    private String receiver;

    @Column()
    private String description;

    @Column()
    private Float balance;

    @Column()
    private String currency;

    @Column()
    private String dueDate;

    @Column()
    private String startDateTime;

    @Column()
    private String  endDateTime;

    @Column()
    private String status;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AppUser> appUsers = new ArrayList<>();

    public BillRecord(String sender, String receiver, String description, Float balance, String currency, String dueDate, String startDateTime, String endDateTime, String status) {
        this.sender = sender;
        this.receiver = receiver;
        this.description = description;
        this.balance = balance;
        this.currency = currency;
        this.dueDate = dueDate;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.status = status;
    }
}
