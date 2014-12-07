package harlequinmettle.android.eat4health.myviews.fullsortablefoodlist;

import harlequinmettle.android.eat4health.Eat4Health;
import harlequinmettle.android.tools.androidsupportlibrary.TextViewFactory;
import harlequinmettle.android.tools.androidsupportlibrary.ViewFactory;

import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchFoodListColapsableGroupScrollingAsyncTasksAndListeners extends SearchFoodListColapsableGroupScrollingData {
	protected void updateFloatingTextLabel() {
		String text = Eat4Health.foodGroups[viewIndex];
		viewIndex++;
		while (!Eat4Health.MY_FOOD_GROUPS[viewIndex])
			viewIndex++;
		imobileLabel.setText(text);
		imobileLabel.setId(text.hashCode());
		scrollingFilterableContainer.setVisibleViewLabel(groupLabels[viewIndex]);
	}

	protected OnClickListener colapseListener = new View.OnClickListener() {

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
			if (!isCurrentlyViewableInScroll[id]) {
				isCurrentlyViewableInScroll[id] = true;
				toggleableContainer.addView(hideable);
			} else {
				isCurrentlyViewableInScroll[id] = false;
				toggleableContainer.removeView(hideable);
				focusOnView(id);
			}
		}

		private final void focusOnView(final int index) {
			new Handler().post(new Runnable() {
				@Override
				public void run() {
					if (index + 1 < Eat4Health.FOOD_GROUP_COUNT)

						scrollingFilterableContainer.scrollTo(0, groupInsertableContainers[index].getBottom());
					// animateScroll(index);
					 updateFloatingTextLabel();
				}

				// private void animateScroll(int index) {
				// while
				// (scrollingFilterableContainer.isViewVisible(groupInsertableContainers[index]))
				// scrollingFilterableContainer.scrollBy(0, 1);
				//
				// }
			});
		}
	};
	protected TextWatcher textFilterInputListener = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	// ////////////////////////////////////////ASYNCTASK
	protected class LoadFoodsToUIListAsyncTask extends AsyncTask<Void, Integer, Void> {
		boolean first = true;
		int foodcounter = 0;

		@Override
		protected void onProgressUpdate(Integer... iArray) {
			int i = iArray[0];
			if (Eat4Health.MY_FOOD_GROUPS[i]) {
				if (first) {
					scrollingFilterableContainer.setVisibleViewLabel(groupLabels[i]);
					// scrollingFilterableContainer.setVisibleViewContents(groupContents[i]);
					viewIndex = i;
					first = false;
				}
				child.addView(groupInsertableContainers[i]);
			}
		}

		@Override
		protected Void doInBackground(Void... params) {

			for (int i = 0; i < Eat4Health.FOOD_GROUP_COUNT; i++) {
				// if (!Eat4Health.MY_FOOD_GROUPS[i])
				// continue;
				groupHashCodeFoodTextMap.put(i, new ConcurrentHashMap<Integer, TextView>());
				isCurrentlyViewableInScroll[i] = true;
				groupLabels[i] = TextViewFactory.makeAnotherTextView(Eat4Health.foodGroups[i]);
				indexMap.put(Eat4Health.foodGroups[i].hashCode(), i);
				groupLabels[i].setOnClickListener(colapseListener);

				groupInsertableContainers[i] = ViewFactory.basicLinearLayout();
				groupContents[i] = ViewFactory.basicLinearLayout();

				groupInsertableContainers[i].setId(getIdForGroupContentInsertableContainersLayout(i));
				groupContents[i].setId(getIdForGroupContentLayout(i));
				addContentsToGroup(i);
				groupInsertableContainers[i].addView(groupLabels[i]);
				if (isCurrentlyViewableInScroll[i])
					groupInsertableContainers[i].addView(groupContents[i]);
				publishProgress(i);
			}
			return null;
		}

		private void addContentsToGroup(int group) {
			int i = 0;
			ConcurrentHashMap<Integer, TextView> groupMap = groupHashCodeFoodTextMap.get(i);
			for (int foodId : Eat4Health.foodsByGroup[group]) {
				TextView food = TextViewFactory.makeDefaultTextView(Eat4Health.foods[foodId]);
				groupContents[group].addView(food);
				groupMap.put(food.getId(), food);

			}
		}

	}

	// ///////////////////////
	// //////////////////////

	protected class FilterFoodsToUIListAsyncTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected void onProgressUpdate(Integer... iArray) {

		}

		@Override
		protected void onPostExecute(Void v) {

		}

		@Override
		protected Void doInBackground(Void... params) {

			for (int i = 0; i < Eat4Health.FOOD_GROUP_COUNT; i++) {
				if (!Eat4Health.MY_FOOD_GROUPS[i])
					continue;

				isCurrentlyViewableInScroll[i] = true;
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

	protected SearchFoodListColapsableGroupScrollingAsyncTasksAndListeners(Context context) {
		super(context);
	}

}
