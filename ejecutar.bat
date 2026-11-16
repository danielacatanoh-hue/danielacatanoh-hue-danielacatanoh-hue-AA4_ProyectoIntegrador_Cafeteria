@echo off
rem Compila y ejecuta la aplicacion (Windows). Requiere JDK 17 o superior.
chcp 65001 > nul
cd /d "%~dp0"
if exist out rmdir /s /q out
mkdir out
javac -encoding UTF-8 -d out src\com\cafeteria\*.java src\com\cafeteria\modelo\*.java src\com\cafeteria\descuento\*.java src\com\cafeteria\persistencia\*.java src\com\cafeteria\servicio\*.java src\com\cafeteria\vista\*.java
if errorlevel 1 (
  echo Error al compilar. Verifique que tiene instalado el JDK.
  pause
  exit /b 1
)
java --enable-native-access=ALL-UNNAMED -cp "out;lib\sqlite-jdbc-3.46.1.3.jar" com.cafeteria.App
