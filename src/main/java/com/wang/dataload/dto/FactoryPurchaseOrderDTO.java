package com.wang.dataload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FactoryPurchaseOrderDTO {

    private Integer id;

    private Integer proformaInvoiceId;

    private List<FactoryPurchaseOrderItemDTO> factoryPurchaseOrderItemDTOList;

    private String factoryOrderNumber;

    private ExportMerchant exportMerchant;

    private Broker broker;

    private String deliverMode;

    private String packageMode;

    private Date factoryOrderDate;

    private String orderSignLocation;

    private Date createTime;

    private Date lastUpdateDate;

    private String remark;

}
