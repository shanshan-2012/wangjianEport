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
public class ProformaInvoiceDTO {

    private Integer id;

    private String proformaInvoiceNum;

    private String shippingMethod;

    private String paymentMethod;

    private String proformaInvoiceTo;

    private String proformaInvoiceFrom;

    private ImportMerchant importMerchant;

    private List<ProformaInvoiceOrderItemDTO> proformaInvoiceOrderItemDTOList;

    private Broker broker;

    private String remark;

    private Date proformaInvoiceDate;

    private Date createTime;

    private Date lastUpdateDate;


}
