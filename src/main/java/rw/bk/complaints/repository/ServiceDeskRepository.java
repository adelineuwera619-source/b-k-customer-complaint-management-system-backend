
package rw.bk.complaints.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import rw.bk.complaints.domain.ServiceDesk;

public interface ServiceDeskRepository extends JpaRepository<ServiceDesk, UUID> {

    // C2: Find service desks without assigned support agents.
    @Query("SELECT s FROM ServiceDesk s WHERE NOT EXISTS " +
            "(SELECT a FROM SupportAgent a WHERE a.serviceDesk = s) " +
            "ORDER BY s.deskNumber ASC")
    List<ServiceDesk> findFreeServiceDesks();

    // D4: Find a service desk using its desk number.
    Optional<ServiceDesk> findByDeskNumber(int deskNumber);
}
