terraform {
  required_providers {
    ncloud = {
      source = "navercloudplatform/ncloud"
    }
  }

  cloud {
    //organization = ""
    //적어놓기 보다는 동적으로(환경변수로) 입력하도록 변경. terraform 명령어 내리기 전 환경변수 설정 필요.
    // ==> export TF_CLOUD_ORGANIZATION=...
    workspaces {
      name = "food-reservation"
    }
  }
}
