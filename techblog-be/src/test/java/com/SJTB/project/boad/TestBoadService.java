package com.SJTB.project.boad;

import com.SJTB.framework.config.FrameConstants;
import com.SJTB.framework.data.ResultVo;
import com.SJTB.framework.utils.FrameHttpUtil;
import com.SJTB.framework.utils.FrameStringUtil;
import com.SJTB.project.img.ImgFileEntity;
import com.SJTB.project.img.ImgFileRepository;
import com.SJTB.project.md.MdService;
import com.SJTB.project.user.UserEntity;
import com.SJTB.project.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SpringBootTest
//@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestBoadService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoadRepository boadRepository;

    @Autowired
    private BoadContRepository boadContRepository;

    @Autowired
    private ImgFileRepository imgFileRepository;

    @Autowired
    MdService mdService;


    @Test
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)  //trasaction rollback (테스트 내용을 DB에서 롤백한다)
    public void 게시글Insert(){

        BoadRequestDto reqBoad = new BoadRequestDto();
        reqBoad.setUserid("dade@naver.com");
        reqBoad.setTitle("정보 기술 서술 자료");

        reqBoad.setBoadConts("        String question = \"# 정보 기술 (IT)\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"정보 기술(IT)은 컴퓨터, 소프트웨어, 네트워크 및 기타 전자 장치를 사용하여 데이터를 저장, 처리 및 전송하는 기술입니다. IT는 다양한 분야에서 중요한 역할을 하며, 비즈니스, 교육, 의료 등 여러 산업에서 사용됩니다.\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"## 주요 구성 요소\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"1. **하드웨어**\\n\" +\n" +
                "                \"   - 컴퓨터, 서버, 네트워크 장비 등 물리적 장치.\\n\" +\n" +
                "                \"  \\n\" +\n" +
                "                \"2. **소프트웨어**\\n\" +\n" +
                "                \"   - 운영 체제, 애플리케이션 소프트웨어 및 시스템 소프트웨어.\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"3. **네트워킹**\\n\" +\n" +
                "                \"   - 데이터 통신 및 인터넷 연결을 위한 기술.\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"4. **데이터베이스**\\n\" +\n" +
                "                \"   - 데이터를 저장, 관리 및 검색하기 위한 시스템.\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"## IT의 중요성\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- **업무 효율성 향상:** IT는 프로세스를 자동화하고 효율성을 높입니다.\\n\" +\n" +
                "                \"- **정보 접근성:** 필요한 정보를 신속하게 검색하고 공유할 수 있습니다.\\n\" +\n" +
                "                \"- **의사 결정 지원:** 데이터 분석을 통해 보다 나은 의사 결정을 할 수 있습니다.\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"## 미래의 IT\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- 인공지능(AI)과 머신러닝\\n\" +\n" +
                "                \"- 클라우드 컴퓨팅\\n\" +\n" +
                "                \"- 사이버 보안\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"IT는 계속해서 발전하고 있으며, 이러한 기술들은 미래의 업무 방식과 생활 방식을 혁신적으로 변화시킬 것입니다.\";");

        ResultVo result = new ResultVo();
        try {
            UserEntity user = userRepository.findById(reqBoad.getUserid());
            ImgFileEntity imgFileEntity = null;
            if(!FrameStringUtil.isNull(reqBoad.getThumbnailid())) {
                imgFileEntity = imgFileRepository.getById(reqBoad.getThumbnailid());
            } else {
                imgFileEntity = imgFileRepository.getById(1);
            }
            BoadEntity boad = BoadEntity.builder()
                    .user(user)
                    .title(reqBoad.getTitle())
                    .thumbnail(imgFileEntity)
                    .openStatus("1")
                    .summary("테스트 요약내용입니다.")
                    .useYN("Y")
                    .firsRegDt(LocalDateTime.now())
                    .firsRegId(reqBoad.getUserid())
                    .firsRegIp("127.0.0.1")
                    .build();

            BoadEntity boadResult = boadRepository.save(boad);

            mdService.makeMDFile(boadResult, reqBoad.getBoadConts());
            result.setContent("게시물 저장 성공");
        } catch( Exception e) {
            e.printStackTrace();
            result.setIsError(true);
            result.setErrorMsg("Error At Boad Insert");
        } finally {

        }
    }
}
