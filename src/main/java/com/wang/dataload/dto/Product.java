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
public class Product {

    private Integer id;

    private String importProductModel;

    private String exportProductModel;

    private Date createTime;

    private Date lastUpdateDate;
}
