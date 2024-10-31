package com.example.project1763.activity

import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

import com.example.project1763.R
import com.example.project1763.activity.login.LoginActivity


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class IntroActivity : BaseActivity() {
    private var mediaPlayer: MediaPlayer? = null
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        CoroutineScope(SupervisorJob()).launch(Dispatchers.IO)  {
            val source = ImageDecoder.createSource(
                resources, R.drawable.breakrules
            )
            val drawable = ImageDecoder.decodeDrawable(source)

            val imageView = findViewById<ImageView>(R.id.image_view)
            imageView.post {
                imageView.setImageDrawable(drawable)
                (drawable as? AnimatedImageDrawable)?.start()
            }
        }
        CoroutineScope(SupervisorJob()).launch(Dispatchers.IO)  {
            val source1 = ImageDecoder.createSource(
                resources, R.drawable.shopgif
            )
            val drawable1 = ImageDecoder.decodeDrawable(source1)

            val imageView1 = findViewById<ImageView>(R.id.image_view1)
            imageView1.post {
                imageView1.setImageDrawable(drawable1)
                (drawable1 as? AnimatedImageDrawable)?.start()
            }
            // Phát âm thanh
            playSound()
            goToNextActivity()
        }
    }

    private fun playSound() {
        mediaPlayer = MediaPlayer.create(this, R.raw.mp3logo)
        mediaPlayer?.start()
    }


    private suspend fun goToNextActivity() {
        // Tạo Intent để chuyển sang màn hình mới
        val intent = Intent(/* packageContext = */ this,/* cls = */LoginActivity::class.java)
        delay(5000)
        startActivity(intent)
        finish() // Kết thúc Activity hiện tại nếu bạn không muốn quay lại nó khi nhấn nút back
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}
