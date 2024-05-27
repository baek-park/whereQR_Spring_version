package whereQR.project.domain.comment;

import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import whereQR.project.domain.dashboard.Dashboard;
import whereQR.project.domain.member.Member;
import whereQR.project.utils.EntityBase;

import javax.persistence.*;

@Entity
@Getter
@AttributeOverride(name = "id", column = @Column(name = "comment_id", columnDefinition = "BINARY(16)"))
@SQLDelete(sql = "UPDATE member SET deleted = true WHERE id=?") // soft delete
@Where(clause = "deleted=false")
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
    public Boolean isAuthor(Member member){
        if(author.getId().equals(member.getId())){
            return true;
        }else{
            return false;
        }
    }
    public void delete() {
        this.status = CommentStatus.DELETED;
    }

    public void update(String content) {
        this.content = content;
        this.status = CommentStatus.UPDATED;
    }

    public enum CommentStatus {
        ACTIVE,
        DELETED,
        UPDATED
    }
}
