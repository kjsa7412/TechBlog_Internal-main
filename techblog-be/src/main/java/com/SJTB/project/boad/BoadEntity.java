package com.SJTB.project.boad;

import com.SJTB.project.base.BaseEntity;
import com.SJTB.project.img.ImgFileEntity;
import com.SJTB.project.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "boad_mst_info")
public class BoadEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_boad_mst_info_pk")
    @SequenceGenerator(name = "seq_boad_mst_info_pk", sequenceName = "seq_boad_mst_info_pk", allocationSize = 1)
    @Column(name = "boadid", nullable = false)
    private Integer boadId;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "summary", length = 2000)
    private String summary;

    @Column(name = "useyn", length = 1)
    private String useYN;

    @Column(name = "openstatus", length = 8)
    private String openStatus;

//    @Column(name = "thumbnail", length = 500)
//    private String thumbnail;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumbnailid", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ImgFileEntity thumbnail;

    @Column(name = "views")
    private Integer views;

    @Column(name = "likes")
    private Integer likes;

    @Column(name = "lastmd")
    private Integer lastMd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity user;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashid", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private List<BoadHashTagEntity> hashtag;

    @OneToMany(mappedBy = "boad", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BoadContEntity> conts;



    @Builder
    public BoadEntity(Integer boadId, String title, String summary, String useYN, String openStatus, ImgFileEntity thumbnail,
                       Integer views, Integer likes, Integer lastMd, UserEntity user, List<BoadContEntity> conts, String firsRegId, String firsRegIp, LocalDateTime firsRegDt,
                       String finaRegId, String finaRegIp, LocalDateTime finaRegDt) {
        super(firsRegId, firsRegIp, firsRegDt, finaRegId, finaRegIp, finaRegDt);
        this.boadId = boadId;
        this.title = title;
        this.summary = summary;
        this.useYN = useYN;
        this.openStatus = openStatus;
        this.thumbnail = thumbnail;
        this.views = views;
        this.likes = likes;
        this.lastMd = lastMd;
        this.user = user;
        this.conts = conts;
    }
}
