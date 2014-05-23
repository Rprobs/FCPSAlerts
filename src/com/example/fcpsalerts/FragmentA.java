package com.example.fcpsalerts;

import java.io.IOException;
import org.jsoup.Jsoup;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fcpsalerts.fcpsalerts.R;

public class FragmentA extends Fragment {

	private RelativeLayout layout;
	private Button button;
	private TextView statusDisplay;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		layout = (RelativeLayout) inflater.inflate(R.layout.fragment_a,
				container, false);

		statusDisplay = (TextView) layout.findViewById(R.id.status);
		// Sets up refresh button to refresh status
		button = (Button) layout.findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				statusDisplay.setText("Loading...");
				AsyncTaskStatusFetcher runner = new AsyncTaskStatusFetcher();
				runner.execute();
			}
		});
		// Fetches the status on start
		AsyncTaskStatusFetcher onStartStatusFetcher = new AsyncTaskStatusFetcher();
		onStartStatusFetcher.execute();
		// Status status service
		Intent startFCPS_Status_Service = new Intent(getActivity(),
				FCPS_Status_Service.class);
		getActivity().startService(startFCPS_Status_Service);

		return layout;
	}

	private class AsyncTaskStatusFetcher extends
			AsyncTask<String, String, String> {
		private String status;

		@Override
		protected String doInBackground(String... params) {
			publishProgress("Loading");
			try {
				status = Jsoup.connect("http://www.fcps.edu/news/emerg.shtml")
						.get().getElementsByTag("strong").first().text();
			} catch (IOException e) {
				e.printStackTrace();
				status = e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				status = e.getMessage();
			}
			return status;
		}

		@Override
		protected void onPostExecute(String result) {
			((TextView) getActivity().findViewById(R.id.status))
					.setText(result);
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onProgressUpdate(String... text) {

		}
	}

}
