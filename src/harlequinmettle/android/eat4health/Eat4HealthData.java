package harlequinmettle.android.eat4health;

import harlequinmettle.android.eat4health.datautil.GeneralLoadingThread;
import harlequinmettle.android.eat4health.datautil.StatTool;
import harlequinmettle.android.eat4health.staticdataarrays.FG2_I;
import harlequinmettle.android.eat4health.staticdataarrays.HasServingSizeInfo;
import harlequinmettle.android.eat4health.staticdataarrays.I_Preferences;
import harlequinmettle.android.tools.androidsupportlibrary.FloatStringBimap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.widget.ProgressBar;
import android.widget.ScrollView;

public class Eat4HealthData extends ActionBarActivity implements I_Preferences, HasServingSizeInfo, FG2_I {
	//

	public static Eat4Health appSelf;
	public static final FloatStringBimap viewMap = new FloatStringBimap();
	public DrawerLayout mDrawerLayout;
	public ScrollView mDrawerList;
	int content_frame = 1010101;
	public static Thread loadingThread;
	public static ArrayList<TreeMap<Integer, Boolean>> allMyFoods = new ArrayList<TreeMap<Integer, Boolean>>();

	public static HashMap<Integer, Float> myFoodQuantities = new HashMap<Integer, Float>();
	public static HashMap<Integer, Integer> myFoodUnitIDs = new HashMap<Integer, Integer>();
	public static HashMap<Integer, String> myFoodUnits = new HashMap<Integer, String>();

	public static boolean[] myGoodNutrients;
	public static float[] goodNutritionGoals;
	public static boolean _loaded = false;
	public static final int FOOD_GROUP_COUNT = 25;
	public static final int FOOD_COUNT = 8194;// REPLACE ALL APP WIDE INSTANCEES
												// WITH
	// CONTANT

	public static final int USING_KCAL = 200000;
	public static final int USING_SERVING = 400000;
	public static final int USING_GRAMS = 800000;

	public static final int USE_ALL_FOODS = 10000;
	public static final int USE_MY_FOOD_GROUPS = 5000;
	public static final int USE_MY_FOODS = 500;
	public static final int USE_ONE_FOOD_GROUP = 128;

	public static final int SEARCH_TYPE_SUM = 10000002;
	public static final int SEARCH_TYPE_PRODUCT = 10000001;

	// static final int VIEW_HORIZONTAL = 151516161;
	public static final int VIEW_VERTICAL = 515151661;
	// PREFERENCE NEEDS TO BE STORED AND RESTORED AS PREFERENCE
	public static int myFoods_View = VIEW_VERTICAL;
	public static int Foods_Search = USE_ALL_FOODS;
	public static int Nutrient_Measure = USING_GRAMS;
	public static int Food_Group = 21;// default irrelevant
	public static int Search_Type = SEARCH_TYPE_PRODUCT;

	public static boolean longLoading = false;
	public static boolean searching = false;
	public static boolean calculatingStats = false;
	public static boolean statsLoaded = false;
	public static float sw, sh;
	public static float[][] db = new float[130][8194];

	// 4052~23~Honey
	public static String[] foods = new String[8194];
	// static int[] MY_FOODS;
	// static byte[] foodGroupMap = new byte[8194];// CHANGE TO BYTE
	// static float[] kCal ;// CHANGE TO BYTE
	// 9~Fats and Oils
	public static String[] foodGroups = new String[25];
	// 5~kcal~Energy
	public static String[] nutrients = new String[130];
	public static String[] units = new String[130];

	public static int[][] foodsByGroup = new int[25][];

	public static String[] searchResults;
	public static int[] foodCodeResults;
	public static final int PROGRESS_BAR_ID = 100010001;
	public static int[] searchTheseFoods;

	private static final String DATA_100G = "database_object";
	public static final int USING_NOT_ZEROS = 1024;
	public static final int USING_ZEROS = 2048;
	public static int DATA_CHOICES = USING_NOT_ZEROS;
	// loading definitions:
	// a - nutrient units
	// b - foodGroup
	// c - food foodGroupMap kcalPer100G
	GeneralLoadingThread a, b, c;
	public static boolean[] MY_FOOD_GROUPS;
	public static boolean[] MY_NUTRIENTS;
	public static ProgressBar progressBar;
	public static Handler mHandler = new Handler();

	public static HashMap<Integer, StatTool> nutritionStats = new HashMap<Integer, StatTool>();
	public static HashMap<Integer, float[]> highlightFactors = new HashMap<Integer, float[]>();

	// -1 if no serving size info is available
	public static int[] optimalServingId = new int[FOOD_COUNT];
	// all food ids with servings size info (at least 1)
	public static int[] WITH_SERVING_SIZE = HAS_SERVING_INFO;

	public static String[][] oddUnits = new String[FOOD_COUNT][];// for each
																	// food id
	// list of different
	// units
	public static float[][] metricConversion = new float[FOOD_COUNT][];// corresponding
	// grams
	public static float[][] quantityFactor = new float[FOOD_COUNT][];// corresponding
	// amount of
	// different
	// units
	public static final int TEXT_SMALL = 21;
	public static final int TEXT_MED = 25;
	public static final int TEXT_LARGE = 35;
	public static boolean needToSetSearchFoods;
	public static int save_db = 0;

}
