package whereQR.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.entity.dto.MemberDetailDto;
import whereQR.project.entity.dto.MemberSignupDto;
import whereQR.project.entity.dto.MemberLoginDto;
import whereQR.project.entity.dto.TokenInfo;
import whereQR.project.entity.Member;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.exception.CustomExceptions.NotFoundException;
import whereQR.project.jwt.JwtTokenProvider;
import whereQR.project.repository.MemberRepository;
import whereQR.project.utils.GetUser;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public Boolean existsMemberByUsernameAndRoles( String username, String role ){
        return memberRepository.existsMemberByUsernameAndRoles(username, role);
    }

    public Member getMember(String username){
        return memberRepository.findMemberByUsername(username).orElseThrow(() -> new NotFoundException("해당하는 사용자가 존재하지 않습니다.", this.getClass().toString()));
    }

    @Transactional
    public TokenInfo login(MemberLoginDto memberLoginDto){

        log.info("passwordEncoder.encode -> {}", passwordEncoder.encode(memberLoginDto.getPassword()));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberLoginDto.getUsername(),memberLoginDto.getPassword());

        Authentication authentication =  authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        return tokenInfo;
    }

    @Transactional
    public MemberSignupDto signUp(MemberSignupDto memberSignupDto){

        Member member = new Member(memberSignupDto.getUsername(),memberSignupDto.getAge(),  passwordEncoder.encode(memberSignupDto.getPassword()), memberSignupDto.getRoles());
        memberRepository.save(member);
        return member.toMemberSignupDto();
    }


    public MemberDetailDto detail(){

        Member member = memberRepository.findMemberByUsername(GetUser.getUserName()).orElseThrow(() ->  new NotFoundException("존재하지 않는 User입니다.",this.getClass().toString()));

        return member.toMemberDetailDto();
    }
}
