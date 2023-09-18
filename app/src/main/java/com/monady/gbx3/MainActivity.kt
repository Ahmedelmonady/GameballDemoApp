package com.monady.gbx3

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gameball.gameball.GameballApp
import com.gameball.gameball.model.request.Event
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

        //Can be used without initializing GameballApp -> Can be called anywhere.
        var referralCode = GameballApp.getReferralCodeManually(intent)

        binding.btnWidget.text = "Show Widget"
        binding.btnRegisterAuto.text = "Register Player Auto"
        binding.btnRegisterManual.text = "Register Player Manual"

        var apiKey = ""

        val gameballApp = GameballApp.getInstance(applicationContext)

        gameballApp
            .init(apiKey, R.mipmap.ic_launcher, "en", "platform", "shop")

        var playerUniqueId = ""

        gameballApp.initializeFirebase()

        val playerAttributes = PlayerAttributes.Builder()
            .withDisplayName("Monady")
            .build()

        //Registers New Player, auto detects the Referral Code if available
        binding.btnRegisterAuto.setOnClickListener{
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

        /*
            Registers New Player, using the previously retrieved code
            If 'referralCode' is set to Null -> registration continues without any referral code
            If 'referralCode' is not Null -> registration will include the referral code
         */
        binding.btnRegisterManual.setOnClickListener{
            playerUniqueId = binding.playerUniqueIdInput.text.toString()

            if(!playerUniqueId.isNullOrEmpty()){
                gameballApp.registerPlayer(playerUniqueId, playerAttributes, referralCode, object:
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

        //Shows the Player Widget
        binding.btnWidget.setOnClickListener{
            playerUniqueId = binding.playerUniqueIdInput.text.toString()

            if(!playerUniqueId.isNullOrEmpty()){
                gameballApp.showProfile(this, playerUniqueId, "details_referral", true)
            }
        }

        /*To send an Event*/

        var eventBody = Event.Builder()
            .AddEventName("Test Event")
            .AddUniquePlayerId(playerUniqueId)
            .AddEmail("a@b.c")
            .build()

        gameballApp.sendEvent(eventBody, object : Callback<Boolean?> {
            override fun onSuccess(aBoolean: Boolean?) {
                aBoolean?.let {
                    if(!it) {
                        // TODO Handle on request Failed
                    }
                    else{
                        // TODO Handle on request Success
                    }
                }
            }

            override fun onError(e: Throwable) {
                // TODO Handle on Error result
            }
        })
    }
}