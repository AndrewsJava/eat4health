package harlequinmettle.android.eat4health.legacyconversion;

import harlequinmettle.android.eat4health.Eat4Health;
import harlequinmettle.android.eat4health.interfaces.staticdataarrays.FG2_I;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.Button;

public class KeywordScroller extends SubScroll implements FG2_I {
	int group;
	// added to food group title buttons from method titlebutton in menuscroller
	public View.OnClickListener foodGroupDisplayKeywordsListener = new View.OnClickListener() {
		public void onClick(View view) {
			int id = view.getId();
			int[] groupIDS = new int[(FOODGROUPS[group].length)];
			for (int i = 0; i < groupIDS.length; i++)
				groupIDS[i] = group;
			// add rest of searchword buttons and add linearlayout:instanceChild
			// to scrollview
			if (!scrollinflated[group]) {
				addScrollingButtons(FOODGROUPS[group], groupIDS, foodGroupSearchListener, BUTTON_COLORS[SEARCHWORDS]);
				scrollinflated[group] = true;
			}

		}
	};

	// uses button title to search food descriptions
	// displays results as a new subsroll with foods description
	View.OnClickListener foodGroupSearchListener = new View.OnClickListener() {
		// added to keyword buttons ~2000
		public void onClick(View view) {
			int id = view.getId();
			currentFoodGroup = group;
			// introGraphicsCounter = 1;
			// id of food group to search
			Button b = (Button) view;
			b.getBackground().setColorFilter(0xff999999, PorterDuff.Mode.MULTIPLY);
			String searchWord = b.getText().toString();

			index = ((View) view.getParent()).getId() + 1;

			if (index <= lastFoodsId || lastFoodsId == 0) {
				Eat4Health.application.scrollBy((int) (MAX_BUTTON_WIDTH * 0.9), 0);
			} else {
				if (showingNutrients) {
					Eat4Health.application.scrollBy(-MAX_BUTTON_WIDTH, 0);
				}
				if (showingResults) {
					Eat4Health.application.scrollBy(-MAX_BUTTON_WIDTH, 0);
				}
				if (showingStats) {
					Eat4Health.application.scrollBy((int) (-2 * MAX_BUTTON_WIDTH * 0.9), 0);
				}
			}
			if (showingResults) {
				Eat4Health.appAccess.removeView(Eat4Health.appAccess.findViewById(FOOD_RESULTS_ID));
				showingResults = false;
			}
			if (showingNutrients) {
				Eat4Health.appAccess.removeView(Eat4Health.appAccess.findViewById(FOOD_NUTRIENT_ID));
				showingNutrients = false;
			}
			if (showingStats) {
				Eat4Health.appAccess.removeView(Eat4Health.appAccess.findViewById(STATVIEW_ID));
				showingStats = false;
			}
			// Eat4Health.appAccess.refreshDrawableState();

			Eat4Health.setSearchResultsFrom(searchWord, id);

			// Eat4Health.searchResults;//food description
			// Eat4Health.foodCodeResults; //food code
			FoodDescriptionsScroller results = new FoodDescriptionsScroller(context, true);

			// get index of parent subscroll for view insertion

			Eat4Health.appAccess.addView(results, index);

			lastFoodsId = index;

			showingResults = true;
		}
	};

	KeywordScroller(Context c, int group) {
		// BUILD INDIVIDUAL FOOD CATEGORY WORD LIST FROM INTERFACE
		super(c);

		this.group = group;// food group //22~Spices and Herbs

		if (false) {// just not quite ready to throw this away
			// add button with label
			Button label = simpleButton();
			label.setText(Eat4Health.foodGroups[group]);
			label.setId(group);
			label.setOnClickListener(foodGroupDisplayKeywordsListener);
			label.getBackground().setColorFilter(FOOD_GROUP_LABEL_COLOR, PorterDuff.Mode.MULTIPLY);

			instanceChild.addView(label);
		}
		// set all subsequent buttons to group so when searching description
		// search is limited to group

	}

}
