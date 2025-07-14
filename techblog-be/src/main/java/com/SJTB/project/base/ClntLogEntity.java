package com.SJTB.project.base;

import com.SJTB.project.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Table(name = "sy_clnt_log")
public class ClntLogEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_sy_clnt_log_pk")
    @SequenceGenerator(name = "seq_sy_clnt_log_pk", sequenceName = "seq_sy_clnt_log_pk", allocationSize = 1)
    @Column(name = "logid", nullable = false)
    private Integer logid;

    @Column(name = "userid", nullable = false)
    private String userid;

    @Column(name = "actioncate", length = 10)
    private String actioncate;

    @Column(name = "contid")
    private int contid;

    @Column(name = "useragent", length = 500)
    private String useragent;

    @Builder
    public ClntLogEntity(Integer logid, String userid, String actioncate, Integer contid, String useragent,
                         String firsRegId, String firsRegIp, LocalDateTime firsRegDt,
                         String finaRegId, String finaRegIp, LocalDateTime finaRegDt){

        super(firsRegId, firsRegIp, firsRegDt, finaRegId, finaRegIp, finaRegDt);
        this.logid = logid;
        this.userid = userid;
        this.actioncate = actioncate;
        this.contid = contid;
        this.useragent = useragent;
    }
}
