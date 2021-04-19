package com.sksamuel.kt.koin

import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.FunSpec
import io.kotest.koin.KoinExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.koin.core.context.GlobalContext
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.declareMock

class KoinListenerTest : FunSpec(), KoinTest {

   override fun listeners() = listOf(KoinExtension(koinModule))

   private val genericService by inject<GenericService>()

   init {
      test("Should have autowired the service correctly") {
         genericService.foo() shouldBe "Bar"
      }
   }

   override fun afterSpec(spec: Spec) {
      GlobalContext.getOrNull() shouldBe null // We should finish koin after test execution
   }
}

class KoinListenerMockTest : FunSpec(), KoinTest {

   init {
      val genericService by inject<GenericService>()

      listeners(KoinExtension(koinModule) { mockk<GenericRepository>() })

      test("Should allow mocking correctly") {

         declareMock<GenericRepository> {
            every { foo() } returns "DootyDoot"
         }

         genericService.foo() shouldBe "DootyDoot"
      }

      afterSpec {
         GlobalContext.getOrNull() shouldBe null // We should finish koin after test execution
      }
   }
}
