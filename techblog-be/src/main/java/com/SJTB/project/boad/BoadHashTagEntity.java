package com.SJTB.project.boad;


import com.SJTB.project.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "boad_hash_info")
public class BoadHashTagEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_boad_hash_info_pk")
    @SequenceGenerator(name = "seq_boad_hash_info_pk", sequenceName = "seq_boad_hash_info_pk", allocationSize = 1)
    @Column(name = "hashid", nullable = false)
    private Integer hashid;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boadid", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private BoadEntity boad;

    @Column(name = "hashtag")
    private String hashtag;



    @Builder
    public BoadHashTagEntity (Integer hashid, BoadEntity boad, String hashtag,
                              String firsRegId, String firsRegIp, LocalDateTime firsRegDt,
                              String finaRegId, String finaRegIp, LocalDateTime finaRegDt ){
        super(firsRegId, firsRegIp, firsRegDt, finaRegId, finaRegIp, finaRegDt);

        this.hashid = hashid;
        this.boad = boad;
        this.hashtag = hashtag;
    }

}
