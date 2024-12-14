package com.wang.dataload.service;

import com.wang.dataload.dao.ProformaInvoiceMapper;
import com.wang.dataload.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Ignore
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProformaInvoiceTest {

    @Autowired
    private ProformaInvoiceMapper proformaInvoiceMapper;


    @Test
    public void insertBroker(){
        log.debug("before save broker");
        Broker broker = new Broker();
        broker.setBankAccount("12345");
        broker.setBankName("bnp");
        broker.setBankAddress("raffle");
        broker.setBrokerOverseasName("canada merchant1");
        broker.setBrokerOverseasAddress("toronto");
        broker.setBrokerDomesticName("merchant1");
        broker.setBrokerDomesticAddress("hangzhou");
        broker.setSwiftCode("abccd");
        broker.setCreateTime(new Date());
        proformaInvoiceMapper.insertBroker(broker);
        log.debug("aftere save broker " + broker.getId());
    }

    @Test
    public void insertImportMerchant(){
        log.debug("before save importMerchant");
        ImportMerchant importMerchant = new ImportMerchant();
        importMerchant.setMerchantName("importer1");
        importMerchant.setMerchantAddress("tokyo1 ");
        importMerchant.setCreateTime(new Date());
        proformaInvoiceMapper.insertImportMerchant(importMerchant);
        log.debug("aftere save importMerchant"+ importMerchant.getId());
    }

    @Test
    public void insertExportMerchant(){
        log.debug("before save exportMerchant");
        ExportMerchant exportMerchant = new ExportMerchant();
        exportMerchant.setMerchantAddress("taizhou");
        exportMerchant.setMerchantName("guanghe electron");
        exportMerchant.setCreateTime(new Date());
        proformaInvoiceMapper.insertExportMerchant(exportMerchant);
        log.debug("after save exportMerchant" + exportMerchant.getId());
    }

    @Test
    public void insertFactoryPurchaseOrder(){
        log.debug("before save FactoryPurchaseOrder");
        ExportMerchant exportMerchant = new ExportMerchant();
        exportMerchant.setMerchantAddress("taizhou");
        exportMerchant.setMerchantName("guanghe electron");
        exportMerchant.setCreateTime(new Date());

        proformaInvoiceMapper.insertExportMerchant(exportMerchant);
        log.debug("after save exportMerchant" + exportMerchant.getId());

        Broker broker = new Broker();
        broker.setBankAccount("12345");
        broker.setBankName("bnpPO");
        broker.setBankAddress("raffle");
        broker.setBrokerOverseasName("canada merchant1");
        broker.setBrokerOverseasAddress("toronto");
        broker.setBrokerDomesticName("merchant1");
        broker.setBrokerDomesticAddress("hangzhou");
        broker.setSwiftCode("abccd");
        broker.setCreateTime(new Date());
        proformaInvoiceMapper.insertBroker(broker);
        log.debug("aftere save broker " + broker.getId());

        FactoryPurchaseOrderDTO factoryPurchaseOrderDTO = new FactoryPurchaseOrderDTO();
        factoryPurchaseOrderDTO.setProformaInvoiceId(1);
        factoryPurchaseOrderDTO.setFactoryOrderNumber("TL123-5");
        factoryPurchaseOrderDTO.setExportMerchant(exportMerchant);
        factoryPurchaseOrderDTO.setBroker(broker);
        factoryPurchaseOrderDTO.setFactoryOrderDate(new Date());
        factoryPurchaseOrderDTO.setCreateTime(new Date());
        factoryPurchaseOrderDTO.setOrderSignLocation("HangzhouPO");
        proformaInvoiceMapper.insertFactoryPurchaseOrder(factoryPurchaseOrderDTO);
        log.debug("after save FactoryPurchaseOrder" + factoryPurchaseOrderDTO.getId());
    }

    @Test
    public void insertProformaInvoice(){
        log.debug("before save proformaInvoiceOrder");
        ProformaInvoiceDTO proformaInvoiceDTO = new ProformaInvoiceDTO();
        proformaInvoiceDTO.setProformaInvoiceNum("PL34Iet-12");

        ImportMerchant importMerchant = new ImportMerchant();
        importMerchant.setMerchantName("importer1");
        importMerchant.setMerchantAddress("tokyo1ProI");
        importMerchant.setCreateTime(new Date());

        proformaInvoiceMapper.insertImportMerchant(importMerchant);
        log.debug("aftere save importMerchant"+ importMerchant.getId());

        Broker broker = new Broker();
        broker.setBankAccount("12345");
        broker.setBankName("bnpProI");
        broker.setBankAddress("raffle");
        broker.setBrokerOverseasName("canada merchant1");
        broker.setBrokerOverseasAddress("toronto");
        broker.setBrokerDomesticName("merchant1");
        broker.setBrokerDomesticAddress("hangzhou");
        broker.setSwiftCode("abccd");
        broker.setCreateTime(new Date());
        proformaInvoiceMapper.insertBroker(broker);
        log.debug("aftere save broker " + broker.getId());

        proformaInvoiceDTO.setImportMerchant(importMerchant);
        proformaInvoiceDTO.setBroker(broker);
        proformaInvoiceDTO.setRemark("mark1");
        proformaInvoiceDTO.setProformaInvoiceDate(new Date());
        proformaInvoiceDTO.setCreateTime(new Date());
        proformaInvoiceDTO.setProformaInvoiceTo("To");
        proformaInvoiceDTO.setProformaInvoiceFrom("From");

        proformaInvoiceMapper.insertProformaInvoice(proformaInvoiceDTO);

        log.debug("after save proformaInvoiceOrder" + proformaInvoiceDTO.getId());
    }

    @Test
    public void insertProformaInvoiceOrderItem(){
        log.debug("before save proformaInvoiceOrderItem");
        Product product = new Product();
        product.setExportProductModel("1234");
        ProformaInvoiceOrderItemDTO proformaInvoiceOrderItemDTO = new ProformaInvoiceOrderItemDTO();
        proformaInvoiceOrderItemDTO.setProformaInvoiceOrderId(1);
        proformaInvoiceOrderItemDTO.setProduct(product);
        proformaInvoiceOrderItemDTO.setUnitPriceRMB(new BigDecimal("111"));
        //proformaInvoiceOrderItemDTO.setUnitPriceUSD(new BigDecimal(23));
        //proformaInvoiceOrderItemDTO.setAmountRMB(new BigDecimal(124423));
       // proformaInvoiceOrderItemDTO.setAmountUSD(new BigDecimal(3452));
        proformaInvoiceOrderItemDTO.setQuantity(130);
        proformaInvoiceOrderItemDTO.setCreateTime(new Date());
        proformaInvoiceMapper.insertProformaInvoiceItem(proformaInvoiceOrderItemDTO);
        log.debug("after save proformaInvoiceOrderItem" + proformaInvoiceOrderItemDTO.getId());

    }

    @Test
    public void insertFactoryPurchaseOrderItem(){
        log.debug("before save factoryPurchaseOrderItem");
        Product product = new Product();
        product.setExportProductModel("1234");
        FactoryPurchaseOrderItemDTO factoryPurchaseOrderItemDTO = new FactoryPurchaseOrderItemDTO();
        factoryPurchaseOrderItemDTO.setFactoryPurchaseOrderId(1);
        factoryPurchaseOrderItemDTO.setProduct(product);
        factoryPurchaseOrderItemDTO.setUnitPriceRMB(new BigDecimal("123"));
        factoryPurchaseOrderItemDTO.setAmountRMB(new BigDecimal(1253423));
        factoryPurchaseOrderItemDTO.setQuantity(342);
        factoryPurchaseOrderItemDTO.setRemark("remark2");
        factoryPurchaseOrderItemDTO.setCreateTime(new Date());
        proformaInvoiceMapper.insertFactoryPurchaseOrderItem(factoryPurchaseOrderItemDTO);
        log.debug("after save factoryPurchaseOrderItem" + factoryPurchaseOrderItemDTO.getId());
    }
//
//

}
