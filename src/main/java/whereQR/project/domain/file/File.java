package whereQR.project.domain.file;

import lombok.Getter;
import whereQR.project.domain.dashboard.Dashboard;
import whereQR.project.domain.member.Member;
import whereQR.project.utils.EntityBase;

import javax.persistence.*;

@Entity
@Getter
@AttributeOverride(name = "id", column = @Column(name = "file_id", columnDefinition = "BINARY(16)"))
public class File extends EntityBase {

    @Column(name = "url", columnDefinition = "TEXT", nullable =false)
    String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dashboard_id", nullable = true)
    private Dashboard dashboard = null;

    @OneToOne
    @JoinColumn(name = "profile_id",referencedColumnName = "id", nullable = true)
    private Member profile = null;

    @OneToOne
    @JoinColumn(name = "uploader_id", referencedColumnName = "id", nullable = false)
    private Member uploader;

    public File(String url, Member member) {
        this.url = url;
        this.uploader = member;
    }

    public File() {

    }
}
