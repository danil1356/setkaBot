CREATE TABLE users
(
    id SERIAL PRIMARY KEY,
    chatId VARCHAR(255) ,
    Name1 VARCHAR(255),
    Name2 VARCHAR(255),
    userName VARCHAR(255),
    userId VARCHAR(255),
    paymentTime bigint,
    endpaymentTime bigint

);

create table payments(
                         id SERIAL PRIMARY KEY,
                         currency varchar(500),
                         totalAmount varchar(500),
                         invoicePayload varchar(500),
                         shippingOptionId varchar(500),
                         orderInfo_name varchar(500),
                         orderInfo_phoneNumber varchar(500),
                         orderInfo_email varchar(500),
                         orderInfo_shippingAddress varchar(500),
                         telegramPaymentChargeId varchar(500),
                         providerPaymentChargeId varchar(500),
                         paymentsDate bigint,
                         users_id integer references users(id) on delete cascade,

                         invoice_payload varchar(255),
                         order_info_emaild varchar(255),
                         order_info_name varchar(255),
                         order_info_phone_number varchar(255),
                         order_info_shipping_address varchar(255),
                         payments_date bigint,
                         provider_payment_charge_id varchar(255),
                         shipping_option_id varchar(255),
                         telegram_payment_charge_id varchar(255),
                         total_amount varchar(255)

);