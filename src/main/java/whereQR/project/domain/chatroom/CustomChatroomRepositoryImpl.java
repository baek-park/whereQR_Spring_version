package whereQR.project.domain.chatroom;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import whereQR.project.domain.member.Member;
import whereQR.project.domain.chatroom.dto.ChatroomProjectionDto;
import whereQR.project.domain.member.QMember;
import whereQR.project.domain.message.QMessage;

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
    public List<ChatroomProjectionDto> findChatroomsByMember(Member user) {
        QMember member = QMember.member;
        QMessage message = QMessage.message;

        return queryFactory.select(
                        Projections.constructor(
                                ChatroomProjectionDto.class,
                                chatroom.id,
                                chatroom.starter,
                                chatroom.participant,
                                ExpressionUtils.as(
                                        JPAExpressions.select(message.count())
                                                .from(message)
                                                .where(message.chatRoom.id.eq(chatroom.id), message.isRead.isFalse(), message.receiver.eq(member)),
                                        "notReadMessageCount"),
                                ExpressionUtils.as(
                                        JPAExpressions.select(message.updatedAt.max())
                                                .from(message)
                                                .where(message.chatRoom.id.eq(chatroom.id)),
                                        "lastDate"),
                                ExpressionUtils.as(
                                        JPAExpressions.select(message.content)
                                                .from(message)
                                                .where(message.chatRoom.id.eq(chatroom.id),
                                                        message.updatedAt.eq(
                                                            JPAExpressions
                                                                    .select(message.updatedAt.max())
                                                                    .from(message)
                                                                    .where(message.chatRoom.id.eq(chatroom.id)))),
                                        "lastContent"))
                )
                .from(chatroom)
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
