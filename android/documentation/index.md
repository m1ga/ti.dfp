# ti.dfp Module

## Description

Allows for the display of DFP in Titanium Android applications. 

Please note that if your androidManifest has screen support set to: android:anyDensity="false", 
any banner ads will display too small on high density devices. 
It is not clear at this point if this is a bug with DFP or Titanium. 
In any event, you will either need to NOT set your screen support -- 
or set android:anyDensity="true" and adjust your app layout accordingly

## Getting Started

View the [Using Titanium Modules](http://docs.appcelerator.com/titanium/latest/#!/guide/Using_Titanium_Modules) document for instructions on getting
started with using this module in your application.

## Requirements

Add this to the &lt;android /&gt; node in tiapp.xml: 

	<android>
	    <tool-api-level>14</tool-api-level>
	</android>

## Accessing the DFP Module

To access this module from JavaScript, you would do the following (recommended):

	var dfp = require('ti.dfp');

The "dfp" variable is now a reference to the Module object.	

## Doubleclick for Publishers Developer Docs
<https://developers.google.com/mobile-ads-sdk/>

## Functions

### createView({ . . . })

Returns a view with an ad initialized by default.

#### Arguments

parameters[object]: a dictionary object of properties.

#### Example:

	var dfpView = dfp.createView({
		top: 0, 
		left: 0,
		width: '320dp', 
		height: '50dp',
	
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
		// in the console log after the app launched
		testDevices: [ dfp.EMULATOR_ID ],
		
		customTargeting: {
	    	key1: 'value1',
	    	key2: 'value2',
	    },
	    
		location: {
			latitude: 35.779511,
			longitude:  -78.674045,
			accuracy: 10
		}
	});

## Module History

This module is based off the Titanium ti.admob module

