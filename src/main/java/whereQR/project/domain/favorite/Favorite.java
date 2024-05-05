package whereQR.project.domain.favorite;

import lombok.Getter;
import whereQR.project.domain.dashboard.Dashboard;
import whereQR.project.domain.member.Member;
import whereQR.project.utils.EntityBase;

import javax.persistence.*;

@Entity
@Getter
@AttributeOverride(name = "id", column = @Column(name = "like_id", columnDefinition = "BINARY(16)"))
public class Favorite extends EntityBase {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dashboard_id")
    private Dashboard dashboard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Favorite() {
        super();
    }

    public Favorite(Dashboard dashboard, Member member) {
        super();
        this.dashboard = dashboard;
        this.member = member;
    }
}