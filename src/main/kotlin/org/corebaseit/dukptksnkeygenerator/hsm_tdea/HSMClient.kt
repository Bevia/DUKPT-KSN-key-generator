package org.corebaseit.dukptksnkeygenerator.hsm_tdea

class HSMClient {

    fun main() {
        val simulator = DukptSimulator()
        //simulator.runSimulation()
        simulator.runSimulation(pin = "9876", pan = "4012888888881881")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            HSMClient().main()
        }
    }
}