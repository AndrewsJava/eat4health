package harlequinmettle.android.eat4health.myviews.fullsortablefoodlist;

import harlequinmettle.android.eat4health.Eat4Health;
import harlequinmettle.android.eat4health.myviews.fullsortablefoodlist.SearchFoodListColapsableGroupScrollingAsyncTasksAndListeners.FilterFoodsToUIListAsyncTask;
import harlequinmettle.android.tools.androidsupportlibrary.overridecustomization.CustomScrollView;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchFoodListColapsableGroupScrollingData extends LinearLayout {

	protected Context context;
	protected EditText searchBox;
	protected TextView imobileLabel;
	protected CustomScrollView scrollingFilterableContainer;
	protected LinearLayout child;
	protected FilterFoodsToUIListAsyncTask searchFilterAsyncTask;
	protected final boolean[] isCurrentlyViewableInScroll = new boolean[Eat4Health.FOOD_GROUP_COUNT];
	protected final LinearLayout[] groupInsertableContainers = new LinearLayout[Eat4Health.FOOD_GROUP_COUNT];
	protected final TextView[] groupLabels = new TextView[Eat4Health.FOOD_GROUP_COUNT];
	protected final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, TextView>> groupHashCodeFoodTextMap = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, TextView>>();
	protected final LinearLayout[] groupContents = new LinearLayout[Eat4Health.FOOD_GROUP_COUNT];
	protected final HashMap<Integer, Integer> indexMap = new HashMap<Integer, Integer>();
	protected final String defaultFloatingLabelText = "Current Category (click to hide category)";

	protected void tryToSleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// protected int getIdForGroupContentInsertableContainersLayout(int i) {
	//
	// return i * 100000;
	// }
	//
	// protected int getIdForGroupContentLayout(int i) {
	//
	// return i * 1000;
	// }

	protected SearchFoodListColapsableGroupScrollingData(Context context) {
		super(context);
	}

}
