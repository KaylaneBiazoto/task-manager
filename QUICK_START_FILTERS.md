# 🚀 Guia Rápido - Filtros e Busca Textual Implementados

## ✅ O que foi implementado

Seu projeto agora possui **listagem com filtros avançados e busca textual** em:
- ✅ **Usuários** - Busca por username/email + filtro por role
- ✅ **Projetos** - Busca por nome/descrição + filtro por proprietário  
- ✅ **Tarefas** - Busca por título/descrição + filtros por status, prioridade e responsável

---

## 🚀 Como Começar

### 1. Compilar o projeto
```bash
cd /home/kaylane.biazoto@db1.com.br/Music/task-manager-backend
mvn clean install -DskipTests
```

### 2. Iniciar o servidor
```bash
mvn spring-boot:run
```

### 3. Testar os endpoints

#### 📌 Opção A: Usar o script bash (recomendado para testes rápidos)
```bash
chmod +x test-filters.sh
./test-filters.sh
```

#### 📌 Opção B: Usar o Postman
1. Abra o Postman
2. Crie uma nova requisição GET
3. Use os exemplos abaixo

#### 📌 Opção C: Usar curl no terminal
```bash
curl "http://localhost:8080/api/users?role=ADMIN&page=0&size=10"
```

---

## 📊 Exemplos Rápidos

### Usuários
```bash
# Listar todos com paginação
GET http://localhost:8080/api/users?page=0&size=10

# Buscar por username/email
GET http://localhost:8080/api/users?search=john&page=0&size=10

# Filtrar por role
GET http://localhost:8080/api/users?role=ADMIN&page=0&size=10

# Combinado: busca + role
GET http://localhost:8080/api/users?search=john&role=ADMIN&page=0&size=10

# Ordenado
GET http://localhost:8080/api/users?sort=username,asc&page=0&size=10
```

### Projetos
```bash
# Listar todos
GET http://localhost:8080/api/projects?page=0&size=10

# Buscar por nome
GET http://localhost:8080/api/projects?search=Backend&page=0&size=10

# Filtrar por proprietário
GET http://localhost:8080/api/projects?ownerId=1&page=0&size=10

# Combinado
GET http://localhost:8080/api/projects?search=API&ownerId=1&page=0&size=10
```

### Tarefas
```bash
# Listar todas do projeto
GET http://localhost:8080/api/tasks/project/1?page=0&size=10

# Por status
GET http://localhost:8080/api/tasks/project/1?status=IN_PROGRESS&page=0&size=10

# Por prioridade
GET http://localhost:8080/api/tasks/project/1?priority=HIGH&page=0&size=10

# Por responsável
GET http://localhost:8080/api/tasks/project/1?assigneeId=1&page=0&size=10

# Combinado: status + prioridade + responsável
GET http://localhost:8080/api/tasks/project/1?status=IN_PROGRESS&priority=HIGH&assigneeId=1&page=0&size=10

# Com busca + filtros
GET http://localhost:8080/api/tasks/project/1?search=JWT&status=IN_PROGRESS&priority=HIGH&page=0&size=10

# Ordenado por deadline
GET http://localhost:8080/api/tasks/project/1?sort=deadline,asc&page=0&size=10
```

---

## 🎯 Parâmetros Disponíveis

### Paginação
- `page=0` - Número da página (começa em 0)
- `size=10` - Itens por página

### Ordenação
- `sort=campo,direção` - Onde direção é `asc` ou `desc`
- Exemplos: `sort=username,asc`, `sort=createdAt,desc`

### Filtros por Recurso

**Users:**
- `search=texto` - Busca em username e email
- `role=ADMIN|MEMBER` - Filtro por role

**Projects:**
- `search=texto` - Busca em nome e descrição
- `ownerId=1` - Filtro por proprietário

**Tasks:**
- `search=texto` - Busca em título e descrição
- `status=TODO|IN_PROGRESS|DONE` - Filtro por status
- `priority=LOW|MEDIUM|HIGH|CRITICAL` - Filtro por prioridade
- `assigneeId=1` - Filtro por responsável

---

## 📚 Documentação Completa

Para documentação mais detalhada, acesse:

1. **FILTERS_AND_SEARCH_TESTS.md** - 50+ exemplos de testes
2. **IMPLEMENTATION_SUMMARY.md** - Resumo técnico da implementação
3. **POSTMAN_TESTS.md** - Testes existentes (CRUD básico)

---

## 🔧 Arquivos Modificados

### Repositórios
- `src/main/java/.../auth/repositories/UserRepository.java` - Métodos de filtro
- `src/main/java/.../projects/repositories/ProjectRepository.java` - Métodos de filtro
- `src/main/java/.../tasks/repositories/TaskRepository.java` - Métodos de filtro

### Handlers/Usecases
- `src/main/java/.../auth/usecases/ListUsersHandler.java` - Com filtros
- `src/main/java/.../projects/usecases/ListProjectsHandler.java` - Com filtros
- `src/main/java/.../tasks/usecases/ListTasksHandler.java` - **NOVO** com filtros

### Services
- `src/main/java/.../auth/services/UserService.java` - Novo método com filtros
- `src/main/java/.../projects/services/ProjectService.java` - Novo método com filtros
- `src/main/java/.../tasks/services/TaskFacade.java` - Novo método com filtros

### Controllers
- `src/main/java/.../auth/api/AuthController.java` - Endpoints com filtros
- `src/main/java/.../projects/api/ProjectController.java` - Endpoints com filtros
- `src/main/java/.../tasks/api/TaskController.java` - Endpoints com filtros

---

## 📝 Resposta Padrão

Todos os endpoints retornam no mesmo formato:

```json
{
  "success": true,
  "message": "Mensagem descritiva",
  "data": {
    "content": [
      {
        "id": 1,
        "field1": "value1",
        "field2": "value2",
        "createdAt": "2026-04-18T10:00:00Z",
        "updatedAt": "2026-04-18T10:00:00Z"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10,
      "sort": {...}
    },
    "totalElements": 1,
    "totalPages": 1,
    "size": 10,
    "number": 0,
    "numberOfElements": 1,
    "first": true,
    "last": true,
    "empty": false
  }
}
```

---

## ⚠️ Pontos Importantes

1. **Values de enum em UPPERCASE**: `status=IN_PROGRESS`, `priority=CRITICAL`, `role=ADMIN`
2. **Busca case-insensitive**: `search=john` encontra "John", "JOHN", etc
3. **Filtros são AND**: Múltiplos filtros combinam com AND (não OR)
4. **Valores nulos ignorados**: Se um filtro não for fornecido, é ignorado
5. **Paginação padrão**: `page=0&size=10` se não especificado

---

## 🧪 Testando com Postman

### Setup rápido:
1. Abra Postman
2. Crie uma nova requisição GET
3. Copie uma das URLs de exemplo acima
4. Clique em "Send"
5. Veja a resposta em formato JSON

### Variáveis úteis:
```
{{BASE_URL}} = http://localhost:8080
{{PROJECT_ID}} = 1
{{USER_ID}} = 1
{{ADMIN_ROLE}} = ADMIN
```

Depois use: `{{BASE_URL}}/api/users?role={{ADMIN_ROLE}}`

---

## 🎓 Próximos Passos

1. ✅ Teste todos os endpoints
2. ✅ Explore as combinações de filtros
3. ✅ Implemente no seu frontend
4. ✅ Adicione mais filtros se necessário
5. ✅ Implemente autorização por usuário logado

---

## 🆘 Troubleshooting

### Erro: "400 Bad Request"
- Verifique se os valores de enum estão em UPPERCASE
- Exemplo correto: `status=IN_PROGRESS`, não `status=in_progress`

### Erro: "404 Not Found"
- Verifique o ID do recurso (project_id, user_id, etc)
- Verifique se o recurso realmente existe no banco

### Resposta vazia
- Verifique o filtro aplicado
- Tente sem o filtro para confirmar que existem dados
- Verifique a paginação (talvez seja a última página e não tenha dados)

---

## 📞 Suporte

Para mais informações:
- Veja `FILTERS_AND_SEARCH_TESTS.md` para 50+ exemplos
- Veja `IMPLEMENTATION_SUMMARY.md` para detalhes técnicos
- Veja `POSTMAN_TESTS.md` para testes CRUD básicos

---

**Status:** ✅ Implementação Completa e Compilada com Sucesso!

Pronto para usar! 🚀

