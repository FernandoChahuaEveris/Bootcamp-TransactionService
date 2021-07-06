package com.everis.transactionservice.dao.collections;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovementBankingAccount {
	@Id
	private UUID moveBankingAccountId;
	
	private String numberAccount;
	private LocalDate moveDate;
	private double amount;
	private String accountType;
	private String moveType;

}
