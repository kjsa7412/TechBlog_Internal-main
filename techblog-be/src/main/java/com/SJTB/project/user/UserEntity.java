package com.SJTB.project.user;

import com.SJTB.project.base.BaseEntity;
import com.SJTB.project.boad.BoadEntity;
import com.SJTB.project.img.ImgFileEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sy_usr_info")
public class UserEntity extends BaseEntity{
    @Id
    @Column(name = "userid", length = 100)
    private String userId;

    @Column(name = "passwd", length = 250)
    private String passWd;

    @Column(name = "username", length = 10)
    private String userName;

    @Column(name = "profilecont", length = 500)
    private String profileCont;

//    @Column(name = "profilepic", length = 250)
//    private String profilePic;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profilepic", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ImgFileEntity profilePic;

    @Column(name = "useyn", length = 1)
    private String useYN;

    @Column(name = "logintoken", length = 500)
    private String loginToken;

    @Column(name = "lastlogindt")
    private LocalDateTime lastLoginDt;

    @Column(name = "userauth", length = 50)
    private String userAuth;

    // 필요 시 사용
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "user",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private List<BoadEntity> boads;

    @Builder
    public UserEntity(String userId, String passWd, String userName, String profileCont,
                      ImgFileEntity profilePic, String useYN, String loginToken, LocalDateTime lastLoginDt, String userAuth,List<BoadEntity> boads,
                      String firsRegId, String firsRegIp, LocalDateTime firsRegDt, String finaRegId, String finaRegIp, LocalDateTime finaRegDt) {
        super(firsRegId, firsRegIp, firsRegDt, finaRegId, finaRegIp, finaRegDt);
        this.userId = userId;
        this.passWd = passWd;
        this.userName = userName;
        this.profileCont = profileCont;
        this.profilePic = profilePic;
        this.useYN = useYN;
        this.loginToken = loginToken;
        this.lastLoginDt = lastLoginDt;
        this.userAuth = userAuth;
        this.boads = boads;
    }
}
