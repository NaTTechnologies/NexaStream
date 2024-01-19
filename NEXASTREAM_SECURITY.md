## NexaStream Security

NexaStream Security simplifica la seguridad de la ejecución de tareas distribuibles alineándose con nuestra filosofía central de eficiencia, flexibilidad, escalabilidad y simplicidad. A continuación, se destacan algunos puntos clave que resaltan cómo NexaStream Security puede facilitar y fortalecer la seguridad en la ejecución de tareas distribuibles:

1. **Eficiencia en la Implementación:**
   NexaStream Security se integra de manera eficiente en el flujo de ejecución de tareas distribuibles, proporcionando una capa adicional de seguridad sin comprometer el rendimiento. La implementación es sencilla y se ajusta a la filosofía de eficiencia de NexaStream.


2. **Flexibilidad en la Configuración:**
   La seguridad no es un enfoque único para todos, y NexaStream Security reconoce esto al ofrecer flexibilidad en la configuración. Puedes adaptar las medidas de seguridad según los requisitos específicos de tus tareas distribuibles, manteniendo el control sobre el entorno de ejecución.


3. **Escalabilidad sin Complicaciones:**
   Cuando tus aplicaciones y sistemas crecen, NexaStream Security escala contigo. Proporciona una capa de seguridad que puede ajustarse sin complicaciones a medida que tu infraestructura distribuida evoluciona.


4. **Simplicidad en la Gestión:**
   La simplicidad es fundamental en NexaStream Security. Facilita la gestión de la seguridad al proporcionar una integración fluida con las tareas distribuibles, sin requerir una curva de aprendizaje pronunciada. La gestión de claves, autorizaciones y autenticación se simplifica para que puedas concentrarte en el núcleo de tus aplicaciones.


5. **Protección Completa:**
   NexaStream Security no solo se trata de autenticación y autorización básicas. Ofrece un conjunto completo de herramientas para proteger tus tareas distribuibles, desde la comunicación segura hasta el control de accesos, brindando una capa robusta de seguridad integral.


6. **Adaptabilidad a Entornos Distribuidos:**
   Dado que NexaStream está diseñado para entornos distribuidos, NexaStream Security se adapta de manera natural a esta arquitectura. Proporciona una seguridad coherente y efectiva, independientemente de la complejidad de tu infraestructura distribuida.

## Configura NexaStream Security

Para acceder al usuario por defecto en NexaStream Security y obtener un token de acceso, es necesario seguir algunos pasos específicos y realizar ajustes en el archivo users.json. A continuación, se detallan los pasos necesarios:

1. Editar el archivo users.json:
   Abre el archivo users.json, que generalmente se encuentra en la configuración del proyecto. Aquí, deberás agregar los usuarios que se utilizarán para la autenticación básica.

Ejemplo de users.json:

```json
[
  {
    "username": "nexa",
    "password": "1234"
  }
]
```
2. Realizar la Autenticación Básica:
   Para obtener el token de acceso, realiza una solicitud HTTP POST a la siguiente URL, incluyendo la autenticación básica con el usuario y la contraseña que has configurado en el paso anterior:

```http
POST http://localhost:8761/oauth/token
Authorization: Basic <credenciales_en_base64>
Content-Type: application/x-www-form-urlencoded

grant_type=password&username=nexa&password=1234
```
**Nota:** Asegúrate de reemplazar <credenciales_en_base64> con las credenciales codificadas en Base64 del formato client_id:client_secret.

3. Recibir el Token de Acceso:
   Si la autenticación es exitosa, recibirás una respuesta que incluirá el token de acceso. Puedes utilizar este token en las cabeceras de tus solicitudes HTTP para autenticar y autorizar las operaciones.

Ejemplo de Respuesta Exitosa:

```json
{
"access_token": "tu_token_de_acceso",
"token_type": "Bearer",
"expires_in": 3600,
"scope": "read write"
}
```
4. Utilizar el Token de Acceso:
   Incluye el token de acceso en las cabeceras de tus solicitudes HTTP como autorización para acceder a los recursos protegidos por NexaStream Security.

Ejemplo de Cabecera de Solicitud:

```http
GET http://localhost:8761/remote/nodes
Authorization: Bearer tu_token_de_acceso
Siguiendo estos pasos, podrás configurar y utilizar usuarios por defecto para acceder mediante autenticación básica y obtener tokens de acceso en NexaStream Security. Recuerda mantener las credenciales de los usuarios seguras y ajustar la configuración según tus necesidades de seguridad.
```

## Contribuciones

NexaStream es un proyecto de código abierto y estamos emocionados de recibir contribuciones de la comunidad. Si tienes ideas, correcciones o características que te gustaría aportar, por favor consulta nuestra [Guía de Contribución](CONTRIBUTION.md) para obtener más detalles sobre cómo colaborar.

¡Gracias por unirte a la comunidad de NexaStream!

[Website de NaT](https://www.nattechnologiesagency.com/) | [Documentación](WIKI.md)