# Android DevSecOps Pipeline
**Author:** Martin Sebastian Perez Q. - DevSecOps Engineer

![Build Status](https://github.com/Sebasxdd/android_app/actions/workflows/ci-android.yml/badge.svg) ![Platform](https://img.shields.io/badge/Platform-Android-green) ![Security](https://img.shields.io/badge/Security-DevSecOps-blue)

**Android Secure Pipeline Demo** es un proyecto de referencia diseñado para implementar un ciclo de vida de desarrollo seguro (DevSecOps) en aplicaciones móviles. 

Este repositorio demuestra cómo transformar un desarrollo Android estándar en un proceso automatizado, seguro y auditable, cumpliendo con estándares de la industria para CI/CD.

## Características Principales
* **Trazabilidad Total:** Inyección dinámica de metadatos (Commit SHA, Build ID, Branch) visibles directamente en la interfaz de la aplicación.
* **Zero-Trust Security:** Firmado de APKs utilizando certificados en la nube (GitHub Secrets) sin exponer archivos *keystore* físicos en el repositorio.
* **Integridad del Entorno:** Detección automática de dispositivos *rooteados* y verificación de firmas.
* **Entrega Continua:** Despliegue automático a Firebase App Distribution y generación de Releases en GitHub.

---

## Estructura del Repositorio

A continuación, una breve guía de los directorios clave para navegar por el proyecto:

* **`.github/`**: Carpeta de GitHub ( se encuentran las configuracion de github).
    * `workflows/ci-android.yml`: Pipeline de CI/CD que orquesta pruebas, seguridad y compilación.
    * `CODEOWNERS`: Define la gobernanza del proyecto y quién debe aprobar cambios críticos (code owner actual : sebasxdd).
* **`app/`**: Código fuente de la aplicación Android.
    * `src/main/java`: Lógica Kotlin y Dashboard de seguridad.
    * `src/main/res`: Recursos de UI (Layouts XML, Iconos).
    * `src/test`: Pruebas unitarias (JUnit) para asegurar la calidad del código.
* **`gradle/`**: Wrapper de Gradle que garantiza la consistencia de versiones entre entornos.
* **`build.gradle.kts`**: Scripts de configuración de compilación (Kotlin DSL).

---

## Requisitos Técnicos

Para replicar, compilar o contribuir a este proyecto localmente, se necesita:

* **Sistema Operativo:** Windows 10/11, macOS o Linux.
* **IDE:** Android Studio Ladybug (2024.2.1) o superior.
* **JDK:** Java Development Kit versión **17** (Requerido estrictamente).
* **Git:** Para el control de versiones.
* **Dispositivo:** Un emulador (AVD) o dispositivo físico con "Depuración por USB" activada.

---

## Guía de Replicación usando Android Studio

Sigue estos pasos para levantar el proyecto en su máquina local:

1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/Sebasxdd/android_app.git](https://github.com/Sebasxdd/android_app.git)
    cd android_app
    ```

2.  **Abrir en Android Studio:**
    * Inicia Android Studio.
    * Selecciona **File > Open** y busca la carpeta `android_app`.
    * Selecciona el archivo `build.gradle.kts` (o la carpeta raíz) y dale a "OK".

3.  **Sincronización Gradle:**
    * El IDE comenzará a descargar dependencias automáticamente.
    * *Tip:* Si ves un error de Java, ve a **Settings > Build, Execution, Deployment > Build Tools > Gradle** y asegura que "Gradle JDK" esté en **version 17**.

4.  **Ejecución:**
    * Selecciona la variante **Debug** (la variante Release requiere secretos de nube).
    * Haz clic en el botón verde ▶️ **Run**.

---

## Estrategia de Branching & CI/CD

El proyecto utiliza un flujo **GitHub Flow Protegido** para garantizar la calidad:

1.  **`main` (Protected):** Rama de producción. Los *commits* directos están bloqueados. En esta rama siempre se encontrara "Production ready code"
2.  **`feature/*`:** Ramas de trabajo para nuevas funcionalidades.
3.  **`fix/*`:** Ramas de trabajo para refactorizar el codigo de ser necesario.
4.  **Pull Requests (PR):** Única vía para fusionar código a `main`. Requiere pasar los tests automáticos y aprobación del code owner.

### Pipeline de CI/CD (`.github/workflows/ci-android.yml`)
Cada cambio activa una serie de pasos automatizados:
1.  **Setup:** Prepara Java 17 y caché de Gradle.
2.  **Dependencies:** Instalación limpia de librerías.
3.  **Testing:** Ejecución de pruebas unitarias (`./gradlew test`).
4.  **Security:** Inyección segura del Keystore (decodificación en memoria).
5.  **Build:** Compilación del APK Release con versionado semántico.
6.  **Release:** Publicación en GitHub Releases y notificación a Testers (Firebase).

---

## Descargas (Releases)

Este proyecto genera automáticamente un.APK en cada versión estable.

1.  Ve a la sección **[Releases](../../releases)** en la barra lateral derecha.
2.  Busca la etiqueta más reciente (ej. `v1.0.15`).
3.  Despliega la sección **Assets**.
4.  Descarga el archivo `MySecureApp-build-XX.apk`.

---

## Seguridad y Gestión de Secretos (GHAS)

Este proyecto implementa prácticas de seguridad de nivel empresarial ("Bank-Grade Security") utilizando las capacidades nativas de GitHub.

### 1. Gestión de Secretos (Secrets Management)
Las credenciales sensibles **nunca** se almacenan en el código. Se inyectan en tiempo de ejecución utilizando **GitHub Actions Secrets**.

* **Implementación:**
    El keystore de Android se almacena codificado en Base64 y se reconstruye efímeramente solo durante la compilación.
    
    ```yaml
    # Ejemplo real extraído de .github/workflows/ci-android.yml
    - name: Decode Keystore
      run: |
        echo "${{ secrets.KEYSTORE_BASE64 }}" | base64 --decode > app/release.keystore
      
    - name: Build Release APK
      env:
        # Los secretos se inyectan como variables de entorno
        KEYSTORE_PASSWORD: ${{ secrets.KEYSTORE_PASSWORD }}
        KEY_ALIAS: ${{ secrets.KEY_ALIAS }}
        KEY_PASSWORD: ${{ secrets.KEY_PASSWORD }}
    ```

### 2. GitHub Advanced Security (Integración)
El repositorio está configurado para utilizar herramientas de análisis estático (SAST) y análisis de dependencias (SCA).

* **Dependency Review:** Se ejecuta automáticamente en cada Pull Request para bloquear dependencias vulnerables antes de que entren a `main`.
* **CodeQL Analysis (Ejemplo de Workflow):**
    El pipeline soporta la integración de CodeQL para detectar vulnerabilidades en el código Java/Kotlin.

    ```yaml
    # Snippet de integración GHAS / CodeQL
    - name: Initialize CodeQL
      uses: github/codeql-action/init@v2
      with:
        languages: 'java'
        
    - name: Autobuild
      uses: github/codeql-action/autobuild@v2
      
    - name: Perform CodeQL Analysis
      uses: github/codeql-action/analyze@v2
    ```
---

## Gestión de Testers (Firebase App Distribution)

El pipeline está configurado para enviar automáticamente el APK a los testers definidos. Para agregar o quitar personas de la lista de distribución, existen dos métodos:

### Edición Directa en el Pipeline:
Para agregar correos directamente en el código:

1.  Edita el archivo `.github/workflows/ci-android.yml`.
2.  Busca el paso llamado **"Upload to Firebase App Distribution"**.
3.  Modifica el parámetro `testers`. Puedes agregar múltiples correos separados por comas.

```yaml
      - name: Upload to Firebase App Distribution
        uses: wzieba/Firebase-Distribution-Github-Action@v1
        with:
          appId: ${{ secrets.FIREBASE_APP_ID }}
          token: ${{ secrets.FIREBASE_TOKEN }}
          # correos separados por coma (sin espacios entre ellos)
          testers: "martin@email.com,qa-lead@email.com,nuevo-dev@email.com"
          file: MySecureApp-build-${{ github.run_number }}.apk
