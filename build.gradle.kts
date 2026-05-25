import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.Effort

plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
    checkstyle
    id("com.github.spotbugs") version "6.0.25"
    jacoco
    id("info.solidsoft.pitest") version "1.15.0"
    application
}

group = "nu.csse.sqe"
version = "1.0"

repositories {
    mavenCentral()
}

application {
    mainClass.set("domain.Main")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.platform:junit-platform-suite")

    testImplementation("org.easymock:easymock:5.2.0")

    // cucumber
    testImplementation(platform("io.cucumber:cucumber-bom:7.20.1"))
    testImplementation("io.cucumber:cucumber-java")
    testImplementation("io.cucumber:cucumber-junit-platform-engine")
    testImplementation("io.cucumber:cucumber-picocontainer")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

application {
    mainClass.set("ui.Main")
}

javafx {
    version = "17.0.16"
    modules("javafx.controls", "javafx.fxml")
}

tasks.compileJava {
    options.release = 11
}

tasks.withType<Checkstyle>().configureEach {
    reports {
        xml.required = false
        html.required = true
        html.stylesheet = resources.text.fromFile("config/xsl/checkstyle-noframes-severity-sorted.xsl")
    }
}

checkstyle{
    isIgnoreFailures = false
}

spotbugs {
    ignoreFailures = false
    showStackTraces = true
    showProgress = true
    effort = Effort.DEFAULT
    reportLevel = Confidence.DEFAULT
    maxHeapSize = "1g"
}

tasks.spotbugsMain {
    reports.create("html") {
        required = true
        outputLocation = layout.buildDirectory.file("reports/spotbugs/spotbugs.html")
        setStylesheet("fancy-hist.xsl")
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
    //finalizedBy(tasks.pitest)
}

configurations {}

val cucumberRuntime by configurations.creating {
    extendsFrom(configurations["testImplementation"])
}

task("cucumber") {
    dependsOn("assemble", "compileTestJava")
    doLast {
        javaexec {
            mainClass.set("io.cucumber.core.cli.Main")
            classpath = cucumberRuntime + sourceSets.main.get().output + sourceSets.test.get().output
            args = listOf("--plugin", "pretty",
                "--glue", "domain",  // where the step definitions are.
                "src/test/resources")                    // where the feature files are.
            // Configure jacoco agent for the test coverage.
            val jacocoAgent = zipTree(configurations.jacocoAgent.get().singleFile)
                .filter { it.name == "jacocoagent.jar" }
                .singleFile
            jvmArgs = listOf("-javaagent:$jacocoAgent=destfile=$buildDir/results/jacoco/cucumber.exec,append=false")
        }
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    dependsOn("cucumber")

    executionData(
        files(
            "$buildDir/jacoco/test.exec",
            "$buildDir/results/jacoco/cucumber.exec"
        )
    )

    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco"))
    }
}

tasks.build {
    dependsOn("pitest")
}

pitest {
    targetClasses = setOf("domain.*")
    targetTests = setOf("domain.*")
    junit5PluginVersion = "1.2.1"
    pitestVersion = "1.15.0"

    threads = 4
    outputFormats = setOf("HTML")
    timestampedReports = false

    testSourceSets.set(listOf(sourceSets.test.get()))
    mainSourceSets.set(listOf(sourceSets.main.get()))

    jvmArgs.set(listOf("-Xmx1024m"))
    useClasspathFile.set(true)
    fileExtensionsToFilter.addAll("xml")
    exportLineCoverage = true
}
