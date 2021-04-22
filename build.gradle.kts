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
   id("java-library")
   id("maven-publish")
   signing
   maven
   `maven-publish`
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

      js {
         browser()
         nodejs()
      }
   }

   sourceSets {

      val commonMain by getting {
         dependencies {
            implementation(Libs.Koin.core)
            implementation(Libs.Kotest.api)
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

      all {
         languageSettings.useExperimentalAnnotation("kotlin.time.ExperimentalTime")
         languageSettings.useExperimentalAnnotation("kotlin.experimental.ExperimentalTypeInference")
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
