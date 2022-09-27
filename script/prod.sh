# production server 환경에서 실행되는 스크립트입니다.
# DockerHub 으로부터 이미지를 다운받아 실행합니다.
# 스크립트 실행 전 JEP 환경변수 설정이 필요합니다. => export JEP=(비밀번호)

if [ -n "${JEP}" ]; then
  echo up compose...
  docker compose -f docker-compose-prod.yml --project-name food-reservation-prod up -d
else
  echo "스크립트 실행 전 JEP 환경변수 값을 설정해 주세요!"
fi
