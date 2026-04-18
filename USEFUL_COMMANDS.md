# 🎯 COMANDOS ÚTEIS - Testes e Cobertura

## ⚡ TL;DR (Very Quick)

```bash
# Opção 1: Menu interativo (MELHOR)
./run_tests.sh

# Opção 2: Testes + Cobertura (recomendado)
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn clean test jacoco:report

# Opção 3: Ver cobertura no navegador
xdg-open target/site/jacoco/index.html
```

---

## 📋 Todos os Comandos

### Testes
```bash
# Testes rápidos
mvn test

# Testes com limpeza
mvn clean test

# Um teste específico
mvn test -Dtest=CreateProjectHandlerTest

# Padrão de nomes
mvn test -Dtest=*HandlerTest

# Testes com verbose
mvn clean test -X
```

### Cobertura
```bash
# Testes + Cobertura JaCoCo
mvn clean test jacoco:report

# Apenas gerar relatório (sem rodar testes)
mvn jacoco:report

# Ver arquivo de cobertura bruto
cat target/site/jacoco/index.html

# Ver resumo em CSV
cat target/site/jacoco/csv/jacoco.csv
```

### Limpeza
```bash
# Remover tudo gerado
mvn clean

# Remover e reconstruir (sem testes)
mvn clean package -DskipTests

# Remover cache Maven
rm -rf ~/.m2/repository/
```

### Combinado
```bash
# Compile + Testes + Cobertura
mvn clean compile test jacoco:report

# Build completo com testes
mvn clean package

# Build sem testes (rápido)
mvn clean package -DskipTests
```

---

## 🎮 Script Interativo

```bash
# Executar o menu interativo
./run_tests.sh

# Opções:
#  1) Apenas testes (rápido)
#  2) Testes + cobertura (recomendado)
#  3) Abrir relatório no navegador
#  4) Limpar e reconstruir
#  5) Executar um teste específico
#  6) Sair
```

---

## 📊 Entender a Saída

### Sucesso ✅
```
Tests run: 27, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 23.527 s
BUILD SUCCESS
```

### Falha ❌
```
Tests run: 27, Failures: 1, Errors: 2, Skipped: 0
FAILURE
```

### Com Cobertura
```
[INFO] --- jacoco-maven-plugin:0.8.10:report (report) @ task-manager-backend ---
[INFO] Analyzed 60 classes for coverage
[INFO] BUILD SUCCESS
```

---

## 🔍 Debug

### Ver stack trace completo
```bash
mvn test -e
```

### Parar no primeiro erro
```bash
mvn test -DfailIfNoTests=false -f
```

### Rodar um teste específico em debug
```bash
mvn -Dmaven.surefire.debug test -Dtest=CreateProjectHandlerTest
```

---

## 🚀 Atalhos do Sistema

```bash
# Ir para o diretório
cd /home/kaylane.biazoto@db1.com.br/Music/task-manager-backend

# Criar alias (temporário)
alias test_run='JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn clean test'
alias test_cov='JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn clean test jacoco:report'

# Usar
test_run      # Apenas testes
test_cov      # Com cobertura
```

---

## 📈 Monitorar em Tempo Real

```bash
# Testes rodando (ver cada um)
mvn test | grep -E "Running|Tests run|BUILD"

# Ver tempo de execução
mvn test | tee test_output.log
tail -f test_output.log

# Contar testes
mvn test 2>&1 | grep "Tests run" | awk '{print $3}'
```

---

## 🔗 Links Úteis

### Depois de gerar cobertura:
```bash
# Abrir arquivo
xdg-open target/site/jacoco/index.html

# Ou copiar caminho:
file://$(pwd)/target/site/jacoco/index.html
```

### Documentação
- QUICK_TEST_GUIDE.md (⚡ 2 min)
- TESTING_COVERAGE_GUIDE.md (📊 5 min)
- TESTING_GUIDE.md (🧪 15 min)

---

## 💡 Pro Tips

### Executar testes sem recompilar tudo
```bash
mvn test -no-build-cache
```

### Parallelizar testes
```bash
mvn test -DparallelizeTests=true
```

### Apenas relatório (sem rodar)
```bash
mvn jacoco:report jacoco:gui
```

### Salvar saída em arquivo
```bash
mvn clean test > test_results.txt 2>&1
```

### Grep para erros
```bash
mvn test 2>&1 | grep -i "error\|failure"
```

---

## 📝 Scripting

### Verificar se todos passaram
```bash
mvn test &> /dev/null && echo "✅ TESTES OK" || echo "❌ FALHA"
```

### Rodar e contar testes
```bash
TESTS=$(mvn test 2>&1 | grep "Tests run" | awk '{print $3}')
echo "Total de testes: $TESTS"
```

### Continuar se falhar
```bash
mvn test || true
```

---

## 🎯 Workflow Típico

```bash
# 1. Começar trabalho
cd /home/kaylane.biazoto@db1.com.br/Music/task-manager-backend

# 2. Executar testes (rápido)
mvn test

# 3. Se tudo OK, gerar cobertura
mvn clean test jacoco:report

# 4. Ver relatório
xdg-open target/site/jacoco/index.html

# 5. Adicionar novos testes (se necessário)
# ... editar arquivos ...

# 6. Rodar novamente
mvn test
```

---

## ⏱️ Tempos Esperados

| Comando | Tempo |
|---------|-------|
| `mvn test` | 20-25s |
| `mvn clean test` | 23-28s |
| `mvn test jacoco:report` | 25-30s |
| `mvn clean package` | 30-40s |
| Um teste específico | 15-20s |

---

**Status**: ✅ Tudo configurado e pronto!

