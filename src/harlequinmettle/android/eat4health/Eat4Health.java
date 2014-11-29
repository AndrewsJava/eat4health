package harlequinmettle.android.eat4health;

import harlequinmettle.android.eat4health.datautil.GeneralLoadingThread;
import harlequinmettle.android.eat4health.datautil.LoadWeightsConversion;
import harlequinmettle.android.eat4health.datautil.ObjectLoadingThread;
import harlequinmettle.android.eat4health.fragments.IntroFragment;
import harlequinmettle.android.tools.androidsupportlibrary.ContextReference;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Eat4Health extends Eat4HealthNavDrawerSetup {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		runActivityUtilities();
		setDefaults();
		setUpDrawerLayout();

		legacySetup();
		setIntroFragment();
	}

	private void legacySetup() {

		// try to obtain weather use prefers 100g 100kcal or 'serving'
		Nutrient_Measure = restorePreference("SEARCHUNITS");

		Foods_Search = restorePreference("SEARCHFOODS");//

		save_db = restorePreference("SAVEDB");

		restoreGuidelines();

		restoreMyGoodNutrients();

		restoreMyFoodUnitIds();

		if (!loadMyFoodGroups())
			MY_FOOD_GROUPS = DEFAULT_FOOD_GROUPS;

		if (!loadMyNutrients())
			MY_NUTRIENTS = DEFAULT_NUTRIENTS;

		for (int i = 0; i < optimalServingId.length; i++) {
			optimalServingId[i] = -1;
		}
		// OPTIONAL TO SAVE AS OBJECTS AND LOAD LIKE DB

		a = new GeneralLoadingThread(this, nutrients, units, "nutr.txt");

		b = new GeneralLoadingThread(this, foodGroups, "group.txt");
		// KCAL PER100G REPLACE WITH DIRECT REFERENCE TO DB[5]
		c = new GeneralLoadingThread(this, foods, foodsByGroup, "food.txt", GeneralLoadingThread.FOOD);
		// sorts each foodgroup id after loading

		new Thread(a).start();
		new Thread(b).start();
		new Thread(c).start();

		new Thread(new LoadWeightsConversion()).start();
		// load database if objects not found automatically lauch text reading
		// thread
		// either thread finishes by starting thread to load
		_loaded = false;
		ObjectLoadingThread objLoader = new ObjectLoadingThread(this, db);
		loadingThread = new Thread(objLoader);
		loadingThread.start();

		setFoodsIds();

		restoreQuantities();
		restoreUnits();
		progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
		progressBar.setId(PROGRESS_BAR_ID);

		// progressBar.setPadding(0, 0, 0, 0);
		// progressBar.setMinimumHeight(TextViewFactory.getPixelsDensityEquivalent(144));
		// LayoutParams barParams = new LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT);
		// progressBar.setLayoutParams(barParams);
		// progressBar.getLayoutParams().height =
		// TextViewFactory.getPixelsDensityEquivalent(144);
		// progressBar.getLayoutParams().width =
		// TextViewFactory.getPixelsDensityEquivalent((int) (sw * .7));
		progressBar.invalidate();

		Toast mToast = Toast.makeText(this, "\n\n\n\n\nPlease adjust some settings while database loads\n\n\n\n\n", 1);
		mToast.setGravity(Gravity.TOP, 0, 50);

		mToast.show();
	}

	private void runActivityUtilities() {
		ContextReference setUniversalContext = new ContextReference(this);
		setScreenDimensionVariables();
	}

	private void setIntroFragment() {

		Fragment fragment = new IntroFragment(progressBar);
		// Bundle args = new Bundle();
		// args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
		// fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		// mDrawerList.setItemChecked(position, true);
		setTitle("intro fragment");
		mDrawerLayout.closeDrawer(mDrawerList);
	}
}
