package com.olegel.androidndkqt

import android.app.Activity
import android.app.PictureInPictureParams
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.support.v4.media.session.MediaSessionCompat
import android.view.KeyEvent
import androidx.annotation.RequiresApi
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.olegel.androidndkqt.databinding.ActivityAlarmBinding
import kotlinx.android.synthetic.main.activity_alarm.*

class AlarmActivity:BaseActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private var isInPipMode:Boolean = false
    private val mUrl = "http://109.68.41.148:1935/live/inter_2/playlist.m3u8"
    private lateinit var player : SimpleExoPlayer
    private var videoPosition:Long = 0L
    private var isPIPModeeEnabled:Boolean = true
    val ARG_VIDEO_POSITION = "VideoActivity.POSITION"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_alarm)
        intent.extras
    }

    override fun onStart() {
        super.onStart()

        player =  SimpleExoPlayer.Builder(this).build()

        playerView_step_video.player = player

        val dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, applicationInfo.loadLabel(packageManager).toString()))

        when (Util.inferContentType(Uri.parse(mUrl))) {
            C.TYPE_HLS -> {
                val mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(mUrl))
                player.prepare(mediaSource)
            }

            C.TYPE_OTHER -> {
                val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(mUrl))
                player.prepare(mediaSource)
            }

            else -> {
                //This is to catch SmoothStreaming and DASH types which are not supported currently
                setResult(Activity.RESULT_CANCELED)
              //  finish()
            }
        }

        var returnResultOnce:Boolean = true

        player.addListener(object : Player.EventListener{
            override fun onTimelineChanged(timeline: Timeline, manifest: Any?, reason: Int) {}

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}

            override fun onRepeatModeChanged(repeatMode: Int) {}

            override fun onPositionDiscontinuity(reason: Int) {}

            override fun onLoadingChanged(isLoading: Boolean) {}

            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray
            ) {}

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}

            override fun onPlayerError(error: ExoPlaybackException) {
                setResult(Activity.RESULT_CANCELED)
               // finishAndRemoveTask()
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if(playbackState == Player.STATE_READY && returnResultOnce){
                    setResult(Activity.RESULT_OK)
                    returnResultOnce = false
                }
            }

            override fun onSeekProcessed() {}
        })
        player.playWhenReady = true

        //Use Media Session Connector from the EXT library to enable MediaSession Controls in PIP.
        val mediaSession = MediaSessionCompat(this, packageName)
        //TODO Must add flags for sessions
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS or MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
        or MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS)
        val mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlayer(player)
        mediaSession.isActive = true
    }
    override fun onPause() {
        videoPosition = player.currentPosition
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if(videoPosition > 0L && !isInPipMode){
            player.seekTo(videoPosition)
        }
        //Makes sure that the media controls pop up on resuming and when going between PIP and non-PIP states.
        playerView_step_video.useController = true
    }

    override fun onStop() {
        super.onStop()
        playerView_step_video.player = null
        player.release()
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)) {
            finishAndRemoveTask()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
            enterPIPMode()
            // perform your desired action here

            // return 'true' to prevent further propagation of the key event
            return true;
        }

        // let the system handle all other key events
        return super.onKeyDown(keyCode, event)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState.apply {
            this.putLong(ARG_VIDEO_POSITION, player.currentPosition)
        }, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        videoPosition = savedInstanceState!!.getLong(ARG_VIDEO_POSITION)
    }

    override fun onBackPressed(){
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
            && isPIPModeeEnabled) {
            enterPIPMode()
        } else {
            super.onBackPressed()
        }
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration?) {
        if(newConfig !=null){
            videoPosition = player.currentPosition
            isInPipMode = !isInPictureInPictureMode
        }
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
    }

    //Called when the user touches the Home or Recents button to leave the app.
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        enterPIPMode()
    }

    @Suppress("DEPRECATION")
    fun enterPIPMode(){
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)) {
            videoPosition = player.currentPosition
            playerView_step_video.useController = false
                val params = PictureInPictureParams.Builder()
                this.enterPictureInPictureMode(params.build())
            Handler().postDelayed({checkPIPPermission()}, 30)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun checkPIPPermission(){
        isPIPModeeEnabled = isInPictureInPictureMode
        if(!isInPictureInPictureMode){
            onBackPressed()
        }
    }
}