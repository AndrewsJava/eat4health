package harlequinmettle.android.eat4health;

import harlequinmettle.android.tools.androidsupportlibrary.FloatStringBimap;
import harlequinmettle.android.tools.androidsupportlibrary.TextViewFactory;

import java.util.Locale;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Eat4HealthNavDrawerSetup extends Eat4HealthLegacy {
	public String[] mPlanetTitles = { "Mercury", "Venus", "earth", "mars" };
	public DrawerLayout mDrawerLayout;
	public ScrollView mDrawerList;
	int content_frame = 1010101;
	public int sw, sh;
	public final FloatStringBimap viewMap = new FloatStringBimap();
	public ActionBarDrawerToggle mDrawerToggle;

	OnClickListener menuDrawerListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String menuChoice = ((TextView) v).getText().toString();
			int position = (int) viewMap.getFloatFromStringKey(menuChoice);
			selectItem(position);
		}
	};

	protected void setUpDrawerToggle() {
		final CharSequence mTitle = getTitle();
		final CharSequence mDrawerTitle = getTitle();
		mDrawerToggle = new ActionBarDrawerToggle(this, /*
														 * host Activity
														 */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.eat4_health, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_websearch:
			// create intent to perform web search for this planet
			Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
			intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
			// catch event that there's no activity to handle intent
			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(intent);
			} else {
				Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void setScreenDimensionVariables() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		sw = metrics.widthPixels;
		sh = metrics.heightPixels;
	}

	protected void setUpDrawerLayout() {
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
		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		setUpDrawerToggle();
		setContentView(mDrawerLayout);
	}

	private void addViewsFromStrings(ScrollView listview, String[] titles) {
		LinearLayout child = new LinearLayout(this);
		child.setOrientation(LinearLayout.VERTICAL);
		float i = 0;
		for (String title : titles) {
			TextView text = TextViewFactory.makeDefaultTextView(title);
			text.setOnClickListener(menuDrawerListener);
			text.setText(title);
			child.addView(text);
			viewMap.put(title, i++);
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

}
