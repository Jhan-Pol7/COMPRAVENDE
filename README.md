# CompraVende

Sistema de escritorio para publicar, comprar y vender productos con fotos, tickets PDF e historial de transacciones.

## Stack

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 17 | Lógica del sistema |
| JavaFX + FXML | 21.0.2 | Interfaz visual |
| H2 | 2.2.224 | Base de datos embebida |
| HikariCP | 5.1.0 | Pool de conexiones |
| JasperReports | 6.21.5 | Tickets PDF |
| Logback | 1.4.14 | Registro de errores |
| Maven + shade-plugin | — | Compilar y empaquetar |

## Requisitos

- Java 17 instalado
- Maven 3.8+
- IntelliJ IDEA (para desarrollo)

## Ejecutar en desarrollo

```bash
mvn javafx:run
```

## Empaquetar

```bash
mvn package
java -jar target/CompraVende-1.0-SNAPSHOT.jar
```

## Credenciales por defecto

| Usuario | Contraseña | Rol |
|---|---|---|
| admin | 123 | ADMIN |

## Módulos

- **U01** Proyecto Maven + Git
- **U02** Base de datos H2 + HikariCP
- **U03** Entidades + DAO
- **U04** Ventana principal JavaFX
- **U05** CRUD de productos
- **U06** Imágenes BLOB
- **U07** Login y roles
- **U08** Catálogo con búsqueda y filtros
- **U09** Proceso de compra y transacciones
- **U10** Tickets PDF con JasperReports
- **U11** Diseño visual CSS
- **U12** Logback + empaquetado .jar