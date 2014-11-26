package harlequinmettle.android.eat4health;

import android.content.Context;

public class SystemContext extends android.app.Application {
	// require name in android manifest

	// <application
	// android:name="SystemContext"
	private static SystemContext instance;

	public SystemContext() {
		instance = this;
	}

	public Context getContext() {
		return instance;
	}

}
