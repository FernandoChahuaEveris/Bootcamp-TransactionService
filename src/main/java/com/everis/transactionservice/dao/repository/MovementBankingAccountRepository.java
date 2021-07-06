package com.everis.transactionservice.dao.repository;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.everis.transactionservice.dao.collections.MovementBankingAccount;

import reactor.core.publisher.Mono;

@Repository
public interface MovementBankingAccountRepository extends ReactiveMongoRepository<MovementBankingAccount, UUID>{
	Mono<Long> countByMoveDateGreaterThanEqualAndNumberAccount(LocalDate moveDate,String numberAccount);
}
