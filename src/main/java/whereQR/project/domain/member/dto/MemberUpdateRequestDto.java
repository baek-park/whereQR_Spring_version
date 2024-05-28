package whereQR.project.domain.member.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class MemberUpdateRequestDto {

    @NotEmpty(message = "이름 입력은 필수 입니다.")
    @Size(min = 30, message = "30 자리를 넘어선 안 됩니다.")
    private String username;

    @Pattern(regexp = "\"^(010|011)\\\\d{8}$\";", message = "전화번호 형식이 유효하지 않습니다.")
    private String phoneNumber;
}
