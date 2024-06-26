package whereQR.project.domain.message;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import whereQR.project.domain.chatroom.Chatroom;
import whereQR.project.domain.chatroom.QChatroom;
import whereQR.project.domain.member.Member;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomMessageRepositoryImpl implements CustomMessageRepository{
    private final JPAQueryFactory queryFactory;
    private final QMessage message = QMessage.message;

    @Override
    public List<Message> findNotReadMessageByChatroomAndReceiver(Chatroom chatroom, Member receiver) {

        QChatroom qChatroom = QChatroom.chatroom;
        return queryFactory.selectFrom(message)
                .leftJoin(message.chatRoom,qChatroom)
                .where(message.chatRoom.eq(chatroom).and( message.receiver.eq(receiver)).and(message.isRead.eq(false)))
                .fetchAll().stream().collect(Collectors.toList());
    }

    @Override
    public List<Message> findMessagesByChatroomId(UUID chatroomId) {
        QChatroom chatroom = QChatroom.chatroom;

        return queryFactory.selectFrom(message)
                .leftJoin(message.chatRoom, chatroom)
                .where(message.chatRoom.id.eq(chatroomId))
                .orderBy(message.createdAt.asc())
                .fetchAll().stream().collect(Collectors.toList());
    }

    @Override
    public void updateReadByMessageIds(List<UUID> messageIds) {

        Long result = queryFactory.update(message)
                .set(message.isRead, Boolean.TRUE)
                .where(message.id.in(messageIds))
                .execute();
    }


}
