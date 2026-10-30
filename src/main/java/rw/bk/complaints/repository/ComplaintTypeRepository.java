
package rw.bk.complaints.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import rw.bk.complaints.domain.ComplaintType;

public interface ComplaintTypeRepository
        extends JpaRepository<ComplaintType, UUID> {

    // C3: Find complaint types that have no support agents.
    @Query("SELECT ct FROM ComplaintType ct " +
            "WHERE ct.supportAgents IS EMPTY")
    List<ComplaintType> findUnusedComplaintTypes();
}
