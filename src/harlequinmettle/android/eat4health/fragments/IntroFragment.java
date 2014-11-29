package harlequinmettle.android.eat4health.fragments;

import harlequinmettle.android.eat4health.R;
import harlequinmettle.android.tools.androidsupportlibrary.ContextReference;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;

public class IntroFragment extends Fragment {
	ProgressBar progressBar;

	public IntroFragment(ProgressBar pb) {
		// Empty constructor required for fragment subclasses
		this.progressBar = pb;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout rootView = new LinearLayout(ContextReference.getAppContext());
		rootView.setOrientation(LinearLayout.VERTICAL);
		rootView.setGravity(Gravity.CENTER);
		ImageView icon = new ImageView(ContextReference.getAppContext());
		icon.setImageResource(R.drawable.ic_launcher);
		rootView.addView(icon);

		LayoutParams barParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		barParams.width = 200;
		barParams.height = 100;
		progressBar.setLayoutParams(barParams);

		rootView.addView(progressBar);

		getActivity().setTitle("loading...");
		return rootView;
	}
}
