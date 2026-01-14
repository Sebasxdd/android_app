# 🛡️ Secure Android CI/CD Pipeline

![Build Status](https://github.com/Sebasxdd/android_app/actions/workflows/android_build.yml/badge.svg)

Este proyecto implementa una arquitectura **DevSecOps** completa para aplicaciones Android. Utiliza prácticas de seguridad "Shift-Left", automatización de infraestructura con **GitHub Actions** y entrega continua mediante **Firebase App Distribution**.

## 🚀 Características del Pipeline

El flujo de trabajo automatizado (`android_build.yml`) cubre el ciclo de vida completo del software:

1.  **Integración Continua (CI):**
    * Compilación optimizada con Gradle Caching.
    * Ejecución de Tests Unitarios (`testDebugUnitTest`).
    * **Linting:** Análisis estático de calidad de código.
2.  **Seguridad (SAST & Scanning):**
    * **GitHub Advanced Security (GHAS):** CodeQL para detectar vulnerabilidades en el código.
    * **Secret Scanning:** Protección activa contra fugas de credenciales.
3.  **Entrega Continua (CD):**
    * **Firmado Seguro:** Inyección de Keystore en tiempo de ejecución (sin archivos físicos en el repo).
    * **Versionado Automático:** Generación de Tags de Git (`v1.0.X`) tras cada despliegue exitoso.
    * **Distribución:** Despliegue automático a **Firebase App Distribution** para QA/Testers.

---

## 🌿 Estrategia de Branching y Gobernanza

El proyecto sigue una estrategia de flujo de trabajo estricta para asegurar la estabilidad y seguridad del código:

### Rama Principal (`main`)
* **Estado:** Producción / Estable.
* **Protección:** Bloqueo de commits directos ("Direct Push denied").
* **Requisitos de Merge:**
    * Todo cambio debe venir mediante un **Pull Request (PR)**.
    * **Status Checks Obligatorios:** El pipeline de Build y el escaneo de seguridad (CodeQL) deben aprobarse (verde) antes de permitir la fusión.

### Ramas de Desarrollo (`feature/*`, `fix/*`)
* Entorno de trabajo para nuevas funcionalidades.
* Al finalizar, se crea un PR hacia `main` que dispara los tests automáticos.

---

## 🛠️ Setup Local y CI

### 1. Entorno Local (Developer Experience)
Para facilitar la incorporación de nuevos desarrolladores y evitar errores de comandos, se incluye un script de automatización.

**Prerrequisitos:**
* Java JDK 17
* Android Studio

**Ejecución:**
```bash
# Otorga permisos de ejecución al script helper
chmod +x build.sh

# Ejecuta el ciclo: Limpieza -> Tests -> Compilación (Debug)
./build.sh
