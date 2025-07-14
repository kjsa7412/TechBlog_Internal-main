package com.SJTB.project.boad;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class BoadContId implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_boad_cont_info_pk")
    @SequenceGenerator(name = "seq_boad_cont_info_pk", sequenceName = "seq_boad_cont_info_pk", allocationSize = 1)
    @Column(name = "contid", nullable = false)
    private Integer contid;

    @Column(name = "boadid", nullable = false)
    private Integer boadid;
}
