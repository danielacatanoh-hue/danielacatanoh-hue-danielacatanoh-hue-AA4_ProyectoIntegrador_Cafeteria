# EA4. Proyecto integrador — Calculadora de descuentos de la cafetería

Aplicación de escritorio en **Java** (sin frameworks externos) que gestiona los descuentos de los pedidos de una cafetería. Integra lo trabajado en las tres unidades del curso:

| Unidad | Qué se aplica | Dónde está |
|--------|---------------|------------|
| I. Diseño y estructura | Herencia y polimorfismo (`Producto` → `Bebida`, `Comida`), SOLID (abierto/cerrado), bajo acoplamiento | `modelo/`, `descuento/CalculadoraDescuento.java`, `documentacion/diagrama_clases.png` |
| II. Lógica y versiones | Interfaz funcional `ReglaDescuento` y expresión lambda del descuento; repositorio Git | `descuento/ReglaDescuento.java`, `descuento/ReglasDescuento.java` |
| III. Interfaz y persistencia | Swing (`JFrame`, `JPanel`, `JTextField`, `JButton`, `JTextArea`), JDBC con SQLite, try-with-resources, `SQLException` y `ClassNotFoundException` | `vista/`, `persistencia/` |

**Asignatura:** Programación Orientada a Objetos II · **Docente:** Leonardo Montes Gutiérrez · **Institución Universitaria Digital de Antioquia** · 2026

## Regla de descuento

Si el total del pedido es **mayor a $10**, se aplica un **15 %** de descuento; si no, el 0 %:

```java
public static final ReglaDescuento POR_MONTO =
        total -> total > MONTO_MINIMO ? total * PORCENTAJE : 0.0;
```

Para agregar otra regla no se modifica `CalculadoraDescuento`: basta con pasarle otra lambda en `App.java`, por ejemplo `ReglasDescuento.porMontoMinimo(50, 0.20)`.

## Ejecutar

Requiere JDK 17 o superior.

- **Windows:** doble clic en `ejecutar.bat` (aplicación) o en `ejecutar_pruebas.bat` (pruebas de la lógica).

Comandos manuales en Windows, desde la carpeta del proyecto:

```bat
javac -encoding UTF-8 -d out src\com\cafeteria\*.java src\com\cafeteria\modelo\*.java src\com\cafeteria\descuento\*.java src\com\cafeteria\persistencia\*.java src\com\cafeteria\servicio\*.java src\com\cafeteria\vista\*.java
java -cp "out;lib\sqlite-jdbc-3.46.1.3.jar" com.cafeteria.App
```

Al iniciar, la aplicación se conecta a `cafeteria.db` (se crea sola) y muestra el resultado de la conexión en el área de texto. Cada pedido calculado se guarda en la tabla `pedidos`.

### Probar los errores de conexión

| Prueba | Comando | Resultado |
|--------|---------|-----------|
| Falta el driver (`ClassNotFoundException`) | `java -cp out com.cafeteria.App` | Mensaje de error del driver; la calculadora sigue funcionando |
| Ruta inválida (`SQLException`) | `java -Dbd.url=jdbc:sqlite:C:/carpeta_inexistente/cafeteria.db -cp "out;lib\sqlite-jdbc-3.46.1.3.jar" com.cafeteria.App` | Mensaje de error de conexión |

## Estructura

```
AA4_ProyectoIntegrador_Cafeteria/
├── src/com/cafeteria/
│   ├── App.java                         # crea y conecta los objetos; abre la ventana
│   ├── modelo/                          # Producto (abstracta), Bebida, Comida, Menu
│   ├── descuento/                       # ReglaDescuento (@FunctionalInterface), ReglasDescuento (lambdas),
│   │                                    # CalculadoraDescuento, ResultadoDescuento
│   ├── persistencia/                    # ConexionBD, PedidoRepositorio (JDBC, try-with-resources)
│   ├── servicio/                        # ServicioCafeteria (une modelo y persistencia)
│   └── vista/                           # VentanaCafeteria (Swing)
├── test/com/cafeteria/PruebasCafeteria.java   # 11 pruebas sin frameworks
├── lib/sqlite-jdbc-3.46.1.3.jar         # driver JDBC de SQLite
├── documentacion/                       # diagrama de clases (PNG y PPTX editable)
├── evidencias/                          # capturas de ejecución y salida de las pruebas
├── informe/                             # informe del grupo (PDF)
├── codigo_fuente.csv                    # código fuente en formato CSV (archivo; línea; código)
└── ejecutar.bat / ejecutar_pruebas.bat / ejecutar.sh
```

## Historial de commits

El repositorio se construyó en estas etapas (cada una es un commit):

1. `Estructura del proyecto y modelo POO: Producto, Bebida y Comida`
2. `Lógica de descuento con interfaz funcional y expresión lambda`
3. `Conexión JDBC a SQLite con try-with-resources`
4. `Interfaz gráfica Swing y evento Calcular Descuento`
5. `Informe, evidencias, diagrama UML y código en CSV`


