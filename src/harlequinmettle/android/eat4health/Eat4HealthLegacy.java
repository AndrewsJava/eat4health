package harlequinmettle.android.eat4health;

import harlequinmettle.android.eat4health.datautil.SimpleStatBuilder;
import harlequinmettle.android.eat4health.legacyconversion.MenuScroller;
import harlequinmettle.android.tools.androidsupportlibrary.ContextReference;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.content.Context;

public class Eat4HealthLegacy extends Eat4HealthData {

	public static MenuScroller intro;
	public static MenuScroller settings;

	public static void setSearchResultsFrom(String searchWord, int groups_search) {
		searching = true;
		if (searchWord.length() < 3) {
			searchResults = new String[1];
			foodCodeResults = new int[1];
			searchResults[0] = "Water, tap, drinking";
			foodCodeResults[0] = 8081;
			return;
		}
		ArrayList<String> results = new ArrayList<String>();
		ArrayList<Long> resultsID = new ArrayList<Long>();

		// search for search word as one single unit only
		int[] foodIDsToSearch;

		switch (groups_search) {
		case USE_ALL_FOODS:
			foodIDsToSearch = new int[FOOD_COUNT];
			for (int i = 0; i < FOOD_COUNT; i++) {
				foodIDsToSearch[i] = i;
			}
			break;
		case USE_MY_FOOD_GROUPS:
			foodIDsToSearch = constructUniqueValuesFromMyFoodGroups();
			break;
		case USE_MY_FOODS:
			foodIDsToSearch = constructUniqueValuesFromMyFoods();
			break;
		case USE_ONE_FOOD_GROUP:
			foodIDsToSearch = foodsByGroup[Food_Group];
			break;
		default:

			foodIDsToSearch = foodsByGroup[groups_search];
			break;
		}

		String sw = searchWord.trim();
		while (sw.contains("  ")) {
			sw = sw.replaceAll("  ", " ");
		}
		sw = " " + sw + " ";
		// for each food description with spaces added check for an exact
		// match
		for (int fdID : foodIDsToSearch) {
			String item = " " + foods[fdID];
			if (item.toLowerCase().replaceAll("[,\\(\\)]", " ").contains(sw)) {
				results.add(item.trim());
				resultsID.add(new Long(fdID));
			}
		}
		if (false) {
			sw = sw.trim() + " ";
			if (results.size() < 50)
				for (int fdID : foodIDsToSearch) {
					String item = " " + foods[fdID];
					if (item.toLowerCase().replaceAll("[,\\(\\)]", " ").contains(sw) && !results.contains(item.trim())) {
						results.add(item.trim());
						resultsID.add(new Long(fdID));
					}
				}
		}
		// sw = " " + sw.trim();
		sw = sw.trim();
		if (results.size() < 40 && sw.length() > 2)
			for (int fdID : foodIDsToSearch) {
				String item = foods[fdID];
				if (!results.contains(item.trim()) && item.toLowerCase().contains(sw)) {
					// System.out
					// .println("found with regex ---    ---    ----    ---    ----    ----");
					results.add(item.trim());
					resultsID.add(new Long(fdID));
				}
			}

		if (results.size() < 5)
			if (searchWord.length() > 3) {
				searchWord = searchWord.trim().substring(0, searchWord.length() - 1);
				for (int fdID : foodIDsToSearch) {
					String item = " " + foods[fdID];
					if (!results.contains(item.trim()) && item.toLowerCase().replaceAll("[,\\(\\)]", " ").contains(searchWord))
						results.add(item.trim());
					resultsID.add(new Long(fdID));
					// System.out.println("found ON letter less ONE");

				}
			}

		if (results.size() < 50) {
			String[] sWords = searchWord.trim().split(" ");
			if (sWords.length > 1)
				outerloop: for (int fdID : foodIDsToSearch) {
					String item = foods[fdID];
					for (String pt : sWords) {
						if (results.contains(item.trim()) || !item.toLowerCase().contains(pt) && pt.length() > 3)
							continue outerloop;
					}
					results.add(item.trim());
					resultsID.add(new Long(fdID));
					System.out.println("found MULTIPLE WORD CASE---");
					if (results.size() > 50) {
						break outerloop;
					}
				}
		}
		// load results of search int static array
		// string description with cooresponding food code
		searchResults = new String[results.size()];
		foodCodeResults = new int[results.size()];

		for (int i = 0; i < results.size(); i++) {
			searchResults[i] = results.get(i);
			foodCodeResults[i] = (resultsID.get(i).intValue());

		}
		searching = false;
	}

	public static String[] getMyFoodsAsTextArray() {
		ArrayList<String> foodListConstruct = new ArrayList<String>();
		for (TreeMap<Integer, Boolean> foodi : Eat4Health.allMyFoods) {
			for (Integer i : foodi.keySet()) {
				foodListConstruct.add(foods[i]);
			}
		}
		String[] fd = new String[foodListConstruct.size()];
		for (int i = 0; i < fd.length; i++) {
			fd[i] = foodListConstruct.get(i);
		}
		return fd;
	}

	public static int[] getMyFoodsIdsArray() {
		ArrayList<Integer> foodIdsConstruct = new ArrayList<Integer>();
		for (TreeMap<Integer, Boolean> foodi : Eat4Health.allMyFoods) {
			for (Integer i : foodi.keySet()) {
				foodIdsConstruct.add(i);
			}
		}
		int[] ids = new int[foodIdsConstruct.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = foodIdsConstruct.get(i);
		}
		return ids;
	}

	public static void setFoodsIds() {
		int[] theseFoods = null;
		switch (Foods_Search) {
		case USE_ALL_FOODS:
			theseFoods = new int[8194];
			for (int i = 0; i < 8194; i++)
				theseFoods[i] = i;
			break;
		case USE_MY_FOODS:
			ArrayList<Integer> getMySelectFoods = new ArrayList<Integer>();

			for (TreeMap<Integer, Boolean> amf : allMyFoods) {
				for (Entry<Integer, Boolean> ent : amf.entrySet()) {
					if (ent.getValue())
						getMySelectFoods.add(ent.getKey());
				}
			}
			theseFoods = new int[getMySelectFoods.size()];
			for (int i = 0; i < theseFoods.length; i++)
				theseFoods[i] = getMySelectFoods.get(i);

			break;
		case USE_MY_FOOD_GROUPS:
			if (_loaded)
				theseFoods = constructUniqueValuesFromMyFoodGroups();
			else
				needToSetSearchFoods = true;
			break;
		case USE_ONE_FOOD_GROUP:
			theseFoods = foodsByGroup[Food_Group];
			break;
		default:
			theseFoods = foodsByGroup[Food_Group];
			break;
		}

		searchTheseFoods = theseFoods;
	}

	public static String getFoodsSearchDescription() {

		switch (Foods_Search) {
		case USE_ALL_FOODS:
			return "all foods in database";

		case USE_MY_FOODS:
			return "My Foods";
		case USE_MY_FOOD_GROUPS:
			return "My Food Groups";
		case USE_ONE_FOOD_GROUP:
			return foodGroups[Food_Group];
		default:
			break;
		}

		return "";
	}

	private static int[] constructUniqueValuesFromMyFoods() {
		HashSet<Integer> uniqueValues = new HashSet<Integer>();
		for (int i = 0; i < allMyFoods.size(); i++) {
			TreeMap<Integer, Boolean> perGroup = allMyFoods.get(i);
			if (MY_FOOD_GROUPS[i]) {
				for (int j : perGroup.keySet()) {
					uniqueValues.add(j);
				}
			}
		}
		int counter = 0;
		int[] uniques = new int[uniqueValues.size()];
		for (int i : uniqueValues) {
			uniques[counter++] = i;
		}
		return uniques;
	}

	public static int[] getFoodSearchIds() {
		return searchTheseFoods;
	}

	public static int findGroupFromFoodID(int foodId) {
		for (int i = 0; i < 25; i++) {
			if (Arrays.binarySearch(foodsByGroup[i], foodId) >= 0)
				return i;
		}

		return 10;
	}

	public static void saveObject(Object OBJ, String obLocatorName) {
		try {
			FileOutputStream fos = ContextReference.getAppContext().openFileOutput(obLocatorName, Context.MODE_PRIVATE);
			ObjectOutputStream objout = new ObjectOutputStream(fos);
			objout.writeObject(OBJ);
			objout.flush();
			objout.close();

		} catch (IOException ioe) {
		}
	}

	protected void restoreMyGoodNutrients() {
		int def = 0;
		boolean[] x = null;
		try {
			FileInputStream fis = ContextReference.getAppContext().openFileInput("GOODNUTR");
			ObjectInputStream objin = new ObjectInputStream(fis);
			x = (boolean[]) objin.readObject();
			objin.close();

		} catch (Exception ioe) {
		}
		if (x != null)
			myGoodNutrients = x;
		else
			myGoodNutrients = DEFAULT_GOOD_NUTRIENTS.clone();
	}

	protected int restorePreference(String string) {
		int def = 0;
		try {
			FileInputStream fis = this.openFileInput(string);
			ObjectInputStream objin = new ObjectInputStream(fis);
			def = (Integer) objin.readObject();
			objin.close();

		} catch (Exception ioe) {
			if (string.equals("SEARCHUNITS"))
				return Eat4HealthData.USING_GRAMS;
			else if (string.equals("SEARCHFOODS"))
				return Eat4HealthData.USE_MY_FOOD_GROUPS;
			else
				return 0;
		}
		return def;
	}

	static void restoreGuidelines() {
		int def = 0;
		float[] x = null;
		try {
			FileInputStream fis = ContextReference.getAppContext().openFileInput("GOODNUTRGOALS");
			ObjectInputStream objin = new ObjectInputStream(fis);
			x = (float[]) objin.readObject();
			objin.close();

		} catch (Exception ioe) {
		}
		if (x != null)
			goodNutritionGoals = x;
		else
			goodNutritionGoals = DEFAULT_NUTR_GOALS.clone();
	}

	protected void restoreUnits() {
		int def = 0;
		HashMap<Integer, String> x = null;
		try {
			FileInputStream fis = this.openFileInput("UNITYMAP");
			ObjectInputStream objin = new ObjectInputStream(fis);
			x = (HashMap<Integer, String>) objin.readObject();
			objin.close();

		} catch (Exception ioe) {
		}
		if (x != null)
			myFoodUnits = x;
	}

	protected void restoreMyFoodUnitIds() {
		int def = 0;
		HashMap<Integer, Integer> x = null;
		try {
			FileInputStream fis = this.openFileInput("UNITIDMAP");
			ObjectInputStream objin = new ObjectInputStream(fis);
			x = (HashMap<Integer, Integer>) objin.readObject();
			objin.close();

		} catch (Exception ioe) {
		}
		if (x != null)
			myFoodUnitIDs = x;
	}

	protected void restoreQuantities() {
		int def = 0;
		HashMap<Integer, Float> x = null;
		try {
			FileInputStream fis = this.openFileInput("QUANTITYMAP");
			ObjectInputStream objin = new ObjectInputStream(fis);
			x = (HashMap<Integer, Float>) objin.readObject();
			objin.close();

		} catch (Exception ioe) {
		}
		if (x != null)
			myFoodQuantities = x;
	}

	public boolean loadMyFoodGroups() {
		try {
			// TEST ALTERNATIVE LOAD FROM PRESTORED OBJECTS ASSETMANAGER
			FileInputStream fis = this.openFileInput("MYFOODGROUPS");
			ObjectInputStream objin = new ObjectInputStream(fis);
			MY_FOOD_GROUPS = (boolean[]) objin.readObject();
			objin.close();

		} catch (Exception ioe) {
			return false;
		}
		return true;
	}

	public boolean loadMyFoods() {
		try {
			// TEST ALTERNATIVE LOAD FROM PRESTORED OBJECTS ASSETMANAGER
			FileInputStream fis = this.openFileInput("MYFOODS");
			ObjectInputStream objin = new ObjectInputStream(fis);
			allMyFoods = (ArrayList<TreeMap<Integer, Boolean>>) objin.readObject();
			objin.close();

		} catch (Exception ioe) {
			return false;
		}
		return true;
	}

	public boolean loadMyNutrients() {
		try {
			// TEST ALTERNATIVE LOAD FROM PRESTORED OBJECTS ASSETMANAGER
			FileInputStream fis = this.openFileInput("MYNUTRIENTS");
			ObjectInputStream objin = new ObjectInputStream(fis);
			MY_NUTRIENTS = (boolean[]) objin.readObject();
			objin.close();

		} catch (Exception ioe) {
			return false;
		}
		return true;
	}

	protected void setDefaults() {
		for (int i = 0; i < FOOD_COUNT; i++) {
			oddUnits[i] = new String[1];// for each food id
			oddUnits[i][0] = "grams"; // list of different
			metricConversion[i] = new float[1];// corresponding
			metricConversion[i][0] = 100;// defalut convert 100g into 100g
			quantityFactor[i] = new float[1];// corresponding
			quantityFactor[i][0] = 100;
		}
	}

	private static int[] constructUniqueValuesFromMyFoodGroups() {
		HashSet<Integer> uniqueValues = new HashSet<Integer>();
		for (int i = 0; i < MY_FOOD_GROUPS.length; i++) {
			if (MY_FOOD_GROUPS[i]) {
				for (int j : foodsByGroup[i]) {
					uniqueValues.add(j);
				}
			}
		}
		int counter = 0;
		int[] uniques = new int[uniqueValues.size()];
		for (int i : uniqueValues) {
			uniques[counter++] = i;
		}
		return uniques;
	}

	// CALL AFTER DATABASE IS LOADED AND AFTER CHANGES TO SETTINGS
	public static void calculateHighlightNumbers() {
		new Thread(new SimpleStatBuilder()).start();
	}

	public static float getPer100KcalDataPoint(int foodID, float data) {
		float calories = db[5][foodID];
		// RETURN NEGATIVE IF CALORIES ARE ZERO AND USE SERVING
		float rData = (float) (100.0 * data);
		if (calories > 10)
			rData /= (db[5][foodID]);
		else
			rData /= (20);
		return (float) (((int) (1000 * (rData))) / 1000.0);

	}

	// 7416~1~tsp~2.2 - weights conversion
	//
	public static float getPerServingDataPoint(int foodID, float nutritionPer100Grams) {
		if (quantityFactor[foodID] == null || optimalServingId[foodID] < 0)
			return -1;

		float quantityOfOddUnit = quantityFactor[foodID][optimalServingId[foodID]];
		float gramsPerOddUnit = metricConversion[foodID][optimalServingId[foodID]];

		float rData = nutritionPer100Grams * gramsPerOddUnit / 100.0f;
		return (float) (((int) (1000 * (rData))) / 1000.0);

	}
}
