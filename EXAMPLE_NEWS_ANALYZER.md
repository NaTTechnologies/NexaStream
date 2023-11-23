## Documentación del Ejemplo: News Analyzer
## Descripción
Este ejemplo muestra cómo utilizar NexaStream para analizar datos provenientes de un periódico en WordPress. El objetivo es contar la frecuencia de palabras y agruparlas por fechas. Para lograr esto, se utiliza un nodo llamado news-analyzer.

## Requisitos Previos
Antes de ejecutar este ejemplo, asegúrate de tener instalado NexaStream y las dependencias necesarias. También, ten en cuenta que este ejemplo hace uso de RxJava y OkHttpClient.

```xml
<dependency>
    <groupId>com.nat.nexastream</groupId>
    <artifactId>nexa-stream</artifactId>
    <version>1.0</version>
</dependency>

<dependency>
    <groupId>io.reactivex.rxjava3</groupId>
    <artifactId>rxjava</artifactId>
    <version>3.1.8</version>
</dependency>

<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>4.12.0</version>
</dependency>
```

## Configuración
El nodo news-analyzer está configurado como un nodo distribuido (@Node(name = "news-analyzer")). Este nodo tiene una dependencia de datos (@InjectDependency(name = "data")) que se conecta a la API de WordPress para obtener los datos de los posts.
```java
@Node(name = "news-analyzer")
public class NewsAnalyzer {
    // Lógica del nodo
}
```

## Ejecución
El método getData() realiza una solicitud a la API de WordPress para obtener los datos de los posts. La respuesta se emite como un flujo de datos observable (Observable<List>). Se utiliza un bucle para paginar a través de los resultados, y la respuesta se emite hasta que no hay más páginas.
```java
@InjectDependency(name = "data")
public Observable<List> data;

public static final int entries = 5;

@DataDependency(dataKey = "data")
public Observable<List> getData() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Crear un cliente OkHttp para hacer la solicitud a la API de WordPress
        OkHttpClient client = new OkHttpClient();

        // URL base de la API de WordPress
        String apiUrl = "https://roatanhableclaro.com/wp-json/wp/v2/posts";

        return Observable.create(emitter -> {
        String nextPageUrl = apiUrl;
        boolean hasMoreData = true;

        int count = 0;

        while (hasMoreData) {
            try {
                count++;
                System.out.println("Data encontred");
                // Crear una solicitud GET
                Request request = new Request.Builder()
                    .url(nextPageUrl)
                    .build();
        
                // Realizar la solicitud
                Response response = client.newCall(request).execute();
        
                // Verificar si la solicitud fue exitosa
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
            //                        System.out.println(responseBody);
                    emitter.onNext(objectMapper.readValue(responseBody, List.class)); // Emitir los datos obtenidos
            
                    // Verificar si hay más paginas disponibles en la respuesta
                    String linkHeader = response.header("Link");
                    // (linkHeader != null && linkHeader.contains("rel=\"next\""))
                        if (count < entries) {
                            int startIndex = linkHeader.indexOf('<');
                            int endIndex = linkHeader.indexOf('>');
                            nextPageUrl = linkHeader.substring(startIndex + 1, endIndex);
                        } else {
                            hasMoreData = false;
                        }
                    } else {
                            emitter.onError(new Exception("Error en la solicitud a la API"));
                    }
                } catch (Exception e) {
                    emitter.onError(e); // Manejar errores
                }
            }

            emitter.onComplete(); // Marcar la finalización del flujo de datos
        });
}
```
El método analyzeData() realiza el análisis de los datos. Se convierten los datos crudos en objetos Post, y se cuenta la frecuencia de palabras en el contenido de cada post. Los resultados se agrupan por fecha y se emiten como una lista de mapas.
```java
@DistributableTask(name = "analyze-data")
public Iterable<List<Map<String, Map<String, Integer>>>> analyzeData() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        SimpleDateFormat simpleDateFormatTo = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        File file = new File("data/");
        file.mkdirs();

        return data
            .map(list -> {
                return list
            .stream()
            .map(o -> {
                    return objectMapper.convertValue(o, Post.class);
                }).collect(Collectors.toList());
            })
            .map(list -> {
                List<Post> dataList = (List<Post>) list;
                List<Map<String, Map<String, Integer>>> maps = new ArrayList<>();
        
                dataList.forEach(post -> {
                    Map<String, Map<String, Integer>> dataPost = new HashMap<>();
            
                    String content = post.getContent().getRendered();
                    String[] words = content.split("\\s+"); // Dividir el texto en palabras
                    Map<String, Integer> wordCount = new HashMap<>();
            
                    for (String word : words) {
                        // Elimina signos de puntuación y convierte a minusculas
                        word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
            
                        // Actualiza el conteo de palabras
                        if (!word.isEmpty()) {
                            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                        }
                    }
            
                    //Agregamos al nuevo mapa
                    //dataPost.put(simpleDateFormat.format(post.getDate()), wordCount);
                    try {
                        dataPost.put(simpleDateFormatTo.format(simpleDateFormat.parse(post.getDate())), wordCount);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    maps.add(dataPost);
                });

            return maps;
        })
        .blockingIterable(entries);
```
## Uso del Ejemplo
Para ejecutar este ejemplo, crea una instancia de NewsAnalyzer y ejecuta el nodo news-analyzer. Asegúrate de tener una conexión a Internet para acceder a la API de WordPress.

```bash
curl -X POST http://localhost:8080/nodes/run-node/news-analyzer
```

## Resultados
Los resultados del análisis se emiten en el método analyzeData(). Cada resultado es una lista de mapas, donde cada mapa contiene la frecuencia de palabras para un post en particular. Puedes ajustar la lógica de emisión para guardar estos resultados en archivos JSON o realizar otras acciones según tus necesidades.

¡Esperamos que este ejemplo te ayude a comprender cómo puedes utilizar NexaStream para analizar datos en tiempo real provenientes de fuentes externas!

## Contribuciones

NexaStream es un proyecto de código abierto y estamos emocionados de recibir contribuciones de la comunidad. Si tienes ideas, correcciones o características que te gustaría aportar, por favor consulta nuestra [Guía de Contribución](CONTRIBUTION.md) para obtener más detalles sobre cómo colaborar.

¡Gracias por unirte a la comunidad de NexaStream!

[Website de NaT](https://www.nattechnologiesagency.com/) | [Documentación](WIKI.md)