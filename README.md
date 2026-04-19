# Task Manager Backend

Um sistema simplificado de gerenciamento de tarefas (task manager) para equipes de desenvolvimento. O sistema permite que usuários criem, editem, acompanhem e organizem tarefas dentro de projetos.

## Pré-requisitos

- Java 21 ou superior
- Maven 3.6+
- PostgreSQL 12+ (opcional, por padrão usa H2 em memória)

## Instalação e Execução

### 1. Clonar o repositório
```bash
git clone <repository-url>
cd task-manager-backend
```

### 2. Executar a aplicação

#### Com Maven (desenvolvimento)
```bash
mvn spring-boot:run
```

#### Compilar e executar JAR
```bash
mvn clean package
java -jar target/task-manager-backend-0.0.1-SNAPSHOT.jar
```

### 3. Acessar a aplicação

A aplicação estará disponível em: `http://localhost:8080`

#### Swagger UI (Documentação da API)
```
http://localhost:8080/swagger-ui.html
```

#### H2 Database Console (desenvolvimento)
```
http://localhost:8080/h2-console
```

## Configuração

### Banco de Dados Padrão (H2 em memória)
Por padrão, a aplicação usa H2 em memória para desenvolvimento rápido.

### Usar PostgreSQL em Produção
Modifique `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/task_manager
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQL10Dialect
```

### Docker Compose (Recomendado para Produção)
Use o arquivo `docker-compose.yml` para executar a aplicação com PostgreSQL:

```bash
docker-compose up -d
```

Isso irá iniciar:
- PostgreSQL 15 na porta 5432
- Task Manager Backend na porta 8080

Para parar os containers:
```bash
docker-compose down
```

Para remover volumes (limpar dados):
```bash
docker-compose down -v
```

## Endpoints Principais

### Autenticação
- `POST /api/auth/register` - Registrar novo usuário
- `POST /api/auth/login` - Fazer login

### Tarefas
- `POST /api/tasks` - Criar tarefa
- `GET /api/tasks/{taskId}` - Obter tarefa por ID
- `GET /api/tasks/project/{projectId}` - Listar tarefas do projeto
- `PUT /api/tasks/{taskId}` - Atualizar tarefa
- `DELETE /api/tasks/{taskId}` - Deletar tarefa

### Projetos
- `POST /api/projects` - Criar projeto
- `GET /api/projects/{projectId}` - Obter projeto por ID
- `GET /api/projects` - Listar projetos
- `PUT /api/projects/{projectId}` - Atualizar projeto
- `DELETE /api/projects/{projectId}` - Deletar projeto
- `POST /api/projects/{projectId}/members` - Adicionar membro ao projeto
- `GET /api/projects/{projectId}/members` - Listar membros do projeto

### Relatórios
- `GET /api/reports/project/{projectId}` - Gerar relatório do projeto

Para documentação completa de todos os endpoints, acesse: `http://localhost:8080/swagger-ui.html`

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/example/task_manager_backend/
│   │       ├── core/              # Configurações e utilidades globais
│   │       ├── features/          # Funcionalidades (auth, tasks, projects, reports)
│   │       └── infrastructure/    # Infraestrutura (segurança, banco de dados)
│   └── resources/
│       └── application.properties  # Configurações da aplicação
└── test/
    └── java/                       # Testes unitários
```

## Testes

### Executar testes
```bash
mvn test
```

### Cobertura de testes (JaCoCo)
```bash
mvn jacoco:report
# Relatório disponível em: target/site/jacoco/index.html
```

## Docker

### Construir imagem Docker
```bash
docker build -t task-manager-backend:latest .
```

### Executar container
```bash
docker run -p 8080:8080 task-manager-backend:latest
```

### Com variáveis de ambiente (PostgreSQL)
```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/task_manager \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=password \
  -e SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQL10Dialect \
  task-manager-backend:latest
```

## Segurança

- A aplicação usa Spring Security com JWT para autenticação
- Token secreto pode ser configurado em `api.security.token.secret` no `application.properties`
- Para produção, altere a chave secreta!

## Decisões Técnicas e Tradeoffs

### 1. Banco de Dados H2 em Desenvolvimento
**Decisão**: Usar H2 em memória como padrão de desenvolvimento
- ✅ **Vantagens**: Rápido setup, sem dependências externas, ideal para testes
- ❌ **Tradeoffs**: Dados não persistem entre reinicializações, não representa ambiente de produção

### 2. Spring Boot + Spring Data JPA
**Decisão**: Framework consolidado para desenvolvimento ágil
- ✅ **Vantagens**: Comunidade grande, documentação extensa, produtividade alta
- ❌ **Tradeoffs**: Menos controle fino sobre queries, overhead em aplicações muito simples

### 3. JWT para Autenticação
**Decisão**: Tokens stateless com JWT
- ✅ **Vantagens**: Escalável, sem estado no servidor, suporta múltiplos servidores
- ❌ **Tradeoffs**: Token não pode ser revogado imediatamente, requer HTTPS em produção

### 4. Arquitetura em Camadas com Use Cases
**Decisão**: Separação clara de responsabilidades com handlers
- ✅ **Vantagens**: Código testável, manutenível, fácil de evoluir
- ❌ **Tradeoffs**: Mais boilerplate inicial, mais classes para simples operações CRUD

### 5. H2 Database Console Habilitado
**Decisão**: Permitir acesso ao console H2 em desenvolvimento
- ✅ **Vantagens**: Facilita debug, visualização de dados em tempo real
- ❌ **Tradeoffs**: Segurança (desabilitar em produção), não disponível com PostgreSQL

## O Que Faria Diferente com Mais Tempo

### 1. **PostgreSQL** 
- Substituir H2 por PostgreSQL como banco padrão
- Implementar migrations com Flyway
- Benefício: Dados persistentes, suporte a múltiplas conexões, melhor performance

### 2. **Exclusão em Cascata**
- Implementar delete cascade nas relações (Projeto → Tarefas, etc)
- Atualmente, existe uma exclusão manual de tarefas antes de deletar projeto
- Benefício: Integridade referencial automática, operações mais seguras

### 3. **Cacheamento com Redis**
- Cache em memória para queries frequentes (projetos, tarefas, relatórios)
- Implementar invalidação de cache inteligente
- Benefício: Redução de queries ao banco, melhor performance

### 4. **Sistema de Auditoria**
- Rastrear todas as mudanças (create, update, delete) com user e timestamp
- Histórico completo de quem fez o quê e quando
- Benefício: Compliance, rastreabilidade, segurança

### 5. **Cobertura de Testes Expandida**
- Aumentar cobertura de testes unitários para >80%
- Implementar testes de integração e E2E
- Adicionar testes de performance
- Benefício: Maior confiabilidade, detecção precoce de bugs

### 6. **Frontend com Angular**
- Desenvolver interface web completa com Angular
- Benefício: Experiência do usuário, interface intuitiva


