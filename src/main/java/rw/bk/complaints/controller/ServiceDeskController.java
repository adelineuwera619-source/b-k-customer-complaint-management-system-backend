
package rw.bk.complaints.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rw.bk.complaints.domain.ServiceDesk;
import rw.bk.complaints.service.ServiceDeskService;

@RestController
@RequestMapping("/api/service-desks")
public class ServiceDeskController {

    @Autowired
    private ServiceDeskService serviceDeskService;

    // A2: Save a service desk
    @PostMapping("/save")
    public ResponseEntity<String> saveServiceDesk(
            @RequestBody ServiceDesk serviceDesk) {

        serviceDeskService.saveServiceDesk(serviceDesk);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Service desk saved successfully");
    }

    // A2: Get all service desks
    @GetMapping("/all")
    public ResponseEntity<List<ServiceDesk>> getAllServiceDesks() {
        return ResponseEntity.ok(serviceDeskService.getAllServiceDesks());
    }

    // A2: Get service desk by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceDeskById(@PathVariable UUID id) {

        Optional<ServiceDesk> serviceDesk =
                serviceDeskService.getServiceDeskById(id);

        if (serviceDesk.isPresent()) {
            return ResponseEntity.ok(serviceDesk.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("The service desk with that id does not exist");
    }

    // A2: Update a service desk
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateServiceDesk(
            @PathVariable UUID id,
            @RequestBody ServiceDesk updatedServiceDesk) {

        Optional<ServiceDesk> serviceDesk =
                serviceDeskService.updateServiceDesk(id, updatedServiceDesk);

        if (serviceDesk.isPresent()) {
            return ResponseEntity.ok(serviceDesk.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("The service desk with that id does not exist");
    }

    // A2: Delete a service desk
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteServiceDesk(@PathVariable UUID id) {

        boolean deleted = serviceDeskService.deleteServiceDesk(id);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The service desk with that id does not exist");
        }

        return ResponseEntity.ok("Service desk deleted successfully");
    }

    // C2: Get service desks without support agents
    @GetMapping("/free")
    public ResponseEntity<List<ServiceDesk>> getFreeServiceDesks() {
        return ResponseEntity.ok(serviceDeskService.getFreeServiceDesks());
    }
}
