package whereQR.project.domain.member.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class MemberEmailSignupDto {

    @NotEmpty(message = "이름 입력은 필수 입니다.")
    @Size(max = 30, message = "30 자리를 넘어선 안 됩니다.")
    private String username;

    @NotEmpty(message = "이메일 입력은 필수 입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @Pattern(regexp = "regexp=\"(?=.*[0-9])(?=.*[a-z])(?=.*\\\\W)(?=\\\\S+$).{8,12}", message = "비밀번호는 0-9 중 최소 1개, a-z에서 최소 1개를 포함해 8자리 이상 12자리 미만이여야합니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Size(max = 12, message = "비밀번호는 최대 12자까지 입니다.")
    private String password;

    private String phoneNumber;
}
