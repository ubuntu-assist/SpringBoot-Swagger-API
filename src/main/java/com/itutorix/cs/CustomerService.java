package com.itutorix.cs;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerDTOMapper customerDTOMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerDTOMapper customerDTOMapper) {
        this.customerRepository = customerRepository;
        this.customerDTOMapper = customerDTOMapper;
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository
                .findAll()
                .stream()
                .map(customerDTOMapper)
                .collect(Collectors.toList());
    }

    public CustomerDTO getSingleCustomer(Integer id) {
        return customerRepository.findById(id)
                .map(customerDTOMapper)
                .orElseThrow(() -> new IllegalArgumentException("Customer with id [%s] not found".formatted(id)));
    }

    public void createCustomer(Customer newCustomer) {
        boolean exists = customerRepository.selectExistsEmail(newCustomer.getEmail());

        if(exists)
            throw new IllegalArgumentException("Email [%s] is already taken".formatted(newCustomer.getEmail()));

        customerRepository.save(newCustomer);
    }

    public void deleteCustomer(Integer id) {
        boolean exists = customerRepository.existsById(id);

        if(!exists)
            throw new IllegalArgumentException("Customer with id [%s] not found".formatted(id));

        customerRepository.deleteById(id);
    }

    public void updateCustomer(Integer id, CustomerDTO request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer with id [%s] not found".formatted(id)));

        boolean changes = false;

        if(request.email() != null && !customer.getEmail().equals(request.email())) {
            customer.setEmail(request.email());
            changes = true;
        }

        if(request.name() != null && !customer.getName().equals(request.name())) {
            customer.setName(request.name());
            changes = true;
        }

        if(request.age() != null && !request.age().equals(customer.getAge())) {
            customer.setAge(request.age());
            changes = true;
        }

        if(!changes)
            throw new IllegalArgumentException("No changes found");

        customerRepository.save(customer);
    }
}
