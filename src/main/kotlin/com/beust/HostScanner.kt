package com.beust

import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException
import java.util.concurrent.ExecutorCompletionService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class HostScanner {
    fun connect(hostName: String, portNumber: Int): Result {
        val result =
            try {
                val socket = Socket()
                socket.connect(InetSocketAddress(hostName, portNumber), 1000)
                true
            } catch(ex: SocketTimeoutException) {
                false
            }
        return Result(hostName, portNumber, result)
    }

    data class Result(val host: String, val port: Int, val success: Boolean)
    fun connect2(hostName: String, portNumber: Int): Result {
        return Result(hostName, portNumber, hostName.endsWith("17"))
    }

    fun scan() {
        val subNet = "47.208"
//        val subNet = "47.208.180"
        val portNumber = 4444

        val executor = Executors.newFixedThreadPool(100)
        val pool = ExecutorCompletionService<Result>(executor)
        val futures = arrayListOf< Future<Result>>()
        var count = 0
        (195..197).forEach { j ->
            (0..255).forEach { i ->
                futures.add(pool.submit { connect("$subNet.$j.$i", portNumber) })
            }
        }

        println("Running")
        val successes = arrayListOf<Result>()
        futures.forEach {
            count++
            if ((count % 100) == 0) println("Processed $count ports\r")
            val r = it.get()
            if (r.success) {
                println("Success: ${r.host}:${r.port}")
                successes.add(r)
            }
        }
        println("Done running, successes: " + successes)
    }
}

fun main(args: Array<String>) {
    HostScanner().scan()
}
