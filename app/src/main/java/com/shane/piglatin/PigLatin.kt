package com.shane.piglatin

import android.util.Log

class PigLatin {
    companion object {
        val constanants = "bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ"

        fun convertSentenceToPigLatin(sentence: String) : String {
            if (sentence.length < 2) return sentence

            var result = ""
            val words = sentence.split(" ")
            Log.i("MainActivityFragment", "words: " + words)
            Log.i("MainActivityFragment", "words[0]: " + words[0])

            for (word in words) {
                result = result.plus(convertWordToPigLatin(word) + " ")

                Log.i("MainActivityFragment", "result before: " + result)
            }

            Log.i("MainActivityFragment", "result: " + result)

            return result
        }

        fun convertWordToPigLatin(word: String): String {
            if (word.isEmpty())
                return word

            val firstLetter = word[0]

            if (!constanants.contains(firstLetter + "")) {
                println(firstLetter)
                return word + "way"
            }

            var indexOfLastContiguousConstanant = 0

            while (indexOfLastContiguousConstanant + 1 < word.length
                    && constanants.contains(word[indexOfLastContiguousConstanant])) {
                indexOfLastContiguousConstanant += 1
            }

            val result = (word.substring(indexOfLastContiguousConstanant)
                    + word.substring(0, indexOfLastContiguousConstanant)
                    + "ay")

            return result
        }
    }
}