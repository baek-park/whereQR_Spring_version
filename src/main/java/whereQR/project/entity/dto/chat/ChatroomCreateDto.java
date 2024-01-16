package whereQR.project.entity.dto.chat;

import lombok.Data;

import java.util.UUID;

@Data
public class ChatroomCreateDto {

    UUID starter;

    UUID participant;

    public ChatroomCreateDto(){
    }
}
