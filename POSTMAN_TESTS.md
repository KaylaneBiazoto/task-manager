## 📮 Testes com Postman - CRUD de Tarefas

Abaixo estão as chamadas HTTP prontas para testar no Postman.

---

## 1️⃣ CRIAR TAREFA (POST)

**URL:** `http://localhost:8080/api/tasks`

**Method:** `POST`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "title": "Implementar autenticação JWT",
  "description": "Adicionar sistema de login com JWT tokens",
  "priority": "HIGH",
  "deadline": "2026-05-20",
  "projectId": 1,
  "assigneeId": 1
}
```

**Resposta (201 Created):**
```json
{
  "success": true,
  "message": "Task created successfully",
  "data": {
    "id": 1,
    "title": "Implementar autenticação JWT",
    "description": "Adicionar sistema de login com JWT tokens",
    "status": "TODO",
    "priority": "HIGH",
    "deadline": "2026-05-20",
    "projectId": 1,
    "assigneeId": 1,
    "assigneeName": "John Doe",
    "createdAt": "2026-04-18T16:30:00",
    "updatedAt": "2026-04-18T16:30:00"
  }
}
```

---

## 2️⃣ BUSCAR TAREFA POR ID (GET)

**URL:** `http://localhost:8080/api/tasks/1`

**Method:** `GET`

**Resposta (200 OK):**
```json
{
  "success": true,
  "message": "Task retrieved successfully",
  "data": {
    "id": 1,
    "title": "Implementar autenticação JWT",
    "description": "Adicionar sistema de login com JWT tokens",
    "status": "TODO",
    "priority": "HIGH",
    "deadline": "2026-05-20",
    "projectId": 1,
    "assigneeId": 1,
    "assigneeName": "John Doe",
    "createdAt": "2026-04-18T16:30:00",
    "updatedAt": "2026-04-18T16:30:00"
  }
}
```

---

## 3️⃣ LISTAR TAREFAS DO PROJETO (GET)

**URL:** `http://localhost:8080/api/tasks/project/1`

**Method:** `GET`

**Resposta (200 OK):**
```json
{
  "success": true,
  "message": "Tasks retrieved successfully",
  "data": [
    {
      "id": 1,
      "title": "Implementar autenticação JWT",
      "description": "Adicionar sistema de login com JWT tokens",
      "status": "TODO",
      "priority": "HIGH",
      "deadline": "2026-05-20",
      "projectId": 1,
      "assigneeId": 1,
      "assigneeName": "John Doe",
      "createdAt": "2026-04-18T16:30:00",
      "updatedAt": "2026-04-18T16:30:00"
    },
    {
      "id": 2,
      "title": "Criar banco de dados",
      "description": "Configurar PostgreSQL",
      "status": "IN_PROGRESS",
      "priority": "CRITICAL",
      "deadline": "2026-05-15",
      "projectId": 1,
      "assigneeId": 2,
      "assigneeName": "Jane Smith",
      "createdAt": "2026-04-17T10:15:00",
      "updatedAt": "2026-04-18T14:20:00"
    }
  ]
}
```

---

## 4️⃣ ATUALIZAR TAREFA (PUT)

**URL:** `http://localhost:8080/api/tasks/1`

**Method:** `PUT`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON) - Mudar para IN_PROGRESS:**
```json
{
  "status": "IN_PROGRESS"
}
```

**Resposta (200 OK):**
```json
{
  "success": true,
  "message": "Task updated successfully",
  "data": {
    "id": 1,
    "title": "Implementar autenticação JWT",
    "description": "Adicionar sistema de login com JWT tokens",
    "status": "IN_PROGRESS",
    "priority": "HIGH",
    "deadline": "2026-05-20",
    "projectId": 1,
    "assigneeId": 1,
    "assigneeName": "John Doe",
    "createdAt": "2026-04-18T16:30:00",
    "updatedAt": "2026-04-18T16:35:00"
  }
}
```

---

## 5️⃣ ATUALIZAR MÚLTIPLOS CAMPOS (PUT)

**URL:** `http://localhost:8080/api/tasks/1`

**Method:** `PUT`

**Body (JSON):**
```json
{
  "title": "Implementar autenticação OAuth 2.0",
  "priority": "CRITICAL",
  "status": "IN_PROGRESS",
  "deadline": "2026-05-10",
  "assigneeId": 2
}
```

**Resposta (200 OK):**
```json
{
  "success": true,
  "message": "Task updated successfully",
  "data": {
    "id": 1,
    "title": "Implementar autenticação OAuth 2.0",
    "description": "Adicionar sistema de login com JWT tokens",
    "status": "IN_PROGRESS",
    "priority": "CRITICAL",
    "deadline": "2026-05-10",
    "projectId": 1,
    "assigneeId": 2,
    "assigneeName": "Jane Smith",
    "createdAt": "2026-04-18T16:30:00",
    "updatedAt": "2026-04-18T16:40:00"
  }
}
```

---

## 6️⃣ DELETAR TAREFA (DELETE)

**URL:** `http://localhost:8080/api/tasks/1`

**Method:** `DELETE`

**Resposta (200 OK):**
```json
{
  "success": true,
  "message": "Task deleted successfully",
  "data": null
}
```

---

## ⚠️ TESTES DE VALIDAÇÕES E REGRAS DE NEGÓCIO

### Erro 1: Tarefa DONE voltando para TODO ❌

**URL:** `http://localhost:8080/api/tasks/2`

**Method:** `PUT`

**Body:**
```json
{
  "status": "TODO"
}
```

**Resposta (409 Conflict):**
```json
{
  "success": false,
  "message": "A task with DONE status cannot return to TODO. Only IN_PROGRESS is allowed.",
  "data": null
}
```

---

### Erro 2: MEMBER tentando fechar tarefa CRITICAL ❌

**URL:** `http://localhost:8080/api/tasks/3`

**Method:** `PUT`

**Body:**
```json
{
  "status": "DONE"
}
```

**Resposta (409 Conflict):**
```json
{
  "success": false,
  "message": "Only project ADMIN can mark CRITICAL priority tasks as DONE",
  "data": null
}
```

---

### Erro 3: Exceder WIP Limit (5 tarefas IN_PROGRESS) ❌

**URL:** `http://localhost:8080/api/tasks`

**Method:** `POST` (quando assignee já tem 5 tarefas IN_PROGRESS)

**Body:**
```json
{
  "title": "6ª Tarefa",
  "priority": "MEDIUM",
  "projectId": 1,
  "assigneeId": 1
}
```

**Resposta (409 Conflict):**
```json
{
  "success": false,
  "message": "Assignee has reached the maximum limit of 5 in-progress tasks (WIP limit exceeded)",
  "data": null
}
```

---

### Erro 4: Assignee não é membro do projeto ❌

**URL:** `http://localhost:8080/api/tasks`

**Method:** `POST`

**Body:**
```json
{
  "title": "Tarefa sem membro",
  "priority": "LOW",
  "projectId": 1,
  "assigneeId": 999
}
```

**Resposta (409 Conflict):**
```json
{
  "success": false,
  "message": "Assignee is not a member of the project",
  "data": null
}
```

---

### Erro 5: Projeto não encontrado ❌

**URL:** `http://localhost:8080/api/tasks`

**Method:** `POST`

**Body:**
```json
{
  "title": "Tarefa",
  "priority": "LOW",
  "projectId": 999,
  "assigneeId": 1
}
```

**Resposta (409 Conflict):**
```json
{
  "success": false,
  "message": "Project not found",
  "data": null
}
```

---

### Erro 6: Tarefa não encontrada ❌

**URL:** `http://localhost:8080/api/tasks/999`

**Method:** `GET`

**Resposta (404 Not Found):**
```json
{
  "success": false,
  "message": "Task not found",
  "data": null
}
```

---

## 📊 FLUXO DE TESTE COMPLETO

Siga esse fluxo para testar tudo:

1. **Criar tarefa** → `POST /api/tasks`
2. **Buscar tarefa** → `GET /api/tasks/1`
3. **Listar tarefas do projeto** → `GET /api/tasks/project/1`
4. **Atualizar para IN_PROGRESS** → `PUT /api/tasks/1` com `status: IN_PROGRESS`
5. **Atualizar para DONE** → `PUT /api/tasks/1` com `status: DONE`
6. **Tentar voltar para TODO** → `PUT /api/tasks/1` com `status: TODO` (deve falhar)
7. **Voltar para IN_PROGRESS** → `PUT /api/tasks/1` com `status: IN_PROGRESS` (deve funcionar)
8. **Deletar tarefa** → `DELETE /api/tasks/1`

---

## 🔧 DICAS IMPORTANTES

### 1. Verificar se o servidor está rodando
```
Acesse: http://localhost:8080/swagger-ui.html
(se houver Swagger configurado)
```

### 2. Variáveis úteis para reutilizar no Postman

Crie estas variáveis de ambiente no Postman:

```
BASE_URL = http://localhost:8080
PROJECT_ID = 1
TASK_ID = 1
ASSIGNEE_ID = 1
```

Depois use nas URLs:
```
{{BASE_URL}}/api/tasks
{{BASE_URL}}/api/tasks/{{TASK_ID}}
{{BASE_URL}}/api/tasks/project/{{PROJECT_ID}}
```

### 3. Ordem de Prioridades (para testes)

Sempre use:
- LOW
- MEDIUM
- HIGH
- CRITICAL

### 4. Status válidos

Sempre use:
- TODO
- IN_PROGRESS
- DONE

---

## ✅ CHECKLIST DE TESTES

- [ ] Criar tarefa simples
- [ ] Criar tarefa com todos os campos
- [ ] Buscar tarefa por ID
- [ ] Listar tarefas do projeto
- [ ] Atualizar título
- [ ] Atualizar status para IN_PROGRESS
- [ ] Atualizar status para DONE
- [ ] Tentar mudar DONE para TODO (deve falhar)
- [ ] Tentar mudar DONE para IN_PROGRESS (deve funcionar)
- [ ] Deletar tarefa
- [ ] Buscar tarefa deletada (deve retornar 404)
- [ ] Testar WIP limit com 6 tarefas
- [ ] Testar atribuição de tarefa a não-membro
- [ ] Testar tarefa CRITICAL com MEMBER
- [ ] Testar tarefa CRITICAL com ADMIN

