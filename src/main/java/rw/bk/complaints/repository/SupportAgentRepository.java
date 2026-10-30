
package rw.bk.complaints.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rw.bk.complaints.domain.SupportAgent;

public interface SupportAgentRepository
        extends JpaRepository<SupportAgent, UUID> {

    // C1: Find support agents by complaint type name,
    // ignoring uppercase and lowercase differences.
    @Query("SELECT DISTINCT s FROM SupportAgent s " +
            "JOIN s.complaintTypes ct " +
            "WHERE LOWER(ct.name) = LOWER(:name)")
    List<SupportAgent> findByComplaintTypeName(
            @Param("name") String name);

    // D4: Check whether another support agent occupies the service desk.
    boolean existsByServiceDesk_IdAndIdNot(
            UUID serviceDeskId, UUID supportAgentId);
}
