
package rw.bk.complaints.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rw.bk.complaints.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    // B1: Find customers by last name, ignoring case,
    // and sort by first name in ascending order.
    List<Customer> findByLastNameIgnoreCaseOrderByFirstNameAsc(
            String lastName);

    // C4: Find distinct customers who have complaints
    // assigned to a particular support agent.
    @Query("SELECT DISTINCT c FROM Customer c " +
            "JOIN Complaint comp ON comp.customer = c " +
            "WHERE comp.supportAgent.id = :supportAgentId")
    List<Customer> findCustomersBySupportAgentId(
            @Param("supportAgentId") UUID supportAgentId);
}
