package com.olegel.androidndkqt

import android.content.Intent
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log

class MediaCallBack: MediaSessionCompat.Callback() {
    private val TAG = MediaCallBack::class.java.simpleName
    override fun onPlay() {
        super.onPlay()
        Log.d(TAG,"play")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"stop")
    }

    override fun onSkipToNext() {
        super.onSkipToNext()
        Log.d(TAG,"skip")
    }

    override fun onPlayFromSearch(query: String?, extras: Bundle?) {
        super.onPlayFromSearch(query, extras)
        Log.d(TAG,"play from search")
    }

    override fun onMediaButtonEvent(mediaButtonEvent: Intent?): Boolean {
        return super.onMediaButtonEvent(mediaButtonEvent)
        Log.d(TAG,"event")
    }

    override fun onPrepare() {
        super.onPrepare()
        Log.d(TAG,"prepare")
    }
}