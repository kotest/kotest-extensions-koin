buildscript {
   repositories {
      mavenCentral()
      maven {
         url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
      }
      maven {
         url = uri("https://plugins.gradle.org/m2/")
      }
   }
}

plugins {
   java
   `java-library`
   `maven-publish`
   signing
   id("org.jetbrains.dokka") version Libs.dokkaVersion
   kotlin("multiplatform").version(Libs.kotlinVersion)
}

repositories {
   mavenLocal()
   mavenCentral()
   maven {
      url = uri("https://oss.sonatype.org/content/repositories/snapshots")
   }
}

group = Libs.org
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

      targets {
         metadata {
            compilations.all {
               kotlinOptions {
                  freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
               }
            }
         }

         jvm {
            compilations.all {
               kotlinOptions {
                  jvmTarget = "1.8"
                  freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
               }
            }
         }

         js(IR) {
            browser()
            nodejs()
            compilations.all {
               kotlinOptions {
                  freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
               }
            }
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
   }

   sourceSets {

      val commonMain by getting {
         dependencies {
            implementation(Libs.Kotest.api)
            implementation(Libs.Koin.core)
            implementation(Libs.Koin.test) {
               exclude(group = "junit", module = "junit")
            }
         }
      }

      val jvmMain by getting {
         dependsOn(commonMain)
         dependencies {
         }
      }

      val jvmTest by getting {
         dependsOn(jvmMain)
         dependencies {
            implementation(Libs.Kotest.junit5)
            implementation(Libs.Mocking.mockk)

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
