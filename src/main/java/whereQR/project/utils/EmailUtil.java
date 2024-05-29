package whereQR.project.utils;


import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EmailUtil {

    public static MimeMessage createEmailForm(String receiver, String subject, String code, JavaMailSender mailSender){


        MimeMessage message = mailSender.createMimeMessage();
        try{
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setSubject(subject);
            messageHelper.setTo(receiver);
            messageHelper.setText(
                    "<!DOCTYPE html>\n" +
                            "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"KO\">\n" +
                            " <head>\n" +
                            "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                            "  <title>Demystifying Email Design</title>\n" +
                            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                            "</head>\n" +
                            "<body style=\"width:468px; margin: auto;\">\n" +
                            "    <img alt=\"logo\" src=\"https://kr.object.ncloudstorage.com/whereqr/logo.png\" style=\"width: 120px; height: 50px;\">\n" +
                            "<div style=\"border-bottom: 1px solid #D7D9D8; width:468px; margin-top:12px;\"></div>\n" +
                            "    <h3 style=\"color: #2472FA; font-size: 24px; height: 16px;\">분실물 찾기는 whereqr!</h3>\n" +
                            "<p style=\"font-size: 16px; line-height: 24px; font-weight: 400;\" >\n" +
                            "    whereqr 회원가입 인증과정에 필요합니다.<br>\n" +
                            "아래 6자리 숫자를 인증코드 칸에 입력해주세요.\n" +
                            "    </p>\n" +
                            "  <p style=\"font-size: 24px; font-weight: 700; line-height: 42px; color:#2472FA;\">\n" +
                                 code +
                            "  </p>\n" +
                            "    <p style=\"color: #98999A; margin-top: 40px;\">\n" +
                            "        ©baek and park, All rights reserved.\n" +
                            "    </p>\n" +
                            "</body>\n" +
                            "</html>", true
            );


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return message;
    }

    public static String generateRandomCode(){

        int codeLength = 6;

        SecureRandom secureRandom;
        try{
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        StringBuilder code = new StringBuilder(codeLength);
        for(int i=0; i<codeLength; i++){
            code.append(secureRandom.nextInt(10));
        }
        return code.toString();

    }
}
