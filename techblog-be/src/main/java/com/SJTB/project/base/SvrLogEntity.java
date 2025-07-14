package com.SJTB.project.base;


import com.SJTB.project.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Table(name = "sy_svr_log")
public class SvrLogEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_sy_svr_log_pk")
    @SequenceGenerator(name = "seq_sy_svr_log_pk", sequenceName = "seq_sy_svr_log_pk", allocationSize = 1)
    @Column(name = "logid", nullable = false)
    private Integer logid;

    @Column(name = "userid", nullable = false)
    private String userid;

    @Column(name = "actioncate", length = 10)
    private String actioncate;

    @Column(name = "prosuccyn", length = 1)
    private String prosuccyn;

    @Builder
    public SvrLogEntity(Integer logid, String userid, String actioncate, String prosuccyn,
                        String firsRegId, String firsRegIp, LocalDateTime firsRegDt,
                        String finaRegId, String finaRegIp, LocalDateTime finaRegDt) {
        super(firsRegId, firsRegIp, firsRegDt, finaRegId, finaRegIp, finaRegDt);
        this.logid = logid;
        this.userid = userid;
        this.actioncate = actioncate;
        this.prosuccyn = prosuccyn;

    }
}
