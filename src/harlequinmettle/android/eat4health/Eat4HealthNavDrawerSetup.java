package harlequinmettle.android.eat4health;

import harlequinmettle.android.tools.androidsupportlibrary.TextViewFactory;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Eat4HealthNavDrawerSetup extends Eat4HealthListeners {

	int content_frame = 1010101;

	public ActionBarDrawerToggle mDrawerToggle;
	LinearLayout menuchild;

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
		menuchild = new LinearLayout(this);
		GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] { 0xFF616261, 0xAA131313 });
		gd.setCornerRadius(0f);

		menuchild.setBackgroundDrawable(gd);

		mDrawerList.addView(menuchild);
		menuchild.setOrientation(LinearLayout.VERTICAL);

		addViewsFromStrings("Explore Foods", INTRO, navigationListener);
		addViewsFromStrings("Settings/Preferences", PREFS, navigationListener);

		// DrawerLayout.LayoutParams lp = new DrawerLayout.LayoutParams((int)
		// (0.9 * sw), LinearLayout.LayoutParams.MATCH_PARENT);
		DrawerLayout.LayoutParams lp = new DrawerLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);

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

	// private void addViewsFromStrings(String menuCategory, String[] titles) {
	// TextView menucat = TextViewFactory.makeLeftTextView(menuCategory);
	// menuchild.addView(menucat);
	//
	// float i = 0;
	// for (String title : titles) {
	// TextView text = TextViewFactory.makeDefaultTextView(title);
	// text.setOnClickListener(menuDrawerListener);
	//
	// menuchild.addView(text);
	// viewMap.put(title, i++);
	// }
	// }

	private void addViewsFromStrings(String menuCategory, String[] titles, View.OnClickListener actionDefinition) {
		TextView menucat = TextViewFactory.makeLeftTextView(menuCategory);
		menuchild.addView(menucat);

		int i = 0;
		for (String title : titles) {
			TextView text = TextViewFactory.makeDefaultTextView(title);
			text.setOnClickListener(actionDefinition);

			text.setId(title.hashCode());
			menuchild.addView(text);
			viewMap.put(title, (float) title.hashCode());
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

}
