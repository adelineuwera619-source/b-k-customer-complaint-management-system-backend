
package rw.bk.complaints.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rw.bk.complaints.domain.Customer;
import rw.bk.complaints.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping({"", "/save"})
    public ResponseEntity<String> save(@RequestBody Customer customer) {
        customerService.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Customer saved successfully");
    }

    @GetMapping({"", "/all"})
    public ResponseEntity<List<Customer>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    // B1: Find customers by last name, ignoring case and sorting by first name
    @GetMapping("/by-lastname")
    public ResponseEntity<List<Customer>> findByLastName(
            @RequestParam String lastName) {

        return ResponseEntity.ok(
                customerService.findByLastName(lastName));
    }

    // C4: Find customers who have complaints assigned to a support agent
    @GetMapping("/of-support-agent/{supportAgentId}")
    public ResponseEntity<?> findCustomersBySupportAgent(
            @PathVariable UUID supportAgentId) {

        return customerService.findCustomersBySupportAgentId(supportAgentId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The support agent with that id does not exist"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        return customerService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The customer with that id does not exist"));
    }

    @PutMapping({"/{id}", "/update/{id}"})
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody Customer customer) {

        if (customerService.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The customer with that id does not exist");
        }

        customer.setId(id);
        customerService.save(customer);

        return ResponseEntity.ok("Customer updated successfully");
    }

    @DeleteMapping({"/{id}", "/delete/{id}"})
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        if (!customerService.deleteById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The customer with that id does not exist");
        }

        return ResponseEntity.ok("Customer deleted successfully");
    }
}
