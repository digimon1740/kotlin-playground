package com.digimon.kotlinplayground.coroutines.channel

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import kotlin.system.measureTimeMillis

val nItems = 10_000_000

suspend fun testChannel() = coroutineScope {
    val channel = Channel<Int>(Channel.UNLIMITED)
    val producer = launch {
        repeat(nItems) {
            channel.send(it)
        }
        channel.close()
    }
    val consumer = launch {
        for (item in channel) {
            // consume the item
        }
    }
    producer.join()
    consumer.join()
}

fun testLinkedBlockingQueue() {
    val executorService = Executors.newFixedThreadPool(2)
    val queue = LinkedBlockingQueue<Int>()
    val producer = executorService.submit {
        repeat(nItems) {
            queue.put(it)
        }
    }
    val consumer = executorService.submit {
        while (true) {
            val item = queue.take()
            // consume the item
            if (item == nItems - 1) break
        }
    }
    producer.get()
    consumer.get()
    executorService.shutdown()
}

fun main() = runBlocking {
    var timeChannelTotal = 0L
    repeat(10) {
        val timeChannel = measureTimeMillis {
            testChannel()
        }
        timeChannelTotal += timeChannel
        println("Time taken by Channel: $timeChannel ms")
    }
    println("Average Time taken by Channel: ${timeChannelTotal.div(10)} ms")

    var timeQueueTotal = 0L
    repeat(10) {
        val timeQueue = measureTimeMillis {
            testLinkedBlockingQueue()
        }
        timeQueueTotal += timeQueue
        println("Time taken by LinkedBlockingQueue: $timeQueue ms")
    }
    println("Average Time taken by LinkedBlockingQueue: ${timeQueueTotal.div(10)} ms")
}