package com.itutorix.cs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("{customerId}")
    public ResponseEntity<CustomerDTO> getSingleCustomer(@PathVariable("customerId") Integer id) {
        return ResponseEntity.ok(customerService.getSingleCustomer(id));
    }

    @DeleteMapping("{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("customerId") Integer id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable("customerId") Integer id, @RequestBody CustomerDTO customerDTO) {
        customerService.updateCustomer(id, customerDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setName(customerDTO.name());
        customer.setAge(customerDTO.age());
        customer.setEmail(customerDTO.email());

        customerService.createCustomer(customer);
        return ResponseEntity.ok().build();
    }
}
