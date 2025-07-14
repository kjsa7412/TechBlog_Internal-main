## 1. Project Config
- Language: HTML, CSS, JavaScript
- Runtime: Node.js 20.9
- JavaScript Framework: React
- React Framework: Next.js 14.2.5 (app router)
- State Management: Recoil
- library: Axios

## 2. Running the Project
- dev 
  - next dev = npm run dev
- build 
  - next build = npm run build
- next 
  - next start = npm start
- next dev: 개발 모드에서 애플리케이션을 실행합니다. 이 모드는 소스 코드에 대한 실시간 변경사항을 반영하고, 디버그 정보를 포함하여 개발자가 오류를 더 쉽게 찾을 수 있게 도와줍니다.
- next build: 프로덕션 모드를 위한 애플리케이션을 빌드합니다. 이 과정에서 코드 최적화, 불필요한 코드 제거, 압축 등이 수행되어 배포를 위한 정적 파일들이 생성됩니다.
- next start: next build로 생성된 빌드 결과물을 사용하여 애플리케이션을 프로덕션 모드로 실행합니다. 개발 모드 대비 최적화가 이루어져 있어서 사용자에게 제공할 때 사용하기 적합합니다.
- 개발 중에는 주로 next dev를 사용하며, 실제 서비스 배포 시에는 next build를 실행한 후 생성된 빌드 결과물로 next start를 통해 서버를 실행합니다.