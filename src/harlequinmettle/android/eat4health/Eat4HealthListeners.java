package harlequinmettle.android.eat4health;

import harlequinmettle.android.eat4health.fragments.FoodListViewPagerFragment;
import harlequinmettle.android.eat4health.fragments.FragmentCore;
import harlequinmettle.android.eat4health.legacyconversion.FoodDescriptionsScroller;
import harlequinmettle.android.eat4health.myviews.fullsortablefoodlist.SearchFoodListColapsableGroupScrollingLayoutView;
import harlequinmettle.android.tools.androidsupportlibrary.ContextReference;
import harlequinmettle.android.tools.androidsupportlibrary.ViewFactory;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;
import android.widget.Toast;

public class Eat4HealthListeners extends Eat4HealthLegacy {

	public static final String[] INTRO = { "Food Lists", "Word Search", "Search By Nutrient", "Evaluate Diet" };
	static final int FOOD_LIST_ID = INTRO[0].hashCode();
	public static final String[] PREFS = { "Food Groups", "Nutrients", "Set Nutrient Goals", "My Foods: View", "My Foods: Remove",
			"Options" };
	// public static final int[] NAV_IDS = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	// public static final int[] NAV_IDS_2 = { 10, 11, 12, 13, 14, 15, 16, 17,
	// 18, 19 };

	public static boolean[] scrollinflated = new boolean[25];
	static int scrollCount = 0;
	FoodDescriptionsScroller results;

	// initial navigation buttons menu: INTRO
	protected View.OnClickListener navigationListener = new View.OnClickListener() {

		public void onClick(View view) {
			// introGraphicsCounter = 0;
			int id = view.getId();
			CharSequence label = ((TextView) view).getText();
			FragmentCore.resetDisplayConditions();
			FragmentCore.fromKeywordSearch = false;
			FragmentCore.fromFoodSuggestions = false;
			// ADD BOOLEAN FOR LONGER FILELOADING THREAD TO SHOW TOAST
			if (!Eat4Health._loaded) {
				Toast mToast = Toast.makeText(ContextReference.getAppContext(),
						"\n\n\n\n\nPlease adjust some settings while database loads\n\n\n\n\n", 1);
				mToast.setGravity(Gravity.TOP, 0, 50);

				mToast.show();
			}
			scrollinflated = new boolean[25];
			// index = 1;
			if (id == INTRO[0].hashCode()) {
				scrollinflated = new boolean[25];
				// load food group labeled scrolls
				scrollCount = 1;

				setFragment(new FoodListViewPagerFragment(), label);
			}

			if (id == INTRO[1].hashCode()) {

				Eat4Health.appSelf.appContainer.removeAllViews();
				Eat4Health.appSelf.appContainer.addView(new SearchFoodListColapsableGroupScrollingLayoutView());
			}

			// case 1:// search by typing a keyword
			// // 2 causes search results/nutrition info to be placed to
			// // right of text
			// lastFoodsId = 2;
			// results = new
			// FoodDescriptionsScroller(ContextReference.getAppContext(),
			// false);
			// SubScroll.fromKeywordSearch = true;
			// LinearLayout title = basicLinearLayout();// vertical
			//
			// title.setId(scrollCount);
			//
			// ourInput = editText("Please enter a keyword");
			//
			// Button searchButton = simpleButton();
			// searchButton.setText("Search");
			// searchButton.setId(KEYWORD_SEARCH_ID);
			// searchButton.setOnClickListener(foodGroupSearchListener);
			//
			// title.addView(ourInput);
			// title.addView(searchButton);
			// title.addView(results);
			// // title.addView(kws2);
			//
			// Eat4Health.appAccess.addView(title, 1);
			//
			// break;
			// case 2:// nutrient priority search view
			// NutrientSearchView nsv = new
			// NutrientSearchView(ContextReference.getAppContext());
			// Eat4Health.appAccess.addView(nsv);
			//
			// break;
			//
			// case 3:
			// fromFoodSuggestions = true;
			// System.out.println("------>IN CASE 4 CREATE NUTRITIONCALC<---------------");
			// NutritionCalculatorView caclulateNutritionState = new
			// NutritionCalculatorView();
			// caclulateNutritionState.setId(NUTRITION_CALCULATOR_ID);
			// Eat4Health.appAccess.addView(caclulateNutritionState);
			//
			// break;
			// default:
			// break;
			//
			// }

			final ViewTreeObserver viewTreeObserver4 = Eat4Health.application.getViewTreeObserver();
			if (viewTreeObserver4.isAlive()) {
				viewTreeObserver4.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {

						Eat4Health.application.scrollBy((int) (0.9 * ViewFactory.MAX_BUTTON_WIDTH), 0);
						viewTreeObserver4.removeGlobalOnLayoutListener(this);
					}
				});
			}

			setTitle(label);
			mDrawerLayout.closeDrawer(mDrawerList);
		}

	};

	OnClickListener menuDrawerListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String menuChoice = ((TextView) v).getText().toString();
			int position = (int) viewMap.getFloatFromStringKey(menuChoice);
			selectItem(position);
		}
	};

	protected void selectItem(int position) {
		// // update the main content by replacing fragments
		// Fragment fragment = new PlanetFragment();
		// Bundle args = new Bundle();
		// args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
		// fragment.setArguments(args);
		//
		// FragmentManager fragmentManager = getFragmentManager();
		// fragmentManager.beginTransaction().replace(content_frame,
		// fragment).commit();
		//
		// // update selected item and title, then close the drawer
		// // mDrawerList.setItemChecked(position, true);
		// setTitle(mPlanetTitles[position]);
		// mDrawerLayout.closeDrawer(mDrawerList);
	}

	// /**
	// * Fragment that appears in the "content_frame", shows a planet
	// */
	// public static class PlanetFragment extends Fragment {
	// public static final String ARG_PLANET_NUMBER = "planet_number";
	//
	// public PlanetFragment() {
	// // Empty constructor required for fragment subclasses
	// }
	//
	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// View rootView = inflater.inflate(R.layout.fragment_planet, container,
	// false);
	// int i = getArguments().getInt(ARG_PLANET_NUMBER);
	// String planet = getResources().getStringArray(R.array.planets_array)[i];
	//
	// int imageId =
	// getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
	// "drawable", getActivity().getPackageName());
	// ((ImageView)
	// rootView.findViewById(R.id.image)).setImageResource(imageId);
	// getActivity().setTitle(planet);
	// return rootView;
	// }
	// }
}
