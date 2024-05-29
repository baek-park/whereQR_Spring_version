package whereQR.project.domain.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.domain.file.File;
import whereQR.project.domain.file.FileService;
import whereQR.project.domain.file.dto.FileResponseDto;
import whereQR.project.domain.member.dto.KakaoSignupDto;
import whereQR.project.domain.member.dto.MemberEmailSignupDto;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.exception.CustomExceptions.NotFoundException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;

    public Boolean existsMemberByKakaoIdAndRole( Long kakaoId, Role role ){
        return memberRepository.existsMemberByKakaoIdAndRole(kakaoId, role);
    }

    public Boolean existsMemberByPhoneNumberAndRole(String phoneNumber, Role role){
        return memberRepository.existsMemberByPhoneNumberAndRole(phoneNumber, role);
    }

    public Boolean existsMemberByEmailAndRole(String email, Role role){
        return memberRepository.existsMemberByEmailAndRole(email, role);
    }

    public Member getMemberById(UUID id){
        return memberRepository.findById(id).orElseThrow(() -> new NotFoundException("해당하는 사용자가 존재하지 않습니다.", this.getClass().toString()));
    }

    public Member getMemberByKakaoIdAndRole(Long kakaoId, Role role){
        return memberRepository.findMemberByKakaoIdAndRole(kakaoId, role).orElseThrow(() -> new NotFoundException("해당하는 사용자가 존재하지 않습니다.", this.getClass().toString()));
    }

    public Member getMemberByPhoneNumberAndRole(String phoneNumber, Role role){
        return memberRepository.findMemberByPhoneNumberAndRole(phoneNumber, role).orElseThrow(() -> new NotFoundException("해당하는 사용자가 존재하지 않습니다.", this.getClass().toString()));
    }

    public Member getMemberByEmailAndRole(String email, Role role){
        return memberRepository.findMemberByEmailAndRole(email, role).orElseThrow(() -> new NotFoundException("해당하는 사용자가 존재하지 않습니다.", this.getClass().toString()));
    }

    public Member getMemberByRefreshToken(String refreshToken){
        return memberRepository.findMemberByRefreshToken(refreshToken).orElseThrow(() ->new NotFoundException("해당하는 사용자가 존재하지 않습니다.", this.getClass().toString()));
    }

    public void validatePassword(String providedPassword, String storedPasswordHash) {
        if (!passwordEncoder.matches(providedPassword, storedPasswordHash)) {
            throw new BadRequestException("비밀번호가 일치하지 않습니다.", this.getClass().toString());
        }
    }
    @Transactional
    public Member kakaoSignUp(KakaoSignupDto signupDto, Role role){
        Member member = new Member(signupDto.getKakaoId(),signupDto.getUsername(),signupDto.getPhoneNumber(), role);
        memberRepository.save(member);
        return member;
    }


    @Transactional
    public Member linkEmail(Member member, String email){

        member.linkEmail(email);
        return member;
    }

    @Transactional
    public Member linkKakao(Member member, Long kakaoId){
        member.linkKakao(kakaoId);
        return member;
    }

    @Transactional
    public FileResponseDto uploadProfile(Member member, UUID profileId){
        File profile = fileService.getFileById(profileId);
        profile.updateProfile(member);
        return profile.toFileResponseDto();
    }

    @Transactional
    public void deleteMemberById(Member member){
        memberRepository.deleteById(member.getId());
    }

    @Transactional
    public Member emailSignUp(MemberEmailSignupDto signupDto, Role role){
        String passwordHash = passwordEncoder.encode(signupDto.getPassword());
        Member member = new Member(signupDto.getEmail(),signupDto.getUsername(),signupDto.getPhoneNumber(), passwordHash, role);
        memberRepository.save(member);
        return member;
    }
}
