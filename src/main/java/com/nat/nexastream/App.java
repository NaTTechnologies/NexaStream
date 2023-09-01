package com.nat.nexastream;

import com.nat.nexastream.core.tasks.TaskExecutionContext;

/**
 * David Briones
 *
 */
public class App 
{

    public static void main(String[] args) {
        // Especifica el nombre del paquete donde se encuentran las tareas anotadas
        String packageName = "com.nat.nexastream.example"; // Reemplaza con el nombre correcto del paquete

        // Crea una instancia de TaskExecutionContext
        TaskExecutionContext context = new TaskExecutionContext(packageName);

        try {
            // Ejecuta una tarea por su nombre
            String taskName = "processTask"; // Reemplaza con el nombre de la tarea que deseas ejecutar
            context.executeTask(taskName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
