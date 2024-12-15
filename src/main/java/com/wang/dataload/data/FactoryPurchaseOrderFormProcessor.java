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
public class FactoryPurchaseOrderFormProcessor implements DataProcessor {

    private boolean remarkInd = false;

    private boolean orderFormItemInd = false;

    private boolean packingInd = false;

    private boolean deliveryInd = false;

    private boolean orderFormInd = false;

    private boolean exporterMerchantInd = false;

    private FactoryPurchaseOrderDTO factoryPurchaseOrderDTO;

    private FactoryPurchaseOrderItemDTO factoryPurchaseOrderItemDTO;

    private List<FactoryPurchaseOrderItemDTO> factoryPurchaseOrderItemDTOList;

    private ExportMerchant exportMerchant;

    private Broker broker;

    private Product product;

    private StringBuffer remarkBF;

    private StringBuffer packageBF;

    public FactoryPurchaseOrderDTO getFactoryPurchaseOrderDTO() {
        return factoryPurchaseOrderDTO;
    }

    public void setFactoryPurchaseOrderDTO(FactoryPurchaseOrderDTO factoryPurchaseOrderDTO) {
        this.factoryPurchaseOrderDTO = factoryPurchaseOrderDTO;
    }

    public void processData(Cell cell) {
        Object cellValue = DataUtils.getCellValue(cell);
        if (cellValue instanceof String) {
            String cellValueStr = (String) cellValue;
            log.debug("FactoryPurchaseOrderFormProcessor string cell value " + cellValueStr);
            if (cellValue.equals(ExporterConstants.FACTORY_ORDER_FORM_REMARK)) {
                log.debug(" set order form remark ind");
                remarkInd = true;
                remarkBF = new StringBuffer();
            } else if (cellValue.equals(ExporterConstants.FACTORY_ORDER_FORM_ITEM_START)) {
                log.debug(" set order form item ind");
                exporterMerchantInd = false;
                orderFormItemInd = true;
            }
            else if (cellValue.equals(ExporterConstants.FACTORY_ORDER_FORM_ITEM_TOTALS)) {
                log.debug(" end order form item ind");

                orderFormItemInd = false;
            }
            else if (cellValue.equals(ExporterConstants.FACTORY_ORDER_FORM_DELIVERY)) {
                log.debug(" set order form delivery ind");
                deliveryInd = true;
                remarkInd = false;
                factoryPurchaseOrderDTO.setRemark(remarkBF.toString());
            } else if (cellValue.equals(ExporterConstants.FACTORY_ORDER_FORM_PACKAGE) || cellValue.equals(ExporterConstants.FACTORY_ORDER_FORM_PACKAGE1)) {
                log.debug(" set order form package ind");
                packingInd = true;
                packageBF = new StringBuffer();
                deliveryInd = false;
            } else if (cellValue.equals(ExporterConstants.FACTORY_ORDER_FORM_NO)) {
                log.debug(" set order form  ind");
                orderFormInd = true;
                if (factoryPurchaseOrderDTO == null) {
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
            } else if (cellValue.equals(ExporterConstants.FACTORY_ORDER_FORM_EXPORTER_NAME)) {
                log.debug(" set order form export ind");
                orderFormInd = false;
                exporterMerchantInd = true;

            } else if (((String) cellValue).contains(ExporterConstants.WANGJIAN_EOD)){
                packingInd = false;
                factoryPurchaseOrderDTO.setPackageMode(packageBF.toString());
                String factoryOrderDate = ((String)cellValue).substring(ExporterConstants.WANGJIAN_EOD.length()).trim();
                log.debug("factoryOrderDate " + factoryOrderDate);
                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date localDate = null;
                try {
                    localDate = formatter1.parse(factoryOrderDate);
                    log.debug("localDate " + localDate);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }

                factoryPurchaseOrderDTO.setFactoryOrderDate(localDate);
            }


        }
        if (orderFormInd) {

            processOrder(cell);
        } else if (exporterMerchantInd) {
            processExporterMerchant(cell);
        } else if (orderFormItemInd) {
            log.debug("call processOrderFormItem");
            processOrderFormItem(cell);
        } else if (remarkInd) {
            processOrderFormRemark(cell);
        } else if (deliveryInd) {
            processOrderFormDelivery(cell);
        } else if (packingInd) {
            processOrderFormPacking(cell);
        }
    }

    public void processOrder(Cell cell) {
        Object cellValue = DataUtils.getCellValue(cell);
        if (cellValue instanceof String) {
            String cellValueStr = (String) cellValue;
            log.debug("FactoryPurchaseOrderFormProcessor processOrder string cell value " + cellValueStr);
            if (cell.getColumnIndex() == ExporterConstants.FACTORY_ORDER_FORM_ORDER_COLUMN) {
                log.debug("setFactoryOrderNumber " + cellValueStr);
                factoryPurchaseOrderDTO.setFactoryOrderNumber(cellValueStr);
              }
            else if(cell.getColumnIndex() == ExporterConstants.FACTORY_ORDER_FORM_BROKER_NAME_COLUMN) {
                log.debug("setBrokerOverseasName " + cellValueStr);
                factoryPurchaseOrderDTO.getBroker().setBrokerOverseasName(cellValueStr);
            }
        }
    }

    public void processExporterMerchant(Cell cell) {
        Object cellValue = DataUtils.getCellValue(cell);
        if (cellValue instanceof String) {
            String cellValueStr = (String) cellValue;
            log.debug("FactoryPurchaseOrderFormProcessor processExporterMerchant string cell value " + cellValueStr);
            if (cell.getColumnIndex() == ExporterConstants.FACTORY_ORDER_FORM_EXPORTER_MERCHANT_COLUMN) {
                log.debug("setMerchantName " + cellValueStr);
                factoryPurchaseOrderDTO.getExportMerchant().setMerchantName(cellValueStr);
            }
        }
    }


    public void processOrderFormDelivery(Cell cell) {
        Object cellValue = DataUtils.getCellValue(cell);
        if (cellValue instanceof String) {
            String cellValueStr = (String) cellValue;
            log.debug("FactoryPurchaseOrderFormProcessor processOrderFormDelivery string cell value " + cellValueStr);
            if (cell.getColumnIndex() == ExporterConstants.FACTORY_ORDER_FORM_DELIVERY_COLUMN) {
                log.debug("setDeliverMode " + cellValueStr);
                factoryPurchaseOrderDTO.setDeliverMode(cellValueStr);
            }
        }
    }

    public void processOrderFormRemark(Cell cell) {
        Object cellValue = DataUtils.getCellValue(cell);
        if (cellValue instanceof String) {
            String cellValueStr = (String) cellValue;
            log.debug("FactoryPurchaseOrderFormProcessor processOrderFormRemark string cell value " + cellValueStr);
            if (cell.getColumnIndex() == ExporterConstants.FACTORY_ORDER_FORM_REMARK_COLUMN) {
                log.debug("FACTORY_ORDER_FORM_REMARK_COLUMN " + cellValueStr);
                remarkBF.append(cellValueStr + "\n");

            }
        }
    }

    public void processOrderFormPacking(Cell cell) {
        Object cellValue = DataUtils.getCellValue(cell);
        if (cellValue instanceof String) {
            String cellValueStr = (String) cellValue;
            log.debug("FactoryPurchaseOrderFormProcessor processOrderFormPacking string cell value " + cellValueStr);
            if (cell.getColumnIndex() == ExporterConstants.FACTORY_ORDER_FORM_PACKAGE_COLUMN) {
                log.debug("FACTORY_ORDER_FORM_PACKAGE_COLUMN " + cellValueStr);
                packageBF.append(cellValueStr + "\n");

            }
        }
    }

    public void processOrderFormItem(Cell cell) {
        log.debug("call  processOrderFormItem " + cell.getColumnIndex());
        Object cellValue = DataUtils.getCellValue(cell);
        Double cellValueDouble = null;
        String cellValueStr = null;
        log.debug("cellvalue instance " + cellValue.getClass());
        if (cellValue instanceof String) {
            cellValueStr = ((String) cellValue).trim();

            log.debug("FactoryPurchaseOrderFormProcessor processOrderFormItem string cell value:" + cellValueStr);
            if (cell.getColumnIndex() == ExporterConstants.FACTORY_ORDER_FORM_ITEM_EXPORTER_MODELS_COLUMN) {
                log.debug("FACTORY_ORDER_FORM_ITEM_MODEL ExporterConstants.FACTORY_ORDER_FORM_ITEM_MODEL:" + ExporterConstants.FACTORY_ORDER_FORM_ITEM_MODEL);
                log.debug("FACTORY_ORDER_FORM_ITEM_MODEL ExporterConstants.FACTORY_ORDER_FORM_ITEM_START:" + ExporterConstants.FACTORY_ORDER_FORM_ITEM_START);
                log.debug("cellValueStr.equals(ExporterConstants.FACTORY_ORDER_FORM_ITEM_MODEL):" + cellValueStr.equals(ExporterConstants.FACTORY_ORDER_FORM_ITEM_MODEL));
                log.debug("cellValueStr.equals(ExporterConstants.FACTORY_ORDER_FORM_ITEM_START):" + cellValueStr.equals(ExporterConstants.FACTORY_ORDER_FORM_ITEM_START));

                if (!cellValueStr.equals(ExporterConstants.FACTORY_ORDER_FORM_ITEM_MODEL) && !cellValueStr.equals(ExporterConstants.FACTORY_ORDER_FORM_ITEM_START)) {
                    log.debug("FACTORY_ORDER_FORM_ITEM_MODEL    " + cellValueStr);

                    factoryPurchaseOrderItemDTO = new FactoryPurchaseOrderItemDTO();
                    factoryPurchaseOrderItemDTO.setCreateTime(new Date());
                    product = new Product();
                    product.setImportProductModel(cellValueStr);
                    factoryPurchaseOrderItemDTO.setProduct(product);
                    log.debug("factoryPurchaseOrderItemDTO   " + factoryPurchaseOrderItemDTO.toString());
                }
            } else if (cell.getColumnIndex() == ExporterConstants.FACTORY_ORDER_FORM_ITEM_EXPORTER_REMARK_COLUMN) {
                if (!cellValueStr.equals(ExporterConstants.FACTORY_ORDER_FORM_ITEM_REMARK)) {
                    log.debug("FACTORY_ORDER_FORM_ITEM_REMARK   " + cellValueStr);

                    factoryPurchaseOrderItemDTO.setRemark(cellValueStr);
                }
            }
        } else if (cellValue instanceof Double) {
            cellValueDouble = (Double) cellValue;
            log.debug("FactoryPurchaseOrderFormProcessor processOrderFormItem double cell value " + cellValueDouble);
            if (cell.getColumnIndex() == ExporterConstants.FACTORY_ORDER_FORM_ITEM_EXPORTER_QUANTITY_COLUMN) {

                log.debug("quantity   " + cellValueDouble);
                Integer quantity = (int) cellValueDouble.doubleValue();
                factoryPurchaseOrderItemDTO.setQuantity(quantity);
            } else if (cell.getColumnIndex() == ExporterConstants.FACTORY_ORDER_FORM_ITEM_UNIT_PRICE_COLUMN) {
                log.debug("unit price   " + cellValueDouble);
                BigDecimal unitPrice = BigDecimal.valueOf(cellValueDouble.doubleValue());
                factoryPurchaseOrderItemDTO.setUnitPriceRMB(unitPrice);
            } else if (cell.getColumnIndex() == ExporterConstants.FACTORY_ORDER_FORM_ITEM_TOTAL_COLUMN) {
                BigDecimal amount = BigDecimal.valueOf(cellValueDouble.doubleValue());
                log.debug("AMOUNT   " + amount);
                factoryPurchaseOrderItemDTO.setAmountRMB(amount);
                factoryPurchaseOrderDTO.getFactoryPurchaseOrderItemDTOList().add(factoryPurchaseOrderItemDTO);
                log.debug("getFactoryPurchaseOrderItemDTOList " + factoryPurchaseOrderDTO.getFactoryPurchaseOrderItemDTOList().toString());

            }
        }
    }


}