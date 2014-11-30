package harlequinmettle.android.eat4health.fragments;

import harlequinmettle.android.eat4health.Eat4Health;
import harlequinmettle.android.eat4health.Eat4HealthFunctions;
import harlequinmettle.android.eat4health.Eat4HealthListeners;
import harlequinmettle.android.eat4health.staticdataarrays.FG2_I;
import harlequinmettle.android.tools.androidsupportlibrary.ContextReference;
import harlequinmettle.android.tools.androidsupportlibrary.ViewFactory;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

public class FoodListViewPagerFragment extends Fragment {

	public FoodListViewPagerFragment() {
		// Empty constructor required for fragment subclasses
	}

	protected View.OnClickListener foodWordsListener = new View.OnClickListener() {

		public void onClick(View view) {

		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// ViewPager foodListsPager = new
		// ViewPager(ContextReference.getAppContext());

		HorizontalScrollView application = new HorizontalScrollView(ContextReference.getAppContext());
		LinearLayout appAccess = new LinearLayout(ContextReference.getAppContext());

		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < Eat4Health.FOOD_GROUP_COUNT; i++) {
			if (!Eat4Health.MY_FOOD_GROUPS[i])
				continue;

			LinearLayout child = ViewFactory.basicLinearLayout();

			Eat4HealthFunctions.addViewsFromStrings(Eat4Health.foodGroups[i], child, FG2_I.FOODGROUPS[i], foodWordsListener);

			ScrollView foodList = new ScrollView(ContextReference.getAppContext());
			foodList.addView(child);
			appAccess.addView(foodList);
		}

		// rootView.addView(foodLists);
		getActivity().setTitle(Eat4HealthListeners.INTRO[0]);
		// return rootView;
		return appAccess;
	}

}
