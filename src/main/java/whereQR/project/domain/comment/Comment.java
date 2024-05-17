package whereQR.project.domain.comment;

import lombok.Getter;
import whereQR.project.domain.dashboard.Dashboard;
import whereQR.project.domain.member.Member;
import whereQR.project.utils.EntityBase;

import javax.persistence.*;

@Entity
@Getter
@AttributeOverride(name = "id", column = @Column(name = "comment_id", columnDefinition = "BINARY(16)"))
public class Comment extends EntityBase {

    @Column(nullable = false, length = 1000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Member author;

    @ManyToOne
    @JoinColumn(name = "dashboard_id", referencedColumnName = "dashboard_id")
    private Dashboard dashboard;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "comment_id")
    private Comment parent;

    @Enumerated(EnumType.STRING)
    private CommentStatus status = CommentStatus.ACTIVE;


    public Comment() {
        super();
    }

    public Comment(String content, Member author, Dashboard dashboard, Comment parent) {
        this.content = content;
        this.author = author;
        this.dashboard = dashboard;
        this.parent = parent;
    }
    public enum CommentStatus {
        ACTIVE,
        DELETED,
        UPDATED
    }
}
