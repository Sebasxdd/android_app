Write-Host "🚀 Iniciando Build Local..." -ForegroundColor Yellow

# Limpiar y Testear
./gradlew.bat clean testDebugUnitTest
if ($LASTEXITCODE -ne 0) { Write-Host "❌ Error en los tests" -ForegroundColor Red; exit 1 }

# Compilar
./gradlew.bat assembleDebug
if ($LASTEXITCODE -ne 0) { Write-Host "❌ Error en la compilación" -ForegroundColor Red; exit 1 }

Write-Host "✅ ¡Éxito! APK generado." -ForegroundColor Green
