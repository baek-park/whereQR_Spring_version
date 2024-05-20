package whereQR.project.domain.file;

import lombok.Getter;
import whereQR.project.domain.dashboard.Dashboard;
import whereQR.project.domain.file.dto.FileResponseDto;
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
    @JoinColumn(name = "profile_file_id",referencedColumnName = "id", unique = true)
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

    public void updateProfile(Member member){
        this.profile = member;
    }

    public void updateDashboard(Dashboard dashboard){
        this.dashboard = dashboard;
    }

    public FileResponseDto toFileResponseDto(){
        return new FileResponseDto(this.id, this.url);
    }
}
