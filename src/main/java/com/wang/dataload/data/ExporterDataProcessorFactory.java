package com.wang.dataload.data;

import com.wang.dataload.util.ExporterConstants;


public class ExporterDataProcessorFactory {

    public static DataProcessor createProduct(String type) {
        if (type == null || type.isEmpty()) {
            return null;
        }
        if (ExporterConstants.FACTORY_PURCHASE_ORDER.equals(type)) {
            return new FactoryPurchaseOrderProcessor();
        } else if (ExporterConstants.PROFORMA_INVOICE.equals(type)) {
            return new ProformaInvoiceDataProcessor();
        } else if (ExporterConstants.FACTORY_PURCHASE_ORDER_FORM.equals(type)){
            return new FactoryPurchaseOrderFormProcessor();
        }
        return null;
    }
}

