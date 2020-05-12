@file:Suppress("UNUSED_VARIABLE", "RedundantSuspendModifier")

package com.racepartyapp.js

import com.racepartyapp.shared.HeartRate
import com.racepartyapp.shared.Location
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlin.browser.document

suspend fun main() {
    val client = HttpClient(Js) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    val response: Location = client.get("http://127.0.0.1:8080/race")
    client.close()

    document.bgColor = "red"
    document.body?.appendChild(document.createTextNode("${HeartRate(125)}, Race: ${response.accuracy}, Hello"))
}