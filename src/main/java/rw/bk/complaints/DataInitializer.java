
package rw.bk.complaints;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import rw.bk.complaints.domain.Complaint;
import rw.bk.complaints.domain.ComplaintStatus;
import rw.bk.complaints.domain.ComplaintType;
import rw.bk.complaints.domain.Customer;
import rw.bk.complaints.domain.ServiceDesk;
import rw.bk.complaints.domain.SupportAgent;
import rw.bk.complaints.repository.ComplaintRepository;
import rw.bk.complaints.repository.ComplaintTypeRepository;
import rw.bk.complaints.repository.CustomerRepository;
import rw.bk.complaints.repository.ServiceDeskRepository;
import rw.bk.complaints.repository.SupportAgentRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeData(
            ComplaintRepository complaintRepository,
            CustomerRepository customerRepository,
            SupportAgentRepository supportAgentRepository,
            ComplaintTypeRepository complaintTypeRepository,
            ServiceDeskRepository serviceDeskRepository) {

        return args -> {

            List<Customer> customers = customerRepository.findAll();
            List<SupportAgent> agents = supportAgentRepository.findAll();
            List<ComplaintType> types = complaintTypeRepository.findAll();
            List<ServiceDesk> desks = serviceDeskRepository.findAll();
            List<Complaint> complaints = complaintRepository.findAll();

            if (customers.size() < 5 || agents.size() < 3
                    || types.size() < 4 || desks.size() < 4
                    || complaints.size() < 9) {

                System.out.println(
                        "DataInitializer skipped: expected existing sample records were not found.");
                return;
            }

            Customer marie = customerRepository.findById(
                    UUID.fromString(
                            "b72e22c5-18df-49a9-b5ce-d3a60e3654a1"))
                    .orElseThrow();

            SupportAgent alice = supportAgentRepository.findById(
                    UUID.fromString(
                            "09139d7f-39ce-4fc2-99dc-c82da09bdf73"))
                    .orElseThrow();

            SupportAgent eric = supportAgentRepository.findById(
                    UUID.fromString(
                            "389bb511-4568-4fcf-b9b7-0e2bda220706"))
                    .orElseThrow();

            SupportAgent jean = supportAgentRepository.findById(
                    UUID.fromString(
                            "a1cf991e-3527-4d48-b07a-d68eacb72590"))
                    .orElseThrow();

            ComplaintType account = complaintTypeRepository.findById(
                    UUID.fromString(
                            "77a7c317-c557-4637-ba94-54096358fa65"))
                    .orElseThrow();

            ComplaintType mobile = complaintTypeRepository.findById(
                    UUID.fromString(
                            "0b8c8dba-2c6e-4792-979d-d6b74842ab6a"))
                    .orElseThrow();

            ComplaintType card = complaintTypeRepository.findById(
                    UUID.fromString(
                            "21014a1c-b895-4c36-b832-0349671ed317"))
                    .orElseThrow();

            ServiceDesk desk1 = serviceDeskRepository.findById(
                    UUID.fromString(
                            "7a26d7be-f16b-44a9-96c9-434a2a83ebe9"))
                    .orElseThrow();

            // Set the required complaint-type assignments.
            alice.setComplaintTypes(List.of(card));
            eric.setComplaintTypes(List.of());
            jean.setComplaintTypes(List.of(account, mobile));

            // Move Jean to Desk 1, leaving Desk 4 unassigned.
            jean.setServiceDesk(desk1);

            supportAgentRepository.saveAll(List.of(alice, eric, jean));

            // Keep the previously tested withdrawn complaint on October 20.
            for (Complaint complaint : complaints) {
                if (complaint.getId().toString().equals(
                        "e103c202-e428-4788-a67c-60e05877855e")) {

                    complaint.setComplaintDate(
                            LocalDate.of(2026, 10, 20));

                    complaint.setStatus(ComplaintStatus.WITHDRAWN);
                }
            }

            // Add the tenth complaint only when there are exactly nine.
            if (complaints.size() == 9) {
                Complaint extra = new Complaint();

                extra.setComplaintDate(LocalDate.of(2026, 10, 20));
                extra.setStatus(ComplaintStatus.RECEIVED);
                extra.setSubject("Unable to access mobile banking");
                extra.setPriority("MEDIUM");
                extra.setCustomer(marie);
                extra.setSupportAgent(jean);

                complaints.add(extra);
            }

            // Count existing statuses before making any changes.
            long resolvedCount = complaints.stream()
                    .filter(c -> c.getStatus() == ComplaintStatus.RESOLVED)
                    .count();

            long inProgressCount = complaints.stream()
                    .filter(c -> c.getStatus() == ComplaintStatus.IN_PROGRESS)
                    .count();

            // Ensure at least two RESOLVED and one IN_PROGRESS complaint.
            for (Complaint complaint : complaints) {

                if (resolvedCount >= 2 && inProgressCount >= 1) {
                    break;
                }

                if (complaint.getStatus() == ComplaintStatus.WITHDRAWN) {
                    continue;
                }

                if (resolvedCount < 2
                        && complaint.getStatus() != ComplaintStatus.RESOLVED) {

                    complaint.setStatus(ComplaintStatus.RESOLVED);
                    resolvedCount++;

                } else if (inProgressCount < 1
                        && complaint.getStatus() != ComplaintStatus.IN_PROGRESS
                        && complaint.getStatus() != ComplaintStatus.RESOLVED) {

                    complaint.setStatus(ComplaintStatus.IN_PROGRESS);
                    inProgressCount++;
                }
            }

            complaintRepository.saveAll(complaints);

            System.out.println(
                    "DataInitializer completed. Complaints: "
                            + complaintRepository.count());
        };
    }
}
