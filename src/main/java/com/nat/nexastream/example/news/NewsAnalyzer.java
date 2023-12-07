package com.nat.nexastream.example.rrss;

import com.nat.nexastream.annotations.distribution.Node;
import com.nat.nexastream.annotations.distribution.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;

@Node(name = "news-analyzer")
public class NewsAnalyzer {

    @InjectDependency(name = "data")
    public Observable<String> data;

    @DataDependency(dataKey = "data")
    public Observable<String> getData() {
        // Crear un cliente OkHttp para hacer la solicitud a la API de WordPress
        OkHttpClient client = new OkHttpClient();

        // URL base de la API de WordPress
        String apiUrl = "https://roatanhableclaro.com/wp-json/wp/v2/posts";

        return Observable.create(emitter -> {
            String nextPageUrl = apiUrl;
            boolean hasMoreData = true;

            while (hasMoreData) {
                try {
                    // Crear una solicitud GET
                    Request request = new Request.Builder()
                            .url(nextPageUrl)
                            .build();

                    // Realizar la solicitud
                    Response response = client.newCall(request).execute();

                    // Verificar si la solicitud fue exitosa
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        System.out.println(responseBody);
                        emitter.onNext(responseBody); // Emitir los datos obtenidos

                        // Verificar si hay más paginas disponibles en la respuesta
                        String linkHeader = response.header("Link");
                        if (linkHeader != null && linkHeader.contains("rel=\"next\"")) {
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

            emitter.onCompleted(); // Marcar la finalización del flujo de datos
        });
    }

    @DistributableTask
    public void analyzeData(TaskExecutionContext context) {
        // Obtiene el contenido de texto del campo "content" de los datos
        String content = "Los goles de Nathan Aké, Phil Foden y Erling Haaland, que ya suma 35 dianas en una sola temporada, más que nadie en la historia de la Premier League, devuelven al Manchester City al liderato de la liga tras el triunfo ante el West Ham United (3-0).";

        // Realiza el procesamiento del contenido para contar las palabras
        String[] words = content.split("\\s+"); // Dividir el texto en palabras
        Map<String, Integer> wordCount = new HashMap<>();

        for (String word : words) {
            // Elimina signos de puntuación y convierte a minúsculas
            word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();

            // Actualiza el conteo de palabras
            if (!word.isEmpty()) {
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            }
        }

        // Emite el resultado en forma de tabla
        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            String word = entry.getKey();
            int count = entry.getValue();
            context.log(String.format("Palabra: %s, Frecuencia: %d", word, count));
        }
    }


}
