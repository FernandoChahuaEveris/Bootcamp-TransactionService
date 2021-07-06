package com.everis.transactionservice.movementbankingaccounttest;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.UUID;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.everis.transactionservice.client.SavingAccountService;
import com.everis.transactionservice.dao.collections.MovementBankingAccount;
import com.everis.transactionservice.dao.repository.MovementBankingAccountRepository;
import com.everis.transactionservice.dto.BankingAccountDTO;
import com.everis.transactionservice.dto.ClientDTO;
import com.everis.transactionservice.dto.ProductDTO;
import com.everis.transactionservice.service.MovementBankingAccountService;
import com.everis.transactionservice.service.impl.MovementBankingAccountServiceImpl;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TransactionBankingAccountTests {
	
	@Mock
	MovementBankingAccountRepository movementBankingRepository;
	@Mock
	SavingAccountService savingAccountService;
	
	@InjectMocks
	MovementBankingAccountServiceImpl movementBankingAccountService;
	
	@Before(value = "")
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	
	@Test
	public void PersonalSavingAccountWithdraw() {
		BankingAccountDTO savingAccount = BankingAccountDTO.builder()
										.accountNumber("1234")
										.accountNumberCard("123456789")
										.amountAvailable(1000)
										.client(new ClientDTO(UUID.randomUUID(),"P","77777777"))
										.idAccountSaving(UUID.randomUUID())
										.product(ProductDTO.builder()
															.amountMaintenance(10)
															.currency("PEN")
															.dateMovement(null)
															.limitMovements(10)
															.name("Saving Account Test")
															.typeProduct("SAP")
															.build())
										.build();
		
		when(savingAccountService.SavingAccountPersonalByAccountNumber("1234"))
			.thenReturn(Mono.just(savingAccount));
		
		when(movementBankingRepository.countByMoveDateGreaterThanEqualAndNumberAccount(LocalDate.now().withDayOfMonth(1), "1234"))
			.thenReturn(Mono.just(Long.valueOf(5)));
		
		MovementBankingAccount movementBankingAccount = MovementBankingAccount.builder()
																				.accountType("SAP")
																				.amount(300)
																				.numberAccount("1234")
																				.build();
		
		
		when(movementBankingRepository.save(movementBankingAccount)).thenReturn(Mono.just(movementBankingAccount));
		
		StepVerifier.create(movementBankingAccountService.withdrawalBankingAccount(movementBankingAccount))
					.expectNext(movementBankingAccount)
					.expectComplete()
					.verify();
		
		
		
	}
}
