# Task Manager Backend — Architecture Overview

## 1. Visão geral

O `task-manager-backend` é uma API REST desenvolvida em **Java + Spring Boot** para gerenciamento de tarefas em equipe.

A proposta da arquitetura é manter o código organizado, testável e fácil de evoluir. Para isso, o projeto segue um estilo **feature-driven** com separação clara entre camadas de responsabilidade.

Em vez de concentrar toda a lógica em services genéricos, a solução é dividida por domínio de negócio. Cada domínio possui suas próprias classes de API, coordenação, regras e acesso a dados.

---

## 2. Objetivos de arquitetura

A arquitetura foi pensada para alcançar os seguintes objetivos:

1. **Separação de responsabilidades**
   - Controllers recebem requisições HTTP.
   - Facades coordenam fluxos de uso.
   - Handlers executam regras de negócio.
   - Repositories acessam o banco.

2. **Evolução por módulo**
   - Cada feature pode crescer sem poluir as demais.
   - Auth, Projects e Tasks evoluem de forma independente.

3. **Manutenção simples**
   - Estrutura de pacotes previsível.
   - Classes com papéis bem definidos.
   - Menor acoplamento entre módulos.

4. **Preparação para segurança e validação**
   - JWT para autenticação.
   - Controle de acesso por perfil e por pertencimento ao projeto.
   - Tratamento padronizado de erros.

5. **Persistência versionada**
   - PostgreSQL como banco principal.
   - Flyway para migrations versionadas.

6. **Identificadores únicos**
    - UUIDs para melhor distribuição e clustering.
    - Rastreabilidade global sem contenção de sequências.

---

## 3. Visão em alto nível

A aplicação foi organizada em quatro blocos principais:

```text
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Controllers   │───▶│    Facades      │───▶│   Handlers      │───▶│  Repositories  │
│  (REST APIs)    │    │ (Orchestration) │    │ (Use Cases)     │    │ (Data Access)  │
└─────────────────┘    └─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │                       │
         ▼                       ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   ApiResponse   │◀───│   Result<T>     │◀───│ Business Rules   │◀───│ PostgreSQL DB   │
└─────────────────┘    └─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Como ler esse desenho

- **Controller**: expõe endpoints REST.
- **Facade**: organiza o fluxo de uma operação e conversa com vários handlers quando necessário.
- **Handler**: concentra o caso de uso e as regras de negócio daquele cenário.
- **Repository**: realiza leitura e escrita no banco.
- **ApiResponse / Result**: representam contratos e padronização de retorno.

---

## 4. Estrutura de pacotes

A base do projeto está organizada em `src/main/java/com/example/task_manager_backend/` e segue a lógica abaixo:

```text
com.example.task_manager_backend
├── TaskManagerBackendApplication.java
├── core
│   ├── advice
│   │   └── GlobalExceptionHandler.java
│   └── dto
│       └── ApiResponse.java
├── features
│   ├── auth
│   │   ├── api
│   │   │   ├── AuthController.java
│   │   │   ├── LoginController.java
│   │   │   └── UsersController.java
│   │   ├── core
│   │   │   ├── CreateUserRequest.java
│   │   │   ├── UpdateUserRequest.java
│   │   │   ├── LoginRequest.java
│   │   │   ├── RegisterRequest.java
│   │   │   ├── LoginResponse.java
│   │   │   ├── UserDto.java
│   │   │   ├── UserNotFoundException.java
│   │   │   └── UserBusinessException.java
│   │   ├── domain
│   │   │   ├── User.java
│   │   │   └── Role.java
│   │   ├── repositories
│   │   │   └── UserRepository.java
│   │   ├── services
│   │   │   ├── UserService.java (Facade)
│   │   │   └── AuthService.java
│   │   └── usecases
│   │       ├── CreateUserHandler.java
│   │       ├── GetUserByIdHandler.java
│   │       ├── ListUsersHandler.java
│   │       ├── UpdateUserHandler.java
│   │       ├── DeleteUserHandler.java
│   │       ├── LoginHandler.java
│   │       └── LoginUseCase.java
│   ├── projects
│   │   ├── api
│   │   │   └── ProjectController.java
│   │   ├── core
│   │   │   ├── CreateProjectRequest.java
│   │   │   ├── UpdateProjectRequest.java
│   │   │   ├── AddProjectMemberRequest.java
│   │   │   ├── ProjectDto.java
│   │   │   ├── ProjectNotFoundException.java
│   │   │   └── ProjectBusinessException.java
│   │   ├── domain
│   │   │   ├── Project.java
│   │   │   └── ProjectMember.java
│   │   ├── repositories
│   │   │   ├── ProjectRepository.java
│   │   │   └── ProjectMemberRepository.java
│   │   ├── services
│   │   │   └── ProjectFacade.java
│   │   └── usecases
│   │       ├── CreateProjectHandler.java
│   │       ├── GetProjectByIdHandler.java
│   │       ├── ListProjectsHandler.java
│   │       ├── UpdateProjectHandler.java
│   │       ├── DeleteProjectHandler.java
│   │       └── (mais handlers)
│   └── tasks
│       ├── api
│       │   └── TaskController.java
│       ├── core
│       │   ├── CreateTaskRequest.java
│       │   ├── UpdateTaskRequest.java
│       │   ├── TaskDto.java
│       │   ├── TaskNotFoundException.java
│       │   └── TaskBusinessException.java
│       ├── domain
│       │   ├── Task.java
│       │   ├── TaskStatus.java
│       │   └── TaskPriority.java
│       ├── repositories
│       │   └── TaskRepository.java
│       ├── services
│       │   ├── TaskFacade.java
│       │   └── TaskService.java
│       └── usecases
│           ├── CreateTaskHandler.java
│           ├── GetTaskByIdHandler.java
│           ├── ListTasksByProjectHandler.java
│           ├── ListTasksHandler.java
│           ├── UpdateTaskHandler.java
│           └── DeleteTaskHandler.java
└── infrastructure
    ├── persistence
    │   └── BaseEntity.java
    └── security
        ├── SecurityConfig.java
        ├── TokenService.java
        └── CustomUserDetailsService.java
```

### O que cada pasta representa

#### `core`
Contém componentes transversais da aplicação, como:
- resposta padrão da API (`ApiResponse`)
- tratamento global de exceções

#### `features`
Contém os módulos de negócio. Hoje os principais são:
- `auth` - autenticação e gerenciamento de usuários
- `projects` - gerenciamento de projetos
- `tasks` - gerenciamento de tarefas

Cada feature possui suas próprias classes e evita dependências desnecessárias com as outras.

#### `infrastructure`
Contém os detalhes técnicos:
- configuração de persistência
- segurança
- integrações com framework
- implementação de pontos mais específicos da aplicação

---

## 5. Camadas da aplicação

### 5.1 Controllers / API

Os controllers são a porta de entrada da aplicação.

#### Responsabilidades
- Receber requisições HTTP.
- Validar payloads de entrada.
- Chamar a facade correspondente.
- Retornar respostas HTTP apropriadas.

#### Exemplos de classes
- `AuthController`
- `ProjectController`
- `TaskController`

#### Princípios dessa camada
- Não deve conter regra de negócio.
- Não deve falar diretamente com o banco.
- Deve ser fina e objetiva.

---

### 5.2 Facades / Services

As facades fazem a orquestração da operação.

#### Responsabilidades
- Coordenar fluxos de uso maiores.
- Combinar múltiplos handlers quando necessário.
- Manter os controllers simples.
- Mapear entidades para DTOs.

#### Exemplos de classes
- `UserService` (Facade para Auth)
- `ProjectFacade`
- `TaskFacade`

#### Quando usar uma facade
Use uma facade quando uma operação envolver mais de uma etapa, por exemplo:
- validar usuário
- carregar projeto
- verificar permissão
- executar regra de negócio
- persistir alteração

---

### 5.3 Handlers / Use Cases

Os handlers representam os casos de uso principais.

#### Responsabilidades
- Aplicar regras de negócio.
- Validar condições do domínio.
- Executar lógica de criação, atualização, busca e transição de estado.
- Chamar repositories quando necessário.

#### Exemplos de classes
- `CreateUserHandler`, `GetUserByIdHandler`, `ListUsersHandler`
- `CreateProjectHandler`, `UpdateProjectHandler`
- `CreateTaskHandler`, `UpdateTaskHandler`, `DeleteTaskHandler`

#### Regras que devem ficar aqui
No módulo de tarefas, por exemplo:
- uma tarefa `DONE` não pode voltar para `TODO`
- `CRITICAL` só pode ser fechada por `ADMIN`
- no máximo 5 tarefas `IN_PROGRESS` por responsável
- o responsável deve ser membro do projeto

---

### 5.4 Repositories

Os repositories concentram o acesso a dados.

#### Responsabilidades
- Consultar e persistir no banco.
- Oferecer métodos de busca filtrada e ordenada.
- Encapsular detalhes do JPA ou outra tecnologia de persistência.

#### Exemplos de interfaces
- `UserRepository`
- `ProjectRepository`
- `ProjectMemberRepository`
- `TaskRepository`

---


## 6. Módulos de negócio

### 6.1 Auth

O módulo de autenticação é responsável por login e autorização.

#### Objetivos
- autenticar por email e senha
- emitir JWT
- proteger endpoints
- diferenciar perfis `ADMIN` e `MEMBER`

#### Componentes principais
- `AuthController`, `LoginController`, `UsersController`
- `UserService` (Facade)
- Handlers: CreateUserHandler, GetUserByIdHandler, ListUsersHandler, UpdateUserHandler, DeleteUserHandler
- `UserRepository`
- `User` (domain) / DTOs

#### Regras esperadas
- somente usuários autenticados acessam a API protegida
- o perfil do usuário interfere nas permissões
- `ADMIN` tem privilégios adicionais em projetos e tarefas

---

### 6.2 Projects

O módulo de projetos organiza o contexto de trabalho.

#### Objetivos
- criar, editar, listar e remover projetos
- registrar dono do projeto
- associar membros ao projeto
- garantir que apenas membros enxerguem o projeto

#### Componentes principais
- `ProjectController`
- `ProjectFacade`
- Handlers para CRUD de projetos
- `ProjectRepository`, `ProjectMemberRepository`
- `Project`, `ProjectMember` (domain) / DTOs

#### Regras esperadas
- todo projeto possui um dono
- um projeto pode ter N membros
- somente usuários do projeto podem manipulá-lo
- `ADMIN` gerencia membros do projeto

---

### 6.3 Tasks

O módulo de tarefas é o coração do produto.

#### Objetivos
- CRUD de tarefas dentro de projeto
- controle de status e prioridade
- atribuição de responsável
- filtros e ordenação
- busca textual
- resumo por status e prioridade

#### Componentes principais
- `TaskController`
- `TaskFacade`
- Handlers: CreateTaskHandler, GetTaskByIdHandler, ListTasksByProjectHandler, UpdateTaskHandler, DeleteTaskHandler
- `TaskRepository`
- `Task` (domain) / DTOs

#### Regras de negócio principais
- `DONE` não volta para `TODO`
- `CRITICAL` só pode ser concluída por `ADMIN` do projeto
- um responsável não pode ter mais de 5 tarefas `IN_PROGRESS`
- o responsável deve ser membro do projeto
- apenas usuários vinculados ao projeto podem consultar ou alterar a tarefa

---

## 7. Fluxo de requisição

O fluxo completo de uma operação segue esta sequência:

```text
HTTP Request
   ↓
Controller (validação básica)
   ↓
Facade/Service (orquestração)
   ↓
Handler (regra de negócio)
   ↓
Repository (acesso a dados)
   ↓
Database
   ↓
Repository (retorna resultado)
   ↓
Handler (processa resultado)
   ↓
Facade/Service (mapeia DTO)
   ↓
Controller (retorna resposta)
   ↓
HTTP Response
```

### Exemplo prático: criar tarefa

1. O frontend chama `POST /api/tasks`.
2. O `TaskController` recebe e valida o payload.
3. O controller chama a `TaskFacade.createTask()`.
4. A facade delega para o `CreateTaskHandler`.
5. O handler valida:
   - se o projeto existe
   - se o responsável pertence ao projeto
   - se o limite de WIP foi respeitado
6. O handler persiste a tarefa via `TaskRepository`.
7. A facade mapeia a entidade para `TaskDto`.
8. O controller retorna com status 201 e corpo padronizado.

---

## 8. Persistência e banco de dados

A persistência foi planejada para **PostgreSQL**.

### Estratégia
- PostgreSQL como banco principal.
- Flyway para versionar o schema.
- Hibernate/JPA para mapeamento de entidades e consultas.
- UUIDs como identificadores primários.
- Auditoria nativa via JPA listeners.

### Arquivos importantes
- `src/main/resources/application.properties`
- `src/main/resources/db/migration/V*.sql`

### Benefícios dessa abordagem
- histórico claro das mudanças de banco
- reprodução fácil do ambiente
- menos risco de diferenças entre máquinas
- melhor controle sobre evolução do schema

### Tabelas principais
- `users` - usuários do sistema
- `projects` - projetos
- `project_members` - associação entre usuários e projetos
- `tasks` - tarefas dentro de projetos

---

## 9. Segurança

A segurança da aplicação está em `infrastructure/security`.

### Estratégia definida
- autenticação com JWT
- autorização baseada em papéis e pertencimento ao projeto
- proteção de endpoints por Spring Security

### Arquivo base
- `SecurityConfig.java`

### Regras esperadas
- usuários `MEMBER` só manipulam tarefas conforme permissão
- usuários `ADMIN` possuem funções administrativas do projeto

---

## 10. Identificadores e UUIDs

Todas as entidades usam **UUID (Universally Unique Identifier)** como chave primária.

### Vantagens
- Distribuição: não há contenção em sequências
- Clustering: melhor distribuição entre shards
- Replicação: sem conflitos de ID
- Auditoria: rastreabilidade global

### Implementação
- Gerados via `@GeneratedValue(strategy = GenerationType.UUID)`
- Armazenados como tipo `UUID` no PostgreSQL
- Usados em todos os relacionamentos (FK)

---

## 11. Tratamento de erro e resposta padrão

Para manter consistência, a API responde de forma padronizada.

### Componentes já preparados
- `core/dto/ApiResponse`
- `core/advice/GlobalExceptionHandler`

### Objetivo
- retornar mensagens claras
- facilitar o consumo pelo frontend
- padronizar erros de validação e regra de negócio

### Exemplo de estratégia
- erros de validação: `400 Bad Request`
- não autorizado: `401 Unauthorized`
- sem permissão: `403 Forbidden`
- recurso não encontrado: `404 Not Found`
- conflito de regra: `409 Conflict`
- erro interno: `500 Internal Server Error`

---

## 12. DTOs

A aplicação separa os objetos expostos na API do domínio interno.

### DTOs utilizados
- Entrada de dados (CreateXxxRequest, UpdateXxxRequest)
- Saída de dados (XxxDto)
- Evitar expor detalhes internos do domínio

### Benefícios
- menos acoplamento com o banco
- maior liberdade para evolução
- melhor controle dos dados expostos
- separação clara entre camadas

---

## 13. Clean Architecture

### O que foi priorizado
- clareza
- separação de responsabilidades
- organização por feature
- facilidade de manutenção

### O que foi evitado
- excesso de abstrações sem necessidade
- muitos adaptadores precoces
- arquitetura difícil de entender no início

### Resultado esperado
- código legível
- evolução gradual
- testes mais simples
- menor custo de manutenção


---

## 3. Visão em alto nível

A aplicação foi organizada em quatro blocos principais:

```text
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Controllers   │───▶│    Facades      │───▶│   Handlers      │───▶│  Repositories  │
│  (REST APIs)    │    │ (Orchestration) │    │ (Use Cases)     │    │ (Data Access)  │
└─────────────────┘    └─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │                       │
         ▼                       ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   ApiResponse   │◀───│   Result<T>     │◀───│ Business Rules   │◀───│ PostgreSQL DB   │
└─────────────────┘    └─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Como ler esse desenho

- **Controller**: expõe endpoints REST.
- **Facade**: organiza o fluxo de uma operação e conversa com vários handlers quando necessário.
- **Handler**: concentra o caso de uso e as regras de negócio daquele cenário.
- **Repository**: realiza leitura e escrita no banco.
- **ApiResponse / Result**: representam contratos e padronização de retorno.

---

## 4. Estrutura de pacotes

A base do projeto está organizada em `src/main/java/com/example/task_manager_backend/` e segue a lógica abaixo:

```text
com.example.task_manager_backend
├── TaskManagerBackendApplication.java
├── core
│   ├── advice
│   └── dto
├── features
│   ├── auth
│   │   ├── api
│   │   ├── dto
│   │   ├── facade
│   │   ├── handler
│   │   ├── model
│   │   └── repository
│   ├── projects
│   │   ├── api
│   │   ├── dto
│   │   ├── facade
│   │   ├── handler
│   │   ├── model
│   │   └── repository
│   └── tasks
│       ├── api
│       ├── dto
│       ├── facade
│       ├── handler
│       ├── model
│       └── repository
└── infrastructure
    ├── persistence
    └── security
```

### O que cada pasta representa

#### `core`
Contém componentes transversais da aplicação, como:
- resposta padrão da API (`ApiResponse`)
- tratamento global de exceções

#### `features`
Contém os módulos de negócio. Hoje os principais são:
- `auth`
- `projects`
- `tasks`

Cada feature possui suas próprias classes e evita dependências desnecessárias com as outras.

#### `infrastructure`
Contém os detalhes técnicos:
- configuração de persistência
- segurança
- integrações com framework
- implementação de pontos mais específicos da aplicação

---

## 5. Camadas da aplicação

## 5.1 Controllers / API

Os controllers são a porta de entrada da aplicação.

### Responsabilidades
- Receber requisições HTTP.
- Validar payloads de entrada.
- Chamar a facade correspondente.
- Retornar respostas HTTP apropriadas.

### Exemplos de classes
- `AuthController`
- `ProjectResource`
- `TaskController`

### Princípios dessa camada
- Não deve conter regra de negócio.
- Não deve falar diretamente com o banco.
- Deve ser fina e objetiva.

---

## 5.2 Facades

As facades fazem a orquestração da operação.

### Responsabilidades
- Coordenar fluxos de uso maiores.
- Combinar múltiplos handlers quando necessário.
- Manter os controllers simples.

### Exemplos de classes
- `AuthFacade`
- `ProjectFacade`
- `TaskFacade`

### Quando usar uma facade
Use uma facade quando uma operação envolver mais de uma etapa, por exemplo:
- validar usuário
- carregar projeto
- verificar permissão
- executar regra de negócio
- persistir alteração

---

## 5.3 Handlers

Os handlers representam os casos de uso principais.

### Responsabilidades
- Aplicar regras de negócio.
- Validar condições do domínio.
- Executar lógica de criação, atualização, busca e transição de estado.
- Chamar repositories quando necessário.

### Exemplos de classes
- `AuthHandler`
- `ProjectHandler`
- `TaskHandler`

### Regras que devem ficar aqui
No módulo de tarefas, por exemplo:
- uma tarefa `DONE` não pode voltar para `TODO`
- `CRITICAL` só pode ser fechada por `ADMIN`
- no máximo 5 tarefas `IN_PROGRESS` por responsável
- o responsável deve ser membro do projeto

---

## 5.4 Repositories

Os repositories concentram o acesso a dados.

### Responsabilidades
- Consultar e persistir no banco.
- Oferecer métodos de busca filtrada e ordenada.
- Encapsular detalhes do JPA ou outra tecnologia de persistência.

### Exemplos de interfaces
- `UserRepository`
- `ProjectRepository`
- `TaskRepository`

---

## 6. Módulos de negócio

## 6.1 Auth

O módulo de autenticação é responsável por login e autorização.

### Objetivos
- autenticar por email e senha
- emitir JWT
- proteger endpoints
- diferenciar perfis `ADMIN` e `MEMBER`

### Componentes principais
- `AuthController`
- `AuthFacade`
- `AuthHandler`
- `UserRepository`
- `User` / DTOs de login

### Regras esperadas
- somente usuários autenticados acessam a API protegida
- o perfil do usuário interfere nas permissões
- `ADMIN` tem privilégios adicionais em projetos e tarefas

---

## 6.2 Projects

O módulo de projetos organiza o contexto de trabalho.

### Objetivos
- criar, editar, listar e remover projetos
- registrar dono do projeto
- associar membros ao projeto
- garantir que apenas membros enxerguem o projeto

### Componentes principais
- `ProjectResource`
- `ProjectFacade`
- `ProjectHandler`
- `ProjectRepository`
- `Project` / DTOs

### Regras esperadas
- todo projeto possui um dono
- um projeto pode ter N membros
- somente usuários do projeto podem manipulá-lo
- `ADMIN` gerencia membros do projeto

---

## 6.3 Tasks

O módulo de tarefas é o coração do produto.

### Objetivos
- CRUD de tarefas dentro de projeto
- controle de status e prioridade
- atribuição de responsável
- filtros e ordenação
- busca textual
- resumo por status e prioridade

### Componentes principais
- `TaskController`
- `TaskFacade`
- `TaskHandler`
- `TaskRepository`
- `Task` / DTOs

### Regras de negócio principais
- `DONE` não volta para `TODO`
- `CRITICAL` só pode ser concluída por `ADMIN` do projeto
- um responsável não pode ter mais de 5 tarefas `IN_PROGRESS`
- o responsável deve ser membro do projeto
- apenas usuários vinculados ao projeto podem consultar ou alterar a tarefa

---

## 7. Fluxo de requisição

O fluxo completo de uma operação segue esta sequência:

```text
HTTP Request
   ↓
Controller
   ↓
Facade
   ↓
Handler
   ↓
Repository
   ↓
Database
   ↓
Repository
   ↓
Handler
   ↓
Facade
   ↓
Controller
   ↓
HTTP Response
```

### Exemplo prático: criar tarefa

1. O frontend chama `POST /api/tasks`.
2. O `TaskController` recebe o payload.
3. O controller chama a `TaskFacade`.
4. A facade delega para o `TaskHandler`.
5. O handler valida:
   - se o projeto existe
   - se o responsável pertence ao projeto
   - se o limite de WIP foi respeitado
6. O handler persiste a tarefa via `TaskRepository`.
7. O retorno sobe novamente até o controller.
8. A API responde com status apropriado e corpo padronizado.

---

## 8. Persistência e banco de dados

A persistência foi planejada para **PostgreSQL**.

### Estratégia
- PostgreSQL como banco principal.
- Flyway para versionar o schema.
- Hibernate/JPA para mapeamento de entidades e consultas.

### Arquivos importantes
- `src/main/resources/application.properties`
- `src/main/resources/db/migration/V1__create_initial_schema.sql`

### Benefícios dessa abordagem
- histórico claro das mudanças de banco
- reprodução fácil do ambiente
- menos risco de diferenças entre máquinas
- melhor controle sobre evolução do schema

### Tabelas principais previstas
- `users`
- `projects`
- `project_members`
- `tasks`

---

## 9. Segurança

A segurança da aplicação está sendo preparada em `infrastructure/security`.

### Estratégia definida
- autenticação com JWT
- autorização baseada em papéis e pertencimento ao projeto
- proteção de endpoints por Spring Security

### Arquivo base
- `SecurityConfig.java`

### Regras esperadas
- usuários `MEMBER` só manipulam tarefas conforme permissão
- usuários `ADMIN` possuem funções administrativas do projeto

---

## 10. Tratamento de erro e resposta padrão

Para manter consistência, a API deve responder de forma padronizada.

### Componentes já preparados
- `core/dto/ApiResponse`
- `core/advice/GlobalExceptionHandler`

### Objetivo
- retornar mensagens claras
- facilitar o consumo pelo frontend
- padronizar erros de validação e regra de negócio

### Exemplo de estratégia
- erros de validação: `400 Bad Request`
- não autorizado: `401 Unauthorized`
- sem permissão: `403 Forbidden`
- recurso não encontrado: `404 Not Found`
- conflito de regra: `409 Conflict`

---

## 11. DTOs

A aplicação separa os objetos expostos na API.

### DTOs
Os DTOs servem para:
- entrada de dados
- saída de dados
- evitar expor detalhes internos do domínio
- 
### Benefícios
- menos acoplamento com o banco
- maior liberdade para evolução
- melhor controle dos dados expostos

---

## 12. Clean architecture

### O que foi priorizado
- clareza
- separação de responsabilidades
- organização por feature
- facilidade de manutenção

### O que foi evitado
- excesso de abstrações sem necessidade
- muitos adaptadores precoces
- arquitetura difícil de entender no início

### Resultado esperado
- código legível
- evolução gradual
- testes mais simples
- menor custo de manutenção

