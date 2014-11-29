package harlequinmettle.android.eat4health.legacyconversion;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import harlequinmettle.android.eat4health.Eat4Health;
import harlequinmettle.android.tools.androidsupportlibrary.ContextReference;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NutritionSettings extends LinearLayout {
	SubScroll myNutrients;
	LinearLayout panel;
	EditText[] amounts = new EditText[130];
	CheckBox[] onoff = new CheckBox[130];

	View.OnClickListener saveSettingsListener = new View.OnClickListener() {
		public void onClick(View view) {
			int actionID = view.getId();
			// find EditText and get numbers - save
			switch (actionID) {
			case SAVE:
				Eat4Health.myGoodNutrients = new boolean[130];
				for (int i = 0; i < 130; i++) {
					// if i not in my food groups dont add to scroll
					// if(false)
					Eat4Health.myGoodNutrients[i] = true;
					if (!Eat4Health.MY_NUTRIENTS[i])
						continue;
					float amount = 1;
					try {
						String amnt = (amounts[i].getText().toString());
						String[] timeA = amnt.split("\\*");
						for (String q : timeA) {
							amount *= Float.valueOf(q);
						}
					} catch (NumberFormatException nfe) {

					}
					Eat4Health.goodNutritionGoals[i] = amount;
					Eat4Health.myGoodNutrients[i] = onoff[i].isChecked();
				}
				Eat4Health.saveObject(Eat4Health.goodNutritionGoals, "GOODNUTRGOALS");
				Eat4Health.saveObject(Eat4Health.myGoodNutrients, "GOODNUTR");
				break;
			case RESET:
				Eat4Health.goodNutritionGoals = Eat4Health.DEFAULT_NUTR_GOALS.clone();
				Eat4Health.myGoodNutrients = Eat4Health.DEFAULT_GOOD_NUTRIENTS.clone();
				for (int i = 0; i < 130; i++) {
					// if i not in my food groups dont add to scroll
					// if(false)
					if (!Eat4Health.MY_NUTRIENTS[i])
						continue;
					if (Eat4Health.myGoodNutrients[i]) {
						if (onoff != null) {
							onoff[i].setChecked(true);
							onoff[i].setBackgroundColor(0xff00ff00);
						}
						if (amounts[i] != null)
							amounts[i].setText("" + Eat4Health.goodNutritionGoals[i]);
					} else {
						if (onoff != null) {
							onoff[i].setChecked(false);
							onoff[i].setBackgroundColor(0xffff0000);
						}
						if (amounts[i] != null)
							amounts[i].setText("" + Eat4Health.goodNutritionGoals[i]);
						// amounts[i].setText("As low as possible");
					}
				}
				break;
			default:

				break;
			}
		}
	};

	CompoundButton.OnCheckedChangeListener nutrientBenefitListener = new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			int id = buttonView.getId();
			// save wether or not this is a good nutrient

			if (isChecked) {
				buttonView.setChecked(true);
				buttonView.setBackgroundColor(0xff00ff00);
				if (amounts[id] != null)
					amounts[id].setText("" + Eat4Health.goodNutritionGoals[id]);
				Eat4Health.myGoodNutrients[id] = true;
			} else {

				buttonView.setChecked(false);
				buttonView.setBackgroundColor(0xffff0000);
				Eat4Health.myGoodNutrients[id] = false;
				if (amounts[id] != null)
					amounts[id].setText("" + Eat4Health.goodNutritionGoals[id]);
			}
		}
	};
	private static final int RESET = 43837;
	private static final int SAVE = 8473;

	public NutritionSettings() {
		super(ContextReference.getAppContext());
		this.setOrientation(VERTICAL);
		// /////////////////////////////////////
		myNutrients = new SubScroll(ContextReference.getAppContext());

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1.0f);
		myNutrients.setLayoutParams(params);
		panel = myNutrients.basicLinearLayout();
		buildSettingView();
	}

	private void buildSettingView() {
		for (int i = 0; i < 130; i++) {
			// if i not in my food groups dont add to scroll
			// if(false)
			if (!Eat4Health.MY_NUTRIENTS[i])
				continue;

			LinearLayout horizontalL = new LinearLayout(ContextReference.getAppContext());

			horizontalL.setId(i);// access layout to access button to
									// change title
			// //////////////////////////////////////////////////////
			CheckBox selected = new CheckBox(ContextReference.getAppContext());
			onoff[i] = selected;
			selected.setWidth(250);
			selected.setText(Eat4Health.nutrients[i]);
			selected.setId(i);
			// listener to change buttontitle and quantity and unit
			selected.setOnCheckedChangeListener(nutrientBenefitListener);
			horizontalL.addView(selected);
			EditText goal = SubScroll.editText("Enter Nutritient Goal");
			amounts[i] = goal;

			goal.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
			if (Eat4Health.myGoodNutrients[i]) {
				selected.setChecked(true);
				selected.setBackgroundColor(0xff00ff00);
				goal.setText("" + Eat4Health.goodNutritionGoals[i]);
			} else {

				selected.setChecked(false);
				selected.setBackgroundColor(0xffff0000);

				goal.setText("" + Eat4Health.goodNutritionGoals[i]);
			}
			horizontalL.addView(goal);

			TextView units = SubScroll.textView();
			units.setText("" + Eat4Health.units[i]);

			horizontalL.addView(units);
			myNutrients.instanceChild.addView(horizontalL);
		}

		this.addView(myNutrients);
		LinearLayout hz = new LinearLayout(ContextReference.getAppContext());

		Button clr = SubScroll.simpleButton();
		clr.setText("Reset Defaults");
		clr.setId(RESET);
		clr.setOnClickListener(saveSettingsListener);

		Button save = SubScroll.simpleButton();
		save.setText("Save These Settings");
		save.setId(SAVE);
		save.setOnClickListener(saveSettingsListener);

		hz.addView(clr);
		hz.addView(save);

		this.addView(hz);
	}

}
