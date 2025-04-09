package br.com.beauty_book.customer_management.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payload to update an existing customer")
public record UpdateCustomerApiRequest(

        @Schema(description = "Customer full name", example = "Giovanna Lima")
        String name,

        @Schema(description = "CPF of the customer", example = "123.456.789-00")
        String cpf,

        @Schema(description = "Phone number with area code", example = "+55 11 91234-5678")
        String phoneNumber,

        @Schema(description = "Email address", example = "giovanna@example.com")
        String email

) {}
