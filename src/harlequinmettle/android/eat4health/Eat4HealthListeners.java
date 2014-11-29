package harlequinmettle.android.eat4health;

import harlequinmettle.android.tools.androidsupportlibrary.FloatStringBimap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Eat4HealthListeners extends Eat4HealthLegacy {

	public final FloatStringBimap viewMap = new FloatStringBimap();
	static final String[] INTRO = { "Food Lists", "Word Search", "Search By Nutrient", "Evaluate Diet" };
	static final String[] PREFS = { "Food Groups", "Nutrients", "Set Nutrient Goals", "My Foods: View", "My Foods: Remove", "Options" };
	static final int[] NAV_IDS = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	static final int[] NAV_IDS_2 = { 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 };

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
