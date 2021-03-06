package harlequinmettle.android.eat4health.datautil;

import harlequinmettle.android.eat4health.Eat4HealthData;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import android.content.Context;

public class ObjectStoringThread implements Runnable {

	Context context;
	float[][] db;
	long time;
	float saveTime;

	public ObjectStoringThread(Context context, float[][] db) {
		this.context = context;
		this.db = db;
	}

	public void run() {
		if (!Eat4HealthData._loaded) {
			try {
				Eat4HealthData.loadingThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("STARTING TO SAVE DATA AS OBJECT NOW:  ---->");
		time = System.currentTimeMillis();
		for (int i = 0; i < db.length; i++) {

			try {
				FileOutputStream fos = context.openFileOutput(ObjectLoadingThread.DATA_100G + i, Context.MODE_PRIVATE);
				ObjectOutputStream objout = new ObjectOutputStream(fos);
				objout.writeObject(db[i]);
				objout.flush();
				objout.close();

			} catch (IOException ioe) {
				System.out.println("NO save: saver");
			}
			Thread.yield();
		}

		saveTime = (float) ((System.currentTimeMillis() - time) / 1000.0);
		System.out.println("----DONE SAVING DB---time to save db---->" + (saveTime));
	}
}
