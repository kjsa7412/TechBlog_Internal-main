package com.SJTB.project.gpt;


import com.SJTB.framework.data.ResultVo;
import com.SJTB.project.base.BaseController;
import com.SJTB.project.boad.BoadContRequestDto;
import com.SJTB.project.boad.BoadContResponseDto;
import com.SJTB.project.boad.BoadEntity;
import com.SJTB.project.user.UserRequestDto;
import com.SJTB.project.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GptController extends BaseController {

    private final GptService gptService;

    /**
     * 게시글 작성시 AI기능 요청을 처리한다.
     *
     * @param  request
     * @param  gptRequestDto
     * @return CHAT GPT API 조회한 결과를 return 한다.
     */
    @PostMapping("/private/post/gpt/comp")
    public ResultVo<GptResponseDto> ChatGPTAPIComp(HttpServletRequest request, @RequestBody GptRequestDto gptRequestDto) {
        return gptService.askGPT(request, gptRequestDto);
    }

//    @GetMapping("/public/post/gpt/smry")
//    public void ChatGPTExtractSummary(Integer boadid){
//        gptService.summaryBoadCont(boadid);
//    }
}
