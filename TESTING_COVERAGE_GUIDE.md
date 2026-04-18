# 📊 GUIA DE TESTES E COBERTURA DE CÓDIGO

## 🧪 Comandos de Teste

### 1️⃣ Executar TODOS os testes
```bash
cd /home/kaylane.biazoto@db1.com.br/Music/task-manager-backend
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn clean test
```

**Resultado esperado:**
```
Tests run: 27, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

### 2️⃣ Executar testes COM COBERTURA DE CÓDIGO (JaCoCo)
```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn clean test jacoco:report
```

**Isso vai:**
- ✅ Executar todos os 27 testes
- ✅ Gerar relatório de cobertura JaCoCo
- ✅ Salvar relatório em: `target/site/jacoco/index.html`

---

### 3️⃣ Visualizar o Relatório de Cobertura

**No Linux/Mac:**
```bash
# Abrir no navegador padrão
xdg-open target/site/jacoco/index.html

# Ou copiar o caminho completo:
echo "file://$(pwd)/target/site/jacoco/index.html"
```

**No Windows:**
```bash
start target\site\jacoco\index.html
```

---

## 📈 Entender o Relatório de Cobertura

### Estrutura do Relatório

O relatório mostra **3 tipos de cobertura**:

| Métrica | Significado |
|---------|-------------|
| **Line Coverage** | % de linhas de código executadas pelos testes |
| **Branch Coverage** | % de decisões (if/else) cobertas |
| **Complexity** | Complexidade ciclomática |

### Exemplo de Saída

```
Package                                Instructions    Branches    Cxty    Lines   Methods   Classes
com.example.task_manager_backend              87%         85%        12      89%      91%      95%
├─ features/projects/usecases                 95%         92%         8      96%      98%     100%
│  ├─ CreateProjectHandler                   100%        100%         3     100%     100%     100%
│  └─ AddProjectMemberHandler                 90%         85%        10      92%      95%     100%
├─ features/tasks/usecases                    92%         88%        12      94%      93%      95%
│  ├─ CreateTaskHandler                      100%        100%         4     100%     100%     100%
│  └─ UpdateTaskHandler                       85%         76%        20      88%      90%     100%
```

---

## 🎯 Metas de Cobertura

Recomendações por tipo de classe:

| Tipo | Meta |
|------|------|
| Handlers (Use Cases) | **90%+** |
| Services (Facades) | **80%+** |
| Controllers | **75%+** |
| Repositories | **50%+** (low priority) |
| Models/Entities | **30%+** (low priority) |
| **GLOBAL** | **70%+** |

---

## 📋 Atalhos Úteis

### Executar testes rápidos (sem limpeza)
```bash
mvn test
```

### Executar testes com limpeza completa
```bash
mvn clean test
```

### Executar uma classe específica
```bash
mvn test -Dtest=CreateProjectHandlerTest
```

### Executar vários testes com padrão
```bash
mvn test -Dtest=*HandlerTest
```

### Pular testes (apenas compilar)
```bash
mvn clean package -DskipTests
```

### Cobertura com limite mínimo (falha se < 70%)
```bash
mvn clean test jacoco:report jacoco:check -Djacoco.check.rules=src/test/resources/jacoco-rules.xml
```

---

## 🔍 Interpretar a Saída

### Exemplo Completo

```bash
$ JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn clean test jacoco:report
```

**Saída Esperada:**
```
[INFO] --- jacoco-maven-plugin:0.8.10:prepare-agent (default) @ task-manager-backend ---
[INFO] surefireArgLine set to: -javaagent:/home/user/.m2/repository/...jacoco-agent.jar=destfile=target/jacoco.exec

[INFO] --- maven-surefire-plugin:3.5.5:test (default-test) @ task-manager-backend ---
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider

[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.example.task_manager_backend.TaskManagerBackendApplicationTests
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 7.874 s
[INFO] Running com.example.task_manager_backend.features.projects.usecases.AddProjectMemberHandlerTest
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.597 s
[INFO] Running com.example.task_manager_backend.features.projects.usecases.CreateProjectHandlerTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.035 s
[INFO] Running com.example.task_manager_backend.features.tasks.usecases.UpdateTaskHandlerTest
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.124 s
[INFO] Running com.example.task_manager_backend.features.tasks.usecases.CreateTaskHandlerTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.043 s

[INFO] Results:
[INFO] Tests run: 27, Failures: 0, Errors: 0, Skipped: 0

[INFO] --- jacoco-maven-plugin:0.8.10:report (report) @ task-manager-backend ---
[INFO] Loading execution data file: /home/user/.../target/jacoco.exec
[INFO] Analyzed 60 classes for coverage
[INFO] 
[INFO] BUILD SUCCESS
[INFO] Total time: 25.345 s
```

---

## 📂 Arquivos Gerados

```
target/
├── jacoco.exec                          # Dados de execução (binário)
├── site/
│   └── jacoco/
│       ├── index.html                   # 📊 RELATÓRIO PRINCIPAL
│       ├── csv/
│       │   └── jacoco.csv               # Dados em CSV
│       └── sessions.html                # Detalhes das sessões
```

---

## 🖥️ Visualizar no Navegador

### Passo 1: Gerar o relatório
```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn clean test jacoco:report
```

### Passo 2: Abrir no navegador
```bash
# Linux/Mac
xdg-open target/site/jacoco/index.html

# Windows
start target\site\jacoco\index.html

# Ou simplesmente copie o caminho:
file:///home/kaylane.biazoto@db1.com.br/Music/task-manager-backend/target/site/jacoco/index.html
```

### Passo 3: Navegar no relatório
- 🔴 **Vermelho** = Não coberto (0% de execução)
- 🟡 **Amarelo** = Parcialmente coberto (1-50%)
- 🟢 **Verde** = Bem coberto (>50%)

Click em qualquer classe para ver linha por linha qual código foi testado.

---

## 📊 Exemplo de Cobertura Atual

**Após implementar 27 testes:**

```
Estatísticas Gerais:
├─ Linhas de Código: 60 classes analisadas
├─ Handlers Testados: 4 (100% cada)
├─ Line Coverage: ~35-40% (foco nos handlers)
└─ Overall: ~30% (muitas classes não testadas ainda)
```

**Detalhes por Package:**
```
✅ features.projects.usecases      → 95%+ (handlers com testes)
✅ features.tasks.usecases         → 90%+ (handlers com testes)
⚠️  features.projects.services     → 0% (sem testes)
⚠️  features.tasks.services        → 0% (sem testes)
⚠️  features.projects.api          → 0% (sem testes)
⚠️  features.tasks.api             → 0% (sem testes)
⚠️  features.auth                  → 0% (sem testes)
```

---

## 🚀 Próximas Etapas para Aumentar Cobertura

### Fase 2 (Services/Facades)
```bash
# Adicionar testes para:
mvn test -Dtest=*ServiceTest,*FacadeTest
```

### Fase 3 (Controllers)
```bash
# Adicionar testes para:
mvn test -Dtest=*ControllerTest,*IntegrationTest
```

### Fase 4 (Verificação de Cobertura)
```bash
# Verificar que atingiu meta de 70%
mvn clean test jacoco:report
```

---

## 💡 Dicas Avançadas

### Excluir Classes da Cobertura (opcional)

Se desejar excluir Entities, Configs, etc:

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
    <configuration>
        <excludes>
            <exclude>**/domain/**</exclude>
            <exclude>**/config/**</exclude>
            <exclude>**/dto/**</exclude>
        </excludes>
    </configuration>
    <!-- ... rest of config ... -->
</plugin>
```

---

## 📖 Referências

- [JaCoCo Official Docs](https://www.eclemma.org/jacoco/trunk/doc/maven.html)
- [Maven Surefire Plugin](https://maven.apache.org/plugins/maven-surefire-plugin/)
- [Code Coverage Best Practices](https://www.eclemma.org/userdoc/faq.html)

---

## ✅ Checklist

- ✅ JaCoCo adicionado ao pom.xml
- ✅ Comando para gerar cobertura
- ✅ Comando para visualizar relatório
- ✅ Entender cores do relatório
- ✅ Metas de cobertura definidas
- ✅ Próximas fases planejadas

---

**Status**: 🚀 Pronto para usar!

**Data**: 18 de Abril, 2026

