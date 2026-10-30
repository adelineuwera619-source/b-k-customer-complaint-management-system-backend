
package rw.bk.complaints.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rw.bk.complaints.domain.ResolutionAction;

public interface ResolutionActionRepository
        extends JpaRepository<ResolutionAction, UUID> {

    // E2: Find resolution actions by customer account number,
    // with the newest action first.
    @Query("SELECT r FROM ResolutionAction r " +
           "WHERE r.complaint.customer.accountNumber = :accountNumber " +
           "ORDER BY r.actionDate DESC")
    List<ResolutionAction> findByAccountNumber(
            @Param("accountNumber") String accountNumber);

    // E3: Check whether a complaint has any resolution actions.
    boolean existsByComplaint_Id(UUID complaintId);
}
