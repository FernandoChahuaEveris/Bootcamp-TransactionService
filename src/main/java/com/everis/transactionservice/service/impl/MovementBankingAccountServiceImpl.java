package com.everis.transactionservice.service.impl;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.everis.transactionservice.client.SavingAccountService;
import com.everis.transactionservice.dao.collections.MovementBankingAccount;
import com.everis.transactionservice.dao.repository.MovementBankingAccountRepository;
import com.everis.transactionservice.dto.BankingAccountDTO;
import com.everis.transactionservice.service.MovementBankingAccountService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class MovementBankingAccountServiceImpl implements MovementBankingAccountService{
	@Autowired
	private MovementBankingAccountRepository movementBankingAccountRepository;
	
	@Value("transaction.type.withdrawal")
	private String withdrawalType;
	
	@Value("transaction.type.deposit")
	private String depositType;
	
	@Autowired
	private SavingAccountService savingAccountService;
	
	
	@Override
	public Mono<MovementBankingAccount> findById(UUID id) {
		return movementBankingAccountRepository.findById(id)
				.switchIfEmpty(null);
	}

	@Override
	public Flux<MovementBankingAccount> findAll() {
		return movementBankingAccountRepository.findAll();
	}

	@Override
	public Mono<MovementBankingAccount> update(UUID id, MovementBankingAccount entity) {
		log.debug("Update movement banking account, id={}, entity={} / method from MovementBankingAccountService",id,entity);
        return movementBankingAccountRepository.findById(id)
                .filter(e -> e.getMoveBankingAccountId().equals(entity.getMoveBankingAccountId()))
                .switchIfEmpty(Mono.error(new Exception("No coincide")))
                .flatMap(e -> movementBankingAccountRepository.save(e));
	}
	
	@Override
	public Mono<MovementBankingAccount> create(MovementBankingAccount entity) {
		return null;
	}

	@Override
	public Mono<MovementBankingAccount> deleteById(UUID id) {
		log.debug("Delete by id ,id={} / method from MovementBankingAccountServiceImpl",id);
        return movementBankingAccountRepository
                .findById(id)
                .flatMap(p -> movementBankingAccountRepository.deleteById(p.getMoveBankingAccountId()).thenReturn(p));
	}

	
	/*
	 * Cuentas Bancarias
	 * 
	 * Personal
	 ****Cuenta Ahorro
	 * Cuenta Corriente
	 * Cuenta Plazo Fijo
	 * 
	 * Empresarial
	 *****Cuenta Corriente
	 * */
	@Override
	public Mono<MovementBankingAccount> withdrawalBankingAccount(MovementBankingAccount movementBankingAccount) {
		/*
		 * Proceso de Retiro 
		 * Validar si la cuenta existe con ese tipo de cuenta (AccountType)
		 * Validar limite de movimientos
		 * validar si tiene saldo
		 * 
		 * */
		movementBankingAccount.setMoveDate(LocalDate.now());
		movementBankingAccount.setMoveType(withdrawalType);
		movementBankingAccount.setMoveBankingAccountId(UUID.randomUUID());
		Mono<BankingAccountDTO> monoBankingAccount = null;
		if(movementBankingAccount.getAccountType().equals("SAP")) {
			monoBankingAccount = savingAccountService.SavingAccountPersonalByAccountNumber(movementBankingAccount.getNumberAccount());
			
		}
		
		if(monoBankingAccount == null) return Mono.error(new Exception("No se encontro ningun flujo para los datos ingresados"));
		Mono<Long> contBankingAccount = movementBankingAccountRepository.countByMoveDateGreaterThanEqualAndNumberAccount(movementBankingAccount.getMoveDate().withDayOfMonth(1),movementBankingAccount.getNumberAccount());
		 
		return Mono.zip(monoBankingAccount, contBankingAccount).flatMap(d -> {
			
			Long monthMovements = d.getT2();
			BankingAccountDTO bankingAccount = d.getT1();
			
			if(bankingAccount.getProduct().getLimitMovements() <= monthMovements) {
				return Mono.error(new Exception("La cuenta ya cumplió con el limite de movimientos del mes"));
			}
			
			if(bankingAccount.getAmountAvailable() < movementBankingAccount.getAmount()) {
				return Mono.error(new Exception("La cuenta no tiene saldo suficiente para hacer el retiro"));
			}
			
			// Actualizar nuevo saldo
			
			double newAmount = bankingAccount.getAmountAvailable() - movementBankingAccount.getAmount();
			// TO DO consumir servicio para que actualice saldo
			
			return movementBankingAccountRepository.save(movementBankingAccount);
		});
		
		
	}

	@Override
	public Mono<MovementBankingAccount> depositBankingAccount(MovementBankingAccount movementBankingAccount) {
		/*
		 * Proceso de Deposito 
		 * Validar si la cuenta existe con ese tipo de cuenta (AccountType)
		 * Validar limite de movimientos
		 * */
		movementBankingAccount.setMoveDate(LocalDate.now());
		movementBankingAccount.setMoveType(depositType);
		movementBankingAccount.setMoveBankingAccountId(UUID.randomUUID());
		
		Mono<BankingAccountDTO> monoBankingAccount = null;
		if(movementBankingAccount.getAccountType().equals("SAP")) {
			monoBankingAccount = savingAccountService.SavingAccountPersonalByAccountNumber(movementBankingAccount.getNumberAccount());
			
		}
		
		
		Mono<Long> contBankingAccount = movementBankingAccountRepository.countByMoveDateGreaterThanEqualAndNumberAccount(movementBankingAccount.getMoveDate().withDayOfMonth(1),movementBankingAccount.getNumberAccount());
		 
		return Mono.zip(monoBankingAccount, contBankingAccount).flatMap(d -> {
			Long monthMovements = d.getT2();
			BankingAccountDTO bankingAccount = d.getT1();
			
			if(bankingAccount.getProduct().getLimitMovements() <= monthMovements) {
				return Mono.error(new Exception("La cuenta ya cumplió con el limite de movimientos del mes"));
			}
			
			
			double newAmount = bankingAccount.getAmountAvailable() + movementBankingAccount.getAmount();
			// TO DO consumir servicio para que actualice saldo
			
			return movementBankingAccountRepository.save(movementBankingAccount);
		});
	}
	

}
