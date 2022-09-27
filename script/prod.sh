# production server 환경에서 실행되는 스크립트입니다.
# 스크립트 실행 전 IMAGE_NAME, JEP 환경변수 설정이 필요합니다. CD 과정에서 자동으로 설정됩니다.

if [ -z "${IMAGE_NAME}" ]; then
  echo "스크립트 실행 전 IMAGE_NAME 환경변수 값을 설정해 주세요!"
  exit 1
fi

if [ -z "${JEP}" ]; then
  echo "스크립트 실행 전 JEP 환경변수 값을 설정해 주세요!"
  exit 1
fi

echo up compose...
docker compose -f docker-compose-prod.yml --project-name food-reservation-prod up -d
