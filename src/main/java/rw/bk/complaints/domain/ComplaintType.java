
package rw.bk.complaints.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "complaint_type")
public class ComplaintType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "complaintTypes")
    @JsonIgnore
    private List<SupportAgent> supportAgents = new ArrayList<>();

    public ComplaintType() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SupportAgent> getSupportAgents() {
        return supportAgents;
    }

    public void setSupportAgents(List<SupportAgent> supportAgents) {
        this.supportAgents = supportAgents;
    }
}
