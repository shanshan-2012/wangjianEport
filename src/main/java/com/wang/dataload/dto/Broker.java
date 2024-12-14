package com.wang.dataload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Broker {

    private Integer id;

    private String brokerOverseasName;

    private String brokerOverseasAddress;

    private String brokerOverseasPhone;

    private String brokerDomesticName;

    private String brokerDomesticAddress;

    private String brokerDomesticPhone;

    private String bankName;

    private String bankAddress;

    private String swiftCode;

    private String bankAccount;

    private Date createTime;

    private Date lastUpdateDate;
}
