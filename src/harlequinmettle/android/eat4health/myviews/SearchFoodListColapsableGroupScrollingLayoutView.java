package harlequinmettle.android.eat4health.myviews;

import harlequinmettle.android.eat4health.Eat4Health;
import harlequinmettle.android.tools.androidsupportlibrary.ContextReference;
import harlequinmettle.android.tools.androidsupportlibrary.TextViewFactory;
import harlequinmettle.android.tools.androidsupportlibrary.ViewFactory;

import java.util.HashMap;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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
	boolean[] isShowing = new boolean[Eat4Health.FOOD_GROUP_COUNT];
	LinearLayout[] groupInsertableContainers = new LinearLayout[Eat4Health.FOOD_GROUP_COUNT];
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
			int labelHashCodeToIntID = indexMap.get(id);
			// int containerID =
			// getIdForGroupContentInsertableContainersLayout(labelHashCodeToIntID);
			// int contentID = getIdForGroupContentLayout(labelHashCodeToIntID);

			LinearLayout toggleableContainer = groupInsertableContainers[labelHashCodeToIntID];
			if (toggleableContainer == null)
				return;
			LinearLayout hideable = groupContents[labelHashCodeToIntID];
			hideable.addView(TextViewFactory.makeDefaultTextView("laksjd;flsakdjf));alsdkjf;asldkjf;asldkfjas;ldkfjasdkf"));
			if (!isShowing[labelHashCodeToIntID]) {
				isShowing[labelHashCodeToIntID] = true;
				Log.v("show layout", "containerindex: " + labelHashCodeToIntID);
				toggleableContainer.addView(hideable);
			} else {
				isShowing[labelHashCodeToIntID] = false;
				toggleableContainer.removeView(hideable);
			}
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
		setBackgroundColor(0xFF202020);
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
			if (!Eat4Health.MY_FOOD_GROUPS[i])
				continue;
			new LoadFoodsToUIListAsyncTask().execute(i);
		}
	}

	private void addContentsToGroup(int group) {
		int i = 0;
		for (int foodId : Eat4Health.foodsByGroup[group]) {
			TextView food = TextViewFactory.makeDefaultTextView(Eat4Health.foods[foodId]);
			// if (i++ % 50 == 0)
			// tryToSleep(30);
			Thread.yield();
			groupContents[group].addView(food);
		}
	}

	protected void tryToSleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int getIdForGroupContentInsertableContainersLayout(int i) {

		return i * 100000;
	}

	private int getIdForGroupContentLayout(int i) {

		return i * 1000;
	}

	private class LoadFoodsToUIListAsyncTask extends AsyncTask<Integer, Integer, Integer> {
		boolean first = true;

		@Override
		protected void onProgressUpdate(Integer... progress) {

		}

		@Override
		protected void onPostExecute(final Integer i) {
			Eat4Health.appSelf.runOnUiThread(new Runnable() {
				public void run() {
					if (first) {
						first = false;
						tryToSleep(400);
					}
					child.addView(groupInsertableContainers[i.intValue()]);
				}
			});
			return;

		}

		@Override
		protected Integer doInBackground(Integer... params) {
			int i = params[0];
			isShowing[i] = true;
			groupLabels[i] = TextViewFactory.makeLeftTextView(Eat4Health.foodGroups[i]);
			indexMap.put(Eat4Health.foodGroups[i].hashCode(), i);
			groupLabels[i].setOnClickListener(colapseListener);
			groupInsertableContainers[i] = ViewFactory.basicLinearLayout();
			groupContents[i] = ViewFactory.basicLinearLayout();
			groupInsertableContainers[i].setId(getIdForGroupContentInsertableContainersLayout(i));
			groupContents[i].setId(getIdForGroupContentLayout(i));
			addContentsToGroup(i);
			groupInsertableContainers[i].addView(groupLabels[i]);
			groupInsertableContainers[i].addView(groupContents[i]);
			return i;
		}
	}
}
