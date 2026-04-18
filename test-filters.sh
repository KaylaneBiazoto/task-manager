#!/bin/bash

# 🚀 Script de teste rápido - Filtros e Busca Textual
# Execute este script após iniciar o servidor com: mvn spring-boot:run

BASE_URL="http://localhost:8080"

echo "=========================================="
echo "🧪 Iniciando testes de Filtros e Busca"
echo "=========================================="
echo ""

# Cores para output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Função para fazer requests e exibir resultado
test_endpoint() {
    local method=$1
    local endpoint=$2
    local description=$3

    echo -e "${BLUE}📌 Testando: ${description}${NC}"
    echo -e "${YELLOW}${method} ${BASE_URL}${endpoint}${NC}"
    echo ""

    if [ "$method" = "GET" ]; then
        curl -s -X GET "${BASE_URL}${endpoint}" \
            -H "Content-Type: application/json" | jq '.' || echo "Erro na requisição"
    fi

    echo ""
    echo "=========================================="
    echo ""
    sleep 1
}

# ==================== TESTES DE USUÁRIOS ====================
echo -e "${GREEN}👥 TESTES DE USUÁRIOS${NC}"
echo ""

test_endpoint "GET" "/api/users?page=0&size=10" \
    "Listar todos os usuários"

test_endpoint "GET" "/api/users?search=john&page=0&size=10" \
    "Buscar usuários por 'john'"

test_endpoint "GET" "/api/users?role=ADMIN&page=0&size=10" \
    "Listar apenas usuários ADMIN"

test_endpoint "GET" "/api/users?role=MEMBER&page=0&size=10" \
    "Listar apenas usuários MEMBER"

test_endpoint "GET" "/api/users?search=john&role=ADMIN&page=0&size=10" \
    "Buscar usuário ADMIN contendo 'john'"

test_endpoint "GET" "/api/users?sort=createdAt,desc&page=0&size=10" \
    "Listar usuários ordenados por data de criação (desc)"

# ==================== TESTES DE PROJETOS ====================
echo -e "${GREEN}📁 TESTES DE PROJETOS${NC}"
echo ""

test_endpoint "GET" "/api/projects?page=0&size=10" \
    "Listar todos os projetos"

test_endpoint "GET" "/api/projects?search=API&page=0&size=10" \
    "Buscar projetos contendo 'API'"

test_endpoint "GET" "/api/projects?ownerId=1&page=0&size=10" \
    "Listar projetos do proprietário ID 1"

test_endpoint "GET" "/api/projects?search=Backend&ownerId=1&page=0&size=10" \
    "Buscar projetos 'Backend' do proprietário ID 1"

test_endpoint "GET" "/api/projects?sort=name,asc&page=0&size=10" \
    "Listar projetos ordenados alfabeticamente"

# ==================== TESTES DE TAREFAS ====================
echo -e "${GREEN}✅ TESTES DE TAREFAS${NC}"
echo ""

test_endpoint "GET" "/api/tasks/project/1?page=0&size=10" \
    "Listar todas as tarefas do projeto 1"

test_endpoint "GET" "/api/tasks/project/1?search=autenticação&page=0&size=10" \
    "Buscar tarefas contendo 'autenticação'"

test_endpoint "GET" "/api/tasks/project/1?status=TODO&page=0&size=10" \
    "Listar tarefas com status TODO"

test_endpoint "GET" "/api/tasks/project/1?status=IN_PROGRESS&page=0&size=10" \
    "Listar tarefas com status IN_PROGRESS"

test_endpoint "GET" "/api/tasks/project/1?status=DONE&page=0&size=10" \
    "Listar tarefas com status DONE"

test_endpoint "GET" "/api/tasks/project/1?priority=HIGH&page=0&size=10" \
    "Listar tarefas de alta prioridade"

test_endpoint "GET" "/api/tasks/project/1?priority=CRITICAL&page=0&size=10" \
    "Listar tarefas críticas"

test_endpoint "GET" "/api/tasks/project/1?assigneeId=1&page=0&size=10" \
    "Listar tarefas atribuídas ao usuário 1"

# ==================== TESTES COMBINADOS ====================
echo -e "${GREEN}🔗 TESTES COMBINADOS${NC}"
echo ""

test_endpoint "GET" "/api/tasks/project/1?status=IN_PROGRESS&priority=HIGH&page=0&size=10" \
    "Tarefas IN_PROGRESS de alta prioridade"

test_endpoint "GET" "/api/tasks/project/1?status=IN_PROGRESS&priority=HIGH&assigneeId=1&page=0&size=10" \
    "Tarefas IN_PROGRESS, alta prioridade, atribuídas a usuário 1"

test_endpoint "GET" "/api/tasks/project/1?status=IN_PROGRESS&priority=CRITICAL&search=JWT&page=0&size=10" \
    "Tarefas críticas em progresso contendo 'JWT'"

# ==================== TESTES DE PAGINAÇÃO ====================
echo -e "${GREEN}📄 TESTES DE PAGINAÇÃO${NC}"
echo ""

test_endpoint "GET" "/api/users?page=0&size=5" \
    "Primeira página com 5 itens"

test_endpoint "GET" "/api/users?page=1&size=5" \
    "Segunda página com 5 itens"

test_endpoint "GET" "/api/users?page=0&size=100" \
    "Paginação grande: 100 itens"

# ==================== TESTES DE ORDENAÇÃO ====================
echo -e "${GREEN}🔢 TESTES DE ORDENAÇÃO${NC}"
echo ""

test_endpoint "GET" "/api/tasks/project/1?sort=deadline,asc&page=0&size=10" \
    "Tarefas ordenadas por deadline (asc)"

test_endpoint "GET" "/api/tasks/project/1?sort=createdAt,desc&page=0&size=10" \
    "Tarefas ordenadas por data criação (desc)"

test_endpoint "GET" "/api/tasks/project/1?sort=priority,desc&page=0&size=10" \
    "Tarefas ordenadas por prioridade (desc)"

echo -e "${GREEN}✅ Testes concluídos!${NC}"
echo ""
echo "Para mais exemplos e documentação, veja:"
echo "  - FILTERS_AND_SEARCH_TESTS.md"
echo "  - IMPLEMENTATION_SUMMARY.md"

