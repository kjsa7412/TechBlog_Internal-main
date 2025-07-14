package com.SJTB.project.gpt;

import com.SJTB.framework.data.ResultVo;
import com.SJTB.framework.config.FrameConstants;
import com.SJTB.framework.utils.FrameDateUtil;
import com.SJTB.framework.utils.FrameHttpUtil;
import com.SJTB.project.base.BaseService;
import com.SJTB.project.base.ClntLogRepository;
import com.SJTB.project.base.SvrLogRepository;
import com.SJTB.project.boad.*;
import com.SJTB.project.boad.QBoadEntity;
import com.SJTB.project.md.MdFileEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;

import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Slf4j
@Service
public class GptService extends BaseService {

    private final GptRepository gptRepository;
    private final BoadContRepository boadContRepository;
    private final BoadHashTagRepository boadHashTagRepository;
    private final JPAQueryFactory queryFactory;

    @Autowired
    public GptService(ClntLogRepository clntLogRepository, BoadContRepository boadContRepository, JPAQueryFactory queryFactory,
                      SvrLogRepository svrLogRepository, GptRepository gptRepository, BoadHashTagRepository boadHashTagRepository) {
        super(clntLogRepository, svrLogRepository);
        this.queryFactory = queryFactory;
        this.gptRepository = gptRepository;
        this.boadContRepository = boadContRepository;
        this.boadHashTagRepository = boadHashTagRepository;
    }

    /*
    * ChatGPT API 요청시 role에 따른 대화내용 입력차이 있음
    * role-user : 일반적으로 대화의 참가자, 즉 질문을 하고 정보를 요청하는 사람
    * role-system : 대화의 컨텍스트나 지침을 설정하는 역할을 합니다.
    * role-assistant : ChatGPT가 하는 답변
    * role system으로 설정하여 어떤식으로 대화할지 주제는 잡아줌
    * {"role": "system", "content": "질문에 대해서 전체적인 문법적 오류나 오탈자들 글을 교정해주는 역활을 해주는거야."},
    * {"role": "user", "content": "아래줄부터 나오는 전체 문단에 대해서 틀린내용이없는지 확인해줘 그리고 어떠한 키워드가 중요한지도 뽑아줘"},
    * {"role": "assistant", "content": "요청해주신 글의 분석내용은 ~~~ 중요내용을 요약하면 ~~~~"}
    * */


    @Transactional
    //질의응답 매뉴얼
    public ResultVo<GptResponseDto> askGPT(HttpServletRequest request, GptRequestDto gptRequestDto) {
        String userIp = FrameHttpUtil.getUserIp(request);
        return processGPTAPI(FrameConstants.CHATAPI_CHAT, gptRequestDto.getUserid(), gptRequestDto.getQuestion(),  userIp);
    }


    /*@Async 어노테이션을 사용하여 메서드를 비동기적으로 실행*/
    @Transactional
    public void summaryBoadCont(MdFileEntity resultMd, BoadEntity boad, String boadConts){
/*        try {
            Thread.sleep(5000);
        } catch( Exception e){
            e.printStackTrace();
        }*/
//        List<BoadContEntity> boadContList = boadContRepository.findAllBoadContByBoadJoin(resultMd.getBoadid());
//
//        List<BoadContResponseDto> boadList = boadContList.stream()
//                .map(data -> BoadContResponseDto.builder()
//                        .contid(data.getContid())
//                        .boadid(data.getBoadid())
//                        .contcate(data.getContcate())
//                        .cont(data.getCont())
//                        .build())
//                .collect(Collectors.toList());
//        String summaryContent = "";
//        for(BoadContResponseDto item: boadList){
//            if(!item.getContcate().equals("IMAG") && !item.getContcate().equals("BRKL")){
//                summaryContent +=item.getCont();
//            }
//        }

        ResultVo<GptResponseDto> result = processGPTAPI(FrameConstants.CHATAPI_SUMMARY, "SYSTEM", boadConts, "0.0.0.0" );
        QBoadEntity qBoadEntity = QBoadEntity.boadEntity;
        String summaryAnswer = result.getContent().getAnswer();
        String hashTagList = "";
        if (summaryAnswer.contains("||")) {
            String[] answerList = summaryAnswer.split("\\|\\|");
            summaryAnswer = answerList[0];
            hashTagList = answerList[1];
        } else {
            hashTagList = "";
        }

        long updateRowCnt = queryFactory.update(qBoadEntity)
                .where(qBoadEntity.boadId.eq(resultMd.getBoadid()))
//                .set(qBoadEntity.summary, result.getContent().getAnswer())
                .set(qBoadEntity.summary, summaryAnswer)
                .set(qBoadEntity.finaRegDt, LocalDateTime.now())
                .set(qBoadEntity.finaRegIp, "0.0.0.0")
                .set(qBoadEntity.finaRegId, "SYSTEM")
                .execute();

        List<BoadHashTagEntity> boadHashTagEntities =
                Arrays.stream(hashTagList.replaceAll("#","").split(" "))
                        .map(item -> {
                            BoadHashTagEntity itemEntity = BoadHashTagEntity.builder()
                                    .boad(boad)
                                    .hashtag(item)
                                    .firsRegDt(FrameDateUtil.getLocalDateTime())
                                    .firsRegId(boad.getUser().getUserId())
//                                    .firsRegIp()
                                    .build();
                            return itemEntity;
                        })
                        .collect(Collectors.toList());

        boadHashTagRepository.saveAll(boadHashTagEntities);


        try {
            Path mdPath = Paths.get(resultMd.getMdpath() + "/" + resultMd.getMdname());
            BufferedReader reader = Files.newBufferedReader(mdPath);
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("description")) {
                    line = "description: " + summaryAnswer;
                }
                content.append(line).append(System.lineSeparator());
            }
            reader.close();

            // 파일 쓰기
            BufferedWriter writer = Files.newBufferedWriter(mdPath);
            writer.write(content.toString());
            writer.close();
        } catch( Exception e){

        }

        log.info("파일이 성공적으로 수정되었습니다.");
        log.info("########## update Row Count : " + updateRowCnt);
    }

    @Transactional
    public ResultVo<GptResponseDto> processGPTAPI(String jobCate, String userId, String cont, String userIp) {
        ResultVo<GptResponseDto> resultVO = new ResultVo<>(GptResponseDto.class);
        GptResponseDto result = new GptResponseDto();
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        String processYN = "N";
        String resultCont = "";
        String systemSetMsg = "";
        int maxToken = 0;

        // 프롬프트 설정 및 토큰 수 설정
        if (jobCate.equals(FrameConstants.CHATAPI_CHAT)) {
            systemSetMsg = "마크다운 게시물을 작성하는데 도움이 되는 내용으로 답변을 구성해줘";
            maxToken = FrameConstants.CHAT_MAX_TOKEN_VALUE;
        } else if (jobCate.equals(FrameConstants.CHATAPI_SUMMARY)) {
            systemSetMsg =
                    "질문으로 들어오는 내용에 대해서 비즈니스 문법으로 요약해줘. " +
                            "그리고 해당 내용에 대한 해시태그를 최대 10개까지 만들어줘. " +
                            "해시태그는 구분자로 쉼표를 사용하고 요약내용과 해시태그 목록은 '||'를 이용해서 앞에는 요약내용, 뒤에는 해시태그 목록을 나눠서 출력해줘";
            maxToken = FrameConstants.SUMMARY_MAX_TOKEN_VALUE;
        }

        try {
            // 헤더 구성 (Authorization, Content-Type)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + FrameConstants.CHATAPI_KEY);

            // 메시지 구성: system 메시지 + user 질문
            List<Map<String, String>> messages = new ArrayList<>();

            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", systemSetMsg);
            messages.add(systemMessage);

            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", cont);
            messages.add(userMessage);

            result.setQuestion(cont);

            // API 요청 본문 구성
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", maxToken);
            requestBody.put("model", FrameConstants.GPT_MODEL);
            if (jobCate.equals(FrameConstants.CHATAPI_CHAT)) {
                requestBody.put("temperature", FrameConstants.CHAT_TEMP);
            } else if (jobCate.equals(FrameConstants.CHATAPI_SUMMARY)) {
                requestBody.put("temperature", FrameConstants.SUMMRARY_TEMP);
            }

            // HTTP 요청 전송
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    FrameConstants.CHATAPI_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // 응답 처리
            result.setResponsecode(response.getStatusCode().toString());

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("############Response: " + response.getBody());

                Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);

                // 응답 메시지 추출
                if (responseBody.containsKey("choices")) {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                    if (choices != null && !choices.isEmpty() && choices.get(0).containsKey("message")) {
                        Map<String, Object> messageResponse = (Map<String, Object>) choices.get(0).get("message");
                        if (messageResponse.containsKey("content")) {
                            result.setAnswer((String) messageResponse.get("content"));
                        }
                    }
                }

                // 토큰 사용량 추출
                if (responseBody.containsKey("usage")) {
                    Map<String, Object> usage = (Map<String, Object>) responseBody.get("usage");
                    result.setRequesttoken((int) usage.get("prompt_tokens"));
                    result.setResponsetoken((int) usage.get("completion_tokens"));
                }

                processYN = "Y";
                resultCont = result.getAnswer();
            } else {
                resultCont = "ChatGPT API 요청 실패: " + response.getStatusCode();
            }
        } catch (Exception e) {
            log.error("[GPT] API 호출 중 예외 발생", e);
            resultCont = "ChatGPT API 호출 중 오류 발생";
        } finally {
            //  DB에 로그 저장
            GptEntity gptEntity = GptEntity.builder()
                    .userid(userId)
                    .workcate(FrameConstants.CHATAPI_CHAT)
                    .requestcont(cont)
                    .responsecont(result.getAnswer())
                    .requesttoken(result.getRequesttoken())
                    .responsetoken(result.getResponsetoken())
                    .responsecode(result.getResponsecode())
                    .firsRegId(userId)
                    .firsRegDt(LocalDateTime.now())
                    .firsRegIp(userIp)
                    .build();

            gptRepository.save(gptEntity);

            // 최종 결과 세팅
            result.setProsuccyn(processYN);
            resultVO.setContent(result);
            resultVO.setIsError(!processYN.equals("Y"));

            if (!processYN.equals("Y")) {
                resultVO.setWarningMsg(resultCont);
            }

            // summary 작업 시 추가 로그 저장
            if (jobCate.equals(FrameConstants.CHATAPI_SUMMARY)) {
                saveServerLog(userId, jobCate, processYN);
            }
        }

        return resultVO;
    }
}
