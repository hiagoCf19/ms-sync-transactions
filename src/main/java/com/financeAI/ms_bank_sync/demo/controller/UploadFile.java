package com.financeAI.ms_bank_sync.demo.controller;

import com.financeAI.ms_bank_sync.demo.service.GeminiService;
import com.financeAI.ms_bank_sync.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/upload")

public class UploadFile {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private GeminiService geminiService;

    @PostMapping("/csv")
    public void uploadCsv(@RequestParam("file") MultipartFile file, @RequestParam String userId) {
        transactionService.uploadFile(file, userId);
    }

}
