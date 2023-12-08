# NexaStream Functions

¡Bienvenido a NexaStream Functions, la nueva joya de nuestra plataforma que amplía tus posibilidades de desarrollo! 🚀💻

## ¿Qué es NexaStream Functions?
NexaStream Functions es una emocionante adición a nuestro ecosistema que permite integrar funciones en NexaStream utilizando el lenguaje JACTL, un lenguaje de programación versátil similar a Python y Perl. Con esta funcionalidad, experimentarás un nuevo nivel de flexibilidad y capacidad de personalización en tu desarrollo.

## ¿Cómo Comenzar?
**1. Definición de Nodo y Tarea Distribuible:**

Define tu nodo y las tareas distribuibles en una interfaz anotada. Especifica el entorno JACTL para aprovechar al máximo las funciones.

```java
@Node(name = "example")
public interface ExampleNodeScript {
    @DistributableTask(name = "hello-world", enviroment = DistributableTask.Enviroment.JCTL)
    Object helloWorld(Map<String, Object> returnValues);
}
```
**2. Estructura del Proyecto:**

Organiza tu proyecto con un directorio "scripts". Dentro de este, crea una carpeta para cada nodo y agrega archivos JACTL con el nombre de las tareas distribuibles.
```bash
- src
  - main
    - resources
      - scripts
        - example
          - hello-world.jctl
```
**3. Código JACTL Ejemplo:**

Escribe tu lógica de función JACTL. Recuerda llamar a la función y pasarle el returnValues proveniente de las dependencias de la tarea.

```groovy
def helloWorld(returnValues){
    println("Hello World");
    return "Hello World";
}

helloWorld(returnValues);
```

**4. Ejecución:**

Una vez que has iniciado un nodo, puedes ejecutar una tarea específica en ese nodo. Aquí tienes un ejemplo:
```bash
curl -X POST http://localhost:8080/nodes/run-task/hello-world
```
**¿Por Qué NexaStream Functions?**

**Flexibilidad Increíble:**
Experimenta con funciones personalizadas y adapta NexaStream a tus necesidades específicas.

**Integración Sencilla:**
La integración es tan sencilla como definir tu tarea distribuible y ejecutarla desde el servidor.

**Potencial Ilimitado:**
Desbloquea un potencial ilimitado de desarrollo distribuido con la facilidad y versatilidad de JACTL.

**¡Inspírate y Desarrolla con NexaStream Functions!**

Este readme es solo el comienzo de tu viaje con NexaStream Functions. Explora, experimenta y lleva tu programación distribuida al siguiente nivel. ¿Estás listo para desatar todo el potencial de NexaStream? 🚀🌐

¡Happy Coding! 🖥️✨

## Contribuciones

NexaStream es un proyecto de código abierto y estamos emocionados de recibir contribuciones de la comunidad. Si tienes ideas, correcciones o características que te gustaría aportar, por favor consulta nuestra [Guía de Contribución](CONTRIBUTION.md) para obtener más detalles sobre cómo colaborar.

¡Gracias por unirte a la comunidad de NexaStream!

[Website de NaT](https://www.nattechnologiesagency.com/) | [Documentación](WIKI.md)