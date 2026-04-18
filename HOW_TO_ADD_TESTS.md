# 🚀 GUIA PARA ADICIONAR MAIS TESTES

## 📝 Visão Geral

Este documento fornece instruções passo-a-passo para adicionar mais testes ao projeto.

---

## 1️⃣ Adicionar Testes para GetProjectByIdHandler

### Arquivo
`src/test/java/com/example/task_manager_backend/features/projects/usecases/GetProjectByIdHandlerTest.java`

### Casos de Teste Recomendados (3-4 testes)

```java
@ExtendWith(MockitoExtension.class)
class GetProjectByIdHandlerTest {

    @Mock
    private ProjectRepository projectRepository;

    private GetProjectByIdHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetProjectByIdHandler(projectRepository);
    }

    @Test
    void testExecute_WithValidId_ShouldReturnProject() {
        // Arrange
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setName("Test Project");
        project.setActive(true);

        when(projectRepository.findByIdAndActiveTrue(projectId))
            .thenReturn(Optional.of(project));

        // Act
        Project result = handler.execute(projectId);

        // Assert
        assertThat(result)
            .isNotNull()
            .extracting(Project::getId, Project::getName)
            .containsExactly(projectId, "Test Project");

        verify(projectRepository).findByIdAndActiveTrue(projectId);
    }

    @Test
    void testExecute_WithInvalidId_ShouldThrowProjectNotFoundException() {
        // Arrange
        Long invalidId = 999L;
        when(projectRepository.findByIdAndActiveTrue(invalidId))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(invalidId))
            .isInstanceOf(ProjectNotFoundException.class)
            .hasMessage("Project not found");

        verify(projectRepository).findByIdAndActiveTrue(invalidId);
    }

    @Test
    void testExecute_WithInactiveProject_ShouldThrowProjectNotFoundException() {
        // Arrange
        Long projectId = 1L;
        when(projectRepository.findByIdAndActiveTrue(projectId))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(projectId))
            .isInstanceOf(ProjectNotFoundException.class);
    }
}
```

---

## 2️⃣ Adicionar Testes para ListProjectsHandler

### Arquivo
`src/test/java/com/example/task_manager_backend/features/projects/usecases/ListProjectsHandlerTest.java`

### Casos de Teste Recomendados (3-4 testes)

```java
@ExtendWith(MockitoExtension.class)
class ListProjectsHandlerTest {

    @Mock
    private ProjectRepository projectRepository;

    private ListProjectsHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ListProjectsHandler(projectRepository);
    }

    @Test
    void testExecute_ShouldReturnProjectPage() {
        // Arrange
        Project project1 = new Project();
        project1.setId(1L);
        project1.setName("Project 1");

        Project project2 = new Project();
        project2.setId(2L);
        project2.setName("Project 2");

        Page<Project> projectPage = new PageImpl<>(
            List.of(project1, project2),
            PageRequest.of(0, 10),
            2
        );

        when(projectRepository.findByActiveTrue(any(Pageable.class)))
            .thenReturn(projectPage);

        // Act
        Page<Project> result = handler.execute(PageRequest.of(0, 10));

        // Assert
        assertThat(result)
            .isNotNull()
            .hasSize(2);

        verify(projectRepository).findByActiveTrue(any(Pageable.class));
    }

    @Test
    void testExecute_WithEmptyDatabase_ShouldReturnEmptyPage() {
        // Arrange
        Page<Project> emptyPage = new PageImpl<>(
            Collections.emptyList(),
            PageRequest.of(0, 10),
            0
        );

        when(projectRepository.findByActiveTrue(any(Pageable.class)))
            .thenReturn(emptyPage);

        // Act
        Page<Project> result = handler.execute(PageRequest.of(0, 10));

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void testExecute_WithSearch_ShouldFilterProjects() {
        // Arrange
        Project project = new Project();
        project.setId(1L);
        project.setName("API Project");

        Page<Project> filteredPage = new PageImpl<>(
            List.of(project),
            PageRequest.of(0, 10),
            1
        );

        when(projectRepository.findByNameContainingIgnoreCaseAndActiveTrue(
            eq("API"), any(Pageable.class)))
            .thenReturn(filteredPage);

        // Act
        Page<Project> result = handler.execute("API", null, PageRequest.of(0, 10));

        // Assert
        assertThat(result).hasSize(1);
        verify(projectRepository).findByNameContainingIgnoreCaseAndActiveTrue(
            eq("API"), any(Pageable.class));
    }
}
```

---

## 3️⃣ Adicionar Testes para UpdateProjectHandler

### Arquivo
`src/test/java/com/example/task_manager_backend/features/projects/usecases/UpdateProjectHandlerTest.java`

### Casos de Teste Recomendados (4-5 testes)

```java
@ExtendWith(MockitoExtension.class)
class UpdateProjectHandlerTest {

    @Mock
    private ProjectRepository projectRepository;

    private UpdateProjectHandler handler;

    @BeforeEach
    void setUp() {
        handler = new UpdateProjectHandler(projectRepository);
    }

    @Test
    void testExecute_WithValidData_ShouldUpdateProject() {
        // Arrange
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setName("Old Name");
        project.setDescription("Old Description");

        UpdateProjectRequest request = new UpdateProjectRequest();
        request.setName("New Name");
        request.setDescription("New Description");

        Project updatedProject = new Project();
        updatedProject.setId(projectId);
        updatedProject.setName("New Name");
        updatedProject.setDescription("New Description");

        when(projectRepository.findByIdAndActiveTrue(projectId))
            .thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class)))
            .thenReturn(updatedProject);

        // Act
        Project result = handler.execute(projectId, request);

        // Assert
        assertThat(result)
            .extracting(Project::getName, Project::getDescription)
            .containsExactly("New Name", "New Description");

        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void testExecute_WithInvalidId_ShouldThrowProjectNotFoundException() {
        // Arrange
        UpdateProjectRequest request = new UpdateProjectRequest();

        when(projectRepository.findByIdAndActiveTrue(999L))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(999L, request))
            .isInstanceOf(ProjectNotFoundException.class);

        verify(projectRepository, never()).save(any());
    }

    @Test
    void testExecute_WithPartialUpdate_ShouldUpdateOnlyProvidedFields() {
        // Arrange
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setName("Old Name");
        project.setDescription("Old Description");

        UpdateProjectRequest request = new UpdateProjectRequest();
        request.setName("New Name");
        request.setDescription(null); // Não atualizar

        Project updatedProject = new Project();
        updatedProject.setName("New Name");
        updatedProject.setDescription("Old Description"); // Mantém valor anterior

        when(projectRepository.findByIdAndActiveTrue(projectId))
            .thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class)))
            .thenReturn(updatedProject);

        // Act
        Project result = handler.execute(projectId, request);

        // Assert
        assertThat(result.getDescription()).isEqualTo("Old Description");
    }
}
```

---

## 4️⃣ Adicionar Testes para DeleteProjectHandler

### Arquivo
`src/test/java/com/example/task_manager_backend/features/projects/usecases/DeleteProjectHandlerTest.java`

### Casos de Teste Recomendados (2-3 testes)

```java
@ExtendWith(MockitoExtension.class)
class DeleteProjectHandlerTest {

    @Mock
    private ProjectRepository projectRepository;

    private DeleteProjectHandler handler;

    @BeforeEach
    void setUp() {
        handler = new DeleteProjectHandler(projectRepository);
    }

    @Test
    void testExecute_WithValidId_ShouldSoftDeleteProject() {
        // Arrange
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setActive(true);

        when(projectRepository.findByIdAndActiveTrue(projectId))
            .thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class)))
            .thenReturn(project);

        // Act
        handler.execute(projectId);

        // Assert
        verify(projectRepository).findByIdAndActiveTrue(projectId);
        verify(projectRepository).save(argThat(p -> !p.getActive()));
    }

    @Test
    void testExecute_WithInvalidId_ShouldThrowProjectNotFoundException() {
        // Arrange
        when(projectRepository.findByIdAndActiveTrue(999L))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(999L))
            .isInstanceOf(ProjectNotFoundException.class);

        verify(projectRepository, never()).save(any());
    }
}
```

---

## 5️⃣ Adicionar Testes para GetTaskByIdHandler

### Arquivo
`src/test/java/com/example/task_manager_backend/features/tasks/usecases/GetTaskByIdHandlerTest.java`

### Casos de Teste Recomendados (2-3 testes)

```java
@ExtendWith(MockitoExtension.class)
class GetTaskByIdHandlerTest {

    @Mock
    private TaskRepository taskRepository;

    private GetTaskByIdHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GetTaskByIdHandler(taskRepository);
    }

    @Test
    void testExecute_WithValidId_ShouldReturnTask() {
        // Arrange
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Test Task");
        task.setStatus(TaskStatus.TODO);

        when(taskRepository.findByIdAndActiveTrue(taskId))
            .thenReturn(Optional.of(task));

        // Act
        Task result = handler.execute(taskId);

        // Assert
        assertThat(result)
            .isNotNull()
            .extracting(Task::getId, Task::getTitle)
            .containsExactly(taskId, "Test Task");
    }

    @Test
    void testExecute_WithInvalidId_ShouldThrowTaskNotFoundException() {
        // Arrange
        when(taskRepository.findByIdAndActiveTrue(999L))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> handler.execute(999L))
            .isInstanceOf(TaskNotFoundException.class)
            .hasMessage("Task not found");
    }
}
```

---

## 📋 Checklist para Adicionar Novo Teste

Use este checklist ao criar um novo teste:

- [ ] Criar arquivo em estrutura correta: `src/test/java/[pacote]/[NomeHandler]Test.java`
- [ ] Adicionar `@ExtendWith(MockitoExtension.class)`
- [ ] Adicionar `@Mock` para cada dependência
- [ ] Implementar `@BeforeEach` para inicializar handler
- [ ] Criar mínimo 3 testes por handler
- [ ] Nomear testes com padrão: `test[Metodo]_[Cenario]_[Resultado]()`
- [ ] Usar estrutura AAA (Arrange, Act, Assert)
- [ ] Mockar dependências com `when().thenReturn()`
- [ ] Validar resultado com `assertThat()`
- [ ] Verificar chamadas com `verify()`
- [ ] Testar casos de sucesso E erro
- [ ] Usar `assertThatThrownBy()` para exceções
- [ ] Executar `mvn test` para validar

---

## 🔧 Executar Testes

### Todos
```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn clean test
```

### Específico
```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn test -Dtest=GetProjectByIdHandlerTest
```

### Com Padrão
```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn test -Dtest=*ProjectHandler*Test
```

---

## 💡 Dicas e Truques

### 1. Usar ArgumentCaptor para Validações Complexas
```java
@Test
void testSaveWithValidation() {
    ArgumentCaptor<Project> captor = ArgumentCaptor.forClass(Project.class);
    
    handler.execute(request);
    
    verify(projectRepository).save(captor.capture());
    Project saved = captor.getValue();
    
    assertThat(saved.getName()).isEqualTo("Expected Name");
}
```

### 2. Usar Spy para Testes Parciais
```java
@Test
void testWithSpy() {
    ProjectRepository spyRepo = spy(new ProjectRepository());
    
    doReturn(Optional.of(project)).when(spyRepo).findByIdAndActiveTrue(1L);
    
    // Agora spyRepo comporta-se como um mock, mas com implementação real
}
```

### 3. Usar ArgumentMatchers para Flexibilidade
```java
// Aceitar qualquer Long
when(repository.findById(anyLong())).thenReturn(Optional.of(project));

// Aceitar apenas valores específicos
when(repository.findById(eq(1L))).thenReturn(Optional.of(project));

// Aceitar strings contendo "test"
when(repository.findByNameContaining(argThat(s -> s.contains("test"))))
    .thenReturn(List.of());
```

### 4. Validar Múltiplos Argumentos
```java
verify(repository).save(argThat(p ->
    p.getName().equals("Test") &&
    p.getActive() == true &&
    p.getId() != null
));
```

---

## 📚 Referências

- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html)
- [AssertJ Documentation](https://assertj.github.io/assertj-core-features-highlight.html)

---

**Última Atualização:** Abril 2026

