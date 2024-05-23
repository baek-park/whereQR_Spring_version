package whereQR.project.domain.dashboard;

import lombok.Getter;
import whereQR.project.domain.comment.dto.CommentInfoDto;
import whereQR.project.domain.dashboard.dto.DashboardDetailResponseDto;
import whereQR.project.domain.dashboard.dto.DashboardResponseDto;
import whereQR.project.domain.file.File;
import whereQR.project.utils.EntityBase;
import whereQR.project.domain.member.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter
@AttributeOverride(name = "id", column = @Column(name = "dashboard_id", columnDefinition = "BINARY(16)"))
public class Dashboard extends EntityBase { // EntityBase 상속

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

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Member author;

    @OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> images = new ArrayList<>();

    // 기본 생성자
    public Dashboard() {
        super(); // EntityBase의 생성자 호출
    }

    // 모든 필드를 포함한 생성자(생성 시간과 수정 시간 제외)
    public Dashboard(String title, String content, String lostedType, String lostedCity, String lostedDistrict, Member author) {
        super(); // EntityBase의 생성자 호출
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
        // updatedAt은 EntityBase에 정의된 @LastModifiedDate 어노테이션이 자동으로 처리
    }

    public void addImage(File image){
        this.images.add(image);
        image.updateDashboard(this);
    }

    public void deleteImage(){
        this.images.clear();
    }

    public Boolean isAuthor(Member member){
        if(author.getId().equals(member.getId())){
            return true;
        }else{
            return false;
        }
    }

    public DashboardResponseDto toDashboardResponseDto(){
        return new DashboardResponseDto(
                this.id,
                this.title,
                this.content,
                this.author.getId().toString(),
                this.author.getUsername(),
                this.lostedCity,
                this.lostedDistrict,
                this.lostedType,
                this.images.stream().map(File::toFileResponseDto).collect(Collectors.toList()),
                this.createdAt
                );
    }

    public DashboardDetailResponseDto toDashboardDetailResponseDto(boolean isFavorite, long favoriteCount, List<CommentInfoDto> comments){
        return new DashboardDetailResponseDto(
                this.id,
                this.title,
                this.content,
                this.author.getId().toString(),
                this.author.getUsername(),
                this.lostedCity,
                this.lostedDistrict,
                this.lostedType,
                isFavorite,
                favoriteCount,
                comments,
                this.images.stream().map(File::toFileResponseDto).collect(Collectors.toList()),
                this.createdAt
        );
    }
}
