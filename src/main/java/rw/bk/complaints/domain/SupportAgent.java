
package rw.bk.complaints.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "support_agent")
public class SupportAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    @ManyToOne
    @JoinColumn(name = "service_desk_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ServiceDesk serviceDesk;

    @ManyToMany
    @JoinTable(
        name = "support_agent_complaint_type",
        joinColumns = @JoinColumn(name = "support_agent_id"),
        inverseJoinColumns = @JoinColumn(name = "complaint_type_id")
    )
    private List<ComplaintType> complaintTypes = new ArrayList<>();

    public SupportAgent() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ServiceDesk getServiceDesk() {
        return serviceDesk;
    }

    public void setServiceDesk(ServiceDesk serviceDesk) {
        this.serviceDesk = serviceDesk;
    }

    public List<ComplaintType> getComplaintTypes() {
        return complaintTypes;
    }

    public void setComplaintTypes(List<ComplaintType> complaintTypes) {
        this.complaintTypes = complaintTypes;
    }
}

