package harlequinmettle.android.eat4health.legacyconversion;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import harlequinmettle.android.eat4health.Eat4Health;
import harlequinmettle.android.tools.androidsupportlibrary.ContextReference;

import java.util.ArrayList;
import java.util.TreeMap;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class NutrientSearchView extends LinearLayout {
	CheckBox[] highs = new CheckBox[130];
	CheckBox[] lows = new CheckBox[130];
	static final int HIGH_COLOR = 0xFF22FF22;
	static final int LOW_COLOR = 0xffff2222;

	static final int SEARCH_BUTTON = 111111111;
	static final int RESET_BUTTON = 22222222;
	View.OnClickListener nutrientSearchListener = new View.OnClickListener() {
		// added to keyword buttons ~2000
		public void onClick(View view) {
			int id = view.getId();
			Eat4Health.appAccess.removeView(Eat4Health.appAccess.findViewById(SubScroll.FOOD_RESULTS_ID));
			Eat4Health.appAccess.removeView(Eat4Health.appAccess.findViewById(SubScroll.FOOD_NUTRIENT_ID));
			SubScroll.lastFoodsId = 2;
			if (id == RESET_BUTTON) {

				for (int i = 0; i < 130; i++) {

					highs[i].setChecked(false);
					lows[i].setChecked(false);

					// highs[i].invalidate();
					// lows[i].invalidate();
				}

			}
			if (id == SEARCH_BUTTON) {
				ArrayList<Integer> highSearch = new ArrayList<Integer>();
				ArrayList<Integer> lowSearch = new ArrayList<Integer>();
				for (int i = 0; i < 130; i++) {
					// float dataPoint = Eat4Health.db[i][nID];
					if (!Eat4Health.MY_NUTRIENTS[i])
						continue;
					if (highs[i].isChecked())
						highSearch.add(i);
					if (lows[i].isChecked())
						lowSearch.add(i);

				}
				doNutrientSearch(highSearch, lowSearch);
			}
		}
	};

	public NutrientSearchView(Context context) {
		super(context);

		// this.setGravity(Gravity.CENTER_HORIZONTAL)
		this.setOrientation(VERTICAL);
		// /////////////////////////////////////
		// add high low title
		LinearLayout titleBar = new LinearLayout(context);
		titleBar.setMinimumWidth(400);
		// titleBar.setOrientation(Horizontal)//default is horizontal
		TextView tvHigh = new TextView(context);
		TextView tvLow = new TextView(context);
		tvHigh.setBackgroundColor(HIGH_COLOR);
		tvHigh.setTextColor(0xff000000);
		tvHigh.setText("High In");
		tvHigh.setWidth(200);
		tvHigh.setHeight(50);

		tvLow.setBackgroundColor(LOW_COLOR);
		tvLow.setTextColor(0xff000000);
		tvLow.setText("Low In");
		tvLow.setWidth(200);
		tvLow.setHeight(50);

		titleBar.addView(tvHigh);
		titleBar.addView(tvLow);

		this.addView(titleBar);
		// //////////////////////////////////////
		// add scroll checkbox/button/checkbox
		ScrollView nutrientScroll = new ScrollView(context);
		LinearLayout iChild = basicLinearLayout(context);
		nutrientScroll.addView(iChild);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1.0f);
		nutrientScroll.setLayoutParams(params);

		// optional add radiogroup or two for search options to top of scroll
		// eg 100g or 100cal and allfoodgroups myfoodgroups 1foodgroup or
		// myfoods only
		for (int i = 0; i < 130; i++) {
			// float dataPoint = Eat4Health.db[i][nID];
			if (!Eat4Health.MY_NUTRIENTS[i]) {
				highs[i] = new CheckBox(context);
				lows[i] = new CheckBox(context);
				continue;
			}
			LinearLayout searchParam = new LinearLayout(context);// horizontal
																	// by
																	// default
			searchParam.setMinimumWidth(400);
			searchParam.setGravity(Gravity.CENTER_VERTICAL);
			CheckBox highIn = new CheckBox(context);
			CheckBox lowIn = new CheckBox(context);
			highs[i] = highIn;
			lows[i] = lowIn;
			TextView nutrientT = new TextView(context);
			nutrientT.setHeight(50);
			nutrientT.setWidth(300);
			nutrientT.setGravity(Gravity.CENTER);

			highIn.setWidth(50);

			lowIn.setWidth(50);

			// highIn.setId(2000000 + i);
			// lowIn.setId(1000000 + i);

			highIn.setBackgroundColor(HIGH_COLOR);
			lowIn.setBackgroundColor(LOW_COLOR);

			// highIn.setGravity(Gravity.LEFT);
			// lowIn.setGravity(Gravity.RIGHT);
			int bgColor = SubScroll.GENERAL;
			if (i >= 8 && i <= 14) {
				bgColor = SubScroll.INTERESTING;
			}
			if (i >= 15 && i <= 24) {
				bgColor = SubScroll.CARB;

			}
			if (i >= 25 && i <= 35) {
				bgColor = SubScroll.MINERAL;

			}
			if (i >= 36 && i <= 65) {
				bgColor = SubScroll.VITAMIN;

			}
			if (i >= 66 && i <= 84) {
				bgColor = SubScroll.PROTEIN;

			}
			if (i >= 85 && i <= 129) {
				bgColor = SubScroll.FAT;

			}
			int alpha = 150;

			int colorCombo = bgColor | (alpha << 24);
			nutrientT.setBackgroundColor(colorCombo);
			nutrientT.setText(Eat4Health.nutrients[i]);

			searchParam.addView(highIn);
			searchParam.addView(nutrientT);
			searchParam.addView(lowIn);
			iChild.addView(searchParam);
		}

		this.addView(nutrientScroll);
		// ///////////////////////////////////////
		// add search submit button/buttons
		LinearLayout bottomButtons = new LinearLayout(context);
		bottomButtons.setGravity(Gravity.CENTER);
		Button search = simpleButton(context);
		Button clear = simpleButton(context);
		search.setText("Search");
		clear.setText("Clear");
		search.setId(SEARCH_BUTTON);
		clear.setId(RESET_BUTTON);
		search.setOnClickListener(nutrientSearchListener);
		clear.setOnClickListener(nutrientSearchListener);
		bottomButtons.addView(search);
		bottomButtons.addView(clear);

		this.addView(bottomButtons);
	}

	public Button simpleButton(Context context) {
		Button button = new Button(context);
		button.setHeight(WRAP_CONTENT);
		button.setMaxWidth(400);
		button.setMaxLines(5);
		button.setTextSize(24);
		return button;
	}

	// ///////////////////////////////////////
	public LinearLayout basicLinearLayout(Context context) {
		LinearLayout basic = new LinearLayout(context);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
		basic.setLayoutParams(params);
		basic.setOrientation(VERTICAL);
		return basic;
	}

	public static void doNutrientSearch(ArrayList<Integer> h, ArrayList<Integer> l) {

		long time = System.currentTimeMillis();
		TreeMap<Float, Integer> foodRanking = new TreeMap<Float, Integer>();
		int[] foodsToSearch = Eat4Health.getFoodSearchIds();

		float[] rankHigh = new float[Eat4Health.FOOD_COUNT];
		float[] rankLow = new float[Eat4Health.FOOD_COUNT];
		if (false)
			for (int i = 0; i < Eat4Health.FOOD_COUNT; i++) {
				rankHigh[i] = -100;
				rankLow[i] = 100;
			}
		for (int i : foodsToSearch) {
			rankHigh[i] = 1;
			rankLow[i] = 1;
		}
		float null_rank_constant = 0;
		float measuredFactor = 1.0f;

		for (int nutrientID : h) {

			float[] nstat = Eat4Health.highlightFactors.get(nutrientID);
			float min = nstat[0];
			float mean = nstat[1];
			float max = nstat[2];
			// OPTIONS FOR SEARCHING 1 FOOD GROUP/ MYFOODS/ MYFOODGROUPS
			for (int i : foodsToSearch) {

				float dataPoint = Eat4Health.db[nutrientID][i];

				if (dataPoint < 0) {
					rankHigh[i] += null_rank_constant;
					continue;
				}

				switch (Eat4Health.Nutrient_Measure) {
				case Eat4Health.USING_KCAL:
					dataPoint = Eat4Health.getPer100KcalDataPoint(i, dataPoint);

					break;
				case Eat4Health.USING_SERVING:
					dataPoint = Eat4Health.getPerServingDataPoint(i, dataPoint);
					if (dataPoint < 0) {

						dataPoint = Eat4Health.db[nutrientID][i];
					}
					break;
				case Eat4Health.USING_GRAMS:

					break;
				// ADD CASE ID_PER_SERVING:
				default:

					break;
				}

				float relativeData = dataPoint / max;

				if (relativeData > 1)
					relativeData = (float) (1 + Math.atan(relativeData - 1) / 2);
				// ranks[i] -=
				// Eat4Health.getRelativeAbundance(nutrientID,dataPoint);
				switch (Eat4Health.Search_Type) {
				case Eat4Health.SEARCH_TYPE_SUM:
					rankHigh[i] += (measuredFactor + relativeData);
					break;

				case Eat4Health.SEARCH_TYPE_PRODUCT:
					rankHigh[i] *= (measuredFactor + relativeData);

					break;
				default:
					break;

				}

				rankHigh[i] -= (Math.random() / 100000000000.0);
			}
		}

		for (int nutrientID : l) {

			float[] nstat = Eat4Health.highlightFactors.get(nutrientID);
			float min = nstat[0];
			float mean = nstat[1];
			float max = nstat[2];

			for (int i : foodsToSearch) {

				float dataPoint = Eat4Health.db[nutrientID][i];
				if (dataPoint < 0) {
					rankLow[i] += null_rank_constant;
					continue;

				}

				switch (Eat4Health.Nutrient_Measure) {
				case Eat4Health.USING_KCAL:
					dataPoint = Eat4Health.getPer100KcalDataPoint(i, dataPoint);

					break;
				case Eat4Health.USING_SERVING:
					dataPoint = Eat4Health.getPerServingDataPoint(i, dataPoint);
					if (dataPoint < 0) {

						dataPoint = Eat4Health.db[nutrientID][i];
					}
					break;
				case Eat4Health.USING_GRAMS:

					break;
				// ADD CASE ID_PER_SERVING:
				default:

					break;
				}
				float relativeData = dataPoint / max;

				if (relativeData > 1)
					relativeData = (float) (1 + Math.atan(relativeData - 1));
				// ranks[i] -=
				// Eat4Health.getRelativeAbundance(nutrientID,dataPoint);
				switch (Eat4Health.Search_Type) {
				case Eat4Health.SEARCH_TYPE_SUM:
					rankLow[i] += (measuredFactor + relativeData);
					break;

				case Eat4Health.SEARCH_TYPE_PRODUCT:
					rankLow[i] *= (measuredFactor + relativeData);
					break;
				default:
					break;

				}

				rankLow[i] -= (Math.random() / 100000000000.0);
			}
		}
		int ctr = 0;
		for (int i : foodsToSearch) {
			float highRank = -(rankHigh[i]);
			float lowRank = (rankLow[i]);
			// order of tree set is from lowest to highest
			// high ranking use negative
			foodRanking.put(lowRank + highRank, i);
		}
		int counter = 0;
		int MAX = foodRanking.size() - 1;
		if (MAX > 300)
			MAX = 300;
		if (MAX < 1)
			MAX = 2;
		String[] priorityResults = new String[MAX];
		int[] pResIDs = new int[MAX];
		for (int rank : foodRanking.values()) {

			priorityResults[counter] = Eat4Health.foods[rank];
			pResIDs[counter] = rank;

			if (counter++ == MAX - 1)
				break;
		}

		System.out.println("---::>DONE SEARCH:  " + (System.currentTimeMillis() - time));
		Eat4Health.searchResults = priorityResults;
		Eat4Health.foodCodeResults = pResIDs;
		// Eat4Health.appAccess.removeViewAt(2);
		FoodDescriptionsScroller fds = new FoodDescriptionsScroller(ContextReference.getAppContext(), true);
		Eat4Health.appAccess.addView(fds, 2);

		final ViewTreeObserver viewTreeObserver4 = Eat4Health.application.getViewTreeObserver();
		if (viewTreeObserver4.isAlive()) {
			viewTreeObserver4.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {

					Eat4Health.application.scrollBy((int) (0.9 * SubScroll.MAX_BUTTON_WIDTH), 0);
					viewTreeObserver4.removeGlobalOnLayoutListener(this);
				}
			});
		}

	}// END OF SEARCH

}// END OF CLASS
