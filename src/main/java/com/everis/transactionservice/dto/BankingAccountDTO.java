package com.everis.transactionservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankingAccountDTO {
	private UUID idAccountSaving;
	
	private ClientDTO client;
	private ProductDTO product;
	private String accountNumberCard;
	private String accountNumber;
    private double amountAvailable;
}
