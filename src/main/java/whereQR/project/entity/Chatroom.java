package whereQR.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Chatroom {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    public Chatroom(){
        this.id = UUID.randomUUID();
    }
}
