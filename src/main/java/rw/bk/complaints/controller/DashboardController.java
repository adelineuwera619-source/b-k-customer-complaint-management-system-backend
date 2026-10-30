
package rw.bk.complaints.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rw.bk.complaints.repository.ComplaintRepository;
import rw.bk.complaints.repository.ComplaintTypeRepository;
import rw.bk.complaints.repository.CustomerRepository;
import rw.bk.complaints.repository.ServiceDeskRepository;
import rw.bk.complaints.repository.SupportAgentRepository;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final ComplaintRepository complaintRepository;
    private final CustomerRepository customerRepository;
    private final SupportAgentRepository supportAgentRepository;
    private final ComplaintTypeRepository complaintTypeRepository;
    private final ServiceDeskRepository serviceDeskRepository;

    public DashboardController(
            ComplaintRepository complaintRepository,
            CustomerRepository customerRepository,
            SupportAgentRepository supportAgentRepository,
            ComplaintTypeRepository complaintTypeRepository,
            ServiceDeskRepository serviceDeskRepository) {

        this.complaintRepository = complaintRepository;
        this.customerRepository = customerRepository;
        this.supportAgentRepository = supportAgentRepository;
        this.complaintTypeRepository = complaintTypeRepository;
        this.serviceDeskRepository = serviceDeskRepository;
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Long>> getDashboardSummary() {

        Map<String, Long> summary = new HashMap<>();

        summary.put("totalComplaints", complaintRepository.count());
        summary.put("totalCustomers", customerRepository.count());
        summary.put("totalSupportAgents", supportAgentRepository.count());
        summary.put("totalComplaintTypes", complaintTypeRepository.count());
        summary.put("totalServiceDesks", serviceDeskRepository.count());

        return ResponseEntity.ok(summary);
    }
}
