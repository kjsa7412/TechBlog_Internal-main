package com.SJTB.project.gpt;

import com.SJTB.framework.config.FrameConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@SpringBootTest
//@Transactional  //trasaction rollback (테스트 내용을 DB에서 롤백한다)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestGptService {

    /*
     * ChatGPT API 요청시 role에 따른 대화내용 입력차이 있음
     * role-user : 일반적으로 대화의 참가자, 즉 질문을 하고 정보를 요청하는 사람
     * role-system : 대화의 컨텍스트나 지침을 설정하는 역할을 합니다.
     * role-assistant : ChatGPT가 하는 답변
     * role system으로 설정하여 어떤식으로 대화할지 주제는 잡아줌
     * {"role": "system", "content": "질문에 대해서 문법적 오류나 오탈자 내용을 교정해주는 역활을 해주는거야."},
     * {"role": "user", "content": "아래줄부터 나오는 전체 문단에 대해서 틀린내용이없는지 확인해줘 그리고 어떠한 키워드가 중요한지도 뽑아줘"},
     * {"role": "assistant", "content": "삐빅-삐빅 너님 글수준은~~~ 3줄요약하면 ~~~~"}
     * */

    @Autowired
    private GptRepository gptRepository;

    @Test
    public void GPT대화테스트(){
        //cost소모되니깐 자주안하기를 권장..............
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        String topic = "질문으로 들어오는 내용에 대해서 비즈니스 문법으로 요약해줘. 그리고 해당 내용에 대한 해시태그를 최대 10개까지 만들어줘. 해시태그는 구분자로 쉼표를 사용하고 요약내용과 해시태그 목록은 '||'를 이용해서 앞에는 요약내용, 뒤에는 해시태그목록을 출력해줘";//답변에 대한 내용가이드
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


        String answer = "";
        String summaryAnswer = "";
        String hashTagList = "";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + FrameConstants.CHATAPI_KEY);

            List<Map<String, String>> messages = new ArrayList<>();

            // System message with preMessage
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", topic);
            messages.add(systemMessage);

            // User message with the question
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", question);
            messages.add(userMessage);

            // 답변 가능한 최대 글자수, 사용 모델 설정
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 500);
            requestBody.put("model", FrameConstants.GPT_MODEL);//모델선택
            requestBody.put("temperature",FrameConstants.CHAT_TEMP);

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            ResponseEntity<String> response = restTemplate.exchange(FrameConstants.CHATAPI_URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Response: " + response.getBody());
                Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);
                if (responseBody.containsKey("choices")) {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                    if (choices != null && !choices.isEmpty() && choices.get(0).containsKey("message")) {
                        Map<String, Object> messageResponse = (Map<String, Object>) choices.get(0).get("message");
                        if (messageResponse.containsKey("content")) {
                            answer = (String) messageResponse.get("content");
                            String[] answerList = answer.split("\\|\\|");
                            summaryAnswer = answerList[0];
                            hashTagList = answerList[1];

                        }
                    }
                }
            } else {
                throw new RuntimeException("ChatGPT API 요청 실패: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("ChatGPT API 호출 중 오류 발생", e);
        }
        if(answer.equals("")) {
            throw new RuntimeException("ChatGPT API 호출에서 예상치 못한 상황 발생");
        } else {
            log.info("@@@@ hashTag : " + hashTagList);
            log.info("@@@@ answer : " + summaryAnswer);
        }

    }

    @Test
    public void 대화요청DBINSERT(){

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        GptResponseDto result = new GptResponseDto();
        String question = "USB-C타입에 어떤종류가 있는지 알려줘";
        String answer = "";
        int promptToken = 0;
        int completionToken = 0;
        String responsecode = "";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + FrameConstants.CHATAPI_KEY);



            List<Map<String, String>> messages = new ArrayList<>();

            // System message with preMessage
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", "메시지 질문 내용");
            messages.add(systemMessage);

            // User message with the question
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", question);
            messages.add(userMessage);

            // 답변 가능한 최대 글자수, 사용 모델 설정
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 500);
            requestBody.put("model", "gpt-3.5-turbo");

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            ResponseEntity<String> response = restTemplate.exchange(FrameConstants.CHATAPI_URL, HttpMethod.POST, entity, String.class);
            responsecode = response.getStatusCode().toString();
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Response: " + response.getBody());
                Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);
                if (responseBody.containsKey("choices")) {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                    if (choices != null && !choices.isEmpty() && choices.get(0).containsKey("message")) {
                        Map<String, Object> messageResponse = (Map<String, Object>) choices.get(0).get("message");
                        if (messageResponse.containsKey("content")) {
                            answer = (String) messageResponse.get("content");
                        }
                    }
                }
                if(responseBody.containsKey("usage")){
                    Map<String, Object> usage = (Map<String, Object>) responseBody.get("usage");
                    promptToken = (int) usage.get("prompt_tokens");
                    completionToken = (int) usage.get("completion_tokens");
                }
            } else {
                throw new RuntimeException("ChatGPT API 요청 실패: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("ChatGPT API 호출 중 오류 발생", e);
        } finally {
            GptEntity gptEntity = GptEntity.builder()
                    .userid("dade1075@naver.com")
                    .workcate(FrameConstants.CHATAPI_CHAT)
                    .requestcont(question)
                    .responsecont(answer)
                    .requesttoken(promptToken)
                    .responsetoken(completionToken)
                    .responsecode(responsecode)
                    .build();

            gptRepository.save(gptEntity);
        }
    }

}
