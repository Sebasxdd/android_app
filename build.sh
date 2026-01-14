#!/bin/bash

# ==========================================
# Script de Construcción Local
# Autor: Martin Perez Lead DevSecOps      vSecOps
# ==========================================

# font para los mensajes
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Función para manejar errores
handle_error() {
    echo -e "${RED}❌ Error: El paso anterior falló. El script se detendrá.${NC}"
    exit 1
}

# Detener el script si hay errores
set -e
trap 'handle_error' ERR

echo -e "${YELLOW}🚀 Iniciando proceso de Build Local...${NC}"

# 1. Permisos
echo -e "${YELLOW}🔑 Asignando permisos de ejecución al Wrapper de Gradle...${NC}"
chmod +x gradlew

# 2. Limpieza
echo -e "${YELLOW}🧹 Limpiando artefactos previos...${NC}"
./gradlew clean

# 3. Análisis y Tests (Fail Fast: Primero calidad, luego compilación)
echo -e "${YELLOW}🧪 Ejecutando Tests Unitarios y Linting...${NC}"
# Ejecutamos lintDebug para ver problemas de código y testDebugUnitTest para lógica
./gradlew lintDebug testDebugUnitTest

# 4. Compilación
echo -e "${YELLOW}🔨 Compilando APK (Versión Debug)...${NC}"
# Usamos Debug localmente para no requerir secretos/keystore en la PC del desarrollador
./gradlew assembleDebug

# 5. Resultado
echo -e "${GREEN}✅ ¡ÉXITO! Build completado correctamente.${NC}"
echo -e "${GREEN}📦 Tu APK está listo en: app/build/outputs/apk/debug/app-debug.apk${NC}"
