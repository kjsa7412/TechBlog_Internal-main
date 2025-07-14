package com.SJTB.framework.config;

public class FrameConstants {
    /* error constants */
    public static final String ERROR_SERVICE    = "[Service Error]";
    public static final String ERROR_CONTROLLER = "[Controller Error]";

    /* jwt */
    public static final String JWT_SECRET_KEY          = "Thetokenvalueusedinthedatabasequeryrepresentsakeyfor\"sjinc\"company.Thistokenistypicallyusedforauthenticationauthorization,andsecuritypurposes";
    public static final String JWT_REFRESH_SECRET_KEY  = "Thetokenvalueusedinthedatabasequeryrepresentsakeyfor\"sjinc\"company.Thistokenistypicallyusedforauthenticationauthorization,andsecuritypurposes";
    public static final long   JWT_ACCESS_TOKEN_VALID_TIME  = 1000L * 60 * 60; //1시간 유효시간
    public static final long   JWT_REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 60; //60시간 유효시간
    public static final String JWT_ACCESS_TOKEN_NAME   = "X-AUTH-TOKEN";
    public static final String JWT_REFRESH_TOKEN_NAME  = "X-RENEW-TOKEN";
    public static final String JWT_BEARER_PREFIX       = "Bearer ";
    public static final String JWT_EXPIRED_TOKEN_MSG   = "Expired TOKEN";
    public static final String JWT_INVALID_TOKEN_MSG   = "Invalid TOKEN";

    /*GPT API*/
    public static final String CHATAPI_URL              = "https://api.openai.com/v1/chat/completions";//Request URL
    public static final String CHATAPI_KEY              = "?????"; // API Key
    public static final int    CHAT_MAX_TOKEN_VALUE     = 500;
    public static final int    SUMMARY_MAX_TOKEN_VALUE = 200;
    public static final String GPT_MODEL                = "gpt-4o-mini";
    public static final float  CHAT_TEMP                = 0.7f;//질문 온도값 (값이 높을수록 정확도, 창의성이 높아짐(0 ~ 2))
    public static final float  SUMMRARY_TEMP            = 0.2f;//내용요약 온도값 (값이 높을수록 정확도, 창의성이 높아짐(0 ~ 2))

    /*BOARD PAGE LIMIT*/
    public static final int    BOARD_PAGE_COUNT = 10;//게시판 목록 아이템 갯수

    public static final String CHATAPI_CHAT         = "AIQN";//CHATGPT 대화
    public static final String CHATAPI_SUMMARY      = "AISP";//CHATGPT 게시물요약
    public static final String TEMP_IMAGE_CLEAN     = "TMIC";//임시 이미지 파일 정리
    public static final String BOARD_UPDATE         = "BOWR";//인기글목록
    public static final String BOARD_SEARCH         = "SRLS";//게시글검색(목록과 같이사용)
    public static final String BOARD_INSERT         = "BOIN";//게시글작성
    public static final String BOARD_VIEW           = "BOVW";//게시글조회
    public static final String BOARD_DELETE         = "BODL";//게시글삭제
    public static final String BOARD_IMAGE_UPLOAD   = "IMUL";//게시물이미지 업로드
    public static final String VIEW_LIKE            = "BOLK";//좋아요 클릭
    public static final String BOARD_COMMENT_WRITE  = "CMIN";//댓글입력
    public static final String BOARD_COMMENT_DELTE  = "CMDT";//댓글삭제

    public static final String BLOG_SIGNUP          = "SGUP";//회원가입
    public static final String BLOG_LOGIN           = "LOGN";//로그인
    public static final String BLOG_PF_IMAGE_INSERT  = "IMPF";//이미지 프로필 업로드
    public static final String BLOG_PROFILE_UPDATE  = "PFMF";//프로필 수정
    public static final String BLOG_LOGOUT          = "LOGO";//로그아웃
    public static final String BLOG_RENEW           = "RENE";//토큰 재발급

    public static final String MARKDOWN_FILE_MAKE   = "MKMD";//마크다운 파일 생성
    public static final String H1_TAG           = "H1TG";//H1태그
    public static final String H2_TAG           = "H2TG";//H2태그
    public static final String H3_TAG           = "H3TG";//H3태그
    public static final String ITALIC_FONT      = "ITLI";//기울임꼴
    public static final String BOLD_FONT        = "BOLD";//굵은 글씨
    public static final String ORDERED_LIST     = "OLST";//글번호UL
    public static final String UNORDERED_LIST   = "ULST";//UL
    public static final String CODE_TEMPLATE    = "CODE";//코드블록
    public static final String HORIZONTAL_RULE  = "HRUL";//HR수평줄
    public static final String URL_LINK         = "LINK";//외부링크[링크 텍스트](URL)
    public static final String IMAGE_TAG        = "IMAG";//첨부이미지[![Image](이미지 내용)](링크 URL)
    public static final String DIV              = "DIVS";//DIV 태그

    public static final String IMAGE_UPLOAD_THUMBNAIL   ="THUM";
    public static final String IMAGE_UPLOAD_CONTENT     ="CONT";
    public static final String IMAGE_UPLOAD_PROFILE     ="PROF";

}
