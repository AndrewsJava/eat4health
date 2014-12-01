package harlequinmettle.android.eat4health;


public class Eat4HealthViewPagerTest extends Eat4HealthNavDrawerSetup {

	// ViewPager viewPager;
	// MyPagerAdapter myPagerAdapter;
	//
	// protected void setUpViewPagerTest() {
	//
	// LinearLayout mainLayout = new
	// LinearLayout(ContextReference.getAppContext());
	// setContentView(mainLayout);
	// viewPager = (new ViewPager(ContextReference.getAppContext()));
	// mainLayout.addView(viewPager);
	// LinearLayout.LayoutParams params = new
	// LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
	// LayoutParams.MATCH_PARENT);
	// viewPager.setLayoutParams(params);
	// myPagerAdapter = new MyPagerAdapter();
	// viewPager.setAdapter(myPagerAdapter);
	//
	// }
	//
	// private class MyPagerAdapter extends PagerAdapter {
	//
	// int NumberOfPages = 5;
	//
	// int[] res = { android.R.drawable.ic_dialog_alert,
	// android.R.drawable.ic_menu_camera, android.R.drawable.ic_menu_compass,
	// android.R.drawable.ic_menu_directions, android.R.drawable.ic_menu_gallery
	// };
	// int[] backgroundcolor = { 0xFF101010, 0xFF202020, 0xFF303030, 0xFF404040,
	// 0xFF505050 };
	//
	// @Override
	// public int getCount() {
	// return NumberOfPages;
	// }
	//
	// @Override
	// public boolean isViewFromObject(View view, Object object) {
	// return view == object;
	// }
	//
	// @Override
	// public Object instantiateItem(ViewGroup container, int position) {
	//
	// TextView textView = new TextView(ContextReference.getAppContext());
	// textView.setTextColor(Color.WHITE);
	// textView.setTextSize(30);
	// textView.setTypeface(Typeface.DEFAULT_BOLD);
	// textView.setText(String.valueOf(position));
	//
	// ImageView imageView = new ImageView(ContextReference.getAppContext());
	// imageView.setImageResource(res[position]);
	// LayoutParams imageParams = new LayoutParams(LayoutParams.MATCH_PARENT,
	// LayoutParams.MATCH_PARENT);
	// imageView.setLayoutParams(imageParams);
	//
	// LinearLayout layout = new LinearLayout(ContextReference.getAppContext());
	// layout.setOrientation(LinearLayout.VERTICAL);
	// LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
	// LayoutParams.MATCH_PARENT);
	// layout.setBackgroundColor(backgroundcolor[position]);
	// layout.setLayoutParams(layoutParams);
	// layout.addView(textView);
	// layout.addView(imageView);
	//
	// final int page = position;
	// layout.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// Toast.makeText(ContextReference.getAppContext(), "Page " + page +
	// " clicked", Toast.LENGTH_LONG).show();
	// }
	// });
	//
	// container.addView(layout);
	// return layout;
	// }
	//
	// @Override
	// public void destroyItem(ViewGroup container, int position, Object object)
	// {
	// container.removeView((LinearLayout) object);
	// }
	//
	// }

}
