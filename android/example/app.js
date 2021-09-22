var win = Titanium.UI.createWindow({
    backgroundColor: "#FFFFFF"
});

var dfp = require('ti.dfp');

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

// then create an dfp view
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

    // or you can specify an array of supported ad sizes
    adSizes: [
        {width: 320, height: 100},
        {width: 320, height: 50},
        {width: 320, height: 240}
    ],

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
        key2: 'value2',
    },

    location: {
        latitude: 35.779511,
        longitude:  -78.674045,
        accuracy: 10
    }
});


var adRequestBtn = Ti.UI.createButton({
    title:"Request an ad",
    top:"10%",
    height: "10%",
    width: "80%"
});

adRequestBtn.addEventListener("click",function(){
    adRequestBtn.title = "Requesting...";
    adRequestBtn.enabled = false;
    dfpView.requestAd();
});

dfpView.addEventListener("ad_received", function(){
    Ti.API.info("ad received");
    adRequestBtn.title = "Request an ad";
    adRequestBtn.enabled = true;
});

dfpView.addEventListener("ad_not_received", function(){
    Ti.API.info("ad not received");
    adRequestBtn.title = "Request an ad";
    adRequestBtn.enabled = true;
});


win.add(dfpView);
win.add(adRequestBtn);
win.open();
