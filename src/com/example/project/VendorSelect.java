package com.example.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
//import android.widget.TextView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

public class VendorSelect extends Activity {

	String lid1, mid;
	JSONParser jParser = new JSONParser();
	int success;
	ProgressDialog pDialog;
	ListView lVendor;
	ArrayAdapter<String> adapter;
	List<String> lv;
	HashMap<String, String> map;
	//TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vendor_select);
		
		//tv = (TextView)findViewById(R.id.tv);
		lid1 = getIntent().getExtras().getString("id1");
		mid = getIntent().getExtras().getString("id");
		
		map = new HashMap<String, String>();
		lv = new ArrayList<String>();
		lVendor = (ListView)findViewById(R.id.lvVendor);
		
		//lid1 = "1";
		//lid1 =  String.valueOf(1) ; 
		//tv.setText(lid);
		
		new VendorList().execute();
		
		
		
		lVendor.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				String namev = parent.getItemAtPosition(pos).toString();
				Intent i = new Intent(getApplicationContext(), VendorInfo.class);
				i.putExtra("vid",map.get(namev));
				i.putExtra("id", mid);
				i.putExtra("idl", lid1);
				startActivity(i);
			}
			
		});
		
	}

	class VendorList extends AsyncTask<Void, Void, Void>
	{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			pDialog = new ProgressDialog(VendorSelect.this);
			pDialog.setTitle("Fetching Vendors.. please wait!! ");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			params.add(new BasicNameValuePair("lid", lid1));
			
			
			
				JSONObject json = jParser.makeHttpRequest("http://10.0.2.2/android/OrderurTiffinDatabase/fetchVendorNames.php", "GET", params);
				
				Log.d("Response:", json.toString());
			try{	
				success = json.getInt("success");
				JSONArray vendors = json.getJSONArray("vendors");
				for(int i=0;i<vendors.length();i++)
				{	
					JSONObject ven = vendors.getJSONObject(i);
					String vname = ven.getString("vendor_name");
					String vid = ven.getString("vendor_id");
					
					map.put(vname, vid);
					
					lv.add(vname);
				}
				//if(success == 1){
					//Toast.makeText(getApplicationContext(), "successful", Toast.LENGTH_LONG).show();
				//}
			}catch(JSONException e){
				e.printStackTrace();
			}   
			
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(pDialog.isShowing())
				pDialog.cancel();
			
			if(success == 1){
				//Toast.makeText(getApplicationContext(), "successful", Toast.LENGTH_LONG).show();
				adapter = new ArrayAdapter<String>(VendorSelect.this,android.R.layout.simple_list_item_1, lv);
				lVendor.setAdapter(adapter);
			}
		}
	}
	
}
