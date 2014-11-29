package harlequinmettle.android.eat4health.legacyconversion;

import harlequinmettle.android.eat4health.Eat4Health;

import java.util.TreeMap;

import android.content.Context;
import android.view.View;

public class FoodRemovalScroller extends SubScroll {

	View.OnClickListener foodRemovalListener = new View.OnClickListener() {
		public void onClick(View view) {

			for (TreeMap<Integer, Boolean> foodi : Eat4Health.allMyFoods) {
				foodi.remove(view.getId());
				removeButtonById(view.getId());
			}
			Eat4Health.saveObject(Eat4Health.allMyFoods, "MYFOODS");
		}

	};

	FoodRemovalScroller(Context c) {
		super(c);
		// addScrollingButtons(String[] buttonNames, int[] buttonIds,
		// View.OnClickListener listener, int BUTTON_CLR) {
		addScrollingButtons(Eat4Health.getMyFoodsAsTextArray(), Eat4Health.getMyFoodsIdsArray(), foodRemovalListener, 0);

	}

	private void removeButtonById(int id) {
		instanceChild.removeView(instanceChild.findViewById(id));
	}

}
