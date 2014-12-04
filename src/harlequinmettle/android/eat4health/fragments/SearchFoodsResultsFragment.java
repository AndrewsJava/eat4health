package harlequinmettle.android.eat4health.fragments;

import harlequinmettle.android.eat4health.Eat4Health;
import harlequinmettle.android.eat4health.Eat4HealthFunctions;
import harlequinmettle.android.tools.androidsupportlibrary.ContextReference;
import harlequinmettle.android.tools.androidsupportlibrary.CustomTextView;
import harlequinmettle.android.tools.androidsupportlibrary.ViewFactory;
import android.app.Fragment;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

public class SearchFoodsResultsFragment extends Fragment {

	public SearchFoodsResultsFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		LinearLayout child = ViewFactory.basicLinearLayout();

		Eat4HealthFunctions.addViewsFromStrings("heading", child, Eat4Health.searchResults, foodGroupSearchListener);

		ScrollView foodList = new ScrollView(ContextReference.getAppContext());
		foodList.addView(child);

		LinearLayout layout = new LinearLayout(ContextReference.getAppContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layout.setBackgroundColor(0xFF202020);
		layout.setLayoutParams(layoutParams);
		layout.addView(foodList);

		container.removeAllViews();
		container.addView(layout);

		getActivity().setTitle(" new fragment ");
		return layout;

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
			b.getBackground().setColorFilter(0xff999999, PorterDuff.Mode.MULTIPLY);
			String searchWord = b.getText().toString();

			Eat4Health.setSearchResultsFrom(searchWord, id);

			Eat4Health.appSelf.setFragment(new SearchFoodsResultsFragment(), searchWord);

		}
	};
}
