package com.wang.mgt.controller;

import com.wang.dataload.dto.Product;
import com.wang.dataload.dto.ProformaInvoiceDTO;
import com.wang.dataload.dto.ProformaInvoiceOrderWithItem;
import com.wang.mgt.service.MerchantService;
import com.wang.mgt.service.ProformaInvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.apache.commons.io.IOUtils;

import com.wang.dataload.dto.ImportMerchant;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/proformalInvoice")
public class ProformaInvoiceController {

    @Autowired MerchantService merchantService;

    @Autowired
    ProformaInvoiceService proformaInvoiceService;

    @GetMapping("/findMerchantByNum")
    public ImportMerchant searchMerchant(){
        System.out.println("ProformaInvoiceController searchMerchant ");
        ImportMerchant importMerchant = merchantService.listImporterMerchant(118);

        return importMerchant;
    }

    @GetMapping("/findMerchant")
    public ImportMerchant getMerchant(){
        System.out.println("ProformaInvoiceController getMerchant ");
        ImportMerchant importMerchant = new ImportMerchant();
        importMerchant.setMerchantName("france");
        importMerchant.setMerchantAddress("urvan visat");
        IOUtils ioUtils = new IOUtils();

        return importMerchant;
    }

    @GetMapping("/findProformaInvoiceByNum")
    public ProformaInvoiceDTO searchProformaInvoice(){
        System.out.println("ProformaInvoiceController searchProformaInvoice ");
        ProformaInvoiceDTO proformaInvoiceDTO = proformaInvoiceService.searchProformaInvoiceByInvoiceNo("22W-012");
        System.out.println("proformaInvoiceDTO " + proformaInvoiceDTO.toString());
        return proformaInvoiceDTO;
    }

    @GetMapping("/findProformaInvoiceByProductModel/{productModel}")
    public List<ProformaInvoiceOrderWithItem>   searchProformaInvoiceById(@PathVariable("productModel") String productModel){
        System.out.println("ProformaInvoiceController searchProformaInvoiceById ");
        List<ProformaInvoiceOrderWithItem>  proformaInvoiceOrderWithItemList = proformaInvoiceService.selectProformaInvoiceWithItems(productModel);
        System.out.println("proformaInvoiceOrderWithItemList " + proformaInvoiceOrderWithItemList.toString());
        return proformaInvoiceOrderWithItemList;
    }

        @GetMapping("/findProductModels")
    public List<String> searchProductModels(){
        List<String> productModelList = proformaInvoiceService.selectProductModels();
        return productModelList;
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        // 检查文件是否为空
        if (file.isEmpty()) {
            return "文件上传失败: 文件为空";
        }
        boolean result = false;
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 设置上传目录
        String uploadPath = "/wangjianExport/backend/file";
        //String uploadPath ="E:\\wangjian\\upload";
        // 检查目录是否存在，不存在则创建
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        File dest = null;
        try {
            // 将文件保存到目标路径
            dest = new File(uploadPath + fileName);
            log.debug("import file " + dest.getName());
            file.transferTo(dest);
            result = proformaInvoiceService.loadUploadFile(dest);
            return "文件上传成功: " + fileName;
        } catch (IOException e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            return "文件上传失败: " + e.getMessage();

        }
    }

}
