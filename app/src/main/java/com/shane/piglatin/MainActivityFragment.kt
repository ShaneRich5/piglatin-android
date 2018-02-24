package com.shane.piglatin

import android.app.Activity.RESULT_OK
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*


class MainActivityFragment : Fragment(), TextToSpeech.OnInitListener {
    private val REQUEST_CODE_SPEECH_INPUT = 100

    lateinit var textToSpeech: TextToSpeech

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {

            val result = textToSpeech.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported")
            } else {
                raw_text_header.isEnabled = true
                translated_text_header.isEnabled = true
            }

        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textToSpeech = TextToSpeech(activity, this)

        english_edit_text.afterTextChanged { handleTextChanged(it) }
        if (activity != null) {
            handleTextFromIntent(activity)
            copy_button.setOnClickListener({ copyTranslationToClipBoard(activity as Context) })
            share_button.setOnClickListener({ shareTranslation(activity as Context) })
        }

        clear_button.setOnClickListener({ english_edit_text.setText("") })

        raw_text_header.setOnClickListener({
            val text = english_edit_text.text.toString()
            if ( ! text.isEmpty()) playAudioForText(text)
        })

        translated_text_header.setOnClickListener({
            val text = translated_text.text.toString()
            playAudioForText(text)
        })

        microphone_button.setOnClickListener({
            promptSpeechInput(activity as Context)
        })
    }

    private fun promptSpeechInput(context: Context) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt))

        intent.resolveActivity(context.packageManager)?.let {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
        } ?: run {
            Toast.makeText(context, getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_SPEECH_INPUT -> {
                if (resultCode == RESULT_OK) {
                    data?.let {
                        val result = it.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                        english_edit_text.setText(result[0])
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        textToSpeech.let {
            it.stop()
            it.shutdown()
        }
    }

    private fun playAudioForText(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null)
    }

    private fun shareTranslation(context: Context) {
        val text = translated_text.text
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, text)
        sendIntent.type = "text/plain"

        val packageManager = context.packageManager

        sendIntent.resolveActivity(packageManager)?.let {
            startActivity(Intent.createChooser(sendIntent, "Send pig latin to"))
        } ?: run {
            Toast.makeText(context, "Failed to share text with other apps.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun copyTranslationToClipBoard(context: Context) {
        val text = translated_text.text
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Pig Latin Translation", text)
        clipboard.primaryClip = clip
        Toast.makeText(activity, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
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
