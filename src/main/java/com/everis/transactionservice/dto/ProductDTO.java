package com.everis.transactionservice.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private UUID idProduct;
    private String name;
    private int limitMovements;
    private String currency;
    private double amountMaintenance;
    
    private String typeProduct;
    private Date dateMovement;
}
