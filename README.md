# food-reservation

## 프로젝트 실행 방법
프로젝트 실행, 개발을 위해 스크립트가 준비되어 있습니다.
* 로컬에서 프로젝트 실행
  * docker compose를 이용하여 프로젝트에 필요한 모든 서비스들을 로컬에서 생성하고 실행합니다.
  * 실행 방법
    * 터미널을 프로젝트 루트에 위치시킨 후 아래의 명령어를 입력해 주세요.
      * ```shell
        $ export JEP=... # jasypt encryptor password 값을 "JEP" 환경변수에 넣어줍니다.    
        $ sh script/dev.sh # 스크립트를 실행합니다.
        ```
        
<br>

* 로컬에서 앱을 제외한 환경(db 등)만 세팅
  * docker compose를 이용하여 로컬에서 앱을 제외한 나머지 서비스들을 실행합니다.
  * 이 후, IDE 등을 이용해 로컬에서 앱을 개발합니다.
  * 실행 방법
      * 터미널을 프로젝트 루트에 위치시킨 후 아래의 명령어를 입력해 주세요.
        * ```shell
          $ sh script/dev-env.sh
          ```
      * IDE 실행 옵션을 다음과 같이 세팅한 후 앱을 실행시킵니다.
        * spring.active.profile=dev
        * JEP=...

## 인프라 설정 방법
[링크](./infra/README.md)

## API Schema

## Rules
- [Git branch naming](document/rules/git-branch-naming.md)
- [GitHub PR naming](document/rules/github-pr-naming.md)

## Technical Issues
기술적으로 고민했던 부분을 정리하였습니다.
- [enum vs. 테이블](document/technical-issues/enum-vs-table.md) - 진행 중
- [연관관계 annotation 사용 여부](document/technical-issues/association-annotation.md) - 진행 중
- [비즈니스 로직 위치 - domain vs. service](document/technical-issues/business-logic-domain-vs-service.md) - 진행 중
- [패키지 구조](document/technical-issues/package-structure.md) - 진행 중
- [서비스와 컨트롤러 메서드의 파라미터, 리턴 타입 정책](document/technical-issues/service-controller-return-type-policy.md) - 진행 중
- [프론트엔드가 사용하기 쉬운 API 리턴 타입](document/technical-issues/api-return-type.md) - 진행 중

## ETC
- [no new line 경고 해결](document/etc/no-new-line.md)
