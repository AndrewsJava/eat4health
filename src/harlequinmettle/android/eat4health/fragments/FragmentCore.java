package harlequinmettle.android.eat4health.fragments;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import harlequinmettle.android.eat4health.Eat4Health;
import harlequinmettle.android.eat4health.legacyconversion.KeywordScroller;
import harlequinmettle.android.tools.androidsupportlibrary.ContextReference;
import harlequinmettle.android.tools.androidsupportlibrary.ViewFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

//base class for other more specific types of scroll:menu,keyword,searchResults,nutritionInfo
public class FragmentCore extends Fragment {
	public static boolean fromKeywordSearch;
	public static boolean fromFoodSuggestions;
	static final int BUTTON_COLORS[] = { //
	0xff77aaaa, // navigaton
			0xff444488, // searchwords
			0xff229922, // serch results
			0xff0000ff, // not used nutrients use several colors defined
			0xff5555cc, // food group
			0xffaaaaaa, // not used histogram expected to be its own view
			0xff3333cc, // settings button colors
			0XFFDDAA00, // food group preferences
			0xffee22aa, // nutrient preferences
	};
	public static boolean[] scrollinflated = new boolean[25];
	static final int FOOD_GROUP_LABEL_COLOR = 0xff44aaff;

	static final int RADIO_OPTIONS_1_COLOR = 0xff77BB55;
	static final int RADIO_OPTIONS_2_COLOR = 0xff9999cc;
	static final int RADIO_OPTIONS_3_COLOR = 0xff22cc77;
	static final int RADIO_OPTIONS_4_COLOR = 0xff22ccff;

	static final int FOOD_RESULTS_ID = 1000055001;
	static final int FOOD_NUTRIENT_ID = 1055055001;
	static final int STATVIEW_ID = 1010101111;
	static final int GROUP_CHOICE_MENU_ID = 123311111;
	static final int NUTRIENT_CHOICE_MENU_ID = 1221000011;
	static final int FOOD_CHOICE_MENU_ID = 1222244011;
	static final int KEYWORD_SEARCH_ID = 2111211112;
	static final int NUTRITION_CHOICES_ID = 211333332;
	static final int OPTIONS_MENU_ID = 2131313222;
	static final int ID_PER_100_GRAMS = 343422222;
	static final int ID_PER_100_KCALS = 43432211;
	static final int ID_PER_SERVING = 44444411;
	static final int NUTRIENT_SEARCH_ID = 551155;
	static final int FOOD_REMOVAL_ID = 5555555;
	static final int NUTRITION_CALCULATOR_ID = 525252525;

	static final int NAVIGATION = 0;
	static final int SEARCHWORDS = 1;
	static final int SEARCH_RESULTS = 2;
	static final int NUTRIENTS = 3;
	static final int FOODGROUP = 4;
	static final int HISTOGRAM = 5;
	static final int SETTINGS = 6;
	static final int GROUPPREFS = 7;
	static final int NUTRPREFS = 8;

	static final int GENERAL = 0x002055dd;// 0-7
	static final int INTERESTING = 0x004FBB1a;// 8-14
	static final int CARB = 0x00ababab;// 15-24
	static final int MINERAL = 0x00FF00CC;// 25-35
	static final int VITAMIN = 0x00CC00FF;// 36-65
	static final int PROTEIN = 0x00bb2255;// 66-84
	static final int FAT = 0x00EEEE55;// 85-129

	static int lastFoodsId;
	static int index;
	static int foodPosition;
	static int nutritionInfoPosition;

	static int scrollCount = 0;

	LinearLayout instanceChild;
	static int currentFoodID, currentNutrientID;
	static int currentFoodGroup;
	static EditText ourInput;
	Button[] buttons;// setText setID addlistener
	static KeywordScroller[] foodGroupScrolls;
	static boolean showingResults, showingNutrients, showingStats;// initially
																	// false
	static boolean optionMenuShowing = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		instanceChild = ViewFactory.basicLinearLayout();

		this.instanceChild.setId(scrollCount++);

		return instanceChild;
	}

	FragmentCore() {

	}

	// fills instnaceChild linearlayout full of buttons
	public void addScrollingButtons(String[] buttonNames, int[] buttonIds, View.OnClickListener listener, int BUTTON_CLR) {
		// instanceChild.removeAllViews();
		buttons = new Button[buttonNames.length];

		for (int i = 0; i < buttonNames.length; i++) {
			buttons[i] = simpleButton();
			buttons[i].setText(buttonNames[i]);
			buttons[i].setId(buttonIds[i]);
			// NAVIGATION or SEARCHWORDS or FOODINFO
			buttons[i].setOnClickListener(listener);
			if (BUTTON_CLR != 0) {
				buttons[i].getBackground().setColorFilter(BUTTON_CLR, PorterDuff.Mode.MULTIPLY);
			}
			instanceChild.addView(buttons[i]);
		}
		// add layout full of buttons to scrollview

	}

	public static Button simpleButton() {
		Button button = new Button(ContextReference.getAppContext());
		button.setHeight(WRAP_CONTENT);
		button.setMaxLines(5);
		button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, Eat4Health.TEXT_MED);
		return button;
	}

	public static void resetDisplayConditions() {
		showingStats = false;
		lastFoodsId = 0;
		showingNutrients = false;
		showingResults = false;
	}

	public class RandomScroller implements OnGlobalLayoutListener {
		ScrollView scroll;
		boolean hasBeenSet = false;

		public RandomScroller(ScrollView scroll) {
			this.scroll = scroll;
		}

		public void onGlobalLayout() {
			if (!hasBeenSet) {
				scroll.scrollTo(0, (int) (Math.random() * scroll.getHeight() * 6));
				hasBeenSet = true;
			}
		}
	}

}
