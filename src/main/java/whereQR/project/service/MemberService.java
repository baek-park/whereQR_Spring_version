package whereQR.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.entity.Role;
import whereQR.project.entity.Member;
import whereQR.project.entity.dto.member.KakaoSignupDto;
import whereQR.project.entity.dto.member.TokenInfo;
import whereQR.project.exception.CustomExceptions.NotFoundException;
import whereQR.project.jwt.JwtTokenProvider;
import whereQR.project.repository.MemberRepository;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final JwtTokenProvider jwtTokenProvider;

    private final MemberRepository memberRepository;

    public Boolean existsMemberByKakaoIdAndRole( Long kakaoId, Role role ){
        return memberRepository.existsMemberByKakaoIdAndRole(kakaoId, role);
    }

    public Member getMemberByUsername(String username){
        return memberRepository.findMemberByUsername(username).orElseThrow(() -> new NotFoundException("해당하는 사용자가 존재하지 않습니다.", this.getClass().toString()));
    }

    public Member getMemberById(UUID id){
        System.out.println(id);
        return memberRepository.findById(id).orElseThrow(() -> new NotFoundException("해당하는 사용자가 존재하지 않습니다.", this.getClass().toString()));
    }

    public Member getMemberByKakaoId(Long kakaoId){
        return memberRepository.findMemberByKakaoId(kakaoId).orElseThrow(() -> new NotFoundException("해당하는 사용자가 존재하지 않습니다.", this.getClass().toString()));
    }

    @Transactional
    public TokenInfo updateToken(Member member){
        // Token 발급
        String accessToken = jwtTokenProvider.getAccessToken(member);
        String refreshToken = jwtTokenProvider.getRefreshToken(member);

        // Refresh Token 저장
        member.setRefreshToken(refreshToken);

        return new TokenInfo(accessToken, refreshToken);
    }

    @Transactional
    public Member signUp(KakaoSignupDto signupDto, Role role){
        Member member = new Member(signupDto.getKakaoId(),signupDto.getUsername(),signupDto.getPhoneNumber(), role);
        memberRepository.save(member);
        return member;
    }

    @Transactional
    public void removeRefreshToken(Member member){
        member.setRefreshToken(null);
    }

    public void removeAccessTokenInCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("WhereQr-AccessToken", null);
        cookie.setPath("/");
        cookie.setSecure(false); // Todo : ssl 구매 후 변경 & local은 postman -> false
        cookie.setHttpOnly(true);

        // cookie를 저장
        response.addCookie(cookie);
    }

    public void accessTokenToCookie(String accessToken, HttpServletResponse response){

        Cookie cookie = new Cookie("WhereQr-AccessToken", accessToken);
        cookie.setPath("/");
        cookie.setSecure(false); // Todo : ssl 구매 후 변경 & local은 postman -> false
        cookie.setHttpOnly(true);
        cookie.setMaxAge(86400); // 1일로 설정. access token

       // cookie를 저장
        response.addCookie(cookie);
    }


}
