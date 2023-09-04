package com.monady.gbx3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.gameball.gameball.GameballApp
import com.gameball.gameball.model.response.PlayerAttributes
import com.gameball.gameball.model.response.PlayerRegisterResponse
import com.gameball.gameball.network.Callback
import com.monady.gbx3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnWidget.text = "Show Widget"
        binding.btnRegister.text = "Register Player"

        var apiKey = ""

        val gameballApp = GameballApp.getInstance(applicationContext)

        gameballApp
            .init(apiKey, R.mipmap.ic_launcher, "en", "platform", "shop")
        var playerUniqueId = ""

        gameballApp.initializeFirebase()

        val playerAttributes = PlayerAttributes.Builder()
            .withDisplayName("Monady")
            .build()

        binding.btnRegister.setOnClickListener{
            playerUniqueId = binding.playerUniqueIdInput.text.toString()

            if(!playerUniqueId.isNullOrEmpty()){
                gameballApp.registerPlayer(playerUniqueId, playerAttributes, this, intent, object:
                    Callback<PlayerRegisterResponse> {
                    override fun onSuccess(t: PlayerRegisterResponse?) {
                        t?.gameballId?.let { Log.d("DemoApp", "gameballId: ${it} ") }
                    }

                    override fun onError(e: Throwable?) {
                        Log.d("DemoApp", e.toString())
                    }
                })
            }
        }

        binding.btnWidget.setOnClickListener{
            playerUniqueId = binding.playerUniqueIdInput.text.toString()

            if(!playerUniqueId.isNullOrEmpty()){
                gameballApp.showProfile(this, playerUniqueId, "details_referral", false)
            }
        }
    }
}