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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchFoodListColapsableGroupScrollingAsyncTasksAndListeners extends SearchFoodListColapsableGroupScrollingData {
	// //BUILD UI FOR FOOD LIST
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
			// int i = 0;
			ConcurrentHashMap<Integer, TextView> groupMap = new ConcurrentHashMap<Integer, TextView>();

			for (int foodId : Eat4Health.foodsByGroup[group]) {
				TextView food = TextViewFactory.makeDefaultTextView(Eat4Health.foods[foodId]);
				groupContents[group].addView(food);
				groupMap.put(food.getId(), food);

			}
			groupHashCodeFoodTextMap.put(group, groupMap);
		}
	}

	// ///////////////////////
	// //////////////////////

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
			String searchText = s.toString().toLowerCase();
			if (searchText.length() < 2)
				return;
			if (searchFilterAsyncTask != null) {
				searchFilterAsyncTask.cancel(true);
				tryToSleep(10);
			}
			searchFilterAsyncTask = new FilterFoodsToUIListAsyncTask();
			searchFilterAsyncTask.execute(searchText);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	// //SEARCH FOODS ASYNCTASK
	protected class FilterFoodsToUIListAsyncTask extends AsyncTask<String, CopyOnWriteArrayList<Integer>, Void> {

		@Override
		protected void onProgressUpdate(CopyOnWriteArrayList<Integer>... iArray) {
			CopyOnWriteArrayList<Integer> filteredSet = iArray[0];
			int group = filteredSet.remove(0);
			ConcurrentHashMap<Integer, TextView> groupMap = groupHashCodeFoodTextMap.get(group);
			LinearLayout container = groupContents[group];
			container.removeAllViews();
			for (int result : filteredSet) {

				if (isCancelled())
					break;
				if (groupMap.containsKey(result))
					container.addView(groupMap.get(result));

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
				String foodText = Eat4Health.foods[index].toLowerCase();

				if (foodText.contains(filterFor)) {

					filteredHashCodes.add(foodText.hashCode());
				}
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
