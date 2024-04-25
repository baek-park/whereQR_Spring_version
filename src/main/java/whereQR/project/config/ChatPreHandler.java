package whereQR.project.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import whereQR.project.domain.member.Member;
import whereQR.project.domain.member.Role;
import whereQR.project.exception.CustomExceptions.MalformedJwtException;
import whereQR.project.jwt.JwtTokenProvider;
import whereQR.project.jwt.MemberDetails;

import java.util.List;

/**
 * Todo : solve -> setAuthentication에 동작해서 들어가지만, MemberUtils에서 값을 가져오지 못함
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class ChatPreHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        try {
            StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
            String authorizationHeader = String.valueOf(headerAccessor.getNativeHeader("Authorization"));
            StompCommand command = headerAccessor.getCommand();


            if(command.equals(StompCommand.UNSUBSCRIBE) || command.equals(StompCommand.MESSAGE) ||
                    command.equals(StompCommand.CONNECTED) || command.equals(StompCommand.SEND)){
                return message;
            }

            else if (command.equals(StompCommand.ERROR)) {
                throw new MessageDeliveryException("error stomp command");
            }

            if (authorizationHeader == null) {
                log.error("chat header가 없는 요청입니다.");
                throw new MalformedJwtException("header가 없습니다.",this.getClass().toString());
            }

            //token 분리
            String token = "";
            String authorizationHeaderStr = authorizationHeader.replace("[","").replace("]","");
            if (authorizationHeaderStr.startsWith("Bearer ")) {
                token = authorizationHeaderStr.replace("Bearer ", "");
            } else {
                log.error("Authorization 헤더 형식이 틀립니다. : {}", authorizationHeader);
                throw new MalformedJwtException("Authorization 헤더 형식이 틀립니다.",this.getClass().toString());
            }

            Member member;
            try {
                MemberDetails memberDetails = jwtTokenProvider.getMemberByToken(token);
                member = memberDetails.getMember();
            } catch (Exception e) {
                throw new MalformedJwtException(e.getMessage(),this.getClass().toString());
            }

            boolean isTokenValid = jwtTokenProvider.validateToken(token);

            if (isTokenValid) {
                this.setAuthentication(message, headerAccessor, member);
            }
            log.info("jwt error not");
        } catch (Exception e) {
            log.error("JWT에러");
            throw new MalformedJwtException(e.getMessage(),this.getClass().toString());
        }

        return message;
    }

    private void setAuthentication(Message<?> message, StompHeaderAccessor headerAccessor, Member member) {
        MemberDetails memberDetails = new MemberDetails(member, new SimpleGrantedAuthority(Role.USER.getName()));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(memberDetails, null, List.of(new SimpleGrantedAuthority(Role.USER.getName())));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        headerAccessor.setUser(authentication);
        log.info("setAuthentication -> {}", authentication);
    }
}
