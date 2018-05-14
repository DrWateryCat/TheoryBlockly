package frc.team2186.robot.lib.blockly

import javax.script.ScriptEngineManager

typealias BlocklyCallback = (x: Double, y: Double, twist: Double, time: Double) -> Unit

class BlocklyInterpreter(val blockly: Blockly) {
    private val engine = ScriptEngineManager().getEngineByName("nashorn")

    init {
        engine.eval(javaClass.getResourceAsStream("js/BlocklyInit.js").bufferedReader())
    }

    private val bindings = engine.createBindings().apply {
        put("blockly", blockly)
    }

    fun recieveInstruction(instruction: String) = engine.eval(instruction)
}

class Blockly {
    private val callbacks = arrayListOf<BlocklyCallback>()
    fun callback(block: BlocklyCallback) {
        callbacks.add(block)
    }

    fun setSpeed(x: Double, y: Double, twist: Double, time: Double) {
        callbacks.forEach {
            it.invoke(x, y, twist, time)
        }
    }

    fun stop() = setSpeed(0.0, 0.0, 0.0, 0.0)
}