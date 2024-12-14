package com.wang.dataload.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProformaInvoiceOrderItemDTO {

    private Integer id;

    private Integer proformaInvoiceOrderId;

    private Product product;

    private BigDecimal unitPriceRMB;

    private BigDecimal unitPriceUSD;

    private BigDecimal amountUSD;

    private BigDecimal amountRMB;

    private Integer quantity;

    private String remark;

    private Date createTime;

    private Date lastUpdateDate;
}
