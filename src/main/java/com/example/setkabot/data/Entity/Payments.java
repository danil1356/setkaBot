package com.example.setkabot.data.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
public class Payments extends BaseEntity {

    @Column(name = "currency")
    private String currency;

    @Column(name = "totalAmount")
    private String totalAmount;

    @Column(name = "invoicePayload")
    private String invoicePayload;

    @Column(name = "shippingOptionId")
    private String shippingOptionId;

    @Column(name = "orderInfo_name")
    private String orderInfo_name;

    @Column(name = "orderInfo_phoneNumber")
    private String orderInfo_phoneNumber;

    @Column(name = "orderInfo_email")
    private String orderInfo_email;

    @Column(name = "orderInfo_shippingAddress")
    private String orderInfo_shippingAddress;

    @Column(name = "telegramPaymentChargeId")
    private String telegramPaymentChargeId;

    @Column(name = "providerPaymentChargeId")
    private String providerPaymentChargeId;

    @Column(name = "paymentsDate")
    private Long paymentsDate;

    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id", nullable = false)
    private Users user;

    public Payments(Long id){
        super(id);
    }

    public Payments(Long id, String currency, String totalAmount, String invoicePayload, String shippingOptionId, String orderInfo_name, String orderInfo_phoneNumber, String orderInfo_email, String orderInfo_shippingAddress, String telegramPaymentChargeId, String providerPaymentChargeId, Long paymentsDate, Users user){
        super(id);
        this.currency = currency;
        this.totalAmount = totalAmount;
        this.invoicePayload = invoicePayload;
        this.shippingOptionId = shippingOptionId;
        this.orderInfo_name = orderInfo_name;
        this.orderInfo_phoneNumber = orderInfo_phoneNumber;
        this.orderInfo_email = orderInfo_email;
        this.orderInfo_shippingAddress = orderInfo_shippingAddress;
        this.telegramPaymentChargeId = telegramPaymentChargeId;
        this.providerPaymentChargeId = providerPaymentChargeId;
        this.paymentsDate = paymentsDate;
        this.user = user;
    }
}
