package com.wang.dataload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProformaInvoiceOrderWithItem {

    private Integer id;

    private String proformaInvoiceNum;

    private String factoryOrderNumber;

    private Date proformaInvoiceDate;

    private String productModel;

    private BigDecimal unitPriceRMB;

    private BigDecimal unitPriceUSD;

    private Integer invoiceQuantity;

    private Integer factoryQuantity;

    private BigDecimal amountUSD;

    private BigDecimal amountRMB;


}
