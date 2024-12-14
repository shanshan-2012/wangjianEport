package com.wang.dataload.dao;

import com.wang.dataload.dto.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface ProformaInvoiceMapper {

       public int insertBroker(Broker broker);

        public int insertImportMerchant(ImportMerchant importMerchant);

        public Broker searchBrokerByDomesticName(String brokerDomesticName);

       public Broker searchBrokerByOverseasName(String brokerOverseasName);

        public  int insertExportMerchant(ExportMerchant exportMerchant);

        public ImportMerchant searchImportMerchantByName(String merchantName);

        public ExportMerchant searchExportMerchantByName(String merchantName);

        public  int insertProduct(Product product);

        public Product searchProductByImportProductModel(String importProductModel);

        public int insertProformaInvoice(ProformaInvoiceDTO proformaInvoiceDTO);

        public int insertProformaInvoiceItem(ProformaInvoiceOrderItemDTO proformaInvoiceOrderItemDTO);

        public int insertFactoryPurchaseOrder(FactoryPurchaseOrderDTO factoryPurchaseOrderDTO);

        public int insertFactoryPurchaseOrderItem(FactoryPurchaseOrderItemDTO factoryPurchaseOrderItemDTO);


}
