# Task Manager Backend - CRUD de Tarefas

## Visão Geral

Esta documentação descreve a implementação completa do módulo de **Tarefas (Tasks)** do Task Manager Backend, incluindo CRUD, validações e regras de negócio.

## Estrutura de Arquivos

A implementação segue a arquitetura do projeto, organizada da seguinte forma:

```
src/main/java/com/example/task_manager_backend/features/tasks/
├── api/
│   └── TaskController.java          # Endpoints REST
├── core/
│   ├── CreateTaskRequest.java       # DTO para criar tarefa
│   ├── TaskDto.java                 # DTO para resposta
│   ├── UpdateTaskRequest.java       # DTO para atualizar tarefa
│   ├── TaskBusinessException.java   # Exceção de regra de negócio
│   └── TaskNotFoundException.java    # Exceção de não encontrado
├── domain/
│   ├── Task.java                    # Entidade JPA
│   ├── TaskStatus.java              # Enum: TODO, IN_PROGRESS, DONE
│   └── TaskPriority.java            # Enum: LOW, MEDIUM, HIGH, CRITICAL
├── repositories/
│   └── TaskRepository.java          # Acesso a dados
├── services/
│   └── TaskFacade.java              # Orquestração
└── usecases/
    ├── CreateTaskHandler.java       # Caso de uso: criar
    ├── GetTaskByIdHandler.java      # Caso de uso: buscar por ID
    ├── ListTasksByProjectHandler.java # Caso de uso: listar por projeto
    ├── UpdateTaskHandler.java       # Caso de uso: atualizar
    └── DeleteTaskHandler.java       # Caso de uso: deletar
```

## Campos da Tarefa

A entidade `Task` contém os seguintes campos:

| Campo | Tipo | Descrição | Obrigatório |
|-------|------|-----------|------------|
| `id` | Long | Identificador único | Sim (auto-gerado) |
| `title` | String | Título da tarefa | Sim (máx 200 caracteres) |
| `description` | String | Descrição detalhada | Não |
| `status` | TaskStatus | TODO, IN_PROGRESS ou DONE | Sim (default: TODO) |
| `priority` | TaskPriority | LOW, MEDIUM, HIGH ou CRITICAL | Sim |
| `deadline` | LocalDate | Prazo da tarefa | Não |
| `project` | Project | Projeto ao qual pertence | Sim |
| `assignee` | User | Usuário responsável | Sim |
| `active` | Boolean | Indicador de ativo/deletado | Sim (default: true) |
| `createdAt` | ZonedDateTime | Data de criação | Sim (auto) |
| `updatedAt` | ZonedDateTime | Data de última atualização | Sim (auto) |

## Enums

### TaskStatus
- **TODO**: Estado inicial de toda tarefa
- **IN_PROGRESS**: Tarefa em progresso
- **DONE**: Tarefa concluída

### TaskPriority
- **LOW**: Baixa prioridade
- **MEDIUM**: Prioridade média
- **HIGH**: Alta prioridade
- **CRITICAL**: Prioridade crítica

## API Endpoints

### 1. Criar Tarefa

**Endpoint:** `POST /api/tasks`

**Request:**
```json
{
  "title": "Implementar login",
  "description": "Implementar sistema de autenticação com JWT",
  "priority": "HIGH",
  "deadline": "2026-05-18",
  "projectId": 1,
  "assigneeId": 5
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Task created successfully",
  "data": {
    "id": 1,
    "title": "Implementar login",
    "description": "Implementar sistema de autenticação com JWT",
    "status": "TODO",
    "priority": "HIGH",
    "deadline": "2026-05-18",
    "projectId": 1,
    "assigneeId": 5,
    "assigneeName": "João Silva",
    "createdAt": "2026-04-18T16:30:00",
    "updatedAt": "2026-04-18T16:30:00"
  }
}
```

### 2. Buscar Tarefa por ID

**Endpoint:** `GET /api/tasks/{taskId}`

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Task retrieved successfully",
  "data": {
    "id": 1,
    "title": "Implementar login",
    "description": "Implementar sistema de autenticação com JWT",
    "status": "TODO",
    "priority": "HIGH",
    "deadline": "2026-05-18",
    "projectId": 1,
    "assigneeId": 5,
    "assigneeName": "João Silva",
    "createdAt": "2026-04-18T16:30:00",
    "updatedAt": "2026-04-18T16:30:00"
  }
}
```

**Possíveis Erros:**
- `404 Not Found`: Tarefa não encontrada

### 3. Listar Tarefas por Projeto

**Endpoint:** `GET /api/tasks/project/{projectId}`

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Tasks retrieved successfully",
  "data": [
    {
      "id": 1,
      "title": "Implementar login",
      "status": "TODO",
      "priority": "HIGH",
      "projectId": 1,
      "assigneeId": 5,
      "assigneeName": "João Silva",
      "createdAt": "2026-04-18T16:30:00",
      "updatedAt": "2026-04-18T16:30:00"
    },
    {
      "id": 2,
      "title": "Criar banco de dados",
      "status": "IN_PROGRESS",
      "priority": "CRITICAL",
      "projectId": 1,
      "assigneeId": 6,
      "assigneeName": "Maria Santos",
      "createdAt": "2026-04-17T10:15:00",
      "updatedAt": "2026-04-18T14:20:00"
    }
  ]
}
```

### 4. Atualizar Tarefa

**Endpoint:** `PUT /api/tasks/{taskId}`

**Request:**
```json
{
  "title": "Implementar login com OAuth",
  "status": "IN_PROGRESS",
  "priority": "CRITICAL",
  "deadline": "2026-05-10",
  "assigneeId": 6
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Task updated successfully",
  "data": {
    "id": 1,
    "title": "Implementar login com OAuth",
    "status": "IN_PROGRESS",
    "priority": "CRITICAL",
    "deadline": "2026-05-10",
    "projectId": 1,
    "assigneeId": 6,
    "assigneeName": "Maria Santos",
    "createdAt": "2026-04-18T16:30:00",
    "updatedAt": "2026-04-18T17:00:00"
  }
}
```

### 5. Deletar Tarefa (Soft Delete)

**Endpoint:** `DELETE /api/tasks/{taskId}`

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Task deleted successfully",
  "data": null
}
```

## Regras de Negócio

### Regra 1: Tarefa DONE não volta para TODO

Uma tarefa que está com status **DONE** não pode ser alterada diretamente para **TODO**. Ela só pode ser revertida para **IN_PROGRESS**.

**Cenários:**
- ❌ DONE → TODO: **Bloqueado**
- ✓ DONE → IN_PROGRESS: **Permitido**

**Erro Retornado (409 Conflict):**
```json
{
  "success": false,
  "message": "A task with DONE status cannot return to TODO. Only IN_PROGRESS is allowed.",
  "data": null
}
```

### Regra 2: Apenas ADMIN pode fechar tarefas CRITICAL

Tarefas com prioridade **CRITICAL** só podem ser marcadas como **DONE** por usuários com perfil **ADMIN** do projeto.

**Cenários:**
- ✓ ADMIN marca CRITICAL como DONE: **Permitido**
- ❌ MEMBER marca CRITICAL como DONE: **Bloqueado**

**Erro Retornado (409 Conflict):**
```json
{
  "success": false,
  "message": "Only project ADMIN can mark CRITICAL priority tasks as DONE",
  "data": null
}
```

### Regra 3: WIP Limit (Work in Progress)

Um responsável (assignee) não pode ter mais de **5 tarefas com status IN_PROGRESS** simultaneamente. Isso evita sobrecarga.

**Validação ocorre em:**
1. **Criação de tarefa**: Se o assignee já tem 5 tarefas em progresso
2. **Atualização de status**: Se mudar para IN_PROGRESS e já tem 5
3. **Troca de responsável**: Se o novo responsável já tem 5 tarefas em progresso

**Erro Retornado (409 Conflict):**
```json
{
  "success": false,
  "message": "Assignee has reached the maximum limit of 5 in-progress tasks (WIP limit exceeded)",
  "data": null
}
```

### Regra 4: Assignee deve ser membro do projeto

Toda tarefa deve ser atribuída a um usuário que seja membro ativo do projeto.

**Validações:**
- Na criação: verifica se assignee é membro do projeto
- Na atualização de assignee: verifica se o novo assignee é membro

**Erro Retornado (409 Conflict):**
```json
{
  "success": false,
  "message": "Assignee is not a member of the project",
  "data": null
}
```

## Arquitetura e Padrões

### Handlers (Use Cases)

Cada operação possui seu próprio handler, responsável por executar a lógica de negócio:

- **CreateTaskHandler**: Valida projeto, assignee, pertencimento ao projeto e WIP limit
- **GetTaskByIdHandler**: Busca tarefa ativa por ID
- **ListTasksByProjectHandler**: Lista todas as tarefas ativas de um projeto
- **UpdateTaskHandler**: Valida transições de status e aplica regras de negócio
- **DeleteTaskHandler**: Realiza soft delete (marca como inactive)

### Facade (TaskFacade)

A facade orquestra os handlers e realiza conversão entre entidades e DTOs:

```java
public TaskDto createTask(CreateTaskRequest request) {
    Task task = createTaskHandler.execute(request);
    return mapToDto(task);
}
```

### Controller

O controller REST expõe os endpoints e delega para a facade:

```java
@PostMapping
public ResponseEntity<ApiResponse<TaskDto>> createTask(@Valid @RequestBody CreateTaskRequest request) {
    TaskDto taskDto = taskFacade.createTask(request);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
}
```

### DTOs (Data Transfer Objects)

- **CreateTaskRequest**: Recebe dados para criar tarefa
- **UpdateTaskRequest**: Recebe dados para atualizar tarefa
- **TaskDto**: Retorna dados da tarefa na resposta

### Repositories

O **TaskRepository** fornece métodos para:

```java
Optional<Task> findByIdAndActiveTrue(Long id);
List<Task> findByProjectIdAndActiveTrue(Long projectId);
List<Task> findByAssigneeIdAndStatusAndActiveTrue(Long assigneeId, TaskStatus status);
long countInProgressTasksByAssignee(Long assigneeId, TaskStatus status);
List<Task> findByProjectIdAndStatusAndActiveTrue(Long projectId, TaskStatus status);
```

## Tratamento de Erros

O **GlobalExceptionHandler** captura exceções específicas:

| Exceção | Status HTTP | Descrição |
|---------|-----------|-----------|
| `TaskNotFoundException` | 404 | Tarefa não encontrada |
| `TaskBusinessException` | 409 | Violação de regra de negócio |
| `Exception` | 500 | Erro interno do servidor |

## Migrations do Banco

A tabela `tasks` já existe no schema inicial. Seus campos:

```sql
CREATE TABLE tasks (
    id UUID PRIMARY KEY,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    project_id UUID NOT NULL,
    assignee_id UUID,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL,
    priority VARCHAR(20) NOT NULL,
    deadline DATE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_tasks_project FOREIGN KEY (project_id) REFERENCES projects (id),
    CONSTRAINT fk_tasks_assignee FOREIGN KEY (assignee_id) REFERENCES users (id)
);
```

## Exemplos de Uso

### Exemplo 1: Criar e Atualizar Tarefa

```bash
# 1. Criar tarefa
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Implementar validação",
    "description": "Adicionar validações em tarefas",
    "priority": "HIGH",
    "deadline": "2026-05-20",
    "projectId": 1,
    "assigneeId": 5
  }'

# 2. Atualizar para IN_PROGRESS
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "status": "IN_PROGRESS"
  }'

# 3. Tentar mudar para DONE (se CRITICAL e não ADMIN, será bloqueado)
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "status": "DONE"
  }'
```

### Exemplo 2: Testar WIP Limit

```bash
# Criar 5 tarefas IN_PROGRESS para o mesmo assignee
# (a 6ª tentativa será bloqueada)

for i in {1..5}; do
  curl -X POST http://localhost:8080/api/tasks \
    -H "Content-Type: application/json" \
    -d "{
      \"title\": \"Tarefa $i\",
      \"priority\": \"MEDIUM\",
      \"projectId\": 1,
      \"assigneeId\": 5
    }"
done

# Esta chamada será bloqueada:
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Tarefa 6",
    "priority": "MEDIUM",
    "projectId": 1,
    "assigneeId": 5
  }'
```

## Próximos Passos

1. **Integração com Security**: Extrair user ID e role do contexto de segurança
2. **Testes Unitários**: Criar testes para handlers e facade
3. **Testes de Integração**: Testar endpoints com dados reais
4. **Filtros Avançados**: Adicionar filtros por status, prioridade, deadline, assignee
5. **Paginação**: Implementar paginação nas listagens
6. **Busca Textual**: Implementar busca por título e descrição

## Notas de Desenvolvimento

- **Soft Delete**: Tarefas são marcadas como `active = false` em vez de deletadas fisicamente
- **Lazy Loading**: Relacionamentos são carregados sob demanda para performance
- **Validação**: DTOs utilizam `@Valid` e constraints do Jakarta Validation
- **Transações**: Operações são executadas dentro de transações JPA

