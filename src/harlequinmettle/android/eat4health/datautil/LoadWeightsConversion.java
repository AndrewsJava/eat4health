package harlequinmettle.android.eat4health.datautil;

import harlequinmettle.android.eat4health.Eat4HealthData;
import harlequinmettle.android.tools.androidsupportlibrary.ContextReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.res.AssetManager;

public class LoadWeightsConversion implements Runnable {
	// reluctant to use but seems necessary by convention
	// divide [3]/[1] for per unit

	public LoadWeightsConversion() {

	}

	public void run() {

		ArrayList<ArrayList<String>> uns = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<Float>> quants = new ArrayList<ArrayList<Float>>();
		ArrayList<ArrayList<Float>> grams = new ArrayList<ArrayList<Float>>();

		for (int i = 0; i < Eat4HealthData.FOOD_COUNT; i++) {
			uns.add(new ArrayList<String>());
			quants.add(new ArrayList<Float>());
			grams.add(new ArrayList<Float>());
		}
		long start = System.currentTimeMillis();
		int readCount = 0;
		AssetManager am = ContextReference.getAppContext().getAssets();
		BufferedReader br = null;
		try {
			InputStream is = am.open("wt_con.txt");
			br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			String holder = "";
			while ((holder = br.readLine()) != null) {

				String[] values = holder.split("~");
				int id = GeneralLoadingThread.doInt(values[0]);

				// 2460~1~oz~28.35
				// 2460~1~cubic inch~17
				// 2460~1~cup, crumbled, not packed~135

				uns.get(id).add(values[2]);
				quants.get(id).add((float) GeneralLoadingThread.doDouble(values[1]));
				grams.get(id).add((float) GeneralLoadingThread.doDouble(values[3]));

				if (readCount++ % 1000 == 10) {
					Thread.yield();
				}
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i : Eat4HealthData.HAS_SERVING_INFO) {
			Eat4HealthData.oddUnits[i] = new String[uns.get(i).size()];
			Eat4HealthData.metricConversion[i] = new float[grams.get(i).size()];
			Eat4HealthData.quantityFactor[i] = new float[quants.get(i).size()];
			for (int j = 0; j < uns.get(i).size(); j++) {
				Eat4HealthData.oddUnits[i][j] = uns.get(i).get(j);
				Eat4HealthData.metricConversion[i][j] = grams.get(i).get(j);
				Eat4HealthData.quantityFactor[i][j] = quants.get(i).get(j);
			}

		}

		for (int i : Eat4HealthData.HAS_SERVING_INFO) {
			for (int j = 0; j < uns.get(i).size(); j++) {
				Eat4HealthData.oddUnits[i][j] = uns.get(i).get(j);
				Eat4HealthData.metricConversion[i][j] = grams.get(i).get(j);
				Eat4HealthData.quantityFactor[i][j] = quants.get(i).get(j);
			}

		}
		// Eat4HealthData.oddUnits string
		// Eat4HealthData.metricConversion float number of grams in quantity of
		// odd
		// units
		// Eat4HealthData.quantityFactor float number of odd units
		// conventioanlly
		// applied

		for (int id : Eat4HealthData.WITH_SERVING_SIZE) {
			int optimalServingSizeID = 0;
			float smallestServing = 1000000;
			int index = 0;
			for (float weightOfServing : Eat4HealthData.metricConversion[id]) {
				if (weightOfServing < smallestServing) {
					smallestServing = weightOfServing;
					optimalServingSizeID = index;
				}
				index++;
			}
			Eat4HealthData.optimalServingId[id] = optimalServingSizeID;
		}
		float threadTime = (float) ((System.currentTimeMillis() - start) / 1000.0);

		System.out.println("--------------------------->" + threadTime + "\n" + Eat4HealthData.oddUnits[0].length);
	}
}
