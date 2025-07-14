package com.SJTB.project.md;

import com.SJTB.project.base.BaseEntity;
import com.SJTB.project.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sy_file_md_info")
public class MdFileEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_sy_file_md_info_pk")
    @SequenceGenerator(name = "seq_sy_file_md_info_pk", sequenceName = "seq_sy_file_md_info_pk", allocationSize = 1)
    @Column(name = "mdid", nullable = false)
    private Integer mdid;

    @Column(name = "boadid")
    private Integer boadid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity user;

    @Column(name = "mdname", length = 300)
    private String mdname;

    @Column(name = "mdpath", length = 500)
    private String mdpath;

    @Column(name = "mdversion", length = 10)
    private String mdversion;


    @Builder
    public MdFileEntity(Integer mdid, Integer boadid, UserEntity user, String mdname, String mdpath, String mdversion,
                        String firsRegId, String firsRegIp, LocalDateTime firsRegDt,
                        String finaRegId, String finaRegIp, LocalDateTime finaRegDt){
        super(firsRegId, firsRegIp, firsRegDt, finaRegId, finaRegIp, finaRegDt);
        this.mdid = mdid;
        this.boadid = boadid;
        this.user = user;
        this.mdname = mdname;
        this.mdpath = mdpath;
        this.mdversion = mdversion;

    }

}
