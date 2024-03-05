package whereQR.project.entity;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
public class Dashboard {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID dashboardId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 5500)
    private String content;

    @Column(nullable = false, length = 20)
    private String lostedType;

    @Column(nullable = false, length = 20)
    private String lostedCity;

    @Column(length = 20)
    private String lostedDistrict;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Member author;

    // 기본 생성자
    public Dashboard() {
    }

    // 모든 필드를 포함한 생성자(생성 시간과 수정 시간 제외)
    public Dashboard(String title, String content, String lostedType, String lostedCity, String lostedDistrict, Member author) {
        this.dashboardId = UUID.randomUUID();
        this.title = title;
        this.content = content;
        this.lostedType = lostedType;
        this.lostedCity = lostedCity;
        this.lostedDistrict = lostedDistrict;
        this.author = author;
    }

    public void update(String title, String content, String lostedType, String lostedCity, String lostedDistrict) {
        this.title = title;
        this.content = content;
        this.lostedType = lostedType;
        this.lostedCity = lostedCity;
        this.lostedDistrict = lostedDistrict;
        // updatedAt은 @UpdateTimestamp 어노테이션이 자동으로 처리
    }

}
