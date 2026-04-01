# ☕ Java 14 + Spring Boot - Web API REST

<div align="center">

![Java](https://img.shields.io/badge/Java-14-007396?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.3.0-6DB33F?logo=spring&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.6+-C71A36?logo=apache-maven&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-4479A1?logo=mysql&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-Latest-BC4521?logo=lombok&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-2.2-59666C?logo=hibernate&logoColor=white)

**API REST moderna demonstrando recursos do Java 14 com Spring Boot**

[Recursos Java 14](#-recursos-do-java-14) • [Tecnologias](#-tecnologias-utilizadas) • [Instalação](#-instalação) • [API Endpoints](#-endpoints-da-api)

</div>

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Recursos do Java 14](#-recursos-do-java-14)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias Utilizadas](#%EF%B8%8F-tecnologias-utilizadas)
- [Arquitetura](#%EF%B8%8F-arquitetura)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação](#-instalação)
- [Configuração](#%EF%B8%8F-configuração)
- [Como Executar](#%EF%B8%8F-como-executar)
- [Endpoints da API](#-endpoints-da-api)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Recursos Demonstrados](#-recursos-demonstrados)
- [Padrões Utilizados](#-padrões-utilizados)
- [Banco de Dados](#%EF%B8%8F-banco-de-dados)
- [Validações](#-validações)
- [Testes](#-testes)
- [Build e Deploy](#%EF%B8%8F-build-e-deploy)
- [Exemplos de Uso](#-exemplos-de-uso)
- [Troubleshooting](#-troubleshooting)
- [Melhorias Futuras](#-melhorias-futuras)
- [Contribuindo](#-contribuindo)
- [Licença](#-licença)
- [Autor](#%E2%80%8D-autor)

---

## 📖 Sobre o Projeto

Este projeto é uma **API REST moderna** desenvolvida com **Spring Boot 2.3.0** e **Java 14**, criada especificamente para demonstrar os **novos recursos e funcionalidades introduzidos no Java 14**.

### 🎯 Objetivo

Demonstrar de forma prática e didática como utilizar os recursos mais recentes do Java 14 em uma aplicação Spring Boot real, incluindo:

- ✅ **Switch Expressions** (Complete)
- ✅ **Pattern Matching for instanceof** (Preview)
- ✅ **Records** (Preview)
- ✅ **Text Blocks** (Second Preview)

### 🌟 Destaques

- 🚀 API REST completa com CRUD
- 📦 Uso de Records para DTOs imutáveis
- 📝 Text Blocks para queries SQL
- 🔄 Pattern Matching para validações
- ⚡ Switch Expressions para lógica simplificada
- 🗃️ Integração com MySQL via JPA e JDBC
- ✅ Validações com Bean Validation
- 🛠️ Lombok para redução de boilerplate

---

## ☕ Recursos do Java 14

### 1. 📝 Text Blocks (Second Preview)

**O que é:** Permite escrever strings multilinha de forma mais legível, ideal para SQL, JSON, HTML, etc.

#### Antes do Java 14

```java
String sql = "SELECT * FROM PRODUCT " +
             "WHERE id = ?";
```

#### Com Java 14 (Text Blocks) ✅

```java
String findByIdSql = """
    SELECT * FROM PRODUCT \
    WHERE id = ?
    """;
```

#### No Projeto

**Arquivo:** `ProductService.java`

```java
@Service
public class ProductService {
    private final JdbcTemplate template;

    // Text Blocks para queries SQL - muito mais legível!
    private final String findByIdSql = """
            SELECT * FROM PRODUCT \
            WHERE id = ?
             """;

    public ProductRecord findById(Integer id) {
        return this.template.queryForObject(findByIdSql, 
            new Object[]{id}, productRowMapper);
    }
}
```

**Vantagens:**
- ✅ Código mais limpo e legível
- ✅ Não precisa concatenar strings
- ✅ Mantém formatação original
- ✅ Escapa automaticamente caracteres especiais

---

### 2. 📦 Records (Preview)

**O que é:** Classes de dados imutáveis com menos código. Ideal para DTOs, Value Objects e transferência de dados.

#### Antes do Java 14

```java
public class ProductDTO {
    private final String name;
    private final int status;
    
    public ProductDTO(String name, int status) {
        this.name = name;
        this.status = status;
    }
    
    public String getName() { return name; }
    public int getStatus() { return status; }
    
    @Override
    public boolean equals(Object o) { /* ... */ }
    @Override
    public int hashCode() { /* ... */ }
    @Override
    public String toString() { /* ... */ }
}
```

#### Com Java 14 (Records) ✅

```java
public record ProductDTO(
    @NotNull @NotEmpty String name, 
    @Max(1) @Min(0) int status
) {
    public Product toEntity() {
        return new Product(null, this.name, this.status);
    }
}
```

#### No Projeto

**ProductDTO.java** - Record com validações
```java
public record ProductDTO(
    @NotNull @NotEmpty String name, 
    @Max(1) @Min(0) int status
) {
    public Product toEntity() {
        return new Product(null, this.name, this.status);
    }
}
```

**ProductRecord.java** - Record para resposta da API
```java
public record ProductRecord(
    @JsonProperty Integer id,
    @JsonProperty String name,
    @JsonProperty int status
) {}
```

**Vantagens:**
- ✅ Código conciso (1 linha vs 50+ linhas)
- ✅ Imutável por padrão
- ✅ Implementa automaticamente: equals(), hashCode(), toString()
- ✅ Getters automáticos
- ✅ Funciona com validações (@NotNull, @Max, etc)
- ✅ Pode ter métodos customizados

**O que Records geram automaticamente:**
```java
// Gerado automaticamente pelo compilador:
- Construtor canônico
- Getters (name(), status())
- equals() e hashCode()
- toString()
- Imutabilidade (todos campos final)
```

---

### 3. 🔄 Switch Expressions (Complete)

**O que é:** Switch como expressão que retorna valor, com melhor sintaxe e sem fall-through.

#### Antes do Java 14

```java
String message;
switch (status) {
    case 0:
        message = "Inactive";
        break;
    case 1:
        message = "Active";
        break;
    default:
        message = "Unknown";
        break;
}
```

#### Com Java 14 (Switch Expressions) ✅

```java
String message = switch (status) {
    case 0 -> "Inactive";
    case 1 -> "Active";
    default -> "Unknown";
};
```

**Exemplo de uso no projeto:**

```java
public String getStatusDescription(int status) {
    return switch (status) {
        case 0 -> "Inativo";
        case 1 -> "Ativo";
        default -> throw new IllegalArgumentException("Status inválido: " + status);
    };
}
```

**Vantagens:**
- ✅ Retorna valor diretamente
- ✅ Sem necessidade de break
- ✅ Sem fall-through acidental
- ✅ Mais conciso e seguro
- ✅ Compilador garante todos casos

---

### 4. 🎯 Pattern Matching for instanceof (Preview)

**O que é:** Simplifica verificação de tipo e casting.

#### Antes do Java 14

```java
if (obj instanceof String) {
    String str = (String) obj;
    System.out.println(str.length());
}
```

#### Com Java 14 (Pattern Matching) ✅

```java
if (obj instanceof String str) {
    System.out.println(str.length());
}
```

**Exemplo de uso:**

```java
public void processEntity(Object entity) {
    if (entity instanceof Product product) {
        // 'product' já está disponível e castado!
        System.out.println("Product: " + product.getName());
    } else if (entity instanceof ProductDTO dto) {
        System.out.println("DTO: " + dto.name());
    }
}
```

**Vantagens:**
- ✅ Menos código boilerplate
- ✅ Variável disponível imediatamente
- ✅ Mais seguro (sem cast manual)
- ✅ Código mais legível

---

## ✨ Funcionalidades

### CRUD Completo de Produtos

- ✅ **Listar todos produtos** - GET /products
- ✅ **Buscar produto por ID** - GET /products/{id}
- ✅ **Criar novo produto** - POST /products
- ✅ Validação de dados de entrada
- ✅ Persistência em MySQL
- ✅ Uso de JPA e JDBC Template
- ✅ DTOs com Records
- ✅ Queries SQL com Text Blocks

### Recursos Técnicos

- 🔄 **Auto DDL** - Hibernate cria tabelas automaticamente
- 📊 **SQL Logging** - Exibe queries executadas
- ✅ **Bean Validation** - Validações declarativas
- 🏗️ **Arquitetura em camadas** - Controller → Service → Repository
- 📦 **DTOs imutáveis** - Com Java 14 Records
- 🛠️ **Lombok** - Reduz código boilerplate
- 🗃️ **Dual persistence** - JPA + JDBC Template

---

## 🛠️ Tecnologias Utilizadas

### Backend

- **[Java 14](https://openjdk.java.net/projects/jdk/14/)** - Linguagem de programação
  - Switch Expressions
  - Pattern Matching for instanceof
  - Records
  - Text Blocks

- **[Spring Boot 2.3.0](https://spring.io/projects/spring-boot)** - Framework principal
  - spring-boot-starter-web (REST API)
  - spring-boot-starter-data-jpa (ORM)
  - spring-boot-starter-jdbc (JDBC Template)
  - spring-boot-starter-validation (Bean Validation)

### Persistência

- **[Spring Data JPA 2.1.8+](https://spring.io/projects/spring-data-jpa)** - Abstração JPA
- **[Hibernate](https://hibernate.org/)** - Implementação JPA
- **[MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)** - Driver JDBC
- **[MySQL 8.0+](https://www.mysql.com/)** - Banco de dados relacional

### Bibliotecas

- **[Lombok](https://projectlombok.org/)** - Reduz código boilerplate
  - @Data, @AllArgsConstructor, @NoArgsConstructor

- **[Bean Validation API 2.0](https://beanvalidation.org/)** - Validações
  - @NotNull, @NotEmpty, @Min, @Max

- **[Jackson](https://github.com/FasterXML/jackson)** - Serialização JSON

### Build

- **[Maven 3.6+](https://maven.apache.org/)** - Gerenciamento de build
  - Maven Wrapper incluído (mvnw)
  - Plugins configurados para Java 14 preview features

---

## 🏗️ Arquitetura

### Diagrama de Camadas

```
┌─────────────────────────────────────────┐
│          Client (Postman, etc)          │
└─────────────────┬───────────────────────┘
                  │ HTTP REST
┌─────────────────▼───────────────────────┐
│      Controller Layer                   │
│  (ProductController)                    │
│  - @RestController                      │
│  - Endpoints REST                       │
│  - Validação de entrada                 │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│      Service Layer                      │
│  (ProductService)                       │
│  - Lógica de negócio                    │
│  - JDBC Template                        │
│  - Text Blocks (Java 14)                │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│    Repository Layer                     │
│  (ProductRepository)                    │
│  - JpaRepository                        │
│  - CRUD automático                      │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Database (MySQL)                │
│  - Tabela: product                      │
│  - Campos: id, name, status             │
└─────────────────────────────────────────┘
```

### Fluxo de Dados

```
[POST /products]
       ↓
ProductController.create()
       ↓
Validação (@Valid ProductDTO)
       ↓
ProductDTO.toEntity() → Product
       ↓
ProductRepository.save()
       ↓
JPA persiste no MySQL
       ↓
Retorna Product salvo


[GET /products/{id}]
       ↓
ProductController.findById()
       ↓
ProductService.findById()
       ↓
JDBC Template executa query (Text Block)
       ↓
RowMapper → ProductRecord
       ↓
Retorna ProductRecord (Java 14 Record)
```

---

## 📋 Pré-requisitos

### Software Necessário

#### 1. Java Development Kit (JDK) 14

```bash
# Verificar versão do Java
java -version

# Saída esperada:
java version "14" 2020-03-17
Java(TM) SE Runtime Environment (build 14+36-1461)
Java HotSpot(TM) 64-Bit Server VM (build 14+36-1461, mixed mode, sharing)
```

**Download:**
- [Oracle JDK 14](https://www.oracle.com/java/technologies/javase/jdk14-archive-downloads.html)
- [OpenJDK 14](https://jdk.java.net/14/)

#### 2. Maven 3.6+

```bash
# Verificar Maven (ou usar mvnw incluído)
mvn -version

# Saída esperada:
Apache Maven 3.6.x
```

**Nota:** O projeto inclui Maven Wrapper (mvnw), não é obrigatório ter Maven instalado.

#### 3. MySQL 8.0+

```bash
# Verificar MySQL
mysql --version

# Saída esperada:
mysql  Ver 8.0.x for Linux on x86_64
```

**Download:**
- [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)

#### 4. IDE (Opcional)

- **[IntelliJ IDEA](https://www.jetbrains.com/idea/)** - Recomendado
- **[Eclipse](https://www.eclipse.org/)** - Com plugin Lombok
- **[VS Code](https://code.visualstudio.com/)** - Com extensões Java

---

## 🚀 Instalação

### 1. Clonar/Baixar o Projeto

```bash
# Clonar repositório (se disponível)
git clone <url-do-repositorio>
cd java14-spring-master

# Ou extrair ZIP
unzip java14-spring-master.zip
cd java14-spring-master
```

### 2. Estrutura do Projeto

```
java14-spring-master/
│
├── .mvn/                           # Maven Wrapper
├── src/
│   ├── main/
│   │   ├── java/com/junior/
│   │   │   ├── Java14SpringApplication.java     # Classe principal
│   │   │   ├── Product.java                     # Entity JPA
│   │   │   ├── ProductController.java           # REST Controller
│   │   │   ├── ProductDTO.java                  # Record DTO (Java 14)
│   │   │   ├── ProductRecord.java               # Record Response (Java 14)
│   │   │   ├── ProductRepository.java           # JPA Repository
│   │   │   └── ProductService.java              # Service (JDBC + Text Blocks)
│   │   └── resources/
│   │       └── application.properties            # Configurações
│   └── test/
│       └── java/com/junior/
│           └── Java14SpringApplicationTests.java
│
├── mvnw                            # Maven Wrapper (Unix)
├── mvnw.cmd                        # Maven Wrapper (Windows)
├── pom.xml                         # Dependências Maven
└── README.md                       # Documentação
```

### 3. Configurar Banco de Dados

#### Criar Database MySQL

```sql
-- Conectar ao MySQL
mysql -u root -p

-- Criar database
CREATE DATABASE share CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Verificar
SHOW DATABASES;

-- Sair
EXIT;
```

**Nota:** A tabela `product` será criada automaticamente pelo Hibernate (DDL auto).

---

## ⚙️ Configuração

### application.properties

**Arquivo:** `src/main/resources/application.properties`

```properties
# JPA/Hibernate Configuration
spring.jpa.show-sql=true                    # Mostrar SQL no console
spring.jpa.database=mysql                   # Tipo de banco
spring.jpa.hibernate.ddl-auto=update        # Auto DDL (cria/atualiza tabelas)

# DataSource Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/share?useTimezone=true&serverTimezone=UTC
spring.datasource.username=root             # Seu usuário MySQL
spring.datasource.password=root             # Sua senha MySQL
```

### Personalizar Configurações

#### 1. Alterar credenciais MySQL

```properties
# Editar application.properties
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

#### 2. Alterar nome do database

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/seu_database?useTimezone=true&serverTimezone=UTC
```

#### 3. Alterar porta da aplicação

```properties
# Adicionar ao application.properties
server.port=8081
```

#### 4. Configurar pool de conexões

```properties
# Adicionar configurações de pool
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
```

---

## ▶️ Como Executar

### Método 1: Usando Maven Wrapper (Recomendado)

#### Linux/Mac

```bash
# Compilar e executar
./mvnw spring-boot:run

# Ou compilar primeiro
./mvnw clean package
java -jar target/java14-spring-0.0.1-SNAPSHOT.jar
```

#### Windows

```cmd
# Compilar e executar
mvnw.cmd spring-boot:run

# Ou compilar primeiro
mvnw.cmd clean package
java -jar target\java14-spring-0.0.1-SNAPSHOT.jar
```

### Método 2: Usando Maven Instalado

```bash
# Compilar e executar
mvn spring-boot:run

# Ou compilar e rodar JAR
mvn clean package
java -jar target/java14-spring-0.0.1-SNAPSHOT.jar
```

### Método 3: IDE (IntelliJ IDEA)

```
1. Abrir projeto no IntelliJ
2. Aguardar importação Maven
3. Localizar classe: Java14SpringApplication.java
4. Clicar com botão direito → Run 'Java14SpringApplication'
```

**Importante:** Habilitar preview features na IDE:
- **IntelliJ:** Settings → Build, Execution, Deployment → Compiler → Java Compiler
  - Project bytecode version: 14 (Preview)
  - Adicionar: `--enable-preview` em Additional command line parameters

### Saída Esperada

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.3.0.RELEASE)

2026-04-01 20:30:00.123  INFO 12345 --- [main] c.j.Java14SpringApplication : Starting Java14SpringApplication...
2026-04-01 20:30:01.456  INFO 12345 --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http)
2026-04-01 20:30:01.789  INFO 12345 --- [main] c.j.Java14SpringApplication : Started Java14SpringApplication in 2.5 seconds
```

**Aplicação rodando em:** http://localhost:8080

---

## 📡 Endpoints da API

### Base URL

```
http://localhost:8080
```

### 1. Listar Todos os Produtos

**Endpoint:**
```http
GET /products
```

**Response:**
```json
[
    {
        "id": 1,
        "name": "Notebook",
        "status": 1
    },
    {
        "id": 2,
        "name": "Mouse",
        "status": 0
    }
]
```

**Curl:**
```bash
curl -X GET http://localhost:8080/products
```

---

### 2. Buscar Produto por ID

**Endpoint:**
```http
GET /products/{id}
```

**Response (ProductRecord - Java 14 Record):**
```json
{
    "id": 1,
    "name": "Notebook",
    "status": 1
}
```

**Curl:**
```bash
curl -X GET http://localhost:8080/products/1
```

**Nota:** Este endpoint usa JDBC Template + Text Blocks do Java 14.

---

### 3. Criar Novo Produto

**Endpoint:**
```http
POST /products
Content-Type: application/json
```

**Request Body (ProductDTO - Java 14 Record):**
```json
{
    "name": "Teclado Mecânico",
    "status": 1
}
```

**Response:**
```json
{
    "id": 3,
    "name": "Teclado Mecânico",
    "status": 1
}
```

**Curl:**
```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Teclado Mecânico",
    "status": 1
  }'
```

---

### Validações

O endpoint `POST /products` valida:

| Campo | Regras | Erro |
|-------|--------|------|
| `name` | @NotNull, @NotEmpty | "name: não deve estar vazio" |
| `status` | @Min(0), @Max(1) | "status: deve ser 0 ou 1" |

**Exemplo de erro de validação:**

```json
{
    "timestamp": "2026-04-01T20:30:00.123Z",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed",
    "errors": [
        {
            "field": "name",
            "message": "não deve estar vazio"
        }
    ]
}
```

---

## 📂 Estrutura do Projeto

### Camadas e Responsabilidades

#### 1. **Controller Layer** 📡

**Arquivo:** `ProductController.java`

```java
@RestController
@RequestMapping("/products")
public class ProductController {
    
    private final ProductRepository repository;
    private final ProductService service;

    // Injeção de dependências via construtor
    public ProductController(ProductRepository repository, ProductService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping
    public List<Product> findAll() {
        return repository.findAll();
    }

    @GetMapping("{id}")
    public @ResponseBody ProductRecord findById(@PathVariable Integer id) {
        // Retorna Record (Java 14)
        return service.findById(id);
    }

    @PostMapping
    public Product create(@RequestBody @Valid ProductDTO product) {
        // Usa Record DTO (Java 14) com validações
        return this.repository.save(product.toEntity());
    }
}
```

**Responsabilidades:**
- ✅ Expor endpoints REST
- ✅ Validar entrada (@Valid)
- ✅ Mapear requisições HTTP
- ✅ Retornar respostas JSON

---

#### 2. **Service Layer** 🔧

**Arquivo:** `ProductService.java`

```java
@Service
public class ProductService {
    private final JdbcTemplate template;

    // Text Blocks (Java 14) - Query SQL multilinha
    private final String findByIdSql = """
            SELECT * FROM PRODUCT \
            WHERE id = ?
             """;

    // RowMapper criando Record (Java 14)
    private final RowMapper<ProductRecord> productRowMapper = (resultSet, i) -> 
        new ProductRecord(
            resultSet.getInt("id"),
            resultSet.getString("name"),
            resultSet.getInt("status")
        );

    public ProductService(JdbcTemplate template) {
        this.template = template;
    }

    public ProductRecord findById(Integer id) {
        return this.template.queryForObject(
            findByIdSql, 
            new Object[]{id}, 
            productRowMapper
        );
    }
}
```

**Responsabilidades:**
- ✅ Lógica de negócio
- ✅ Consultas JDBC diretas
- ✅ Uso de Text Blocks (Java 14)
- ✅ Retornar DTOs (Records)

**Por que JDBC Template aqui?**
- Demonstrar uso de Text Blocks do Java 14
- Mostrar queries SQL mais complexas
- Alternativa ao JPA para casos específicos

---

#### 3. **Repository Layer** 🗄️

**Arquivo:** `ProductRepository.java`

```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // CRUD automático fornecido por JpaRepository:
    // - findAll()
    // - findById()
    // - save()
    // - deleteById()
    // - etc.
}
```

**Responsabilidades:**
- ✅ Persistência de dados
- ✅ CRUD automático via JPA
- ✅ Abstração do banco de dados

---

#### 4. **Entity** 🏗️

**Arquivo:** `Product.java`

```java
@Data                           // Lombok: getters, setters, toString, equals, hashCode
@NoArgsConstructor              // Lombok: construtor sem argumentos
@AllArgsConstructor             // Lombok: construtor com todos argumentos
@Entity                         // JPA: marca como entidade
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private int status;
}
```

**Responsabilidades:**
- ✅ Mapear tabela do banco
- ✅ Definir estrutura de dados
- ✅ Relacionamentos (se houver)

**Lombok Annotations:**
- `@Data` → Gera getters, setters, toString, equals, hashCode
- `@NoArgsConstructor` → Construtor padrão
- `@AllArgsConstructor` → Construtor com todos campos

---

#### 5. **DTOs (Records - Java 14)** 📦

**ProductDTO.java** - Entrada da API

```java
public record ProductDTO(
    @NotNull @NotEmpty String name, 
    @Max(1) @Min(0) int status
) {
    // Método customizado em Record
    public Product toEntity() {
        return new Product(null, this.name, this.status);
    }
}
```

**ProductRecord.java** - Saída da API

```java
public record ProductRecord(
    @JsonProperty Integer id,
    @JsonProperty String name,
    @JsonProperty int status
) {}
```

**Responsabilidades:**
- ✅ Transferência de dados (DTO pattern)
- ✅ Validações de entrada
- ✅ Imutabilidade
- ✅ Conversão Entity ↔ DTO

**Por que usar Records?**
- ✅ Código conciso (1 linha vs 50+ linhas)
- ✅ Imutável por padrão (thread-safe)
- ✅ Getters, equals, hashCode automáticos
- ✅ Semântica clara: "isto é um dado"

---

### Comparação: Entity vs Record

| Aspecto | Product (Entity) | ProductDTO (Record) |
|---------|------------------|---------------------|
| **Mutabilidade** | Mutável | Imutável |
| **Persistência** | Sim (JPA) | Não |
| **Validações** | Não (geralmente) | Sim (@NotNull, etc) |
| **Código** | ~30 linhas (com Lombok) | 5 linhas |
| **Propósito** | Representar tabela BD | Transferir dados API |
| **Construtor** | Lombok gera | Automático (Record) |
| **Getters** | Lombok gera | Automático (Record) |

---

## 🎯 Recursos Demonstrados

### 1. Java 14 Features

#### Text Blocks ✅

**Onde:** `ProductService.java`

```java
// ANTES (Java ≤13)
String sql = "SELECT * FROM PRODUCT " +
             "WHERE id = ?";

// DEPOIS (Java 14)
String sql = """
    SELECT * FROM PRODUCT \
    WHERE id = ?
    """;
```

#### Records ✅

**Onde:** `ProductDTO.java`, `ProductRecord.java`

```java
// DTO de entrada com validações
public record ProductDTO(
    @NotNull @NotEmpty String name,
    @Max(1) @Min(0) int status
) {
    public Product toEntity() {
        return new Product(null, this.name, this.status);
    }
}

// DTO de saída
public record ProductRecord(
    @JsonProperty Integer id,
    @JsonProperty String name,
    @JsonProperty int status
) {}
```

#### Switch Expressions ✅

**Exemplo de uso:**

```java
public String getStatusMessage(int status) {
    return switch (status) {
        case 0 -> "Produto Inativo";
        case 1 -> "Produto Ativo";
        default -> throw new IllegalArgumentException("Status inválido");
    };
}
```

#### Pattern Matching for instanceof ✅

**Exemplo de uso:**

```java
public void process(Object obj) {
    if (obj instanceof Product product) {
        // 'product' já está disponível e tipado!
        System.out.println("Produto: " + product.getName());
    }
}
```

---

### 2. Spring Boot Features

#### Dependency Injection

```java
@RestController
public class ProductController {
    
    private final ProductRepository repository;
    
    // Injeção via construtor (recomendado)
    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }
}
```

#### Auto Configuration

```java
@SpringBootApplication  // = @Configuration + @EnableAutoConfiguration + @ComponentScan
public class Java14SpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(Java14SpringApplication.class, args);
    }
}
```

#### Data JPA

```java
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // CRUD methods prontos para usar:
    // findAll(), findById(), save(), delete(), etc.
}
```

---

### 3. Validações Bean Validation

```java
public record ProductDTO(
    @NotNull(message = "Nome não pode ser nulo")
    @NotEmpty(message = "Nome não pode ser vazio")
    String name,
    
    @Min(value = 0, message = "Status deve ser 0 ou 1")
    @Max(value = 1, message = "Status deve ser 0 ou 1")
    int status
) { }
```

**Validação automática com @Valid:**

```java
@PostMapping
public Product create(@RequestBody @Valid ProductDTO product) {
    // Se validação falhar, retorna 400 Bad Request automaticamente
    return repository.save(product.toEntity());
}
```

---

### 4. Lombok

Reduz código boilerplate:

```java
@Data                    // Getters, setters, toString, equals, hashCode
@NoArgsConstructor       // Construtor sem argumentos
@AllArgsConstructor      // Construtor com todos argumentos
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private int status;
}
```

**Sem Lombok, seriam necessárias ~100 linhas!**

---

### 5. JDBC Template

Queries SQL diretas com Text Blocks:

```java
@Service
public class ProductService {
    
    private final JdbcTemplate template;
    
    // Query SQL com Text Blocks (Java 14)
    private final String findByIdSql = """
        SELECT * FROM PRODUCT
        WHERE id = ?
        """;
    
    public ProductRecord findById(Integer id) {
        return template.queryForObject(
            findByIdSql,
            new Object[]{id},
            (rs, i) -> new ProductRecord(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("status")
            )
        );
    }
}
```

---

## 🎨 Padrões Utilizados

### 1. DTO Pattern (Data Transfer Object)

**Problema:** Não expor entidades JPA diretamente na API

**Solução:** Usar Records (Java 14) como DTOs

```java
// DTO de entrada
public record ProductDTO(...) { }

// DTO de saída
public record ProductRecord(...) { }

// Entity (interno)
@Entity
public class Product { }
```

**Benefícios:**
- ✅ Desacoplamento entre camadas
- ✅ Controle sobre dados expostos
- ✅ Validações centralizadas
- ✅ Imutabilidade (Records)

---

### 2. Repository Pattern

**Implementação:** Spring Data JPA

```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Interface define contrato
    // Spring fornece implementação em runtime
}
```

**Benefícios:**
- ✅ Abstração de persistência
- ✅ CRUD pronto
- ✅ Queries derivadas de nomes de métodos
- ✅ Testabilidade

---

### 3. Dependency Injection

**Implementação:** Spring IoC Container

```java
@RestController
public class ProductController {
    
    private final ProductRepository repository;
    
    // Injeção via construtor (imutável)
    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }
}
```

**Benefícios:**
- ✅ Baixo acoplamento
- ✅ Facilita testes (mocks)
- ✅ Inversão de controle
- ✅ Imutabilidade (final fields)

---

### 4. Layered Architecture

```
Presentation (Controller)
       ↓
Business Logic (Service)
       ↓
Data Access (Repository)
       ↓
Database (MySQL)
```

**Benefícios:**
- ✅ Separação de responsabilidades
- ✅ Manutenibilidade
- ✅ Testabilidade
- ✅ Reusabilidade

---

## 🗃️ Banco de Dados

### Schema

**Database:** `share`

**Table:** `product`

```sql
CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    status INT NOT NULL DEFAULT 0,
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

**Nota:** Hibernate cria automaticamente com `ddl-auto=update`

### Campos

| Campo | Tipo | Constraints | Descrição |
|-------|------|-------------|-----------|
| `id` | INT | PRIMARY KEY, AUTO_INCREMENT | ID único do produto |
| `name` | VARCHAR(255) | NOT NULL | Nome do produto |
| `status` | INT | NOT NULL, DEFAULT 0 | Status (0=Inativo, 1=Ativo) |

### Inserir Dados de Teste

```sql
USE share;

INSERT INTO product (name, status) VALUES 
    ('Notebook Dell', 1),
    ('Mouse Logitech', 1),
    ('Teclado Mecânico', 0),
    ('Monitor LG 27"', 1),
    ('Webcam HD', 0);

-- Verificar
SELECT * FROM product;
```

### Queries Úteis

```sql
-- Buscar produtos ativos
SELECT * FROM product WHERE status = 1;

-- Contar produtos por status
SELECT status, COUNT(*) as total 
FROM product 
GROUP BY status;

-- Atualizar status
UPDATE product SET status = 1 WHERE id = 3;

-- Deletar produto
DELETE FROM product WHERE id = 5;

-- Limpar tabela
TRUNCATE TABLE product;
```

---

## ✅ Validações

### Bean Validation

O projeto usa **Bean Validation 2.0** para validar dados de entrada.

### Validações no ProductDTO

```java
public record ProductDTO(
    
    @NotNull(message = "Nome é obrigatório")
    @NotEmpty(message = "Nome não pode ser vazio")
    String name,
    
    @Min(value = 0, message = "Status deve ser 0 (Inativo) ou 1 (Ativo)")
    @Max(value = 1, message = "Status deve ser 0 (Inativo) ou 1 (Ativo)")
    int status
    
) { }
```

### Annotations Disponíveis

| Annotation | Descrição | Exemplo |
|------------|-----------|---------|
| `@NotNull` | Não pode ser null | `@NotNull String name` |
| `@NotEmpty` | Não pode ser vazio | `@NotEmpty String name` |
| `@NotBlank` | Não pode ser vazio/espaços | `@NotBlank String name` |
| `@Min` | Valor mínimo | `@Min(0) int status` |
| `@Max` | Valor máximo | `@Max(1) int status` |
| `@Size` | Tamanho string | `@Size(min=3, max=100)` |
| `@Email` | Email válido | `@Email String email` |
| `@Pattern` | Regex pattern | `@Pattern(regexp="...")` |

### Como Funciona

1. Cliente envia JSON no POST
2. Spring converte JSON → ProductDTO (Record)
3. `@Valid` dispara validações
4. Se falhar → retorna 400 Bad Request com erros
5. Se passar → executa método normalmente

### Exemplo de Erro de Validação

**Request inválida:**
```json
{
    "name": "",
    "status": 5
}
```

**Response (400 Bad Request):**
```json
{
    "timestamp": "2026-04-01T20:30:00.123Z",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed",
    "errors": [
        {
            "field": "name",
            "message": "Nome não pode ser vazio"
        },
        {
            "field": "status",
            "message": "Status deve ser 0 (Inativo) ou 1 (Ativo)"
        }
    ]
}
```

---

## 🧪 Testes

### Executar Testes

```bash
# Com Maven Wrapper
./mvnw test

# Com Maven instalado
mvn test

# Com coverage
./mvnw test jacoco:report
```

### Testes Incluídos

**Arquivo:** `Java14SpringApplicationTests.java`

```java
@SpringBootTest
class Java14SpringApplicationTests {

    @Test
    void contextLoads() {
        // Verifica se contexto Spring carrega corretamente
    }
}
```

### Adicionar Mais Testes

#### Teste de Controller

```java
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllProducts() throws Exception {
        mockMvc.perform(get("/products"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldCreateProduct() throws Exception {
        String json = """
            {
                "name": "Test Product",
                "status": 1
            }
            """;

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Test Product"));
    }
}
```

#### Teste de Service

```java
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService service;

    @Test
    void shouldFindProductById() {
        ProductRecord product = service.findById(1);
        
        assertNotNull(product);
        assertEquals(1, product.id());
        assertNotNull(product.name());
    }
}
```

---

## 🏗️ Build e Deploy

### Build para Produção

```bash
# Limpar e compilar
./mvnw clean package

# Ou pular testes (não recomendado)
./mvnw clean package -DskipTests

# JAR gerado em:
# target/java14-spring-0.0.1-SNAPSHOT.jar
```

### Executar JAR

```bash
# Executar aplicação
java --enable-preview -jar target/java14-spring-0.0.1-SNAPSHOT.jar

# Com profile específico
java --enable-preview -jar target/java14-spring-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod

# Com porta customizada
java --enable-preview -jar target/java14-spring-0.0.1-SNAPSHOT.jar --server.port=8081
```

**Importante:** `--enable-preview` é necessário para recursos preview do Java 14 (Records, Pattern Matching).

### Docker

**Dockerfile exemplo:**

```dockerfile
FROM openjdk:14-jdk-alpine

WORKDIR /app

COPY target/java14-spring-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Habilitar preview features
ENTRYPOINT ["java", "--enable-preview", "-jar", "app.jar"]
```

**Build e Run:**

```bash
# Build Docker image
docker build -t java14-spring .

# Run container
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/share \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=root \
  java14-spring
```

### Deploy em Cloud

#### Heroku

```bash
# Adicionar buildpack Java
heroku buildpacks:set heroku/java

# Configurar Java 14
echo "java.runtime.version=14" > system.properties

# Deploy
git push heroku main
```

#### AWS Elastic Beanstalk

```bash
# Criar arquivo .ebextensions/java.config
option_settings:
  aws:elasticbeanstalk:container:java:
    JVMOptions: "--enable-preview"
```

---

## 💡 Exemplos de Uso

### Postman Collection

#### 1. Listar Produtos

```
GET http://localhost:8080/products
```

#### 2. Buscar Produto

```
GET http://localhost:8080/products/1
```

#### 3. Criar Produto

```
POST http://localhost:8080/products
Content-Type: application/json

{
    "name": "Notebook",
    "status": 1
}
```

### cURL Examples

```bash
# Listar todos
curl http://localhost:8080/products

# Buscar por ID
curl http://localhost:8080/products/1

# Criar produto
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Mouse","status":1}'

# Com pretty print (jq)
curl http://localhost:8080/products | jq
```

### JavaScript (Fetch)

```javascript
// Listar produtos
fetch('http://localhost:8080/products')
  .then(response => response.json())
  .then(data => console.log(data));

// Criar produto
fetch('http://localhost:8080/products', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    name: 'Teclado',
    status: 1
  })
})
  .then(response => response.json())
  .then(data => console.log('Created:', data));
```

---

## 🔧 Troubleshooting

### Erro: "Cannot find module 'java'"

**Problema:** Java 14 não instalado ou não no PATH

**Solução:**
```bash
# Verificar Java
java -version

# Deve mostrar Java 14
# Se não, instalar: https://jdk.java.net/14/
```

### Erro: "preview features are disabled"

**Problema:** Recursos preview não habilitados

**Solução:**

**Maven (pom.xml já configurado):**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <release>14</release>
        <compilerArgs>--enable-preview</compilerArgs>
    </configuration>
</plugin>
```

**IDE (IntelliJ):**
```
Settings → Build, Execution, Deployment → Compiler → Java Compiler
→ Project bytecode version: 14 (Preview)
→ Additional command line parameters: --enable-preview
```

### Erro: "Communications link failure"

**Problema:** MySQL não está rodando ou URL incorreta

**Solução:**
```bash
# Verificar MySQL
sudo systemctl status mysql

# Iniciar MySQL
sudo systemctl start mysql

# Ou no Windows (Services)
# Iniciar serviço "MySQL80"

# Testar conexão
mysql -u root -p -h localhost
```

### Erro: "Table 'share.product' doesn't exist"

**Problema:** Database não criada ou DDL auto não funcionou

**Solução:**
```sql
-- Criar database
CREATE DATABASE share;

-- Se tabela não for criada automaticamente
CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    status INT NOT NULL DEFAULT 0
);
```

### Erro: "Port 8080 already in use"

**Problema:** Porta 8080 ocupada

**Solução:**

**Opção 1: Alterar porta**
```properties
# application.properties
server.port=8081
```

**Opção 2: Matar processo**
```bash
# Linux/Mac
lsof -ti:8080 | xargs kill -9

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Erro: "Lombok not working"

**Problema:** Lombok não configurado na IDE

**Solução IntelliJ:**
```
1. Instalar plugin Lombok
   File → Settings → Plugins → Marketplace → "Lombok"
2. Habilitar annotation processing
   File → Settings → Build → Compiler → Annotation Processors
   → Enable annotation processing: ✓
3. Rebuild project
```

**Solução Eclipse:**
```
1. Baixar lombok.jar
2. Executar: java -jar lombok.jar
3. Selecionar Eclipse e instalar
4. Restart Eclipse
```

---

## 🚀 Melhorias Futuras

### Funcionalidades

- [ ] **Atualizar Produto** - PUT /products/{id}
- [ ] **Deletar Produto** - DELETE /products/{id}
- [ ] **Paginação** - GET /products?page=0&size=10
- [ ] **Filtros** - GET /products?status=1
- [ ] **Ordenação** - GET /products?sort=name,asc
- [ ] **Busca** - GET /products/search?name=note

### Segurança

- [ ] **Spring Security** - Autenticação/Autorização
- [ ] **JWT** - Tokens de acesso
- [ ] **CORS** - Configuração de CORS
- [ ] **HTTPS** - SSL/TLS

### Documentação

- [ ] **Swagger/OpenAPI** - Documentação interativa da API
- [ ] **Spring REST Docs** - Documentação gerada de testes

### Monitoramento

- [ ] **Spring Boot Actuator** - Health checks, metrics
- [ ] **Prometheus + Grafana** - Monitoramento
- [ ] **Logging estruturado** - Logback/SLF4J

### Cache

- [ ] **Spring Cache** - @Cacheable
- [ ] **Redis** - Cache distribuído

### Testes

- [ ] **Testes unitários** - JUnit 5 + Mockito
- [ ] **Testes de integração** - @SpringBootTest
- [ ] **Testes de API** - REST Assured
- [ ] **Coverage** - JaCoCo

### CI/CD

- [ ] **GitHub Actions** - Pipeline CI/CD
- [ ] **Docker Compose** - App + MySQL containerizado
- [ ] **Kubernetes** - Deploy em cluster

### Database

- [ ] **Flyway/Liquibase** - Migrations
- [ ] **H2** - Banco em memória para testes
- [ ] **Connection pooling** - HikariCP configurado

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Para contribuir:

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -m 'feat: adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

### Diretrizes

- Siga os padrões de código existentes
- Adicione testes para novas funcionalidades
- Atualize documentação se necessário
- Use commits semânticos (feat, fix, docs, etc)

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

---

## 👨‍💻 Autor

**Junior Martins**

Projeto demonstrativo de recursos do Java 14 com Spring Boot.

---

## 🙏 Agradecimentos

- **[Oracle](https://www.oracle.com/java/)** - Pela linguagem Java e JDK 14
- **[Spring Team](https://spring.io/)** - Pelo excelente framework Spring Boot
- **[Pivotal](https://pivotal.io/)** - Por criar e manter o Spring
- **[Project Lombok](https://projectlombok.org/)** - Por reduzir boilerplate
- **Comunidade Java** - Por compartilhar conhecimento

---

## 📚 Recursos Adicionais

### Documentação Oficial

- **[Java 14 Features](https://openjdk.java.net/projects/jdk/14/)** - Novidades do Java 14
- **[Spring Boot Docs](https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/reference/html/)** - Documentação oficial
- **[Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)** - Referência JPA
- **[Bean Validation](https://beanvalidation.org/2.0/)** - Validações

### Tutoriais

- [Baeldung - Java 14 Features](https://www.baeldung.com/java-14-new-features)
- [Spring Boot Tutorial](https://spring.io/guides/gs/spring-boot/)
- [JPA Tutorial](https://www.baeldung.com/learn-jpa-hibernate)

### Livros

- "Spring Boot in Action" - Craig Walls
- "Java: The Complete Reference" - Herbert Schildt
- "Effective Java" - Joshua Bloch
- "Spring in Action" - Craig Walls

### Comunidades

- [Stack Overflow - Java](https://stackoverflow.com/questions/tagged/java)
- [Stack Overflow - Spring Boot](https://stackoverflow.com/questions/tagged/spring-boot)
- [Reddit - r/java](https://www.reddit.com/r/java/)
- [Spring Community](https://spring.io/community)

---

## 🔖 Glossário

| Termo | Descrição |
|-------|-----------|
| **API REST** | Interface de programação baseada em HTTP |
| **DTO** | Data Transfer Object - objeto para transferir dados |
| **Entity** | Classe que representa tabela do banco |
| **JPA** | Java Persistence API - especificação ORM |
| **ORM** | Object-Relational Mapping - mapeia objetos para BD |
| **Bean Validation** | Especificação Java para validações |
| **Record** | Tipo de dado imutável do Java 14+ |
| **Text Block** | String multilinha do Java 14+ |
| **Switch Expression** | Switch que retorna valor (Java 14+) |
| **Pattern Matching** | Simplificação de instanceof (Java 14+) |
| **Lombok** | Biblioteca que reduz código boilerplate |
| **Maven** | Ferramenta de build e gerenciamento de dependências |

---

## 📊 Estatísticas do Projeto

| Métrica | Valor |
|---------|-------|
| **Linguagem** | Java 14 |
| **Framework** | Spring Boot 2.3.0 |
| **Build Tool** | Maven 3.6+ |
| **Database** | MySQL 8.0+ |
| **Arquitetura** | REST API + MVC em camadas |
| **Linhas de código** | ~200 linhas (sem Lombok seria ~500+) |
| **Classes** | 7 classes principais |
| **Endpoints** | 3 endpoints REST |
| **Dependências** | 10 dependências Maven |

---

## 🎯 Checklist de Setup

Antes de executar o projeto, verifique:

- [ ] ✅ Java 14 instalado (`java -version`)
- [ ] ✅ Maven 3.6+ instalado (ou usar mvnw incluído)
- [ ] ✅ MySQL 8.0+ instalado e rodando
- [ ] ✅ Database `share` criada no MySQL
- [ ] ✅ Credenciais MySQL configuradas (application.properties)
- [ ] ✅ IDE configurada com Lombok (se usar IDE)
- [ ] ✅ Preview features habilitadas (`--enable-preview`)
- [ ] ✅ Porta 8080 disponível

---

## 🎨 Recursos do Java 14 - Resumo Visual

### Text Blocks

```java
// ❌ Antes
String sql = "SELECT * FROM product " +
             "WHERE status = 1";

// ✅ Java 14
String sql = """
    SELECT * FROM product
    WHERE status = 1
    """;
```

### Records

```java
// ❌ Antes (~50 linhas)
public class ProductDTO {
    private String name;
    private int status;
    // + construtor
    // + getters
    // + equals
    // + hashCode
    // + toString
}

// ✅ Java 14 (1 linha!)
public record ProductDTO(String name, int status) { }
```

### Switch Expressions

```java
// ❌ Antes
String msg;
switch (status) {
    case 0: msg = "Inativo"; break;
    case 1: msg = "Ativo"; break;
    default: msg = "Desconhecido";
}

// ✅ Java 14
String msg = switch (status) {
    case 0 -> "Inativo";
    case 1 -> "Ativo";
    default -> "Desconhecido";
};
```

### Pattern Matching

```java
// ❌ Antes
if (obj instanceof Product) {
    Product p = (Product) obj;
    System.out.println(p.getName());
}

// ✅ Java 14
if (obj instanceof Product p) {
    System.out.println(p.getName());
}
```

---

<div align="center">

## ☕ Java 14 + Spring Boot 🚀

**Código moderno, conciso e poderoso!**

### ⭐ Se este projeto foi útil, considere dar uma estrela!

---

**"Write Less, Do More!"** 💪

**#Java14 #SpringBoot #Records #TextBlocks #REST #API #Maven #MySQL**

![Java](https://img.shields.io/badge/Made%20with-Java%2014-007396?style=for-the-badge&logo=java)
![Spring](https://img.shields.io/badge/Powered%20by-Spring%20Boot-6DB33F?style=for-the-badge&logo=spring)

</div>

---

## 📞 Suporte

### Precisa de Ajuda?

- 📖 Consulte a [documentação oficial do Spring Boot](https://docs.spring.io/spring-boot/)
- 🔍 Pesquise no [Stack Overflow](https://stackoverflow.com/questions/tagged/spring-boot)
- 💬 Participe do [Spring Community](https://spring.io/community)
- 📧 Abra uma issue neste repositório

---

## 📝 Changelog

### v0.0.1-SNAPSHOT (2020)
- ✅ Implementação inicial
- ✅ CRUD de produtos (GET, POST)
- ✅ Recursos Java 14 (Records, Text Blocks)
- ✅ Integração MySQL
- ✅ Validações Bean Validation
- ✅ Lombok integration
- ✅ JPA + JDBC Template

---

<div align="center">

**Desenvolvido com ☕ e ❤️**

**Java 14 - Código Conciso e Moderno!**

</div>
