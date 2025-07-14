package com.SJTB.project.img;

import com.SJTB.project.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sy_file_img_info")
public class ImgFileEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_sy_file_img_info_pk")
    @SequenceGenerator(name = "seq_sy_file_img_info_pk", sequenceName = "seq_sy_file_img_info_pk", allocationSize = 1)
    @Column(name = "imgid", nullable = false)
    private Integer imgid;

    @Column(name = "imgcate", nullable = false)
    private String imgcate;

    @Column(name = "imgcateid")
    private Integer imgcateid;

    @Column(name = "imgname", nullable = false)
    private String imgname;

    @Column(name = "imghashname", nullable = false)
    private String imghashname;

    @Column(name = "imgpath")
    private String imgpath;

    @Column(name = "useyn")
    private String useyn;


    @Builder
    public ImgFileEntity(Integer imgid, String imgcate, Integer imgcateid, String imgname, String imghashname, String imgpath, String useyn,
            String firsRegId, String firsRegIp, LocalDateTime firsRegDt, String finaRegId, String finaRegIp, LocalDateTime finaRegDt){
        super(firsRegId, firsRegIp, firsRegDt, finaRegId, finaRegIp, finaRegDt);
        this.imgid = imgid;
        this.imgcate = imgcate;
        this.imgcateid = imgcateid;
        this.imgname = imgname;
        this.imghashname = imghashname;
        this.imgpath = imgpath;
        this.useyn = useyn;
    }

}
