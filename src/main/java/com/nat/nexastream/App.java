package com.nat.nexastream;

import com.nat.nexastream.core.tasks.TaskAssignmentManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App implements CommandLineRunner{

    public static void main(String[] args) throws IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        SpringApplication.run(App.class, args);

        // Crea una instancia de TaskExecutionContext
//        TaskExecutionContext context = new TaskExecutionContext(packageName);
//
//        try {
//            // Ejecuta una tarea por su nombre
//            String taskName = "processTask"; // Reemplaza con el nombre de la tarea que deseas ejecutar
//            context.executeTask(taskName);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // Crea un TaskAssignmentManager
//        TaskAssignmentManager taskAssignmentManager = new TaskAssignmentManager(packageName, numNodes);
//
//        // Ejecuta los nodos en paralelo
//        for (int i = 0; i < numNodes; i++) {
//            new Thread(() -> taskAssignmentManager.runNode()).start();
//        }
//
//        // Envía tareas al TaskAssignmentManager con sus respectivas prioridades
//        taskAssignmentManager.submitTask("processTask", TaskPriority.HIGH);
//        taskAssignmentManager.submitTask("dataFetcher", TaskPriority.NORMAL);

        // Puedes enviar más tareas según sea necesario

        // Espera un tiempo suficiente para que las tareas se ejecuten
//        try {
//            Thread.sleep(5000); // Espera 5 segundos (ajusta según sea necesario)
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }

        // Apaga el TaskAssignmentManager
//        taskAssignmentManager.shutdown();
        // Especifica el nombre del paquete donde se encuentran las tareas anotadas

        //taskAssignmentManager.shutdown();
    }

    @Override
    public void run(String... args) throws Exception {
//        String packageName = "com.nat.nexastream.example.other"; // Reemplaza con el nombre correcto del paquete
//
//        TaskAssignmentManager taskAssignmentManager = new TaskAssignmentManager(packageName);
//        taskAssignmentManager.runNode("exampleNode");
//        taskAssignmentManager.runNode("otherNode");
    }
}
