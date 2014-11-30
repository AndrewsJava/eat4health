package harlequinmettle.android.eat4health;

import harlequinmettle.android.tools.androidsupportlibrary.TextViewFactory;
import android.app.Fragment;
import android.app.FragmentManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Eat4HealthFunctions extends Eat4HealthData {

	protected void setFragment(Fragment fragment, String title) {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(content_frame, fragment).commit();
		// for back navigation
		// fragmentManager.beginTransaction().replace(content_frame,
		// fragment).addToBackStack(title).commit();
		setTitle(title);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	public static void addViewsFromStrings(String heading, LinearLayout layout, String[] titles, View.OnClickListener actionDefinition) {
		TextView menucat = TextViewFactory.makeLeftTextView(heading);
		layout.addView(menucat);

		int i = 0;
		for (String title : titles) {
			TextView text = TextViewFactory.makeDefaultTextView(title);
			text.setOnClickListener(actionDefinition);

			layout.addView(text);
			viewMap.put(title, (float) title.hashCode());
		}
	}

}
