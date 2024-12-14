package com.wang.mgt.dao;

import com.wang.dataload.dto.Product;
import com.wang.dataload.dto.ProformaInvoiceDTO;
import com.wang.dataload.dto.ProformaInvoiceOrderWithItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProformaInvoiceDAO {

    ProformaInvoiceDTO searchProformaInvoiceByNum(@Param("proformaInvoiceNum") String proformaInvoiceNum );

    List<ProformaInvoiceOrderWithItem> selectProformaInvoiceWithItems(@Param("productModel") String productModel);

    List<Product> selectProductModels ();
}
