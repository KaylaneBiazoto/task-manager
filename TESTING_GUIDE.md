# 🧪 Guia de Testes - Task Manager Backend

## 📋 Índice

1. [Visão Geral](#visão-geral)
2. [Estrutura de Testes](#estrutura-de-testes)
3. [Padrões de Teste](#padrões-de-teste)
4. [Como Executar](#como-executar)
5. [Cobertura de Testes](#cobertura-de-testes)
6. [Boas Práticas](#boas-práticas)

---

## 🎯 Visão Geral

Este projeto implementa três tipos de testes:

1. **Testes Unitários** - Testam handlers e lógica de negócio em isolamento com Mockito
2. **Testes de Integração** - Testam controllers com MockMvc
3. **Testes End-to-End** - Testam fluxos completos com o contexto Spring completo

### Stack de Testes

- **JUnit 5** - Framework de testes
- **Mockito** - Mocking de dependências
- **AssertJ** - Assertions fluentes
- **Spring Test** - Testes de integração Spring
- **MockMvc** - Testes de controllers HTTP

---

## 📁 Estrutura de Testes

```
src/test/java/com/example/task_manager_backend/
├── TaskManagerBackendApplicationTests.java          # Testes de inicialização
├── TaskManagerEndToEndTest.java                    # Testes E2E completos
├── features/
│   ├── projects/
│   │   ├── api/
│   │   │   └── ProjectControllerIntegrationTest.java
│   │   ├── usecases/
│   │   │   ├── CreateProjectHandlerTest.java
│   │   │   └── AddProjectMemberHandlerTest.java
│   │   └── services/
│   │       └── ProjectServiceTest.java (TODO)
│   └── tasks/
│       ├── api/
│       │   └── TaskControllerIntegrationTest.java
│       ├── usecases/
│       │   ├── CreateTaskHandlerTest.java
│       │   └── UpdateTaskHandlerTest.java
│       └── services/
│           └── TaskFacadeTest.java (TODO)
└── auth/
    └── api/
        └── AuthControllerIntegrationTest.java (TODO)
```

---

## 🔬 Padrões de Teste

### 1. Testes Unitários com Mockito

**Padrão: Arrange-Act-Assert (AAA)**

```java
@ExtendWith(MockitoExtension.class)
class CreateProjectHandlerTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private UserRepository userRepository;

    private CreateProjectHandler handler;

    @BeforeEach
    void setUp() {
        handler = new CreateProjectHandler(
            projectRepository,
            projectMemberRepository,
            userRepository
        );
    }

    @Test
    void testExecute_WithValidData_ShouldCreateProject() {
        // Arrange - Preparar dados e mocks
        Long ownerId = 1L;
        User owner = new User();
        owner.setId(ownerId);
        owner.setActive(true);

        CreateProjectRequest request = new CreateProjectRequest();
        request.setName("Test Project");
        request.setOwnerId(ownerId);

        Project savedProject = new Project();
        savedProject.setId(1L);
        savedProject.setName("Test Project");

        when(userRepository.findByIdAndActiveTrue(ownerId))
            .thenReturn(Optional.of(owner));
        when(projectRepository.save(any(Project.class)))
            .thenReturn(savedProject);

        // Act - Executar o método sendo testado
        Project result = handler.execute(request, ownerId);

        // Assert - Verificar o resultado
        assertThat(result)
            .isNotNull()
            .extracting(Project::getId, Project::getName)
            .containsExactly(1L, "Test Project");

        // Verify - Verificar chamadas aos mocks
        verify(userRepository).findByIdAndActiveTrue(ownerId);
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void testExecute_WithInvalidOwner_ShouldThrowException() {
        // Arrange
        Long invalidOwnerId = 999L;
        CreateProjectRequest request = new CreateProjectRequest();
        request.setName("Test Project");

        when(userRepository.findByIdAndActiveTrue(invalidOwnerId))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(request, invalidOwnerId))
            .isInstanceOf(ProjectBusinessException.class)
            .hasMessage("Owner user not found");

        // Verify - Garantir que não chamou save
        verify(projectRepository, never()).save(any());
    }
}
```

**Boas Práticas:**
- Use `@ExtendWith(MockitoExtension.class)` para habilitar Mockito
- Nomeie testes com padrão: `testNomeMetodo_Cenario_ResultadoEsperado()`
- Use `@Mock` para dependências
- Use `when().thenReturn()` para configurar comportamento esperado
- Use `verify()` para garantir que métodos foram chamados corretamente
- Use `never()` para garantir que algo NÃO foi chamado

### 2. Testes de Integração com MockMvc

**Padrão: Teste de Controller com Mocks de Service**

```java
@WebMvcTest(ProjectController.class)
class ProjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectService projectService;

    @Test
    void testCreateProject_ShouldReturn201Created() throws Exception {
        // Arrange
        CreateProjectRequest request = new CreateProjectRequest();
        request.setName("New Project");
        request.setOwnerId(1L);

        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(1L);
        projectDto.setName("New Project");

        when(projectService.createProject(any(CreateProjectRequest.class), eq(1L)))
            .thenReturn(projectDto);

        // Act & Assert
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").value(1L))
            .andExpect(jsonPath("$.data.name").value("New Project"));

        verify(projectService).createProject(any(CreateProjectRequest.class), eq(1L));
    }

    @Test
    void testGetProject_WithValidId_ShouldReturn200Ok() throws Exception {
        // Arrange
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(1L);
        projectDto.setName("Test Project");

        when(projectService.getProjectById(1L)).thenReturn(projectDto);

        // Act & Assert
        mockMvc.perform(get("/api/projects/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").value(1L));

        verify(projectService).getProjectById(1L);
    }

    @Test
    void testCreateProject_WithInvalidData_ShouldReturn400BadRequest() throws Exception {
        // Arrange
        CreateProjectRequest request = new CreateProjectRequest();
        // Missing required fields

        // Act & Assert
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(projectService, never()).createProject(any(), any());
    }
}
```

**Boas Práticas:**
- Use `@WebMvcTest(Controller.class)` para testar apenas o controller
- Use `@MockBean` para mockar o service
- Use `mockMvc.perform()` para fazer requests HTTP
- Use `andExpect()` para validar respostas
- Valide status HTTP, estrutura JSON e valores específicos

### 3. Testes End-to-End

**Padrão: Teste com Contexto Spring Completo**

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TaskManagerEndToEndTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCompleteWorkflow_CreateUserProjectAndTask() throws Exception {
        // === Step 1: Create User ===
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("alice");
        userRequest.setEmail("alice@example.com");
        userRequest.setPassword("pass123");

        MvcResult userResult = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
            .andExpect(status().isCreated())
            .andReturn();

        // === Step 2: Create Project ===
        CreateProjectRequest projectRequest = new CreateProjectRequest();
        projectRequest.setName("Test Project");
        projectRequest.setOwnerId(1L);

        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectRequest)))
            .andExpect(status().isCreated());

        // === Step 3: Add Member ===
        AddProjectMemberRequest memberRequest = new AddProjectMemberRequest();
        memberRequest.setUserId(2L);
        memberRequest.setProjectRole("MEMBER");

        mockMvc.perform(post("/api/projects/1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberRequest)))
            .andExpect(status().isCreated());

        // === Step 4: Create Task ===
        CreateTaskRequest taskRequest = new CreateTaskRequest();
        taskRequest.setTitle("Test Task");
        taskRequest.setProjectId(1L);
        taskRequest.setAssigneeId(2L);
        taskRequest.setPriority(TaskPriority.HIGH);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskRequest)))
            .andExpect(status().isCreated());

        // === Step 5: Update Task Status ===
        UpdateTaskRequest updateRequest = new UpdateTaskRequest();
        updateRequest.setStatus(TaskStatus.IN_PROGRESS);

        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk());
    }
}
```

**Boas Práticas:**
- Use `@SpringBootTest` para contexto completo
- Teste fluxos completos que envolvem múltiplas operações
- Use `MvcResult` para capturar e reutilizar dados entre chamadas
- Valide que regras de negócio funcionam em integração

---

## 🚀 Como Executar

### Executar todos os testes

```bash
cd /home/kaylane.biazoto@db1.com.br/Music/task-manager-backend
./mvnw test
```

### Executar testes de uma classe específica

```bash
./mvnw test -Dtest=CreateProjectHandlerTest
```

### Executar testes de um pacote específico

```bash
./mvnw test -Dtest=com/example/task_manager_backend/features/projects/**
```

### Executar com cobertura de código

```bash
./mvnw test jacoco:report
# Relatório em: target/site/jacoco/index.html
```

### Executar apenas testes de integração

```bash
./mvnw test -Dtest=**IntegrationTest
```

### Executar apenas testes unitários

```bash
./mvnw test -Dtest=**Test -DexcludedGroups=integration
```

---

## 📊 Cobertura de Testes

### Testes Implementados

| Classe | Tipo | Casos | Status |
|--------|------|-------|--------|
| CreateProjectHandler | Unitário | 3 | ✅ |
| AddProjectMemberHandler | Unitário | 7 | ✅ |
| CreateTaskHandler | Unitário | 6 | ✅ |
| UpdateTaskHandler | Unitário | 8 | ✅ |
| ProjectController | Integração | 6 | ✅ |
| TaskController | Integração | 9 | ✅ |
| End-to-End | E2E | 3 | ✅ |

### Testes TODO (Próximos Passos)

- [ ] ProjectService (unitário)
- [ ] TaskFacade (unitário)
- [ ] UserService (unitário)
- [ ] AuthController (integração)
- [ ] DeleteProjectHandler (unitário)
- [ ] DeleteTaskHandler (unitário)
- [ ] GetProjectByIdHandler (unitário)
- [ ] ListProjectsHandler (unitário)
- [ ] Testes de validação de constraints
- [ ] Testes de erro 404 e 409

### Cobertura Desejada

- **Handlers**: 90%+ (lógica crítica de negócio)
- **Controllers**: 80%+ (validações e respostas)
- **Services**: 85%+ (orquestração)
- **Overall**: 75%+

---

## 💡 Boas Práticas

### 1. Nomenclatura de Testes

```
✅ Bom:   testCreateProject_WithValidOwner_ShouldSucceed()
❌ Ruim:  testCreate()
❌ Ruim:  test1()
```

**Padrão:** `test[NomeMetodo]_[Cenario]_[ResultadoEsperado]()`

### 2. Estrutura AAA

```java
@Test
void testExample() {
    // Arrange - Preparar dados e dependências
    
    // Act - Executar
    
    // Assert - Validar resultado
}
```

### 3. Um Teste por Cenário

```java
✅ Bom:
@Test
void testCreateProject_WithValidOwner_ShouldSucceed() { }

@Test
void testCreateProject_WithInvalidOwner_ShouldThrowException() { }

❌ Ruim:
@Test
void testCreateProject() {
    // Testa múltiplos cenários
}
```

### 4. Use Assertions Fluentes (AssertJ)

```java
✅ Bom:
assertThat(result)
    .isNotNull()
    .extracting(Project::getId, Project::getName)
    .containsExactly(1L, "Test");

❌ Ruim:
assertTrue(result != null);
assertEquals(result.getId(), 1L);
assertEquals(result.getName(), "Test");
```

### 5. Mock apenas o necessário

```java
✅ Bom:
@Mock
private ProjectRepository projectRepository;

❌ Ruim:
@Mock
private ProjectRepository projectRepository;

@Mock
private ProjectMemberRepository projectMemberRepository;

@Mock
private UserRepository userRepository;

@Mock
private ProjectService projectService; // Testar handler, não service
```

### 6. Verifique comportamento, não implementação

```java
✅ Bom:
assertThat(result.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);

❌ Ruim:
// Não valide detalhes de implementação
verify(taskRepository).save(any());
verify(taskRepository).flush();
```

### 7. Use Test Data Builders

```java
class TaskTestDataBuilder {
    private String title = "Default Title";
    private TaskStatus status = TaskStatus.TODO;
    
    public TaskTestDataBuilder withTitle(String title) {
        this.title = title;
        return this;
    }
    
    public Task build() {
        Task task = new Task();
        task.setTitle(title);
        task.setStatus(status);
        return task;
    }
}

// Uso:
Task task = new TaskTestDataBuilder()
    .withTitle("Custom Title")
    .build();
```

---

## 🎯 Próximos Passos

1. **Aumentar Cobertura**
   - Implementar testes para GetProjectByIdHandler
   - Implementar testes para ListProjectsHandler
   - Implementar testes para DeleteProjectHandler

2. **Testes de Validação**
   - Testar constraints (@NotBlank, @NotNull, etc)
   - Testar validações de negócio (WIP limit, CRITICAL tasks)
   - Testar transições de status inválidas

3. **Testes de Erro**
   - Testar respostas 404
   - Testar respostas 409
   - Testar respostas 400 (validação)

4. **Performance**
   - Implementar testes de performance
   - Validar queries otimizadas
   - Testar paginação com grandes datasets

5. **Integração com BD**
   - Migrar testes E2E para usar BD real
   - Implementar TestContainers para isolamento
   - Implementar rollback automático entre testes

---

**📝 Última Atualização:** Abril 2026

