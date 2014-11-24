package harlequinmettle.android.eat4health;

import java.util.Locale;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class Eat4Health extends ActionBarActivity {
	private String[] mPlanetTitles = { "Mercury", "Venus", "earth", "mars" };
	private DrawerLayout mDrawerLayout;
	private ScrollView mDrawerList;
	int content_frame = 1010101;
	private int sw, sh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setScreenDimensionVariables();

		// setUpDrawerFromXML();
		setUpDrawerLayout();

	}

	private void setScreenDimensionVariables() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		sw = metrics.widthPixels;
		sh = metrics.heightPixels;
	}

	// private void setUpDrawerFromXML() {
	// setContentView(R.layout.activity_eat4_health);
	//
	// mPlanetTitles = getResources().getStringArray(R.array.planets_array);
	// mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	// mDrawerList = (ListView) findViewById(R.id.left_drawer);
	//
	// // Set the adapter for the list view
	// mDrawerList.setAdapter(new ArrayAdapter<String>(this,
	// R.layout.drawer_list_item, mPlanetTitles));
	// // Set the list's click listener
	// mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	// }

	private void setUpDrawerLayout() {
		mDrawerLayout = new android.support.v4.widget.DrawerLayout(this);
		mDrawerLayout.setScrimColor(0x77556677);
		FrameLayout fl = new FrameLayout(this);
		fl.setId(content_frame);
		mDrawerList = new ScrollView(this);

		addViewsFromStrings(mDrawerList, mPlanetTitles);
		// mDrawerList.setAdapter(new ArrayAdapter<String>(this,
		// R.layout.drawer_list_item, mPlanetTitles));
		DrawerLayout.LayoutParams lp = new DrawerLayout.LayoutParams((int) (0.9 * sw), LinearLayout.LayoutParams.MATCH_PARENT);

		lp.gravity = Gravity.START;
		mDrawerList.setLayoutParams(lp);

		// mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		mDrawerLayout.addView(fl, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mDrawerLayout.addView(mDrawerList);

		setContentView(mDrawerLayout);
	}

	private void addViewsFromStrings(ScrollView listview, String[] titles) {
		LinearLayout child = new LinearLayout(this);
		child.setOrientation(LinearLayout.VERTICAL);
		for (String title : titles) {
			TextView text = new TextView(this);
			text.setText(title);
			child.addView(text);
		}
		listview.addView(child);
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		Fragment fragment = new PlanetFragment();
		Bundle args = new Bundle();
		args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		// mDrawerList.setItemChecked(position, true);
		setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	/**
	 * Fragment that appears in the "content_frame", shows a planet
	 */
	public static class PlanetFragment extends Fragment {
		public static final String ARG_PLANET_NUMBER = "planet_number";

		public PlanetFragment() {
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
			int i = getArguments().getInt(ARG_PLANET_NUMBER);
			String planet = getResources().getStringArray(R.array.planets_array)[i];

			int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()), "drawable", getActivity().getPackageName());
			((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
			getActivity().setTitle(planet);
			return rootView;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.eat4_health, menu);
		return true;
	}

}
