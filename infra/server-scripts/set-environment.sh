# https://docs.docker.com/engine/install/ubuntu/

## Set up the repository
sudo apt-get update
sudo apt-get install \
ca-certificates \
curl \
gnupg \
lsb-release

sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null


## Install Docker Engine
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin


## get docker compose file
mkdir food-reservation && cd food-reservation
wget https://kr.object.ncloudstorage.com/food-reservation/docker-compose-prod.yml

## 환경변수 설정
# export JEP = ...

## 실행
docker compose -f docker-compose-prod.yml up
