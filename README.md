# Android DevSecOps Pipeline

![Build Status](https://github.com/Sebasxdd/android_app/actions/workflows/ci-android.yml/badge.svg)

Repositorio oficial del proyecto Android con implementación completa de CI/CD, inyección de secretos, firmado seguro y distribución automática.

## 1. Estrategia de Branching
El proyecto utiliza un flujo de trabajo basado en **GitHub Flow** protegido:

* **`main` (Protected):** Rama de producción. No se permiten commits directos (Direct Push bloqueado).
* **`feature/*`:** Ramas de desarrollo para nuevas funcionalidades.
* **Pull Requests (PR):** Para fusionar código a `main`, es obligatorio abrir un PR que debe pasar exitosamente el pipeline de CI (Tests + Build) antes del merge.

## 2. Setup Local y CI

### Entorno CI (GitHub Actions)
El pipeline se define en `.github/workflows/ci-android.yml` y ejecuta los siguientes pasos:
1.  **Checkout & JDK Setup:** Configuración del entorno Java 17 con caché de Gradle.
2.  **Gestión de Dependencias:** Instalación explícita (`./gradlew dependencies`).
3.  **Tests:** Ejecución de pruebas unitarias (`./gradlew test`).
4.  **Seguridad:** Decodificación segura del Keystore en memoria.
5.  **Build:** Compilación de la variante Release con inyección de variables (Version, Commit Hash).
6.  **Distribución:** Carga de artefactos y despliegue a Firebase.

### Setup Local (Para Desarrolladores)
Para ejecutar el proyecto localmente sin errores de firmado:
```bash
# Dar permisos de ejecución
chmod +x gradlew

# Ejecutar tests unitarios
./gradlew test

# Compilar en modo Debug (No requiere secretos)
./gradlew assembleDebug