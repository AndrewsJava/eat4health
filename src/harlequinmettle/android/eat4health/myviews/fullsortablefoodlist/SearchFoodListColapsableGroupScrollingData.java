package harlequinmettle.android.eat4health.myviews.fullsortablefoodlist;

import harlequinmettle.android.eat4health.Eat4Health;
import harlequinmettle.android.tools.androidsupportlibrary.overridecustomization.CustomScrollView;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchFoodListColapsableGroupScrollingData extends LinearLayout {

	Context context;
	EditText searchBox;
	TextView imobileLabel;
	CustomScrollView scrollingFilterableContainer;
	LinearLayout child;
	boolean[] isCurrentlyViewableInScroll = new boolean[Eat4Health.FOOD_GROUP_COUNT];
	LinearLayout[] groupInsertableContainers = new LinearLayout[Eat4Health.FOOD_GROUP_COUNT];
	TextView[] groupLabels = new TextView[Eat4Health.FOOD_GROUP_COUNT];
	ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, TextView>> groupHashCodeFoodTextMap = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, TextView>>();
	// ScrollView[] groupContents = new ScrollView[Eat4Health.FOOD_GROUP_COUNT];
	LinearLayout[] groupContents = new LinearLayout[Eat4Health.FOOD_GROUP_COUNT];
	HashMap<Integer, Integer> indexMap = new HashMap<Integer, Integer>();
	int viewIndex = 0;
	boolean focusUp = false;

	protected int getIdForGroupBoundaryMarkerLayout(int i) {

		return i * 10000000;
	}

	protected int getIdForGroupContentInsertableContainersLayout(int i) {

		return i * 100000;
	}

	protected int getIdForGroupContentLayout(int i) {

		return i * 1000;
	}

	protected SearchFoodListColapsableGroupScrollingData(Context context) {
		super(context);
	}

}
