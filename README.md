# 🎵 Music APP - Backend

Backend desarrollado con **Spring Boot** para una aplicación de reproducción musical estilo Spotify.

La API gestiona usuarios, autenticación, canciones y subida de archivos multimedia, proporcionando los endpoints necesarios para que el cliente pueda interactuar con la aplicación.
Tanto la base de datos PostgreSQL y el servicio están virtualizados mediante contenedores docker

---

## Tecnologías utilizadas

- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Flyway
- PostgreSQL
- Spring Security
- Maven
- REST API
- Docker

---
## ⚙️ Configuración

Antes de iniciar el proyecto configura la conexión con la base de datos y la ruta donde se guardan las canciones en:

src/main/resources/application.properties

Ejemplo:
```properties
spring.datasource.url=jdbc:postgresql://db:5432/musicdb
spring.datasource.username=root
spring.datasource.password=password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

audio.storage.path=C:/Users/alvaro/Desktop/songs

