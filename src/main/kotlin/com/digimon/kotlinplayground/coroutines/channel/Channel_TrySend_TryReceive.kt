package com.digimon.kotlinplayground.coroutines.channel

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

fun main(args: Array<String>) = runBlocking {
    val channel = Channel<Int>(3) // 버퍼의 크기를 3으로 설정한 채널을 생성한다.
    println("channel : $channel")

    launch {
        repeat(10) {    //  0 부터 9 까지 10번 반복한다.

            // 반복문을 통해 생성된 int를 채널로 보낸다.
            channel.trySend(it)
            // trySend는 suspend 함수가 아니므로 yield를 사용해야한다.
            yield()
        }
        channel.close()

    }

    launch {
        while (!channel.isClosedForSend) {  // 채널에 열려있는 동안 반복한다.

            // 채널에서 값을 받아온다.
            // tryReceive를 사용하면 ChannelResult로 래핑되어 채널 값이 없을 때 예외가 발생하지 않는다.
            println("receive : ${channel.tryReceive().getOrNull()}")
            // tryReceive는 suspend 함수가 아니므로 yield를 사용해야한다.
            yield()
        }
    }

    println("")
}