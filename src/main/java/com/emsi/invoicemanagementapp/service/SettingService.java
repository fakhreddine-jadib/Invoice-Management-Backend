package com.emsi.invoicemanagementapp.service;

import com.emsi.invoicemanagementapp.model.Setting;
import com.emsi.invoicemanagementapp.repository.SettingRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SettingService {

    public static final String VAT_RATE_KEY = "VAT_RATE";
    private static final String DEFAULT_VAT_RATE = "0.20"; // Default 20%

    @Autowired
    private SettingRepository settingRepository;

    // This method runs once after the service is created
    @PostConstruct
    public void initializeVatRate() {
        // If the VAT rate is not already in the database, create it with a default value
        settingRepository.findById(VAT_RATE_KEY).orElseGet(() -> {
            Setting defaultVat = new Setting();
            defaultVat.setKey(VAT_RATE_KEY);
            defaultVat.setValue(DEFAULT_VAT_RATE);
            return settingRepository.save(defaultVat);
        });
    }

    public BigDecimal getVatRate() {
        String vatRateValue = settingRepository.findById(VAT_RATE_KEY)
                .map(Setting::getValue)
                .orElse(DEFAULT_VAT_RATE);
        return new BigDecimal(vatRateValue);
    }

    public void updateVatRate(BigDecimal newRate) {
        Setting vatSetting = settingRepository.findById(VAT_RATE_KEY)
                .orElseThrow(() -> new RuntimeException("VAT_RATE setting not found!"));

        // We store the rate as a string (e.g., 0.20 for 20%)
        vatSetting.setValue(newRate.toPlainString());
        settingRepository.save(vatSetting);
    }
}