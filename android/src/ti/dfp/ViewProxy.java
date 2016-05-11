package ti.dfp;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiContext.OnLifecycleEvent;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.view.TiUIView;

import android.app.Activity;

@Kroll.proxy(creatableInModule = DfpModule.class)
public class ViewProxy extends TiViewProxy implements OnLifecycleEvent {
	private View dfpView;
	private static final String TAG = "DfpViewProxy";

	public ViewProxy() {
		super();
	}

	@Override
	protected KrollDict getLangConversionTable() {
		KrollDict table = new KrollDict();
		table.put("title", "titleid");
		return table;
	}

	@Override
	public TiUIView createView(Activity activity) {
		dfpView = new View(this);
		return dfpView;
	}

	@Kroll.method
	public void requestAd() {
		Log.d(TAG, "[ti.dfp] requestAd()");
		dfpView.requestAd();
	}

	@Override
	public void onDestroy(Activity activity) {
		dfpView.destroy();
	}

	@Override
	public void onPause(Activity activity) {
		dfpView.pause();
	}

	@Override
	public void onResume(Activity activity) {
		dfpView.resume();
	}

	@Override
	public void onStart(Activity activity) {
	}

	@Override
	public void onStop(Activity activity) {
	}

}
