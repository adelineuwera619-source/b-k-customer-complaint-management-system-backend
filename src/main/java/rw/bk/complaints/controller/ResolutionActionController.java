
package rw.bk.complaints.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rw.bk.complaints.domain.ResolutionAction;
import rw.bk.complaints.service.ResolutionActionService;

@RestController
@RequestMapping("/api/resolutionactions")
public class ResolutionActionController {

    private final ResolutionActionService resolutionActionService;

    public ResolutionActionController(
            ResolutionActionService resolutionActionService) {
        this.resolutionActionService = resolutionActionService;
    }

    // E1: Save a resolution action for a complaint.
    @PostMapping("/save")
    public ResponseEntity<String> save(
            @RequestParam UUID complaintId,
            @RequestBody ResolutionAction resolutionAction) {

        String result = resolutionActionService.save(
                complaintId, resolutionAction);

        if (result.equals("Resolution action saved successfully")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } else if (result.equals("The complaint with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else if (result.equals(
                "Resolution actions are only for RESOLVED complaints")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    // E2: Get resolution actions by customer account number.
    @GetMapping("/customer/{accountNumber}")
    public ResponseEntity<List<ResolutionAction>> findByAccountNumber(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
                resolutionActionService.findByAccountNumber(accountNumber));
    }

    // Get one resolution action by ID.
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {

        return resolutionActionService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The resolution action with that id does not exist"));
    }

    // E3: Delete a resolution action.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {

        if (!resolutionActionService.deleteById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The resolution action with that id does not exist");
        }

        return ResponseEntity.ok("Resolution action deleted successfully");
    }
}
