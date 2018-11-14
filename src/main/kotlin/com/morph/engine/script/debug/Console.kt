package com.morph.engine.script.debug

import com.morph.engine.core.Game
import com.morph.engine.util.ScriptUtils
import java.io.OutputStream
import java.io.PrintStream
import java.util.*

/**
 * Created on 11/24/2017.
 */
class Console(private val type: Console.ScriptType, game: Game) {
    val lastLine: String
        get() {
            return currentLine
        }

    private class ConsoleOutputStream : OutputStream() {
        override fun write(buffer: ByteArray, offset: Int, length: Int) {
            val text = String(buffer, offset, length)
            Console.print(text)
        }

        override fun write(b: Int) {
            write(byteArrayOf(b.toByte()), 0, 1)
        }
    }

    class ConsolePrintStream private constructor(out: ConsoleOutputStream) : PrintStream(out) {
        override fun println() {
            super.println()
            Console.newLine()
        }

        override fun println(x: Boolean) {
            super.println(x)
            Console.newLine()
        }

        override fun println(x: Char) {
            super.println(x)
            Console.newLine()
        }

        override fun println(x: Int) {
            super.println(x)
            Console.newLine()
        }

        override fun println(x: Long) {
            super.println(x)
            Console.newLine()
        }

        override fun println(x: Float) {
            super.println(x)
            Console.newLine()
        }

        override fun println(x: Double) {
            super.println(x)
            Console.newLine()
        }

        override fun println(x: CharArray) {
            super.println(x)
            Console.newLine()
        }

        override fun println(x: String) {
            super.println(x)
            Console.newLine()
        }

        override fun println(x: Any?) {
            super.println(x)
            Console.newLine()
        }
    }

    enum class ScriptType {
        KOTLIN, PYTHON, MULTI
    }

    init {
        Console.game = game
    }

    fun readIn(line: String) {
        runLine(line)
    }

    fun getText(): String {
        return Console.text
    }

    private fun runLine(line: String) {
        when (type) {
            Console.ScriptType.MULTI -> {
                val parts = line.split(":").dropLastWhile { it.isEmpty() }.toTypedArray()
                parts[1] = parts[1].trim { it <= ' ' }
                ScriptUtils.readScriptAsync(parts[1], parts[0], this).subscribe({ }, { Console.out.println("Script Error: Input script malformed") })
            }
            Console.ScriptType.KOTLIN -> ScriptUtils.readScriptAsync(line, "kts", this).subscribe()
            Console.ScriptType.PYTHON -> ScriptUtils.readScriptAsync(line, "py", this).subscribe({ }, { Console.out.println("Script Error: Input script malformed") })
        }
    }

    fun getGame(): Game? {
        return game
    }

    fun clear() {
        Console.text = ""
        Console.currentLine = ""
    }

    companion object {
        private var text = ""
        private var currentLine = ""
        private var game: Game? = null

        private val outBytes = ConsoleOutputStream()
        private val errBytes = ConsoleOutputStream()

        @JvmStatic val out = PrintStream(outBytes)
        @JvmStatic val err = PrintStream(errBytes)

        private fun print(line: String) {
            Console.currentLine = line
            Console.text += line
        }

        private fun newLine() {
            Console.currentLine = "\n"
            Console.text += "\n"
        }
    }
}
