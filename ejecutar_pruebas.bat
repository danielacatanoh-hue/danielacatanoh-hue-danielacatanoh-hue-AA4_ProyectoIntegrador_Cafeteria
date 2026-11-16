@echo off
rem Compila y ejecuta las pruebas de la logica (Windows).
chcp 65001 > nul
cd /d "%~dp0"
if exist out rmdir /s /q out
mkdir out
javac -encoding UTF-8 -d out src\com\cafeteria\*.java src\com\cafeteria\modelo\*.java src\com\cafeteria\descuento\*.java src\com\cafeteria\persistencia\*.java src\com\cafeteria\servicio\*.java src\com\cafeteria\vista\*.java test\com\cafeteria\*.java
if errorlevel 1 (
  pause
  exit /b 1
)
java --enable-native-access=ALL-UNNAMED -cp "out;lib\sqlite-jdbc-3.46.1.3.jar" com.cafeteria.PruebasCafeteria
pause
