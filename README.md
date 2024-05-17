# Mail Relayer

Microservicio que re-transmite los correos electrónicos enviados por las aplicaciones clientes hacia otro servidor SMTP.

En la carpeta `mail-relayer` se encuentran las capas de dominio, servicios, puertos de entrada y de salida.

En la carpeta `mail-relayer-rest` se encuentran las configuraciones de los adaptadores y la API REST.

## Arquitectura hexagonal

Se utiliza [arquitectura hexagonal](http://www.dossier-andreas.net/software_architecture/ports_and_adapters.html) con un arquetipo acoplado al framework de [Spring Boot](https://spring.io/projects/spring-boot).

Se recomienda la lectura de [Hexagonal Architecture with Java and Spring](https://reflectoring.io/spring-hexagonal/)

![Arquitectura hexagonal](https://www.dossier-andreas.net/software_architecture/ports-and-adapters.png)

| amarillo    | lógica del núcleo       |
| ----------- | ----------------------- |
| rojo claro  | puertos primarios       |
| azul claro  | adaptadores primarios   |
| rojo oscuro | puertos secundarios     |
| azul oscuro | adaptadores secundarios |

# Domain Driven Design

Se utiliza el Diseño Guiado por Dominio o [_Domain Driven Design_](https://en.wikipedia.org/wiki/Domain-driven_design) por lo que se recomienda la lectura de los conceptos [Aggregate](https://martinfowler.com/bliki/DDD_Aggregate.html) y [Value Object](https://martinfowler.com/bliki/ValueObject.html).

![Domain Driven Design](https://miro.medium.com/v2/resize:fit:440/1*5vtEh7kuh7Tt1ZhX-yOGsg.png)

# Biliografía

- [Hexagonal architecture](https://alistair.cockburn.us/hexagonal-architecture/) por el creador Alistair Cockburn
- [Introducción Arquitectura Hexagonal - DDD](https://www.youtube.com/watch?v=GZ9ic9QSO5U&t=38s&pp=ygUfY29kZWx5dHYgYXJxdWl0ZWN0dXJhIGhleGFnb25hbA%3D%3D)
- [Aprende Arquitectura Hexagonal en 10 minutos](https://www.youtube.com/watch?v=eNFAJbWCSww&t=1s&pp=ygUfY29kZWx5dHYgYXJxdWl0ZWN0dXJhIGhleGFnb25hbA%3D%3D)
- [Get Your Hands Dirty on Clean Architecture](https://reflectoring.io/book/)
- [Curso Arquitectura Hexagonal](https://pro.codely.com/library/arquitectura-hexagonal-31201/66748/about/)
- [Organizing Layers Using Hexagonal Architecture, DDD, and Spring](https://www.baeldung.com/hexagonal-architecture-ddd-spring)
