package com.example.weatherinfo

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun main() = runBlocking {
        // Create a flow that emits a range of numbers
        val numbersFlow = flow {
            for (i in 1..5) {
                emit(i)
                delay(1000) // Delay for demonstration
            }
        }

        // Collect the values emitted by the flow
        numbersFlow.collect { value ->
            println("Received: $value")
        }

        println("Flow completed")
    }

    @Test
    fun nameList() = runBlocking {
        val flows = flow {
            for (i in 1..5){
                emit(i)
                delay(1000)
            }
        }

        flows.collect{ value ->
            println("Received: $value")
        }

        println("Flow completed")

    }

}