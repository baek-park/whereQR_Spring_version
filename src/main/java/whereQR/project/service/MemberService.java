package whereQR.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.config.SecurityConfig;
import whereQR.project.entity.dto.MemberDetailDto;
import whereQR.project.entity.dto.MemberSignupDto;
import whereQR.project.entity.dto.MemberLoginDto;
import whereQR.project.entity.dto.TokenInfo;
import whereQR.project.entity.Member;
import whereQR.project.exception.CustomExceptions.InvalidUsername;
import whereQR.project.exception.CustomExceptions.NotFoundException;
import whereQR.project.jwt.JwtTokenProvider;
import whereQR.project.repository.MemberRepository;
import whereQR.project.utils.GetUser;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TokenInfo login(MemberLoginDto memberLoginDto){
        // Todo : 비밀번호 암호화 사용을 위해 token 발급 method를 JWT.create()로 custom

        System.out.println(passwordEncoder.encode(memberLoginDto.getPassword()));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberLoginDto.getUsername(),memberLoginDto.getPassword());
        System.out.println("hereer======");
        Authentication authentication =  authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        System.out.println("hererererere----");
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        return tokenInfo;
    }

    @Transactional
    public MemberSignupDto signUp(MemberSignupDto memberSignupDto){

        Member member = new Member(memberSignupDto.getUsername(),memberSignupDto.getAge(), memberSignupDto.getEmail(),  passwordEncoder.encode(memberSignupDto.getPassword()), memberSignupDto.getRoles());
        memberRepository.save(member);
        return member.toMemberSignupDto();
    }

    public MemberDetailDto detail(){

        Member member = memberRepository.findMemberByUsername(GetUser.getUserName()).orElseThrow(() ->  new NotFoundException("존재하지 않는 User입니다.",this.getClass().toString()));

        return member.toMemberDetailDto();
    }
}
