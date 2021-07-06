package com.everis.transactionservice.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDTO {
	private UUID idClient;
	private String typeClient;
	private String dni;
	
}
