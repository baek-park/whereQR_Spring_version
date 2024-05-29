package whereQR.project.domain.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import whereQR.project.domain.member.MemberService;
import whereQR.project.domain.member.Role;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.utils.response.ResponseEntity;
import whereQR.project.utils.response.Status;

@RequiredArgsConstructor
@RestController
@RequestMapping("/email")
public class EmailController {

    private final MemberService memberService;
    private final EmailService emailService ;

    @PostMapping("send")
    public ResponseEntity sendEmail(@RequestParam String email){

        // pattern matching
        if(memberService.existsMemberByEmailAndRole(email,Role.USER) == Boolean.TRUE){
            throw new BadRequestException("이미 가입된 이메일입니다.", this.getClass().toString());
        }

        EmailAuthDto emailAuthDto = emailService.sendEmail(email);

        // save code
        if (emailService.existEmailAuth(email)) {
            Email emailAuth = emailService.getEmailAuth(email);
            emailService.updateEmailAuth(emailAuth, emailAuthDto);
        } else {
            emailService.createEmailAuth(emailAuthDto);
        }

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(true)
                .build();
    }

    @GetMapping("valid")
    public ResponseEntity checkEmailCode(@RequestParam String email, @RequestParam String code) {
        Email emailAuth = emailService.getEmailAuth(email);

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(emailService.verifyEmailCode(emailAuth, code))
                .build();
    }



}
