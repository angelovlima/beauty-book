package br.com.beauty_book.customer_management.api.controller;

import br.com.beauty_book.customer_management.api.dto.CreateCustomerApiRequest;
import br.com.beauty_book.customer_management.api.dto.CustomerApiResponse;
import br.com.beauty_book.customer_management.api.dto.UpdateCustomerApiRequest;
import br.com.beauty_book.customer_management.api.dto.mapper.CustomerApiMapper;
import br.com.beauty_book.customer_management.domain.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetAllCustomersUseCase getAllCustomersUseCase;
    private final GetCustomerByIdUseCase getCustomerByIdUseCase;
    private final DeleteCustomerByIdUseCase deleteCustomerByIdUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;

    public CustomerController(
            CreateCustomerUseCase createCustomerUseCase,
            GetAllCustomersUseCase getAllCustomersUseCase,
            GetCustomerByIdUseCase getCustomerByIdUseCase,
            DeleteCustomerByIdUseCase deleteCustomerByIdUseCase,
            UpdateCustomerUseCase updateCustomerUseCase) {
        this.createCustomerUseCase = createCustomerUseCase;
        this.getAllCustomersUseCase = getAllCustomersUseCase;
        this.getCustomerByIdUseCase = getCustomerByIdUseCase;
        this.deleteCustomerByIdUseCase = deleteCustomerByIdUseCase;
        this.updateCustomerUseCase = updateCustomerUseCase;
    }

    @Operation(
            summary = "Create Customer",
            description = "Registers a new customer in the system. Requires name and CPF as mandatory fields.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Customer successfully created",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Customer created",
                                            value = """
                            {
                                "id": 1,
                                "name": "Maria Lima",
                                "cpf": "123.456.789-00",
                                "phoneNumber": "+55 11 91234-5678",
                                "email": "maria@example.com"
                            }
                            """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request body",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Validation error",
                                            value = """
                            {
                                "message": "The field 'name' must not be null"
                            }
                            """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Customer already exists",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "CPF duplicated",
                                            value = """
                            {
                                "message": "A customer with CPF 123.456.789-00 already exists."
                            }
                            """
                                    )
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<CustomerApiResponse> create(@RequestBody CreateCustomerApiRequest request) {
        var customer = CustomerApiMapper.toDomain(request);
        var created = createCustomerUseCase.execute(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerApiMapper.toResponse(created));
    }


    @Operation(
            summary = "List Customers",
            description = "Returns a list of all registered customers in the system.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of customers",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CustomerApiResponse.class)),
                                    examples = @ExampleObject(
                                            name = "Customer List",
                                            value = """
                            [
                              {
                                "id": 1,
                                "name": "Maria Lima",
                                "cpf": "123.456.789-00",
                                "phoneNumber": "+55 11 91234-5678",
                                "email": "maria@example.com"
                              },
                              {
                                "id": 2,
                                "name": "João Lima",
                                "cpf": "987.654.321-00",
                                "phoneNumber": "+55 21 99876-5432",
                                "email": "joao@example.com"
                              }
                            ]
                            """
                                    )
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<CustomerApiResponse>> findAll() {
        var customers = getAllCustomersUseCase.execute();
        var response = customers.stream()
                .map(CustomerApiMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get Customer by ID",
            description = "Retrieves a single customer by its unique identifier.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerApiResponse.class),
                                    examples = @ExampleObject(
                                            name = "Customer Example",
                                            value = """
                            {
                              "id": 1,
                              "name": "Maria Lima",
                              "cpf": "123.456.789-00",
                              "phoneNumber": "+55 11 91234-5678",
                              "email": "maria@example.com"
                            }
                            """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Customer not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Not Found",
                                            value = """
                            {
                              "message": "Customer with ID 99 not found"
                            }
                            """
                                    )
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CustomerApiResponse> findById(@PathVariable Long id) {
        var customer = getCustomerByIdUseCase.execute(id);
        return ResponseEntity.ok(CustomerApiMapper.toResponse(customer));
    }

    @Operation(
            summary = "Delete Customer",
            description = "Deletes an existing customer by ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Customer successfully deleted"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Customer not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Not Found",
                                            value = """
                            {
                              "message": "Customer with ID 99 not found"
                            }
                            """
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        deleteCustomerByIdUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update Customer",
            description = "Updates an existing customer's information by ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer successfully updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerApiResponse.class),
                                    examples = @ExampleObject(
                                            name = "Updated Customer",
                                            value = """
                            {
                              "id": 1,
                              "name": "Giovanna Lima",
                              "cpf": "123.456.789-00",
                              "phoneNumber": "+55 11 90000-0000",
                              "email": "giovanna@example.com"
                            }
                            """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Customer not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Not Found",
                                            value = """
                            {
                              "message": "Customer with ID 99 not found"
                            }
                            """
                                    )
                            )
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CustomerApiResponse> update(@PathVariable Long id,
                                                      @RequestBody UpdateCustomerApiRequest request) {
        var updatedCustomer = updateCustomerUseCase.execute(id, CustomerApiMapper.toDomain(request, id));
        return ResponseEntity.ok(CustomerApiMapper.toResponse(updatedCustomer));
    }

    
}
