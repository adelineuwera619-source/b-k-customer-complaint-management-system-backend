
package rw.bk.complaints.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import rw.bk.complaints.domain.ComplaintType;
import rw.bk.complaints.domain.ServiceDesk;
import rw.bk.complaints.domain.SupportAgent;
import rw.bk.complaints.repository.ComplaintTypeRepository;
import rw.bk.complaints.repository.ServiceDeskRepository;
import rw.bk.complaints.repository.SupportAgentRepository;

@Service
public class SupportAgentService {

    private final SupportAgentRepository supportAgentRepository;
    private final ComplaintTypeRepository complaintTypeRepository;
    private final ServiceDeskRepository serviceDeskRepository;

    public SupportAgentService(
            SupportAgentRepository supportAgentRepository,
            ComplaintTypeRepository complaintTypeRepository,
            ServiceDeskRepository serviceDeskRepository) {

        this.supportAgentRepository = supportAgentRepository;
        this.complaintTypeRepository = complaintTypeRepository;
        this.serviceDeskRepository = serviceDeskRepository;
    }

    // Save a support agent.
    public SupportAgent save(SupportAgent supportAgent) {
        return supportAgentRepository.save(supportAgent);
    }

    // Get all support agents.
    public List<SupportAgent> findAll() {
        return supportAgentRepository.findAll();
    }

    // Get a support agent by ID.
    public Optional<SupportAgent> findById(UUID id) {
        return supportAgentRepository.findById(id);
    }

    // Update a support agent while preserving omitted fields.
    public Optional<SupportAgent> update(UUID id, SupportAgent details) {

        Optional<SupportAgent> optionalAgent =
                supportAgentRepository.findById(id);

        if (optionalAgent.isEmpty()) {
            return Optional.empty();
        }

        SupportAgent existing = optionalAgent.get();

        if (details.getFirstName() != null) {
            existing.setFirstName(details.getFirstName());
        }

        if (details.getLastName() != null) {
            existing.setLastName(details.getLastName());
        }

        if (details.getEmail() != null) {
            existing.setEmail(details.getEmail());
        }

        if (details.getServiceDesk() != null) {
            existing.setServiceDesk(details.getServiceDesk());
        }

        if (details.getComplaintTypes() != null) {
            existing.setComplaintTypes(details.getComplaintTypes());
        }

        return Optional.of(supportAgentRepository.save(existing));
    }

    // Delete a support agent by ID.
    public boolean deleteById(UUID id) {

        if (supportAgentRepository.findById(id).isEmpty()) {
            return false;
        }

        supportAgentRepository.deleteById(id);
        return true;
    }

    // C1: Find support agents by complaint type name.
    public List<SupportAgent> findByComplaintTypeName(String name) {
        return supportAgentRepository.findByComplaintTypeName(name);
    }

    // D3: Add a complaint type to a support agent.
    public String addComplaintType(
            UUID supportAgentId, UUID complaintTypeId) {

        Optional<SupportAgent> optionalAgent =
                supportAgentRepository.findById(supportAgentId);

        if (optionalAgent.isEmpty()) {
            return "The support agent with that id does not exist";
        }

        Optional<ComplaintType> optionalType =
                complaintTypeRepository.findById(complaintTypeId);

        if (optionalType.isEmpty()) {
            return "That complaint type does not exist";
        }

        SupportAgent agent = optionalAgent.get();
        ComplaintType type = optionalType.get();

        if (agent.getComplaintTypes().stream()
                .anyMatch(existing ->
                        existing.getId().equals(complaintTypeId))) {

            return "The support agent already has that complaint type";
        }

        agent.getComplaintTypes().add(type);
        supportAgentRepository.save(agent);

        return "Complaint type added to the support agent";
    }

    // D4: Reassign a support agent to a service desk.
    public String reassignServiceDesk(
            UUID supportAgentId, int deskNumber) {

        Optional<SupportAgent> optionalAgent =
                supportAgentRepository.findById(supportAgentId);

        if (optionalAgent.isEmpty()) {
            return "The support agent with that id does not exist";
        }

        Optional<ServiceDesk> optionalDesk =
                serviceDeskRepository.findByDeskNumber(deskNumber);

        if (optionalDesk.isEmpty()) {
            return "The service desk with that deskNumber does not exist";
        }

        SupportAgent agent = optionalAgent.get();
        ServiceDesk desk = optionalDesk.get();

        boolean occupiedByAnotherAgent =
                supportAgentRepository.existsByServiceDesk_IdAndIdNot(
                        desk.getId(), supportAgentId);

        if (occupiedByAnotherAgent) {
            return "That service desk is taken by another support agent";
        }

        agent.setServiceDesk(desk);
        supportAgentRepository.save(agent);

        return "Service desk changed successfully";
    }
}
