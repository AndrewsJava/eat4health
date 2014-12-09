package harlequinmettle.android.eat4health.myviews.fullsortablefoodlist;

import harlequinmettle.android.eat4health.Eat4Health;
import harlequinmettle.android.tools.androidsupportlibrary.TextViewFactory;
import harlequinmettle.android.tools.androidsupportlibrary.ViewFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchFoodListColapsableGroupScrollingAsyncTasksAndListeners extends SearchFoodListColapsableGroupScrollingData {
	protected void updateFloatingTextLabel(int scrolltop) {
		boolean useDefaultText = false;
		int i = 0;
		for (; i < Eat4Health.FOOD_GROUP_COUNT; i++) {

			if (i >= Eat4Health.FOOD_GROUP_COUNT) {
				useDefaultText = true;
				break;
			}
			if (groupInsertableContainers[i] == null)
				continue;
			if (groupInsertableContainers[i].getY() - scrolltop <= 0 && groupInsertableContainers[i].getBottom() - scrolltop > 0)
				break;
		}
		if (i >= Eat4Health.FOOD_GROUP_COUNT) {
			useDefaultText = true;
		}
		String text = defaultFloatingLabelText;
		if (!useDefaultText)
			text = groupLabels[i].getText().toString();

		imobileLabel.setText(text);
		imobileLabel.setId(text.hashCode());

	}

	protected OnClickListener colapseListener = new View.OnClickListener() {

		public void onClick(View view) {
			// default textview id is hashcode of text
			int id = view.getId();
			if (!indexMap.containsKey(id))
				return;
			int labelHashCodeToIntID = indexMap.get(id);
			toggleViewVisibility(labelHashCodeToIntID);
		}

		private void toggleViewVisibility(int index) {

			LinearLayout toggleableContainer = groupInsertableContainers[index];
			if (toggleableContainer == null)
				return;
			LinearLayout hideable = groupContents[index];
			if (!isCurrentlyViewableInScroll[index]) {
				isCurrentlyViewableInScroll[index] = true;
				toggleableContainer.addView(hideable);
			} else {
				isCurrentlyViewableInScroll[index] = false;
				toggleableContainer.removeView(hideable);
				focusOnView(index);
			}
		}

		private final void focusOnView(final int index) {
			new Handler().post(new Runnable() {
				@Override
				public void run() {
					if (index + 1 < Eat4Health.FOOD_GROUP_COUNT) {
						scrollingFilterableContainer.scrollTo(0, groupInsertableContainers[index].getBottom());
					}
				}

			});
		}
	};
	protected TextWatcher textFilterInputListener = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (s.length() < 3)
				return;
			if (searchFilterAsyncTask != null) {
				searchFilterAsyncTask.cancel(true);
				tryToSleep(10);
			}
			searchFilterAsyncTask = new FilterFoodsToUIListAsyncTask();
			searchFilterAsyncTask.execute(s.toString());
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

		@Override
		protected void onProgressUpdate(Integer... iArray) {
			int i = iArray[0];
			if (Eat4Health.MY_FOOD_GROUPS[i]) {

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

				// groupInsertableContainers[i].setId(getIdForGroupContentInsertableContainersLayout(i));
				// groupContents[i].setId(getIdForGroupContentLayout(i));
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

	protected class FilterFoodsToUIListAsyncTask extends AsyncTask<String, CopyOnWriteArrayList<Integer>, Void> {

		@Override
		protected void onProgressUpdate(CopyOnWriteArrayList<Integer>... iArray) {
			CopyOnWriteArrayList<Integer> filteredSet = iArray[0];
			int group = filteredSet.get(0);
			Log.v("filter", "group: " + group);
			ConcurrentHashMap<Integer, TextView> groupMap = groupHashCodeFoodTextMap.get(group);
			LinearLayout container = groupContents[group];
			container.removeAllViews();
			for (int hashCode : filteredSet) {

				if (isCancelled())
					break;
				TextView foodType = groupMap.get(hashCode);
				if (foodType == null)
					continue;
				Log.v("filter", "TextView ref: " + group);
				container.addView(foodType);
			}

		}

		@Override
		protected Void doInBackground(String... params) {
			String filterFor = params[0];
			for (int i = 0; i < Eat4Health.FOOD_GROUP_COUNT; i++) {

				if (isCancelled())
					break;
				if (!Eat4Health.MY_FOOD_GROUPS[i])
					continue;
				CopyOnWriteArrayList<Integer> filteredHashCodes = new CopyOnWriteArrayList<Integer>();
				filteredHashCodes.add(i);
				// addContentsToGroup(i);
				filterGroup(i, filterFor, filteredHashCodes);
				publishProgress(filteredHashCodes);
			}
			return null;
		}

		private void filterGroup(int i, String filterFor, CopyOnWriteArrayList<Integer> filteredHashCodes) {
			int[] groupFoodIndicies = Eat4Health.foodsByGroup[i];
			for (int index : groupFoodIndicies) {

				if (isCancelled())
					break;
				String foodText = Eat4Health.foods[index];

				Log.v("filter", " checking food: " + foodText);
				if (foodText.contains(filterFor))
					filteredHashCodes.add(foodText.hashCode());
			}

		}

		// get text from edittext
		// iterate through foods compare (possibly regex)
		// check for thread cancelation
		// separate results - partial results - misses
		// display results

		// private void addContentsToGroup(int group) {
		// int i = 0;
		// for (int foodId : Eat4Health.foodsByGroup[group]) {
		// TextView food =
		// TextViewFactory.makeDefaultTextView(Eat4Health.foods[foodId]);
		//
		// groupContents[group].addView(food);
		// }
		// }

	}

	protected SearchFoodListColapsableGroupScrollingAsyncTasksAndListeners(Context context) {
		super(context);
	}

}
