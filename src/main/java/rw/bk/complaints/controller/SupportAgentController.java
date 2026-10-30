
package rw.bk.complaints.controller;

import java.util.List;
import java.util.UUID;

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

import rw.bk.complaints.domain.SupportAgent;
import rw.bk.complaints.service.SupportAgentService;

@RestController
@RequestMapping("/api/support-agents")
public class SupportAgentController {

    private final SupportAgentService supportAgentService;

    public SupportAgentController(SupportAgentService supportAgentService) {
        this.supportAgentService = supportAgentService;
    }

    // Save a support agent.
    @PostMapping("/save")
    public ResponseEntity<String> save(
            @RequestBody SupportAgent supportAgent) {

        supportAgentService.save(supportAgent);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Support agent saved successfully");
    }

    // Get all support agents.
    @GetMapping("/all")
    public ResponseEntity<List<SupportAgent>> findAll() {
        return ResponseEntity.ok(supportAgentService.findAll());
    }

    // C1: Find support agents by complaint type name.
    @GetMapping("/by-complaint-type")
    public ResponseEntity<List<SupportAgent>> findByComplaintType(
            @RequestParam String name) {

        return ResponseEntity.ok(
                supportAgentService.findByComplaintTypeName(name));
    }

    // Get a support agent by ID.
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {

        return supportAgentService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The support agent with that id does not exist"));
    }

    // Update a support agent.
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(
            @PathVariable UUID id,
            @RequestBody SupportAgent supportAgent) {

        if (supportAgentService.update(id, supportAgent).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The support agent with that id does not exist");
        }

        return ResponseEntity.ok("Support agent updated successfully");
    }

    // Delete a support agent.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {

        if (!supportAgentService.deleteById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The support agent with that id does not exist");
        }

        return ResponseEntity.ok("Support agent deleted successfully");
    }

    // D3: Add a complaint type to a support agent.
    @PutMapping("/{supportAgentId}/complaint-types/{complaintTypeId}")
    public ResponseEntity<String> addComplaintType(
            @PathVariable UUID supportAgentId,
            @PathVariable UUID complaintTypeId) {

        String result = supportAgentService.addComplaintType(
                supportAgentId, complaintTypeId);

        if (result.equals("Complaint type added to the support agent")) {
            return ResponseEntity.ok(result);
        }

        if (result.equals("The support agent with that id does not exist")
                || result.equals("That complaint type does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        if (result.equals("The support agent already has that complaint type")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }

        return ResponseEntity.badRequest().body(result);
    }

    // D4: Reassign a support agent to a service desk.
    @PatchMapping("/{supportAgentId}/service-desk")
    public ResponseEntity<String> reassignServiceDesk(
            @PathVariable UUID supportAgentId,
            @RequestParam int deskNumber) {

        String result = supportAgentService.reassignServiceDesk(
                supportAgentId, deskNumber);

        if (result.equals("Service desk changed successfully")) {
            return ResponseEntity.ok(result);
        }

        if (result.equals("The support agent with that id does not exist")
                || result.equals("The service desk with that deskNumber does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        if (result.equals("That service desk is taken by another support agent")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }

        return ResponseEntity.badRequest().body(result);
    }
}
