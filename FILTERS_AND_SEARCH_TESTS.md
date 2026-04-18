# 🔍 Testes com Postman - Listagem com Filtros e Busca Textual

Este documento descreve todos os endpoints de listagem com suporte a **filtros**, **busca textual** e **paginação** para o projeto Task Manager.

---

## 📚 Índice

1. [Usuários (Users)](#usuários-users)
2. [Projetos (Projects)](#projetos-projects)
3. [Tarefas (Tasks)](#tarefas-tasks)
4. [Exemplos Avançados](#exemplos-avançados)
5. [Parâmetros de Query](#parâmetros-de-query)
6. [Casos de Uso Comuns](#casos-de-uso-comuns)

---

## 👥 Usuários (Users)

### ✅ Listar todos os usuários com paginação

**URL:** `http://localhost:8080/api/users`

**Method:** `GET`

**Query Parameters:**
```
page=0
size=10
sort=username,asc
```

**Request Completo:**
```
GET http://localhost:8080/api/users?page=0&size=10&sort=username,asc
```

**Resposta (200 OK):**
```json
{
  "success": true,
  "message": "Users retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "username": "john.doe",
        "email": "john@example.com",
        "role": "ADMIN",
        "createdAt": "2026-04-18T10:00:00Z",
        "updatedAt": "2026-04-18T10:00:00Z"
      },
      {
        "id": 2,
        "username": "jane.smith",
        "email": "jane@example.com",
        "role": "MEMBER",
        "createdAt": "2026-04-17T15:30:00Z",
        "updatedAt": "2026-04-18T09:00:00Z"
      }
    ],
    "pageable": {...},
    "totalElements": 2,
    "totalPages": 1,
    ...
  }
}
```

---

### 🔎 Buscar usuários por texto (username ou email)

**URL:** `http://localhost:8080/api/users?search=john`

**Method:** `GET`

**Query Parameters:**
```
search=john
page=0
size=10
sort=username,asc
```

**Request Completo:**
```
GET http://localhost:8080/api/users?search=john&page=0&size=10&sort=username,asc
```

**Caso de Uso:** Encontra usuários que contenham "john" no username ou email (case-insensitive).

---

### 🏷️ Filtrar usuários por role

**URL:** `http://localhost:8080/api/users?role=ADMIN`

**Method:** `GET`

**Query Parameters:**
```
role=ADMIN
page=0
size=10
sort=createdAt,desc
```

**Request Completo:**
```
GET http://localhost:8080/api/users?role=ADMIN&page=0&size=10&sort=createdAt,desc
```

**Valores válidos para role:**
- `ADMIN`
- `MEMBER`

---

### 🔀 Filtro combinado: busca + role

**URL:** `http://localhost:8080/api/users?search=john&role=ADMIN`

**Method:** `GET`

**Request Completo:**
```
GET http://localhost:8080/api/users?search=john&role=ADMIN&page=0&size=10&sort=username,asc
```

**Resultado:** Usuários ADMIN que contenham "john" no username ou email.

---

## 📁 Projetos (Projects)

### ✅ Listar todos os projetos com paginação

**URL:** `http://localhost:8080/api/projects`

**Method:** `GET`

**Query Parameters:**
```
page=0
size=10
sort=name,asc
```

**Request Completo:**
```
GET http://localhost:8080/api/projects?page=0&size=10&sort=name,asc
```

---

### 🔎 Buscar projetos por nome ou descrição

**URL:** `http://localhost:8080/api/projects?search=Backend`

**Method:** `GET`

**Request Completo:**
```
GET http://localhost:8080/api/projects?search=Backend&page=0&size=10&sort=createdAt,desc
```

**Caso de Uso:** Encontra projetos que contenham "Backend" no nome ou descrição.

---

### 👤 Filtrar projetos por proprietário

**URL:** `http://localhost:8080/api/projects?ownerId=1`

**Method:** `GET`

**Query Parameters:**
```
ownerId=1
page=0
size=10
sort=name,asc
```

**Request Completo:**
```
GET http://localhost:8080/api/projects?ownerId=1&page=0&size=10&sort=name,asc
```

**Caso de Uso:** Lista todos os projetos de um proprietário específico.

---

### 🔀 Filtro combinado: busca + proprietário

**URL:** `http://localhost:8080/api/projects?search=API&ownerId=1`

**Method:** `GET`

**Request Completo:**
```
GET http://localhost:8080/api/projects?search=API&ownerId=1&page=0&size=10&sort=name,asc
```

---

## ✅ Tarefas (Tasks)

### ✅ Listar tarefas de um projeto com paginação

**URL:** `http://localhost:8080/api/tasks/project/1`

**Method:** `GET`

**Query Parameters:**
```
page=0
size=10
sort=createdAt,desc
```

**Request Completo:**
```
GET http://localhost:8080/api/tasks/project/1?page=0&size=10&sort=createdAt,desc
```

---

### 🔎 Buscar tarefas por título ou descrição

**URL:** `http://localhost:8080/api/tasks/project/1?search=autenticação`

**Method:** `GET`

**Request Completo:**
```
GET http://localhost:8080/api/tasks/project/1?search=autenticação&page=0&size=10&sort=createdAt,desc
```

---

### 🏷️ Filtrar tarefas por status

**URL:** `http://localhost:8080/api/tasks/project/1?status=IN_PROGRESS`

**Method:** `GET`

**Query Parameters:**
```
status=IN_PROGRESS
page=0
size=10
sort=priority,desc
```

**Request Completo:**
```
GET http://localhost:8080/api/tasks/project/1?status=IN_PROGRESS&page=0&size=10&sort=priority,desc
```

**Valores válidos para status:**
- `TODO`
- `IN_PROGRESS`
- `DONE`

---

### ⚡ Filtrar tarefas por prioridade

**URL:** `http://localhost:8080/api/tasks/project/1?priority=HIGH`

**Method:** `GET`

**Query Parameters:**
```
priority=HIGH
page=0
size=10
sort=deadline,asc
```

**Request Completo:**
```
GET http://localhost:8080/api/tasks/project/1?priority=HIGH&page=0&size=10&sort=deadline,asc
```

**Valores válidos para priority:**
- `LOW`
- `MEDIUM`
- `HIGH`
- `CRITICAL`

---

### 👤 Filtrar tarefas por responsável

**URL:** `http://localhost:8080/api/tasks/project/1?assigneeId=1`

**Method:** `GET`

**Query Parameters:**
```
assigneeId=1
page=0
size=10
sort=createdAt,desc
```

**Request Completo:**
```
GET http://localhost:8080/api/tasks/project/1?assigneeId=1&page=0&size=10&sort=createdAt,desc
```

---

### 🔀 Filtros combinados: status + prioridade + busca

**URL:** `http://localhost:8080/api/tasks/project/1?status=IN_PROGRESS&priority=HIGH&search=JWT`

**Method:** `GET`

**Request Completo:**
```
GET http://localhost:8080/api/tasks/project/1?status=IN_PROGRESS&priority=HIGH&search=JWT&page=0&size=10&sort=deadline,asc
```

---

### 🔀 Filtros avançados: todas as opções

**URL:** `http://localhost:8080/api/tasks/project/1?status=IN_PROGRESS&priority=HIGH&assigneeId=1&search=autenticação`

**Method:** `GET`

**Request Completo:**
```
GET http://localhost:8080/api/tasks/project/1?status=IN_PROGRESS&priority=HIGH&assigneeId=1&search=autenticação&page=0&size=20&sort=deadline,asc
```

---

## 📊 Exemplos Avançados

### Exemplo 1: Usuários MEMBER criados no último mês, 2ª página

```
GET http://localhost:8080/api/users?role=MEMBER&page=1&size=10&sort=createdAt,desc
```

---

### Exemplo 2: Projetos contendo "API" no nome, ordenados por proprietário

```
GET http://localhost:8080/api/projects?search=API&page=0&size=5&sort=ownerId,asc
```

---

### Exemplo 3: Tarefas de alta prioridade em andamento, 15 por página

```
GET http://localhost:8080/api/tasks/project/1?priority=HIGH&status=IN_PROGRESS&page=0&size=15
```

---

### Exemplo 4: Tarefas atribuídas a um usuário, ordenadas por prazo

```
GET http://localhost:8080/api/tasks/project/1?assigneeId=2&page=0&size=10&sort=deadline,asc
```

---

### Exemplo 5: Busca textual com paginação

```
GET http://localhost:8080/api/users?search=example.com&page=0&size=10&sort=username,asc
```

---

## 🎯 Parâmetros de Query

### Paginação

| Parâmetro | Tipo    | Padrão | Descrição                    |
|-----------|---------|--------|------------------------------|
| `page`    | Integer | 0      | Número da página (começa em 0) |
| `size`    | Integer | 10     | Quantidade de itens por página |

### Ordenação (Sort)

| Parâmetro | Formato           | Exemplo                | Descrição                           |
|-----------|-------------------|------------------------|-------------------------------------|
| `sort`    | field,direction   | `username,asc`         | Campo e direção (asc/desc)         |
|           |                   | `createdAt,desc`       | Múltiplas ordenações separadas por `&` |

**Campos válidos por endpoint:**

**Users:**
- `id`
- `username`
- `email`
- `role`
- `createdAt`
- `updatedAt`

**Projects:**
- `id`
- `name`
- `description`
- `ownerId`
- `createdAt`
- `updatedAt`

**Tasks:**
- `id`
- `title`
- `description`
- `status`
- `priority`
- `deadline`
- `assigneeId`
- `createdAt`
- `updatedAt`

### Filtros Específicos

#### Users

| Parâmetro | Tipo   | Exemplo      | Descrição                        |
|-----------|--------|--------------|----------------------------------|
| `search`  | String | `john`       | Busca em username e email       |
| `role`    | String | `ADMIN`      | Filtro por role (ADMIN, MEMBER) |

#### Projects

| Parâmetro  | Tipo   | Exemplo | Descrição                       |
|------------|--------|---------|----------------------------------|
| `search`   | String | `API`   | Busca em nome e descrição       |
| `ownerId`  | Long   | `1`     | Filtro por proprietário (ID)    |

#### Tasks

| Parâmetro    | Tipo   | Exemplo      | Descrição                                 |
|--------------|--------|--------------|-------------------------------------------|
| `search`     | String | `JWT`        | Busca em título e descrição               |
| `status`     | String | `IN_PROGRESS`| Filtro por status (TODO, IN_PROGRESS, DONE) |
| `priority`   | String | `HIGH`       | Filtro por prioridade (LOW, MEDIUM, HIGH, CRITICAL) |
| `assigneeId` | Long   | `2`          | Filtro por responsável (ID do usuário)    |

---

## 🎯 Casos de Uso Comuns

### 1. "Mostrar minhas tarefas críticas em andamento"

```
GET http://localhost:8080/api/tasks/project/1?priority=CRITICAL&status=IN_PROGRESS&assigneeId=1
```

---

### 2. "Listar todos os ADMINs do sistema"

```
GET http://localhost:8080/api/users?role=ADMIN&page=0&size=50
```

---

### 3. "Buscar projetos que contenham 'Mobile' no nome"

```
GET http://localhost:8080/api/projects?search=Mobile&sort=createdAt,desc
```

---

### 4. "Paginação grande para relatório: 100 itens por página"

```
GET http://localhost:8080/api/users?page=0&size=100&sort=username,asc
```

---

### 5. "Tarefas atrasadas (com deadline próximo)"

```
GET http://localhost:8080/api/tasks/project/1?status=IN_PROGRESS&sort=deadline,asc
```

---

### 6. "Todas as tarefas DONE que não foram atribuídas"

```
GET http://localhost:8080/api/tasks/project/1?status=DONE&sort=createdAt,desc
```

---

### 7. "Buscar por email parcial"

```
GET http://localhost:8080/api/users?search=@gmail.com&page=0&size=10
```

---

### 8. "Tarefas MÉDIAS para um projeto específico"

```
GET http://localhost:8080/api/tasks/project/2?priority=MEDIUM&page=0&size=20&sort=deadline,asc
```

---

## 🔧 Dicas para Testes no Postman

### 1. Criar coleção e variáveis de ambiente

Crie estas variáveis no Postman:

```json
{
  "BASE_URL": "http://localhost:8080",
  "PROJECT_ID": "1",
  "USER_ID": "1",
  "ASSIGNEE_ID": "2",
  "ADMIN_ROLE": "ADMIN",
  "MEMBER_ROLE": "MEMBER",
  "TASK_STATUS_TODO": "TODO",
  "TASK_STATUS_IN_PROGRESS": "IN_PROGRESS",
  "TASK_STATUS_DONE": "DONE",
  "TASK_PRIORITY_LOW": "LOW",
  "TASK_PRIORITY_MEDIUM": "MEDIUM",
  "TASK_PRIORITY_HIGH": "HIGH",
  "TASK_PRIORITY_CRITICAL": "CRITICAL"
}
```

### 2. Usar variáveis em URLs

```
{{BASE_URL}}/api/users?role={{ADMIN_ROLE}}&page=0&size=10
{{BASE_URL}}/api/tasks/project/{{PROJECT_ID}}?priority={{TASK_PRIORITY_CRITICAL}}
```

### 3. Testar cada filtro isoladamente

- Primeiro teste sem filtros
- Depois teste cada filtro individualmente
- Depois combine filtros

### 4. Verificar respostas de erro

Se algum parâmetro for inválido, a API retornará status 400 com erro.

---

## 📝 Checklist de Testes

### Users
- [ ] Listar sem filtros
- [ ] Buscar por username
- [ ] Buscar por email
- [ ] Filtrar por role=ADMIN
- [ ] Filtrar por role=MEMBER
- [ ] Combinar search + role
- [ ] Testar diferentes sort orders
- [ ] Testar múltiplas páginas

### Projects
- [ ] Listar sem filtros
- [ ] Buscar por nome
- [ ] Buscar por descrição
- [ ] Filtrar por ownerId
- [ ] Combinar search + ownerId
- [ ] Ordenar por criação (desc)
- [ ] Testar paginação

### Tasks
- [ ] Listar todas as tarefas do projeto
- [ ] Buscar por título
- [ ] Buscar por descrição
- [ ] Filtrar por status=TODO
- [ ] Filtrar por status=IN_PROGRESS
- [ ] Filtrar por status=DONE
- [ ] Filtrar por priority=CRITICAL
- [ ] Filtrar por priority=HIGH
- [ ] Filtrar por priority=MEDIUM
- [ ] Filtrar por priority=LOW
- [ ] Filtrar por assigneeId
- [ ] Combinar status + priority
- [ ] Combinar priority + assigneeId
- [ ] Combinar search + status + priority + assigneeId
- [ ] Testar sort por deadline
- [ ] Testar paginação (múltiplas páginas)

---

## ⚠️ Notas Importantes

1. **Case-sensitive**: Valores de enum (status, priority, role) devem estar em **UPPERCASE**
2. **Busca**: A busca textual é **case-insensitive**
3. **Valores nulos**: Se um filtro não for fornecido, é ignorado
4. **Paginação padrão**: Se não especificar `page` e `size`, usa 0 e 10
5. **Sort padrão**: Se não especificar `sort`, retorna sem ordenação garantida
6. **Filtros combinados**: Todos funcionam com operador AND (não OR)
7. **ZonedDateTime**: Todas as datas retornam em formato ISO 8601 com timezone

---

## 🚀 Próximos Passos

Agora que os filtros e busca estão implementados:

1. **Faça testes** usando o Postman com os exemplos acima
2. **Implemente paginação no frontend** se necessário
3. **Adicione validações** de entrada para parâmetros inválidos
4. **Implemente cache** para melhorar performance em buscas frequentes

---

Pronto! Seu projeto agora tem listagem completa com filtros e busca textual! 🎉

