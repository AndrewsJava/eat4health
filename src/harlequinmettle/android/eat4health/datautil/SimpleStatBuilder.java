package harlequinmettle.android.eat4health.datautil;

import harlequinmettle.android.eat4health.Eat4HealthData;
import harlequinmettle.android.eat4health.Eat4HealthLegacy;

public class SimpleStatBuilder implements Runnable {

	@Override
	public void run() {
		try {
			Eat4HealthData.loadingThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Eat4HealthData.calculatingStats = true;
		Eat4HealthData.statsLoaded = false;
		long time = System.currentTimeMillis();

		int[] foodIds = Eat4HealthLegacy.getFoodSearchIds();
		for (int i = 0; i < 130; i++) {
			boolean b = Eat4HealthData.MY_NUTRIENTS[i];
			if (!b)
				continue;
			float[] basically = StatTool.simpleStats(foodIds, i);

			Eat4HealthData.highlightFactors.put(i, basically);

			Thread.yield();

		}

		Eat4HealthData.statsLoaded = true;
		Eat4HealthData.calculatingStats = false;
	}

}
