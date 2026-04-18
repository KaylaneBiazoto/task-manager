## 📮 Testes com Postman - CRUD de Usuários

Abaixo estão as chamadas HTTP prontas para testar no Postman.

---

## 1️⃣ CRIAR USUÁRIO (POST)

**URL:** `http://localhost:8080/api/users`

**Method:** `POST`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON) - Usuário MEMBER (padrão):**
```json
{
  "username": "maria",
  "email": "maria@example.com",
  "password": "password123"
}
```

**Resposta (201 Created):**
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "id": 5,
    "username": "maria",
    "email": "maria@example.com",
    "role": "MEMBER",
    "createdAt": "2026-04-18T17:35:00.123456",
    "updatedAt": "2026-04-18T17:35:00.123456"
  }
}
```

---

## 2️⃣ CRIAR USUÁRIO ADMIN (POST)

**URL:** `http://localhost:8080/api/users`

**Method:** `POST`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON) - Usuário ADMIN:**
```json
{
  "username": "superadmin",
  "email": "superadmin@example.com",
  "password": "admin123",
  "role": "ADMIN"
}
```

**Resposta (201 Created):**
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "id": 6,
    "username": "superadmin",
    "email": "superadmin@example.com",
    "role": "ADMIN",
    "createdAt": "2026-04-18T17:36:00.654321",
    "updatedAt": "2026-04-18T17:36:00.654321"
  }
}
```

---

## 3️⃣ BUSCAR USUÁRIO POR ID (GET)

**URL:** `http://localhost:8080/api/users/2`

**Method:** `GET`

**Resposta (200 OK):**
```json
{
  "success": true,
  "message": "User retrieved successfully",
  "data": {
    "id": 2,
    "username": "admin",
    "email": "admin@example.com",
    "role": "ADMIN",
    "createdAt": "2026-04-18T17:28:46.450419706",
    "updatedAt": "2026-04-18T17:28:46.450419706"
  }
}
```

---

## 4️⃣ LISTAR TODOS OS USUÁRIOS (GET)

**URL:** `http://localhost:8080/api/users`

**Method:** `GET`

**Parâmetros Query (Paginação):**
```
page=0
size=20
```

**URL Completa:** `http://localhost:8080/api/users?page=0&size=20`

**Resposta (200 OK):**
```json
{
  "success": true,
  "message": "Users retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "username": "testuser",
        "email": "test@example.com",
        "role": "MEMBER",
        "createdAt": "2026-04-18T17:28:13.405182",
        "updatedAt": "2026-04-18T17:28:13.405182"
      },
      {
        "id": 2,
        "username": "admin",
        "email": "admin@example.com",
        "role": "ADMIN",
        "createdAt": "2026-04-18T17:28:46.450419706",
        "updatedAt": "2026-04-18T17:28:46.450419706"
      },
      {
        "id": 3,
        "username": "john",
        "email": "john@example.com",
        "role": "MEMBER",
        "createdAt": "2026-04-18T17:28:55.026957503",
        "updatedAt": "2026-04-18T17:28:55.026957503"
      },
      {
        "id": 4,
        "username": "jane",
        "email": "jane@example.com",
        "role": "MEMBER",
        "createdAt": "2026-04-18T17:28:55.069717726",
        "updatedAt": "2026-04-18T17:28:55.069717726"
      }
    ],
    "empty": false,
    "first": true,
    "last": true,
    "number": 0,
    "numberOfElements": 4,
    "pageable": {
      "offset": 0,
      "pageNumber": 0,
      "pageSize": 20,
      "paged": true,
      "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
      },
      "unpaged": false
    },
    "size": 20,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "totalElements": 4,
    "totalPages": 1
  }
}
```

---

## 5️⃣ ATUALIZAR USUÁRIO - CHANGE USERNAME (PUT)

**URL:** `http://localhost:8080/api/users/4`

**Method:** `PUT`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "username": "jane_doe"
}
```

**Resposta (200 OK):**
```json
{
  "success": true,
  "message": "User updated successfully",
  "data": {
    "id": 4,
    "username": "jane_doe",
    "email": "jane@example.com",
    "role": "MEMBER",
    "createdAt": "2026-04-18T17:28:55.069717726",
    "updatedAt": "2026-04-18T17:40:00.123456"
  }
}
```

---

## 6️⃣ ATUALIZAR USUÁRIO - CHANGE EMAIL (PUT)

**URL:** `http://localhost:8080/api/users/3`

**Method:** `PUT`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "email": "john.doe@example.com"
}
```

**Resposta (200 OK):**
```json
{
  "success": true,
  "message": "User updated successfully",
  "data": {
    "id": 3,
    "username": "john",
    "email": "john.doe@example.com",
    "role": "MEMBER",
    "createdAt": "2026-04-18T17:28:55.026957503",
    "updatedAt": "2026-04-18T17:40:15.987654"
  }
}
```

---

## 7️⃣ ATUALIZAR USUÁRIO - CHANGE PASSWORD (PUT)

**URL:** `http://localhost:8080/api/users/1`

**Method:** `PUT`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "password": "newpassword456"
}
```

**Resposta (200 OK):**
```json
{
  "success": true,
  "message": "User updated successfully",
  "data": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com",
    "role": "MEMBER",
    "createdAt": "2026-04-18T17:28:13.405182",
    "updatedAt": "2026-04-18T17:40:30.456789"
  }
}
```

---

## 8️⃣ ATUALIZAR USUÁRIO - CHANGE ROLE (PUT)

**URL:** `http://localhost:8080/api/users/4`

**Method:** `PUT`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "role": "ADMIN"
}
```

**Resposta (200 OK):**
```json
{
  "success": true,
  "message": "User updated successfully",
  "data": {
    "id": 4,
    "username": "jane_doe",
    "email": "jane@example.com",
    "role": "ADMIN",
    "createdAt": "2026-04-18T17:28:55.069717726",
    "updatedAt": "2026-04-18T17:41:00.321654"
  }
}
```

---

## 9️⃣ ATUALIZAR MÚLTIPLOS CAMPOS (PUT)

**URL:** `http://localhost:8080/api/users/2`

**Method:** `PUT`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "username": "admin_master",
  "email": "admin.master@example.com",
  "password": "newadminpass123",
  "role": "ADMIN"
}
```

**Resposta (200 OK):**
```json
{
  "success": true,
  "message": "User updated successfully",
  "data": {
    "id": 2,
    "username": "admin_master",
    "email": "admin.master@example.com",
    "role": "ADMIN",
    "createdAt": "2026-04-18T17:28:46.450419706",
    "updatedAt": "2026-04-18T17:41:30.789012"
  }
}
```

---

## 🔟 DELETAR USUÁRIO (DELETE)

**URL:** `http://localhost:8080/api/users/5`

**Method:** `DELETE`

**Resposta (200 OK):**
```json
{
  "success": true,
  "message": "User deleted successfully",
  "data": null
}
```

---

## ⚠️ TESTES DE VALIDAÇÕES E REGRAS DE NEGÓCIO

### Erro 1: Email já registrado ❌

**URL:** `http://localhost:8080/api/users`

**Method:** `POST`

**Body:**
```json
{
  "username": "newuser",
  "email": "admin@example.com",
  "password": "password123"
}
```

**Resposta (409 Conflict):**
```json
{
  "success": false,
  "message": "Email already registered",
  "data": null
}
```

---

### Erro 2: Username já existe ❌

**URL:** `http://localhost:8080/api/users`

**Method:** `POST`

**Body:**
```json
{
  "username": "admin",
  "email": "newadmin@example.com",
  "password": "password123"
}
```

**Resposta (409 Conflict):**
```json
{
  "success": false,
  "message": "Username already taken",
  "data": null
}
```

---

### Erro 3: Email inválido ❌

**URL:** `http://localhost:8080/api/users`

**Method:** `POST`

**Body:**
```json
{
  "username": "invaliduser",
  "email": "not-an-email",
  "password": "password123"
}
```

**Resposta (400 Bad Request):**
```json
{
  "success": false,
  "message": "Email should be valid",
  "data": null
}
```

---

### Erro 4: Username vazio ❌

**URL:** `http://localhost:8080/api/users`

**Method:** `POST`

**Body:**
```json
{
  "username": "",
  "email": "user@example.com",
  "password": "password123"
}
```

**Resposta (400 Bad Request):**
```json
{
  "success": false,
  "message": "Username is required",
  "data": null
}
```

---

### Erro 5: Password vazio ❌

**URL:** `http://localhost:8080/api/users`

**Method:** `POST`

**Body:**
```json
{
  "username": "testuser",
  "email": "user@example.com",
  "password": ""
}
```

**Resposta (400 Bad Request):**
```json
{
  "success": false,
  "message": "Password is required",
  "data": null
}
```

---

### Erro 6: Tentando alterar para role inválido ❌

**URL:** `http://localhost:8080/api/users/3`

**Method:** `PUT`

**Body:**
```json
{
  "role": "SUPERUSER"
}
```

**Resposta (409 Conflict):**
```json
{
  "success": false,
  "message": "Invalid role. Must be ADMIN or MEMBER",
  "data": null
}
```

---

### Erro 7: Usuário não encontrado ❌

**URL:** `http://localhost:8080/api/users/999`

**Method:** `GET`

**Resposta (404 Not Found):**
```json
{
  "success": false,
  "message": "User not found with id: 999",
  "data": null
}
```

---

### Erro 8: Deletar usuário não existente ❌

**URL:** `http://localhost:8080/api/users/999`

**Method:** `DELETE`

**Resposta (404 Not Found):**
```json
{
  "success": false,
  "message": "User not found with id: 999",
  "data": null
}
```

---

### Erro 9: Email duplicado na atualização ❌

**URL:** `http://localhost:8080/api/users/3`

**Method:** `PUT`

**Body:**
```json
{
  "email": "admin@example.com"
}
```

**Resposta (409 Conflict):**
```json
{
  "success": false,
  "message": "Email already registered",
  "data": null
}
```

---

### Erro 10: Username duplicado na atualização ❌

**URL:** `http://localhost:8080/api/users/4`

**Method:** `PUT`

**Body:**
```json
{
  "username": "testuser"
}
```

**Resposta (409 Conflict):**
```json
{
  "success": false,
  "message": "Username already taken",
  "data": null
}
```

---

## 📊 FLUXO DE TESTE COMPLETO

Siga esse fluxo para testar tudo:

1. **Criar usuário MEMBER** → `POST /api/users` com role padrão
2. **Criar usuário ADMIN** → `POST /api/users` com role ADMIN
3. **Buscar usuário por ID** → `GET /api/users/2`
4. **Listar todos usuários** → `GET /api/users?page=0&size=20`
5. **Atualizar username** → `PUT /api/users/4` com novo username
6. **Atualizar email** → `PUT /api/users/3` com novo email
7. **Atualizar password** → `PUT /api/users/1` com nova senha
8. **Promover para ADMIN** → `PUT /api/users/4` com `role: ADMIN`
9. **Atualizar múltiplos campos** → `PUT /api/users/2` com vários campos
10. **Tentar criar email duplicado** → `POST /api/users` com email existente (deve falhar)
11. **Tentar criar username duplicado** → `POST /api/users` com username existente (deve falhar)
12. **Deletar usuário** → `DELETE /api/users/5`
13. **Buscar usuário deletado** → `GET /api/users/5` (deve retornar 404)

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
USER_ID = 2
PAGE = 0
SIZE = 20
```

Depois use nas URLs:
```
{{BASE_URL}}/api/users
{{BASE_URL}}/api/users/{{USER_ID}}
{{BASE_URL}}/api/users?page={{PAGE}}&size={{SIZE}}
```

### 3. Roles válidas

Sempre use:
- ADMIN
- MEMBER (padrão)

### 4. Formato de email válido

Sempre use emails no formato:
```
nome@dominio.com
```

### 5. Sequência recomendada de testes

**Dia 1 - Básico:**
- Criar 3 usuários MEMBER
- Criar 1 usuário ADMIN
- Listar todos

**Dia 2 - Atualizações:**
- Atualizar username
- Atualizar email
- Atualizar password
- Mudar role MEMBER → ADMIN

**Dia 3 - Validações:**
- Testar email duplicado
- Testar username duplicado
- Testar email inválido
- Testar campos vazios

**Dia 4 - Exclusão:**
- Deletar usuário
- Tentar buscar deletado
- Tentar deletar inexistente

---

## ✅ CHECKLIST DE TESTES

- [ ] Criar usuário MEMBER (sem especificar role)
- [ ] Criar usuário ADMIN (com role)
- [ ] Buscar usuário existente por ID
- [ ] Listar todos os usuários com paginação
- [ ] Atualizar apenas username
- [ ] Atualizar apenas email
- [ ] Atualizar apenas password
- [ ] Atualizar apenas role
- [ ] Atualizar múltiplos campos
- [ ] Promover MEMBER para ADMIN
- [ ] Rebaixar ADMIN para MEMBER
- [ ] Tentar criar com email duplicado (deve falhar)
- [ ] Tentar criar com username duplicado (deve falhar)
- [ ] Tentar criar com email inválido (deve falhar)
- [ ] Tentar atualizar com email duplicado (deve falhar)
- [ ] Tentar atualizar com username duplicado (deve falhar)
- [ ] Tentar atualizar com role inválido (deve falhar)
- [ ] Deletar usuário existente
- [ ] Buscar usuário deletado (deve retornar 404)
- [ ] Tentar deletar usuário inexistente (deve falhar)
- [ ] Testar paginação com size diferente
- [ ] Testar com page=1 quando há múltiplos usuários

---

## 🎯 USUÁRIOS PRÉ-CRIADOS PARA TESTES

Ao iniciar a aplicação, existem estes usuários:

| ID | Username | Email | Role | Senha |
|---|---|---|---|---|
| 1 | testuser | test@example.com | MEMBER | password123 |
| 2 | admin | admin@example.com | ADMIN | admin123 |
| 3 | john | john@example.com | MEMBER | john123 |
| 4 | jane | jane@example.com | MEMBER | jane123 |

**Use estes IDs nos testes GET, PUT e DELETE!**

---

## 📝 NOTAS IMPORTANTES

### ⚠️ Dados temporários
- A aplicação usa H2 em memória
- Todos os dados são perdidos ao reiniciar o servidor
- Use para testes locais apenas

### 🔐 Segurança
- As senhas **NÃO** estão sendo criptografadas ainda
- Isso será implementado na integração com JWT
- Por enquanto, são armazenadas em texto plano para testes

### 📦 Integração com Projects e Tasks
- Usuários criados aqui podem ser associados a projetos
- Usuários podem ser atribuídos a tarefas
- Verificações de membership serão feitas quando associar a projetos

