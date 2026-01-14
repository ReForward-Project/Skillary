#!/bin/bash

# Skillary 프로젝트 실행 스크립트
# 사용법: ./실행하기.sh

echo "=========================================="
echo "  Skillary 프로젝트 실행 스크립트"
echo "=========================================="
echo ""

# 프로젝트 루트 디렉토리로 이동
cd "$(dirname "$0")"
PROJECT_ROOT=$(pwd)

echo "📁 프로젝트 루트: $PROJECT_ROOT"
echo ""

# MySQL 확인
echo "🔍 MySQL 데이터베이스 확인 중..."
if docker ps | grep -q mysql; then
    echo "✅ MySQL이 실행 중입니다."
else
    echo "⚠️  MySQL이 실행되지 않았습니다."
    echo "   다음 명령어로 MySQL을 실행하세요:"
    echo "   docker run -d --name mysql-skillary -e MYSQL_ROOT_PASSWORD=root1234 -e MYSQL_DATABASE=testdb -e MYSQL_USER=test -e MYSQL_PASSWORD=1234 -p 3306:3306 mysql:8.0"
    echo ""
    read -p "계속하시겠습니까? (y/n): " -n 1 -r
    echo ""
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

echo ""
echo "=========================================="
echo "  서비스 실행 안내"
echo "=========================================="
echo ""
echo "다음과 같이 3개의 터미널에서 실행하세요:"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "터미널 1 - 백엔드 실행:"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "cd $PROJECT_ROOT/spring-skillary-back"
echo "./gradlew bootRun --args='--spring.profiles.active=local'"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "터미널 2 - 프론트엔드 실행:"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "cd $PROJECT_ROOT/next-skillary-front"
echo "npm run dev"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "접속 주소:"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "프론트엔드: http://localhost:3000"
echo "회원가입 페이지: http://localhost:3000/auth/register"
echo "백엔드 API: http://localhost:8080/api/auth"
echo ""
echo "=========================================="
