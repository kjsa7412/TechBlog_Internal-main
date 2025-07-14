package com.SJTB.project.base;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass // @Entity 대신 사용(공통 범위)
public abstract class BaseEntity {

    @Column(name = "firsregid", updatable = false, length = 50)
    private String firsRegId;

    @Column(name = "firsregip", updatable = false, length = 50)
    private String firsRegIp;

    @Column(name = "firsregdt", updatable = false)
    private LocalDateTime firsRegDt;

    @Column(name = "finaregid", length = 50)
    private String finaRegId;

    @Column(name = "finaregip", length = 50)
    private String finaRegIp;

    @Column(name = "finaregdt")
    private LocalDateTime finaRegDt;

    // protected로 선언하여 자식 클래스만 사용하게끔 구현
    protected BaseEntity(String firsRegId, String firsRegIp, LocalDateTime firsRegDt, String finaRegId, String finaRegIp, LocalDateTime finaRegDt) {
        this.firsRegId = firsRegId;
        this.firsRegIp = firsRegIp;
        this.firsRegDt = firsRegDt;
        this.finaRegId = finaRegId;
        this.finaRegIp = finaRegIp;
        this.finaRegDt = finaRegDt;
    }
}