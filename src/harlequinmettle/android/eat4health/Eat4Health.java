package harlequinmettle.android.eat4health;

import harlequinmettle.android.eat4health.datautil.GeneralLoadingThread;
import harlequinmettle.android.eat4health.datautil.LoadWeightsConversion;
import harlequinmettle.android.eat4health.datautil.ObjectLoadingThread;
import harlequinmettle.android.eat4health.fragments.IntroFragment;
import harlequinmettle.android.tools.androidsupportlibrary.ContextReference;
import android.app.Fragment;
import android.os.Bundle;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class Eat4Health extends TESTINGGROUND {
	int xxhm = 0;

	@Override
	public void onBackPressed() {
		if (VIEW_Q.isEmpty())
			moveTaskToBack(true);
		else
			setContentView(VIEW_Q.pollLast());

	}

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

		// main layout (and its one child) for whole app
		application = new HorizontalScrollView(this);
		appAccess = new LinearLayout(this);// may need to make sure its
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

	}

	private void runActivityUtilities() {
		appSelf = this;
		ContextReference setUniversalContext = new ContextReference(this);
		setScreenDimensionVariables();
	}

	private void setIntroFragment() {

		Fragment fragment = new IntroFragment(progressBar);

		setFragment(fragment, "intro fragment");

	}
}
