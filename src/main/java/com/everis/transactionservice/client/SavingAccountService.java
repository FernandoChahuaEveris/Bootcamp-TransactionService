package com.everis.transactionservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.everis.transactionservice.dto.BankingAccountDTO;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SavingAccountService {
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	public Mono<BankingAccountDTO> SavingAccountPersonalByAccountNumber(String accountNumber)
	{
		return null;
	}
	
	
}
