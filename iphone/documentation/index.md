# ti.dfp Module

## Description

Allows for the display of DFP ads in Titanium Android applications. 

Please note that if your androidManifest has screen support set to: 

    android:anyDensity="false", 

any banner ads will display too small on high density devices. 
It is not clear at this point if this is a bug with DFP or Titanium. 
In any event, you will either need to NOT set your screen support -- 
or set android:anyDensity="true" and adjust your app layout accordingly

## Getting Started

View the [Using Titanium Modules](http://docs.appcelerator.com/titanium/latest/#!/guide/Using_Titanium_Modules) 
document for instructions on getting started with using this module in your application.

## Requirements

Add this to the &lt;android /&gt; node in tiapp.xml: 

    <android>
        <tool-api-level>14</tool-api-level>
    </android>

## Accessing the ti.dfp Module

To access this module from JavaScript, do the following:

	var dfp = require('ti.dfp');

## Doubleclick for Publishers Developer Docs

<https://developers.google.com/mobile-ads-sdk/>

## Functions

### ti.dfp.createView({...})

Creates and returns a [ti.dfp.View][] object which displays ads.

#### Arguments

parameters[object]: a dictionary object of properties defined in [ti.dfp.View][].

#### Example:

    // You can get your physical device's id for testDevices by looking
    // in the console log after the app launched; note that on iOS,
    // the simulator automatically gets test ads (since version 7 of GoogleMobileAds SDK);
    // on android, you have to explicitly specify that the emulator should
    // get test ads by adding dfp.EMULATOR_ID to the testDevices
    var testDevices = [ ];
    if (Ti.Platform.osname === 'android')
    {
        testDevices.push (dfp.EMULATOR_ID);
    }

	var dfpView = dfp.createView({
		top: 0, 
		left: 0,
		width: 320, 
		height: 50,
		
	    adUnitId: "<<AD UNIT ID>>",

		// all parameters below are optional

	    // you can explicitly specify an ad size; by default, will use DFP's SMART_BANNER mechanism
    	adWidth: 320,
	    adHeight: 50,

	    adBackgroundColor:  "#FF8800", 
	    backgroundColorTop: "#738000", 
	    borderColor:        "#000000", 
	    textColor:          "#000000", 
	    urlColor:           "#00FF00", 
	    linkColor:          "#0000FF", 
		
        // You can get your physical device's id for testDevices by looking
        // in the console log after the app launched; the simulator automatically
        // gets test ads (since version 7 of GoogleMobileAds SDK)
		testDevices: testDevices,
		
		customTargeting: {
			key1: 'value1',
			key2: 'value2'
		},
		
		location: {
			latitude: 35.779511,
			longitude:  -78.674045,
			accuracy: 10
		},
	
		// Support the following ad sizes (MultipleAdSizes feature of DFP SDK)
		adSizes: [
			{ width: 320, height: 100 },
			{ width: 320, height: 50 },
			{ width: 300, height: 250 },
			{ width: 728, height: 90 }
		],
	
		// Resize view to dimensions of the displayed ad
		autoResize: true
	});

## Module History

This module is based off the Titanium ti.admob module

