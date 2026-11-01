package rw.bk.complaints.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import rw.bk.complaints.repository.ComplaintRepository;
import rw.bk.complaints.repository.CustomerRepository;
import rw.bk.complaints.repository.ResolutionActionRepository;
import rw.bk.complaints.repository.ServiceDeskRepository;
import rw.bk.complaints.repository.SupportAgentRepository;

@Service
public class DashboardService {

    private final CustomerRepository customerRepository;
    private final SupportAgentRepository supportAgentRepository;
    private final ServiceDeskRepository serviceDeskRepository;
    private final ComplaintRepository complaintRepository;
    private final ResolutionActionRepository resolutionActionRepository;

    public DashboardService(
            CustomerRepository customerRepository,
            SupportAgentRepository supportAgentRepository,
            ServiceDeskRepository serviceDeskRepository,
            ComplaintRepository complaintRepository,
            ResolutionActionRepository resolutionActionRepository) {

        this.customerRepository = customerRepository;
        this.supportAgentRepository = supportAgentRepository;
        this.serviceDeskRepository = serviceDeskRepository;
        this.complaintRepository = complaintRepository;
        this.resolutionActionRepository = resolutionActionRepository;
    }

    public Map<String, Long> getDashboardSummary() {

        Map<String, Long> summary = new LinkedHashMap<>();

        summary.put("customers", customerRepository.count());
        summary.put("supportAgents", supportAgentRepository.count());
        summary.put("freeServiceDesks", (long) serviceDeskRepository.findFreeServiceDesks().size());
        summary.put("complaintsToday", complaintRepository.countByComplaintDate(LocalDate.now()));
        summary.put("received", complaintRepository.countByStatus(
                rw.bk.complaints.domain.ComplaintStatus.RECEIVED));
        summary.put("resolutionActions", resolutionActionRepository.count());

        return summary;
    }
}