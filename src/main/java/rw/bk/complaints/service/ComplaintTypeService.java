
package rw.bk.complaints.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import rw.bk.complaints.domain.ComplaintType;
import rw.bk.complaints.repository.ComplaintTypeRepository;

@Service
public class ComplaintTypeService {

    private final ComplaintTypeRepository complaintTypeRepository;

    public ComplaintTypeService(
            ComplaintTypeRepository complaintTypeRepository) {

        this.complaintTypeRepository = complaintTypeRepository;
    }

    // Save a complaint type.
    public ComplaintType save(ComplaintType complaintType) {
        return complaintTypeRepository.save(complaintType);
    }

    // Get all complaint types.
    public List<ComplaintType> findAll() {
        return complaintTypeRepository.findAll();
    }

    // Get a complaint type by ID.
    public Optional<ComplaintType> findById(UUID id) {
        return complaintTypeRepository.findById(id);
    }

    // C3: Find complaint types that have no support agents.
    public List<ComplaintType> findUnusedComplaintTypes() {
        return complaintTypeRepository.findUnusedComplaintTypes();
    }

    // Delete a complaint type by ID.
    public boolean deleteById(UUID id) {
        if (complaintTypeRepository.findById(id).isEmpty()) {
            return false;
        }

        complaintTypeRepository.deleteById(id);
        return true;
    }
}
