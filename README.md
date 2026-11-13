# RoomServDev – Hotel Reservation System

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?logo=apachemaven&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-9.x-4479A1?logo=mysql&logoColor=white)
![Project](https://img.shields.io/badge/Project-Hotel%20Reservations-blue)
![Repository](https://img.shields.io/badge/Repository-Public-lightgrey)

[![Telegram](https://img.shields.io/badge/Telegram-Team%20Chat-26A5E4?logo=telegram&logoColor=white)](https://web.telegram.org/k/#-5417727149)

## Descripción

RoomServDev es una aplicación de consola desarrollada en Java para la gestión de reservas hoteleras, clientes, hoteles y habitaciones.

El proyecto se desarrolla de forma colaborativa en el contexto de la asignatura **FP447 – Programación Orientada a Objetos con Bases de Datos**.

Su objetivo es aplicar los principios de programación orientada a objetos, separación de responsabilidades, patrones de diseño y persistencia de datos mediante JDBC y MySQL.

Actualmente, el proyecto se encuentra en proceso de migración desde una gestión de datos en memoria hacia una arquitectura de persistencia basada en DAO y MySQL.

## Tabla de contenidos

- [RoomServDev – Hotel Reservation System](#roomservdev--hotel-reservation-system)
  - [Descripción](#descripción)
  - [Tabla de contenidos](#tabla-de-contenidos)
  - [Funcionalidades](#funcionalidades)
  - [Tecnologías](#tecnologías)
  - [Requisitos](#requisitos)
  - [Instalación y configuración](#instalación-y-configuración)
    - [1. Clonar el repositorio](#1-clonar-el-repositorio)
    - [2. Abrir el proyecto](#2-abrir-el-proyecto)
    - [3. Crear la base de datos](#3-crear-la-base-de-datos)
    - [4. Configurar la conexión a MySQL](#4-configurar-la-conexión-a-mysql)
  - [Ejecución del proyecto](#ejecución-del-proyecto)
    - [Compilar](#compilar)
    - [Ejecutar desde IntelliJ IDEA](#ejecutar-desde-intellij-idea)
    - [Ejecutar las pruebas](#ejecutar-las-pruebas)
  - [Pruebas](#pruebas)
  - [Estructura del proyecto](#estructura-del-proyecto)
    - [Responsabilidades de los paquetes](#responsabilidades-de-los-paquetes)
  - [Arquitectura y persistencia](#arquitectura-y-persistencia)
    - [Componentes principales](#componentes-principales)
    - [Modelo de datos](#modelo-de-datos)
  - [Buenas prácticas de desarrollo](#buenas-prácticas-de-desarrollo)
  - [Trabajo colaborativo con Git](#trabajo-colaborativo-con-git)
    - [Flujo de trabajo recomendado](#flujo-de-trabajo-recomendado)
    - [Convención de commits](#convención-de-commits)
  - [Solución de problemas](#solución-de-problemas)
    - [Error de conexión con MySQL](#error-de-conexión-con-mysql)
    - [Error de compilación por versión de Java](#error-de-compilación-por-versión-de-java)
    - [Error de tablas inexistentes](#error-de-tablas-inexistentes)
    - [Maven no descarga las dependencias](#maven-no-descarga-las-dependencias)
  - [Autores](#autores)
  - [Código de conducta](#código-de-conducta)
  - [Licencia](#licencia)
  - [Historial de cambios](#historial-de-cambios)

## Funcionalidades

La aplicación contempla los siguientes módulos:

| Módulo | Funcionalidad | Persistencia actual |
| --- | --- | --- |
| Clientes | Alta, consulta, modificación y eliminación | MySQL mediante DAO |
| Hoteles | Gestión de hoteles | Memoria |
| Habitaciones | Consulta y gestión de habitaciones simples, dobles y suites | Memoria en la aplicación; DAO MySQL implementado |
| Reservas | Registro y consulta de reservas | Memoria en la aplicación; DAO MySQL implementado |

La integración completa de todos los módulos con la base de datos se encuentra pendiente.

## Tecnologías

| Tecnología | Uso |
| --- | --- |
| Java 21 | Lenguaje de programación |
| Maven | Gestión del proyecto y dependencias |
| MySQL | Sistema gestor de bases de datos |
| JDBC | Conexión y comunicación con MySQL |
| MySQL Connector/J 9.5.0 | Driver JDBC |
| JUnit 5 | Pruebas automatizadas |
| Git y GitHub | Control de versiones y colaboración |
| IntelliJ IDEA | Entorno de desarrollo recomendado |

## Requisitos

Antes de instalar el proyecto, asegúrate de disponer de:

- **JDK 21:** para compilar y ejecutar la aplicación.
- **MySQL Server:** para disponer del servidor de base de datos.
- **Git:** para clonar y gestionar el repositorio.
- **IntelliJ IDEA** u otro IDE compatible con proyectos Maven (opcional, pero recomendado).

No es necesario instalar Maven globalmente, ya que el repositorio incluye Maven Wrapper.

Comprueba las instalaciones desde una terminal:

```bash
java -version
git --version
```

La versión de Java debe ser la 21 o compatible con la versión configurada en el proyecto.

## Instalación y configuración

### 1. Clonar el repositorio

Clona el repositorio desde GitHub:

```bash
git clone <URL_DEL_REPOSITORIO>
```

Accede al directorio del proyecto:

```bash
cd hotel-booking-system
```

Sustituye `<URL_DEL_REPOSITORIO>` por la dirección HTTPS o SSH del repositorio.

### 2. Abrir el proyecto

Abre la carpeta raíz del proyecto desde IntelliJ IDEA:

1. Selecciona **File → Open**.
2. Selecciona la carpeta `hotel-booking-system`.
3. Espera a que IntelliJ importe las dependencias de Maven.
4. Comprueba que el proyecto utiliza JDK 21.

Maven reconocerá automáticamente el archivo `pom.xml`.

### 3. Crear la base de datos

El proyecto incluye dos scripts SQL:

- `src/main/resources/bbdd/schema.sql`: creación de la base de datos y sus tablas.
- `src/main/resources/bbdd/data.sql`: inserción de datos iniciales de prueba.

Abre MySQL Workbench o una herramienta compatible con MySQL.

Ejecuta primero:

```text
src/main/resources/bbdd/schema.sql
```

Después ejecuta:

```text
src/main/resources/bbdd/data.sql
```

**Importante:** `schema.sql` contiene instrucciones `DROP TABLE IF EXISTS`. Al ejecutarlo se eliminan las tablas existentes antes de recrearlas. Utilízalo únicamente en un entorno de desarrollo o cuando tengas claro que puedes perder los datos anteriores.

El esquema crea la base de datos:

```text
roomservdev_db
```

### 4. Configurar la conexión a MySQL

El proyecto utiliza un archivo local de configuración para gestionar las credenciales de conexión.

En la carpeta:

```text
src/main/resources/
```

Encontrarás:

```text
db.properties.example
```

Crea una copia del archivo y llámala:

```text
db.properties
```

Puedes hacerlo desde PowerShell:

```powershell
Copy-Item .\src\main\resources\db.properties.example .\src\main\resources\db.properties
```

Edita `db.properties` con los datos de tu instalación de MySQL:

```properties
db.url=jdbc:mysql://localhost:3306/roomservdev_db
db.user=root
db.password=TU_CONTRASEÑA
```

Sustituye los valores por los correspondientes a tu entorno.

**Seguridad:**

- `db.properties` contiene credenciales privadas y no debe subirse a Git.
- El archivo está incluido en `.gitignore`.
- `db.properties.example` sí debe mantenerse versionado para facilitar la configuración de otros desarrolladores.
- Nunca incluyas contraseñas reales en el código fuente ni en los commits.

## Ejecución del proyecto

### Compilar

Desde la carpeta raíz del repositorio, ejecuta:

**Windows (PowerShell):**

```powershell
.\mvnw.cmd clean compile
```

**Linux / macOS:**

```bash
./mvnw clean compile
```

Maven descargará las dependencias necesarias y compilará el proyecto.

### Ejecutar desde IntelliJ IDEA

1. Abre `src/main/java/RoomServDev/Main.java`.
2. Localiza el método `main()`.
3. Pulsa el icono **Run** situado junto al método.
4. La aplicación iniciará su menú principal.

La aplicación necesita que MySQL esté iniciado y que `db.properties` contenga una configuración válida para utilizar el módulo de clientes.

### Ejecutar las pruebas

Para ejecutar las pruebas automatizadas:

**Windows:**

```powershell
.\mvnw.cmd test
```

**Linux / macOS:**

```bash
./mvnw test
```

Para realizar una compilación limpia y ejecutar las pruebas:

```powershell
.\mvnw.cmd clean test
```

## Pruebas

El proyecto contiene pruebas en:

```text
src/test/java/RoomServDev/
```

Actualmente encontramos:

| Archivo | Tipo | Objetivo |
| --- | --- | --- |
| `TestDatos.java` | JUnit 5 | Comprobar operaciones del modelo en memoria |
| `TestDatabaseConnection.java` | Prueba manual | Comprobar la conexión JDBC |
| `TestHabitacionDAO.java` | Prueba manual | Comprobar operaciones DAO de habitaciones |

Las pruebas JUnit se ejecutan automáticamente mediante Maven.

Las pruebas manuales deben ejecutarse individualmente desde el IDE o mediante una configuración de ejecución apropiada.

Las pruebas que requieren MySQL necesitan que el servidor esté iniciado y que la configuración de conexión sea correcta.

## Estructura del proyecto

El proyecto sigue la estructura estándar de Maven:

```text
hotel-booking-system/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── RoomServDev/
│   │   │       ├── Main.java
│   │   │       │
│   │   │       ├── controller/
│   │   │       │
│   │   │       ├── model/
│   │   │       │
│   │   │       ├── persistence/
│   │   │       │   ├── connection/
│   │   │       │   ├── dao/
│   │   │       │   │   └── mysql/
│   │   │       │   └── factory/
│   │   │       │
│   │   │       └── view/
│   │   │
│   │   └── resources/
│   │       ├── db.properties.example
│   │       └── bbdd/
│   │           ├── schema.sql
│   │           └── data.sql
│   │
│   └── test/
│       ├── java/
│       │   └── RoomServDev/
│       └── resources/
│
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

### Responsabilidades de los paquetes

| Paquete | Responsabilidad |
| --- | --- |
| `RoomServDev` | Punto de entrada de la aplicación |
| `controller` | Controladores que coordinan las vistas y el modelo |
| `model` | Entidades, lógica de negocio y gestión de datos |
| `persistence.connection` | Gestión de conexiones JDBC |
| `persistence.dao` | Interfaces DAO y compatibilidad de conexión |
| `persistence.dao.mysql` | Implementaciones DAO para MySQL |
| `persistence.factory` | Creación de implementaciones DAO |
| `view` | Interfaces de usuario y menús |
| `src/test` | Pruebas automatizadas y manuales |

## Arquitectura y persistencia

El proyecto utiliza el patrón **DAO (Data Access Object)** para separar las operaciones de acceso a datos de la lógica de negocio.

También utiliza el patrón **Abstract Factory** para seleccionar las implementaciones de persistencia.

### Componentes principales

- **DatabaseConnection:** centraliza la conexión JDBC y la lectura de la configuración.
- **DAOFactory:** permite obtener la fábrica de acceso a datos.
- **MySQLDAOFactory:** proporciona las implementaciones DAO de MySQL.
- **Interfaces DAO:** definen las operaciones disponibles para cada entidad.
- **Implementaciones MySQL:** realizan las operaciones SQL mediante JDBC y `PreparedStatement`.

### Modelo de datos

La base de datos contempla las siguientes tablas:

- `clientes`
- `hoteles`
- `habitaciones`
- `habitaciones_simple`
- `habitaciones_doble`
- `habitaciones_suite`
- `reservas`

Las habitaciones utilizan una tabla principal y tablas específicas para almacenar los atributos de cada subtipo.

Las relaciones entre tablas se implementan mediante claves primarias y foráneas.

## Buenas prácticas de desarrollo

Para mantener la calidad y facilitar la evolución del proyecto, se recomienda:

- Mantener la separación entre modelo, vistas, controladores y persistencia.
- Definir las operaciones de acceso a datos mediante interfaces DAO.
- Utilizar `PreparedStatement` para ejecutar consultas parametrizadas.
- Gestionar conexiones y recursos JDBC mediante `try-with-resources`.
- Evitar duplicar lógica de negocio entre controladores y DAO.
- Utilizar nombres descriptivos para clases, métodos y variables.
- Mantener las dependencias y versiones centralizadas en `pom.xml`.
- No almacenar credenciales ni información sensible en el repositorio.
- Crear pruebas para las nuevas funcionalidades.
- Actualizar este README cuando cambien los procedimientos de instalación, ejecución o configuración.

## Trabajo colaborativo con Git

El proyecto utiliza Git y GitHub para coordinar el desarrollo entre los integrantes del equipo.

Se recomienda trabajar con ramas independientes para cada tarea o integrante, evitando realizar cambios directamente sobre `main`.

### Flujo de trabajo recomendado

1. Actualizar la rama principal:

```bash
git checkout main
git pull origin main
```

2. Cambiar a la rama de trabajo:

```bash
git checkout nombre-rama
```

3. Integrar los cambios de `main` cuando sea necesario:

```bash
git merge main
```

4. Comprobar los cambios:

```bash
git status
git diff
```

5. Preparar y confirmar los cambios:

```bash
git add .
git commit -m "tipo: descripción del cambio"
```

6. Subir la rama:

```bash
git push origin nombre-rama
```

### Convención de commits

Se recomienda utilizar mensajes breves y descriptivos:

| Prefijo | Uso |
| --- | --- |
| `feat:` | Nueva funcionalidad |
| `fix:` | Corrección de errores |
| `refactor:` | Reestructuración sin cambiar el comportamiento |
| `test:` | Incorporación o modificación de pruebas |
| `docs:` | Documentación |
| `chore:` | Tareas de mantenimiento |

Ejemplos:

```text
feat: implement MySQL persistence for clients
fix: correct room availability validation
refactor: improve DAO connection handling
test: add room DAO integration tests
docs: update project README
```

## Solución de problemas

### Error de conexión con MySQL

Comprueba:

- Que MySQL Server esté iniciado.
- Que el puerto configurado sea correcto.
- Que el nombre de la base de datos sea `roomservdev_db`.
- Que las credenciales de `db.properties` sean válidas.
- Que el archivo se encuentre en `src/main/resources/`.

### Error de compilación por versión de Java

Comprueba que IntelliJ IDEA y Maven utilizan JDK 21.

Puedes verificar la versión activa con:

```bash
java -version
```

### Error de tablas inexistentes

Comprueba que has ejecutado `schema.sql` y que la base de datos se ha creado correctamente.

Si necesitas datos iniciales, ejecuta también `data.sql`.

### Maven no descarga las dependencias

Comprueba la conexión a Internet y vuelve a ejecutar:

```powershell
.\mvnw.cmd clean test
```


### Configuración de la Base de Datos

## ⚙️ Configuración de la Base de Datos

Para proteger las credenciales reales, este proyecto utiliza un archivo properties ignorado por Git. Para ejecutar el proyecto, siga estos dos pasos:

1. Ejecute el script `schema.sql` (ubicado en src/main/resources/bbdd/) en su servidor MySQL local.
2. En esa misma ruta (`src/main/resources/bbdd/`), duplique el archivo `db.properties.example` y renómbrelo a `db.properties`.
3. Edite `db.properties` con sus credenciales locales de MySQL:
   db.url=jdbc:mysql://localhost:3306/roomservdev_db
   db.user=su_usuario
   db.password=su_contraseña

## Autores

Proyecto desarrollado colaborativamente por:

- **[Adrià Rigall](https://www.github.com/Rigui73)**
- **[Erik Coll](https://www.github.com/ecollra)**
- **[J. Alejandro Palomanes](https://www.github.com/AlejandroUOC79)**
- **[Daniel Gallego](https://www.github.com/DaniDAM6767)**

## Código de conducta

Consulta [CODE_OF_CONDUCT.md](.github/CODE_OF_CONDUCT.md) para conocer las normas de colaboración y convivencia del proyecto.

## Licencia

Actualmente, el proyecto no dispone de una licencia de distribución específica.

Consulta el archivo [LICENSE](LICENSE) si se incorpora una licencia en el futuro.

## Historial de cambios

Consulta [CHANGELOG.md](CHANGELOG.md) para revisar el historial de modificaciones del proyecto.
