
package rw.bk.complaints.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import rw.bk.complaints.domain.Customer;
import rw.bk.complaints.repository.CustomerRepository;
import rw.bk.complaints.repository.SupportAgentRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final SupportAgentRepository supportAgentRepository;

    public CustomerService(
            CustomerRepository customerRepository,
            SupportAgentRepository supportAgentRepository) {

        this.customerRepository = customerRepository;
        this.supportAgentRepository = supportAgentRepository;
    }

    // Save a customer.
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    // Get all customers.
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    // Get a customer by ID.
    public Optional<Customer> findById(UUID id) {
        return customerRepository.findById(id);
    }

    // B1: Find customers by last name, ignoring case.
    public List<Customer> findByLastName(String lastName) {
        return customerRepository
                .findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    // C4: Find customers assigned to a support agent.
    public Optional<List<Customer>> findCustomersBySupportAgentId(
            UUID supportAgentId) {

        if (!supportAgentRepository.existsById(supportAgentId)) {
            return Optional.empty();
        }

        return Optional.of(
                customerRepository.findCustomersBySupportAgentId(supportAgentId));
    }

    // Delete a customer by ID.
    public boolean deleteById(UUID id) {
        if (customerRepository.findById(id).isEmpty()) {
            return false;
        }

        customerRepository.deleteById(id);
        return true;
    }
}
