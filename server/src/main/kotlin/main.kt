package com.racepartyapp.server

import com.racepartyapp.shared.HeartRate
import com.racepartyapp.shared.Location
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.html.respondHtml
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.script

fun main() {
    val host = "127.0.0.1"
    val port = 8080

    val server = embeddedServer(Netty, port = port) {
        install(DefaultHeaders)
        install(CallLogging)
        install(Compression)
        install(ContentNegotiation) {
            json()
        }
        install(Routing) {
            get("/") {
                call.respondHtml {
                    body {
                        h1 { +"Hello World" }
                        script(src = "/static/web.js") {}
                    }
                }
            }
            get("/race") {
                call.respond(Location(5.2, -47.23, 10.5f))
            }
            static("/static") {
                resource("web.js")
                resource("android-release-unsigned.apk")
            }
        }
    }

    server.start()

    val address = "http://$host:$port"
    println("=============================================")
    println("")
    println("Server started at $address")
    println("")
    println("Open the following in a browser")
    println("  $address")
    println("Heart rate: ${HeartRate(185)}")
}
