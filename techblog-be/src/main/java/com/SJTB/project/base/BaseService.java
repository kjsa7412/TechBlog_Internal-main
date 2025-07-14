package com.SJTB.project.base;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BaseService {
    // 여기는 백엔드 호출에 대한 로그 추가
    private final ClntLogRepository clntLogRepository;
    private final SvrLogRepository svrLogRepository;


    /*클라이언트 작업 로그 저장(게시물작성, 수정, 로그인, 회워가입등)*/
    public void saveClientLog(String userid, String actioncate, int contid, String useragent){
        ClntLogEntity clntLogEntity = ClntLogEntity.builder()
                                      .userid(userid)
                                      .actioncate(actioncate)
                                      .contid(contid)
                                      .useragent(useragent)
                                      .build();

        clntLogRepository.save(clntLogEntity);

    }

    /*서버 작업 로그 저장(스케줄러, api요청등)*/
    public void saveServerLog(String userid, String actioncate, String prosuccyn){
        SvrLogEntity svrLogEntity = SvrLogEntity.builder()
                                    .userid(userid)
                                    .actioncate(actioncate)
                                    .prosuccyn(prosuccyn)
                                    .build();

        svrLogRepository.save(svrLogEntity);
    }
}
