package com.SJTB.project.log;

import com.SJTB.framework.utils.FrameDateUtil;
import com.SJTB.project.base.ClntLogEntity;
import com.SJTB.project.base.ClntLogRepository;
import com.SJTB.project.base.SvrLogEntity;
import com.SJTB.project.base.SvrLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestLogService {

    @Autowired
    ClntLogRepository clntLogRepository;

    @Autowired
    SvrLogRepository svrLogRepository;



    @Test
    @Transactional
    public void 클라이언트로그Insert(){
        ClntLogEntity clnt = ClntLogEntity.builder()
                            .contid(11)
                            .userid("dade1075@naver.com")
                            .actioncate("SRLS")
                            .firsRegDt(LocalDateTime.now())
                            .firsRegIp("10.10.20.248")
                            .firsRegId("test@test.com")
                            .build();

        clntLogRepository.save(clnt);

        boolean existsId = clntLogRepository.existsById(clnt.getLogid());

        assertThat(existsId).isTrue();
    }

    @Test
    @Transactional
    public void 서버로그Insert(){
        SvrLogEntity svr = SvrLogEntity.builder()
                            .actioncate("AISP")
                            .prosuccyn("Y")
                            .userid("SYSTEM")
                            .firsRegDt(FrameDateUtil.getLocalDateTime())
                            .firsRegId("SYSTEM")
                            .firsRegIp("0.0.0.0")
                            .build();

        svrLogRepository.save(svr);

        boolean existsId = svrLogRepository.existsById(svr.getLogid());

        assertThat(existsId).isTrue();
    }
}
