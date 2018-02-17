package com.shane.piglatin

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_main.*




class MainActivityFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        english_edit_text.afterTextChanged { handleTextChanged(it) }
        if (activity != null) {
            handleTextFromIntent(activity)
            copy_button.setOnClickListener({ copyTranslationToClipBoard() })
        }
    }

    private fun copyTranslationToClipBoard() {
        val text = translated_text.text
        activity.let {
            val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Pig Latin Translation", text)
            clipboard.primaryClip = clip
            Toast.makeText(activity, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleTextChanged(text: String) {
        translateInput(text)
        text.isEmpty().let { textNotPresent ->
            clear_button.visibility = if (textNotPresent) View.GONE else View.VISIBLE
            card_translation_result.visibility = if (textNotPresent) View.GONE else View.VISIBLE
        }
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
