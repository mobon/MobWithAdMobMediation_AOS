package com.hjlee.admopmediationexample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.mobwith.admopmediation.callback.AdmobAdListener
import com.mobwith.admopmediation.ui.AdmobBannerView
import com.mobwith.admopmediation.ui.AdmobInterstitialView
import com.mobwith.admopmediation.ui.AdmobNativeView
import com.mobwith.admopmediation.ui.AdmobRewardedView
import com.mobwith.manager.nativeadview.NativeAdViewItem

class MainActivity: AppCompatActivity(){

    private lateinit var admobBannerView: AdmobBannerView
    private lateinit var admobInterstitialView: AdmobInterstitialView
    private lateinit var admobRewardedView: AdmobRewardedView
    private lateinit var admobNativeView: AdmobNativeView
    private lateinit var containerView: FrameLayout

    private val TAG = "AdmobMediation";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val content = this.window.decorView.findViewById<View?>(android.R.id.content)

        WindowCompat.setDecorFitsSystemWindows(this.window,
            false
        )

        ViewCompat.setOnApplyWindowInsetsListener(content!!
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

        loadInterstitialView()
        loadRewardedView()

        findViewById<View>(R.id.btnBanner50).setOnClickListener {
            loadBannerView("ca-app-pub-7431155442526188/8122154329")
        }

        findViewById<View>(R.id.btnBanner100).setOnClickListener {
            loadBannerView("ca-app-pub-7431155442526188/1524398216")
        }

        findViewById<View>(R.id.btnInterstitial).setOnClickListener {
            if (admobInterstitialView.isLoaded()) {
                admobInterstitialView.show()
            } else {
                admobInterstitialView.loadInterstitialAd()
            }
        }

        findViewById<View>(R.id.btnRewarded).setOnClickListener {
            if (admobRewardedView.isLoaded()) {
                admobRewardedView.show { rewardItem ->
                    Log.d("AdmobMediation","reward 지급 : ${rewardItem.type}");
                }
            } else {
                admobRewardedView.loadRewardedAd()
            }
        }

        findViewById<View>(R.id.btnNative).setOnClickListener {
            loadNativeView()
        }
    }

    private fun loadBannerView(unitId: String) {
        admobBannerView = AdmobBannerView(this).apply {
            setUnitId(unitId)
            setAdListener(object : AdListener() {

                override fun onAdClicked() {
                    super.onAdClicked()
                }

                override fun onAdClosed() {
                    super.onAdClosed()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    containerView.removeAllViews()
                    containerView.addView(this@apply)
                }

                override fun onAdOpened() {
                    super.onAdOpened()
                }

                override fun onAdSwipeGestureClicked() {
                    super.onAdSwipeGestureClicked()
                }
            })
            loadAd()
        }
    }

    private fun loadInterstitialView() {
        admobInterstitialView = AdmobInterstitialView(
            this,
            object : FullScreenContentCallback() {

                override fun onAdClicked() {
                    super.onAdClicked()
                    Log.d(TAG,"onAdClicked")
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    Log.d(TAG,"onAdDismissedFullScreenContent")
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    super.onAdFailedToShowFullScreenContent(adError)
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                    Log.d(TAG,"onAdShowedFullScreenContent")
                }
            }
        ).apply {
            setUnitId("ca-app-pub-7431155442526188/1337606744")

            setAdmobAdListener(object : AdmobAdListener {
                override fun onAdLoaded() {
                    Toast.makeText(this@MainActivity, "전면 광고 로드 성공", Toast.LENGTH_SHORT).show()
                }

                override fun onAdFailedToLoad(errorMessage: String) {
                    Toast.makeText(
                        this@MainActivity,
                        "전면 광고 로드 실패 - $errorMessage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun loadRewardedView() {
        admobRewardedView = AdmobRewardedView(this).apply {
            setUnitId("ca-app-pub-7431155442526188/4026175800")

            setAdmobAdListener(object : AdmobAdListener {
                override fun onAdLoaded() {
                    Toast.makeText(this@MainActivity, "리워드 광고 로드 성공", Toast.LENGTH_SHORT).show()
                }

                override fun onAdFailedToLoad(errorMessage: String) {
                    Toast.makeText(
                        this@MainActivity,
                        "리워드 광고 로드 실패$errorMessage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun loadNativeView() {
        admobNativeView = AdmobNativeView(this).apply {
            setNativeAdViewItem(
                NativeAdViewItem(
                    this@MainActivity,
                    containerView,
                    R.layout.custom_native_ad_view,
                    R.id.mediaContainerView,
                    R.id.imageViewAD,
                    R.id.imageViewLogo,
                    R.id.textViewTitle,
                    R.id.textViewDesc,
                    R.id.buttonGo,
                    R.id.infoViewLayout,
                    R.id.imageViewInfo
                )
            )
            setUnitId("ca-app-pub-7431155442526188/5946951509")
            loadNativeAd()
        }
    }
}