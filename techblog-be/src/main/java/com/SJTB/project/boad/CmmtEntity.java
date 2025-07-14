package com.SJTB.project.boad;

import com.SJTB.project.base.BaseEntity;
import com.SJTB.project.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "boad_cmt_info")
public class CmmtEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_boad_cmt_info_pk")
    @SequenceGenerator(name = "seq_boad_cmt_info_pk", sequenceName = "seq_boad_cmt_info_pk", allocationSize = 1)
    @Column(name = "cmtid", nullable = false)
    private Integer cmtid;

    @Column(name = "boadid", nullable = false)
    private Integer boadid;

//    @Column(name = "userid", nullable = false)
//    private String userid;

    @Column(name = "cmt", nullable = false)
    private String cmt;

    @Column(name = "useyn", nullable = false)
    private String useyn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserEntity user;

    @Builder
    public CmmtEntity(Integer cmtid, Integer boadid, /*String userid,*/ String cmt, String useyn, UserEntity user,
                      String firsRegId, String firsRegIp, LocalDateTime firsRegDt,String finaRegId, String finaRegIp, LocalDateTime finaRegDt)     {
        super(firsRegId, firsRegIp, firsRegDt, finaRegId, finaRegIp, finaRegDt);
        this.cmtid = cmtid;
        this.boadid = boadid;
//        this.userid = userid;
        this.user = user;
        this.cmt = cmt;
        this.useyn = useyn;


    }
}
