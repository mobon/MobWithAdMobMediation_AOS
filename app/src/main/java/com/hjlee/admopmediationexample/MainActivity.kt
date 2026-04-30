package com.hjlee.admopmediationexample

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.mobwith.admobmediation.ui.AdmobNativeView
import com.mobwith.manager.LogPrint
import com.mobwith.manager.nativeadview.NativeAdViewItem

class MainActivity : AppCompatActivity() {

    private val banner50UnitId = "ca-app-pub-7431155442526188/8122154329"
    private val banner100UnitId = "ca-app-pub-7431155442526188/1524398216"
    private val interstitialUnitId = "ca-app-pub-7431155442526188/1337606744"
    private val rewardUnitId = "ca-app-pub-7431155442526188/4026175800"
    private val nativeUnitId = "ca-app-pub-7431155442526188/5946951509"
    private var bannerView: AdView? = null
    private var mInterstitialAd: InterstitialAd? = null
    private var mRewardedAd: RewardedAd? = null
    private var admobNativeView: AdmobNativeView? = null
    private lateinit var containerView: FrameLayout

    private val TAG = "AdmobMediation";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val content = this.window.decorView.findViewById<View?>(android.R.id.content)

        WindowCompat.setDecorFitsSystemWindows(
            this.window,
            false
        )

        ViewCompat.setOnApplyWindowInsetsListener(
            content!!
        ) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        containerView = findViewById(R.id.containerView)

        findViewById<Button>(R.id.btnBanner50).setOnClickListener { loadBannerView(banner50UnitId) }

        findViewById<Button>(R.id.btnBanner100).setOnClickListener { loadBannerView(banner100UnitId) }

        findViewById<Button>(R.id.btnInterstitial).setOnClickListener {
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(this@MainActivity)
            } else {
                loadInterstitialView()
            }
        }

        findViewById<Button>(R.id.btnRewarded).setOnClickListener {
            if (mRewardedAd != null) {
                mRewardedAd?.show(this@MainActivity) { rewardItem ->
                    LogPrint.d("리워드 지급 : " + rewardItem.type + " " + rewardItem.amount)
                    Toast.makeText(
                        this@MainActivity,
                        "리워드 지급 : " + rewardItem.type,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                loadRewardedView()
            }
        }

        findViewById<Button>(R.id.btnNative).setOnClickListener { loadNativeView() }
    }

    private fun loadBannerView(unitId: String) {
        bannerView = AdView(this)
        bannerView?.setAdSize(AdSize.BANNER)
        bannerView?.adUnitId = unitId
        bannerView?.adListener = object : AdListener() {
            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                containerView.removeAllViews()
                containerView.addView(bannerView)
            }

            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdImpression() {
                super.onAdImpression()
            }

            override fun onAdSwipeGestureClicked() {
                super.onAdSwipeGestureClicked()
            }
        }
        bannerView?.loadAd(AdRequest.Builder().build())
    }

    private fun loadInterstitialView() {
        InterstitialAd.load(
            this,
            interstitialUnitId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    mInterstitialAd?.fullScreenContentCallback = object :
                        FullScreenContentCallback() {
                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            super.onAdFailedToShowFullScreenContent(adError)
                        }

                        override fun onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent()
                        }

                        override fun onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent()
                        }

                        override fun onAdImpression() {
                            super.onAdImpression()
                        }

                        override fun onAdClicked() {
                            super.onAdClicked()
                        }
                    }
                    Toast.makeText(this@MainActivity, "전면 광고 로드 성공", Toast.LENGTH_SHORT).show()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    mInterstitialAd = null
                    Toast.makeText(
                        this@MainActivity,
                        "전면 광고 로드 실패 : " + loadAdError.getMessage(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun loadRewardedView() {
        RewardedAd.load(
            this,
            rewardUnitId,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    mRewardedAd = rewardedAd
                    Toast.makeText(this@MainActivity, "리워드 광고 로드 성공", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    mRewardedAd = null
                    Toast.makeText(
                        this@MainActivity,
                        "리워드 광고 로드 실패 : " + loadAdError.getMessage(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun loadNativeView() {
        admobNativeView = AdmobNativeView(
            this,
            nativeUnitId,
            NativeAdViewItem(
                this,
                findViewById(R.id.containerView),
                R.layout.custom_native_ad_view,
                R.id.mediaContainerView,
                R.id.imageViewAD,
                R.id.imageViewLogo,
                R.id.textViewTitle,
                R.id.textViewDesc,
                R.id.buttonGo,
                R.id.infoViewLayout,
                R.id.imageViewInfo
            ),
            object : AdListener() {
                override fun onAdClicked() {
                    super.onAdClicked()
                    LogPrint.d("onAdClicked")
                }

                override fun onAdClosed() {
                    super.onAdClosed()
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    LogPrint.d("onAdImpression")
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    LogPrint.d("onAdLoaded")
                }

                override fun onAdOpened() {
                    super.onAdOpened()
                    LogPrint.d("onAdOpened")
                }

                override fun onAdSwipeGestureClicked() {
                    super.onAdSwipeGestureClicked()
                }

                // The native ad load failed. Check the adError message for failure
                // reasons.
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    LogPrint.d("onAdFailedToLoad : " + adError.getMessage())
                }
            })
        admobNativeView?.loadNativeAd()
    }
}