package com.example.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.ListActivity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
//import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ViewOrder extends ListActivity {

	String vid;
	JSONParser jParser;
	int success;
	ArrayList<HashMap<String,String>> orderList;
	ListView lv;
	JSONArray order;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_order);
		
		jParser = new JSONParser();
		orderList = new ArrayList<HashMap<String,String>>();
		lv = getListView();
		vid = getIntent().getExtras().getString("idv");
		//Toast.makeText(getApplicationContext(), ""+vid, Toast.LENGTH_LONG).show();
		new viewOrder().execute();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long arg3) {
				
				
				TextView tv = ( (TextView)view.findViewById(R.id.tv) );
				String id = tv.getText().toString();
				//Toast.makeText(getApplicationContext(), ""+id, Toast.LENGTH_LONG).show();
				Intent i = new Intent(ViewOrder.this , OrderDetails.class) ; 
				i.putExtra("oid", id);
				i.putExtra("vid", vid);
				startActivity(i) ; 
			}
			
		});
	}

	class viewOrder extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ivd",vid));
			
			JSONObject json = jParser.makeHttpRequest("http://10.0.2.2/android/OrderurTiffinDatabase/viewOrder.php", "GET", params);
			Log.d("Order response: ", json.toString());
			
			try{
				
				success = json.getInt("success");
				if(success == 1){
					order = json.getJSONArray("orders");
					
					for(int i=0;i<order.length();i++){
						JSONObject product = order.getJSONObject(i);
						String pid = product.getString("id");
						String name = product.getString("name");
						
						HashMap<String,String> map = new HashMap<String,String>();
						map.put("id", pid);
						map.put("name", name);
						
						orderList.add(map);
			    }
			}
			}catch(JSONException e){
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			ListAdapter adapter = new SimpleAdapter(ViewOrder.this,orderList,R.layout.list_item,new String[]{"id","name"},new int[]{R.id.tv,R.id.tv1});
			 setListAdapter(adapter);
			
	}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_order, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId())
		{
		case R.id.pass: 
			Intent i = new Intent(this, PasswordChange.class);
			i.putExtra("idv", vid);
			startActivity(i);
			return true;
			
			
		case R.id.update:
			Intent in = new Intent( this, VendorMdetails.class);
			in.putExtra("idv", vid);
			startActivity(in);
			return true;
			
		case R.id.order:
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
}
