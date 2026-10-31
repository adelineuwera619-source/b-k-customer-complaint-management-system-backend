package rw.bk.complaints.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rw.bk.complaints.domain.Complaint;
import rw.bk.complaints.domain.ComplaintStatus;
import rw.bk.complaints.repository.ComplaintRepository;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ResolutionActionService resolutionActionService;

    public ComplaintService(
            ComplaintRepository complaintRepository,
            ResolutionActionService resolutionActionService) {
        this.complaintRepository = complaintRepository;
        this.resolutionActionService = resolutionActionService;
    }

    public Complaint save(Complaint complaint) {
        return complaintRepository.save(complaint);
    }

    public String createComplaint(Complaint complaint) {

        if (complaint.getComplaintDate() == null) {
            return "Complaint date is required";
        }

        if (complaint.getComplaintDate().isBefore(LocalDate.now())) {
            return "Complaint date cannot be in the past";
        }

        if (complaint.getSupportAgent() == null
                || complaint.getSupportAgent().getId() == null) {
            return "A support agent is required";
        }

        if (complaintRepository
                .existsBySupportAgentAndComplaintDateAndStatusNot(
                        complaint.getSupportAgent(),
                        complaint.getComplaintDate(),
                        ComplaintStatus.WITHDRAWN)) {
            return "Support agent already has a complaint assigned on that date";
        }

        complaint.setStatus(ComplaintStatus.RECEIVED);
        complaintRepository.save(complaint);

        return "Complaint saved successfully";
    }

    public List<Complaint> findAll() {
        return complaintRepository.findAll();
    }

    // B2: Find complaints by status.
    public List<Complaint> findByStatus(ComplaintStatus status) {
        return complaintRepository
                .findByStatusOrderByComplaintDateAsc(status);
    }

    // B3: Find complaints between two dates.
    public List<Complaint> findBetweenDates(
            LocalDate start, LocalDate end) {
        return complaintRepository
                .findByComplaintDateBetweenOrderByComplaintDateAsc(
                        start, end);
    }

    // Bonus: Get complaints with pagination and sorting.
    public Page<Complaint> getComplaintsPage(Pageable pageable) {
        return complaintRepository.findAll(pageable);
    }

    public Optional<Complaint> findById(UUID id) {
        return complaintRepository.findById(id);
    }

    // Delete a complaint only if it has no resolution actions.
    public String deleteById(UUID id) {

        Optional<Complaint> optionalComplaint =
                complaintRepository.findById(id);

        if (optionalComplaint.isEmpty()) {
            return "The complaint with that id does not exist";
        }

        if (resolutionActionService.existsByComplaintId(id)) {
            return "That complaint has resolution actions, delete them first";
        }

        complaintRepository.deleteById(id);

        return "Complaint deleted successfully";
    }

    public String updateStatus(UUID id, ComplaintStatus newStatus) {

        Optional<Complaint> optionalComplaint =
                complaintRepository.findById(id);

        if (optionalComplaint.isEmpty()) {
            return "The complaint with that id does not exist";
        }

        Complaint complaint = optionalComplaint.get();
        ComplaintStatus currentStatus = complaint.getStatus();

        boolean allowed = false;

        if (currentStatus == ComplaintStatus.RECEIVED
                && (newStatus == ComplaintStatus.IN_PROGRESS
                || newStatus == ComplaintStatus.WITHDRAWN)) {
            allowed = true;
        } else if (currentStatus == ComplaintStatus.IN_PROGRESS
                && (newStatus == ComplaintStatus.RESOLVED
                || newStatus == ComplaintStatus.WITHDRAWN)) {
            allowed = true;
        }

        if (!allowed) {
            return "Cannot change status from "
                    + currentStatus + " to " + newStatus;
        }

        complaint.setStatus(newStatus);
        complaintRepository.save(complaint);

        return "Complaint status updated successfully";
    }

    // F1: Get complaint counts grouped by status.
    public List<Object[]> getComplaintStatisticsByStatus() {
        return complaintRepository.countComplaintsByStatus();
    }

    // F2: Cancel complaints assigned to an agent on a specific date.
    @Transactional
    public String cancelComplaintsForAgentOnDate(
            UUID supportAgentId, LocalDate date) {

        int cancelledCount =
                complaintRepository.cancelComplaintsForAgentOnDate(
                        supportAgentId, date);

        return cancelledCount + " complaints cancelled";
    }
}