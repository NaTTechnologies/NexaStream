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

# Conexión al WebSocket de NexaStream

NexaStream proporciona un WebSocket para la comunicación en tiempo real entre los nodos y la aplicación cliente. Puedes conectarte a este WebSocket utilizando el protocolo STOMP (Simple Text Oriented Messaging Protocol). A continuación, se muestra un ejemplo de cómo configurar y conectarse utilizando StompJS.

## Configuración básica con StompJS

Primero, asegúrate de incluir StompJS en tu proyecto. Puedes hacerlo mediante CDN o descargando el paquete.

```html
<script src="https://cdn.jsdelivr.net/npm/stompjs"></script>
```

Luego, configura una conexión WebSocket utilizando StompJS.

```javascript
// Configuración de la conexión WebSocket
const socket = new WebSocket('ws://localhost:8080/ws-endpoint');
const stompClient = Stomp.over(socket);

// Conexión al servidor WebSocket
stompClient.connect({}, frame => {
    console.log('Conexión establecida:', frame);

    // Suscripción a un canal específico
    stompClient.subscribe('/topic/node-registered', message => {
        console.log('Mensaje recibido:', JSON.parse(message.body));
    });
});

// Desconexión del servidor WebSocket al cerrar la página
window.onbeforeunload = () => {
    stompClient.disconnect();
};

`````

En este ejemplo, la URL del WebSocket es ws://localhost:8080/ws-endpoint, ajusta la URL según tu entorno de desarrollo.

## Enviando mensajes al servidor
Ahora puedes enviar mensajes al servidor WebSocket, por ejemplo, registrando un nuevo nodo.

```javascript
// Enviar un mensaje para registrar un nuevo nodo
const newNode = {
    id: 'node-123',
    // ... otros atributos del nodo
};
stompClient.send('/app/register-node', {}, JSON.stringify(newNode));
```

Recuerda ajustar la ruta /app/register-node según el controlador que estás utilizando.

¡Ahora estás listo para conectarte y comunicarte con el servidor WebSocket de NexaStream!

Este ejemplo proporciona una configuración básica y muestra cómo subscribirse a un canal específico y enviar mensajes al servidor. Ajusta las rutas y la configuración según tus necesidades específicas.

## Ejecutar un Nodo

Puedes enviar un mensaje para ejecutar un nodo específico.

```javascript
// Enviar un mensaje para ejecutar un nodo
const nodeName = 'node-123';
stompClient.send(`/app/run-node/${nodeName}`, {});
````

Ajusta la ruta /app/run-node/{nodeName} según tu configuración.

## Ejecutar una Tarea
Para ejecutar una tarea, envía un mensaje con el nombre de la tarea.

````javascript
// Enviar un mensaje para ejecutar una tarea
const taskName = 'task-abc';
stompClient.send(`/app/run-task/${taskName}`, {});
````

Ajusta la ruta /app/run-task/{taskName} según tu configuración.

## Actualizar Recursos de un Nodo
Si necesitas actualizar los recursos de un nodo, envía un mensaje con la información actualizada.

`````javascript
// Enviar un mensaje para actualizar los recursos de un nodo
const nodeId = 'node-123';
const updatedResources = { cpu: 4, memory: 8192 };
stompClient.send(`/app/update-node-resources/${nodeId}`, {}, JSON.stringify(updatedResources));
`````

Ajusta la ruta /app/update-node-resources/{nodeId} según tu configuración.

## Obtener Información de un Nodo
Puedes solicitar información sobre un nodo específico enviando un mensaje.

`````javascript
// Enviar un mensaje para obtener información de un nodo
const nodeId = 'node-123';
stompClient.send(`/app/get-node/${nodeId}`, {});
`````

Ajusta la ruta /app/get-node/{nodeId} según tu configuración.

## Eliminar un Nodo
Para eliminar un nodo, envía un mensaje con el ID del nodo.

`````javascript
// Enviar un mensaje para eliminar un nodo
const nodeId = 'node-123';
stompClient.send(`/app/remove-node/${nodeId}`, {});
`````

Ajusta la ruta /app/remove-node/{nodeId} según tu configuración.

¡Ahora tienes ejemplos para conectar y comunicarte con el servidor WebSocket de NexaStream desde tu aplicación cliente usando StompJS! Asegúrate de ajustar las rutas según la configuración de tu aplicación.


Este conjunto de ejemplos proporciona información sobre cómo ejecutar operaciones específicas a través del WebSocket de NexaStream utilizando StompJS. Asegúrate de ajustar las rutas según tu configuración específica.

## Otros articulos

- [Comunicacion entre tareas distribuibles en NexaStream](COMMUNICATION_BETWEEN_DISTRIBUTABLE_TASKS_IN_NEXASTREAM.md)
- [Analizando las noticias](EXAMPLE_NEWS_ANALYZER.md)
- [NexaStream Functions](NEXASTREAM_FUNCTIONS.md)

## ¡Participa en Nuestra Encuesta!

Nos encantaría conocer tu experiencia y perspectiva sobre la integración de NexaStream con entornos de desarrollo existentes. Tu opinión es crucial para mejorar la plataforma y adaptarla aún mejor a tus necesidades.

### ¿Cómo puedes contribuir?
Te invitamos a participar en nuestra [encuesta sobre integración con entornos de desarrollo](https://forms.gle/wj91QUmxi2CMEdsk9). Es una oportunidad para compartir tus experiencias, sugerencias y ayudarnos a entender cómo NexaStream puede optimizar tus flujos de trabajo actuales.

[¡Contestar encuesta!](https://forms.gle/wj91QUmxi2CMEdsk9)

### ¿Por qué participar?
Impacta en el Futuro: Tus respuestas influyen en las futuras mejoras y desarrollos de NexaStream.
Colaboración Constructiva: Comparte tus desafíos y sugerencias para una colaboración más efectiva.
Comunidad en Crecimiento: Únete a otros desarrolladores y forma parte activa de la comunidad NexaStream.
¡Esperamos contar con tu valiosa contribución para hacer de NexaStream una herramienta aún más poderosa y adaptada a tus necesidades específicas!


## Contribuciones

NexaStream es un proyecto de código abierto y estamos emocionados de recibir contribuciones de la comunidad. Si tienes ideas, correcciones o características que te gustaría aportar, por favor consulta nuestra [Guía de Contribución](CONTRIBUTION.md) para obtener más detalles sobre cómo colaborar.

¡Gracias por unirte a la comunidad de NexaStream!

[Website de NaT](https://www.nattechnologiesagency.com/) | [Documentación](WIKI.md)