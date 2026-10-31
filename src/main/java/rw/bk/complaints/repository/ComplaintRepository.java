package rw.bk.complaints.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rw.bk.complaints.domain.Complaint;
import rw.bk.complaints.domain.ComplaintStatus;
import rw.bk.complaints.domain.SupportAgent;

public interface ComplaintRepository extends JpaRepository<Complaint, UUID> {

    // B2: Find complaints by status, earliest date first.
    List<Complaint> findByStatusOrderByComplaintDateAsc(
            ComplaintStatus status);

    // B3: Find complaints between two dates, inclusive.
    List<Complaint> findByComplaintDateBetweenOrderByComplaintDateAsc(
            LocalDate start, LocalDate end);

    // D1: Check duplicate complaints for the same agent and date,
    // excluding complaints with WITHDRAWN status.
    boolean existsBySupportAgentAndComplaintDateAndStatusNot(
            SupportAgent supportAgent,
            LocalDate complaintDate,
            ComplaintStatus status);

    // Bonus dashboard: Count complaints by status.
    long countByStatus(ComplaintStatus status);

    // Count complaints assigned to an agent on a specific date.
    long countBySupportAgent_IdAndComplaintDate(
            UUID supportAgentId,
            LocalDate complaintDate);

    // F1: Count complaints grouped by status using JPQL.
    @Query("SELECT c.status, COUNT(c) FROM Complaint c GROUP BY c.status")
    List<Object[]> countComplaintsByStatus();

    // F2: Withdraw complaints assigned to an agent on a date,
    // excluding RESOLVED complaints.
    @Modifying
    @Query("UPDATE Complaint c SET c.status = " +
            "rw.bk.complaints.domain.ComplaintStatus.WITHDRAWN " +
            "WHERE c.supportAgent.id = :supportAgentId " +
            "AND c.complaintDate = :date " +
            "AND c.status <> " +
            "rw.bk.complaints.domain.ComplaintStatus.RESOLVED")
    int cancelComplaintsForAgentOnDate(
            @Param("supportAgentId") UUID supportAgentId,
            @Param("date") LocalDate date);
}