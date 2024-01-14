package whereQR.project.repository.chatroom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import whereQR.project.entity.Member;
import whereQR.project.entity.QChatroom;
import whereQR.project.entity.QMember;

@Repository
@RequiredArgsConstructor
public class CustomChatroomRepositoryImpl implements CustomChatroomRepository{

    private final JPAQueryFactory queryFactory;
    private final QChatroom chatroom = QChatroom.chatroom;

    @Override
    public Boolean existChatroomByUsers(Member starter, Member participant){

        QMember member = QMember.member;
        return queryFactory.selectFrom(chatroom)
                .leftJoin(chatroom.starter, member)
                .leftJoin(chatroom.participant, member)
                .where(chatroom.starter.eq(starter).and(chatroom.participant.eq(participant)))
                .fetchFirst() !=null;
    }

}
