package ti.dfp;

import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiUIView;

public class View extends TiUIView
{
	private static final String TAG = "ti.dfp";
	AdManagerAdView adView;
	int prop_top;
	int prop_left;
	int prop_right;
	String prop_color_bg;
	String prop_color_bg_top;
	String prop_color_border;
	String prop_color_text;
	String prop_color_link;
	String prop_color_url;

	public View(final TiViewProxy proxy)
	{
		super(proxy);
		Log.d(TAG, "Creating DFP adview...");
	}

	private void createView()
	{

		try {
			Log.d(TAG, "createView()");
			// create the adView
			adView = new AdManagerAdView(proxy.getActivity());
			adView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			MobileAds.initialize(proxy.getActivity(), new OnInitializationCompleteListener() {
				@Override
				public void onInitializationComplete(InitializationStatus initializationStatus)
				{
				}
			});

			if (DfpModule.AD_SIZES != null) {
				Log.d(TAG, "createView() Ad Unit: " + DfpModule.ADUNIT_ID + ", size: multiple");
				adView.setAdSizes(DfpModule.AD_SIZES);
			} else if ((DfpModule.ADWIDTH > 0) && (DfpModule.ADHEIGHT > 0)) {
				Log.d(TAG, "createView() Ad Unit: " + DfpModule.ADUNIT_ID + ", size: " + DfpModule.ADWIDTH + "x"
							   + DfpModule.ADHEIGHT);

				AdSize adsize = new AdSize(DfpModule.ADWIDTH, DfpModule.ADHEIGHT);
				adView.setAdSize(adsize);
			} else {
				Log.d(TAG, "createView() Ad Unit: " + DfpModule.ADUNIT_ID + ", size: SMART_BANNER");
				Display display = TiApplication.getAppCurrentActivity().getWindowManager().getDefaultDisplay();
				DisplayMetrics outMetrics = new DisplayMetrics();
				display.getMetrics(outMetrics);

				float widthPixels = outMetrics.widthPixels;
				float density = outMetrics.density;

				int adWidth = (int) (widthPixels / density);

				adView.setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
					TiApplication.getAppCurrentActivity(), adWidth));
			}
			adView.setAdUnitId(DfpModule.ADUNIT_ID);

			// set the listener
			adView.setAdListener(new AdListener() {
				public void onAdLoaded()
				{
					HashMap<String, Integer> map = new HashMap<String, Integer>();
					map.put("width", new Integer(adView.getWidth()));
					map.put("height", new Integer(adView.getHeight()));

					Log.d(TAG, "onAdLoaded() " + adView.getWidth() + ", " + adView.getHeight());
					if (proxy != null)
						proxy.fireEvent("ad_received", new KrollDict(map));
				}

				public void onAdFailedToLoad(int errorCode)
				{
					Log.d(TAG, "onAdFailedToLoad(): " + errorCode);
					if (proxy != null)
						proxy.fireEvent("ad_not_received", new KrollDict());
				}

				public void onAdOpened()
				{
					Log.d(TAG, "onAdOpened()");
					if (proxy != null)
						proxy.fireEvent("ad_opened", new KrollDict());
				}

				public void onAdClosed()
				{
					Log.d(TAG, "onAdClosed()");
					if (proxy != null)
						proxy.fireEvent("ad_closed", new KrollDict());
				}

				public void onAdLeftApplicadtion()
				{
					Log.d(TAG, "onAdLeftApplicadtion()");
					if (proxy != null)
						proxy.fireEvent("leave_application", new KrollDict());
				}
			});
			adView.setPadding(prop_left, prop_top, prop_right, 0);
			// Add the AdView to your view hierarchy.
			// The view will have no size until the ad is loaded.
			setNativeView(adView);
			loadAd();
		} catch (IllegalStateException e) {
			Log.w(TAG, "EXCEPTION (IllegalStateException): " + e.getMessage());
		} catch (Exception e) {
			Log.w(TAG, "EXCEPTION: " + e.getMessage());
		}
	}

	// load the DFP ad
	public void loadAd()
	{
		proxy.getActivity().runOnUiThread(new Runnable() {
			public void run()
			{
				final AdManagerAdRequest.Builder adRequestBuilder = new AdManagerAdRequest.Builder();
				final RequestConfiguration.Builder globalRequstBilder = new RequestConfiguration.Builder();

				if (DfpModule.LOCATION != null) {
					adRequestBuilder.setLocation(DfpModule.LOCATION);
				}

				Log.d(TAG, "requestAd ()");
				if (DfpModule.TEST_DEVICES != null) {
					globalRequstBilder.setTestDeviceIds(Arrays.asList(DfpModule.TEST_DEVICES.clone()));
				}

				Bundle bundle = createAdRequestProperties();
				if (bundle.size() > 0) {
					Log.d(TAG, "extras.size() > 0 -- set ad properties");
					adRequestBuilder.addNetworkExtrasBundle(AdMobAdapter.class, bundle);
				}

				MobileAds.setRequestConfiguration(globalRequstBilder.build());
				adView.loadAd(adRequestBuilder.build());
			}
		});
	}

	@Override
	public void processProperties(KrollDict d)
	{
		try {
			super.processProperties(d);
			Log.d(TAG, "process properties");
			if (d.containsKey("adUnitId")) {
				Log.d(TAG, "has adUnitId: " + d.getString("adUnitId"));
				DfpModule.ADUNIT_ID = d.getString("adUnitId");
			}
			if (d.containsKey("adHeight")) {
				Log.d(TAG, "has adHeight: " + d.getInt("adHeight"));
				DfpModule.ADHEIGHT = d.getInt("adHeight");
			}
			if (d.containsKey("adWidth")) {
				Log.d(TAG, "has adWidth: " + d.getInt("adWidth"));
				DfpModule.ADWIDTH = d.getInt("adWidth");
			}
			if (d.containsKey("testDevices")) {
				Log.d(TAG, "has test devices");
				DfpModule.TEST_DEVICES = d.getStringArray("testDevices");
			}
			if (d.containsKey("suppressScroll")) {
				Log.d(TAG, "has suppressScroll param: " + d.getBoolean("suppressScroll"));
				DfpModule.SUPPRESS_SCROLL = d.getBoolean("suppressScroll");
			}
			if (d.containsKey("customTargeting")) {
				KrollDict ct = d.getKrollDict("customTargeting");
				Bundle b = new Bundle();
				Log.d(TAG, "has " + ct.size() + " items in customTargeting dictionary:");
				for (Map.Entry<String, Object> entry : ct.entrySet()) {
					Object value = entry.getValue();
					if (value != null) {
						Log.d(TAG, " - " + entry.getKey() + " => " + value);
						b.putString(entry.getKey(), value.toString());
					}
				}
				DfpModule.CUSTOM_TARGETING = b;
			}

			if (d.containsKey("location")) {
				KrollDict ld = d.getKrollDict("location");

				if (!ld.isNull("latitude") && !ld.isNull("longitude") && !ld.isNull("accuracy")) {

					Location l = new Location("");
					l.setLatitude(ld.getDouble("latitude"));
					l.setLongitude(ld.getDouble("longitude"));
					l.setAccuracy(ld.getDouble("accuracy").floatValue());

					Log.d(TAG, "has location:");
					Log.d(TAG, "  - latitude:  " + ld.getDouble("latitude").toString());
					Log.d(TAG, "  - longitude: " + ld.getDouble("longitude").toString());
					Log.d(TAG, "  - accuracy:  " + ld.getDouble("accuracy").toString());
					DfpModule.LOCATION = l;
				}
			}
			if (d.containsKey(DfpModule.PROPERTY_COLOR_BG)) {
				Log.d(TAG, "has PROPERTY_COLOR_BG: " + d.getString(DfpModule.PROPERTY_COLOR_BG));
				prop_color_bg = convertColorProp(d.getString(DfpModule.PROPERTY_COLOR_BG));
			}
			if (d.containsKey(DfpModule.PROPERTY_COLOR_BG_TOP)) {
				Log.d(TAG, "has PROPERTY_COLOR_BG_TOP: " + d.getString(DfpModule.PROPERTY_COLOR_BG_TOP));
				prop_color_bg_top = convertColorProp(d.getString(DfpModule.PROPERTY_COLOR_BG_TOP));
			}
			if (d.containsKey(DfpModule.PROPERTY_COLOR_BORDER)) {
				Log.d(TAG, "has PROPERTY_COLOR_BORDER: " + d.getString(DfpModule.PROPERTY_COLOR_BORDER));
				prop_color_border = convertColorProp(d.getString(DfpModule.PROPERTY_COLOR_BORDER));
			}
			if (d.containsKey(DfpModule.PROPERTY_COLOR_TEXT)) {
				Log.d(TAG, "has PROPERTY_COLOR_TEXT: " + d.getString(DfpModule.PROPERTY_COLOR_TEXT));
				prop_color_text = convertColorProp(d.getString(DfpModule.PROPERTY_COLOR_TEXT));
			}
			if (d.containsKey(DfpModule.PROPERTY_COLOR_LINK)) {
				Log.d(TAG, "has PROPERTY_COLOR_LINK: " + d.getString(DfpModule.PROPERTY_COLOR_LINK));
				prop_color_link = convertColorProp(d.getString(DfpModule.PROPERTY_COLOR_LINK));
			}
			if (d.containsKey(DfpModule.PROPERTY_COLOR_URL)) {
				Log.d(TAG, "has PROPERTY_COLOR_URL: " + d.getString(DfpModule.PROPERTY_COLOR_URL));
				prop_color_url = convertColorProp(d.getString(DfpModule.PROPERTY_COLOR_URL));
			}

			if (d.containsKey("adSizes")) {
				Log.d(TAG, "has adSizes:");

				Object[] adSizes = (Object[]) d.get("adSizes");

				DfpModule.AD_SIZES = new AdSize[adSizes.length];

				for (int i = 0; i < adSizes.length; i++) {
					Map<String, Integer> hm = (Map<String, Integer>) adSizes[i];

					// You now have a HashMap!
					Log.d(TAG, "" + hm);

					DfpModule.AD_SIZES[i] = new AdSize(hm.get("width"), hm.get("height"));
				}
			}

			// now create the adView
			this.createView();
		} catch (Exception e) {
			Log.w(TAG, "EXCEPTION: " + e.getMessage());
		}
	}

	public void pause()
	{
		Log.d(TAG, "pause");
		adView.pause();
	}

	public void resume()
	{
		Log.d(TAG, "resume");
		adView.resume();
	}

	public void destroy()
	{
		Log.d(TAG, "destroy");
		adView.destroy();
	}

	// pass the method the TESTING flag
	public void requestAd()
	{
		Log.d(TAG, "requestAd()");
		loadAd();
	}

	// helper methods

	// create the adRequest extra props
	// http://code.google.com/mobile/ads/docs/bestpractices.html#adcolors
	private Bundle createAdRequestProperties()
	{
		Bundle bundle = new Bundle();

		if (prop_color_bg != null) {
			bundle.putString("color_bg", prop_color_bg);
		}
		if (prop_color_bg_top != null) {
			bundle.putString("color_bg_top", prop_color_bg_top);
		}
		if (prop_color_border != null) {
			bundle.putString("color_border", prop_color_border);
		}
		if (prop_color_text != null) {
			bundle.putString("color_text", prop_color_text);
		}
		if (prop_color_link != null) {
			bundle.putString("color_link", prop_color_link);
		}
		if (prop_color_url != null) {
			bundle.putString("color_url", prop_color_url);
		}

		if (DfpModule.CUSTOM_TARGETING != null) {
			for (String key : DfpModule.CUSTOM_TARGETING.keySet()) {
				bundle.putString(key, DfpModule.CUSTOM_TARGETING.get(key).toString());
			}
		}

		return bundle;
	}

	// modifies the color prop -- removes # and changes constants into hex values
	private String convertColorProp(String color)
	{
		color = color.replace("#", "");

		// these are arbitrary and should probably be eliminated
		if (color.equals("white")) {
			color = "FFFFFF";
		}
		if (color.equals("black")) {
			color = "000000";
		}

		return color;
	}
}
