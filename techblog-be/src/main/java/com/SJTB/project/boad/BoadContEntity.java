package com.SJTB.project.boad;


import com.SJTB.project.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "boad_cont_info")
public class BoadContEntity extends BaseEntity {



    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_boad_cont_info_pk")
    @SequenceGenerator(name = "seq_boad_cont_info_pk", sequenceName = "seq_boad_cont_info_pk", allocationSize = 1)
    @Column(name = "contid", nullable = false)
    private Integer contid;

    @Column(name = "boadid", nullable = false)
    private Integer boadid;

    @Column(name = "contcate", length = 5)
    private String contcate;

    @Column(name = "cont", length = 5000)
    private String cont;

    @ManyToOne
    @JoinColumn(name = "boadid", insertable = false, updatable = false)
    private BoadEntity boad;


    @Builder
    public BoadContEntity(Integer contid, Integer boadid,  String contcate, String cont, String firsRegId, BoadEntity boad, String firsRegIp, LocalDateTime firsRegDt,
                          String finaRegId, String finaRegIp, LocalDateTime finaRegDt){
        super(firsRegId, firsRegIp, firsRegDt, finaRegId, finaRegIp, finaRegDt);
        this.contid = contid;
        this.boadid = boadid;
        this.contcate = contcate;
        this.cont = cont;
        this.boad = boad;

    }

}
