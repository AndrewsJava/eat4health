package harlequinmettle.android.eat4health.fragments;

import harlequinmettle.android.eat4health.Eat4Health;
import harlequinmettle.android.eat4health.Eat4HealthFunctions;
import harlequinmettle.android.eat4health.Eat4HealthListeners;
import harlequinmettle.android.eat4health.staticdataarrays.FG2_I;
import harlequinmettle.android.tools.androidsupportlibrary.ContextReference;
import harlequinmettle.android.tools.androidsupportlibrary.CustomTextView;
import harlequinmettle.android.tools.androidsupportlibrary.ViewFactory;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

public class FoodListViewPagerFragment extends Fragment {

	ViewPager viewPager;
	MyPagerAdapter myPagerAdapter;

	public FoodListViewPagerFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		LinearLayout mainLayout = new LinearLayout(ContextReference.getAppContext());

		viewPager = (new ViewPager(ContextReference.getAppContext()));
		mainLayout.addView(viewPager);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		viewPager.setLayoutParams(params);
		myPagerAdapter = new MyPagerAdapter();
		viewPager.setAdapter(myPagerAdapter);

		getActivity().setTitle(Eat4HealthListeners.INTRO[0]);
		// return rootView;
		return mainLayout;
	}

	private class MyPagerAdapter extends PagerAdapter {

		int NumberOfPages = countPreferredGroups_setArrayIndicies();
		int[] preferredFoodGroupIndicies;

		@Override
		public int getCount() {
			return NumberOfPages;
		}

		private int countPreferredGroups_setArrayIndicies() {
			int prefcount = 0;
			int i = 0;
			for (boolean pref : Eat4Health.MY_FOOD_GROUPS) {
				if (pref)
					prefcount++;
				i++;
			}
			preferredFoodGroupIndicies = new int[prefcount];
			prefcount = 0;
			i = 0;
			for (boolean pref : Eat4Health.MY_FOOD_GROUPS) {
				if (pref)
					preferredFoodGroupIndicies[prefcount++] = i;
				i++;
			}
			return prefcount;
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

			Eat4HealthFunctions.addViewsFromStrings(Eat4Health.foodGroups[preferredFoodGroupIndicies[position]], child,
					FG2_I.FOODGROUPS[preferredFoodGroupIndicies[position]], foodGroupSearchListener);

			ScrollView foodList = new ScrollView(ContextReference.getAppContext());
			foodList.addView(child);

			LinearLayout layout = new LinearLayout(ContextReference.getAppContext());
			layout.setOrientation(LinearLayout.VERTICAL);
			LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			layout.setBackgroundColor(0xFF202020);
			layout.setLayoutParams(layoutParams);
			layout.addView(foodList);

			container.addView(layout);
			return layout;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((LinearLayout) object);
		}

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		View view = Eat4Health.appSelf.appContainer;
		if (view != null) {
			ViewGroup parentViewGroup = (ViewGroup) view.getParent();
			if (parentViewGroup != null) {
				parentViewGroup.removeAllViews();
			}
		}
	}

	View.OnClickListener foodGroupSearchListener = new View.OnClickListener() {
		// added to keyword buttons ~2000
		public void onClick(View view) {
			int id = view.getId();
			CustomTextView b = (CustomTextView) view;
			// b.getBackground().setColorFilter(0xff999999,
			// PorterDuff.Mode.MULTIPLY);
			String searchWord = b.getText().toString();

			Eat4Health.setSearchResultsFrom(searchWord, Eat4Health.USE_ALL_FOODS);

			Eat4Health.appSelf.setFragment(new SearchFoodsResultsFragment(), searchWord);

		}
	};

}
