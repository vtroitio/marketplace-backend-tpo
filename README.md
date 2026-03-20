# Marketplace Backend - TPO Grupo 9

Backend del sistema **Marketplace** desarrollado como Trabajo Práctico Obligatorio para la materia **Aplicaciones Interactivas** en **UADE**.

El proyecto está implementado utilizando **Spring Boot** y sigue una arquitectura **modular por dominio**, priorizando simplicidad, mantenibilidad y claridad en la separación de responsabilidades.

## Integrantes

| Alumno                  | LU      | Mail UADE                    |
|-------------------------|---------|------------------------------|
| Garcia, Matias Nicolas  |         |                              |
| Giulietti, Juan Manuel  | 1199949 | <jgiulietti@uade.edu.ar>     |
| Rodriguez, Tobias       | 1177362 | <tobiarodriguez@uade.edu.ar> |
| Troitiño, Valentin Blas | 1205019 | <vtroitino@uade.edu.ar>      |

## Requisitos para desarrollo

Para poder ejecutar y desarrollar en este proyecto se requiere tener instalado:

### JDK 17 (Java 17)

Verificar instalación:

```bash
java -version
```

### IDE recomendado

Se recomienda utilizar alguno de los siguientes:

* IntelliJ IDEA
* VSCode con extensiones de Java
* Eclipse

## Instalación del proyecto

Clonar el repositorio:

```bash
git clone <repo-url>
```

Entrar al directorio:

```bash
cd marketplace
```

Instalar dependencias:

```bash
mvn clean install
```

## Ejecutar la aplicación

Para iniciar el servidor:

```bash
mvn spring-boot:run
```

La API quedará disponible en:

```bash
http://localhost:8080
```

## Documentación de la API

La documentación interactiva de la API se genera automáticamente con Swagger.

Disponible en:

```bash
http://localhost:8080/swagger-ui/index.html
```

Desde ahi se pueden probar todos los endpoints directamente.

## Arquitectura del proyecto

El proyecto sigue una arquitectura **modular por dominio con capas internas**.

Ejemplo:

```bash
marketplace
│
├── products
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   └── mapper
│
├── users
│
└── orders
```

Cada módulo contiene sus propias capas internas:

### Controller

Responsable de manejar las solicitudes HTTP.

Funciones:

* recibir requests
* validar DTOs
* delegar lógica al service

---

### Service

Contiene la **lógica de negocio** del módulo.

Funciones:

* coordinar operaciones
* manejar errores de negocio
* interactuar con el repositorio

---

### Repository

Encapsula el acceso a datos.

Actualmente se utiliza una implementación **in-memory** para facilitar el desarrollo inicial.

En el futuro puede reemplazarse por una implementación con base de datos sin modificar la lógica de negocio.

---

### Entity

Representa el modelo de dominio utilizado internamente por la aplicación.

---

### DTO

Los DTOs representan los **contratos de la API**.

Se utilizan para:

* requests
* responses

Esto permite desacoplar la API pública del modelo interno.

---

### Mapper

Los mappers se encargan de transformar entre:

```bash
DTO ↔ Entity
```

Esto evita que los controllers o services dependan directamente del formato de la API.

## Convenciones del proyecto

Para mantener consistencia en el código se siguen las siguientes reglas.

### Idioma

Todo el código debe estar escrito en **inglés**, incluyendo:

* nombres de clases
* nombres de métodos
* nombres de variables
* DTOs
* comentarios (evitarlos)
* documentación (Javadoc)

Esto incluye también:

* controllers
* services
* repositories
* entities
* mappers
* dtos

La documentación del repositorio (README, issues, etc.) puede estar en español, pero **el código fuente siempre debe estar en inglés**.

Sí, conviene ampliar esa sección para cubrir **clases, métodos, variables, constantes, paquetes/carpetas y endpoints**. Te dejo una versión más completa y prolija para el README:

### Naming

Para mantener consistencia y legibilidad, todo el código debe seguir una convención de nombres uniforme.

#### Clases

Las clases deben utilizar nombres en singular y en **PascalCase**.

Correcto:

```text
Product
ProductService
ProductRepository
ProductController
CreateProductDto
ProductResponseDto
````

Incorrecto:

```text
ProductsService
ProductsRepository
productService
product_controller
```

#### Métodos

Los métodos deben usar **camelCase** y representar claramente la acción que realizan.

Correcto:

```text
getProducts
getProductById
createProduct
findAll
findById
toEntity
toResponseDto
```

Incorrecto:

```text
GetProducts
product_by_id
doProduct
mapper
```

#### Variables y atributos

Las variables, parámetros y atributos deben usar **camelCase** y nombres descriptivos.

Correcto:

```text
product
productId
productsRepository
createdProduct
requestDto
```

Incorrecto:

```text
p
x
prod_id
repositoryProducts
```

#### Constantes

Las constantes deben escribirse en **UPPER_SNAKE_CASE**.

Correcto:

```text
DEFAULT_PAGE_SIZE
MAX_PRICE
PRODUCT_NOT_FOUND_MESSAGE
```

Incorrecto:

```text
defaultPageSize
MaxPrice
productNotFoundMessage
```

#### Paquetes y carpetas

Los paquetes y carpetas deben escribirse en **minúsculas**, sin espacios ni caracteres especiales, y reflejar claramente su responsabilidad.

Correcto:

```text
products
products.controller
products.service
products.repository
products.dto
products.mapper
products.entity
common.exception
```

Incorrecto:

```text
Products
productService
product-controller
DTOs
Exceptions
```

#### Endpoints REST

Los endpoints deben usar nombres en plural, en minúsculas y orientados a recursos.

Correcto:

```text
GET /products
GET /products/{id}
POST /products
PUT /products/{id}
DELETE /products/{id}
```

Incorrecto:

```text
GET /getProducts
POST /createProduct
GET /product/getById
```

### DTOs

Se utilizan DTOs separados para requests y responses.

Ejemplo:

```text
CreateProductDto
ProductResponseDto
```

Los DTOs utilizan **records** cuando es posible.

### Validación

Las validaciones se definen en los DTOs utilizando **Jakarta Validation**.

Ejemplo:

```java
@NotBlank
@NotNull
@Positive
```

La validación se ejecuta automáticamente mediante `@Valid` en los controllers.

### Repositorios

Los repositorios deben devolver `Optional` cuando una entidad puede no existir.

Ejemplo:

```java
Optional<Product> findById(int id)
```

### Mappers

Cada módulo posee su propio mapper.

Ejemplo:

```java
ProductMapper
UserMapper
```

Los mappers no deben mezclarse entre módulos.

### Streams y Optional

Se utilizan:

* `Optional` para representar valores que pueden no existir
* `Stream` para transformar colecciones
