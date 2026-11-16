#!/bin/sh
# Compila y ejecuta la aplicación (macOS / Linux). Requiere JDK 17 o superior.
cd "$(dirname "$0")"
rm -rf out && mkdir out
javac -encoding UTF-8 -d out $(find src -name '*.java') || exit 1
java --enable-native-access=ALL-UNNAMED -cp "out:lib/sqlite-jdbc-3.46.1.3.jar" com.cafeteria.App
