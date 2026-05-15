package com.gfbdev.jogoDaVelha.presentation.ads

import android.app.Activity
import com.gfbdev.jogoDaVelha.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.util.concurrent.TimeUnit

class AdManager(private val activity: Activity) {
    private var interstitialAd: InterstitialAd? = null
    private var isAdLoading = false
    private var lastAdShownTime = 0L
    private val adLoadInterval = TimeUnit.MINUTES.toMillis(2)

    private val adUnitId: String by lazy {
        activity.getString(R.string.AD_INTERSTICIAL_KEY)
    }

    fun showInterstitialAd(onAdDismissed: () -> Unit) {
        val currentTime = System.currentTimeMillis()
        
        // Regra de negócio do anúncio: tempo e disponibilidade
        if (currentTime - lastAdShownTime >= adLoadInterval && !isAdLoading) {
            loadAndShow(onAdDismissed)
        } else {
            onAdDismissed()
        }
    }

    private fun loadAndShow(onAdDismissed: () -> Unit) {
        isAdLoading = true
        val adRequest = AdRequest.Builder().build()
        
        InterstitialAd.load(activity, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                isAdLoading = false
                interstitialAd = ad
                setupAdCallback(onAdDismissed)
                interstitialAd?.show(activity)
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                isAdLoading = false
                interstitialAd = null
                onAdDismissed()
            }
        })
    }

    private fun setupAdCallback(onAdDismissed: () -> Unit) {
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                lastAdShownTime = System.currentTimeMillis()
                interstitialAd = null
                onAdDismissed()
            }
            override fun onAdFailedToShowFullScreenContent(error: com.google.android.gms.ads.AdError) {
                interstitialAd = null
                onAdDismissed()
            }
        }
    }
}
