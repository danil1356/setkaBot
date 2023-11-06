package com.example.setkabot.data.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table (name = "users")
@Getter
@Setter
@NoArgsConstructor
public class Users extends BaseEntity {

    @Column(name = "chatid")
    private String chatId;

    @Column(name = "name1")
    private String name1;

    @Column(name = "name2")
    private String name2;

    @Column(name = "username")
    private String username;

    @Column(name = "userid")
    private String userid;

    @Column(name = "paymenttime")
    private Long paymenttime;

    @Column(name = "endpaymenttime")
    private Long endpaymenttime;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private List<Payments>payments;


    public Users(Long id) {
        super(id);
    }

    public Users(Long id, String chatId, String name1, String name2, String username, String userid, Long paymenttime, Long endpaymenttime){
        super(id);
        this.chatId = chatId;
        this.name1 = name1;
        this.name2 = name2;
        this.username = username;
        this.userid = userid;
        this.paymenttime = paymenttime;
        this.endpaymenttime = endpaymenttime;
    }
}
