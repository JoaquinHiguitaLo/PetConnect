# 🐾 PetConnect

### Plataforma móvil para el cuidado y la gestión de mascotas

**PetConnect** es una aplicación móvil nativa para Android diseñada para centralizar la información y los servicios relacionados con el cuidado de mascotas, facilitando a sus propietarios la búsqueda de establecimientos, la administración de sus animales y la organización de sus necesidades.

La aplicación permite explorar servicios cercanos, consultar negocios, administrar perfiles de mascotas, gestionar información de vacunación y guardar direcciones personales.

El proyecto está orientado inicialmente a **Medellín, Colombia**.

## 🎯 Problema

Los propietarios de mascotas suelen encontrar información dispersa entre redes sociales, buscadores y recomendaciones personales cuando necesitan servicios veterinarios, peluquerías, guarderías u otras opciones de cuidado.

Esta situación genera dificultades para comparar establecimientos, identificar servicios cercanos y encontrar información confiable en un mismo lugar.

Adicionalmente, la organización de información importante sobre las mascotas, como sus datos personales y registros de vacunación, puede resultar complicada cuando se utilizan diferentes medios para almacenarla.

## 💡 Solución

PetConnect centraliza en una misma aplicación la búsqueda y exploración de servicios, la gestión de mascotas y el acceso a herramientas relacionadas con su cuidado.

La plataforma integra mapas interactivos, geolocalización, información de establecimientos, direcciones personales y funcionalidades de administración de mascotas.

Para su funcionamiento utiliza **Firebase Authentication** en la gestión de usuarios, **Cloud Firestore** para almacenar información y **MapLibre con OpenFreeMap** para visualizar establecimientos en mapas.

El proyecto también incluye una interfaz inicial para futuras recomendaciones personalizadas mediante inteligencia artificial.

---

## ✅ Funcionalidades implementadas

### 👤 Gestión de usuarios

- Registro e inicio de sesión mediante Firebase Authentication.
- Consulta y actualización de información personal.
- Manejo de roles de cliente y prestador de servicios.
- Perfil de usuario con acceso a las distintas funcionalidades.
- Selección temporal de fotografías de perfil.
- Interfaz de perfil organizada en secciones de acceso rápido.

### 🐶 Gestión de mascotas

- Registro de mascotas asociadas al usuario autenticado.
- Consulta de mascotas registradas.
- Actualización de información individual.
- Eliminación de mascotas.
- Acceso al perfil detallado de cada mascota.
- Visualización de información como nombre, especie, raza, edad y otros datos disponibles.
- Selección temporal de fotografías desde la galería.
- Organización del perfil mediante módulos de cuidado y seguimiento.

### 💉 Carnet de vacunación

- Registro de vacunas en Cloud Firestore.
- Asociación de cada vacuna con una mascota.
- Registro de fecha de aplicación.
- Registro opcional de próxima dosis.
- Información de veterinaria y observaciones.
- Consulta del historial individual de vacunación.
- Eliminación de registros con confirmación.
- Interfaz visual unificada con el diseño de PetConnect.

### 🏥 Exploración de servicios

- Visualización de categorías de servicios para mascotas.
- Consulta de establecimientos aprobados por categoría.
- Búsqueda de negocios desde la pantalla Inicio.
- Búsqueda por nombre, categoría y descripción.
- Resultados obtenidos desde Cloud Firestore.
- Tarjetas de resultados con iconos representativos de cada categoría.
- Información detallada de establecimientos.
- Visualización de dirección, teléfono, descripción y calificación.
- Servicios destacados ordenados por calificación.
- Cálculo de distancias desde la dirección activa.
- Navegación desde las tarjetas hacia el detalle del establecimiento.

**Categorías disponibles:**

- Veterinaria.
- Peluquería y estética.
- Baño.
- Guardería.
- Paseadores.
- Adiestramiento.
- Parques y jardines.
- Tiendas.
- Especialistas.
- Hoteles Pet Friendly.

### ⭐ Servicios y especialistas destacados

- Consulta de establecimientos aprobados en Firestore.
- Visualización de servicios destacados en Inicio.
- Presentación de calificaciones y distancias.
- Sección de especialistas destacados.
- Filtrado de establecimientos de la categoría Especialistas.
- Navegación hacia el detalle de los negocios.
- Tarjetas visuales integradas con la identidad de PetConnect.

### 🗺️ Mapas y geolocalización

- Mapa interactivo mediante MapLibre.
- Cartografía proporcionada por OpenFreeMap.
- Visualización de marcadores de establecimientos aprobados.
- Consulta de servicios directamente desde el mapa.
- Filtros horizontales por categorías.
- Búsqueda de establecimientos en el mapa.
- Ordenamiento por cercanía y calificación.
- Obtención de la ubicación actual del usuario.
- Solicitud y gestión de permisos de ubicación.
- Compatibilidad de geolocalización comprobada en dispositivos físicos.
- Obtención de ubicación mediante GPS y proveedores de red disponibles.
- Centrado del mapa en la ubicación obtenida.
- Navegación desde los marcadores y tarjetas hacia el detalle de los negocios.

### 📍 Direcciones personales

- Registro de direcciones mediante selección de coordenadas en un mapa.
- Consulta de direcciones asociadas al usuario.
- Selección de una dirección activa.
- Persistencia de la dirección seleccionada.
- Visualización de la dirección activa en Inicio.
- Cálculo de distancias entre la dirección seleccionada y los establecimientos.
- Interfaz unificada para la gestión y el registro de direcciones.

### 🏪 Gestión de establecimientos

- Registro de negocios por usuarios con rol de prestador.
- Consulta de establecimientos propios.
- Actualización de información del negocio.
- Eliminación de establecimientos.
- Estado de verificación de negocios.
- Visualización de establecimientos aprobados para los clientes.
- Gestión de información como nombre, categoría, descripción, dirección, teléfono y coordenadas.
- Integración con Cloud Firestore.
- Aplicación de reglas de acceso y seguridad en Firestore.
- Interfaz de administración adaptada al diseño de PetConnect.

### 🤖 Asistente de recomendaciones IA — Prototipo

Se desarrolló una interfaz inicial que permitirá incorporar recomendaciones personalizadas en futuras versiones.

**Funcionalidades actuales:**

- Pantalla de asistente con diseño visual de PetConnect.
- Consulta de mascotas reales del usuario desde Firebase.
- Carrusel horizontal de selección de mascotas.
- Selección individual de una mascota.
- Actualización del encabezado de recomendaciones según la mascota seleccionada.
- Carrusel interactivo para seleccionar el tipo de servicio.
- Tarjetas demostrativas de recomendaciones.
- Desplazamiento vertical para consultar el contenido.

**Estado actual:** las recomendaciones todavía utilizan información ficticia y no corresponden a resultados generados por un modelo de inteligencia artificial.

La personalización automática a partir de características reales de las mascotas está prevista para una segunda versión.

### 🎨 Interfaz y experiencia de usuario

- Interfaz desarrollada con Jetpack Compose y Material Design 3.
- Identidad visual basada en verde turquesa, beige y blanco.
- Componentes reutilizables para encabezados, títulos y formularios.
- Navegación inferior con iconografía minimalista.
- Tarjetas con bordes redondeados y sombras suaves.
- Carruseles horizontales de categorías y servicios.
- Desplazamiento vertical en las pantallas principales.
- Interfaz adaptable a distintos tamaños de dispositivos Android.
- Diseño visual consistente en los módulos principales.

### 🚀 Inicio e identidad visual

- Splash Screen nativo mediante AndroidX SplashScreen.
- Pantalla de bienvenida personalizada con Jetpack Compose.
- Logo de PetConnect acompañado del nombre de la aplicación.
- Fondo de bienvenida integrado con la identidad visual.
- Icono launcher personalizado.
- Continuidad hacia el flujo de navegación y autenticación.

---

## 🛠️ Tecnologías utilizadas

| Componente | Tecnología |
|---|---|
| Lenguaje de programación | Kotlin |
| Desarrollo móvil | Android SDK |
| Interfaz de usuario | Jetpack Compose |
| Sistema de diseño | Material Design 3 |
| Arquitectura | MVVM |
| Autenticación | Firebase Authentication |
| Base de datos | Cloud Firestore |
| Mapas | MapLibre |
| Cartografía | OpenFreeMap |
| Geolocalización | Android LocationManager |
| Operaciones asíncronas | Kotlin Coroutines |
| Gestión de estado | StateFlow |
| Splash Screen | AndroidX SplashScreen |
| Entorno de desarrollo | Android Studio |
| Control de versiones | Git y GitHub |

## 🏗️ Arquitectura del proyecto

PetConnect utiliza una arquitectura basada en el patrón **Model–View–ViewModel (MVVM)**, que separa las responsabilidades de la aplicación.

**Model**

Define las estructuras de datos utilizadas para representar usuarios, mascotas, establecimientos, vacunas y direcciones.

**View**

Contiene las pantallas y componentes desarrollados mediante Jetpack Compose.

**ViewModel**

Administra el estado de las interfaces y coordina las operaciones de cada módulo.

**Repository**

Centraliza el acceso a los datos almacenados en Firebase y las operaciones relacionadas con los modelos.

Esta organización facilita el mantenimiento, la reutilización de componentes y la ampliación progresiva del proyecto.

---

## 🚧 Funcionalidades pendientes o en desarrollo

### Gestión de mascotas y cuidados

- Edición de vacunas registradas, si aún no está completada.
- Recordatorios individuales.
- Gestión funcional de citas, medicamentos y otros cuidados.
- Indicadores de próximas vacunas y actividades.
- Persistencia de fotografías de perfil y mascotas.

### Establecimientos y usuarios

- Guardar y eliminar establecimientos favoritos en Firestore.
- Visualización de negocios guardados en **Perfil → Mis favoritos**.
- Conexión del acceso «Ver más» en Especialistas destacados.
- Ampliación de funcionalidades para prestadores.
- Mejoras adicionales de validación y experiencia de usuario.

### Inteligencia artificial

- Recomendaciones reales basadas en las características de las mascotas.
- Integración de información real de establecimientos en los resultados.
- Evaluación de criterios de recomendación.
- Sustitución de los resultados demostrativos.
- Implementación de funcionalidades avanzadas en una segunda versión.

---

## 🔮 Mejoras futuras

- Sistema de notificaciones y recordatorios automáticos.
- Seguimiento de citas y actividades relacionadas con las mascotas.
- Recomendaciones avanzadas mediante inteligencia artificial.
- Ampliación de categorías y cobertura geográfica.
- Mejoras de rendimiento y accesibilidad.
- Optimización de la experiencia de usuario en dispositivos físicos.
- Nuevas herramientas para prestadores y propietarios.

---

## 📋 Gestión del proyecto

El desarrollo de PetConnect se organiza mediante **GitHub Issues, historias de usuario, épicas y sprints**.

Cada historia de usuario representa una funcionalidad específica y cuenta con criterios de aceptación que permiten comprobar su implementación.

El proyecto se desarrolla de manera incremental, priorizando primero las funcionalidades esenciales y posteriormente las características más avanzadas.

## 📌 Estado del proyecto

**Estado actual: En desarrollo — versión funcional inicial.**

PetConnect cuenta con módulos funcionales de autenticación, gestión de mascotas, vacunación, exploración de establecimientos, búsqueda, geolocalización, direcciones y administración de negocios.

También dispone de una interfaz de asistente de recomendaciones y de una identidad visual unificada.

Las funciones de inteligencia artificial avanzada, favoritos persistentes, notificaciones y otras herramientas de cuidado permanecen en desarrollo o están planificadas para próximas versiones.

El proyecto **no debe considerarse finalizado**.

---

**PetConnect — Conectando el bienestar de tu mascota.** 🐾
