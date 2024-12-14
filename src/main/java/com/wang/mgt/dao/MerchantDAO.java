package com.wang.mgt.dao;

import com.wang.dataload.dto.ImportMerchant;
import com.wang.dataload.dto.ProformaInvoiceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MerchantDAO {

    @Select("select * from import_merchant im where im.id = #{merchantId}")
    ImportMerchant listByMerchantId(@Param("merchantId") Integer merchantId);


}
