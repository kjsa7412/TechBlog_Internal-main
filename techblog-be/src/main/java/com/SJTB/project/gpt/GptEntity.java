package com.SJTB.project.gpt;

import com.SJTB.project.base.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor
@Table(name = "sy_ai_work_Hist")
public class GptEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_sy_ai_work_hist_pk")
    @SequenceGenerator(name = "seq_sy_ai_work_hist_pk", sequenceName = "seq_sy_ai_work_hist_pk", allocationSize = 1)
    @Column(name = "workid")
    private Integer workid;

    @Column(name = "userid")
        private String userid;

    @Column(name = "workcate", length = 10)
    private String workcate;

    @Column(name = "requestcont", length = 5000)
    private String requestcont;

    @Column(name = "responsecont", length = 5000)
    private String responsecont;

    @Column(name = "requesttoken")
    private int requesttoken;

    @Column(name = "responsetoken")
    private int responsetoken;

    @Column(name = "responsecode", length = 10)
    private String responsecode;

    @Builder
    public GptEntity(int workid, String userid, String workcate, String requestcont, String responsecont, int requesttoken, int responsetoken, String responsecode,
                    String firsRegId, String firsRegIp, LocalDateTime firsRegDt, String finaRegId, String finaRegIp, LocalDateTime finaRegDt){
        super(firsRegId, firsRegIp, firsRegDt, finaRegId, finaRegIp, finaRegDt);

        this.workid = workid;
        this.userid = userid;
        this.workcate = workcate;
        this.requestcont = requestcont;
        this.requesttoken = requesttoken;
        this.responsetoken = responsetoken;
        this.responsecont = responsecont;
        this.responsecode = responsecode;

    }

}
