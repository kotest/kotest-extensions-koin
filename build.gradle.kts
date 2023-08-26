plugins {
   java
   `java-library`
   `maven-publish`
   signing
   kotlin("multiplatform") version "1.8.21"
}

repositories {
   mavenLocal()
   mavenCentral()
   maven {
      url = uri("https://oss.sonatype.org/content/repositories/snapshots")
   }
}

group = "io.kotest.extensions"
version = Ci.version

kotlin {

   targets {

      jvm {
         compilations.all {
            kotlinOptions {
               jvmTarget = "1.8"
            }
         }
      }

      js(IR) {
         browser()
         nodejs()
      }

      linuxX64()
      mingwX64()

      iosArm32()
      iosArm64()
      iosSimulatorArm64()
      iosX64()

      macosArm64()
      macosX64()

      tvosArm64()
      tvosSimulatorArm64()
      tvosX64()

      watchosArm32()
      watchosArm64()
      watchosSimulatorArm64()
      watchosX64()
      watchosX86()
   }

   sourceSets {

      val commonMain by getting {
         dependencies {
            implementation(libs.kotest.framework.api)
            implementation(libs.koin.core)
            // TODO: Check if we can switch to `libs.koin.test` below..
            // Seems like a bug in Gradle where you can't use an exclude when using a version catalog reference
            implementation("io.insert-koin:koin-test:3.4.3") {
               exclude(group = "junit", module = "junit")
            }
         }
      }

      val jvmMain by getting {
         dependsOn(commonMain)
      }

      val jvmTest by getting {
         dependsOn(jvmMain)
         dependencies {
            implementation(libs.kotest.runner.junit5)
            implementation(libs.mockk)
         }
      }
   }
}

tasks.named<Test>("jvmTest") {
   useJUnitPlatform()
   filter {
      isFailOnNoMatchingTests = false
   }
   testLogging {
      showExceptions = true
      showStandardStreams = true
      events = setOf(
         org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
         org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
      )
      exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
   }
}

apply("./publish-mpp.gradle.kts")
