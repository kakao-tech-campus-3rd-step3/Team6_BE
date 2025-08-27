#!/bin/bash

# ------------------------------
# deploy.sh
# ------------------------------

# 변수 정의
APP_NAME=ice_breaker
IMAGE_TAG=${1:-latest}
CONTAINER_NAME=$APP_NAME
APP_PORT=8080

echo "===== Deploying $APP_NAME:$IMAGE_TAG ====="

# 1. 기존 컨테이너 종료 및 제거
if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
  echo "Stopping running container $CONTAINER_NAME..."
  docker stop $CONTAINER_NAME
fi

if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
  echo "Removing existing container $CONTAINER_NAME..."
  docker rm $CONTAINER_NAME
fi

# 2. 최신 이미지 pull
echo "Pulling image kangtaehyun1107/$APP_NAME:$IMAGE_TAG ..."
docker pull kangtaehyun1107/$APP_NAME:"$IMAGE_TAG"

# 3. 새 컨테이너 실행
echo "Running new container $CONTAINER_NAME ..."
docker run -d \
  --name $CONTAINER_NAME \
  -p $APP_PORT:8080 \
  --env-file .env \
  kangtaehyun1107/$APP_NAME:"$IMAGE_TAG"

# 4. 상태 확인
echo "Deployment finished. Container status:"
docker ps -f name=$CONTAINER_NAME