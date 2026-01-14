# 🛡️ Secure Android CI/CD Pipeline

Este proyecto demuestra la implementación de un pipeline **DevSecOps** completo para Android, utilizando GitHub Actions para la integración continua y Firebase App Distribution para el despliegue continuo (CD).

## 🚀 Características del Pipeline

El flujo de trabajo automatizado (`android_build.yml`) cubre las siguientes etapas:

1.  **Integración Continua (CI):**
    * Compilación automática con Gradle Wrapper (Caching habilitado).
    * Ejecución de Pruebas Unitarias (`testDebugUnitTest`).
    * Análisis estático de código (Linting).
2.  **Seguridad (DevSecOps):**
    * **CodeQL:** Escaneo de vulnerabilidades en el código fuente.
    * **Secret Scanning:** Detección proactiva de credenciales expuestas.
    * **Gestión de Secretos:** Keystore y API Keys inyectados de forma segura vía GitHub Secrets (Base64).
3.  **Entrega Continua (CD):**
    * **Firmado:** Generación de APK `Release` firmado digitalmente.
    * **Versionado:** Creación automática de Tags en Git (v1.0.X) tras cada despliegue exitoso.
    * **Despliegue:** Subida automática a **Firebase App Distribution** para testers.
    * **Artefactos:** Disponibles en la pestaña "Actions" de GitHub para auditoría.

## 🛠️ Setup Local

Para facilitar la experiencia de desarrollo (DevX), se incluye un script de automatización local:

```bash
# Dar permisos
chmod +x build.sh

# Ejecutar ciclo completo (Clean -> Test -> Build Debug)
./build.sh
