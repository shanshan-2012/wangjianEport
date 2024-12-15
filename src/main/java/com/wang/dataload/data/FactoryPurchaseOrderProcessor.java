package com.wang.dataload.data;

import com.wang.dataload.dto.*;
import com.wang.dataload.util.DataUtils;
import com.wang.dataload.util.ExporterConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


@Slf4j
public class FactoryPurchaseOrderProcessor implements DataProcessor {

    private boolean remarkInd = false;

    private boolean orderItemInd = false;

    private boolean orderInd = false;

    private FactoryPurchaseOrderDTO factoryPurchaseOrderDTO;

    private FactoryPurchaseOrderItemDTO factoryPurchaseOrderItemDTO;

    private List<FactoryPurchaseOrderItemDTO> factoryPurchaseOrderItemDTOList;

    private ExportMerchant exportMerchant;

    private Broker broker;

    private Product product;

    private StringBuffer remarkBF;


    public FactoryPurchaseOrderDTO getFactoryPurchaseOrderDTO() {
        return factoryPurchaseOrderDTO;
    }

    public void setFactoryPurchaseOrderDTO(FactoryPurchaseOrderDTO factoryPurchaseOrderDTO) {
        this.factoryPurchaseOrderDTO = factoryPurchaseOrderDTO;
    }

    public void processData(Cell cell) {

        //处理proforma invoice 的 外贸商地址

        Object cellValue = DataUtils.getCellValue(cell);
        if (cellValue instanceof String) {
            String cellValueStr = (String) cellValue;
            log.debug("FactoryPurchaseOrderProcessor string cell value " + cellValueStr);
            if (cellValueStr.contains(ExporterConstants.FACTORY_ORDER_NO)) {
                orderInd = true;

            }
            if (cellValue.equals(ExporterConstants.FACTORY_ORDER_REMARK)) {
                remarkInd = true;
                orderInd = false;
                remarkBF = new StringBuffer();
            } else if (cellValue.equals(ExporterConstants.FACTORY_ORDER_ITEM_IMPORTER_MODELS)) {
                orderItemInd = true;
                remarkInd = false;
             } else if (cellValueStr.contains(ExporterConstants.FACTORY_ORDER_FORM_ITEM_SUMMARY)) {
                orderItemInd = false;
            }else if (cellValueStr.contains(ExporterConstants.FACTORY_ORDER_REMARK_2)) {
                remarkInd = true;
            }
            else if (((String) cellValue).contains(ExporterConstants.FACTORY_ORDER_SELL_STAMP)) {
                remarkInd = false;
                factoryPurchaseOrderDTO.setRemark(remarkBF.toString());
            }

        }
        if(orderInd){
            processFactoryOrder(cell);
        }
        else if (orderItemInd) {
            processFactoryOrderItem(cell);
        } else if (remarkInd) {
            log.debug("PerfInvoiceDataProcessor call processFactoryOrderRemark ");
            processFactoryOrderRemark(cell);
        }

    }

    private void initPurchaseOrder(){
        if(factoryPurchaseOrderDTO == null) {
            factoryPurchaseOrderDTO = new FactoryPurchaseOrderDTO();
            factoryPurchaseOrderDTO.setCreateTime(new Date());
            broker = new Broker();
            broker.setCreateTime(new Date());
            factoryPurchaseOrderDTO.setBroker(broker);
            exportMerchant = new ExportMerchant();
            exportMerchant.setCreateTime(new Date());
            factoryPurchaseOrderDTO.setExportMerchant(exportMerchant);
            factoryPurchaseOrderItemDTOList = new ArrayList<FactoryPurchaseOrderItemDTO>();
            factoryPurchaseOrderDTO.setFactoryPurchaseOrderItemDTOList(factoryPurchaseOrderItemDTOList);
        }
    }

    private void processFactoryOrder(Cell cell) {
        log.debug("process factory order column " + cell.getColumnIndex());
        Object cellValue = DataUtils.getCellValue(cell);
        String cellValueStr = null;
        initPurchaseOrder();
        try {
            if (cellValue instanceof String) {
                cellValueStr = (String) cellValue;
                log.debug("processFactoryOrder  " + cellValueStr);
                if (cellValueStr.contains(ExporterConstants.FACTORY_ORDER_NO)) {
                    log.debug("enter FACTORY_ORDER_NO ");
                            String[] factoryOrderNoParts = cellValueStr.split(":");
                    factoryPurchaseOrderDTO.setFactoryOrderNumber(factoryOrderNoParts[1].trim());
                    log.debug("factoryOrderNoParts " + factoryOrderNoParts[1].trim());


                } else if (cellValueStr.contains(ExporterConstants.FACTORY_ORDER_DATE) && cellValueStr.contains(ExporterConstants.FACTORY_ORDER_SIGN_LOCATION)) {
                    log.debug("enter FACTORY_ORDER_DATE ");
                    int dateIndex = cellValueStr.indexOf(ExporterConstants.FACTORY_ORDER_SIGN_LOCATION);
                    String factoryOrderDate = cellValueStr.substring(0, dateIndex);
                    String factoryOrderSignLocation = cellValueStr.substring(dateIndex, cellValueStr.length());
                    log.debug("factoryOrderDate " + factoryOrderDate);
                    log.debug("factoryOrderSignLocation " + factoryOrderSignLocation);
                    String[] factoryOrderDateParts = factoryOrderDate.split(ExporterConstants.CHINESE_COLON_DELIMITOR);
                    String[] factoryOrderSignLocationParts = factoryOrderSignLocation.split(ExporterConstants.CHINESE_COLON_DELIMITOR);
                    factoryPurchaseOrderDTO.setOrderSignLocation(factoryOrderSignLocationParts[1].trim());
                    log.debug("factoryOrderSignLocationParts[1].trim() " + factoryOrderSignLocationParts[1].trim());
                    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    Date localDate = formatter1.parse(factoryOrderDateParts[1].trim().toLowerCase());
                    log.debug("factoryOrderDateParts[1].trim() " + factoryOrderDateParts[1].trim());

                    factoryPurchaseOrderDTO.setFactoryOrderDate(localDate);
                } else if (cellValueStr.contains(ExporterConstants.FACTORY_ORDER_BROKER_NAME)) {
                    log.debug("enter FACTORY_ORDER_BROKER_NAME cellValueStr " + cellValueStr);
                    String[] factoryOrderBrokerNameParts = cellValueStr.split(ExporterConstants.CHINESE_COLON_DELIMITOR);
                    log.debug("enter FACTORY_ORDER_BROKER_NAME factoryPurchaseOrderDTO.getBroker() " + factoryPurchaseOrderDTO.getBroker());
                    factoryPurchaseOrderDTO.getBroker().setBrokerDomesticName(factoryOrderBrokerNameParts[1].trim());
                    log.debug("factoryOrderBrokerNameParts " + factoryOrderBrokerNameParts[1].trim());

                } else if (cellValueStr.contains(ExporterConstants.FACTORY_ORDER_EXPORTER_NAME)) {
                    log.debug("enter FACTORY_ORDER_EXPORTER_NAME cellValueStr " + cellValueStr);
                    String[] factoryOrderExporterNameParts = cellValueStr.split(ExporterConstants.CHINESE_COLON_DELIMITOR);
                    log.debug("enter FACTORY_ORDER_EXPORTER_NAME factoryPurchaseOrderDTO.getExportMerchant() " + factoryPurchaseOrderDTO.getExportMerchant());
                    factoryPurchaseOrderDTO.getExportMerchant().setMerchantName(factoryOrderExporterNameParts[1].trim());
                    log.debug("factoryOrderExporterNameParts " + factoryOrderExporterNameParts[1].trim());

                } else if (cellValueStr.contains(ExporterConstants.FACTORY_ORDER_BROKER_ADDRESS) && cell.getColumnIndex() == ExporterConstants.FACTORY_ORDER_BROKER_COLUMN) {
                    log.debug("enter FACTORY_ORDER_BROKER_ADDRESS ");
                        String[] factoryOrderBrokerAddressParts = cellValueStr.split(ExporterConstants.CHINESE_COLON_DELIMITOR);
                        factoryPurchaseOrderDTO.getBroker().setBrokerDomesticAddress(factoryOrderBrokerAddressParts[1].trim());
                        log.debug("factoryOrderBrokerAddressParts " + factoryOrderBrokerAddressParts[1].trim());


                } else if (cellValueStr.contains(ExporterConstants.FACTORY_ORDER_EXPORTER_ADDRESS) && cell.getColumnIndex() == ExporterConstants.FACTORY_ORDER_EXPORTER_COLUMN) {
                    log.debug("enter FACTORY_ORDER_EXPORTER_ADDRESS ");

                        String[] factoryOrderExporterAddressParts = cellValueStr.split(ExporterConstants.CHINESE_COLON_DELIMITOR);
                        factoryPurchaseOrderDTO.getExportMerchant().setMerchantAddress(factoryOrderExporterAddressParts[1].trim());
                        log.debug("factoryOrderExporterAddressParts " + factoryOrderExporterAddressParts[1].trim());


                } else if (cellValueStr.contains(ExporterConstants.FACTORY_ORDER_BROKER_PHONE) && cell.getColumnIndex() == ExporterConstants.FACTORY_ORDER_BROKER_COLUMN) {
                    log.debug("enter FACTORY_ORDER_BROKER_PHONE ");

                        String[] factoryOrderBrokerPhoneParts = cellValueStr.split(ExporterConstants.CHINESE_COLON_DELIMITOR);
                        factoryPurchaseOrderDTO.getBroker().setBrokerDomesticPhone(factoryOrderBrokerPhoneParts[1].trim());
                        log.debug("factoryOrderBrokerPhoneParts " + factoryOrderBrokerPhoneParts[1].trim());


                } else if (cellValueStr.contains(ExporterConstants.FACTORY_ORDER_EXPORTER_PHONE) && cell.getColumnIndex() == ExporterConstants.FACTORY_ORDER_EXPORTER_COLUMN) {
                    log.debug("enter FACTORY_ORDER_EXPORTER_PHONE ");

                        String[] factoryOrderExporterPhoneParts = cellValueStr.split(ExporterConstants.CHINESE_COLON_DELIMITOR);
                        factoryPurchaseOrderDTO.getExportMerchant().setMerchantPhone(factoryOrderExporterPhoneParts[1].trim());
                        log.debug("factoryOrderExporterPhoneParts " + factoryOrderExporterPhoneParts[1].trim());


                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void processFactoryOrderItem(Cell cell) {
        Object cellValue = DataUtils.getCellValue(cell);
        Double cellValueDouble = null;
        String cellValueStr = null;
        if (cellValue instanceof String) {
            cellValueStr = (String) cellValue;
            log.debug("FactoryPurchaseOrderProcessor processFactoryOrderItem string cell value " + cellValueStr);
            if (cell.getColumnIndex() == ExporterConstants.FACTORY_ORDER_ITEM_IMPORTER_MODELS_COLUMN) {
                if (!cellValueStr.equals(ExporterConstants.FACTORY_ORDER_ITEM_IMPORTER_MODELS) && !cellValueStr.equals(ExporterConstants.FACTORY_ORDER_FORM_ITEM_SUMMARY)){
                    log.debug("FACTORY_ORDER_ITEM_IMPORTER_MODELS    " + cellValueStr);
                    factoryPurchaseOrderItemDTO = new FactoryPurchaseOrderItemDTO();
                    factoryPurchaseOrderItemDTO.setCreateTime(new Date());
                    product = new Product();
                    if(cellValueStr.contains("/")){
                        String[] productModelArray = cellValueStr.split("/");
                        if(cellValueStr.startsWith(ExporterConstants.CENTER_BEARING_FROM)){
                            product.setImportProductModel(productModelArray[1].trim());
                            product.setExportProductModel(productModelArray[0].trim() + ExporterConstants.CENTER_BEARING_FROM_CN);
                        }
                        else if(cellValueStr.startsWith(ExporterConstants.BOOT_KIT_FROM)){
                            product.setImportProductModel(productModelArray[1].trim());
                            product.setExportProductModel(productModelArray[0].trim() + ExporterConstants.BOOT_KIT_FROM_CN);
                        }

                    }
                    else {

                        product.setImportProductModel(cellValueStr);
                    }
                    product.setCreateTime(new Date());

                    factoryPurchaseOrderItemDTO.setProduct(product);
                }
                else if(cellValueStr.equals(ExporterConstants.FACTORY_ORDER_FORM_ITEM_SUMMARY)){
                    log.debug(" set factoryPurchaseOrderItemDTO to null for summary");
                    factoryPurchaseOrderItemDTO = null;
                }
            } else if (cell.getColumnIndex() == ExporterConstants.FACTORY_ORDER_ITEM_EXPORTER_MODELS_COLUMN) {
                boolean containDigit = DataUtils.checkDigit(cellValueStr);
                if (!cellValueStr.equals(ExporterConstants.FACTORY_ORDER_ITEM_EXPORTER_MODELS)){
                    if(containDigit && !cellValueStr.equals(ExporterConstants.CENTER_BEARING_FROM) && !cellValueStr.equals(ExporterConstants.BOOT_KIT_FROM)) {
                        log.debug("FACTORY_ORDER_ITEM_EXPORTER_MODELS   " + cellValueStr);
                        product.setExportProductModel(cellValueStr);
                    }
                    else {
                        factoryPurchaseOrderItemDTO.setRemark(cellValueStr);
                    }
                }

            }
        } else if (cellValue instanceof Double) {
            cellValueDouble = (Double) cellValue;
            log.debug("FactoryPurchaseOrderProcessor processFactoryOrderItem double cell value " + cellValueDouble);
            if (cell.getColumnIndex() == ExporterConstants.FACTORY_ORDER_ITEM_EXPORTER_QUANTITY_COLUMN) {

                log.debug("quantity   " + cellValueDouble);
                Integer quantity = (int) cellValueDouble.doubleValue();
                    factoryPurchaseOrderItemDTO.setQuantity(quantity);

            } else if (cell.getColumnIndex() == ExporterConstants.FACTORY_ORDER_ITEM_UNIT_PRICE_COLUMN) {
                log.debug("unit price   " + cellValueDouble);
                BigDecimal unitPrice = BigDecimal.valueOf(cellValueDouble.doubleValue());
                    factoryPurchaseOrderItemDTO.setUnitPriceRMB(unitPrice);

            } else if (cell.getColumnIndex() == ExporterConstants.FACTORY_ORDER_ITEM_TOTAL_COLUMN) {
                BigDecimal amount = BigDecimal.valueOf(cellValueDouble.doubleValue());
                log.debug("AMOUNT   " + amount);
                    factoryPurchaseOrderItemDTO.setAmountRMB(amount);
                    factoryPurchaseOrderDTO.getFactoryPurchaseOrderItemDTOList().add(factoryPurchaseOrderItemDTO);
                    log.debug("getFactoryPurchaseOrderItemDTOList " + factoryPurchaseOrderDTO.getFactoryPurchaseOrderItemDTOList().toString());


            }
        }


    }

    private void processFactoryOrderRemark(Cell cell) {
        Object cellValue = DataUtils.getCellValue(cell);
        if (cellValue instanceof String) {
            String cellValueStr = (String) cellValue;
            log.debug("FactoryPurchaseOrderProcessor processFactoryOrderRemark string cell value " + cellValueStr);
            remarkBF.append(cellValueStr + "\n");

        }
    }

}
