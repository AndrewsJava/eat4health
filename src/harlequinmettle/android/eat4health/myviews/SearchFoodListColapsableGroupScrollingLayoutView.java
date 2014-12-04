package harlequinmettle.android.eat4health.myviews;

import harlequinmettle.android.eat4health.Eat4Health;
import harlequinmettle.android.tools.androidsupportlibrary.ContextReference;
import harlequinmettle.android.tools.androidsupportlibrary.TextViewFactory;
import harlequinmettle.android.tools.androidsupportlibrary.ViewFactory;

import java.util.HashMap;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class SearchFoodListColapsableGroupScrollingLayoutView extends LinearLayout {
	Context context;
	EditText searchBox;
	ScrollView scrollingFilterableContainer;
	LinearLayout child;
	TextView[] groupLabels = new TextView[Eat4Health.FOOD_GROUP_COUNT];
	// ScrollView[] groupContents = new ScrollView[Eat4Health.FOOD_GROUP_COUNT];
	LinearLayout[] groupContents = new LinearLayout[Eat4Health.FOOD_GROUP_COUNT];
	HashMap<Integer, Integer> indexMap = new HashMap<Integer, Integer>();

	private OnClickListener colapseListener = new View.OnClickListener() {

		public void onClick(View view) {
			// default textview id is hashcode of text
			int id = view.getId();
			toggleViewVisibility(id);
		}

		private void toggleViewVisibility(int id) {
			View toggleable = child.findViewById(getIdForGroupContentLayout(indexMap.get(id)));
			if (child == null)
				child.addView(toggleable, indexMap.get(id));
			else
				child.removeView(child.findViewById(getIdForGroupContentLayout(indexMap.get(id))));
		}
	};

	public SearchFoodListColapsableGroupScrollingLayoutView() {
		super(ContextReference.getAppContext());
		context = ContextReference.getAppContext();
		setUpSearchabelList();
	}

	public SearchFoodListColapsableGroupScrollingLayoutView(Context context) {
		super(context);
		context = ContextReference.getAppContext();
		setUpSearchabelList();
	}

	private void setUpSearchabelList() {
		searchBox = new EditText(context);
		scrollingFilterableContainer = new ScrollView(context);
		scrollingFilterableContainer.setBackgroundColor(0xFF202020);
		child = ViewFactory.basicLinearLayout();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER_VERTICAL);
		setLayoutParams(params);
		setOrientation(VERTICAL);

		addView(scrollingFilterableContainer);
		scrollingFilterableContainer.addView(child);
		setUpViews();
	}

	private void setUpViews() {
		for (int i = 0; i < Eat4Health.FOOD_GROUP_COUNT; i++) {
			groupLabels[i] = TextViewFactory.makeLeftTextView(Eat4Health.foodGroups[i]);
			indexMap.put(Eat4Health.foodGroups[i].hashCode(), i);
			groupLabels[i].setOnClickListener(colapseListener);
			groupContents[i] = ViewFactory.basicLinearLayout();
			groupContents[i].setId(getIdForGroupContentLayout(i));
			addContentsToGroup(i);
			child.addView(groupLabels[i]);
			child.addView(groupContents[i]);
		}
	}

	private void addContentsToGroup(int group) {
		for (int foodId : Eat4Health.foodsByGroup[group]) {
			TextView food = TextViewFactory.makeDefaultTextView(Eat4Health.foods[foodId]);

			groupContents[group].addView(food);
		}
	}

	private int getIdForGroupContentLayout(int i) {

		return i * 1000;
	}

}
