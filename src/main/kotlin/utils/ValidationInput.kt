package utils

import utils.ScannerInput.readNextInt
import java.util.Scanner

object ValidationInputInput {

    @JvmStatic
    fun readValidCategory(prompt: String?): String {
        print(prompt)
        var input = Scanner(System.`in`).nextLine()
        do {
            if (CategoryUtility.isValidCategory(input))
                return input
            else {
                print("Invalid category $input.  Please try again: ")
                input = Scanner(System.`in`).nextLine()
            }
        } while (true)
    }

    @JvmStatic
    fun readValidPriority(prompt: String?): Int {
        var input = readNextInt(prompt)
        do {
            if (Utilities.validRange(input, 1, 5))
                return input
            else {
                print("Invalid priority $input.")
                input = readNextInt(prompt)
            }
        } while (true)
    }
}