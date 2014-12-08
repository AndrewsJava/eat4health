package harlequinmettle.android.eat4health.myviews.fullsortablefoodlist;

import harlequinmettle.android.tools.androidsupportlibrary.ContextReference;
import harlequinmettle.android.tools.androidsupportlibrary.TextViewFactory;
import harlequinmettle.android.tools.androidsupportlibrary.ViewFactory;
import harlequinmettle.android.tools.androidsupportlibrary.interfaces.ScrollObjectOutOfViewCallBack;
import harlequinmettle.android.tools.androidsupportlibrary.overridecustomization.CustomScrollView;
import android.content.Context;
import android.view.Gravity;
import android.widget.EditText;

public class SearchFoodListColapsableGroupScrollingLayoutView extends SearchFoodListColapsableGroupScrollingAsyncTasksAndListeners
		implements ScrollObjectOutOfViewCallBack {

	public SearchFoodListColapsableGroupScrollingLayoutView() {
		super(ContextReference.getAppContext());
		setUpSearchabelList();
	}

	public SearchFoodListColapsableGroupScrollingLayoutView(Context context) {
		super(context);
		setUpSearchabelList();
	}

	private void setUpSearchabelList() {
		this.context = ContextReference.getAppContext();
		setCustomizations();
		constructLayout();
		new LoadFoodsToUIListAsyncTask().execute();
	}

	private void constructLayout() {

		searchBox = new EditText(context);
		searchBox.setBackgroundColor(0xffffffff);
		searchBox.addTextChangedListener(textFilterInputListener);

		imobileLabel = TextViewFactory.makeAnotherTextView(defaultFloatingLabelText);
		imobileLabel.setBackgroundColor(0xff454545);
		imobileLabel.setOnClickListener(colapseListener);

		scrollingFilterableContainer = new CustomScrollView();
		child = ViewFactory.basicLinearLayout();
		scrollingFilterableContainer.addView(child);
		scrollingFilterableContainer.registerCallBack(this);
		addView(searchBox);
		addView(imobileLabel);
		addView(scrollingFilterableContainer);
	}

	private void setCustomizations() {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER_VERTICAL);
		setLayoutParams(params);
		setOrientation(VERTICAL);
		setBackgroundColor(0xFF202020);
	}

	@Override
	public void notifyTopAfterScrollEvent(int scrolltop) {

		updateFloatingTextLabel(scrolltop);

	}

}
