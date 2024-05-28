package whereQR.project.domain.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.exception.CustomExceptions.InternalException;
import whereQR.project.exception.CustomExceptions.NotFoundException;
import whereQR.project.utils.EmailUtil;

import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;


@Transactional(readOnly = true)
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailRepository emailRepository;

    public Email getEmailAuth(String email) {
        return emailRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("해당하는 이메일이 존재하지 않습니다.", this.getClass().toString()));
    }

    public Boolean existEmailAuth(String email) {
        return emailRepository.existByEmail(email);
    }


    @Transactional
    public EmailAuthDto sendEmail(String email){

        String randomCode = EmailUtil.generateRandomCode();
        MimeMessage emailForm = EmailUtil.createEmailForm(email, "whereqr 회원가입 인증번호", randomCode, mailSender);

        try {
            mailSender.send(emailForm);
        } catch (MailException e) {
            e.printStackTrace();
            throw new InternalException("unable to send email", this.getClass().toString());
        }

        return new EmailAuthDto(email,randomCode, LocalDateTime.now().plusMinutes(3));

    }

    @Transactional
    public Email createEmailAuth(EmailAuthDto dto) {
        Email emailAuth = new Email(
                dto.getEmail(),
                dto.getCode(),
                dto.getExpiresAt()
        );

        return emailRepository.save(emailAuth);
    }

    @Transactional
    public Email updateEmailAuth(Email email, EmailAuthDto dto) {
        email.updateAuth(dto.getCode(), dto.getExpiresAt());
        return email;
    }

    @Transactional
    public boolean verifyEmailCode(Email emailAuth, String code) {
        return emailAuth.getCode().equals(code) && !emailAuth.isExpiredCode();
    }
}
