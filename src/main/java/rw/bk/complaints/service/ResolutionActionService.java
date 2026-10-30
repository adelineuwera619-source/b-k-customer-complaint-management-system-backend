
package rw.bk.complaints.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import rw.bk.complaints.domain.Complaint;
import rw.bk.complaints.domain.ComplaintStatus;
import rw.bk.complaints.domain.ResolutionAction;
import rw.bk.complaints.repository.ComplaintRepository;
import rw.bk.complaints.repository.ResolutionActionRepository;

@Service
public class ResolutionActionService {

    private final ResolutionActionRepository resolutionActionRepository;
    private final ComplaintRepository complaintRepository;

    public ResolutionActionService(
            ResolutionActionRepository resolutionActionRepository,
            ComplaintRepository complaintRepository) {
        this.resolutionActionRepository = resolutionActionRepository;
        this.complaintRepository = complaintRepository;
    }

    // E1: Save a resolution action for a resolved complaint.
    public String save(UUID complaintId, ResolutionAction resolutionAction) {

        Optional<Complaint> optionalComplaint =
                complaintRepository.findById(complaintId);

        if (optionalComplaint.isEmpty()) {
            return "The complaint with that id does not exist";
        }

        Complaint complaint = optionalComplaint.get();

        if (complaint.getStatus() != ComplaintStatus.RESOLVED) {
            return "Resolution actions are only for RESOLVED complaints";
        }

        resolutionAction.setId(null);
        resolutionAction.setComplaint(complaint);
        resolutionAction.setActionDate(LocalDate.now());

        resolutionActionRepository.save(resolutionAction);

        return "Resolution action saved successfully";
    }

    // E2: Find resolution actions by customer account number.
    public List<ResolutionAction> findByAccountNumber(
            String accountNumber) {
        return resolutionActionRepository.findByAccountNumber(accountNumber);
    }

    public Optional<ResolutionAction> findById(UUID id) {
        return resolutionActionRepository.findById(id);
    }

    // E3: Delete an existing resolution action.
    public boolean deleteById(UUID id) {
        if (resolutionActionRepository.findById(id).isEmpty()) {
            return false;
        }

        resolutionActionRepository.deleteById(id);
        return true;
    }

    // Used by the complaint service to protect complaints
    // that still have resolution actions.
    public boolean existsByComplaintId(UUID complaintId) {
        return resolutionActionRepository.existsByComplaint_Id(complaintId);
    }
}
