package com.goldilion.familymultiads;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.applovin.adview.AppLovinAdView;
import com.applovin.mediation.AppLovinExtras;
import com.applovin.mediation.ApplovinAdapter;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinSdkUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.logger.IronSourceError;

public class AndroAdsBanner {
    public static MaxAdView adViewMax;
    public static AdView adViewAdmob;
    public static AppLovinAdView adViewDiscovery;
    public static IronSourceBannerLayout adViewIron;

    public static void SmallBanner(Activity activity, RelativeLayout layAds, String selectAds, String idBanner, String Hpk1,
                                   String Hpk2, String Hpk3, String Hpk4, String Hpk5) {
        switch (selectAds) {
            case "ADMOB":
                Bundle extras = new AppLovinExtras.Builder()
                        .setMuteAudio(true)
                        .build();
                AdRequest request = new AdRequest.Builder().addKeyword(Hpk1).addKeyword(Hpk2)
                        .addKeyword(Hpk3).addKeyword(Hpk4).addKeyword(Hpk5)
                        .addNetworkExtrasBundle(ApplovinAdapter.class, extras)
                        .build();
                adViewAdmob = new AdView(activity);
                adViewAdmob.setAdUnitId(idBanner);
                layAds.addView(adViewAdmob);
                AdSize adSize = getAdSize(activity);
                adViewAdmob.setAdSize(adSize);
                adViewAdmob.loadAd(request);
                break;

            case "APPLOVIN-M":
                adViewMax = new MaxAdView(idBanner, activity);
                adViewMax.stopAutoRefresh();
                final boolean isTablet = AppLovinSdkUtils.isTablet(activity);
                final int heightPx = AppLovinSdkUtils.dpToPx(activity, isTablet ? 90 : 50);
                adViewMax.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx));
                layAds.addView(adViewMax);
                adViewMax.loadAd();
                break;

            case "IRON":
                adViewIron = IronSource.createBanner(activity, ISBannerSize.BANNER);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                layAds.addView(adViewIron, 0, layoutParams);
                IronSource.loadBanner(adViewIron, idBanner);
                break;

            case "APPLOVIN-D":
                AdRequest.Builder builder = new AdRequest.Builder().addKeyword(Hpk1).addKeyword(Hpk2)
                        .addKeyword(Hpk3).addKeyword(Hpk4).addKeyword(Hpk5);
                Bundle bannerExtras = new Bundle();
                bannerExtras.putString("zone_id", idBanner);
                builder.addCustomEventExtrasBundle(AppLovinCustomEventBanner.class, bannerExtras);

                boolean isTablet3 = AppLovinSdkUtils.isTablet(activity);
                AppLovinAdSize adSize3 = isTablet3 ? AppLovinAdSize.LEADER : AppLovinAdSize.BANNER;
                adViewDiscovery = new AppLovinAdView(adSize3, activity);
                layAds.addView(adViewDiscovery);
                adViewDiscovery.loadNextAd();

                break;
        }

    }

    public static void SmallBannerAdmob(Activity activity, RelativeLayout layAds, String selectAdsBackup, String idBanner, String idBannerBackup,String Hpk1,
                                        String Hpk2, String Hpk3, String Hpk4, String Hpk5) {
        Bundle extras = new AppLovinExtras.Builder()
                .setMuteAudio(true)
                .build();
        AdRequest request = new AdRequest.Builder().addKeyword(Hpk1).addKeyword(Hpk2)
                .addKeyword(Hpk3).addKeyword(Hpk4).addKeyword(Hpk5)
                .addNetworkExtrasBundle(ApplovinAdapter.class, extras)
                .build();
        adViewAdmob = new AdView(activity);
        adViewAdmob.setAdUnitId(idBanner);
        layAds.addView(adViewAdmob);
        AdSize adSize = getAdSize(activity);
        adViewAdmob.setAdSize(adSize);
        adViewAdmob.loadAd(request);
        adViewAdmob.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                switch (selectAdsBackup) {
                    case "APPLOVIN-M":
                        if (adViewMax != null) {
                            adViewMax.destroy();
                        }
                        break;
                    case "IRON":
                        if (adViewIron != null) {
                            adViewIron.isDestroyed();
                        }
                        break;
                    case "APPLOVIN-D":
                        if (adViewDiscovery != null) {
                            adViewDiscovery.destroy();
                        }
                        break;
                }
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                switch (selectAdsBackup) {
                    case "APPLOVIN-M":
                        adViewMax = new MaxAdView(idBannerBackup, activity);
                        adViewMax.stopAutoRefresh();
                        final boolean isTablet = AppLovinSdkUtils.isTablet(activity);
                        final int heightPx = AppLovinSdkUtils.dpToPx(activity, isTablet ? 90 : 50);
                        adViewMax.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx));
                        layAds.addView(adViewMax);
                        adViewMax.loadAd();
                        break;
                    case "IRON":
                        adViewIron = IronSource.createBanner(activity, ISBannerSize.BANNER);
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                        layAds.addView(adViewIron, 0, layoutParams);
                        IronSource.loadBanner(adViewIron, idBannerBackup);
                        break;
                    case "APPLOVIN-D":
                        AdRequest.Builder builder = new AdRequest.Builder().addKeyword(Hpk1).addKeyword(Hpk2)
                                .addKeyword(Hpk3).addKeyword(Hpk4).addKeyword(Hpk5);
                        Bundle bannerExtras = new Bundle();
                        bannerExtras.putString("zone_id", idBannerBackup);
                        builder.addCustomEventExtrasBundle(AppLovinCustomEventBanner.class, bannerExtras);

                        boolean isTablet2 = AppLovinSdkUtils.isTablet(activity);
                        AppLovinAdSize adSize = isTablet2 ? AppLovinAdSize.LEADER : AppLovinAdSize.BANNER;
                        adViewDiscovery = new AppLovinAdView(adSize, activity);
                        layAds.addView(adViewDiscovery);
                        adViewDiscovery.loadNextAd();
                        break;
                }
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });


    }

    public static void SmallBannerApplovinDisHPK(Activity activity, RelativeLayout layAds, String selectAdsBackup, String idBanner, String idBannerBackup, String HPK1, String HPK2, String HPK3, String HPK4, String HPK5) {
        AdRequest.Builder builder = new AdRequest.Builder().addKeyword(HPK1).addKeyword(HPK2).addKeyword(HPK3).addKeyword(HPK4).addKeyword(HPK5);
        Bundle bannerExtras = new Bundle();
        bannerExtras.putString("zone_id", idBanner);
        builder.addCustomEventExtrasBundle(AppLovinCustomEventBanner.class, bannerExtras);
        boolean isTablet2 = AppLovinSdkUtils.isTablet(activity);
        AppLovinAdSize adSize = isTablet2 ? AppLovinAdSize.LEADER : AppLovinAdSize.BANNER;
        adViewDiscovery = new AppLovinAdView(adSize, activity);
        AppLovinAdLoadListener loadListener = new AppLovinAdLoadListener() {
            @Override
            public void adReceived(AppLovinAd ad) {
                switch (selectAdsBackup) {
                    case "APPLOVIN-M":
                        if (adViewMax != null) {
                            adViewMax.destroy();
                        }
                        break;
                    case "IRON":
                        if (adViewIron != null) {
                            adViewIron.isDestroyed();
                        }
                        break;
                    case "ADMOB":
                        if (adViewAdmob != null) {
                            adViewAdmob.destroy();
                        }
                        break;
                }
            }

            @Override
            public void failedToReceiveAd(int errorCode) {
                switch (selectAdsBackup) {
                    case "APPLOVIN-M":
                        adViewMax = new MaxAdView(idBannerBackup, activity);
                        adViewMax.stopAutoRefresh();
                        final boolean isTablet = AppLovinSdkUtils.isTablet(activity);
                        final int heightPx = AppLovinSdkUtils.dpToPx(activity, isTablet ? 90 : 50);
                        adViewMax.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx));
                        layAds.addView(adViewMax);
                        adViewMax.loadAd();
                        break;
                    case "IRON":
                        adViewIron = IronSource.createBanner(activity, ISBannerSize.BANNER);
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                        layAds.addView(adViewIron, 0, layoutParams);
                        IronSource.loadBanner(adViewIron, idBannerBackup);
                        break;
                    case "ADMOB":
                        Bundle extras = new AppLovinExtras.Builder()
                                .setMuteAudio(true)
                                .build();
                        AdRequest request = new AdRequest.Builder()
                                .addNetworkExtrasBundle(ApplovinAdapter.class, extras)
                                .build();
                        adViewAdmob = new AdView(activity);
                        adViewAdmob.setAdUnitId(idBannerBackup);
                        layAds.addView(adViewAdmob);
                        AdSize adSizeAdmob = getAdSize(activity);
                        adViewAdmob.setAdSize(adSizeAdmob);
                        adViewAdmob.loadAd(request);
                        break;

                }
            }
        };
        adViewDiscovery.setAdLoadListener(loadListener);
        layAds.addView(adViewDiscovery);
        adViewDiscovery.loadNextAd();

    }

    public static void SmallBannerApplovinDis(Activity activity, RelativeLayout layAds, String selectAdsBackup, String idBanner, String idBannerBackup) {

        AdRequest.Builder builder = new AdRequest.Builder();
        Bundle bannerExtras = new Bundle();
        bannerExtras.putString("zone_id", idBanner);
        builder.addCustomEventExtrasBundle(AppLovinCustomEventBanner.class, bannerExtras);
        boolean isTablet2 = AppLovinSdkUtils.isTablet(activity);
        AppLovinAdSize adSize = isTablet2 ? AppLovinAdSize.LEADER : AppLovinAdSize.BANNER;
        adViewDiscovery = new AppLovinAdView(adSize, activity);
        AppLovinAdLoadListener loadListener = new AppLovinAdLoadListener() {
            @Override
            public void adReceived(AppLovinAd ad) {
                switch (selectAdsBackup) {
                    case "APPLOVIN-M":
                        if (adViewMax != null) {
                            adViewMax.destroy();
                        }
                        break;
                    case "IRON":
                        if (adViewIron != null) {
                            adViewIron.isDestroyed();
                        }
                        break;
                    case "ADMOB":
                        if (adViewAdmob != null) {
                            adViewAdmob.destroy();
                        }
                        break;
                }
            }

            @Override
            public void failedToReceiveAd(int errorCode) {
                switch (selectAdsBackup) {
                    case "APPLOVIN-M":
                        adViewMax = new MaxAdView(idBannerBackup, activity);
                        adViewMax.stopAutoRefresh();
                        final boolean isTablet = AppLovinSdkUtils.isTablet(activity);
                        final int heightPx = AppLovinSdkUtils.dpToPx(activity, isTablet ? 90 : 50);
                        adViewMax.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx));
                        layAds.addView(adViewMax);
                        adViewMax.loadAd();
                        break;
                    case "IRON":
                        adViewIron = IronSource.createBanner(activity, ISBannerSize.BANNER);
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                        layAds.addView(adViewIron, 0, layoutParams);
                        IronSource.loadBanner(adViewIron, idBannerBackup);
                        break;
                    case "ADMOB":
                        Bundle extras = new AppLovinExtras.Builder()
                                .setMuteAudio(true)
                                .build();
                        AdRequest request = new AdRequest.Builder()
                                .addNetworkExtrasBundle(ApplovinAdapter.class, extras)
                                .build();
                        adViewAdmob = new AdView(activity);
                        adViewAdmob.setAdUnitId(idBannerBackup);
                        layAds.addView(adViewAdmob);
                        AdSize adSizeAdmob = getAdSize(activity);
                        adViewAdmob.setAdSize(adSizeAdmob);
                        adViewAdmob.loadAd(request);
                        break;
                }
            }
        };
        adViewDiscovery.setAdLoadListener(loadListener);
        layAds.addView(adViewDiscovery);
        adViewDiscovery.loadNextAd();

    }

    public static void SmallBannerApplovinMax(Activity activity, RelativeLayout layAds, String selectAdsBackup, String idBanner, String idBannerBackup) {

        adViewMax = new MaxAdView(idBanner, activity);
        adViewMax.stopAutoRefresh();
        MaxAdViewAdListener listener = new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd ad) {

            }

            @Override
            public void onAdCollapsed(MaxAd ad) {

            }

            @Override
            public void onAdLoaded(MaxAd ad) {
                switch (selectAdsBackup) {
                    case "APPLOVIN-D":
                        if (adViewDiscovery != null) {
                            adViewDiscovery.destroy();
                        }
                        break;
                    case "IRON":
                        if (adViewIron != null) {
                            adViewIron.isDestroyed();
                        }
                        break;
                    case "ADMOB":
                        if (adViewAdmob != null) {
                            adViewAdmob.destroy();
                        }
                        break;
                }
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {

            }

            @Override
            public void onAdHidden(MaxAd ad) {

            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                switch (selectAdsBackup) {
                    case "APPLOVIN-D":
                        AdRequest.Builder builder = new AdRequest.Builder();
                        Bundle bannerExtras = new Bundle();
                        bannerExtras.putString("zone_id", idBannerBackup);
                        builder.addCustomEventExtrasBundle(AppLovinCustomEventBanner.class, bannerExtras);
                        boolean isTablet2 = AppLovinSdkUtils.isTablet(activity);
                        AppLovinAdSize adSize = isTablet2 ? AppLovinAdSize.LEADER : AppLovinAdSize.BANNER;
                        adViewDiscovery = new AppLovinAdView(adSize, activity);
                        layAds.addView(adViewDiscovery);
                        adViewDiscovery.loadNextAd();
                        break;
                    case "IRON":
                        adViewIron = IronSource.createBanner(activity, ISBannerSize.BANNER);
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                        layAds.addView(adViewIron, 0, layoutParams);
                        IronSource.loadBanner(adViewIron, idBannerBackup);
                        break;
                    case "ADMOB":
                        Bundle extras = new AppLovinExtras.Builder()
                                .setMuteAudio(true)
                                .build();
                        AdRequest request = new AdRequest.Builder()
                                .addNetworkExtrasBundle(ApplovinAdapter.class, extras)
                                .build();
                        adViewAdmob = new AdView(activity);
                        adViewAdmob.setAdUnitId(idBannerBackup);
                        layAds.addView(adViewAdmob);
                        AdSize adSizeAdmob = getAdSize(activity);
                        adViewAdmob.setAdSize(adSizeAdmob);
                        adViewAdmob.loadAd(request);
                        break;
                }

            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {

            }
        };
        adViewMax.setListener(listener);
        final boolean isTablet = AppLovinSdkUtils.isTablet(activity);
        final int heightPx = AppLovinSdkUtils.dpToPx(activity, isTablet ? 90 : 50);
        adViewMax.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx));
        layAds.addView(adViewMax);
        adViewMax.loadAd();
    }

    public static void SmallBannerIron(Activity activity, RelativeLayout layAds, String selectAdsBackup, String idBanner, String idBannerBackup) {
        adViewIron = IronSource.createBanner(activity, ISBannerSize.BANNER);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        layAds.addView(adViewIron, 0, layoutParams);
        com.ironsource.mediationsdk.sdk.BannerListener listener = new com.ironsource.mediationsdk.sdk.BannerListener() {
            @Override
            public void onBannerAdLoaded() {
                switch (selectAdsBackup) {
                    case "APPLOVIN-D":
                        if (adViewDiscovery != null) {
                            adViewDiscovery.destroy();
                        }
                        break;
                    case "APPLOVIN-M":
                        if (adViewMax != null) {
                            adViewMax.destroy();
                        }
                        break;
                    case "ADMOB":
                        if (adViewAdmob != null) {
                            adViewAdmob.destroy();
                        }
                        break;
                }
            }

            @Override
            public void onBannerAdLoadFailed(IronSourceError ironSourceError) {
                switch (selectAdsBackup) {
                    case "APPLOVIN-D":
                        AdRequest.Builder builder = new AdRequest.Builder();
                        Bundle bannerExtras = new Bundle();
                        bannerExtras.putString("zone_id", idBannerBackup);
                        builder.addCustomEventExtrasBundle(AppLovinCustomEventBanner.class, bannerExtras);
                        boolean isTablet2 = AppLovinSdkUtils.isTablet(activity);
                        AppLovinAdSize adSize = isTablet2 ? AppLovinAdSize.LEADER : AppLovinAdSize.BANNER;
                        adViewDiscovery = new AppLovinAdView(adSize, activity);
                        layAds.addView(adViewDiscovery);
                        adViewDiscovery.loadNextAd();
                        break;
                    case "APPLOVIN-M":
                        adViewMax = new MaxAdView(idBannerBackup, activity);
                        adViewMax.stopAutoRefresh();
                        final boolean isTablet = AppLovinSdkUtils.isTablet(activity);
                        final int heightPx = AppLovinSdkUtils.dpToPx(activity, isTablet ? 90 : 50);
                        adViewMax.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx));
                        layAds.addView(adViewMax);
                        adViewMax.loadAd();
                        break;
                    case "ADMOB":
                        Bundle extras = new AppLovinExtras.Builder()
                                .setMuteAudio(true)
                                .build();
                        AdRequest request = new AdRequest.Builder()
                                .addNetworkExtrasBundle(ApplovinAdapter.class, extras)
                                .build();
                        adViewAdmob = new AdView(activity);
                        adViewAdmob.setAdUnitId(idBannerBackup);
                        layAds.addView(adViewAdmob);
                        AdSize adSizeAdmob = getAdSize(activity);
                        adViewAdmob.setAdSize(adSizeAdmob);
                        adViewAdmob.loadAd(request);
                        break;
                }
            }

            @Override
            public void onBannerAdClicked() {

            }

            @Override
            public void onBannerAdScreenPresented() {

            }

            @Override
            public void onBannerAdScreenDismissed() {

            }

            @Override
            public void onBannerAdLeftApplication() {

            }
        };
        adViewIron.setBannerListener(listener);
        IronSource.loadBanner(adViewIron, idBanner);
    }

    public static void MediumBanner(Activity activity, RelativeLayout layAds, String selectAds, String idBanner,String Hpk1, String Hpk2, String Hpk3, String Hpk4, String Hpk5) {
        switch (selectAds) {
            case "ADMOB": {
                Bundle extras = new AppLovinExtras.Builder()
                        .setMuteAudio(true)
                        .build();
                AdRequest request = new AdRequest.Builder().addKeyword(Hpk1).addKeyword(Hpk2)
                        .addKeyword(Hpk3).addKeyword(Hpk4).addKeyword(Hpk5)
                        .addNetworkExtrasBundle(ApplovinAdapter.class, extras)
                        .build();
                adViewAdmob = new AdView(activity);
                adViewAdmob.setAdUnitId(idBanner);
                layAds.addView(adViewAdmob);
                adViewAdmob.setAdSize(AdSize.MEDIUM_RECTANGLE);
                adViewAdmob.loadAd(request);
                break;
            }
            case "APPLOVIN-M": {
                adViewMax = new MaxAdView(idBanner, MaxAdFormat.MREC, activity);
                final int widthPx = AppLovinSdkUtils.dpToPx(activity, 300);
                final int heightPx = AppLovinSdkUtils.dpToPx(activity, 250);
                adViewMax.setLayoutParams(new ConstraintLayout.LayoutParams(widthPx, heightPx));
                layAds.addView(adViewMax);
                adViewMax.loadAd();
                break;
            }
            case "IRON":
                adViewIron = IronSource.createBanner(activity, ISBannerSize.RECTANGLE);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                layAds.addView(adViewIron, 0, layoutParams);
                IronSource.loadBanner(adViewIron, idBanner);
                break;
            case "APPLOVIN-D":
                AdRequest.Builder builder = new AdRequest.Builder().addKeyword(Hpk1).addKeyword(Hpk2)
                        .addKeyword(Hpk3).addKeyword(Hpk4).addKeyword(Hpk5);
                Bundle bannerExtras = new Bundle();
                bannerExtras.putString("zone_id", idBanner);
                builder.addCustomEventExtrasBundle(AppLovinCustomEventBanner.class, bannerExtras);

                boolean isTablet2 = AppLovinSdkUtils.isTablet(activity);
                AppLovinAdSize adSize = isTablet2 ? AppLovinAdSize.LEADER : AppLovinAdSize.BANNER;
                adViewDiscovery = new AppLovinAdView(adSize, activity);
                layAds.addView(adViewDiscovery);
                adViewDiscovery.loadNextAd();
                break;
        }
    }

    public static void MediumBannerAdmob(Activity activity, RelativeLayout layAds, String selectAdsBackup, String idBanner, String idBannerBackup, String Hpk1, String Hpk2, String Hpk3, String Hpk4, String Hpk5) {
        Bundle extras = new AppLovinExtras.Builder()
                .setMuteAudio(true)
                .build();
        AdRequest request = new AdRequest.Builder().addKeyword(Hpk1).addKeyword(Hpk2)
                .addKeyword(Hpk3).addKeyword(Hpk4).addKeyword(Hpk5)
                .addNetworkExtrasBundle(ApplovinAdapter.class, extras)
                .build();
        AdView adView;
        adView = new AdView(activity);
        adView.setAdUnitId(idBanner);
        layAds.addView(adView);
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.loadAd(request);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                switch (selectAdsBackup) {
                    case "APPLOVIN-M": {
                        adViewMax = new MaxAdView(idBannerBackup, MaxAdFormat.MREC, activity);
                        final int widthPx = AppLovinSdkUtils.dpToPx(activity, 300);
                        final int heightPx = AppLovinSdkUtils.dpToPx(activity, 250);
                        adViewMax.setLayoutParams(new ConstraintLayout.LayoutParams(widthPx, heightPx));
                        layAds.addView(adViewMax);
                        adViewMax.loadAd();
                        break;
                    }
                    case "IRON":
                        adViewIron = IronSource.createBanner(activity, ISBannerSize.RECTANGLE);
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                        layAds.addView(adViewIron, 0, layoutParams);
                        IronSource.loadBanner(adViewIron, idBannerBackup);
                        break;
                    case "APPLOVIN-D":
                        AdRequest.Builder builder = new AdRequest.Builder().addKeyword(Hpk1).addKeyword(Hpk2)
                                .addKeyword(Hpk3).addKeyword(Hpk4).addKeyword(Hpk5);
                        Bundle bannerExtras = new Bundle();
                        bannerExtras.putString("zone_id", idBannerBackup);
                        builder.addCustomEventExtrasBundle(AppLovinCustomEventBanner.class, bannerExtras);

                        boolean isTablet2 = AppLovinSdkUtils.isTablet(activity);
                        AppLovinAdSize adSize = isTablet2 ? AppLovinAdSize.LEADER : AppLovinAdSize.BANNER;
                        adViewDiscovery = new AppLovinAdView(adSize, activity);
                        layAds.addView(adViewDiscovery);
                        adViewDiscovery.loadNextAd();
                        break;
                }
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

    }

    private static AdSize getAdSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

}