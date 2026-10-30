
package rw.bk.complaints.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rw.bk.complaints.domain.ComplaintType;
import rw.bk.complaints.service.ComplaintTypeService;

@RestController
@RequestMapping("/api/complainttypes")
public class ComplaintTypeController {

    private final ComplaintTypeService complaintTypeService;

    public ComplaintTypeController(ComplaintTypeService complaintTypeService) {
        this.complaintTypeService = complaintTypeService;
    }

    // Save a complaint type.
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody ComplaintType complaintType) {
        complaintTypeService.save(complaintType);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Complaint type saved successfully");
    }

    // Get all complaint types.
    @GetMapping("/all")
    public ResponseEntity<List<ComplaintType>> findAll() {
        return ResponseEntity.ok(complaintTypeService.findAll());
    }

    // C3: Find complaint types that have no support agents.
    @GetMapping("/unused")
    public ResponseEntity<List<ComplaintType>> findUnusedComplaintTypes() {
        return ResponseEntity.ok(
                complaintTypeService.findUnusedComplaintTypes());
    }

    // Get a complaint type by ID.
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        return complaintTypeService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The complaint type with that id does not exist"));
    }

    // Update a complaint type.
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody ComplaintType complaintType) {

        if (complaintTypeService.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The complaint type with that id does not exist");
        }

        complaintType.setId(id);
        complaintTypeService.save(complaintType);

        return ResponseEntity.ok("Complaint type updated successfully");
    }

    // Delete a complaint type.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        if (!complaintTypeService.deleteById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The complaint type with that id does not exist");
        }

        return ResponseEntity.ok("Complaint type deleted successfully");
    }
}
