package com.emsi.invoicemanagementapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "settings")
@Data
public class Setting {

    @Id
    @Column(name = "setting_key")
    private String key;

    @Column(name = "setting_value", nullable = false)
    private String value;
}