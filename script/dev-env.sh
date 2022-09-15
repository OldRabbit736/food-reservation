# app을 제외한 나머지 서비스(DB 등)를 띄움
docker compose -f docker-compose-dev.yml --project-name food-reservation-dev up

# app 을 intellij 등 IDE에서 실행하며 다음과 같은 configuration 필요
# active profile = dev
# JEP = (비밀번호)
