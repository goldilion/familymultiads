package com.goldilion.familymultiads;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.goldilion.familymultiads.config.AppLovinCustomEventBanner;
import com.goldilion.familymultiads.interfaces.banner.OnLoadBannerAdmob;
import com.goldilion.familymultiads.interfaces.banner.OnLoadBannerAlienMediation;
import com.goldilion.familymultiads.interfaces.banner.OnLoadBannerAlienView;
import com.goldilion.familymultiads.interfaces.banner.OnLoadBannerApplovinDiscovery;
import com.goldilion.familymultiads.interfaces.banner.OnLoadBannerApplovinMax;
import com.goldilion.familymultiads.interfaces.banner.OnLoadBannerFacebook;
import com.goldilion.familymultiads.interfaces.banner.OnLoadBannerGoogle;
import com.goldilion.familymultiads.interfaces.banner.OnLoadBannerIronSource;
import com.goldilion.familymultiads.interfaces.banner.OnLoadBannerStartApp;
import com.goldilion.sdkads.interfaces.OnLoadBannerMediation;
import com.goldilion.sdkads.interfaces.OnLoadBannerView;
import com.goldilion.sdkads.type.mediation.AlienMediationAds;
import com.goldilion.sdkads.type.view.AlienViewAds;
import com.applovin.adview.AppLovinAdView;
import com.applovin.mediation.MaxAd;
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
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.logger.IronSourceError;

public class AndroAdsBanner {
    public static MaxAdView adViewMax;
    public static AdView adViewAdmob;
    public static AdManagerAdView bannerGoogleAds;
    public static AppLovinAdView adViewDiscovery;
    public static IronSourceBannerLayout adViewIron;
//    public static com.facebook.ads.AdView adViewFAN;

    public static OnLoadBannerAdmob onLoadBannerAdmob;
    public static OnLoadBannerGoogle onLoadBannerGoogle;
    public static OnLoadBannerFacebook onLoadBannerFacebook;
    public static OnLoadBannerApplovinDiscovery onLoadBannerApplovinDiscovery;
    public static OnLoadBannerApplovinMax onLoadBannerApplovinMax;
    public static OnLoadBannerStartApp onLoadBannerStartApp;
    public static OnLoadBannerIronSource onLoadBannerIronSource;
    public static OnLoadBannerAlienView onLoadBannerAlienView;
    public static OnLoadBannerAlienMediation onLoadBannerAlienMediation;

    public static void SmallBannerGoogleAds(Activity activity, RelativeLayout layAds, String selectAdsBackup, String idBanner, String idBannerBackup) {

        AdManagerAdRequest adRequest =
                new AdManagerAdRequest.Builder()
                        .build();

        bannerGoogleAds = new AdManagerAdView(activity);
        bannerGoogleAds.setAdUnitId(idBanner);
        layAds.addView(bannerGoogleAds);
        AdSize adaptiveSize = getAdSize(activity);
        bannerGoogleAds.setAdSize(adaptiveSize);
        bannerGoogleAds.loadAd(adRequest);
        bannerGoogleAds.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (onLoadBannerGoogle!=null){
                    onLoadBannerGoogle.onAdLoaded();
                }
                switch (selectAdsBackup) {
                    case "APPLOVIN-M":
                        if (adViewMax != null) {
                            adViewMax.destroy();
                        }
                        break;
                    case "MOPUB":
                    case "UNITY":

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
//                    case "FACEBOOK":
//                        if (adViewFAN != null) {
//                            adViewFAN.destroy();
//                        }
//                        break;
                }
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                if (onLoadBannerGoogle!=null){
                    onLoadBannerGoogle.onAdFailedToLoad("");
                }
                switch (selectAdsBackup) {
                    case "APPLOVIN-M":
                        adViewMax = new MaxAdView(idBannerBackup, activity);

                        final boolean isTablet = AppLovinSdkUtils.isTablet(activity);
                        final int heightPx = AppLovinSdkUtils.dpToPx(activity, isTablet ? 90 : 50);
                        adViewMax.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx));
                        layAds.addView(adViewMax);
                        adViewMax.loadAd();
                        break;
                    case "MOPUB":
                    case "UNITY":

                        break;
                    case "IRON":
                        adViewIron = IronSource.createBanner(activity, ISBannerSize.BANNER);
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                        layAds.addView(adViewIron, 0, layoutParams);
                        IronSource.loadBanner(adViewIron, idBannerBackup);
                        break;
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
                    case "ALIEN-V":
                        AlienViewAds.Banner(activity,layAds,idBannerBackup);
                        break;
                    case "ALIEN-M":
                        AlienMediationAds.SmallBanner(activity,layAds,idBannerBackup);
                        break;
                }
            }

            @Override
            public void onAdOpened() {
                if (onLoadBannerGoogle!=null){
                    onLoadBannerGoogle.onAdOpened();
                }
            }

            @Override
            public void onAdClicked() {
                if (onLoadBannerGoogle!=null){
                    onLoadBannerGoogle.onAdClicked();
                }
            }

            @Override
            public void onAdClosed() {
                if (onLoadBannerGoogle!=null){
                    onLoadBannerGoogle.onAdClosed();
                }
            }
        });


    }

    public static void SmallBannerAdmob(Activity activity, RelativeLayout layAds, String selectAdsBackup, String idBanner, String idBannerBackup, String Hpk1,
                                        String Hpk2, String Hpk3, String Hpk4, String Hpk5) {
//        Bundle extras = new FacebookExtras()
//                .setNativeBanner(true)
//                .build();
        AdRequest request = new AdRequest.Builder().addKeyword(Hpk1).addKeyword(Hpk2)
                .addKeyword(Hpk3).addKeyword(Hpk4).addKeyword(Hpk5)
                //.addNetworkExtrasBundle(FacebookAdapter.class, extras)
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
                if (onLoadBannerAdmob!=null){
                    onLoadBannerAdmob.onAdLoaded();
                }
                switch (selectAdsBackup) {
                    case "APPLOVIN-M":
                        if (adViewMax != null) {
                            adViewMax.destroy();
                        }
                        break;
                    case "MOPUB":
                    case "UNITY":

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
//                    case "FACEBOOK":
//                        if (adViewFAN != null) {
//                            adViewFAN.destroy();
//                        }
//                        break;
                }
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                if (onLoadBannerAdmob!=null){
                    onLoadBannerAdmob.onAdFailedToLoad("");
                }
                switch (selectAdsBackup) {
                    case "APPLOVIN-M":
                        adViewMax = new MaxAdView(idBannerBackup, activity);

                        final boolean isTablet = AppLovinSdkUtils.isTablet(activity);
                        final int heightPx = AppLovinSdkUtils.dpToPx(activity, isTablet ? 90 : 50);
                        adViewMax.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx));
                        layAds.addView(adViewMax);
                        adViewMax.loadAd();
                        break;
                    case "MOPUB":
                    case "UNITY":

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
                    case "ALIEN-V":
                        AlienViewAds.Banner(activity,layAds,idBannerBackup);
                        break;
                    case "ALIEN-M":
                        AlienMediationAds.SmallBanner(activity,layAds,idBannerBackup);
                        break;
                }
            }

            @Override
            public void onAdOpened() {
                if (onLoadBannerAdmob!=null){
                    onLoadBannerAdmob.onAdOpened();
                }
            }

            @Override
            public void onAdClicked() {
                if (onLoadBannerAdmob!=null){
                    onLoadBannerAdmob.onAdClicked();
                }
            }

            @Override
            public void onAdClosed() {
                if (onLoadBannerAdmob!=null){
                    onLoadBannerAdmob.onAdClosed();
                }
            }
        });


    }

    public static void SmallBannerApplovinDisHPK(Activity activity, RelativeLayout layAds, String selectAdsBackup, String idBanner, String idBannerBackup, String HPK1,
                                                 String HPK2, String HPK3, String HPK4, String HPK5) {

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
                if (onLoadBannerApplovinDiscovery!=null){
                    onLoadBannerApplovinDiscovery.adReceived();
                }
                switch (selectAdsBackup) {
                    case "APPLOVIN-M":
                        if (adViewMax != null) {
                            adViewMax.destroy();
                        }
                        break;
                    case "MOPUB":
                    case "UNITY":

                        break;
                    case "IRON":
                        if (adViewIron != null) {
                            adViewIron.isDestroyed();
                        }
                        break; case "ADMOB":
                        if (adViewAdmob != null) {
                            adViewAdmob.destroy();
                        }
                        break;

                    case "GOOGLE-ADS":
                        if (bannerGoogleAds != null) {
                            bannerGoogleAds.destroy();
                        }
                        break;
//                    case "FACEBOOK":
//                        if (adViewFAN != null) {
//                            adViewFAN.destroy();
//                        }
//                        break;
                }
            }

            @Override
            public void failedToReceiveAd(int errorCode) {
                if (onLoadBannerApplovinDiscovery!=null){
                    onLoadBannerApplovinDiscovery.failedToReceiveAd();
                }
                switch (selectAdsBackup) {
                    case "APPLOVIN-M":
                        adViewMax = new MaxAdView(idBannerBackup, activity);

                        final boolean isTablet = AppLovinSdkUtils.isTablet(activity);
                        final int heightPx = AppLovinSdkUtils.dpToPx(activity, isTablet ? 90 : 50);
                        adViewMax.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx));
                        layAds.addView(adViewMax);
                        adViewMax.loadAd();
                        break;
                    case "MOPUB":
                    case "UNITY":

                        break;
                    case "IRON":
                        adViewIron = IronSource.createBanner(activity, ISBannerSize.BANNER);
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                        layAds.addView(adViewIron, 0, layoutParams);
                        IronSource.loadBanner(adViewIron, idBannerBackup);
                        break;
                    case "ADMOB":
//                        Bundle extras = new FacebookExtras()
//                                .setNativeBanner(true)
//                                .build();
                        AdRequest request = new AdRequest.Builder()
                                //.addNetworkExtrasBundle(FacebookAdapter.class, extras)
                                .build();
                        adViewAdmob = new AdView(activity);
                        adViewAdmob.setAdUnitId(idBannerBackup);
                        layAds.addView(adViewAdmob);
                        AdSize adSizeAdmob = getAdSize(activity);
                        adViewAdmob.setAdSize(adSizeAdmob);
                        adViewAdmob.loadAd(request);
                        break;
                    case "GOOGLE-ADS":
                        AdManagerAdRequest adRequest =
                                new AdManagerAdRequest.Builder()
                                        .build();

                        bannerGoogleAds = new AdManagerAdView(activity);
                        bannerGoogleAds.setAdUnitId(idBannerBackup);
                        layAds.addView(bannerGoogleAds);
                        AdSize adaptiveSize = getAdSize(activity);
                        bannerGoogleAds.setAdSize(adaptiveSize);
                        bannerGoogleAds.loadAd(adRequest);
                        break;
//                    case "FACEBOOK":
//                        adViewFAN = new com.facebook.ads.AdView(activity, idBannerBackup,
//                                com.facebook.ads.AdSize.BANNER_HEIGHT_50);
//                        layAds.addView(adViewFAN);
//                        adViewFAN.loadAd();
//                        break;
                    case "ALIEN-V":
                        AlienViewAds.Banner(activity,layAds,idBannerBackup);
                        break;
                    case "ALIEN-M":
                        AlienMediationAds.SmallBanner(activity,layAds,idBannerBackup);
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
                if (onLoadBannerApplovinDiscovery!=null){
                    onLoadBannerApplovinDiscovery.adReceived();
                }
                switch (selectAdsBackup) {
                    case "APPLOVIN-M":
                        if (adViewMax != null) {
                            adViewMax.destroy();
                        }
                        break;
                    case "MOPUB":
                    case "UNITY":

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
                    case "GOOGLE-ADS":
                        if (bannerGoogleAds != null) {
                            bannerGoogleAds.destroy();
                        }
                        break;
//                    case "FACEBOOK":
//                        if (adViewFAN != null) {
//                            adViewFAN.destroy();
//                        }
//                        break;
                }
            }

            @Override
            public void failedToReceiveAd(int errorCode) {
                if (onLoadBannerApplovinDiscovery!=null){
                    onLoadBannerApplovinDiscovery.failedToReceiveAd();
                }
                switch (selectAdsBackup) {
                    case "APPLOVIN-M":
                        adViewMax = new MaxAdView(idBannerBackup, activity);
                        final boolean isTablet = AppLovinSdkUtils.isTablet(activity);
                        final int heightPx = AppLovinSdkUtils.dpToPx(activity, isTablet ? 90 : 50);
                        adViewMax.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx));
                        layAds.addView(adViewMax);
                        adViewMax.loadAd();
                        break;
                    case "MOPUB":
                    case "UNITY":

                        break;
                    case "IRON":
                        adViewIron = IronSource.createBanner(activity, ISBannerSize.BANNER);
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                        layAds.addView(adViewIron, 0, layoutParams);
                        IronSource.loadBanner(adViewIron, idBannerBackup);
                        break;
                    case "ADMOB":
//                        Bundle extras = new FacebookExtras()
//                                .setNativeBanner(true)
//                                .build();
                        AdRequest request = new AdRequest.Builder()
                                //.addNetworkExtrasBundle(FacebookAdapter.class, extras)
                                .build();
                        adViewAdmob = new AdView(activity);
                        adViewAdmob.setAdUnitId(idBannerBackup);
                        layAds.addView(adViewAdmob);
                        AdSize adSizeAdmob = getAdSize(activity);
                        adViewAdmob.setAdSize(adSizeAdmob);
                        adViewAdmob.loadAd(request);
                        break;
                    case "GOOGLE-ADS":
                        AdManagerAdRequest adRequest =
                                new AdManagerAdRequest.Builder()
                                        .build();

                        bannerGoogleAds = new AdManagerAdView(activity);
                        bannerGoogleAds.setAdUnitId(idBannerBackup);
                        layAds.addView(bannerGoogleAds);
                        AdSize adaptiveSize = getAdSize(activity);
                        bannerGoogleAds.setAdSize(adaptiveSize);
                        bannerGoogleAds.loadAd(adRequest);
                        break;
//                    case "FACEBOOK":
//                        adViewFAN = new com.facebook.ads.AdView(activity, idBannerBackup,
//                                com.facebook.ads.AdSize.BANNER_HEIGHT_50);
//                        layAds.addView(adViewFAN);
//                        adViewFAN.loadAd();
//                        break;
                    case "ALIEN-V":
                        AlienViewAds.Banner(activity,layAds,idBannerBackup);
                        break;
                    case "ALIEN-M":
                        AlienMediationAds.SmallBanner(activity,layAds,idBannerBackup);
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

        MaxAdViewAdListener listener = new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd ad) {
            if (onLoadBannerApplovinMax!=null){
                onLoadBannerApplovinMax.onAdExpanded();
            }
            }

            @Override
            public void onAdCollapsed(MaxAd ad) {
                if (onLoadBannerApplovinMax!=null){
                    onLoadBannerApplovinMax.onAdCollapsed();
                }
            }

            @Override
            public void onAdLoaded(MaxAd ad) {
                if (onLoadBannerApplovinMax!=null){
                    onLoadBannerApplovinMax.onAdLoaded();
                }
                switch (selectAdsBackup) {
                    case "APPLOVIN-D":
                        if (adViewDiscovery != null) {
                            adViewDiscovery.destroy();
                        }
                        break;
                    case "MOPUB":
                    case "UNITY":

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
                    case "GOOGLE-ADS":
                        if (bannerGoogleAds != null) {
                            bannerGoogleAds.destroy();
                        }
                        break;
//                    case "FACEBOOK":
//                        if (adViewFAN != null) {
//                            adViewFAN.destroy();
//                        }
//                        break;
                }
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                if (onLoadBannerApplovinMax!=null){
                    onLoadBannerApplovinMax.onAdDisplayed();
                }
            }

            @Override
            public void onAdHidden(MaxAd ad) {
                if (onLoadBannerApplovinMax!=null){
                    onLoadBannerApplovinMax.onAdHidden();
                }
            }

            @Override
            public void onAdClicked(MaxAd ad) {
                if (onLoadBannerApplovinMax!=null){
                    onLoadBannerApplovinMax.onAdClicked();
                }
            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                if (onLoadBannerApplovinMax!=null){
                    onLoadBannerApplovinMax.onAdLoadFailed();
                }
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
                    case "MOPUB":
                    case "UNITY":

                        break;
                    case "IRON":
                        adViewIron = IronSource.createBanner(activity, ISBannerSize.BANNER);
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                        layAds.addView(adViewIron, 0, layoutParams);
                        IronSource.loadBanner(adViewIron, idBannerBackup);
                        break;
                    case "ADMOB":
//                        Bundle extras = new FacebookExtras()
//                                .setNativeBanner(true)
//                                .build();
                        AdRequest request = new AdRequest.Builder()
                                //.addNetworkExtrasBundle(FacebookAdapter.class, extras)
                                .build();
                        adViewAdmob = new AdView(activity);
                        adViewAdmob.setAdUnitId(idBannerBackup);
                        layAds.addView(adViewAdmob);
                        AdSize adSizeAdmob = getAdSize(activity);
                        adViewAdmob.setAdSize(adSizeAdmob);
                        adViewAdmob.loadAd(request);
                        break;
                    case "GOOGLE-ADS":
                        AdManagerAdRequest adRequest =
                                new AdManagerAdRequest.Builder()
                                        .build();

                        bannerGoogleAds = new AdManagerAdView(activity);
                        bannerGoogleAds.setAdUnitId(idBannerBackup);
                        layAds.addView(bannerGoogleAds);
                        AdSize adaptiveSize = getAdSize(activity);
                        bannerGoogleAds.setAdSize(adaptiveSize);
                        bannerGoogleAds.loadAd(adRequest);
                        break;
                    case "ALIEN-V":
                        AlienViewAds.Banner(activity,layAds,idBannerBackup);
                        break;
                    case "ALIEN-M":
                        AlienMediationAds.SmallBanner(activity,layAds,idBannerBackup);
                        break;
                }

            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                if (onLoadBannerApplovinMax!=null){
                    onLoadBannerApplovinMax.onAdDisplayFailed();
                }
            }
        };
        adViewMax.setListener(listener);
        final boolean isTablet = AppLovinSdkUtils.isTablet(activity);
        final int heightPx = AppLovinSdkUtils.dpToPx(activity, isTablet ? 90 : 50);
        adViewMax.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx));
        layAds.addView(adViewMax);
        adViewMax.loadAd();
    }

    public static void SmallBannerMopub(Activity activity, RelativeLayout layAds, String selectAdsBackup, String idBanner, String idBannerBackup) {

    }

    public static void SmallBannerIron(Activity activity, RelativeLayout layAds, String selectAdsBackup, String idBanner, String idBannerBackup) {
        adViewIron = IronSource.createBanner(activity, ISBannerSize.BANNER);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        layAds.addView(adViewIron, 0, layoutParams);
        com.ironsource.mediationsdk.sdk.BannerListener listener = new com.ironsource.mediationsdk.sdk.BannerListener() {
            @Override
            public void onBannerAdLoaded() {
                if (onLoadBannerIronSource!=null){
                    onLoadBannerIronSource.onBannerAdLoaded();
                }
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
                    case "MOPUB":
                    case "UNITY":

                        break;
                    case "ADMOB":
                        if (adViewAdmob != null) {
                            adViewAdmob.destroy();
                        }
                        break;
                    case "GOOGLE-ADS":
                        if (bannerGoogleAds != null) {
                            bannerGoogleAds.destroy();
                        }
                        break;

                }
            }

            @Override
            public void onBannerAdLoadFailed(IronSourceError ironSourceError) {
                if (onLoadBannerIronSource!=null){
                    onLoadBannerIronSource.onBannerAdLoadFailed();
                }
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

                        final boolean isTablet = AppLovinSdkUtils.isTablet(activity);
                        final int heightPx = AppLovinSdkUtils.dpToPx(activity, isTablet ? 90 : 50);
                        adViewMax.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx));
                        layAds.addView(adViewMax);
                        adViewMax.loadAd();
                        break;
                    case "MOPUB":
                    case "UNITY":

                        break;
                    case "ADMOB":
//                        Bundle extras = new FacebookExtras()
//                                .setNativeBanner(true)
//                                .build();
                        AdRequest request = new AdRequest.Builder()
                                //.addNetworkExtrasBundle(FacebookAdapter.class, extras)
                                .build();
                        adViewAdmob = new AdView(activity);
                        adViewAdmob.setAdUnitId(idBannerBackup);
                        layAds.addView(adViewAdmob);
                        AdSize adSizeAdmob = getAdSize(activity);
                        adViewAdmob.setAdSize(adSizeAdmob);
                        adViewAdmob.loadAd(request);
                        break;
                    case "GOOGLE-ADS":
                        AdManagerAdRequest adRequest =
                                new AdManagerAdRequest.Builder()
                                        .build();

                        bannerGoogleAds = new AdManagerAdView(activity);
                        bannerGoogleAds.setAdUnitId(idBannerBackup);
                        layAds.addView(bannerGoogleAds);
                        AdSize adaptiveSize = getAdSize(activity);
                        bannerGoogleAds.setAdSize(adaptiveSize);
                        bannerGoogleAds.loadAd(adRequest);
                        break;
//                    case "FACEBOOK":
//                        adViewFAN = new com.facebook.ads.AdView(activity, idBannerBackup,
//                                com.facebook.ads.AdSize.BANNER_HEIGHT_50);
//                        layAds.addView(adViewFAN);
//                        adViewFAN.loadAd();
//                        break;
                    case "ALIEN-V":
                        AlienViewAds.Banner(activity,layAds,idBannerBackup);
                        break;
                    case "ALIEN-M":
                        AlienMediationAds.SmallBanner(activity,layAds,idBannerBackup);
                        break;
                }
            }

            @Override
            public void onBannerAdClicked() {
                if (onLoadBannerIronSource!=null){
                    onLoadBannerIronSource.onBannerAdClicked();
                }
            }

            @Override
            public void onBannerAdScreenPresented() {
                if (onLoadBannerIronSource!=null){
                    onLoadBannerIronSource.onBannerAdScreenPresented();
                }
            }

            @Override
            public void onBannerAdScreenDismissed() {
                if (onLoadBannerIronSource!=null){
                    onLoadBannerIronSource.onBannerAdScreenDismissed();
                }
            }

            @Override
            public void onBannerAdLeftApplication() {
                if (onLoadBannerIronSource!=null){
                    onLoadBannerIronSource.onBannerAdLeftApplication();
                }
            }
        };
        adViewIron.setBannerListener(listener);
        IronSource.loadBanner(adViewIron, idBanner);
    }

    public static void SmallBannerUnity(Activity activity, RelativeLayout layAds, String selectAdsBackup, String idBanner, String idBannerBackup) {

    }

    public static void SmallBannerAlienView(Activity activity, RelativeLayout layAds, String selectAdsBackup, String idBanner, String idBannerBackup) {
    AlienViewAds.Banner(activity,layAds,idBanner);
    AlienViewAds.onLoadBannerView = new OnLoadBannerView() {
        @Override
        public void onBannerAdLoaded() {
            if (onLoadBannerAlienView!=null){
                onLoadBannerAlienView.onBannerAdLoaded();
            }
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
                case "MOPUB":
                case "UNITY":

                    break;
                case "ADMOB":
                    if (adViewAdmob != null) {
                        adViewAdmob.destroy();
                    }
                    break;
                case "GOOGLE-ADS":
                    if (bannerGoogleAds != null) {
                        bannerGoogleAds.destroy();
                    }
                    break;
                case "IRON":
                    if (adViewIron != null) {
                        adViewIron.isDestroyed();
                    }
                    break;
            }
        }

        @Override
        public void onBannerAdClicked() {
            if (onLoadBannerAlienView!=null){
                onLoadBannerAlienView.onBannerAdClicked();
            }
        }

        @Override
        public void onBannerAdFailedToLoad(String error) {
            if (onLoadBannerAlienView!=null){
                onLoadBannerAlienView.onBannerAdFailedToLoad("");
            }
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

                    final boolean isTablet = AppLovinSdkUtils.isTablet(activity);
                    final int heightPx = AppLovinSdkUtils.dpToPx(activity, isTablet ? 90 : 50);
                    adViewMax.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx));
                    layAds.addView(adViewMax);
                    adViewMax.loadAd();
                    break;
                case "MOPUB":
                case "UNITY":

                    break;
                case "ADMOB":
//                    Bundle extras = new FacebookExtras()
//                            .setNativeBanner(true)
//                            .build();
                    AdRequest request = new AdRequest.Builder()
//                            .addNetworkExtrasBundle(FacebookAdapter.class, extras)
                            .build();
                    adViewAdmob = new AdView(activity);
                    adViewAdmob.setAdUnitId(idBannerBackup);
                    layAds.addView(adViewAdmob);
                    AdSize adSizeAdmob = getAdSize(activity);
                    adViewAdmob.setAdSize(adSizeAdmob);
                    adViewAdmob.loadAd(request);
                    break;
                case "GOOGLE-ADS":
                    AdManagerAdRequest adRequest =
                            new AdManagerAdRequest.Builder()
                                    .build();

                    bannerGoogleAds = new AdManagerAdView(activity);
                    bannerGoogleAds.setAdUnitId(idBannerBackup);
                    layAds.addView(bannerGoogleAds);
                    AdSize adaptiveSize = getAdSize(activity);
                    bannerGoogleAds.setAdSize(adaptiveSize);
                    bannerGoogleAds.loadAd(adRequest);
                    break;
//                case "FACEBOOK":
//                    adViewFAN = new com.facebook.ads.AdView(activity, idBannerBackup,
//                            com.facebook.ads.AdSize.BANNER_HEIGHT_50);
//                    layAds.addView(adViewFAN);
//                    adViewFAN.loadAd();
//                    break;
                case "IRON":
                    adViewIron = IronSource.createBanner(activity, ISBannerSize.BANNER);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT);
                    layAds.addView(adViewIron, 0, layoutParams);
                    IronSource.loadBanner(adViewIron, idBannerBackup);
                    break;
                case "ALIEN-M":
                    AlienMediationAds.SmallBanner(activity,layAds,idBannerBackup);
                    break;
            }
        }
    };
    }

    public static void SmallBannerAlienMediation(Activity activity, RelativeLayout layAds, String selectAdsBackup, String idBanner, String idBannerBackup) {
        AlienMediationAds.SmallBanner(activity,layAds,idBannerBackup);
        AlienMediationAds.onLoadBannerMediation = new OnLoadBannerMediation() {
            @Override
            public void onBannerAdLoaded() {
                if (onLoadBannerAlienMediation!=null){
                    onLoadBannerAlienMediation.onBannerAdLoaded();
                }
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
//                    case "STARTAPP":
//                        startAppBanner.hideBanner();
//                        break;
                    case "MOPUB":
                    case "UNITY":

                        break;
                    case "ADMOB":
                        if (adViewAdmob != null) {
                            adViewAdmob.destroy();
                        }
                        break;
                    case "GOOGLE-ADS":
                        if (bannerGoogleAds != null) {
                            bannerGoogleAds.destroy();
                        }
                        break;
//                    case "FACEBOOK":
//                        if (adViewFAN != null) {
//                            adViewFAN.destroy();
//                        }
//                        break;
                    case "IRON":
                        if (adViewIron != null) {
                            adViewIron.isDestroyed();
                        }
                        break;
                }
            }

            @Override
            public void onBannerAdClicked() {
                if (onLoadBannerAlienMediation!=null){
                    onLoadBannerAlienMediation.onBannerAdClicked();
                }
            }

            @Override
            public void onBannerAdFailedToLoad(String error) {
                if (onLoadBannerAlienMediation!=null){
                    onLoadBannerAlienMediation.onBannerAdFailedToLoad("");
                }
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

                        final boolean isTablet = AppLovinSdkUtils.isTablet(activity);
                        final int heightPx = AppLovinSdkUtils.dpToPx(activity, isTablet ? 90 : 50);
                        adViewMax.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightPx));
                        layAds.addView(adViewMax);
                        adViewMax.loadAd();
                        break;
                    case "MOPUB":
                    case "UNITY":

                        break;
                    case "ADMOB":
//                        Bundle extras = new FacebookExtras()
//                                .setNativeBanner(true)
//                                .build();
                        AdRequest request = new AdRequest.Builder()
//                                .addNetworkExtrasBundle(FacebookAdapter.class, extras)
                                .build();
                        adViewAdmob = new AdView(activity);
                        adViewAdmob.setAdUnitId(idBannerBackup);
                        layAds.addView(adViewAdmob);
                        AdSize adSizeAdmob = getAdSize(activity);
                        adViewAdmob.setAdSize(adSizeAdmob);
                        adViewAdmob.loadAd(request);
                        break;
                    case "GOOGLE-ADS":
                        AdManagerAdRequest adRequest =
                                new AdManagerAdRequest.Builder()
                                        .build();

                        bannerGoogleAds = new AdManagerAdView(activity);
                        bannerGoogleAds.setAdUnitId(idBannerBackup);
                        layAds.addView(bannerGoogleAds);
                        AdSize adaptiveSize = getAdSize(activity);
                        bannerGoogleAds.setAdSize(adaptiveSize);
                        bannerGoogleAds.loadAd(adRequest);
                        break;
                    case "IRON":
                        adViewIron = IronSource.createBanner(activity, ISBannerSize.BANNER);
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                        layAds.addView(adViewIron, 0, layoutParams);
                        IronSource.loadBanner(adViewIron, idBannerBackup);
                        break;
                    case "ALIEN-V":
                        AlienViewAds.Banner(activity,layAds,idBannerBackup);
                        break;
                }
            }
        };

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
