package com.everis.transactionservice.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everis.transactionservice.dao.collections.MovementBankingAccount;
import com.everis.transactionservice.service.MovementBankingAccountService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(path = "/transactions/banking-account")
public class MovementBankingAccountController {
    @Autowired
    private MovementBankingAccountService movementBankingAccountService;

    @GetMapping
    public Flux<MovementBankingAccount> findAll() {
        log.debug("Find all transactionH » method from MovementBankingAccountController");
        return movementBankingAccountService.findAll();
    }

    @PostMapping
    public Mono<MovementBankingAccount> create(@RequestBody MovementBankingAccount entity){
        log.debug("Create transaction, {} / method from MovementBankingAccountController",entity);
        return movementBankingAccountService.create(entity);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<MovementBankingAccount>> findById(@PathVariable("id") UUID id){
        log.debug("Find by Id transaction, id = {} / method from MovementBankingAccountController",id);
        return movementBankingAccountService.findById(id)
                .map(e -> ResponseEntity.ok().body(e));
    }

    @DeleteMapping("/{id}")
    public Mono<MovementBankingAccount> deleteById(@PathVariable("id") UUID id){
        log.debug("Delete by Id , id = {} / method from MovementBankingAccountController",id);
        return movementBankingAccountService.deleteById(id);
    }

    @PutMapping("/{id}")
    public Mono<MovementBankingAccount> update(@PathVariable("id")UUID id, @RequestBody MovementBankingAccount entity){
        log.debug("Update transaction, id = {}, entity = {} / method from MovementBankingAccountController",id,entity);
        return movementBankingAccountService.update(id,entity);
    }
    
    @PostMapping("/withdrawal")
    public Mono<MovementBankingAccount> withdrawalBankingAccount(@RequestBody MovementBankingAccount movementBankingAccount){
    	return movementBankingAccountService.withdrawalBankingAccount(movementBankingAccount);
    }
    
    @PostMapping("/deposit")
    public Mono<MovementBankingAccount> depositBankingAccount(@RequestBody MovementBankingAccount movementBankingAccount){
    	return movementBankingAccountService.depositBankingAccount(movementBankingAccount);
    }
}