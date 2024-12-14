package com.wang.dataload.util;

import com.wang.dataload.data.ExporterDataProcessorFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

@Slf4j
public class DataUtils {

    public static Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                log.debug(cell.getStringCellValue().trim() + "\t");
                return cell.getStringCellValue().trim();

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    log.debug(cell.getDateCellValue() + "\t");
                    return cell.getDateCellValue();
                } else {
                    log.debug(cell.getNumericCellValue() + "\t");
                    return Double.valueOf(cell.getNumericCellValue());
                }

            case BOOLEAN:
                log.debug(cell.getBooleanCellValue() + "\t");
                return Boolean.valueOf(cell.getBooleanCellValue());

            case FORMULA:
                log.debug(cell.getCellFormula() + "\t");
                log.debug("cell.getNumericCellValue() " + cell.getNumericCellValue());
                return Double.valueOf(cell.getNumericCellValue());

            default:
                log.debug(" ");
                return "";
        }

    }

    public static boolean  checkDigit(String item){
        boolean containsNumber = item.matches(".*\\d.*");
        return containsNumber;
    }


}
