package whereQR.project.repository.chatroom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import whereQR.project.entity.Chatroom;
import whereQR.project.entity.Member;
import whereQR.project.entity.QChatroom;
import whereQR.project.entity.QMember;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Override
    public List<Chatroom> findChatroomsByMember(Member user) {
        QMember member = QMember.member;
        return queryFactory.selectFrom(chatroom)
                .leftJoin(chatroom.starter, member)
                .leftJoin(chatroom.participant, member)
                .where(chatroom.starter.eq(user).or(chatroom.participant.eq(user)))
                .fetchAll().stream().collect(Collectors.toList());
    }

    @Override
    public Optional<UUID> findChatroomByMemberIds(UUID starterId, UUID participantId) {
        return Optional.ofNullable(queryFactory.select(chatroom.id)
                        .from(chatroom)
                        .where(chatroom.starter.id.eq(starterId).and(chatroom.participant.id.eq(participantId)))
                        .fetchOne());
    }

}
