# Comunicación entre Tareas Distribuibles en NexaStream
¡Bienvenido a la emocionante era de la programación distribuida con NexaStream! Aquí te explicamos cómo las Tareas Distribuibles pueden comunicarse entre sí para lograr flujos de trabajo complejos y eficientes.

## Comprendiendo la Comunicación
En NexaStream, las Tareas Distribuibles pueden depender de los resultados de otras tareas, creando una red de dependencias que se traduce en una ejecución coordinada y eficiente. La clave de esta comunicación es el uso del parámetro returnValues en el método de la tarea.

```java
@DistributableTask(name = "analyze-data", dependencies = {"greeting"})
public Iterable<List<Map<String, Map<String, Integer>>>> analyzeData(Map<String, Object> returnValues) {
// Lógica de la tarea...
}
```

## El Mapa de Retorno (returnValues)
El parámetro returnValues es un mapa donde cada clave representa el nombre de una tarea de la que depende la tarea actual, y el valor es el resultado devuelto por esa tarea. Esto facilita la comunicación y el intercambio de datos entre tareas distribuibles.

## Coordinación de Tareas
Con esta capacidad de comunicación, puedes coordinar tareas distribuibles de manera efectiva. Cada tarea puede acceder a los resultados de las tareas de las que depende, permitiendo flujos de trabajo más complejos y una programación distribuida más potente.

## Ejemplo Práctico
Supongamos que tienes una tarea "greeting" que saluda y devuelve un mensaje. Puedes utilizar este mensaje en la tarea "analyze-data" para personalizar la análisis de datos.

```java
@DistributableTask(name = "greeting")
public String greetingTask() {
    return "¡Hola, mundo!";
}


@DistributableTask(name = "analyze-data", dependencies = {"greeting"})
public Iterable<List<Map<String, Map<String, Integer>>>> analyzeData(Map<String, Object> returnValues) {
    String greetingMessage = (String) returnValues.get("greeting");
    // Lógica de la tarea usando el mensaje de saludo...
}
```
¡Explora las posibilidades infinitas de coordinación entre tareas distribuibles en NexaStream y lleva tus flujos de trabajo al siguiente nivel!

## Ejemplo de guardado de news-analyzer

En esta ocasion trabajaremos un poco con una tarea en la que se procesan los datos y luego en otra funcion se persisten los datos

```java
@DistributableTask(name = "on-save", dependencies = {"analyze-data"})
public Iterable<List<Map<String, Map<String, Integer>>>> onSave(Map<String, Object> returnValues){
        Iterable<List<Map<String, Map<String, Integer>>>> data = (Iterable<List<Map<String, Map<String, Integer>>>>) returnValues.get("analyze-data");
        data
                .forEach(maps -> {
                    // Objeto ObjectMapper de Jackson
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        // Convierte el objeto 'data' a formato JSON y lo guarda en un archivo
                        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("data.json"), data);

                        System.out.println("Datos guardados exitosamente en 'data.json'");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        return data;
}
```
## Contribuciones

NexaStream es un proyecto de código abierto y estamos emocionados de recibir contribuciones de la comunidad. Si tienes ideas, correcciones o características que te gustaría aportar, por favor consulta nuestra [Guía de Contribución](CONTRIBUTION.md) para obtener más detalles sobre cómo colaborar.

¡Gracias por unirte a la comunidad de NexaStream!

[Website de NaT](https://www.nattechnologiesagency.com/) | [Documentación](WIKI.md)