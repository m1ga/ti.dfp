# ti.dfp Module

## Description

Shows ads from DFP.

## Accessing the ti.dfp Module

To access this module from JavaScript, you would do the following:

	var dfp = require('ti.dfp');

## Doubleclick for Publishers Developer Docs
<https://developers.google.com/mobile-ads-sdk/>

## Constants

### String SIMULATOR_ID

A constant to be passed in an array to the `testDevices` property to get test ads on the simulator.

## Functions

### ti.dfp.createView({...})

Creates and returns a [ti.dfp.View][] object which displays ads.

#### Arguments

parameters[object]: a dictionary object of properties defined in [ti.dfp.View][].

#### Example:

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
		
		// You can get your device's id for testDevices by looking in the console log after the app launched
		testDevices: [ dfp.SIMULATOR_ID ],
		
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

