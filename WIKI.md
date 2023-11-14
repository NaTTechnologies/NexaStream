# Guía de Uso de NexaStream

Esta guía te ayudará a comenzar con NexaStream y te mostrará cómo programar los nodos utilizando la anotación `@Node`. Además, aprenderás a acceder a los nodos a través de la API REST.

## Prerrequisitos

1. Antes de empezar, asegúrate de que has clonado el proyecto NexaStream y has instalado la dependencia en tu proyecto Maven.
```bash
git clone https://github.com/NaTTechnologies/NexaStream.git
cd NexaStream
mvn clean install
```
2. Luego agrega la dependencia en tu pom.xml.
```xml
<dependency>
    <groupId>com.nat.nexastream</groupId>
    <artifactId>nexa-stream</artifactId>
    <version>1.0</version>
</dependency>
```

## Programación de Nodos con @Node

La anotación `@Node` se utiliza para marcar una clase como un nodo que ejecutará tareas distribuidas. Para programar un nodo, sigue estos pasos:

1.  Anota tu clase con `@Node` y proporciona un nombre descriptivo al nodo.
```java
@Node(name = "MiNodo")
public class MyNode {
    // Lógica del nodo
}
```
2. Crea métodos en tu clase que realizarán tareas específicas. Estos métodos se marcarán con `@DistributableTask`.
```java
@DistributableTask(priority = 1, dependencies = {"OtraTarea"})
public void realizarTarea() {
    // Lógica de la tarea
}
```
3. Asegúrate de que tu nodo y sus tareas estén disponibles en el paquete escaneado por NexaStream, especificando en el *application.properties*.
```properties
com.nat.nexastream.packageName=com.nat.nexastream.example.other
```
## Iniciar un Nodo

Para iniciar un nodo, puedes hacerlo a través de la API REST. Puedes utilizar CURL o cualquier otra herramienta similar. Aquí hay un ejemplo usando CURL:
```bash
curl -X POST http://localhost:8080/nodes/run-node/MiNodo
```
Reemplaza `MiNodo` con el nombre del nodo que deseas ejecutar.

## Ejecutar una Tarea

Una vez que has iniciado un nodo, puedes ejecutar una tarea específica en ese nodo. Aquí tienes un ejemplo:
```bash
curl -X POST http://localhost:8080/nodes/run-task/realizarTarea
```
Reemplaza `realizarTarea` con el nombre de la tarea que deseas ejecutar.

Estos son ejemplos iniciales de cómo puedes usar NexaStream y programar nodos utilizando `@Node` y `@DistributableTask`. Puedes personalizar y ampliar estos ejemplos según tus necesidades y requisitos específicos.

## Contribuciones

NexaStream es un proyecto de código abierto y estamos emocionados de recibir contribuciones de la comunidad. Si tienes ideas, correcciones o características que te gustaría aportar, por favor consulta nuestra [Guía de Contribución](CONTRIBUTION.md) para obtener más detalles sobre cómo colaborar.

¡Gracias por unirte a la comunidad de NexaStream!

[Website de NaT](https://www.nattechnologiesagency.com/) | [Documentación](WIKI.md)