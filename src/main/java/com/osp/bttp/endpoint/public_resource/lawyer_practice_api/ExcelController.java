//package com.osp.bttp.endpoint.public_resource.lawyer_practice_api;
//
//import com.osp.bttp.dao.service.common.ExcelUtilService;
//import com.osp.bttp.dao.service.common.modelExcelIn.TCLSVn;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//
//@RestController
//@RequestMapping("/v1/api/excel")
//public class ExcelController {
//    @Autowired
//    private ExcelUtilService excelUtilService;
//
//    @PostMapping("/")
//    public List<TCLSVn> getExcel(@RequestParam("file") MultipartFile file) throws IOException {
//       return excelUtilService.readExcelFile(file);
//    }
//
//}
