
package rw.bk.complaints.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rw.bk.complaints.domain.ServiceDesk;
import rw.bk.complaints.repository.ServiceDeskRepository;

@Service
public class ServiceDeskService {

    @Autowired
    private ServiceDeskRepository serviceDeskRepository;

    public ServiceDesk saveServiceDesk(ServiceDesk serviceDesk) {
        return serviceDeskRepository.save(serviceDesk);
    }

    public List<ServiceDesk> getAllServiceDesks() {
        return serviceDeskRepository.findAll();
    }

    public Optional<ServiceDesk> getServiceDeskById(UUID id) {
        return serviceDeskRepository.findById(id);
    }

    public Optional<ServiceDesk> updateServiceDesk(
            UUID id, ServiceDesk updatedServiceDesk) {

        Optional<ServiceDesk> existing =
                serviceDeskRepository.findById(id);

        if (existing.isEmpty()) {
            return Optional.empty();
        }

        ServiceDesk serviceDesk = existing.get();
        serviceDesk.setDeskNumber(updatedServiceDesk.getDeskNumber());
        serviceDesk.setName(updatedServiceDesk.getName());

        return Optional.of(serviceDeskRepository.save(serviceDesk));
    }

    public boolean deleteServiceDesk(UUID id) {
        Optional<ServiceDesk> existing =
                serviceDeskRepository.findById(id);

        if (existing.isEmpty()) {
            return false;
        }

        serviceDeskRepository.delete(existing.get());
        return true;
    }

    public List<ServiceDesk> getFreeServiceDesks() {
        return serviceDeskRepository.findFreeServiceDesks();
    }

    public Optional<ServiceDesk> getServiceDeskByDeskNumber(
            int deskNumber) {
        return serviceDeskRepository.findByDeskNumber(deskNumber);
    }
}
