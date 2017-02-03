package com.example.project;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordChange extends Activity {
	
	EditText eNew, eOld, eRe;
	Button bUpdate;
	String e1, e2, e3, vid;
	JSONParser jParser;
	int success;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_change);
		
		eNew = (EditText)findViewById(R.id.eNew);
		eOld = (EditText)findViewById(R.id.eOld);
		eRe = (EditText)findViewById(R.id.eRe);
		bUpdate = (Button)findViewById(R.id.bChange);
		jParser = new JSONParser();
		vid = getIntent().getExtras().getString("idv");
		
		bUpdate.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				e1 = eOld.getText().toString();
				e2 = eNew.getText().toString();
				e3 = eRe.getText().toString();
				
				if( e1.isEmpty() && e2.isEmpty() && e3.isEmpty())
				{
					Toast.makeText(getApplicationContext(), "Fill the credentials !!", Toast.LENGTH_LONG).show();
				}
				
				else if(!e2.equals(e3) )
				{
					eRe.setText("");
					eRe.setHint("Password do not match");
					Toast.makeText(getApplicationContext(), "Re-confirm your new password", Toast.LENGTH_LONG).show();
				}
				else{
					new updatePass().execute();
				}
			}
			
		});
		
	}

	class updatePass extends AsyncTask<Void, Void, Void>
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
			params.add(new BasicNameValuePair("old",e1));
			params.add(new BasicNameValuePair("new", e2));
			params.add(new BasicNameValuePair("idv", vid));
			
			JSONObject json = jParser.makeHttpRequest("http://10.0.2.2/android/OrderurTiffinDatabase/changePass.php", "GET", params);
			Log.d("pass Respose: ", json.toString());
			
			try{
				success = json.getInt("success");
			}catch(JSONException e){
				
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(success == 1)
			{
				Toast.makeText(getApplicationContext(), "Password changed successfully !!", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.password_change, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId())
		{
		case R.id.pass: 
			return true;
			
		case R.id.update:
			Intent i = new Intent(this, VendorMdetails.class);
			i.putExtra("idv", vid);
			startActivity(i);
			return true;
			
		case R.id.order:
			Intent in = new Intent(this, ViewOrder.class);
			in.putExtra("idv", vid);
			startActivity(in);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
