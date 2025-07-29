# Universidad Politécnica Salesiana
* Facultad de Ingeniería
* Carrera de Ingeniería en Computación
* Materia: Estructura de Datos
* Autores: Erick Yunga, Brandon Rivera
* Correos institucionales:
* - erick.yunga@est.ups.edu.ec
* - brandon.rivera@est.ups.edu.ec

---
# Descripción del problema
### Se requiere desarrollar una aplicación de escritorio en Java que permita la resolución visual de laberintos mediante diferentes algoritmos de búsqueda. La aplicación debe permitir al usuario establecer el tamaño del laberinto, seleccionar el punto de inicio, final y obstáculos, y posteriormente ejecutar uno de los algoritmos para visualizar su funcionamiento, identificando los caminos explorados y el camino final encontrado, si existe.

---

#  Propuesta de solución
## Marco teórico 
### Algoritmos implementados:

* Recursivo: Algoritmo sencillo que explora en profundidad hasta encontrar una solución, sin garantizar el camino más corto.

* Recursivo Completo: Asegura que todas las rutas posibles se exploren hasta encontrar una solución, si existe.

* Recursivo Completo con Backtracking: Variante que explora todos los caminos posibles y retrocede inteligentemente cuando encuentra bloqueos.

* DFS (Depth-First Search): Búsqueda en profundidad. Explora lo más lejos posible en una dirección antes de retroceder. Eficiente pero no garantiza el camino más corto.

* BFS (Breadth-First Search): Búsqueda por anchura. Explora por niveles, garantizando siempre el camino más corto. Requiere más memoria que DFS.

---

# Tecnologías utilizadas
* Lenguaje de programación: Java 17
* Interfaz gráfica: Java Swing
* Arquitectura: MVC (Modelo-Vista-Controlador) y DAO
* Persistencia de datos: Archivos .csv
* IDE usado: IntelliJ IDEA
* Control de versiones: Git y GitHub

---

#  Diagrama UML 

---

# Conclusiones
## Erick Yunga
- #### Desde mi experiencia desarrollando y probando los distintos algoritmos de resolución de laberintos, considero que el algoritmo BFS (Breadth-First Search) es el más óptimo en términos de calidad de solución, ya que siempre encuentra el camino más corto si existe, lo cual es esencial en contextos donde la eficiencia del recorrido es crítica, como en navegación de robots o rutas de evacuación.

- #### Sin embargo, reconozco que su desventaja es el alto uso de memoria, especialmente en laberintos grandes. Aun así, en la mayoría de los casos prácticos, su precisión y confiabilidad justifican su elección.

## Brandon Rivera
- #### Después de implementar y observar el comportamiento de todos los algoritmos, considero que el algoritmo DFS (Depth-First Search) es el más práctico y eficiente para laberintos pequeños o con pocos obstáculos. Su ventaja principal es el bajo uso de memoria, ya que no necesita almacenar todos los caminos posibles, lo que lo vuelve ideal para sistemas con recursos limitados.

- #### No obstante, hay que tener en cuenta que no garantiza el camino más corto, por lo que su uso debe limitarse a contextos donde lo importante sea encontrar una salida, no necesariamente la más corta.

---

# Recomendaciones y aplicaciones futuras





