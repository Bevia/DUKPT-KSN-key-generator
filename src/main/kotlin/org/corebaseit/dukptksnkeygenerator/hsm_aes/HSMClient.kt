package org.corebaseit.dukptksnkeygenerator.hsm_aes

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

class HSMClient {

    fun main() {
        //val simulator = DukptSimulator()
        //simulator.runSimulation(pin = "5678", pan = "4012888888881881")

        //If you’re using AESCMAC, don’t forget to initialize Bouncy Castle:
        Security.addProvider(BouncyCastleProvider())
        val simulator = DukptSimulator()
        simulator.runSimulation()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            HSMClient().main()
        }
    }
}