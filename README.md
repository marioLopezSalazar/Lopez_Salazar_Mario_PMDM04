# SpyroTheDragon

## INTRODUCCIÓN

**Spyro The Dragon** es una aplicación que permite **adentrarse en el universo mágico de Spyro**, pudiendo conocer algunos de sus elementos principales: Personajes, mundos y coleccionables. Está desarrollada en Java para dispositivos Android, y presenta una interfaz muy visual, intuitiva y de ágil navegación.

Se trata de una aplicación de carácter académico desarrollada para el módulo ***"Programación Multimedia y Dispositivos Móviles"*** dentro del *CFGM Dessarrollo de Aplicaciones Multiplataforma* a distancia en el IES Aguadulce (Almería, España).

## CARACTERÍSTICAS PRINCIPALES

Las funcionalidades clave de la aplicación son las sigiuentes:

* **Pestañas con información del universo de Spyro The Dragon**: Ofrece un listado de los personajes principales, algunos de los mundos que lo componen, y los tipos de objetos coleccionables que pueden hallarse a lo largo de estos mundos.
  
  ![Tabs](https://github.com/user-attachments/assets/7cbbb429-f7e1-4ddc-b86f-64b2e5536dbf)

* **Guía interactiva**: Incluye un recorrido visual explicativo por las diferentes secciones de la aplicación, marcando e indicando la información que puede hallarse en cada opción. La guía se visualiza de manera predeterminada en la primera ejecución de la app, aunque también puede volver a visualizarse a través del botón habilitado al efecto en el menú principal.

  ![Guide](https://github.com/user-attachments/assets/705c4d5e-8181-4d13-8739-79ed29415c80)

* **Easter Egg "Bola de fuego"**: En la pestaña de Personajes, al hacer una pulsación larga sobre Spyro, se obtiene una animación del dragón protagonista lanzando fuego por la boca.

  ![Fire](https://github.com/user-attachments/assets/81b2da35-dbfb-4eb9-be4b-dc872fd448f9)

* **Easter Egg "Vídeo"**: Al realizar 4 pulsaciones consecutivas sobre el coleccionable Gemas, aparece por sorpresa el clásico vídeo introductivo de uno de los primeros videojuegos de Spyro.

  ![Video](https://github.com/user-attachments/assets/977f5f8a-bee8-4eef-a388-a21c16f17b72)

* **Acerca de...**: Informa sobre la versión de la APP y su desarrollador.
  
  ![About](https://github.com/user-attachments/assets/b4bd7531-8cf2-41bf-8859-847b6a57270d)
  

## TECNOLOGÍAS UTILIZADAS

* **Android Navigation**: Componente que facilita la implementación de la navegación en aplicaciones Android. Permite mover a los usuarios entre pantallas (en este caso, *fragments*) y gestionar elementos relacionados con la navegación, como el manejo de la pila de retroceso. La aplicación dispone de dos controladores de navegación: Uno para las pestañas de la aplicación, y otro para la navegación por las pantallas de la guía.
* **BottomNavigationView**: Componente de interfaz de usuario que facilita la navegación entre diferentes secciones utilizando una barra de navegación en la parte inferior de la pantalla. Proporciona un acceso rápido y consistente a cada sección. Este componente se integra fácilmente con Android Navigation.
* **RecyclerView**: Componente de interfaz de usuario utilizado para mostrar grandes conjuntos de datos de forma eficiente, en este caso en forma de lista. Ofrece flexibilidad y rendimiento al reutilizar vistas mediante un patrón llamado "ViewHolder". RecyclerView permite personalizar fácilmente la presentación de los elementos mediante LayoutManagers (en este caso CardViews). Su diseño modular lo hace ideal para listas complejas y dinámicas, con soporte nativo para arrastrar y soltar, y también para deslizamiento, tal y como se emplea en esta aplicación.
* **SharedPreferences**: API Android que permite almacenar y recuperar datos clave-valor de forma persistente, ideal para guardar configuraciones o preferencias de usuario. En esta aplicación se emplea para almacenar si el usuario ya ha visualizado la guía en algún momento previo.
* **Drawable**: Es cualquier recurso gráfico que puede mostrarse en la interfaz de usuario. Puede ser una imagen, una forma definida en XML, un selector de estados o incluso gráficos generados dinámicamente mediante código. La aplicación los utiliza de manera profusa en guía interactiva (imágenes, fondos...).
* **Transitions**: Efectos visuales que se aplican a un elemento (aparición, desvanecimiento, transformación, etc.). En la app, concretamente se utilizan transiciones al entrar o salir en cada una de las páginas de la guía interactiva.
* **Animators**: Clase empleada crear animaciones sofisticadas en la interfaz de usuario. Permite modificar propiedades de vistas a lo largo del tiempo, proporcionando efectos que pueden personalizarse con todo detalle. En la guía interactiva y en el Easter Egg "Bola de fuego" se emplean para implementar animaciones en la forma, transparencia, tamaño o posición de los objetos.
* **SoundPool**: Clase que permite reproducir pequeños fragmentos de sonido, que aportan a la aplicación un efecto inmersivo mágico.
* **VideoView**: Elemento visual que permite introducir, dentro de un layout, la reproducción de un vídeo.
* **MediaController**: Clase empleada en la aplicación para proporcionar al usuario controles durante la visualización del vídeo del Easter Egg "Bola de fuego".
* **Canvas**: Clase que proporciona una superficie para dibujar gráficos. En la aplicación, se apoya de las clases **Paint** (pincel), **Path** (camino, trazado cons segmentos y curvas construidos a partir de coordenadas de puntos) o **RadialGradient** (coloreado mediante degradado radial). En cualquier caso, las posibilidades son prácticamente infinitas, aunque el trabajo de diseño e implementación manual puede resultar árduo.

## INSTRUCCIONES DE USO

1. En la [página principal del repositorio](https://github.com/marioLopezSalazar/Lopez_Salazar_Mario_PMDM04/), hacer clic en el botón verde `<> code` y copiar la dirección HTTPS que aparece (`https://github.com/marioLopezSalazar/Lopez_Salazar_Mario_PMDM04.git`).
2. En Android Studio, utilizamos el menú: *File -> New... -> Project from Version Control...*.
3. En el diálogo emergente, seleccionaremos:
    * *Repository URL*
    * *Version control: Git*
    * *URL:* En este campo pegaremos la dirección previamente copiada.
    * *Directory:* Directorio donde se descargará el proyecto.
    * Haremos clic en el botón *Clone*.
4. En unos segundos, tendremos el proyecto descargado en nuestro equipo.

Los archivos `build.gradle.kts` (principalmente el de nivel de App) ya contienen referencias a las librerías o dependencias que necesita la aplicación para funcionar. Así, en el proceso de construcción (*Build*) del proyecto, el propio Android Studio obtendrá de manera transparente las librerías necesarias. No obstante, también puede lanzarse este proceso mediante la opción *File -> Sync Project with Gradle Files*.

Para la ejecución de la aplicación es necesario disponer de un dispositivo emulador (puede usarse el *Device Manager* que provee Android Studio) con API mínima 31. Basta seleccionar el emulador deseado y utilizar la opción *Run -> Run 'App'*.


## CONCLUSIONES DEL DESARROLLADOR

En este caso, el desarrollo partía de una [versión base de la aplicación](https://github.com/lbarmar/SpyroTheDragon), con las pestañas de navegación totalmente funcionales. El desarrollo ha consistido en el diseño e implementación de los diferentes elementos multimedia.

#### Guía interactiva 
Ha resultado la parte del desarrollo que más trabajo ha requerido, principalmente para conseguir que los diferentes layouts y animaciones se comportasen de manera *responsive* ante diferentes tamaños y orientación de pantalla.

![Guide_info](https://github.com/user-attachments/assets/e33a12d2-c085-4052-9ec1-654beba0608b)

En este sentido, las animaciones de los diferentes `Drawable` tienen en cuenta la posición física de los mismos en términos de píxeles (obtenida en tiempo de ejecución), siendo necesario controlar el completo inflado de la interfaz antes de obtener las coordenadas, empleadas en último término para configurar las animaciones.

Por otra parte, la distribución de los diferentes *paneles* que componen la guía y su adecuación a la orientación *portrait*/*landscape* ha requerido, finalmente, emplear ficheros de *layout* específicos para cada orientación, si bien los elementos visuales que aparecen en ambas versiones son los mismos.

![GuideLand](https://github.com/user-attachments/assets/42c1e744-4817-45ff-b754-070cf50dad53)

Se ha optado por implementar la guía en base a un grafo de navegación, siendo cada página de la guía un `Fragment` distinto. Esto ha facilitado la correcta integración con otros elementos, como por ejemplo el uso del botón *back* para retroceder en la guía, o el mantenimiento en la misma página aunque la aplicación pase por segundo plano o la pantalla gire.

La guía está desarrollada en base a varios `Drawable`, efectos y animaciones, de elaboración *ex-profeso*, habiendo conseguido un resultado bastante integrado y robusto.

#### Easter Egg "Bola de fuego"

En este caso el reto ha estado en tener la paciencia suficiente para elaborar un `Path` con forma de llamarada de dragón. Por falta de tiempo, el diseño final ha resultado básico aunque (considero) suficiente. 

Ha resultado una simbiosis técnico-crativa el ajuste de los parámetros de los distintos objetos Java involucrados en la animación, ajustando los valores hasta la décima para conseguir un efecto visual uniforme y convincente.

Como en el caso anterior, se han utilizado las coordenadas (en términos de píxeles en tiempo de ejecución) de la imagen de Spyro, para así calcular dónde dibujar la llama y con qué tamaño, lo que hace a la animación independiente del dispositivo y la orientación. Como aspecto interesante, destacar que, al estar la imagen incursa en ScrollView+RecyclerView, ha sido necesario tener en consideración el tamaño de la Barra del Sistema y la Barra de Navegación (externas a la app).


#### Easter Egg "Vídeo"

La introducción del vídeo en la app es un proceso sencillo. Algo más complicado ha resultado el mantener viva la reproducción ante un cambio de orientación o un paso a segundo plano. No obstante, este aspecto ha permitido profundizar en el funcionamiento de los métodos del ciclo de vida de la `Activity`, y qué sucede con la misma cuando se producen los eventos antes indicados.


#### Documentación
Finalmente, el proceso de documentación JavaDoc de todas las clases implementadas ha permitido una revisión general del código, refinando aspectos de modularidad e implementación, así como una profundización en las llamadas y métodos de las librerías empleadas.
