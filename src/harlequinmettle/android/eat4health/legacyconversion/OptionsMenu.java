package harlequinmettle.android.eat4health.legacyconversion;

import harlequinmettle.android.eat4health.Eat4Health;
import harlequinmettle.android.eat4health.datautil.ObjectStoringThread;
import harlequinmettle.android.tools.androidsupportlibrary.ContextReference;
import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class OptionsMenu extends SubScroll {
	CompoundButton.OnCheckedChangeListener dbsaver = new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				new Thread(new ObjectStoringThread(context, Eat4Health.db)).start();
				Eat4Health.saveObject(1, "SAVEDB");
			} else {
				// delete saved data?
				Eat4Health.saveObject(0, "SAVEDB");
			}
		}
	};
	RadioGroup.OnCheckedChangeListener setState = new RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// clear previously stored statistics now need other stats per
			// 100kcal or per 100g
			if (!Eat4Health._loaded || Eat4Health.calculatingStats) {
				return;
			}

			Eat4Health.nutritionStats.clear();
			switch (checkedId) {
			case ID_PER_SERVING:
				Eat4Health.Nutrient_Measure = Eat4Health.USING_SERVING;
				break;
			case ID_PER_100_KCALS:
				Eat4Health.Nutrient_Measure = Eat4Health.USING_KCAL;
				break;
			case ID_PER_100_GRAMS:
				Eat4Health.Nutrient_Measure = Eat4Health.USING_GRAMS;
				break;
			case Eat4Health.USE_MY_FOOD_GROUPS:
				Eat4Health.Foods_Search = Eat4Health.USE_MY_FOOD_GROUPS;
				// Eat4Health.setFoodsIds();
				break;
			case Eat4Health.USE_ALL_FOODS:
				Eat4Health.Foods_Search = Eat4Health.USE_ALL_FOODS;
				// Eat4Health.setFoodsIds();
				break;
			case Eat4Health.USE_MY_FOODS:
				Eat4Health.Foods_Search = Eat4Health.USE_MY_FOODS;
				// Eat4Health.setFoodsIds();
				// .setChecked(true);
				break;
			case Eat4Health.SEARCH_TYPE_SUM:
				Eat4Health.Search_Type = Eat4Health.SEARCH_TYPE_SUM;

				break;
			case Eat4Health.SEARCH_TYPE_PRODUCT:
				Eat4Health.Search_Type = Eat4Health.SEARCH_TYPE_PRODUCT;
				break;

			case 111111111:
				// case Eat4Health.VIEW_HORIZONTAL:
				// Eat4Health.myFoods_View = Eat4Health.VIEW_HORIZONTAL;

				break;
			case Eat4Health.VIEW_VERTICAL:
				Eat4Health.myFoods_View = Eat4Health.VIEW_VERTICAL;
				break;
			// ADD CASE ID_PER_SERVING:
			default:

				break;
			}

			if (checkedId < 25) {
				Eat4Health.Foods_Search = Eat4Health.USE_ONE_FOOD_GROUP;
				Eat4Health.Food_Group = checkedId;
			} else {
				Eat4Health.saveObject(Eat4Health.Nutrient_Measure, "SEARCHUNITS");

				if (Eat4Health.Foods_Search == Eat4Health.USE_ALL_FOODS || Eat4Health.Foods_Search == Eat4Health.USE_MY_FOOD_GROUPS
						|| Eat4Health.Foods_Search == Eat4Health.USE_MY_FOODS)
					Eat4Health.saveObject(Eat4Health.Foods_Search, "SEARCHFOODS");
			}
			Eat4Health.setFoodsIds();
			Eat4Health.highlightFactors.clear();
			Eat4Health.calculateHighlightNumbers();

		}

	};

	public OptionsMenu(Context context) {
		super(context);
		boolean setFoodGroup = false;
		CheckBox optimize = new CheckBox(ContextReference.getAppContext());
		if (Eat4Health.save_db == 1) {
			optimize.setChecked(true);
		}
		optimize.setId(848484848);
		optimize.setOnCheckedChangeListener(dbsaver);
		optimize.setText("Optimize database loading - requires 4 MB - loads 4X as fast");
		optimize.setMaxWidth(SubScroll.MAX_BUTTON_WIDTH);
		instanceChild.addView(optimize);
		// //////////////////////////////////////////////
		RadioGroup cal = new RadioGroup(context);
		cal.setBackgroundColor(RADIO_OPTIONS_1_COLOR);
		cal.setOnCheckedChangeListener(setState);
		RadioButton gram100 = simpleRadioButton();
		RadioButton cal100 = simpleRadioButton();
		RadioButton serving = simpleRadioButton();

		gram100.setText("Nutrients per 100 grams");
		cal100.setId(ID_PER_100_KCALS);

		cal100.setText("Nutrients per 100 Calories");
		gram100.setId(ID_PER_100_GRAMS);

		serving.setText("Nutrients per serving");
		serving.setId(ID_PER_SERVING);

		switch (Eat4Health.Nutrient_Measure) {
		case Eat4Health.USING_KCAL:
			cal100.setChecked(true);

			break;
		case Eat4Health.USING_GRAMS:
			gram100.setChecked(true);

			break;
		case Eat4Health.USING_SERVING:
			serving.setChecked(true);

			break;
		// ADD CASE ID_PER_SERVING:
		default:

			break;
		}
		cal.addView(serving);
		cal.addView(gram100);
		cal.addView(cal100);
		instanceChild.addView(cal);

		// //////////////////////////////////////////////

		// //////////////////////////////////////////////
		if (false) {
			RadioGroup algoritym = new RadioGroup(context);
			algoritym.setBackgroundColor(RADIO_OPTIONS_3_COLOR);
			algoritym.setOnCheckedChangeListener(setState);
			RadioButton sum = simpleRadioButton();
			RadioButton product = simpleRadioButton();

			sum.setText("Search algorithym: Sum");
			product.setText("Search algorithym: Product");
			sum.setId(Eat4Health.SEARCH_TYPE_SUM);
			product.setId(Eat4Health.SEARCH_TYPE_PRODUCT);
			switch (Eat4Health.Search_Type) {
			case Eat4Health.SEARCH_TYPE_SUM:
				sum.setChecked(true);

				break;
			case Eat4Health.SEARCH_TYPE_PRODUCT:
				product.setChecked(true);
				break;
			// ADD CASE ID_PER_SERVING:
			default:

				break;
			}

			algoritym.addView(sum);
			algoritym.addView(product);
			instanceChild.addView(algoritym);
		}
		// /////////////////////////////////////
		// //////////////////////////////////////////////
		if (false) {
			RadioGroup foodView = new RadioGroup(context);
			foodView.setBackgroundColor(RADIO_OPTIONS_3_COLOR);
			foodView.setOnCheckedChangeListener(setState);
			RadioButton horizontal = simpleRadioButton();
			RadioButton vertical = simpleRadioButton();

			horizontal.setText("MyFoods view: horizontal");
			vertical.setText("MyFoods view: vertical");
			// horizontal.setId(Eat4Health.VIEW_HORIZONTAL);
			vertical.setId(Eat4Health.VIEW_VERTICAL);
			switch (Eat4Health.myFoods_View) {
			case 10:
				// case Eat4Health.VIEW_HORIZONTAL:
				horizontal.setChecked(true);

				break;
			case Eat4Health.VIEW_VERTICAL:
				vertical.setChecked(true);
				break;
			// ADD CASE ID_PER_SERVING:
			default:

				break;
			}

			foodView.addView(vertical);
			foodView.addView(horizontal);
			instanceChild.addView(foodView);
		}
		// /////////////////////////////////////
		RadioGroup search = new RadioGroup(context);
		search.setBackgroundColor(RADIO_OPTIONS_4_COLOR);
		search.setOnCheckedChangeListener(setState);
		RadioButton myfoodgroups = simpleRadioButton();
		RadioButton allfoodgroups = simpleRadioButton();
		myfoodgroups.setBackgroundColor(RADIO_OPTIONS_2_COLOR);
		allfoodgroups.setBackgroundColor(RADIO_OPTIONS_2_COLOR);
		myfoodgroups.setText("Search my food groups");
		allfoodgroups.setText("Search all foods");
		myfoodgroups.setId(Eat4Health.USE_MY_FOOD_GROUPS);
		allfoodgroups.setId(Eat4Health.USE_ALL_FOODS);
		switch (Eat4Health.Foods_Search) {
		case Eat4Health.USE_MY_FOOD_GROUPS:
			myfoodgroups.setChecked(true);

			break;
		case Eat4Health.USE_ALL_FOODS:
			allfoodgroups.setChecked(true);

			break;
		case Eat4Health.USE_ONE_FOOD_GROUP:
			// .setChecked(true);
			setFoodGroup = true;
			break;
		case Eat4Health.USE_MY_FOODS:
			// .setChecked(true);

			break;
		// ADD CASE ID_PER_SERVING:
		default:

			break;
		}

		search.addView(myfoodgroups);
		search.addView(allfoodgroups);
		instanceChild.addView(search);
		// ///////////////////////////////////////
		// add all foodgroup radios
		// RadioGroup fdgp = new RadioGroup(context);
		// fdgp.setBackgroundColor(RADIO_OPTIONS_4_COLOR);
		// fdgp.setOnCheckedChangeListener(setState);
		for (int i = 0; i < Eat4Health.foodGroups.length; i++) {
			RadioButton grp = simpleRadioButton();
			if (setFoodGroup && Eat4Health.Food_Group == i) {
				grp.setChecked(true);
			}
			grp.setText("Search only: " + Eat4Health.foodGroups[i]);
			grp.setId(i);
			// fdgp.addView(grp);
			search.addView(grp);
		}
		// instanceChild.addView(fdgp);
	}

}
