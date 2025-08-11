//package com.osp.bttp.endpoint.public_resource;
//
//import com.osp.bttp.dao.service.common.saveExcel.SaveLsvn;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/v1/api/import-excel")
//public class ExcelImportController {
////    @Autowired
////    private ExcelUtilServiceimpl excelUtilService1;
//
//    @Autowired
//    private SaveLsvn saveLsvn;
//
////    @Autowired
////    private ExcelUtilServiceimpl3 excelUtilService3;
////
////    @Autowired
////    private ExcelUtilServiceimpl4 excelUtilService4;
//
////    @PostMapping("/tcvn")
////    public List<TCLSVn> getExcel(@RequestParam("file") MultipartFile file) throws IOException {
////        return excelUtilService1.readExcelFile(file);
////    }
//
//    @PostMapping("/lsvn/")
//    public void getExcel2(@RequestParam("file") MultipartFile file) throws IOException {
//        saveLsvn.importLsVn(file);
//    }
//
////    @PostMapping("/tcng")
////    public List<TCLSNg> getExcel3(@RequestParam("file") MultipartFile file) throws IOException {
////        return excelUtilService3.readExcelFile3(file);
////    }
////
////
////    @PostMapping("/lsng")
////    public List<LSNg> getExcel4(@RequestParam("file") MultipartFile file) throws IOException {
////        return excelUtilService4.readExcelFile4(file);
////    }
//
//
//}
