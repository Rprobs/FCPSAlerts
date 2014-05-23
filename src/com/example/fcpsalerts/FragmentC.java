package com.example.fcpsalerts;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.fcpsalerts.fcpsalerts.R;

public class FragmentC extends Fragment {

	private RelativeLayout layout;
	private Switch notificationsSwitch;
	private EditText refreshRate;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout = (RelativeLayout) inflater.inflate(R.layout.fragment_c,
				container, false);
		notificationsSwitch = (Switch) layout
				.findViewById(R.id.notificationSwitch);
		notificationsSwitch.setChecked(getActivity().getSharedPreferences(
				"ASyncTask_Demo", 0).getBoolean("notifications", true));
		notificationsSwitch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						SharedPreferences.Editor editor = getActivity()
								.getSharedPreferences("ASyncTask_Demo", 0)
								.edit();
						editor.putBoolean("notifications", isChecked);
						Log.v("Switch State=", "" + isChecked);
					}
				});

		refreshRate = (EditText) layout.findViewById(R.id.refreshRate);
		refreshRate.setText(
				""
						+ getActivity().getSharedPreferences("ASyncTask_Demo",
								0).getInt("refreshRate", 60),
				TextView.BufferType.EDITABLE);
		refreshRate.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// NOTHING

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// NOTHING

			}

			@Override
			public void afterTextChanged(Editable s) {
				SharedPreferences.Editor editor = getActivity()
						.getSharedPreferences("ASyncTask_Demo", 0).edit();
				if (s.toString().length() > 0
						&& Integer.parseInt(s.toString()) >= 5) {
					editor.putInt("refreshRate", Integer.parseInt(s.toString()));
					Log.v("SharedPreferencesRefrestRate=", s.toString());
				}
			}

		});

		return layout;
	}

}
