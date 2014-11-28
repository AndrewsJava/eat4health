package harlequinmettle.android.eat4health;

import harlequinmettle.android.eat4health.datautil.SimpleStatBuilder;
import harlequinmettle.android.tools.androidsupportlibrary.ContextReference;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Eat4HealthLegacy extends Eat4HealthData {

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

	public static int[] getFoodSearchIds() {
		return searchTheseFoods;
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
