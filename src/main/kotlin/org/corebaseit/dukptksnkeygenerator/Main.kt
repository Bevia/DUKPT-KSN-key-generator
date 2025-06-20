package org.corebaseit.dukptksnkeygenerator

fun main() {
    val args = arrayOf(
        "-b", "0123456789ABCDEF0123456789ABCDEF",  // BDK value (16 bytes)
        "-k", "0123456789ABCDEF0123"               // KSN value (10 bytes)
    )
    DukptCliTool(args).run()
}