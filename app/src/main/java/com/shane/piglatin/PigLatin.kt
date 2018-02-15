package com.shane.piglatin

class PigLatin {
    companion object {
        private val consonants = "bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ"

        fun convertSentenceToPigLatin(sentence: String) : String {
            if (sentence.length < 2) return sentence

            var result = ""
            sentence.split(" ").forEach { result = result + convertWordToPigLatin(it) + " " }
            return result
        }

        fun convertWordToPigLatin(word: String): String {
            if (word.isEmpty())
                return word

            val firstLetter = word[0]

            if (!consonants.contains(firstLetter + "")) {
                println(firstLetter)
                return word + "way"
            }

            var indexOfLastContiguousConstanant = 0

            while (indexOfLastContiguousConstanant + 1 < word.length
                    && consonants.contains(word[indexOfLastContiguousConstanant])) {
                indexOfLastContiguousConstanant += 1
            }

            val result = (word.substring(indexOfLastContiguousConstanant)
                    + word.substring(0, indexOfLastContiguousConstanant)
                    + "ay")

            return result
        }
    }
}