package frc.team2186.robot.lib.server

import com.google.gson.JsonObject
import edu.wpi.first.wpilibj.DriverStation
import frc.team2186.robot.Drive
import frc.team2186.robot.lib.addProperty
import frc.team2186.robot.lib.blockly.Blockly
import frc.team2186.robot.lib.blockly.BlocklyInterpreter
import io.ktor.network.sockets.*
import kotlinx.coroutines.experimental.io.readASCIILine
import kotlinx.coroutines.experimental.io.writeBytes
import kotlinx.coroutines.experimental.io.writeStringUtf8
import kotlinx.coroutines.experimental.launch
import java.net.InetSocketAddress

fun server(port: Int = 654) = launch {
    val blockly = Blockly()
    blockly.callback { x, y, twist, time ->
        if (DriverStation.getInstance().isAutonomous) {
            println("Moving for $time at speeds x: $x, y: $y, twist: $twist")
            Drive.xSpeed = x
            Drive.ySpeed = y
            Drive.twistSpeed = twist
        }
    }

    val interpreter = BlocklyInterpreter(blockly)

    val serverSocket = aSocket().tcp().bind(InetSocketAddress("127.0.0.1", port))
    while (true) {
        val client = serverSocket.accept()

        launch {
            println("Accepted connection from ${client.remoteAddress}")
            val input = client.openReadChannel()
            val output = client.openWriteChannel(autoFlush = true)

            while (client.isClosed.not()) {
                val line = input.readASCIILine()
                line?.let {
                    try {
                        interpreter.recieveInstruction(line)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        val errorJson = JsonObject().apply {
                            addProperty("error", e.message)
                            addProperty("stack_trace", e.stackTrace.map { it.toString() }.toTypedArray())
                        }
                        output.writeBytes(errorJson.toString())

                        client.awaitClosed()
                    }
                }

                output.writeStringUtf8(Drive.currentState.toString())
            }
        }
    }
}