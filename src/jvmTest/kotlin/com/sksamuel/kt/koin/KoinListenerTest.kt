package com.sksamuel.kt.koin

import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.FunSpec
import io.kotest.koin.KoinExtension
import io.kotest.matchers.shouldBe
import org.koin.core.context.GlobalContext
import org.koin.test.KoinTest
import org.koin.test.inject

class KoinListenerTest : FunSpec(), KoinTest {

   override fun extensions() = listOf(KoinExtension(koinModule))

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
