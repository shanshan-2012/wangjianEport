package com.wang.dataload.service;

import com.wang.dataload.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wang.dataload.dao.ProformaInvoiceMapper;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@Service
public class PersistentOrderService {

    @Autowired
    public ProformaInvoiceMapper proformaInvoiceMapper;

    //   @Transactional
//    public void createOrder(Order order, List<OrderItem> orderItems) {
//        // 插入订单记录，MyBatis会自动将生成的ID设置到order对象中
//        orderMapper.insertOrder(order);
//
//        // 获取生成的订单ID
//        Long orderId = order.getId();
//
//        // 插入每个订单项，使用生成的订单ID
//        for (OrderItem item : orderItems) {
//            item.setOrderId(orderId);
//            orderItemMapper.insertOrderItem(item);
//        }
//    }

    @Transactional
    public boolean persistentOrder(ProformaInvoiceDTO proformaInvoiceDTO, List<FactoryPurchaseOrderDTO> factoryPurchaseOrderDTOList) {
        log.debug("start to persistent order  factoryPurchaseOrderDTOList" + factoryPurchaseOrderDTOList.toString());
        log.debug("start to persistent order  proformaInvoiceDTO" + proformaInvoiceDTO.toString());
        ProformaInvoiceDTO proformaInvoiceDTO1 = saveProformaInvoice(proformaInvoiceDTO);
        List<FactoryPurchaseOrderDTO> factoryPurchaseOrderDTOList1 = syncProformaInvoiceOrderItemProductIdWithFactoryPurchaseOrderItem(proformaInvoiceDTO1, factoryPurchaseOrderDTOList);
        saveFactoryPurchaseOrder(factoryPurchaseOrderDTOList1, proformaInvoiceDTO1.getId());
        return true;
    }

    @Transactional
    public ProformaInvoiceDTO saveProformaInvoice(ProformaInvoiceDTO proformaInvoiceDTO) {

        Broker broker = proformaInvoiceMapper.searchBrokerByOverseasName(proformaInvoiceDTO.getBroker().getBrokerOverseasName());

        if(broker == null) {
            log.debug("no broker proformaInvoiceDTO broker" + proformaInvoiceDTO.getBroker().toString());

            proformaInvoiceMapper.insertBroker(proformaInvoiceDTO.getBroker());
            log.debug("after save broker " + proformaInvoiceDTO.getBroker().getId());
        }
        else{
            log.debug("found broker  " + proformaInvoiceDTO.getBroker().getId());
            proformaInvoiceDTO.getBroker().setId(broker.getId());
        }
        ImportMerchant importMerchant = proformaInvoiceMapper.searchImportMerchantByAddress(proformaInvoiceDTO.getImportMerchant().getMerchantAddress());
        if(importMerchant == null) {
            log.debug("not found importMerchant  " + proformaInvoiceDTO.getImportMerchant().toString());
            proformaInvoiceMapper.insertImportMerchant(proformaInvoiceDTO.getImportMerchant());
            log.debug("after save importMerchant " + proformaInvoiceDTO.getImportMerchant().getId());
        }
        else{
            log.debug(" found importMerchant  " + proformaInvoiceDTO.getImportMerchant().getId());
            proformaInvoiceDTO.getImportMerchant().setId(importMerchant.getId());
        }
        proformaInvoiceMapper.insertProformaInvoice(proformaInvoiceDTO);
        log.debug("after save proformaInvoiceDTO  " + proformaInvoiceDTO.getId());

        List<ProformaInvoiceOrderItemDTO> proformaInvoiceOrderItemDTOList = proformaInvoiceDTO.getProformaInvoiceOrderItemDTOList();
        for (ProformaInvoiceOrderItemDTO proformaInvoiceOrderItemDTO : proformaInvoiceOrderItemDTOList) {
            log.debug("proformaInvoiceOrderItemDTO " + proformaInvoiceOrderItemDTO.toString());
            Product product = proformaInvoiceMapper.searchProductByImportProductModel(proformaInvoiceOrderItemDTO.getProduct().getImportProductModel());

            if(product == null) {
                log.debug("not found product  " + proformaInvoiceOrderItemDTO.getProduct().toString());
                proformaInvoiceMapper.insertProduct(proformaInvoiceOrderItemDTO.getProduct());
                log.debug("after save product  " + proformaInvoiceOrderItemDTO.getProduct().getId());
            }
            else{
                log.debug(" found product  " + proformaInvoiceOrderItemDTO.getProduct().getId());
                proformaInvoiceOrderItemDTO.getProduct().setId(product.getId());
            }
            proformaInvoiceOrderItemDTO.setProformaInvoiceOrderId(proformaInvoiceDTO.getId());
            proformaInvoiceMapper.insertProformaInvoiceItem(proformaInvoiceOrderItemDTO);
            log.debug("after save proformaInvoiceOrderItem  " + proformaInvoiceOrderItemDTO.getId());
        }
        return proformaInvoiceDTO;
    }


    @Transactional
    public void saveFactoryPurchaseOrder(List<FactoryPurchaseOrderDTO> factoryPurchaseOrderDTOList, Integer proformaInvoiceId) {

        for (FactoryPurchaseOrderDTO factoryPurchaseOrderDTO : factoryPurchaseOrderDTOList) {
            log.debug("factoryPurchaseOrderDTO after sync " + factoryPurchaseOrderDTO.toString());
            factoryPurchaseOrderDTO.setProformaInvoiceId(proformaInvoiceId);
            Broker brokerDomestic = proformaInvoiceMapper.searchBrokerByDomesticName(factoryPurchaseOrderDTO.getBroker().getBrokerDomesticName());
            Broker brokerOverseas = proformaInvoiceMapper.searchBrokerByOverseasName(factoryPurchaseOrderDTO.getBroker().getBrokerOverseasName());

            if(brokerDomestic == null && brokerOverseas == null){
                log.debug("not found broker  " + factoryPurchaseOrderDTO.getBroker().toString());
                Broker brokerIns = factoryPurchaseOrderDTO.getBroker();
                if(StringUtils.isNotEmpty(brokerIns.getBrokerDomesticName()) || StringUtils.isNotEmpty(brokerIns.getBrokerOverseasName()) ) {
                    proformaInvoiceMapper.insertBroker(factoryPurchaseOrderDTO.getBroker());
                    log.debug("insert broker  " + factoryPurchaseOrderDTO.getBroker().getId());
                }
            }
            else if (brokerDomestic != null){
                log.debug("found domestic broker  " + brokerDomestic.getId());
                factoryPurchaseOrderDTO.getBroker().setId(brokerDomestic.getId());
            }
            else if (brokerOverseas != null){
                log.debug("found overseas broker  " + brokerOverseas.getId());
                factoryPurchaseOrderDTO.getBroker().setId(brokerOverseas.getId());
            }
            log.debug("after save broker " + factoryPurchaseOrderDTO.getBroker().getId());
            ExportMerchant exportMerchant = proformaInvoiceMapper.searchExportMerchantByName(factoryPurchaseOrderDTO.getExportMerchant().getMerchantName());
            if(exportMerchant == null){
                log.debug("not found exportMerchant  " + factoryPurchaseOrderDTO.getExportMerchant().toString());
                proformaInvoiceMapper.insertExportMerchant(factoryPurchaseOrderDTO.getExportMerchant());
                log.debug("insert exportMerchant  " + factoryPurchaseOrderDTO.getExportMerchant().getId());
            }
            else{
                log.debug("found exportMerchant  " + exportMerchant.getId());
                factoryPurchaseOrderDTO.getExportMerchant().setId(exportMerchant.getId());
            }
            log.debug("after save exportMerchant " + factoryPurchaseOrderDTO.getExportMerchant().getId());
            proformaInvoiceMapper.insertFactoryPurchaseOrder(factoryPurchaseOrderDTO);
            log.debug("after save factoryPurchaseOrder " + factoryPurchaseOrderDTO.getId());
            List<FactoryPurchaseOrderItemDTO> factoryPurchaseOrderItemDTOList = factoryPurchaseOrderDTO.getFactoryPurchaseOrderItemDTOList();
            for (FactoryPurchaseOrderItemDTO factoryPurchaseOrderItemDTO : factoryPurchaseOrderItemDTOList) {
                log.debug("factoryPurchaseOrderItemDTO " + factoryPurchaseOrderItemDTO.toString());
                factoryPurchaseOrderItemDTO.setFactoryPurchaseOrderId(factoryPurchaseOrderDTO.getId());
                proformaInvoiceMapper.insertFactoryPurchaseOrderItem(factoryPurchaseOrderItemDTO);
                log.debug("after save factoryPurchaseOrderItemDTO " + factoryPurchaseOrderItemDTO.getId());
            }

        }

    }

    public List<FactoryPurchaseOrderDTO> syncProformaInvoiceOrderItemProductIdWithFactoryPurchaseOrderItem(ProformaInvoiceDTO proformaInvoiceDTO, List<FactoryPurchaseOrderDTO> factoryPurchaseOrderDTOList){
        log.debug("start to sync ProformaInvoiceOrderItemProductIdWithFactoryPurchaseOrderItem factoryPurchaseOrderDTOList " + factoryPurchaseOrderDTOList.toString());
        log.debug("start to sync ProformaInvoiceOrderItemProductIdWithFactoryPurchaseOrderItem  proformaInvoiceDTO" + proformaInvoiceDTO.toString());
        factoryPurchaseOrderDTOList.forEach(factoryPurchaseOrderDTO ->{
            log.debug("factoryPurchaseOrderDTO in the loop"+ factoryPurchaseOrderDTO.toString());
            factoryPurchaseOrderDTO.getFactoryPurchaseOrderItemDTOList().forEach(factoryPurchaseOrderItemDTO -> {
                log.debug("factoryPurchaseOrderItemDTO "+ factoryPurchaseOrderItemDTO.toString());

                proformaInvoiceDTO.getProformaInvoiceOrderItemDTOList().stream()
                        .filter(proformaInvoiceOrderItemDTO -> {
                            boolean isMatch = proformaInvoiceOrderItemDTO.getProduct().getImportProductModel()
                                    .equals(factoryPurchaseOrderItemDTO.getProduct().getImportProductModel());
                            log.debug("Comparing Proforma Product Model: {} with Factory Product Model: {}, Match: {}",
                                    proformaInvoiceOrderItemDTO.getProduct().getImportProductModel(),
                                    factoryPurchaseOrderItemDTO.getProduct().getImportProductModel(),
                                    isMatch);
                            return isMatch;
                        })
                        .findFirst()
                        .ifPresent(proformaInvoiceOrderItemDTO -> {
                            factoryPurchaseOrderItemDTO.getProduct().setId(proformaInvoiceOrderItemDTO.getProduct().getId());
                            log.debug("Set Product ID: {} to Factory Purchase Order Item", proformaInvoiceOrderItemDTO.getProduct().getId());

                        });
                log.debug("after set product id factoryPurchaseOrderItemDTO "+ factoryPurchaseOrderItemDTO.toString());
            });
        });

        // 输出 OrderB 的 product 以验证
        factoryPurchaseOrderDTOList.forEach(factoryPurchaseOrderDTO -> {
            log.debug("factoryPurchaseOrderDTO in the product loop"+ factoryPurchaseOrderDTO.toString());
            factoryPurchaseOrderDTO.getFactoryPurchaseOrderItemDTOList().forEach(orderItem ->
                    log.debug("factoryPurchaseOrderDTO Product Name: " + orderItem.getProduct().getImportProductModel() +
                            ", Product ID: " + orderItem.getProduct().getId())
            );
        });
        log.debug("after to sync ProformaInvoiceOrderItemProductIdWithFactoryPurchaseOrderItem " + factoryPurchaseOrderDTOList.toString());
        return factoryPurchaseOrderDTOList;
    }

}
