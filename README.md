# 🐾 PetConnect

### Plataforma móvil para el cuidado y la gestión de mascotas

**PetConnect** es una aplicación móvil nativa para Android que busca centralizar la información y los servicios relacionados con el cuidado de mascotas, facilitando a sus propietarios la búsqueda de establecimientos, la gestión de sus animales y la organización de sus necesidades.

El proyecto está orientado inicialmente a Medellín, Colombia.

## 🎯 Problema

Los propietarios de mascotas suelen encontrar información dispersa entre redes sociales, buscadores y recomendaciones personales cuando necesitan servicios veterinarios, peluquerías, guarderías u otras opciones de cuidado.

Esta situación dificulta la comparación de establecimientos y la identificación de servicios cercanos.

## 💡 Solución

PetConnect integra en una misma aplicación la gestión de mascotas, la exploración de establecimientos, los mapas interactivos, las direcciones personales y herramientas para organizar su cuidado.

La aplicación utiliza Firebase para autenticar usuarios y almacenar información, y MapLibre para la visualización de establecimientos en mapas.

## ✅ Funcionalidades implementadas

### Gestión de usuarios
- Registro e inicio de sesión.
- Consulta y edición de información personal.
- Roles de cliente y prestador.
- Avatar de perfil con selección temporal de fotografías.

### Gestión de mascotas
- Registro, consulta, actualización y eliminación de mascotas.
- Asociación de mascotas con su propietario.
- Fotografías temporales desde la galería.
- Carnet de vacunación individual.

### Carnet de vacunación
- Registro de vacunas en Cloud Firestore.
- Fecha de aplicación y próxima dosis opcional.
- Información de veterinaria y observaciones.
- Historial individual por mascota.
- Eliminación de registros con confirmación.

### Exploración de servicios
- Visualización de categorías.
- Consulta de establecimientos por categoría.
- Información detallada de negocios.
- Búsqueda y filtros.
- Servicios destacados con ordenamiento por calificación y cercanía.

### Mapas y geolocalización
- Mapa interactivo mediante MapLibre.
- Marcadores de establecimientos aprobados.
- Visualización de la ubicación del usuario.
- Búsqueda, filtros y ordenamiento de servicios.
- Navegación entre mapa y detalle de establecimientos.

### Direcciones personales
- Registro de direcciones desde un mapa.
- Gestión de direcciones propias.
- Selección y persistencia de la dirección activa.
- Cálculo de distancias desde la dirección seleccionada.

### Gestión de establecimientos
- Registro de negocios por prestadores.
- Consulta, actualización y eliminación de establecimientos propios.
- Estado de verificación.
- Reglas de acceso y seguridad mediante Firestore.

## 🛠️ Tecnologías utilizadas

- **Lenguaje:** Kotlin
- **Interfaz:** Jetpack Compose y Material Design 3
- **Arquitectura:** MVVM
- **Autenticación:** Firebase Authentication
- **Base de datos:** Cloud Firestore
- **Mapas:** MapLibre y OpenFreeMap
- **Entorno de desarrollo:** Android Studio
- **Control de versiones:** Git y GitHub

## 🚧 Funcionalidades en desarrollo

- Edición de vacunas registradas.
- Recordatorios individuales para mascotas.
- Indicadores automáticos de próximos cuidados.
- Persistencia de fotografías de perfil y mascotas.
- Búsqueda global desde Inicio.
- Gestión de favoritos.
- Recomendaciones personalizadas a partir de información real.

## 🔮 Mejoras futuras

- Recomendaciones avanzadas mediante inteligencia artificial.
- Notificaciones y recordatorios automáticos.
- Mejoras de experiencia de usuario y rendimiento.
- Ampliación progresiva de las funcionalidades para prestadores y propietarios.

## 📋 Gestión del proyecto

El desarrollo se organiza mediante GitHub Issues, historias de usuario, épicas y sprints.

Cada historia contiene funcionalidades específicas y criterios de aceptación que permiten verificar su cumplimiento.

## 📌 Estado del proyecto

**En desarrollo.** PetConnect cuenta con módulos funcionales de usuarios, mascotas, establecimientos, geolocalización, direcciones y vacunación. Algunas funcionalidades siguen pendientes y no deben considerarse finalizadas.
