package com.olegel.androidndkqt

import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import com.olegel.androidndkqt.databinding.ActivityMainBinding
import java.util.*


class MainActivity : BaseActivity(), TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var binding: ActivityMainBinding
    private val openTTSSettingsAction = "com.android.settings.TTS_SETTINGS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tts = TextToSpeech(this, this)
        // Example of a call to a native method
        binding.mainActivitySampleText.text = "${stringFromJNI()} ${myCustomString("Hi string")}"
        Handler().postDelayed({ setStr("JNI was called") }, 1000)
        binding.mainActivitySayBtn.setOnClickListener {
            //TODO Uncomment before commit
           // say(binding.mainActivityEditText.text.toString())
        }
        binding.mainActivitySettingsBtn.setOnClickListener {
            intent = Intent()
            intent.apply {
                action = openTTSSettingsAction
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        binding.mainActivitySampleText.text = intent?.action
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String
    external fun myCustomString(str: String): String
    external fun setStr(str: String)

    /**
     *Methods send and receive data structures to NDK
     */
    /* external fun setUser(user: User)
     external fun getUser(): User*/

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }

    /**
     * Method calls from C++ code
     */
    fun setDataFromJNI(jni: String) {
        binding.mainActivitySampleText.text = jni
        say(jni)
    }

    override fun onInit(p0: Int) {
        when {
            p0 == TextToSpeech.SUCCESS -> {
                tts.language = Locale.US
            }
            else -> {
                showLongToast("Text to speech was failed")
            }
        }
    }

    private fun say(message: String) {
        val bundle = Bundle()
        bundle.putInt(TextToSpeech.Engine.KEY_PARAM_VOLUME, AudioManager.ADJUST_LOWER)
        tts.speak(message, TextToSpeech.QUEUE_FLUSH, bundle, "111")
    }

    override fun onStop() {
        super.onStop()
        takeIf { tts.isSpeaking }.apply {
            tts.stop()
        }
    }
}
