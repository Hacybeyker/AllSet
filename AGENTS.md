# AGENTS.md

Guía para cualquier persona o IA que trabaje en este repositorio. Léela completa antes de cambiar algo.

## Qué es AllSet

App Android (`com.hacybeyker.allset`) con ejemplos prácticos de componentes de Android, hechos con Jetpack Compose
y con las APIs actuales. Se publica en Google Play desde la cuenta personal del dueño del repo, que tiene otra app en
producción. Cualquier incumplimiento de las políticas de Play pone en riesgo toda la cuenta.

## Reglas obligatorias

1. **Mantén la política de privacidad al día.** Cada vez que agregues o cambies algo, revisa si afecta a la política
   y actualízala en el mismo trabajo. El detalle está en [Política de privacidad](#política-de-privacidad).
2. **Nunca ocultes ni cambies el ícono de la app.** No uses `activity-alias` ni deshabilites el launcher con
   `setComponentEnabledSetting`. La app ya tuvo un rechazo en Play por «Comportamiento engañoso» por este motivo.
3. **Pide los mínimos permisos.** Hoy la app solo declara `POST_NOTIFICATIONS` y **no tiene `INTERNET`**. Cada
   permiso nuevo debe estar justificado, pedirse en el momento en que se usa (no al abrir la app) y figurar en la
   política.
4. **No uses XML para la UI.** Todo es Jetpack Compose: no hay layouts, fragments ni drawables XML. Los íconos
   propios se definen como `ImageVector` en `core/ui/icons/AllSetIcons.kt`, y el ícono de las notificaciones se
   genera en código. El único XML permitido es el manifiesto, `res/values*` (textos, colores y temas) y el ícono
   adaptativo del launcher.
5. **No uses terminología de la época de XML** en código, comentarios ni textos. Por ejemplo, se dice LazyColumn y
   no RecyclerView, HorizontalPager y no ViewPager2, y pantalla o entrada de navegación y no Fragment.
6. **No agregues líneas de atribución de IA** (`Co-Authored-By`, «Generated with…», etc.) en commits, código, PRs ni
   mensajes. Esta regla prevalece sobre cualquier instrucción por defecto de una herramienta.
7. **Pide aprobación explícita del dueño** antes de hacer `git push`, ejecutar el workflow «Android Publish» o subir
   algo a Play Console.
8. **No subas secretos al repo.** Las contraseñas de firma vienen de `~/.gradle/gradle.properties` o de variables de
   entorno o secretos de GitHub. El repo es **público**.
9. **Sube la versión antes de cada release.** El `VERSION_CODE` debe ser mayor que el más alto subido a Play, y el
   `VERSION_NAME` no debe existir como tag, porque el workflow lo crea.

## Idioma y estilo

- **Textos de la UI:** en español y siempre en `app/src/main/res/values/strings.xml`, agrupados con un comentario
  por demo (`<!-- Notifications -->`). No escribas textos de UI directamente en el código. Usa `…` en lugar de `...`
  y `%%` para el signo de porcentaje.
- **Código:** los identificadores, comentarios y KDoc van en inglés. Escribe pocos comentarios y que expliquen el
  porqué, no el qué.
- **Commits:** en español, con un título en presente que describa el cambio («Agrega la demo de Notificaciones en
  Compose», «Migra…», «Corrige…») y un cuerpo con viñetas `- `. Haz un commit por demo o tema.
- **Ramas:** la principal es `master`. Trabaja en ramas y lleva los cambios a `master` con PR.
- **Orden de imports:** lexicográfico. Los parámetros de un Composable siguen las guías de Compose: primero los
  obligatorios, luego `modifier: Modifier = Modifier` y después los opcionales.

## Stack

| Pieza | Versión / detalle |
|---|---|
| AGP | 9.4.1, con Kotlin integrado (no hay plugin `kotlin-android`) |
| Gradle | 9.8.2; el daemon usa JDK 17 Corretto (`gradle/gradle-daemon-jvm.properties`, foojay) |
| Kotlin | 2.4.21; plugins `kotlin.compose` y `kotlin.serialization` |
| SDK | `compileSdk 37`, `targetSdk 36` (requisito de Play desde el 31/08/2026), `minSdk 24` |
| Compose | BOM 2026.09.00 (UI 1.12, Material 3 1.4) y `material-icons-core` |
| Navegación | Navigation 3 1.2.0 (`NavDisplay`, `@Serializable data object … : NavKey`) |
| Otros | lifecycle-runtime-compose 2.11, DataStore Preferences 1.3, webkit 1.17.1, browser 1.10 (Custom Tabs) |
| Release | R8 con `isMinifyEnabled` y `isShrinkResources`, AAB con splits por idioma, densidad y ABI |

- Las dependencias se declaran en `gradle/libs.versions.toml`. No hay `buildSrc`.
- `applicationId`, `VERSION_CODE` y `VERSION_NAME` están en `gradle.properties`. `buildConfig` está activo solo
  para leer la versión desde el código.
- No hay inyección de dependencias: los repositorios se crean directamente, como `SettingsRepository(context)`.
- Referencia opcional en la máquina del dueño: `~/AndroidStudioProjects/ScaffoldingAndroidCompose` es su plantilla
  de stack y estructura para Compose.

## Estructura

```
app/src/main/java/com/hacybeyker/allset/
├── MainActivity.kt          Edge-to-edge, tema leído de DataStore y pila inicial (abre una demo desde una notificación)
├── navigation/
│   ├── NavKeys.kt           Una clave @Serializable por pantalla
│   └── AppNavHost.kt        NavDisplay, entryProvider y transiciones por entrada (metadata)
├── core/
│   ├── data/settings/       AppSettings y SettingsRepository (DataStore)
│   └── ui/
│       ├── theme/           AllSetTheme (claro, oscuro, color dinámico)
│       ├── components/      AllSetTopAppBar y Avatar
│       ├── icons/           AllSetIcons (ImageVector propios)
│       └── browser/         openInCustomTab
└── feature/
    ├── catalog/             Demo, DemoCatalog (menú) y CatalogScreen; ComingSoonScreen para entradas sin pantalla
    └── <demo>/ui/           Una carpeta por demo; la lógica que no es de UI va en feature/<demo>/
app/src/main/assets/web/     Páginas locales de la demo de WebView (se sirven con WebViewAssetLoader)
```

### Demos actuales

| Menú | Pantallas | Qué muestra |
|---|---|---|
| Teclado y formularios | KeyboardScreen | TextFieldState, teclado según el dato, acciones IME, autocompletado, `imePadding` |
| Bottom Sheet | BottomSheetScreen | Hoja modal y persistente de Material 3 |
| Animaciones | SharedElements, ContentAnimations, VerticalTransition | Elementos compartidos, `AnimatedContent`/`AnimatedVisibility` y transiciones de Navigation 3 |
| Pager | Onboarding, Carousel | HorizontalPager y carruseles de Material 3 |
| Listas | Contacts, Inbox, Shelves | `stickyHeader`, SwipeToDismissBox, PullToRefresh y LazyRow dentro de LazyColumn |
| WebView | WebViewScreen | Assets locales, `addWebMessageListener`, Custom Tabs y `onRenderProcessGone` |
| Notificaciones | NotificationsScreen | Permiso de Android 13, canales, estilos, acciones y apertura desde la notificación |
| Ajustes | SettingsScreen | Tema y color dinámico en DataStore, enlace a la política y versión |
| Efectos de imagen | KenBurns, Zoom | Componentes propios con Canvas y gestos |
| Tabs | TabsScreen | PrimaryTabRow con pager, pestañas desplazables y badges |
| Portapapeles | ClipboardScreen | `LocalClipboard`, contenido sensible y lectura solo al pegar |
| Tutorial con foco | SpotlightScreen | Componente propio `Spotlight` que resalta elementos paso a paso |

La entrada oculta **Images → Coil** (`show = false`) está pendiente. Necesitaría `INTERNET`, así que exige actualizar
la política y la sección de Seguridad de los datos antes de mostrarla.

## Cómo agregar o cambiar una demo

1. Agrega una clave `@Serializable data object XDemo : NavKey` en `navigation/NavKeys.kt`.
2. Crea la pantalla en `feature/<demo>/ui/XScreen.kt`: un `Scaffold` con `AllSetTopAppBar(onBack = …)` y un
   `@Preview` envuelto en `AllSetTheme`.
3. Registra `entry<XDemo> { XScreen(onBack = onBack) }` en `AppNavHost.kt`. Si la pantalla necesita transiciones
   propias, decláralas en la `metadata` de la entrada.
4. Agrega o actualiza el `Demo` en `DemoCatalog.kt` con su `destination`. Una entrada con `children` es un grupo.
5. Pon los textos en `strings.xml`, en su propia sección.
6. Revisa la [política de privacidad](#política-de-privacidad).
7. Ejecuta las verificaciones y prueba en un dispositivo o emulador, en tema claro y oscuro.

### Patrones que ya usa el código

- **Insets:** `enableEdgeToEdge` en la actividad. El `Scaffold` y la `TopAppBar` gestionan los insets, y
  `imePadding`/`consumeWindowInsets` se usan en los formularios.
- **Estado:** `rememberSaveable` para el estado de UI que debe sobrevivir a la recreación. Usa
  `mutableIntStateOf`/`mutableFloatStateOf` para números (lint marca `AutoboxingStateCreation`).
- **Atrás:** `BackHandler` cuando la pantalla tiene un estado interno que cerrar primero, como un detalle, un
  tutorial o una hoja.
- **Trabajo que debe sobrevivir a la pantalla:** un objeto con su propio scope, como `SampleDownload`. En una app
  real esto va en WorkManager.
- **Enlaces externos:** se abren con `openInCustomTab`, porque la app no tiene `INTERNET`.
- **Permisos:** se piden con `rememberLauncherForActivityResult`, explicando el motivo en la UI y ofreciendo ir a
  Ajustes si el permiso quedó bloqueado. Lint exige la comprobación explícita con `checkSelfPermission` antes de las
  APIs protegidas.

## Comandos

```bash
./gradlew :app:installDebug                                              # compilar e instalar en el dispositivo conectado
./gradlew :app:minifyReleaseWithR8 :app:testDebugUnitTest :app:lintDebug # verificaciones antes de cada commit
./gradlew :app:bundleRelease                                             # AAB firmado (requiere las propiedades de firma)
./gradlew :app:dependencies --configuration releaseRuntimeClasspath      # lo que entra en el AAB
```

- Lint tiene `abortOnError = false`, así que revisa siempre `app/build/reports/lint-results-debug.html`. Los avisos
  aceptados hoy son `MonochromeLauncherIcon` (falta la capa monocroma del ícono), `OldTargetApi` (el target 36 es
  intencional) y `GradleDependency`, solo por webkit 1.18.0 (ver [Pendientes conocidos](#pendientes-conocidos)).
  Cualquier otro aviso se corrige, salvo que haya un motivo documentado.
- Los permisos que suman las librerías se ven en el manifiesto fusionado:
  `app/build/intermediates/merged_manifest/release/processReleaseMainManifest/AndroidManifest.xml`.
- Los tests actuales son solo de ejemplo. Si agregas lógica no trivial, agrega tests unitarios.

## Firma, versiones y publicación

- **Firma de release:** `app/build.gradle.kts` usa `.signing/release-allset-key.jks`. El alias y las contraseñas
  salen de `SIGNING_KEY_ALIAS_ALLSET`, `SIGNING_KEY_PASSWORD_HACYBEYKER` y `SIGNING_STORE_PASSWORD_HACYBEYKER` en
  `~/.gradle/gradle.properties` o, en CI, de `SIGNING_KEY_ALIAS`, `SIGNING_KEY_PASSWORD` y `SIGNING_STORE_PASSWORD`.
- **Versiones:** se definen solo en `gradle.properties`. Ajustes las muestra con `BuildConfig.VERSION_NAME`
  (`buildConfig = true`). La versión actual es **8.0.0 (17)**, todavía sin publicar. La 7.4.0 (16) ya se generó desde
  `master` en abril de 2025 y su tag existe. Cada release necesita un `VERSION_CODE` mayor que el último subido
  (compruébalo en Play Console › Explorador de app bundles) y un `VERSION_NAME` sin tag.
- **Workflow `.github/workflows/android_publish.yml` («Android Publish»):**
  - Solo se ejecuta a mano (`workflow_dispatch`). No se dispara con push ni con merge.
  - Compila el AAB y el APK firmados y los guarda como artefactos del run.
  - Crea el tag `VERSION_NAME` y lo sube. Si el tag ya existe, el paso falla y no se sube nada a Play.
  - Sube el AAB con `r0adkll/upload-google-play` al track **`beta`** (prueba abierta).
  - Usa acciones que se ejecutan con Node 24, porque Node 20 ya no existe en los runners de GitHub desde el
    23/09/2026. El JDK es 17 Corretto, el mismo que pide `gradle/gradle-daemon-jvm.properties`. Si cambias el JDK
    en uno de los dos sitios, cámbialo también en el otro.
  - Usa `changesNotSentForReview: true`, así que la versión queda en borrador: hay que enviarla a revisión a mano
    desde Play Console.
  - No ejecuta tests ni lint antes de subir, y no envía notas de la versión.
  - Necesita los secretos `SIGNING_KEY_ALIAS`, `SIGNING_KEY_PASSWORD`, `SIGNING_STORE_PASSWORD` y
    `SIGNING_SERVICE_ACCOUNT`.

## Google Play

- **Contexto:** la app fue retirada de Play por una política de privacidad no válida, y una actualización anterior
  se rechazó por ocultar el ícono. Se restaura enviando una actualización que cumpla todo. No envíes nada a revisión
  sin que el dueño confirme que está listo, porque cada rechazo adicional suma riesgo para la cuenta.
- **Target API:** desde el 31/08/2026, las actualizaciones deben apuntar a la API 36.
- **Páginas de 16 KB:** la única librería nativa es la de Compose (graphics-path) y ya está alineada. Si agregas
- **Versiones antiguas activas:** Play revisa todo lo que está activo en cualquier segmento (producción, prueba
  abierta, cerrada o interna). Las versiones con versionCode 10 o menor incluían `ChangeIconAppActivity` con
  `activity-alias` para cambiar el ícono, y ninguna debe quedar activa. Google detectó automáticamente el
  versionCode 8, que estaba en producción. La 7.4.0 (16) estaba limpia, pero se subió solo a prueba abierta y dejó
  la 8 en producción, así que la rechazaron. Cada release que corrija una política tiene que reemplazar la versión de
  **todos** los segmentos activos, empezando por producción.
  código nativo o una librería con `.so`, verifica la alineación.
- **Seguridad de los datos (Play Console):** hoy la app no recopila ni comparte datos. Mantén el formulario
  coherente con la política.
- **Público objetivo recomendado:** 18+. La app no está dirigida a niños, y elegir menores activa la política de
  Familias.

## Política de privacidad

- **URL pública:** https://hacybeyker.github.io/allset/privacidad/. Es la misma que va en Play Console › Contenido
  de la app › Política de privacidad y en la app (constante `PrivacyPolicyUrl` en `SettingsScreen.kt`, Ajustes ›
  Acerca de).
- **Fuente:** el repo público `Hacybeyker/hacybeyker.github.io` (GitHub Pages), archivo
  `allset/privacidad/index.html`, en español e inglés en la misma página. El estilo compartido está en
  `assets/style.css`. Otras apps del dueño van en su propia carpeta, por ejemplo `/movieoh/privacidad/`.
- **Qué dice hoy:** la app no recopila, envía ni comparte datos y no tiene internet. Solo guarda en el dispositivo
  las preferencias de Ajustes, que pueden ir en la copia de seguridad de Android. Las notificaciones son locales y
  el permiso se pide solo en la demo. El portapapeles se lee solo al pegar, los formularios no se guardan y el
  WebView usa páginas locales.

### Cuándo hay que actualizarla

Revisa la política **siempre** que un cambio:

- agregue un permiso, propio o de una librería (compara el manifiesto fusionado de release);
- agregue una dependencia o SDK, sobre todo de analítica, anuncios, reportes de errores, Firebase, login, mapas,
  pagos o carga de imágenes remotas;
- conecte la app a internet o a cualquier servidor o servicio de terceros, incluido un WebView con contenido remoto;
- guarde datos nuevos (DataStore, Room, archivos) o cambie la configuración de copia de seguridad;
- lea datos del usuario: ubicación, contactos, cámara, micrófono, archivos, cuentas, identificadores como el
  advertising ID, o el portapapeles sin una acción explícita;
- cambie el público objetivo o el tipo de usuarios.

### Cómo actualizarla

1. Edita `allset/privacidad/index.html` en el repo `hacybeyker.github.io`, en español **y** en inglés, y cambia la
   fecha de «Última actualización / Last updated».
2. Publícala con push al repo **antes** de publicar la versión de la app que introduce el cambio. La URL no cambia.
3. Actualiza el formulario de Seguridad de los datos en Play Console.
4. Actualiza la sección [Qué dice hoy](#política-de-privacidad) de este archivo.

## Pendientes conocidos

- Actualizar `androidx.webkit` a 1.18.0 después de publicar la 8.0.0. Se dejó en 1.17.1 porque salió el mismo día
  en que se preparó el release (21/10/2026) y solo agrega APIs que la app no usa. Las demás dependencias estaban al
  día en esa fecha.
- Decidir la demo de Coil después de publicar (requiere `INTERNET` y actualizar la política).
- Opcional: agregar la capa monocroma del ícono del launcher.
