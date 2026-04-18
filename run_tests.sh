#!/bin/bash

# 🧪 Script para executar testes e gerar cobertura
# Task Manager Backend

PROJECT_DIR="/home/kaylane.biazoto@db1.com.br/Music/task-manager-backend"
JAVA_HOME="/usr/lib/jvm/java-21-openjdk-amd64"

echo "════════════════════════════════════════════════════════════"
echo "🧪 Task Manager Backend - Test Runner & Coverage Generator"
echo "════════════════════════════════════════════════════════════"
echo ""

cd "$PROJECT_DIR" || exit

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_menu() {
    echo ""
    echo -e "${BLUE}Escolha uma opção:${NC}"
    echo "  1) 🧪  Executar apenas testes (rápido)"
    echo "  2) 📊 Executar testes + gerar cobertura com JaCoCo"
    echo "  3) 🔍 Abrir relatório de cobertura no navegador"
    echo "  4) 🧹 Limpar arquivos de teste e reconstruir"
    echo "  5) 📈 Executar um teste específico"
    echo "  6) ✨ Sair"
    echo ""
    read -p "Opção: " choice
}

run_tests_only() {
    echo ""
    echo -e "${BLUE}▶ Executando testes...${NC}"
    JAVA_HOME=$JAVA_HOME mvn clean test 2>&1 | tee test_output.log

    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Testes executados com sucesso!${NC}"
    else
        echo -e "${RED}❌ Alguns testes falharam!${NC}"
    fi
}

run_tests_with_coverage() {
    echo ""
    echo -e "${BLUE}▶ Executando testes com cobertura JaCoCo...${NC}"
    echo "  (Isso pode levar 30-40 segundos)"
    echo ""

    JAVA_HOME=$JAVA_HOME mvn clean test jacoco:report 2>&1 | tee test_coverage_output.log

    if [ $? -eq 0 ]; then
        echo ""
        echo -e "${GREEN}✅ Testes e cobertura gerados com sucesso!${NC}"
        echo ""
        echo -e "${YELLOW}📍 Relatório de cobertura em:${NC}"
        echo "   file://$(pwd)/target/site/jacoco/index.html"
        echo ""
        echo -e "${YELLOW}Para abrir no navegador:${NC}"
        echo "   xdg-open target/site/jacoco/index.html"
    else
        echo -e "${RED}❌ Erro ao gerar cobertura!${NC}"
    fi
}

open_coverage_report() {
    echo ""
    if [ -f "target/site/jacoco/index.html" ]; then
        echo -e "${GREEN}📂 Abrindo relatório de cobertura...${NC}"
        xdg-open "target/site/jacoco/index.html" 2>/dev/null || \
        echo -e "${YELLOW}file://$(pwd)/target/site/jacoco/index.html${NC}"
    else
        echo -e "${RED}❌ Relatório não encontrado!${NC}"
        echo "   Execute a opção 2 primeiro para gerar o relatório."
    fi
}

clean_rebuild() {
    echo ""
    echo -e "${BLUE}▶ Limpando e reconstruindo...${NC}"
    JAVA_HOME=$JAVA_HOME mvn clean package -DskipTests 2>&1 | tail -20
    echo -e "${GREEN}✅ Limpeza concluída!${NC}"
}

run_specific_test() {
    echo ""
    echo -e "${BLUE}Testes disponíveis:${NC}"
    echo "  1) CreateProjectHandlerTest"
    echo "  2) AddProjectMemberHandlerTest"
    echo "  3) CreateTaskHandlerTest"
    echo "  4) UpdateTaskHandlerTest"
    echo "  5) Todos os *HandlerTest"
    echo ""
    read -p "Selecione (1-5): " test_choice

    case $test_choice in
        1) TEST_NAME="CreateProjectHandlerTest" ;;
        2) TEST_NAME="AddProjectMemberHandlerTest" ;;
        3) TEST_NAME="CreateTaskHandlerTest" ;;
        4) TEST_NAME="UpdateTaskHandlerTest" ;;
        5) TEST_NAME="*HandlerTest" ;;
        *) echo -e "${RED}Opção inválida${NC}"; return ;;
    esac

    echo ""
    echo -e "${BLUE}▶ Executando: $TEST_NAME${NC}"
    JAVA_HOME=$JAVA_HOME mvn test -Dtest="$TEST_NAME" 2>&1 | tail -30
}

# Main loop
while true; do
    print_menu

    case $choice in
        1) run_tests_only ;;
        2) run_tests_with_coverage ;;
        3) open_coverage_report ;;
        4) clean_rebuild ;;
        5) run_specific_test ;;
        6)
            echo ""
            echo -e "${GREEN}👋 Até logo!${NC}"
            exit 0
            ;;
        *)
            echo -e "${RED}Opção inválida!${NC}"
            ;;
    esac
done

