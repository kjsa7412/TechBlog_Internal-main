package com.SJTB.project.md;

import com.SJTB.framework.utils.FrameHttpUtil;
import com.SJTB.project.boad.BoadContEntity;
import com.SJTB.project.boad.BoadContId;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestMdService {

    @Autowired
    MdFileRepository mdFileRepository;


    @Test
    public void 마크다운파일생성(){

    }

    @Test
    @Transactional
    public void 마크다운내용읽기(){
//        String testString="내용1 내용1내용1내용1내용1내용1내용1내용1내용1내용1내용1내용1내용1내용1내용1내용1내용1내용1내용1내용1내용1\n" +
//                "\n" +
//                "내용2내용2내용2내용2내용2내용2내용2내용2내용2내용2내용2내용2내용2내용2내용2내용2\n" +
//                "\n" +
//                "# 대제목1\n" +
//                "\n" +
//                "## 대제목2\n" +
//                "\n" +
//                "***\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "* 분류1\n" +
//                "\n" +
//                "  * 분류1-1탭\n" +
//                "  * 분류1-2탭\n" +
//                "* 분류2\n" +
//                "\n" +
//                "- [ ] 체크리스트1\n" +
//                "\n" +
//                "  * [ ] 체크리스트1-1탭\n" +
//                "\n" +
//                "* [ ] 체크리스트2\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "```\n" +
//                "코드첫줄\n" +
//                "코드두줄\n" +
//                "세줄\n" +
//                "네줄네줄네줄네줄네줄네줄네줄네줄네줄네줄네줄네줄네줄네줄네줄네줄네줄네줄네줄네줄네줄네줄네줄네줄네줄\n" +
//                "```\n";

        String question = "# 정보 기술 (IT)\n" +
                "\n" +
                "정보 기술(IT)은 컴퓨터, 소프트웨어, 네트워크 및 기타 전자 장치를 사용하여 데이터를 저장, 처리 및 전송하는 기술입니다. IT는 다양한 분야에서 중요한 역할을 하며, 비즈니스, 교육, 의료 등 여러 산업에서 사용됩니다.\n" +
                "\n" +
                "## 주요 구성 요소\n" +
                "\n" +
                "1. **하드웨어**\n" +
                "   - 컴퓨터, 서버, 네트워크 장비 등 물리적 장치.\n" +
                "  \n" +
                "2. **소프트웨어**\n" +
                "   - 운영 체제, 애플리케이션 소프트웨어 및 시스템 소프트웨어.\n" +
                "\n" +
                "3. **네트워킹**\n" +
                "   - 데이터 통신 및 인터넷 연결을 위한 기술.\n" +
                "\n" +
                "4. **데이터베이스**\n" +
                "   - 데이터를 저장, 관리 및 검색하기 위한 시스템.\n" +
                "\n" +
                "## IT의 중요성\n" +
                "\n" +
                "- **업무 효율성 향상:** IT는 프로세스를 자동화하고 효율성을 높입니다.\n" +
                "- **정보 접근성:** 필요한 정보를 신속하게 검색하고 공유할 수 있습니다.\n" +
                "- **의사 결정 지원:** 데이터 분석을 통해 보다 나은 의사 결정을 할 수 있습니다.\n" +
                "\n" +
                "## 미래의 IT\n" +
                "\n" +
                "- 인공지능(AI)과 머신러닝\n" +
                "- 클라우드 컴퓨팅\n" +
                "- 사이버 보안\n" +
                "\n" +
                "IT는 계속해서 발전하고 있으며, 이러한 기술들은 미래의 업무 방식과 생활 방식을 혁신적으로 변화시킬 것입니다.";//질문 내용

        List<MarkdownElement> list = parseMarkdown(question);

        for(MarkdownElement item : list){
            log.info(item.toStringContent());
        }
//        List<BoadContEntity> boadContEntities = list.stream()
//                .map(dto -> {
//                    BoadContId newId = new BoadContId();
//                    newId.setBoadid(11);
//                    BoadContEntity boadContEntity = BoadContEntity.builder()
//                            .boadid(11)
//                            .contcate(dto.type)
//                            .cont(dto.content)
//                            .firsRegDt(LocalDateTime.now())
//                            .firsRegId("dade@naver.com")
//                            .firsRegIp("127.0.0.1")
//                            .build();
//                    return boadContEntity;
//                })
//                .collect(Collectors.toList());

//        boadContRepository.saveAll(boadContEntities);

    }

    public static List<MarkdownElement> parseMarkdown(String markdown) {
        List<MarkdownElement> sections = new ArrayList<>();
        StringBuilder codeBlock = new StringBuilder();
        boolean inCodeBlock = false;

        // Markdown을 줄 단위로 분리
        String[] lines = markdown.split("\n");
        StringBuffer sb = new StringBuffer();
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                if (line.startsWith("```")) {
                    if (inCodeBlock) {
                        // 코드 블록 종료
                        sections.add(new MarkdownElement(codeBlock.toString(), "code", 0));
                        codeBlock.setLength(0); // StringBuilder 초기화
                        inCodeBlock = false;
                    } else {
                        // 코드 블록 시작
                        inCodeBlock = true;
                    }
                } else {
                    if (inCodeBlock) {
                        codeBlock.append(line).append("\n");
                    } else {
                        MarkdownElement mde = classifyLine(line);
                        if(!mde.content.equals("")){
                            sb.append(mde.toString()).append("\n");
                        }
                    }
                }
            }
        }

        log.info("### " + sb.toString());

        return sections;
    }

    public static MarkdownElement classifyLine(String line) {
        if (line.startsWith("# ")) {
            return new MarkdownElement(line, "h1", 0);
        } else if (line.startsWith("## ")) {
            return new MarkdownElement(line, "h2", 0);
        } else if (line.startsWith("### ")) {
            return new MarkdownElement(line, "h3", 0);
        } else if (line.startsWith("#### ")) {
            return new MarkdownElement(line, "h4", 0);
        } else if (line.startsWith("##### ")) {
            return new MarkdownElement(line, "h5", 0);
        } else if (line.startsWith("###### ")) {
            return new MarkdownElement(line, "h6", 0);
        } else if (line.startsWith("***")) {
            return new MarkdownElement(line, "hr", 0);
        } else if (line.trim().startsWith("* ") || line.trim().startsWith("- ")) {
            return new MarkdownElement(line, "ul", countDepth(line));
        } else if (line.trim().matches("^\\d+\\. .*")) {
            return new MarkdownElement(line, "ol", countDepth(line));
        } else {
            return new MarkdownElement(line, "p", countDepth(line)); // 기본 문단
        }

    }

    // Depth 계산
    public static int countDepth(String line) {
        int depth = 0;
        while (line.startsWith("  ")) { // 두 개의 공백으로 깊이 증가
            depth++;
            line = line.substring(2);
        }
        return depth;
    }
}
