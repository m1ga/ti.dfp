# DFP Module

## Description

This is a Titanium Mobile module for displaying DFP ads in your app.  I started with Appcelerator's admob module, followed a few things that Wienke Giezeman (https://github.com/wienke/) did to add DFP support, updated to the latest Google Mobile Ads SDK and Google Play Services, and then brought the APIs for iOS and android into sync.

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

### Get it [![gitTio](http://gitt.io/badge.png)](http://gitt.io/component/ti.dfp)
Download the latest ZIP-file and consult the [Titanium Documentation](http://docs.appcelerator.com/titanium/latest/#!/guide/Using_a_Module) on how install it, or simply use the [gitTio CLI](http://gitt.io/cli):

`$ gittio install ti.dfp`
