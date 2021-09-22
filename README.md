# DFP Module

## Description

This is a Titanium Mobile module for displaying DFP ads in your app. <a href="https://github.com/jpriebe">Jason Priebe</a> started with Appcelerator's admob module, followed a few things that <a href="https://github.com/wienke/">Wienke Giezeman</a> did to add DFP support, updated to the latest Google Mobile Ads SDK and Google Play Services, and then brought the APIs for iOS and android into sync.

## Quick Start

Android: add this to your tiapp.xml (otherwise the app won't start)
```
<manifest>
    <application>
        <!-- Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713 -->
        <meta-data
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="ca-app-pub-xxxxxxxxxxxxxxxx~yyyyyyyyyy"/>
    </application>
</manifest>
```

## Installation

* Download ZIP from https://github.com/m1ga/ti.dfp/releases/
* place it inside your app root folder
* add `<module>ti.dfp</module>` to tiapp.xml
* compile your app
