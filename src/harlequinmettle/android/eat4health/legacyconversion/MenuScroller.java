package harlequinmettle.android.eat4health.legacyconversion;

import harlequinmettle.android.eat4health.Eat4Health;
import android.content.Context;
import android.graphics.PorterDuff;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MenuScroller extends SubScroll {

	static final String[] INTRO = { "Food Lists", "Word Search", "Search By Nutrient", "Evaluate Diet" };
	static final String[] PREFS = { "Food Groups", "Nutrients", "Set Nutrient Goals", "My Foods: View", "My Foods: Remove", "Options" };
	static final int[] NAV_IDS = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	static final int[] NAV_IDS_2 = { 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 };
	FoodDescriptionsScroller results;

	// initial navigation buttons menu: INTRO
	protected View.OnClickListener navigationListener = new View.OnClickListener() {

		public void onClick(View view) {
			// introGraphicsCounter = 0;
			int id = view.getId();
			resetDisplayConditions();
			SubScroll.fromKeywordSearch = false;
			SubScroll.fromFoodSuggestions = false;
			// ADD BOOLEAN FOR LONGER FILELOADING THREAD TO SHOW TOAST
			if (!Eat4Health._loaded) {
				Toast mToast = Toast.makeText(context, "\n\n\n\n\nPlease adjust some settings while database loads\n\n\n\n\n", 1);
				mToast.setGravity(Gravity.TOP, 0, 50);

				mToast.show();
			}

			Eat4Health.appAccess.removeAllViews();
			Eat4Health.appAccess.addView(Eat4Health.intro);

			// index = 1;
			switch (id) {
			case 0:// Search Foods by Food Group
				scrollinflated = new boolean[25];
				// load food group labeled scrolls
				scrollCount = 1;
				for (int i = 0; i < 25; i++) {
					// if i not in my food groups dont add to scroll
					if (!Eat4Health.MY_FOOD_GROUPS[i])
						continue;
					// original KeywordScroller - label button doesn't scroll
					// but inflates list of keywords
					KeywordScroller kws = new KeywordScroller(context, i);
					kws.setId(i);
					// linear layout to prevent title button from scrolling
					LinearLayout title = basicLinearLayout();
					// title button with kws.inflatorlistener id i
					Button titleButton = titleButton(kws, i);
					// add title button to layout
					title.addView(titleButton);
					// add uninflated keywordscroller to linear layout
					title.addView(kws);
					// add linear layout to application - default possition
					Eat4Health.appAccess.addView(title);

				}
				break;
			case 1:// search by typing a keyword
					// 2 causes search results/nutrition info to be placed to
					// right of text
				lastFoodsId = 2;
				results = new FoodDescriptionsScroller(context, false);
				SubScroll.fromKeywordSearch = true;
				LinearLayout title = basicLinearLayout();// vertical

				title.setId(scrollCount);

				ourInput = editText("Please enter a keyword");

				Button searchButton = simpleButton();
				searchButton.setText("Search");
				searchButton.setId(KEYWORD_SEARCH_ID);
				searchButton.setOnClickListener(foodGroupSearchListener);

				title.addView(ourInput);
				title.addView(searchButton);
				title.addView(results);
				// title.addView(kws2);

				Eat4Health.appAccess.addView(title, 1);

				break;
			case 2:// nutrient priority search view
				NutrientSearchView nsv = new NutrientSearchView(context);
				Eat4Health.appAccess.addView(nsv);

				break;

			case 3:
				fromFoodSuggestions = true;
				System.out.println("------>IN CASE 4 CREATE NUTRITIONCALC<---------------");
				NutritionCalculatorView caclulateNutritionState = new NutritionCalculatorView();
				caclulateNutritionState.setId(NUTRITION_CALCULATOR_ID);
				Eat4Health.appAccess.addView(caclulateNutritionState);

				break;
			default:
				break;

			}
			final ViewTreeObserver viewTreeObserver4 = Eat4Health.application.getViewTreeObserver();
			if (viewTreeObserver4.isAlive()) {
				viewTreeObserver4.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {

						Eat4Health.application.scrollBy((int) (0.9 * MAX_BUTTON_WIDTH), 0);
						viewTreeObserver4.removeGlobalOnLayoutListener(this);
					}
				});
			}
		}

	};

	protected View.OnClickListener settingsListener = new View.OnClickListener() {
		public void onClick(View view) {
			int id = view.getId();

			SubScroll.fromKeywordSearch = false;
			SubScroll.fromFoodSuggestions = false;
			if (Eat4Health._loaded)
				instanceChild.removeView(instanceChild.findViewById(Eat4Health.PROGRESS_BAR_ID));
			Eat4Health.appAccess.removeAllViews();
			Eat4Health.appAccess.addView(Eat4Health.intro);

			switch (id) {
			case 10:
				// food group selection menu
				// Eat4Health.appAccess.addView(Eat4Health.settings);

				PreferencesScroller groupSettings = new PreferencesScroller(context, 0);
				groupSettings.setId(GROUP_CHOICE_MENU_ID);
				Eat4Health.appAccess.addView(groupSettings);

				break;
			case 11:

				PreferencesScroller nutreintSettings = new PreferencesScroller(context, 1);
				nutreintSettings.setId(NUTRIENT_CHOICE_MENU_ID);
				Eat4Health.appAccess.addView(nutreintSettings);

				break;
			case 12:

				NutritionSettings nutrientSet = new NutritionSettings();
				Eat4Health.appAccess.addView(nutrientSet);
				break;
			case 13:

				PreferencesScroller myFoodsEdit = new PreferencesScroller(context, 2);
				myFoodsEdit.setId(FOOD_CHOICE_MENU_ID);
				Eat4Health.appAccess.addView(myFoodsEdit);

				break;
			case 14:

				FoodRemovalScroller removeFoods = new FoodRemovalScroller(context);
				removeFoods.setId(FOOD_REMOVAL_ID);
				Eat4Health.appAccess.addView(removeFoods);

				break;
			case 15:
				if (!optionMenuShowing) {
					Eat4Health.intro.instanceChild.removeView(Eat4Health.intro.instanceChild.findViewById(OPTIONS_MENU_ID));
					OptionsMenu options = new OptionsMenu(context);
					options.setId(OPTIONS_MENU_ID);
					Eat4Health.intro.instanceChild.addView(options);
					// Eat4Health.appAccess.addView(options);
					optionMenuShowing = true;
				} else {

					Eat4Health.intro.instanceChild.removeView(Eat4Health.intro.instanceChild.findViewById(OPTIONS_MENU_ID));
					optionMenuShowing = false;
				}
				break;

			default:
				break;

			}

			if (false) {
				final ViewTreeObserver viewTreeObserver4 = Eat4Health.application.getViewTreeObserver();
				if (viewTreeObserver4.isAlive()) {
					viewTreeObserver4.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
						@Override
						public void onGlobalLayout() {

							Eat4Health.application.scrollBy((int) (0.9 * MAX_BUTTON_WIDTH), 0);
							viewTreeObserver4.removeGlobalOnLayoutListener(this);
						}
					});
				}
			}
		}

	};

	// uses button title to search food descriptions
	// displays results as a new subsroll with foods description
	View.OnClickListener foodGroupSearchListener = new View.OnClickListener() {
		// added to keyword buttons ~2000
		public void onClick(View view) {
			int id = view.getId();
			// id of food group to search

			String searchWord = ourInput.getText().toString();

			Eat4Health.setSearchResultsFrom(searchWord, Eat4Health.Foods_Search);

			results.instanceChild.removeAllViews();
			results.addScrollingButtons(Eat4Health.searchResults, Eat4Health.foodCodeResults, results.nutritionInfoListener,
					BUTTON_COLORS[SEARCH_RESULTS]);

			// Eat4Health.searchResults;//food description
			// Eat4Health.foodCodeResults; //food code

			results.setId(FOOD_RESULTS_ID);
			// get index of parent subscroll for view insertion
			// Eat4Health.appAccess.removeView(Eat4Health.appAccess.findViewById(FOOD_RESULTS_ID));
			// Eat4Health.appAccess.addView(results,2);

		}
	};

	MenuScroller(Context c, ProgressBar pb, int type) {

		super(c);
		// intro menu

		if (type == 0) {

			instanceChild.addView(pb);

			TextView label = new TextView(c);
			label.setBackgroundColor(0xff3377cc);
			label.setTextSize(TypedValue.COMPLEX_UNIT_DIP, Eat4Health.TEXT_SMALL);
			label.setTextColor(0xff000000);
			label.setText("-Explore Foods-");
			label.setGravity(Gravity.CENTER);
			instanceChild.addView(label);

			addScrollingButtons(INTRO, NAV_IDS, navigationListener, BUTTON_COLORS[NAVIGATION]);

			label = new TextView(c);
			label.setBackgroundColor(0xff3377cc);
			label.setTextSize(TypedValue.COMPLEX_UNIT_DIP, Eat4Health.TEXT_SMALL);
			label.setTextColor(0xff000000);
			label.setText("-Settings-");
			label.setGravity(Gravity.CENTER);
			instanceChild.addView(label);

			addScrollingButtons(PREFS, NAV_IDS_2, settingsListener, BUTTON_COLORS[SETTINGS]);

		}
	}

	public Button titleButton(KeywordScroller keywordTitleScroll, int group) {

		Button label = simpleButton();
		label.setText(Eat4Health.foodGroups[group]);
		label.setId(group);
		label.setOnClickListener(keywordTitleScroll.foodGroupDisplayKeywordsListener);
		label.getBackground().setColorFilter(FOOD_GROUP_LABEL_COLOR, PorterDuff.Mode.MULTIPLY);
		return label;
	}
}
