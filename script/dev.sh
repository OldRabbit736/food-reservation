# app과 나머지 서비스(DB 등)를 띄움
if [ -n "${JEP}" ]; then
  ./gradlew bootJar
  docker compose -f docker-compose-dev.yml --profile app up --build
else
  echo "스크립트 실행 전 JEP 환경변수 값을 설정해 주세요!"
fi
