package com.wang.dataload.data;

import com.wang.dataload.dto.FactoryPurchaseOrderDTO;
import com.wang.dataload.dto.ProformaInvoiceDTO;
import com.wang.dataload.service.PersistentOrderService;
import com.wang.dataload.util.ExporterConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@Slf4j
public class DataFileImporter {

    String exportDataType = null;
    DataProcessor dataProcessor = null;
    String cellValue = null;


    @Autowired
    PersistentOrderService persistentOrderService;

    public static void main(String[] args) {
        DataFileImporter dataFileImporter = new DataFileImporter();
        //dataFileImporter.fileImporter(args[0]);
        //dataFileImporter.testDateTime();
    }

    public void testDateTime() {
        try {
            // SimpleDateFormat formatter1 = new SimpleDateFormat("MMM dd, yyyy",Locale.ENGLISH);
            //Date localDate1 = formatter1.parse("APR 11, 2022");
            //System.out.println("localDate1 " + localDate1.toString());

            //SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);


            // Parse the date string into a Date object
            //String dateString = "09-Aug-2023";
            //Date date = formatter.parse(dateString);

            // Output the parsed Date object
            //System.out.println("Parsed date: " + date);

            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
            //LocalDateTime localDate = LocalDateTime.parse("APR 11, 2022", formatter);
            //System.out.println("localDate " + localDate.toString());

            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            //Parse the date-time string into a LocalDateTime object
            //String dateTimeString = "2023-08-09 14:30:00";
            //String dateTimeString = "2023-08-09";
            //LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
            //System.out.println("dateTime " + dateTime.toString());

            String dateString = "2023-aug-09";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd", Locale.ENGLISH);

            try {
                LocalDate date = LocalDate.parse(dateString, formatter);
                System.out.println("Parsed date: " + date);
            } catch (DateTimeParseException e) {
                System.out.println("Failed to parse date: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean fileImporter(File file) {
        log.debug("fileImporter filename " + file.getName());
        ProformaInvoiceDTO proformaInvoiceDTO = null;
        List<FactoryPurchaseOrderDTO> factoryPurchaseOrderDTOList = null;
        FactoryPurchaseOrderDTO factoryPurchaseOrderDTO = null;
       try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            // Get the first sheet
            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            log.debug(" retrieve sheet");
            while (sheetIterator.hasNext()) {
                Sheet sheet = sheetIterator.next();
                // Iterate through rows
                Iterator<Row> rowIterator = sheet.iterator();
                log.debug(" retrieve row ");
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    int rowIndex = row.getRowNum();
                    log.debug("Row " + rowIndex + ": ");
                    // Iterate through cells
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        createDataProcessBasedCellValue(cell);
                    }
                 }
                log.debug(" sheet name " + sheet.getSheetName());
                if (sheet.getSheetName().equals(ExporterConstants.PROFORMAL_INVOICE_SHEET)) {
                    log.debug(" ProformaInvoice data object sheet");
                    ProformaInvoiceDataProcessor proformaInvoiceDataProcessor = (ProformaInvoiceDataProcessor)dataProcessor;
                    proformaInvoiceDTO = proformaInvoiceDataProcessor.getProformaInvoiceDTO();
                    log.debug(" ProformaInvoice data object " + proformaInvoiceDTO.toString());
                }
                else  if (sheet.getSheetName().equals(ExporterConstants.FACTORY_PURCHASE_ORDER_YOUKAI)) {
                    log.debug(" FactoryPurchaseOrderProcessor data object sheet YOUKAI");
                    FactoryPurchaseOrderProcessor factoryPurchaseOrderProcessor = (FactoryPurchaseOrderProcessor)dataProcessor;
                    factoryPurchaseOrderDTO = factoryPurchaseOrderProcessor.getFactoryPurchaseOrderDTO();
                    log.debug("factoryPurchaseOrderDTO " + factoryPurchaseOrderDTO.toString());
                    if(factoryPurchaseOrderDTOList == null) {
                        factoryPurchaseOrderDTOList = new ArrayList<FactoryPurchaseOrderDTO>();
                    }
                    factoryPurchaseOrderDTOList.add(factoryPurchaseOrderDTO);

                }
                else  if (sheet.getSheetName().equals(ExporterConstants.FACTORY_PURCHASE_ORDER_SIJIEER) ||sheet.getSheetName().equals(ExporterConstants.FACTORY_PURCHASE_ORDER_ZHANWANG)
                || sheet.getSheetName().equals(ExporterConstants.FACTORY_PURCHASE_ORDER_CHONNGCHUAN) || sheet.getSheetName().equals(ExporterConstants.FACTORY_PURCHASE_ORDER_YONGCHAO)) {
                    log.debug(" ProformaInvoice data object CHONNGCHUAN  or ZHANWANG or SIJIEER " );
                    FactoryPurchaseOrderFormProcessor factoryPurchaseOrderFormProcessor = (FactoryPurchaseOrderFormProcessor)dataProcessor;

                    factoryPurchaseOrderDTO = factoryPurchaseOrderFormProcessor.getFactoryPurchaseOrderDTO();
                    log.debug("factoryPurchaseOrderDTO " + factoryPurchaseOrderDTO.toString());
                    if(factoryPurchaseOrderDTOList == null) {
                        factoryPurchaseOrderDTOList = new ArrayList<FactoryPurchaseOrderDTO>();
                    }
                    factoryPurchaseOrderDTOList.add(factoryPurchaseOrderDTO);

                }
            }

        } catch (IOException e) {
           log.error("An error occurred: {}", e.getMessage(), e);
        }

        return persistentOrderService.persistentOrder(proformaInvoiceDTO, factoryPurchaseOrderDTOList);
    }

    public void createDataProcessBasedCellValue(Cell cell) {
        int cellIndex = cell.getColumnIndex();
        //log.debug("Cell " + cellIndex + ": ");

        //log.debug("CellType " + cell.getCellType() + ": ");
        switch (cell.getCellType()) {
            case STRING:
                log.debug("cell value" + cell.getStringCellValue() + "\t");
                cellValue = cell.getStringCellValue();
                // 基于每个sheet 页面的标题，设置状态标志。
                String cellValueTrimBetweenSpace = cellValue.replaceAll("\\s", "");
                if(cellValueTrimBetweenSpace.equals(ExporterConstants.FACTORY_PURCHASE_ORDER_FORM)){
                    log.debug("delete space between ExporterConstants.FACTORY_PURCHASE_ORDER_FORM");
                    cellValue = cellValueTrimBetweenSpace;
                }
                if (StringUtils.equals(cellValue, ExporterConstants.PROFORMA_INVOICE) ) {
                    log.debug("start to process proforma invoice ");
                    exportDataType = ExporterConstants.PROFORMA_INVOICE;
                    dataProcessor = ExporterDataProcessorFactory.createProduct(exportDataType);
                    log.debug("dataProcessor " + dataProcessor.toString());

                } else if (StringUtils.equals(cellValue, ExporterConstants.FACTORY_PURCHASE_ORDER)) {
                    log.debug("start to process factory purchase order");
                    exportDataType = ExporterConstants.FACTORY_PURCHASE_ORDER;
                    dataProcessor = ExporterDataProcessorFactory.createProduct(exportDataType);
                    log.debug("start to process factory purchase order " + dataProcessor.toString());
                } else if (StringUtils.equals(cellValue, ExporterConstants.FACTORY_PURCHASE_ORDER_FORM)) {
                    log.debug("start to process factory purchase order form");
                    exportDataType = ExporterConstants.FACTORY_PURCHASE_ORDER_FORM;
                    dataProcessor = ExporterDataProcessorFactory.createProduct(exportDataType);
                    log.debug("start to process factory purchase order form " + dataProcessor.toString());
                }
               // 基于状态标志 确定对应处理器

                if (dataProcessor != null) {
                    log.debug("dataProcessor " + dataProcessor.toString());
                    dataProcessor.processData(cell);
                }
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    log.debug(cell.getDateCellValue() + "\t");
                } else {
                    log.debug(cell.getNumericCellValue() + "\t");
                }
                // 基于状态标志 确定对应处理器

                if (dataProcessor != null) {
                   dataProcessor.processData(cell);
                }
                break;
            case BOOLEAN:
                log.debug(cell.getBooleanCellValue() + "\t");
               break;
            case FORMULA:
                log.debug(cell.getCellFormula() + "\t");
                // 基于状态标志 确定对应处理器
                if (dataProcessor != null) {
                  dataProcessor.processData(cell);
                }
                break;
            default:
                log.debug(" ");
        }
    }

    public void syncProformaInvoiceOrderItemProductIdWithFactoryPurchaseOrderItem(List<FactoryPurchaseOrderDTO> factoryPurchaseOrderDTOList, ProformaInvoiceDTO proformaInvoiceDTO){
        log.debug("start to sync ProformaInvoiceOrderItemProductIdWithFactoryPurchaseOrderItem");
        log.debug("factoryPurchaseOrderDTO.getFactoryPurchaseOrderItemDTOList()" + factoryPurchaseOrderDTOList.toString());
        factoryPurchaseOrderDTOList.forEach(factoryPurchaseOrderDTO ->{
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
            });
            factoryPurchaseOrderDTO.getFactoryPurchaseOrderItemDTOList().forEach(orderItem ->
                    log.debug("factoryPurchaseOrderDTO Product Name: " + orderItem.getProduct().getImportProductModel() +
                            ", Product ID: " + orderItem.getProduct().getId())
            );

        });


    }

}

