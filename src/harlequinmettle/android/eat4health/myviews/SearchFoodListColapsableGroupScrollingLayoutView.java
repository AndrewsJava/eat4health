package harlequinmettle.android.eat4health.myviews;

import harlequinmettle.android.eat4health.Eat4Health;
import harlequinmettle.android.tools.androidsupportlibrary.ContextReference;
import harlequinmettle.android.tools.androidsupportlibrary.TextViewFactory;
import harlequinmettle.android.tools.androidsupportlibrary.ViewFactory;
import harlequinmettle.android.tools.androidsupportlibrary.interfaces.ScrollObjectOutOfViewCallBack;
import harlequinmettle.android.tools.androidsupportlibrary.overridecustomization.CustomScrollView;

import java.util.HashMap;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchFoodListColapsableGroupScrollingLayoutView extends LinearLayout implements ScrollObjectOutOfViewCallBack {
	Context context;
	EditText searchBox;
	TextView imobileLabel;
	CustomScrollView scrollingFilterableContainer;
	LinearLayout child;
	boolean[] isViewable = new boolean[Eat4Health.FOOD_GROUP_COUNT];
	LinearLayout[] groupInsertableContainers = new LinearLayout[Eat4Health.FOOD_GROUP_COUNT];
	TextView[] groupLabels = new TextView[Eat4Health.FOOD_GROUP_COUNT];
	// ScrollView[] groupContents = new ScrollView[Eat4Health.FOOD_GROUP_COUNT];
	LinearLayout[] groupContents = new LinearLayout[Eat4Health.FOOD_GROUP_COUNT];
	HashMap<Integer, Integer> indexMap = new HashMap<Integer, Integer>();
	int viewIndex = 0;
	private OnClickListener colapseListener = new View.OnClickListener() {

		public void onClick(View view) {
			// default textview id is hashcode of text
			int id = view.getId();
			int labelHashCodeToIntID = indexMap.get(id);
			toggleViewVisibility(labelHashCodeToIntID);
		}

		private void toggleViewVisibility(int id) {

			LinearLayout toggleableContainer = groupInsertableContainers[id];
			if (toggleableContainer == null)
				return;
			LinearLayout hideable = groupContents[id];
			if (!isViewable[id]) {
				isViewable[id] = true;
				toggleableContainer.addView(hideable);
			} else {
				isViewable[id] = false;
				toggleableContainer.removeView(hideable);
				focusOnView(id);
			}
		}
	};

	public SearchFoodListColapsableGroupScrollingLayoutView() {
		super(ContextReference.getAppContext());
		setUpSearchabelList();
	}

	public SearchFoodListColapsableGroupScrollingLayoutView(Context context) {
		super(context);
		setUpSearchabelList();
	}

	private void setUpSearchabelList() {
		this.context = ContextReference.getAppContext();
		setCustomizations();
		constructLayout();
		new LoadFoodsToUIListAsyncTask().execute();
	}

	private void constructLayout() {

		searchBox = new EditText(context);
		searchBox.setBackgroundColor(0xffffffff);

		imobileLabel = TextViewFactory.makeAnotherTextView("stationary food group label");
		imobileLabel.setBackgroundColor(0xff454545);

		scrollingFilterableContainer = new CustomScrollView();
		child = ViewFactory.basicLinearLayout();
		scrollingFilterableContainer.addView(child);
		scrollingFilterableContainer.registerCallBack(this);
		addView(searchBox);
		addView(imobileLabel);
		addView(scrollingFilterableContainer);
	}

	private void setCustomizations() {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER_VERTICAL);
		setLayoutParams(params);
		setOrientation(VERTICAL);
		setBackgroundColor(0xFF202020);
	}

	// protected void tryToSleep(long time) {
	// try {
	// Thread.sleep(time);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }

	private int getIdForGroupBoundaryMarkerLayout(int i) {

		return i * 10000000;
	}

	private int getIdForGroupContentInsertableContainersLayout(int i) {

		return i * 100000;
	}

	private int getIdForGroupContentLayout(int i) {

		return i * 1000;
	}

	private final void focusOnView(final int index) {
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				if (index + 1 < Eat4Health.FOOD_GROUP_COUNT)

					scrollingFilterableContainer.scrollTo(0, groupInsertableContainers[index].getTop());
				// animateScroll(index);

			}

			// private void animateScroll(int index) {
			// while
			// (scrollingFilterableContainer.isViewVisible(groupInsertableContainers[index]))
			// scrollingFilterableContainer.scrollBy(0, 1);
			//
			// }
		});
	}

	@Override
	public void doWhenViewScollsOutOfView() {

		String text = Eat4Health.foodGroups[viewIndex];
		viewIndex++;
		while (!Eat4Health.MY_FOOD_GROUPS[viewIndex])
			viewIndex++;
		imobileLabel.setText(text);
		imobileLabel.setId(text.hashCode());
		imobileLabel.setOnClickListener(colapseListener);
		scrollingFilterableContainer.setVisibleView(groupLabels[viewIndex]);
	}

	@Override
	public void doWhenViewScollsIntoView() {
		int lastIndex = viewIndex;
		if (viewIndex > 0)
			viewIndex--;
		while (!Eat4Health.MY_FOOD_GROUPS[viewIndex])
			if (viewIndex > 0)
			viewIndex--;
			else viewIndex = lastIndex;
		String text = Eat4Health.foodGroups[viewIndex];
		imobileLabel.setText(text);
		imobileLabel.setId(text.hashCode());
		imobileLabel.setOnClickListener(colapseListener);
		scrollingFilterableContainer.setVisibleView(groupLabels[viewIndex]);

	}

	// ////////////////////////////////////////ASYNCTASK
	private class LoadFoodsToUIListAsyncTask extends AsyncTask<Void, Integer, Void> {
		boolean first = true;

		@Override
		protected void onProgressUpdate(Integer... iArray) {
			int i = iArray[0];
			if (Eat4Health.MY_FOOD_GROUPS[i]) {
				if (first) {
					scrollingFilterableContainer.setVisibleView(groupLabels[i]);
					viewIndex = i;
					first = false;
				}
				child.addView(groupInsertableContainers[i]);
			}
		}

		@Override
		protected void onPostExecute(Void v) {

		}

		@Override
		protected Void doInBackground(Void... params) {

			for (int i = 0; i < Eat4Health.FOOD_GROUP_COUNT; i++) {
				// if (!Eat4Health.MY_FOOD_GROUPS[i])
				// continue;

				isViewable[i] = true;
				groupLabels[i] = TextViewFactory.makeAnotherTextView(Eat4Health.foodGroups[i]);
				indexMap.put(Eat4Health.foodGroups[i].hashCode(), i);
				groupLabels[i].setOnClickListener(colapseListener);

				groupInsertableContainers[i] = ViewFactory.basicLinearLayout();
				groupContents[i] = ViewFactory.basicLinearLayout();

				groupInsertableContainers[i].setId(getIdForGroupContentInsertableContainersLayout(i));
				groupContents[i].setId(getIdForGroupContentLayout(i));
				addContentsToGroup(i);
				groupInsertableContainers[i].addView(groupLabels[i]);
				groupInsertableContainers[i].addView(groupContents[i]);
				publishProgress(i);
			}
			return null;
		}

		private void addContentsToGroup(int group) {
			int i = 0;
			for (int foodId : Eat4Health.foodsByGroup[group]) {
				TextView food = TextViewFactory.makeDefaultTextView(Eat4Health.foods[foodId]);

				groupContents[group].addView(food);
			}
		}

	}

}
