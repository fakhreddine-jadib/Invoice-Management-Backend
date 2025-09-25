package com.emsi.invoicemanagementapp.controller;

import com.emsi.invoicemanagementapp.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/settings")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @GetMapping("/vat")
    public ResponseEntity<Map<String, BigDecimal>> getVatRate() {
        BigDecimal rate = settingService.getVatRate();
        return ResponseEntity.ok(Map.of("vatRate", rate));
    }

    @PutMapping("/vat")
    public ResponseEntity<Void> updateVatRate(@RequestBody Map<String, String> payload) {
        try {
            BigDecimal newRate = new BigDecimal(payload.get("vatRate"));
            settingService.updateVatRate(newRate);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}