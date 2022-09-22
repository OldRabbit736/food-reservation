# 로컬 컴퓨터에서 prod 프로퍼티 적용한 모든 service 를 container 환경에서 실행하기 위한 스크립트입니다.
# DockerHub 으로부터 이미지를 다운받아 실행합니다.
# 스크립트 실행 전 JEP 환경변수 설정이 필요합니다. => export JEP=(비밀번호)

if [ -n "${JEP}" ]; then
#  임시
  ./gradlew build
  docker build -f docker/app/Dockerfile -t oldrabbit736/food-reservation-app:temp .
#  임시
  docker compose -f docker-compose-prod.yml --project-name food-reservation-prod up
else
  echo "스크립트 실행 전 JEP 환경변수 값을 설정해 주세요!"
fi
