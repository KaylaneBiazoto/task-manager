         # ⚡ QUICK START - Testes e Cobertura

## 🚀 Forma Mais Rápida

### Opção 1: Script Interativo (RECOMENDADO)
```bash
cd /home/kaylane.biazoto@db1.com.br/Music/task-manager-backend
./run_tests.sh
```

Depois escolha:
- **Opção 1**: Apenas testes (rápido)
- **Opção 2**: Testes + cobertura (recomendado)
- **Opção 3**: Abrir relatório no navegador

---

## 📊 Forma Direta por Comando

### Testes Rápidos (2-3 segundos)
```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn clean test
```

### Testes + Cobertura (20-30 segundos)
```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn clean test jacoco:report
```

**Depois abrir:**
```bash
xdg-open target/site/jacoco/index.html
```

---

## 📈 Ver Resumo de Cobertura

Após executar com cobertura, no terminal:
```bash
grep -A 20 "Analyzed" target/site/jacoco/index.html | head -20
```

---

## 🎯 Resultado Esperado

```
✅ Tests run: 27
✅ Failures: 0
✅ Errors: 0
✅ Skipped: 0

📊 Coverage:
   Handlers: 90-100%
   Overall: 30-35%

BUILD SUCCESS
```

---

## 💡 Atalhos

| Comando | Descrição |
|---------|-----------|
| `./run_tests.sh` | Menu interativo (MELHOR) |
| `mvn test` | Apenas testes rápido |
| `mvn test jacoco:report` | Testes + cobertura |
| `mvn test -Dtest=CreateProjectHandlerTest` | Um teste só |
| `mvn clean test -Dtest=*HandlerTest` | Todos os handlers |

---

## 🔍 Interpretar Cores do Relatório

No `target/site/jacoco/index.html`:

- 🟢 **Verde** = Código testado (>50%)
- 🟡 **Amarelo** = Parcialmente testado (1-50%)
- 🔴 **Vermelho** = Não testado (0%)

Clique em qualquer classe para ver linha por linha o que foi testado.

---

**Status**: ✅ Tudo pronto! Use `./run_tests.sh` para começar.

