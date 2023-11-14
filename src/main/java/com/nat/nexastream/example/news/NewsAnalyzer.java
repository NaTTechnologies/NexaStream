package com.nat.nexastream.example.news;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.nat.nexastream.annotations.distribution.Node;
import com.nat.nexastream.annotations.distribution.*;
import com.nat.nexastream.example.news.model.Post;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Node(name = "news-analyzer")
public class NewsAnalyzer {
    public NewsAnalyzer() {
    }

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

    public int count = 0;

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
//                .subscribe(list -> {
//                    list.forEach(stringMapMap -> {
//                        System.out.println("SWITCH");
//
//                        stringMapMap.forEach((s, stringIntegerMap) -> {
//                            // Emite el resultado en forma de tabla
//                            for (Map.Entry<String, Integer> entry : stringIntegerMap.entrySet()) {
//                                String word = entry.getKey();
//                                int count = entry.getValue();
//
//                                // Palabra
//                                System.out.println(String.format("Palabra: %s, Frecuencia: %d", word, count));
//                            }
//                        });
//                    });
//
//                    count++;
//                    try {
//                        // Escribir el List<Map> en un archivo JSON
//                        objectMapper.writeValue(new File("data/file_" + count + ".json"), list);
//                        System.out.println("Datos guardados en " + "data/file_" + count + ".json");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
    }


}
