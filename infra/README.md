# Infra 설정 방법
이 프로젝트는 Naver cloud platform(NCP)과 Terraform을 이용하여 인프라를 생성, 관리합니다.
아래는 프로젝트를 위한 인프라 생성 방법을 정리한 내용입니다.

## Step 1 - NCP 준비
- NCP에 가입합니다.
- ncloud.com > 마이페이지 > "인증키 관리" 메뉴에서 API 인증키를 생성하고 다음이 생성되었는지 확인합니다.
  - Access Key ID
  - Secret Key

## Step 2 - Terraform CLI 설치
- https://developer.hashicorp.com/terraform/tutorials/aws-get-started/install-cli 
  - Install Terraform, Verify the installation 까지만 진행

## Step 3 - Terraform Cloud 준비
Terraform Cloud에 프로젝트의 infra 구성 결과(.tfstate 파일)를 저장하는 공간을 만듭니다.
로컬에 구성 결과를 저장할 수도 있지만 실수로 해당 파일을 지울 수도 있기 때문에, 클라우드에 저장하는 것이 좀 더 안전합니다.

- Terraform Cloud에 가입합니다.
- (optional) organization 이름을 원하는 것으로 변경합니다.
- "food-reservation" 이라는 이름으로 workspace를 생성합니다.
- "food-reservation" workspace의 "Variables" 메뉴에서 다음의 Workspace Variable을 생성합니다.
  - ncp_secret_key
    - category: Terraform variable
    - key: ncp_secret_key
    - value: NCP의 Access Key ID 값
  - ncp_access_key
    - category: Terraform variable
    - key: ncp_access_key
    - value: NCP의 Secret Key 값
  - 참고: "Sensitive" 란을 체크하면 웹 페이지에서 값을 안 보이도록 할 수 있기에 설정하는 게 보안에 더 좋습니다.

## Step 4 - 인프라 생성
이제 준비가 끝났습니다. terraform 명령어를 이용하여 NCP에 인프라를 생성합니다.
- 터미널 루트를 이 폴더(infra)에 놓습니다.
- terraform cloud organization 이름을 환경 변수 TF_CLOUD_ORGANIZATION 에 지정합니다.
  ```shell
  $ export TF_CLOUD_ORGANIZATION=<Terraform cloud organization 이름>
  ```
- 필요한 terraform 설정을 하도록 초기화 명령합니다.
  ```shell
  $ terraform init # "Terraform Cloud has been successfully initialized!" 문구가 출력되면 초기화 성공.
  ```
- infra 구성을 명령합니다.
  ```shell
  $ terraform apply  
  ```
  - "Plan: x to add, 0 to change, 0 to destroy" 등과 같이 생성할 리소스 개수에 대한 정보가 출력됩니다.
  - "Do you want to perform these actions in workspace "food-reservation"?" 에 "yes"를 입력합니다.
  - NCP에 리소스 생성이 시작됩니다. 완료까지 4 ~ 10분 정도 소요됩니다.
- NCP console에서 생성된 리소스를 확인합니다.
- Terraform cloud에서 지정한 organization, food-reservation workspace에 리소스 구성 결과가 저장되어 있는지 확인합니다.

## Step 5 - 서버에 ssh 접속, 초기화
생성된 public ip를 통해 서버에 ssh 접속 확인합니다.
- ssh 접속합니다.
  ```shell
  $ ssh root@<public ip> -p 22
  ```
  - 필요한 public ip, root password는 food-reservation workspace의 "States"에서 확인할 수 있습니다.
- infra/server-scripts 안의 스크립트들을 실행시켜서 초기화 시켜줍니다.
