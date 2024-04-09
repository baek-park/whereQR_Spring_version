package whereQR.project.domain.favorite;

import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import whereQR.project.domain.dashboard.Dashboard;
import whereQR.project.domain.member.Member;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
public class Favorite {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dashboard_id")
    private Dashboard dashboard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Favorite() {}

    public Favorite(Dashboard dashboard, Member member) {
        this.dashboard = dashboard;
        this.member = member;
    }
}
