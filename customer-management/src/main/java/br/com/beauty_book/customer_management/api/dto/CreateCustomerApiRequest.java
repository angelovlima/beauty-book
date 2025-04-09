package br.com.beauty_book.customer_management.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payload to register a new customer")
public record CreateCustomerApiRequest(

        @Schema(description = "Customer full name", example = "Maria Lima")
        String name,

        @Schema(description = "CPF of the customer", example = "123.456.789-00")
        String cpf,

        @Schema(description = "Phone number with area code", example = "+55 11 91234-5678")
        String phoneNumber,

        @Schema(description = "Email address", example = "maria@example.com")
        String email

) {}
