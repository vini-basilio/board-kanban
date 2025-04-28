# 🛠️ Simple Task Board - Terminal Edition

Projeto de um **board de tarefas** simples, rodando direto no **terminal**, inspirado em ferramentas como Jira, mas focado no aprendizado de bancos de dados e boas práticas de desenvolvimento.

---

## 🚀 Stack Tecnológica

- **Java 17**
- **Gradle** (build e gerenciamento de dependências)
- **Liquibase** (controle de versionamento de banco de dados)
- **MySQL Connector** (driver JDBC para integração com banco)
- **Lombok** (para reduzir boilerplate no código)

### 📦 Principais Dependências

```groovy
dependencies {
    implementation("org.liquibase:liquibase-core:4.29.1")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testImplementation("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
}
```

---

## 💻 Sobre o Projeto

Este projeto foi desenvolvido como parte de um processo de estudo, utilizando como referência o repositório de aula da [Digital Innovation One (DIO)](https://github.com/digitalinnovationone/board/tree/master).  
A ideia foi replicar o conteúdo, consolidando conhecimentos sobre a construção de sistemas de gerenciamento de tarefas com banco de dados real e boas práticas de organização.

**Principais funcionalidades:**
- CRUD de tarefas via linha de comando.
- Versionamento de banco de dados usando Liquibase.
- Implementação de rollback de transações para garantir segurança dos dados.

---

## 🎯 Principais Aprendizados

- **Controle manual de SQL**: Experiência nova saindo do conforto dos ORMs, escrevendo e gerenciando queries diretamente.
- **Rollback de transações**: Aprendi como garantir a consistência dos dados em casos de erro, com aplicações reais em sistemas críticos como bancos.
- **Adaptação de projetos guiados**: Mais do que copiar, busquei entender o fluxo e imaginar como evoluir o projeto pra outros contextos.
