## 🗺️ ROADMAP DE TESTES - Sistema Completo

Guia passo a passo para testar a integração completa: **Usuários → Projetos → Tarefas**

---

## 📋 ÍNDICE

1. [Setup Inicial](#setup-inicial)
2. [Fase 1: Criar Usuários](#fase-1-criar-usuários)
3. [Fase 2: Criar Projetos](#fase-2-criar-projetos)
4. [Fase 3: Adicionar Membros ao Projeto](#fase-3-adicionar-membros-ao-projeto)
5. [Fase 4: Criar Tarefas](#fase-4-criar-tarefas)
6. [Fase 5: Gerenciar Tarefas](#fase-5-gerenciar-tarefas)
7. [Fluxo Completo Integrado](#fluxo-completo-integrado)
8. [Checklist Final](#checklist-final)

---

## Setup Inicial

Antes de começar os testes:

### ✅ Pré-requisitos

1. **Servidor rodando**
   ```bash
   cd /home/kaylane.biazoto@db1.com.br/Music/task-manager-backend
   JAVA_HOME=/home/kaylane.biazoto@db1.com.br/.sdkman/candidates/java/21.0.10-zulu mvn spring-boot:run
   ```

2. **Postman ou cURL disponível**
   - Postman: https://www.postman.com/downloads/
   - ou usar cURL no terminal

3. **Base URL**
   ```
   http://localhost:8080
   ```

---

## 🟢 FASE 1: Criar Usuários

### Objetivo
Criar 3 usuários para usar nos testes posteriores

### 1.1 Criar Usuário ADMIN (Dono do Projeto)

**Request:**
```
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "username": "alice_admin",
  "email": "alice@example.com",
  "password": "alice123",
  "role": "ADMIN"
}
```

**Resposta Esperada (201 Created):**
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "id": 5,
    "username": "alice_admin",
    "email": "alice@example.com",
    "role": "ADMIN",
    "createdAt": "2026-04-18T17:45:00.123456",
    "updatedAt": "2026-04-18T17:45:00.123456"
  }
}
```

**📌 Salvar:** `ADMIN_USER_ID = 5`

---

### 1.2 Criar Usuário MEMBER (Membro do Projeto)

**Request:**
```
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "username": "bob_member",
  "email": "bob@example.com",
  "password": "bob123"
}
```

**Resposta Esperada (201 Created):**
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "id": 6,
    "username": "bob_member",
    "email": "bob@example.com",
    "role": "MEMBER",
    "createdAt": "2026-04-18T17:45:30.654321",
    "updatedAt": "2026-04-18T17:45:30.654321"
  }
}
```

**📌 Salvar:** `MEMBER_USER_ID = 6`

---

### 1.3 Criar Outro Usuário MEMBER (Segundo Membro)

**Request:**
```
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "username": "charlie_member",
  "email": "charlie@example.com",
  "password": "charlie123"
}
```

**Resposta Esperada (201 Created):**
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "id": 7,
    "username": "charlie_member",
    "email": "charlie@example.com",
    "role": "MEMBER",
    "createdAt": "2026-04-18T17:45:45.987654",
    "updatedAt": "2026-04-18T17:45:45.987654"
  }
}
```

**📌 Salvar:** `MEMBER_USER_ID_2 = 7`

---

### ✅ Fase 1 Completa

Você agora tem:
- **Alice (ID: 5)** - ADMIN
- **Bob (ID: 6)** - MEMBER
- **Charlie (ID: 7)** - MEMBER

---

## 🔵 FASE 2: Criar Projetos

### Objetivo
Criar projetos que serão proprietários de tarefas

> **⚠️ NOTA:** Atualmente o endpoint de criação de projeto usa `ownerId = 1L` hardcoded. Você pode:
> 1. Usar o projeto criado automaticamente
> 2. Ou ajustar o Controller para usar o ADMIN_USER_ID

### 2.1 Criar Projeto 1 (Website Redesign)

**Request:**
```
POST http://localhost:8080/api/projects
Content-Type: application/json

{
  "name": "Website Redesign",
  "description": "Redesenhar o website da empresa com novo design"
}
```

**Resposta Esperada (201 Created):**
```json
{
  "success": true,
  "message": "Project created successfully",
  "data": {
    "id": 1,
    "name": "Website Redesign",
    "description": "Redesenhar o website da empresa com novo design",
    "ownerId": 1,
    "ownerName": "admin",
    "active": true,
    "members": [
      {
        "id": 1,
        "userId": 1,
        "username": "admin",
        "projectRole": "ADMIN",
        "active": true
      }
    ],
    "createdAt": "2026-04-18T17:46:00.123456",
    "updatedAt": "2026-04-18T17:46:00.123456"
  }
}
```

**📌 Salvar:** `PROJECT_ID = 1`

---

### 2.2 Criar Projeto 2 (Mobile App)

**Request:**
```
POST http://localhost:8080/api/projects
Content-Type: application/json

{
  "name": "Mobile App Development",
  "description": "Desenvolver aplicativo mobile para iOS e Android"
}
```

**Resposta Esperada (201 Created):**
```json
{
  "success": true,
  "message": "Project created successfully",
  "data": {
    "id": 2,
    "name": "Mobile App Development",
    "description": "Desenvolver aplicativo mobile para iOS e Android",
    "ownerId": 1,
    "ownerName": "admin",
    "active": true,
    "members": [
      {
        "id": 2,
        "userId": 1,
        "username": "admin",
        "projectRole": "ADMIN",
        "active": true
      }
    ],
    "createdAt": "2026-04-18T17:46:30.654321",
    "updatedAt": "2026-04-18T17:46:30.654321"
  }
}
```

**📌 Salvar:** `PROJECT_ID_2 = 2`

---

### ✅ Fase 2 Completa

Você agora tem:
- **Projeto 1 (ID: 1)** - Website Redesign (Admin: user 1)
- **Projeto 2 (ID: 2)** - Mobile App (Admin: user 1)

---

## 🟣 FASE 3: Adicionar Membros ao Projeto

### Objetivo
Adicionar os usuários criados na Fase 1 aos projetos

### 3.1 Adicionar Bob (MEMBER) ao Projeto 1

**Request:**
```
POST http://localhost:8080/api/projects/1/members
Content-Type: application/json

{
  "userId": 6,
  "projectRole": "MEMBER"
}
```

**Resposta Esperada (201 Created):**
```json
{
  "success": true,
  "message": "Member added successfully",
  "data": {
    "id": 2,
    "userId": 6,
    "username": "bob_member",
    "projectRole": "MEMBER",
    "active": true
  }
}
```

---

### 3.2 Adicionar Charlie (MEMBER) ao Projeto 1

**Request:**
```
POST http://localhost:8080/api/projects/1/members
Content-Type: application/json

{
  "userId": 7,
  "projectRole": "MEMBER"
}
```

**Resposta Esperada (201 Created):**
```json
{
  "success": true,
  "message": "Member added successfully",
  "data": {
    "id": 3,
    "userId": 7,
    "username": "charlie_member",
    "projectRole": "MEMBER",
    "active": true
  }
}
```

---

### 3.3 Adicionar Bob ao Projeto 2

**Request:**
```
POST http://localhost:8080/api/projects/2/members
Content-Type: application/json

{
  "userId": 6,
  "projectRole": "MEMBER"
}
```

**Resposta Esperada (201 Created):**
```json
{
  "success": true,
  "message": "Member added successfully",
  "data": {
    "id": 4,
    "userId": 6,
    "username": "bob_member",
    "projectRole": "MEMBER",
    "active": true
  }
}
```

---

### ✅ Fase 3 Completa

Estrutura agora:
```
Projeto 1 (Website Redesign)
├── Admin: user 1
├── Bob (user 6) - MEMBER
└── Charlie (user 7) - MEMBER

Projeto 2 (Mobile App)
├── Admin: user 1
└── Bob (user 6) - MEMBER
```

---

## 🟠 FASE 4: Criar Tarefas

### Objetivo
Criar tarefas dentro dos projetos com diferentes prioridades e assignees

### 4.1 Criar Tarefa 1 - Design (Projeto 1, Assignee: Bob)

**Request:**
```
POST http://localhost:8080/api/tasks
Content-Type: application/json

{
  "title": "Criar mockups do novo design",
  "description": "Fazer wireframes e mockups do novo layout",
  "priority": "HIGH",
  "status": "TODO",
  "deadline": "2026-05-10",
  "projectId": 1,
  "assigneeId": 6
}
```

**Resposta Esperada (201 Created):**
```json
{
  "success": true,
  "message": "Task created successfully",
  "data": {
    "id": 1,
    "title": "Criar mockups do novo design",
    "description": "Fazer wireframes e mockups do novo layout",
    "status": "TODO",
    "priority": "HIGH",
    "deadline": "2026-05-10",
    "projectId": 1,
    "assigneeId": 6,
    "assigneeName": "bob_member",
    "createdAt": "2026-04-18T17:47:00.123456",
    "updatedAt": "2026-04-18T17:47:00.123456"
  }
}
```

**📌 Salvar:** `TASK_ID_1 = 1`

---

### 4.2 Criar Tarefa 2 - Frontend (Projeto 1, Assignee: Charlie)

**Request:**
```
POST http://localhost:8080/api/tasks
Content-Type: application/json

{
  "title": "Implementar frontend com React",
  "description": "Converter mockups para componentes React",
  "priority": "CRITICAL",
  "status": "TODO",
  "deadline": "2026-05-20",
  "projectId": 1,
  "assigneeId": 7
}
```

**Resposta Esperada (201 Created):**
```json
{
  "success": true,
  "message": "Task created successfully",
  "data": {
    "id": 2,
    "title": "Implementar frontend com React",
    "description": "Converter mockups para componentes React",
    "status": "TODO",
    "priority": "CRITICAL",
    "deadline": "2026-05-20",
    "projectId": 1,
    "assigneeId": 7,
    "assigneeName": "charlie_member",
    "createdAt": "2026-04-18T17:47:30.654321",
    "updatedAt": "2026-04-18T17:47:30.654321"
  }
}
```

**📌 Salvar:** `TASK_ID_2 = 2`

---

### 4.3 Criar Tarefa 3 - Backend (Projeto 1, Assignee: Bob)

**Request:**
```
POST http://localhost:8080/api/tasks
Content-Type: application/json

{
  "title": "Atualizar API REST",
  "description": "Refatorar endpoints da API para melhor performance",
  "priority": "MEDIUM",
  "status": "TODO",
  "deadline": "2026-05-25",
  "projectId": 1,
  "assigneeId": 6
}
```

**Resposta Esperada (201 Created):**
```json
{
  "success": true,
  "message": "Task created successfully",
  "data": {
    "id": 3,
    "title": "Atualizar API REST",
    "description": "Refatorar endpoints da API para melhor performance",
    "status": "TODO",
    "priority": "MEDIUM",
    "deadline": "2026-05-25",
    "projectId": 1,
    "assigneeId": 6,
    "assigneeName": "bob_member",
    "createdAt": "2026-04-18T17:48:00.987654",
    "updatedAt": "2026-04-18T17:48:00.987654"
  }
}
```

**📌 Salvar:** `TASK_ID_3 = 3`

---

### 4.4 Criar Tarefa 4 - Projeto 2 (Mobile, Assignee: Bob)

**Request:**
```
POST http://localhost:8080/api/tasks
Content-Type: application/json

{
  "title": "Configurar ambiente React Native",
  "description": "Setup inicial do projeto React Native",
  "priority": "HIGH",
  "status": "TODO",
  "deadline": "2026-05-05",
  "projectId": 2,
  "assigneeId": 6
}
```

**Resposta Esperada (201 Created):**
```json
{
  "success": true,
  "message": "Task created successfully",
  "data": {
    "id": 4,
    "title": "Configurar ambiente React Native",
    "description": "Setup inicial do projeto React Native",
    "status": "TODO",
    "priority": "HIGH",
    "deadline": "2026-05-05",
    "projectId": 2,
    "assigneeId": 6,
    "assigneeName": "bob_member",
    "createdAt": "2026-04-18T17:48:30.123456",
    "updatedAt": "2026-04-18T17:48:30.123456"
  }
}
```

**📌 Salvar:** `TASK_ID_4 = 4`

---

### ✅ Fase 4 Completa

Você agora tem 4 tarefas distribuídas:
```
Projeto 1 (Website Redesign)
├── Task 1 (Design) - Assignee: Bob - HIGH
├── Task 2 (Frontend) - Assignee: Charlie - CRITICAL
└── Task 3 (Backend) - Assignee: Bob - MEDIUM

Projeto 2 (Mobile App)
└── Task 4 (Setup) - Assignee: Bob - HIGH
```

---

## 🟡 FASE 5: Gerenciar Tarefas

### Objetivo
Testar transições de status e validações de negócio

### 5.1 Mover Tarefa para IN_PROGRESS

**Request:**
```
PUT http://localhost:8080/api/tasks/1
Content-Type: application/json

{
  "status": "IN_PROGRESS"
}
```

**Resposta Esperada (200 OK):**
```json
{
  "success": true,
  "message": "Task updated successfully",
  "data": {
    "id": 1,
    "title": "Criar mockups do novo design",
    "description": "Fazer wireframes e mockups do novo layout",
    "status": "IN_PROGRESS",
    "priority": "HIGH",
    "deadline": "2026-05-10",
    "projectId": 1,
    "assigneeId": 6,
    "assigneeName": "bob_member",
    "createdAt": "2026-04-18T17:47:00.123456",
    "updatedAt": "2026-04-18T17:50:00.123456"
  }
}
```

---

### 5.2 Mover Tarefa para DONE

**Request:**
```
PUT http://localhost:8080/api/tasks/1
Content-Type: application/json

{
  "status": "DONE"
}
```

**Resposta Esperada (200 OK):**
```json
{
  "success": true,
  "message": "Task updated successfully",
  "data": {
    "id": 1,
    "title": "Criar mockups do novo design",
    "description": "Fazer wireframes e mockups do novo layout",
    "status": "DONE",
    "priority": "HIGH",
    "deadline": "2026-05-10",
    "projectId": 1,
    "assigneeId": 6,
    "assigneeName": "bob_member",
    "createdAt": "2026-04-18T17:47:00.123456",
    "updatedAt": "2026-04-18T17:51:00.654321"
  }
}
```

---

### 5.3 Voltar Tarefa DONE para IN_PROGRESS (Permitido)

**Request:**
```
PUT http://localhost:8080/api/tasks/1
Content-Type: application/json

{
  "status": "IN_PROGRESS"
}
```

**Resposta Esperada (200 OK):**
```json
{
  "success": true,
  "message": "Task updated successfully",
  "data": {
    "id": 1,
    "status": "IN_PROGRESS",
    ...
  }
}
```

---

### ❌ 5.4 Tentar Voltar Tarefa DONE para TODO (NÃO Permitido)

**Request:**
```
PUT http://localhost:8080/api/tasks/1
Content-Type: application/json

{
  "status": "TODO"
}
```

**Resposta Esperada (409 Conflict):**
```json
{
  "success": false,
  "message": "A task with DONE status cannot return to TODO. Only IN_PROGRESS is allowed.",
  "data": null
}
```

✅ **Validação funcionando!**

---

### ❌ 5.5 Tentar Fechar Tarefa CRITICAL com MEMBER (NÃO Permitido)

**Primeiro, mover Task 2 (CRITICAL) para IN_PROGRESS:**
```
PUT http://localhost:8080/api/tasks/2
Content-Type: application/json

{
  "status": "IN_PROGRESS"
}
```

**Depois, tentar marcar como DONE (deve falhar):**
```
PUT http://localhost:8080/api/tasks/2
Content-Type: application/json

{
  "status": "DONE"
}
```

**Resposta Esperada (409 Conflict):**
```json
{
  "success": false,
  "message": "Only project ADMIN can mark CRITICAL priority tasks as DONE",
  "data": null
}
```

✅ **Validação de CRITICAL tasks funcionando!**

---

### 5.6 Listar Tarefas do Projeto

**Request:**
```
GET http://localhost:8080/api/tasks/project/1
```

**Resposta Esperada (200 OK):**
```json
{
  "success": true,
  "message": "Tasks retrieved successfully",
  "data": [
    {
      "id": 1,
      "title": "Criar mockups do novo design",
      "status": "IN_PROGRESS",
      "priority": "HIGH",
      "assigneeId": 6,
      "assigneeName": "bob_member",
      ...
    },
    {
      "id": 2,
      "title": "Implementar frontend com React",
      "status": "IN_PROGRESS",
      "priority": "CRITICAL",
      "assigneeId": 7,
      "assigneeName": "charlie_member",
      ...
    },
    {
      "id": 3,
      "title": "Atualizar API REST",
      "status": "TODO",
      "priority": "MEDIUM",
      "assigneeId": 6,
      "assigneeName": "bob_member",
      ...
    }
  ]
}
```

---

### ✅ Fase 5 Completa

Você testou:
- ✅ Transição TODO → IN_PROGRESS
- ✅ Transição IN_PROGRESS → DONE
- ✅ Transição DONE → IN_PROGRESS (permitido)
- ✅ Tentativa DONE → TODO (rejeitado)
- ✅ Tentativa MEMBER fechar CRITICAL (rejeitado)
- ✅ Listar tarefas do projeto

---

## 🚀 Fluxo Completo Integrado

### Sequência Recomendada de Testes (30-45 minutos)

```
TEMPO  │ AÇÃO                              │ ENDPOINT
───────┼──────────────────────────────────┼─────────────────────────
0:00   │ Criar Alice (ADMIN)              │ POST /api/users
       │ Criar Bob (MEMBER)               │ POST /api/users
       │ Criar Charlie (MEMBER)           │ POST /api/users
───────┼──────────────────────────────────┼─────────────────────────
5:00   │ Listar usuários criados          │ GET /api/users
───────┼──────────────────────────────────┼─────────────────────────
7:00   │ Criar Projeto 1 (Website)        │ POST /api/projects
       │ Criar Projeto 2 (Mobile)         │ POST /api/projects
───────┼──────────────────────────────────┼─────────────────────────
10:00  │ Adicionar Bob ao Projeto 1       │ POST /api/projects/1/members
       │ Adicionar Charlie ao Projeto 1   │ POST /api/projects/1/members
       │ Adicionar Bob ao Projeto 2       │ POST /api/projects/2/members
───────┼──────────────────────────────────┼─────────────────────────
15:00  │ Listar membros do Projeto 1      │ GET /api/projects/1/members
───────┼──────────────────────────────────┼─────────────────────────
17:00  │ Criar Task 1 (Design, Bob)       │ POST /api/tasks
       │ Criar Task 2 (Frontend, Charlie) │ POST /api/tasks
       │ Criar Task 3 (Backend, Bob)      │ POST /api/tasks
       │ Criar Task 4 (Setup, Bob)        │ POST /api/tasks
───────┼──────────────────────────────────┼─────────────────────────
22:00  │ Listar tasks Projeto 1           │ GET /api/tasks/project/1
───────┼──────────────────────────────────┼─────────────────────────
24:00  │ Task 1: TODO → IN_PROGRESS       │ PUT /api/tasks/1
       │ Task 1: IN_PROGRESS → DONE       │ PUT /api/tasks/1
───────┼──────────────────────────────────┼─────────────────────────
27:00  │ Task 1: DONE → IN_PROGRESS (OK)  │ PUT /api/tasks/1
───────┼──────────────────────────────────┼─────────────────────────
29:00  │ Task 1: DONE → TODO (ERRO)       │ PUT /api/tasks/1
───────┼──────────────────────────────────┼─────────────────────────
31:00  │ Task 2: IN_PROGRESS (esperado)   │ PUT /api/tasks/2
       │ Task 2: DONE (ERRO - CRITICAL)   │ PUT /api/tasks/2
───────┼──────────────────────────────────┼─────────────────────────
35:00  │ Obter Task 1 por ID              │ GET /api/tasks/1
       │ Deletar Task 3                   │ DELETE /api/tasks/3
───────┼──────────────────────────────────┼─────────────────────────
38:00  │ TESTES COMPLETOS ✅              │
```

---

## ✅ Checklist Final

### Usuários
- [ ] Criar Alice ADMIN
- [ ] Criar Bob MEMBER
- [ ] Criar Charlie MEMBER
- [ ] Listar 3 usuários

### Projetos
- [ ] Criar Projeto 1 (Website)
- [ ] Criar Projeto 2 (Mobile)
- [ ] Adicionar Bob ao Projeto 1
- [ ] Adicionar Charlie ao Projeto 1
- [ ] Adicionar Bob ao Projeto 2
- [ ] Listar membros de cada projeto

### Tarefas - Criação
- [ ] Criar Task 1 (Design, Bob, HIGH)
- [ ] Criar Task 2 (Frontend, Charlie, CRITICAL)
- [ ] Criar Task 3 (Backend, Bob, MEDIUM)
- [ ] Criar Task 4 (Setup, Bob, HIGH - Projeto 2)
- [ ] Listar tasks do Projeto 1 (deve ter 3)
- [ ] Listar tasks do Projeto 2 (deve ter 1)

### Tarefas - Transições
- [ ] Task 1: TODO → IN_PROGRESS
- [ ] Task 1: IN_PROGRESS → DONE
- [ ] Task 1: DONE → IN_PROGRESS (OK)
- [ ] Task 1: DONE → TODO (ERRO)
- [ ] Verificar mensagem de erro

### Tarefas - Validações
- [ ] Task 2: IN_PROGRESS (preparar para teste)
- [ ] Task 2: DONE com MEMBER (ERRO)
- [ ] Verificar mensagem: "Only project ADMIN can mark CRITICAL priority tasks as DONE"

### Tarefas - Gerenciamento
- [ ] Obter Task 1 por ID
- [ ] Atualizar Task 3 (mudar título ou prioridade)
- [ ] Deletar Task 3
- [ ] Tentar obter Task 3 deletada (404)

### Validações Adicionais
- [ ] Criar task com assignee não-membro (ERRO)
- [ ] Criar task em projeto inexistente (ERRO)
- [ ] Atualizar task inexistente (ERRO)

---

## 📊 Resumo do Que Será Testado

### Módulo Usuários ✅
- [x] CRUD completo
- [x] Validações de email/username únicos
- [x] Suporte a roles ADMIN/MEMBER

### Módulo Projetos ✅
- [x] CRUD completo
- [x] Adicionar/remover membros
- [x] Associação com proprietário

### Módulo Tarefas ✅
- [x] CRUD completo
- [x] Transições de status (com validações)
- [x] Regra: DONE não volta a TODO
- [x] Regra: CRITICAL só ADMIN pode fechar
- [x] Associação com projeto e assignee

### Integrações ✅
- [x] Usuários podem ser membros de projetos
- [x] Usuários podem ser assignees de tarefas
- [x] Validação: assignee deve ser membro do projeto
- [x] Validação: projeto deve existir

---

## 🎯 Resultado Esperado

Ao terminar este roadmap, você terá:

1. **3 usuários criados** com diferentes roles
2. **2 projetos criados** com membros associados
3. **4 tarefas criadas** com diferentes prioridades e assignees
4. **Validações testadas** (regras de negócio funcionando)
5. **Sistema integrado** funcionando end-to-end

---

## 💡 Dicas Importantes

### 📌 Salve IDs em um arquivo de texto
```
ADMIN_USER_ID = 5
MEMBER_USER_ID = 6
MEMBER_USER_ID_2 = 7
PROJECT_ID = 1
PROJECT_ID_2 = 2
TASK_ID_1 = 1
TASK_ID_2 = 2
TASK_ID_3 = 3
TASK_ID_4 = 4
```

### 🔄 Use Variáveis no Postman
```javascript
// Após criar um usuário, salve:
pm.environment.set("ADMIN_USER_ID", pm.response.json().data.id);
pm.environment.set("PROJECT_ID", pm.response.json().data.id);
```

### 📝 Documente os Erros
Quando um erro ocorrer, anote:
- Qual requisição causou
- Qual foi a resposta
- Se era esperado ou não

### 🐛 Se algo falhar
1. Verifique os IDs (deve corresponder com criações anteriores)
2. Confirme que o servidor está rodando
3. Verifique a formatação do JSON
4. Leia a mensagem de erro com atenção

---

## 🔗 Próximos Passos

Após este roadmap estar 100% completo, você pode:

1. **Implementar filtros de tarefas**
   - Por status
   - Por prioridade
   - Por responsável
   - Por range de datas

2. **Implementar busca textual**
   - Buscar por título
   - Buscar por descrição

3. **Implementar relatórios**
   - Contadores por status
   - Contadores por prioridade

4. **Implementar autenticação JWT**
   - Login com email/senha
   - Validar tokens
   - Proteger endpoints

---

**🎉 Boa sorte com os testes! Se encontrar algum problema, verifique a saída do servidor para detalhes do erro.**

