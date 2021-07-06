package com.everis.transactionservice.service;

import java.util.UUID;

import com.everis.transactionservice.dao.collections.MovementBankingAccount;

import reactor.core.publisher.Mono;

public interface MovementBankingAccountService extends CRUDService<MovementBankingAccount, UUID>{
	Mono<MovementBankingAccount> withdrawalBankingAccount(MovementBankingAccount movementBankingAccount);
	Mono<MovementBankingAccount> depositBankingAccount(MovementBankingAccount movementBankingAccount);
}
