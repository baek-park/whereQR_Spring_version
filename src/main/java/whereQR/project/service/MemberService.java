package whereQR.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.entity.dto.MemberSignupDto;
import whereQR.project.entity.dto.MemberLoginDto;
import whereQR.project.entity.dto.TokenInfo;
import whereQR.project.entity.member.Member;
import whereQR.project.jwt.JwtTokenProvider;
import whereQR.project.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TokenInfo login(MemberLoginDto memberLoginDto){

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberLoginDto.getUsername(), memberLoginDto.getPassword());
        Authentication authentication =  authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        return tokenInfo;
    }

    @Transactional
    public MemberSignupDto signUp(MemberSignupDto MemberSignupDto){

//        Long memberId = memberRepository.findMemberByUsername(MemberDto.getUsername()).get().getId();
//
//        if(memberRepository.existsById(memberId)){
//            throw new 예외처리 넣기
//        }

        Member member = MemberSignupDto.toMember(MemberSignupDto.getUsername(),MemberSignupDto.getAge(), MemberSignupDto.getEmail(), MemberSignupDto.getPassword());
        memberRepository.save(member);
        return MemberSignupDto;
    }


}
