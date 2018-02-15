package com.shane.piglatin

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment() {
    val constanants = "bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        english_edit_text.afterTextChanged { translateInput(it) }
    }

    private fun translateInput(text: String) {
        Log.i("MainActivityFragment", "to translate: " + text)
        val translatedText = convertSentenceToPigLatin(text)
        translated_text.text = translatedText
    }

    private fun convertSentenceToPigLatin(sentence: String) : String {
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

    private fun convertWordToPigLatin(word: String): String {
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

        return result.toLowerCase()
    }
}
