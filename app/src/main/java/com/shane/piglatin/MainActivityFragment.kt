package com.shane.piglatin

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*


class MainActivityFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        english_edit_text.afterTextChanged { translateInput(it) }

        if (activity != null) handleTextFromIntent(activity)
    }

    private fun handleTextFromIntent(activity: FragmentActivity?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.intent?.let {
                english_edit_text.setText(it.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT))
            }
        }
    }

    private fun translateInput(text: String) {
        val translatedText = PigLatin.convertSentenceToPigLatin(text)
        translated_text.text = translatedText
    }
}
