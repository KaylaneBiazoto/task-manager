# Implementação do CRUD de Projetos

## 📋 Resumo

Foi implementado um **CRUD completo de Projetos** seguindo a arquitetura baseada em **handlers (usecases)** e **facade**, integrando com as tarefas já existentes.

## 🏗️ Estrutura de Arquivos Criados

### Core (DTOs e Exceções)
```
src/main/java/com/example/task_manager_backend/features/projects/core/
├── ProjectDto.java                 # DTO com suporte a nested ProjectMemberDto
├── CreateProjectRequest.java       # Request para criar projeto
├── UpdateProjectRequest.java       # Request para atualizar projeto
├── AddProjectMemberRequest.java    # Request para adicionar membro
├── ProjectNotFoundException.java   # Exceção de projeto não encontrado
└── ProjectBusinessException.java   # Exceção de regra de negócio
```

### Usecases (Handlers)
```
src/main/java/com/example/task_manager_backend/features/projects/usecases/
├── CreateProjectHandler.java       # Cria projeto e adiciona owner como ADMIN
├── GetProjectByIdHandler.java      # Busca projeto por ID
├── ListProjectsHandler.java        # Lista projetos com paginação
├── UpdateProjectHandler.java       # Atualiza projeto
├── DeleteProjectHandler.java       # Deleta projeto (soft delete)
├── AddProjectMemberHandler.java    # Adiciona membro ao projeto
└── GetProjectMembersHandler.java   # Lista membros do projeto
```

### Service Layer
```
src/main/java/com/example/task_manager_backend/features/projects/services/
└── ProjectService.java             # Facade orquestrando todos os handlers
```

### API Layer
```
src/main/java/com/example/task_manager_backend/features/projects/api/
└── ProjectController.java          # Endpoints REST para CRUD
```

### Updated Files
```
src/main/java/com/example/task_manager_backend/features/projects/repositories/
└── ProjectRepository.java          # Adicionado método findByActiveTrue()

src/main/java/com/example/task_manager_backend/core/advice/
└── GlobalExceptionHandler.java     # Adicionados handlers para ProjectNotFoundException 
                                     # e ProjectBusinessException
```

## 🔄 Fluxo de Arquitetura

```
Controller → Facade (ProjectService) → Handlers (Usecases) → Repositories → Database
```

### Exemplo: Create Project
1. **Controller** recebe requisição POST em `/api/projects`
2. **ProjectService (Facade)** chama `createProjectHandler.execute()`
3. **CreateProjectHandler** valida dados e cria o projeto
4. Adiciona automaticamente o owner como membro (ADMIN)
5. Retorna ProjectDto com todos os dados

## ✨ Funcionalidades Implementadas

### 1. CRUD Básico de Projetos
- ✅ **CREATE**: Criar novo projeto (owner é automático como ADMIN)
- ✅ **READ**: Buscar projeto por ID
- ✅ **READ**: Listar todos os projetos com paginação
- ✅ **UPDATE**: Atualizar nome e descrição do projeto
- ✅ **DELETE**: Deletar projeto (soft delete via campo `active`)

### 2. Gerenciamento de Membros
- ✅ **ADD**: Adicionar membro ao projeto
- ✅ **LIST**: Listar todos os membros de um projeto
- ✅ Validação: Previne duplicação de membro no projeto
- ✅ Roles: Suporte a diferentes papéis (ADMIN, MEMBER, etc.)

### 3. Integração com Tasks
- ✅ Projetos possuem relacionamento N:1 com Tarefas
- ✅ Tasks herdam validações de membros do projeto
- ✅ Suporte a WIP limit (máximo 5 tarefas IN_PROGRESS por membro)

### 4. Tratamento de Erros
- ✅ `ProjectNotFoundException` (404): Projeto não encontrado
- ✅ `ProjectBusinessException` (409): Violação de regra de negócio
- ✅ Mensagens de erro padronizadas em `ApiResponse`

## 📊 Endpoints Disponíveis

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/projects` | Criar novo projeto |
| GET | `/api/projects` | Listar projetos (paginado) |
| GET | `/api/projects/{id}` | Buscar projeto por ID |
| PUT | `/api/projects/{id}` | Atualizar projeto |
| DELETE | `/api/projects/{id}` | Deletar projeto |
| POST | `/api/projects/{id}/members` | Adicionar membro |
| GET | `/api/projects/{id}/members` | Listar membros |

## 🗄️ Schema do Banco de Dados

```sql
CREATE TABLE projects (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE project_members (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    project_role VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    UNIQUE (project_id, user_id)
);
```

## 🔐 Padrões Implementados

### Handler/Usecase Pattern
- Cada operação é encapsulada em um Handler (@Component)
- Facilita testes unitários
- Separação clara de responsabilidades

### Facade Pattern
- ProjectService orquestra todos os handlers
- Interface única para o Controller
- Mapeia entidades para DTOs

### Soft Delete
- Campo `active` em projetos e membros
- Permite recuperação lógica de dados
- Mantém referential integrity

### Exception Handling
- Exceções específicas por domínio
- GlobalExceptionHandler converte para ApiResponse
- Códigos HTTP apropriados (404, 409, 500)

## 🚀 Como Testar

### 1. Build
```bash
./mvnw clean package -DskipTests
```

### 2. Run
```bash
java -jar target/task-manager-backend-0.0.1-SNAPSHOT.jar
```

### 3. Test Endpoints
Ver arquivo `PROJECTS_API_TEST.md` para exemplos de curl

### Dados de Teste
O banco é criado com 4 usuários iniciais (IDs 1-4):
- admin, user1, user2, user3

## 📝 Notas

- **Owner automático**: Ao criar projeto, o usuário que cria é adicionado como ADMIN
- **WIP Limit**: Validação já existe em CreateTaskHandler (máx 5 IN_PROGRESS por assignee)
- **Validação de Membro**: Tasks só aceitam assignee que é membro do projeto
- **Soft Delete**: Projetos não são realmente deletados, apenas marcados como inactive

## 🔗 Integração com Tasks

As tarefas já existentes foram integradas com sucesso:
- Tasks pertencem a um Project (FK)
- Assignee de task deve ser membro do projeto
- WIP limit valida número de tasks IN_PROGRESS
- Regra: CRITICAL priority requer ADMIN para fechar
- Regra: DONE não volta para TODO, apenas para IN_PROGRESS

## ✅ Validações Implementadas

- ✅ Nome de projeto obrigatório
- ✅ Usuário deve existir para ser owner
- ✅ Usuário deve existir para ser adicionado como membro
- ✅ Não permite duplicar membro em projeto
- ✅ Projeto deve existir para adicionar membros
- ✅ Todos os dados auditados (createdAt, updatedAt)

