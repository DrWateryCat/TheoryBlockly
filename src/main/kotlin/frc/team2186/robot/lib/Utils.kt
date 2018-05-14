package frc.team2186.robot.lib

import com.google.gson.JsonObject

fun JsonObject.addProperty(key: String, value: Array<String>) {
    val stringBuilder = StringBuilder()
    value.forEach { stringBuilder.appendln(it) }
    addProperty(key, stringBuilder.toString())
}