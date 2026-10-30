
package rw.bk.complaints.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rw.bk.complaints.domain.Complaint;
import rw.bk.complaints.domain.ComplaintStatus;
import rw.bk.complaints.service.ComplaintService;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    // D1: Save a new complaint.
    @PostMapping({"/save", ""})
    public ResponseEntity<String> save(@RequestBody Complaint complaint) {

        String result = complaintService.createComplaint(complaint);

        if (result.equals("Complaint saved successfully")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }

        if (result.equals(
                "Support agent already has a complaint assigned on that date")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    // Get all complaints.
    @GetMapping({"", "/all"})
    public ResponseEntity<List<Complaint>> findAll() {
        return ResponseEntity.ok(complaintService.findAll());
    }

    // Bonus: Get complaints with pagination and sorting.
    @GetMapping("/page")
    public ResponseEntity<Page<Complaint>> getComplaintsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "complaintDate,desc") String sort) {

        if (page < 0 || size < 1) {
            return ResponseEntity.badRequest().build();
        }

        String[] sortParts = sort.split(",", 2);
        String sortField = sortParts[0].trim();

        Sort.Direction direction = Sort.Direction.DESC;

        if (sortParts.length == 2) {
            try {
                direction = Sort.Direction.fromString(sortParts[1].trim());
            } catch (IllegalArgumentException exception) {
                return ResponseEntity.badRequest().build();
            }
        }

        if (!sortField.equals("complaintDate")
                && !sortField.equals("status")) {
            return ResponseEntity.badRequest().build();
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, sortField));

        return ResponseEntity.ok(
                complaintService.getComplaintsPage(pageable));
    }

    // F1: Get complaint statistics grouped by status.
    @GetMapping("/stats/bystatus")
    public ResponseEntity<List<Object[]>> getComplaintStatisticsByStatus() {
        return ResponseEntity.ok(
                complaintService.getComplaintStatisticsByStatus());
    }

    // Get a complaint by ID.
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {

        return complaintService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The complaint with that id does not exist"));
    }

    // Update a complaint without changing its status.
    @PutMapping({"/{id}", "/update/{id}"})
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody Complaint complaint) {

        var existingComplaint = complaintService.findById(id);

        if (existingComplaint.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The complaint with that id does not exist");
        }

        Complaint existing = existingComplaint.get();

        complaint.setId(id);
        complaint.setStatus(existing.getStatus());

        if (complaint.getCustomer() == null) {
            complaint.setCustomer(existing.getCustomer());
        }

        if (complaint.getSupportAgent() == null) {
            complaint.setSupportAgent(existing.getSupportAgent());
        }

        complaintService.save(complaint);

        return ResponseEntity.ok("Complaint updated successfully");
    }

    // Delete a complaint.
    @DeleteMapping({"/{id}", "/delete/{id}"})
    public ResponseEntity<String> delete(@PathVariable UUID id) {

        String result = complaintService.deleteById(id);

        if (result.equals("Complaint deleted successfully")) {
            return ResponseEntity.ok(result);
        }

        if (result.equals("The complaint with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        if (result.equals(
                "That complaint has resolution actions, delete them first")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }

        return ResponseEntity.badRequest().body(result);
    }

    // D2: Update complaint status.
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable UUID id,
            @RequestParam ComplaintStatus status) {

        String result = complaintService.updateStatus(id, status);

        if (result.equals("Complaint status updated successfully")) {
            return ResponseEntity.ok(result);
        }

        if (result.equals("The complaint with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    // F2: Cancel complaints assigned to an agent on a specific date.
    @PatchMapping("/cancelday")
    public ResponseEntity<String> cancelComplaintsForAgentOnDate(
            @RequestParam UUID supportAgentId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        String result = complaintService.cancelComplaintsForAgentOnDate(
                supportAgentId, date);

        return ResponseEntity.ok(result);
    }
}
