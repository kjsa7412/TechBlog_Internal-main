## **1. 프로젝트 개요**

### **1. 프로젝트 소개**
- Spring Boot와 Next.js를 활용하여 개발한 기술 블로그 미니 프로젝트입니다.
- 상황에 따라 Next.js의 다양한 렌더링 기법(**SSR, CSR, SSG, ISR**)을 적절히 활용하여 개발을 진행하였습니다.

    <img width="100%" alt="Service" src="https://github.com/user-attachments/assets/34572a47-d028-42d3-b789-71bafbf0bd5c"/>

- **해당 프로젝트는 내부 사용자를 위한 서비스로**, 블로그 내의 모든 기능을 자유롭게 사용할 수 있도록 구성되어 있습니다.
    
    <img width="100%" alt="Service" src="https://github.com/user-attachments/assets/3cc00bc4-dd96-4a04-a997-c10dc4eabe6f"/>
    
- 외부 사용자용 프로젝트 정보는 [기술 블로그 (외부 사용자)](https://github.com/Oh-byeongju/TechBlog_External)를 참고해주세요.
### **2. 개발 기간**
- 2024.08.01 ~ 2024.12.31

## **2. 기술 및 도구**

### `Frontend`
- Node.js 20.9
- Next.js 14.2.5 (App Router)
- TypeScript
- Axios
- Recoil
- React Query
### `Backend`
- Java 11
- Spring Boot 2.7.3
- Spring Data JPA
- Spring Security
- PostgreSQL
- JWT
### `Library / API`
- OpenAI
- Milkdown Editor
- Unsplash
### `Design Tools`
- Figma
- Confluence
- ERDCloud
  
## **3. ERD 설계**

<img width="100%" alt="ERD" src="https://github.com/user-attachments/assets/1b6b2a81-91b5-479c-a7cd-f8b98c1b0650"/>

## **4. 시스템 아키텍처**

<img width="80%" alt="System" src="https://github.com/user-attachments/assets/19bd741a-e8a1-4788-96d5-a36a5dc29991"/>

&nbsp;&nbsp;

1. 사용자가 Web 또는 Mobile Web 환경(`Chrome`, `Edge` 등)에서 URL에 접속하면, 브라우저는 `Next.js` 기반 프론트엔드 서버로 요청을 보냅니다.
2. 프론트엔드 서버는 `Node.js` 런타임 위에서 작동하며, `TypeScript`와 `React` 기반의 `Next.js (App Router)`를 통해  HTML을 생성합니다. 이 과정에서 필요한 데이터는 Axios를 통해 백엔드 API로 요청됩니다.
3. 백엔드는 `Spring Boot` 서버로 구성되어 있으며, `Apache Tomcat`을 통해 HTTP 요청을 수신합니다. 프론트엔드에서 전달된 API 요청은 Spring MVC, Spring Security를 거쳐 처리됩니다.
4. 필요한 데이터는 `Spring Data JPA`를 통해 `PostgreSQL` 데이터베이스에서 조회되며, 비즈니스 로직에 따라 가공된 결과가 다시 프론트엔드로 전달됩니다.
5. 사용자의 요청 중 일부는 `OpenAI API`와 통신해야 할 수도 있습니다. 예를 들어, **GPT 기반의 텍스트 요약 또는 자동 생성 기능** 등이 이에 해당하며, 백엔드는 외부 `OpenAI` 서버에 HTTP 요청을 보내 응답을 받은 뒤 이를 프론트엔드에 반환합니다.

## 5. 렌더링 구조 및 핵심 기능

### 1. 렌더링 구조

- 대부분의 페이지는 **CSR (Client Side Rendering)** 방식으로 구현되었습니다.
- 외부에 공개되지 않고 내부에서만 사용하는 시스템이기 때문에, SEO를 고려할 필요가 없어 **개발 및 사용자 경험이 좋은 CSR** 방식으로 대부분의 페이지를 구현하였습니다.
- 반면, 기능이 단순하거나 초기 로딩만 필요한 일부 페이지는 **SSR** 방식으로 처리하여 렌더링 효율을 높였습니다.

```tsx
Route (app)                              Size     First Load JS
┌ ○ /                                    3.83 kB         160 kB
├ ○ /_not-found                          883 B          90.3 kB
├ ƒ /api/allPost                         0 B                0 B
├ ƒ /api/createPost                      0 B                0 B
├ ƒ /api/deletePost                      0 B                0 B
├ ƒ /api/getPostBySlug                   0 B                0 B
├ ƒ /api/popPost                         0 B                0 B
├ ƒ /api/search                          0 B                0 B
├ ƒ /api/signEdit                        0 B                0 B
├ ƒ /api/signIn                          0 B                0 B
├ ƒ /api/signUp                          0 B                0 B
├ ƒ /api/updateImage                     0 B                0 B
├ ƒ /board/[slug]                        7.72 kB         300 kB
├ ƒ /board/[slug]/edit                   1.05 kB         293 kB
├ ○ /board/new                           3.07 kB         261 kB
└ ƒ /board/search/[keyword]              3.11 kB         159 kB

- First Load JS shared by all 89.5 kB
├ chunks/2117-1e97556394822c23.js 31.7 kB
├ chunks/fd9d1056-cd655eb3e6ad1550.js 53.7 kB
└ other shared chunks (total) 4.06 kB

ƒ Middleware                             27 kB
○  (Static)   prerendered as static content
ƒ  (Dynamic)  server-rendered on demand
```

### 2. 핵심 기능

- 반응형 웹과 다크 모드를 포함하여, 사용자 경험(UX)을 향상시킬 수 있는 기능을 제공합니다.
- 게시물 작성 시, OpenAI API를 활용하여 내용을 자동 요약하고 관련 해시태그를 생성한 뒤, 이를 메타데이터와 함께 저장합니다.
- 게시물은 `.md` 형식으로 저장되며, 상단에 메타 정보를 포함해 검색, 조회, 렌더링 등 다양한 용도로 효율적으로 활용됩니다.

## 6. 사용자 요청 흐름

<div align="center">
  <img width="85%" alt="Flow" src="https://github.com/user-attachments/assets/7ce83aa4-32cd-45a6-87f0-85342663c919" />
</div>

1. 사용자의 모든 요청은 `Next.js` 미들웨어를 거칩니다.
2. 미들웨어에서는 쿠키에 저장된 JWT 토큰(`AccessToken`)을 읽어, `Authorization` 헤더에 추가합니다.
3. 이후, 백엔드(Spring Boot) API 서버로 **요청을 리다이렉트**하면서, 필요한 헤더 정보들을 함께 전달합니다.

```tsx
import {type NextRequest, NextResponse} from "next/server";

import {EJWT} from "@/types/enums/common-enum";

// 프록시 조건
export const config = {
  matcher: "/APICALL/:path*", // Match all requests under /APICALL
};

export function middleware(request: NextRequest) {
  const authToken = request.cookies.get(EJWT.AccessToken)?.value || '';

  const originalPathname = request.nextUrl.pathname;
  const newPathname = originalPathname.startsWith('/APICALL')
      ? originalPathname.substring('/APICALL'.length)
      : originalPathname;

  // 새로운 요청 헤더 설정
  const requestHeaders = new Headers(request.headers);

  // Authorization 헤더 추가
  if (authToken) {
      requestHeaders.set('Authorization', `Bearer ${authToken}`);
  }

  // API 주소로 프록시하면서 새로운 요청 헤더 설정
  return NextResponse.rewrite(
      new URL(`${process.env.NEXT_PUBLIC_REAL_SVR_BASE_URL}${newPathname}${request.nextUrl.search}`, request.url),
      {
          request: {
              headers: requestHeaders,
          },
      }
  );
}
```

## **7. 기능 소개 Wiki**

**1️⃣ 메인화면 - [상세보기](https://github.com/Oh-byeongju/TechBlog_Internal/wiki/1.-%EB%A9%94%EC%9D%B8%ED%99%94%EB%A9%B4)**

```
- 인기 게시물 조회
- 전체 게시물 조회
- 다크모드 지원
- 반응형 웹 지원
- 게시물 검색
```

**2️⃣ 회원 - [상세보기](https://github.com/Oh-byeongju/TechBlog_Internal/wiki/2.-%ED%9A%8C%EC%9B%90)**

```
- 회원가입
- 로그인 (JWT 기반)
- 회원정보 수정
- 내가 쓴 게시물 조회
```

**3️⃣ 게시물 - [상세보기](https://github.com/Oh-byeongju/TechBlog_Internal/wiki/3.-%EA%B2%8C%EC%8B%9C%EB%AC%BC)**

```
- 게시물 상세조회
- 개시물 공감
- 게시물 작성, 수정, 삭제
- 댓글 작성, 삭제
```

**4️⃣ AI - [상세보기](https://github.com/Oh-byeongju/TechBlog_Internal/wiki/4.-AI)**

```
- 사용자가 작성한 게시물 요약
- 게시물 내용을 기반으로 한 해시태그 추출
```

## **8. 개선사항 및 향후 개발 계획**

### 1. 에디터 기능 강화
- 이미지 첨부 시 드래그 앤 드롭 및 복사-붙여넣기 지원
- 게시물 작성 및 수정 중 임시 저장 기능 제공, 페이지 이탈 시 경고 메시지 출력
- 이미지 업로드 시 `temp` 디렉토리를 활용하여 불필요한 이미지 저장 방지
- 일정 주기로 자동 저장되는 기능 구현

### 2. Markdown 파일 서버 연동 및 이력 관리
- 현재 프론트엔드에서만 사용하는 `md` 파일을 백엔드 서버와 연동하여 관리
- `boad_cont_info` 테이블에 게시물 메타 정보(분류, 공개 여부 등) 저장
- `sy_file_md_info` 테이블을 활용해 버전별 `md` 파일을 관리하고, GitHub처럼 히스토리 관리 기능 제공

### 3. 관리자 페이지 구축
- 공통 코드, 사용자, 게시물 등을 효율적으로 관리할 수 있는 관리자 페이지 개발
- 외부에 공개되는 프로젝트와 연동될 수 있도록 기능 확장 및 API 설계

### 4. AI 기능 비동기화
- 게시물 작성 시 ChatGPT 요약 및 태그 생성을 비동기 처리로 전환하여 사용자 경험 개선

## **9. 개발 후기**

**✏️ 프로젝트에 대한 후기 및 느낀점입니다.**

> 이번 프로젝트를 통해 **Next.js의 각종 렌더링 방식**부터, **파일 저장 구조와 경로 관리**, **암호화 처리**, **OpenAI API 연동**, 그리고 Spring Boot 환경에서의 **전역 설정 및 상수 관리**, **로그·이력 관리 방식**까지 폭넓게 다뤄볼 수 있었습니다. 특히 백엔드에서는 파일 업로드/저장 시 디렉토리 구조를 어떻게 잡을지, 경로를 어떻게 안전하게 관리할지를 직접 설계해보며 실무적인 감각을 키울 수 있었습니다. 이번 경험을 바탕으로 다음에는 더 많은 사람들과 함께 협업하며, 완성도 높은 프로젝트를 만들어보고 싶다는 생각이 들었습니다.