import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.apache.tools.ant.taskdefs.condition.Os // added by Ellen
import org.gradle.internal.os.OperatingSystem
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "org.openrndr.template"
version = "1.0.0"

val applicationMainClass = "Homework9GameKt"

/** ## additional features */
val orxFeatures = setOf("orx-minim")
val ormlFeatures = emptySet<String>()
val openrndrFeatures = setOf("video")

/** ## configure the type of logging this project uses */
enum class Logging { NONE, SIMPLE, FULL }

val applicationLogging = Logging.NONE

// ------------------------------------------------------------------------------------------------------------------ //

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.shadow)
    alias(libs.plugins.runtime)
    alias(libs.plugins.gitarchive.tomarkdown).apply(false)
    alias(libs.plugins.versions)
    alias(libs.plugins.dokka)
    application
}

repositories {
    mavenCentral()
    mavenLocal()
}

// Added by Ellen
if (Os.isFamily(Os.FAMILY_MAC)) {
    application {
        applicationDefaultJvmArgs = listOf("-XstartOnFirstThread")
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.slf4j.api)
    implementation(libs.kotlin.logging)

    when (applicationLogging) {
        Logging.NONE -> {
            runtimeOnly(libs.slf4j.nop)
        }
        Logging.SIMPLE -> {
            runtimeOnly(libs.slf4j.simple)
        }
        Logging.FULL -> {
            runtimeOnly(libs.log4j.slf4j2)
            runtimeOnly(libs.log4j.core)
            runtimeOnly(libs.jackson.databind)
            runtimeOnly(libs.jackson.json)
        }
    }
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(libs.junit)
}

// ------------------------------------------------------------------------------------------------------------------ //

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

// ------------------------------------------------------------------------------------------------------------------ //

application {
    mainClass.set(applicationMainClass)
}

tasks {
    named<ShadowJar>("shadowJar") {
        manifest {
            attributes["Main-Class"] = applicationMainClass
            attributes["Implementation-Version"] = project.version
        }
        minimize {
            exclude(dependency("org.openrndr:openrndr-gl3:.*"))
            exclude(dependency("org.jetbrains.kotlin:kotlin-reflect:.*"))
            exclude(dependency("org.slf4j:slf4j-simple:.*"))
            exclude(dependency("org.apache.logging.log4j:log4j-slf4j2-impl:.*"))
            exclude(dependency("com.fasterxml.jackson.core:jackson-databind:.*"))
            exclude(dependency("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:.*"))
        }
    }
    named<org.beryx.runtime.JPackageTask>("jpackage") {
        doLast {
            val destPath =
                if (OperatingSystem.current().isMacOsX) {
                    "build/jpackage/openrndr-application.app/Contents/Resources/data"
                } else {
                    "build/jpackage/openrndr-application/data"
                }

            copy {
                from("data") {
                    include("**/*")
                }
                into(destPath)
            }
        }
    }
}

// ------------------------------------------------------------------------------------------------------------------ //

val jpackageDir = layout.buildDirectory.dir("jpackage")

tasks.register<Zip>("jpackageZip") {
    archiveFileName.set("openrndr-application.zip")
    from(jpackageDir) {
        include("**/*")
    }
}
tasks.findByName("jpackageZip")?.dependsOn("jpackage")

// ------------------------------------------------------------------------------------------------------------------ //

runtime {
//    jpackage {
//        imageName = "openrndr-application"
//        skipInstaller = true
//        if (OperatingSystem.current() == OperatingSystem.MAC_OS) jvmArgs.add("-XstartOnFirstThread")
//    }
    options.set(listOf("--strip-debug", "--compress", "1", "--no-header-files", "--no-man-pages"))
    modules.set(listOf("jdk.unsupported", "java.management"))
}

// ------------------------------------------------------------------------------------------------------------------ //

tasks.register<org.openrndr.extra.gitarchiver.GitArchiveToMarkdown>("gitArchiveToMarkDown") {
    historySize.set(20)
}

// ------------------------------------------------------------------------------------------------------------------ //

tasks {
    dependencyUpdates {
        gradleReleaseChannel = "current"

        val nonStableKeywords = listOf("alpha", "beta", "rc")

        fun isNonStable(version: String) =
            nonStableKeywords.any {
                version.lowercase().contains(it)
            }

        rejectVersionIf {
            isNonStable(candidate.version) && !isNonStable(currentVersion)
        }
    }
}

// ------------------------------------------------------------------------------------------------------------------ //

class Openrndr {
    val openrndrVersion = libs.versions.openrndr.get()
    val orxVersion = libs.versions.orx.get()
    val ormlVersion = libs.versions.orml.get()

    // choices are "orx-tensorflow-gpu", "orx-tensorflow"
    val orxTensorflowBackend = "orx-tensorflow"

    val currArch = DefaultNativePlatform("current").architecture.name
    val currOs = OperatingSystem.current()
    val os =
        if (project.hasProperty("targetPlatform")) {
            val supportedPlatforms = setOf("windows", "macos", "linux-x64", "linux-arm64")
            val platform: String = project.property("targetPlatform") as String
            if (platform !in supportedPlatforms) {
                throw IllegalArgumentException("target platform not supported: $platform")
            } else {
                platform
            }
        } else {
            when {
                currOs.isWindows -> "windows"
                currOs.isMacOsX ->
                    when (currArch) {
                        "aarch64", "arm-v8" -> "macos-arm64"
                        else -> "macos"
                    }
                currOs.isLinux ->
                    when (currArch) {
                        "x86-64" -> "linux-x64"
                        "aarch64" -> "linux-arm64"
                        else -> throw IllegalArgumentException("architecture not supported: $currArch")
                    }
                else -> throw IllegalArgumentException("os not supported: ${currOs.name}")
            }
        }

    fun orx(module: String) = "org.openrndr.extra:$module:$orxVersion"

    fun orml(module: String) = "org.openrndr.orml:$module:$ormlVersion"

    fun openrndr(module: String) = "org.openrndr:openrndr-$module:$openrndrVersion"

    fun openrndrNatives(module: String) = "org.openrndr:openrndr-$module-natives-$os:$openrndrVersion"

    fun orxNatives(module: String) = "org.openrndr.extra:$module-natives-$os:$orxVersion"

    init {
        dependencies {
            runtimeOnly(openrndr("gl3"))
            runtimeOnly(openrndrNatives("gl3"))
            implementation(openrndr("openal"))
            runtimeOnly(openrndrNatives("openal"))
            implementation(openrndr("application"))
            implementation(openrndr("svg"))
            implementation(openrndr("animatable"))
            implementation(openrndr("extensions"))
            implementation(openrndr("filter"))
            implementation(openrndr("dialogs"))
            if ("video" in openrndrFeatures) {
                implementation(openrndr("ffmpeg"))
                runtimeOnly(openrndrNatives("ffmpeg"))
            }
            for (feature in orxFeatures) {
                implementation(orx(feature))
            }
            for (feature in ormlFeatures) {
                implementation(orml(feature))
            }
            if ("orx-tensorflow" in orxFeatures) runtimeOnly("org.openrndr.extra:$orxTensorflowBackend-natives-$os:$orxVersion")
            if ("orx-kinect-v1" in orxFeatures) runtimeOnly(orxNatives("orx-kinect-v1"))
            if ("orx-olive" in orxFeatures) implementation(libs.kotlin.script.runtime)
        }
    }
}
val openrndr = Openrndr()

if (properties["openrndr.tasks"] == "true") {
    task("create executable jar for $applicationMainClass") {
        group = " \uD83E\uDD8C OPENRNDR"
        dependsOn("shadowJar")
    }

    task("run $applicationMainClass") {
        group = " \uD83E\uDD8C OPENRNDR"
        dependsOn("run")
    }

    task("create standalone executable for $applicationMainClass") {
        group = " \uD83E\uDD8C OPENRNDR"
        dependsOn("jpackageZip")
    }

    task("add IDE file scopes") {
        group = " \uD83E\uDD8C OPENRNDR"
        val scopesFolder = File("${project.projectDir}/.idea/scopes")
        scopesFolder.mkdirs()

        val files =
            listOf(
                "Code" to "file:*.kt||file:*.frag||file:*.vert||file:*.glsl",
                "Text" to "file:*.txt||file:*.md||file:*.xml||file:*.json",
                "Gradle" to "file[*buildSrc*]:*/||file:*gradle.*||file:*.gradle||file:*/gradle-wrapper.properties||file:*.toml",
                "Media" to "file:*.png||file:*.jpg||file:*.dds||file:*.exr||file:*.mp3||file:*.wav||file:*.mp4||file:*.mov||file:*.svg",
            )
        files.forEach { (name, pattern) ->
            val file = File(scopesFolder, "__$name.xml")
            if (!file.exists()) {
                file.writeText(
                    """
                    <component name="DependencyValidationManager">
                      <scope name=" ★ $name" pattern="$pattern" />
                    </component>
                    """.trimIndent(),
                )
            }
        }
    }
}
