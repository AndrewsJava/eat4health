package harlequinmettle.android.eat4health.fragments;

import harlequinmettle.android.eat4health.Eat4Health;
import harlequinmettle.android.eat4health.Eat4HealthFunctions;
import harlequinmettle.android.eat4health.Eat4HealthListeners;
import harlequinmettle.android.eat4health.staticdataarrays.FG2_I;
import harlequinmettle.android.tools.androidsupportlibrary.ContextReference;
import harlequinmettle.android.tools.androidsupportlibrary.ViewFactory;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.Toast;

public class FoodListViewPagerFragment extends Fragment {

	ViewPager viewPager;
	MyPagerAdapter myPagerAdapter;

	public FoodListViewPagerFragment() {
		// Empty constructor required for fragment subclasses
	}

	protected View.OnClickListener foodWordsListener = new View.OnClickListener() {

		public void onClick(View view) {

		}
	};

	protected LinearLayout setUpViewPagerTest() {

		LinearLayout mainLayout = new LinearLayout(ContextReference.getAppContext());

		viewPager = (new ViewPager(ContextReference.getAppContext()));
		mainLayout.addView(viewPager);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		viewPager.setLayoutParams(params);
		myPagerAdapter = new MyPagerAdapter();
		viewPager.setAdapter(myPagerAdapter);
		return mainLayout;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (true)
			return setUpViewPagerTest();
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

	private class MyPagerAdapter extends PagerAdapter {

		int NumberOfPages = Eat4Health.FOOD_GROUP_COUNT;

		// int[] res = { android.R.drawable.ic_dialog_alert,
		// android.R.drawable.ic_menu_camera,
		// android.R.drawable.ic_menu_compass,
		// android.R.drawable.ic_menu_directions,
		// android.R.drawable.ic_menu_gallery };
		// int[] backgroundcolor = { 0xFF101010, 0xFF202020, 0xFF303030,
		// 0xFF404040, 0xFF505050 };

		@Override
		public int getCount() {
			return NumberOfPages;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// while (!Eat4Health.MY_FOOD_GROUPS[position++])
			// continue;

			LinearLayout child = ViewFactory.basicLinearLayout();

			Eat4HealthFunctions.addViewsFromStrings(Eat4Health.foodGroups[position], child, FG2_I.FOODGROUPS[position], foodWordsListener);

			ScrollView foodList = new ScrollView(ContextReference.getAppContext());
			foodList.addView(child);

			LinearLayout layout = new LinearLayout(ContextReference.getAppContext());
			layout.setOrientation(LinearLayout.VERTICAL);
			LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			layout.setBackgroundColor(0xFF202020);
			layout.setLayoutParams(layoutParams);
			layout.addView(foodList);

			final int page = position;
			layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(ContextReference.getAppContext(), "Page " + page + " clicked", Toast.LENGTH_LONG).show();
				}
			});

			container.addView(layout);
			return layout;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((LinearLayout) object);
		}

	}

}
