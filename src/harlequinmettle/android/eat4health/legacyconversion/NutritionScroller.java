package harlequinmettle.android.eat4health.legacyconversion;

import harlequinmettle.android.eat4health.Eat4Health;
import harlequinmettle.android.eat4health.datautil.StatTool;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.Button;

public class NutritionScroller extends SubScroll {

	private View.OnClickListener nutrientListener = new View.OnClickListener() {
		public void onClick(View view) {
			int id = view.getId();
			currentNutrientID = id;
			Eat4Health.appAccess.removeView(Eat4Health.appAccess.findViewById(STATVIEW_ID));
			showingStats = false;
			// ADD STATISTICAL TOOL DB.NUTR(ID)

			StatTool nutrStat = null;
			// IS NUTRITIONSTATS HOLDING NULL FOR SOME
			// BOILEDDOWN.HISTOGRAM.CLONE NULL POINTER EXCEPTIONS?????
			if (Eat4Health.nutritionStats.containsKey(id)) {
				nutrStat = Eat4Health.nutritionStats.get(id);
			} else {
				nutrStat = new StatTool(Eat4Health.getFoodSearchIds(), id);
				Eat4Health.nutritionStats.put(id, nutrStat);
			}

			HistogramView nutrStats = new HistogramView(context, nutrStat, (int) Eat4Health.sw, (int) Eat4Health.sh,
					NutritionScroller.getColorForNutrient(id));

			nutrStats.setId(STATVIEW_ID);
			int statId = lastFoodsId + 2;
			if (SubScroll.fromKeywordSearch) {
				statId -= 1;
			}
			if (SubScroll.fromFoodSuggestions) {
				statId += 2;
			}
			// index = lastFoodsId + 2;
			// Eat4Health.viewHolder.add(index, results);
			Eat4Health.appAccess.addView(nutrStats, statId);
			// nutrStats.invalidate();
			Eat4Health.application.scrollBy((int) (0.9 * MAX_BUTTON_WIDTH), 0);
			showingStats = true;
		}
	};

	NutritionScroller(Context c, int foodCode) {
		super(c);
		addNutritionButtons(foodCode);

	}

	public static int getColorForNutrient(int i) {
		int rColor = GENERAL;
		if (i >= 8 && i <= 14) {
			rColor = INTERESTING;
		}
		if (i >= 15 && i <= 24) {
			rColor = CARB;

		}
		if (i >= 25 && i <= 35) {
			rColor = MINERAL;

		}
		if (i >= 36 && i <= 65) {
			rColor = VITAMIN;

		}
		if (i >= 66 && i <= 84) {
			rColor = PROTEIN;

		}
		if (i >= 85 && i <= 129) {
			rColor = FAT;

		}
		return rColor;
	}

	private void addNutritionButtons(int nID) {
		buttons = new Button[130];
		int actualI = 0;
		for (int i = 0; i < 130; i++) {
			float dataPoint = Eat4Health.db[i][nID];
			if (!Eat4Health.MY_NUTRIENTS[i] || dataPoint <= 0)
				// if (!Eat4Health.MY_NUTRIENTS[i] )
				continue;
			float n = 1;
			// //////////////////////////////////////////
			boolean useServingOK = true;
			switch (Eat4Health.Nutrient_Measure) {
			case Eat4Health.USING_GRAMS:
				// DONT REALLY NEED THIS - ALREADY USING PER 100G BY DEFAULT
				break;
			case Eat4Health.USING_KCAL:
				dataPoint = Eat4Health.getPer100KcalDataPoint(nID, dataPoint);

				break;

			case Eat4Health.USING_SERVING:

				dataPoint = Eat4Health.getPerServingDataPoint(nID, dataPoint);
				if (dataPoint < 0) {
					dataPoint = Eat4Health.db[i][nID];
					useServingOK = false;
				}
			default:
				// NEED ALGORITHM FOR FINDING REASONABLE SERVING SIZE
				break;
			}
			// //////////////////////////////////////////

			buttons[actualI] = simpleButton();

			String nutritionDescription = Eat4Health.nutrients[i] + "  " + dataPoint + " " + Eat4Health.units[i];

			switch (Eat4Health.Nutrient_Measure) {
			case Eat4Health.USING_KCAL:
				nutritionDescription += "  per 100 kilocalories";
				break;
			case Eat4Health.USING_GRAMS:
				nutritionDescription += "  per 100 grams";
				break;
			case Eat4Health.USING_SERVING:

				if (useServingOK) {
					nutritionDescription += " in "
							+ Eat4Health.quantityFactor[SubScroll.currentFoodID][Eat4Health.optimalServingId[SubScroll.currentFoodID]]
							+ " " + Eat4Health.oddUnits[SubScroll.currentFoodID][Eat4Health.optimalServingId[SubScroll.currentFoodID]]
							+ " of " + Eat4Health.foods[SubScroll.currentFoodID];
				} else {
					nutritionDescription += " per 100 grams";
				}
				break;

			default:

				break;
			}

			// ////////////////////////////////////////////////

			float[] nstat = Eat4Health.highlightFactors.get(i);
			float min = nstat[0];
			float mean = nstat[1];
			float max = nstat[2];

			if (dataPoint > mean) {

				n = dataPoint - mean;

				n /= (max - mean);

			} else {

				n = -(mean - dataPoint) / (mean - min);
			}
			int sim = (int) (n * 100);

			if (sim > 100) {
				sim = 110;
			}
			// ///////////////////////////////////////

			buttons[actualI].setText(nutritionDescription);
			buttons[actualI].setId(i);// set id to nutrient

			int bgColor = getColorForNutrient(i);
			if (i == 5 || i == 6) {
				sim = 0;
			}
			// since max is only 3 standard deviations from zero this number can
			// be exceeded
			if (sim > 100)
				sim = 100;

			int alpha = 150 + sim;

			int colorCombo = bgColor | (alpha << 24);
			if (sim < 25 && sim > -25) {
				buttons[actualI].setTextColor(0xffaaaaaa);
			} else if (sim < -25) {

				buttons[actualI].setTextColor(0xff777777);
			} else {

				buttons[actualI].setTextColor(0xffffffff);
			}
			buttons[actualI].setOnClickListener(nutrientListener);
			buttons[actualI].getBackground().setColorFilter(colorCombo, PorterDuff.Mode.SRC_IN);

			instanceChild.addView(buttons[actualI]);
			actualI++;
		}

	}
}
