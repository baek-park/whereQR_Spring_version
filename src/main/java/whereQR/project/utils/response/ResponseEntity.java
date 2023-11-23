package whereQR.project.utils.response;

import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
public class ResponseEntity {

    @Enumerated(EnumType.STRING)
    public Status status;

    public Object data;

}
