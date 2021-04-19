object Libs {

   const val kotlinVersion = "1.4.31"
   const val org = "io.kotest.extensions"
   const val dokkaVersion = "0.10.1"

   object Kotest {
      private const val version = "4.4.3"
      const val assertionsShared = "io.kotest:kotest-assertions-shared:$version"
      const val api = "io.kotest:kotest-framework-api:$version"
      const val junit5 = "io.kotest:kotest-runner-junit5-jvm:$version"
   }

   object Koin {
      private const val version = "3.0.1"
      const val core = "io.insert-koin:koin-core:$version"
      const val test = "io.insert-koin:koin-test:$version"
   }

   object Mocking {
      const val mockk = "io.mockk:mockk:1.9.3"
   }
}
