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
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class VendorLogin extends Activity implements OnClickListener {
   int success ; 
	private ProgressDialog pDialog;
	EditText etName,etPass;
	Button bLogin;
	String vid;
	TextView tvSignUp;
	JSONParser jParser = new JSONParser();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vendor_login);
		
		etName = (EditText)findViewById(R.id.etName);
		etPass = (EditText)findViewById(R.id.etPass);
		bLogin = (Button)findViewById(R.id.bLogin);
		//tvSignUp = (TextView)findViewById(R.id.tvSignUp);
		
		bLogin.setOnClickListener(this);
		/*tvSignUp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), VendorMdetails.class);
				i.putExtra("vname", "");
				i.putExtra("vemail", "");
				i.putExtra("vcontact", "");
				i.putExtra("idv",vid);
				startActivity(i);
			}
		});*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.vendor_login, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String name = etName.getText().toString();
		String pass = etPass.getText().toString();
		
		if(!name.isEmpty() && !pass.isEmpty())
		{
			new LoginAsync().execute();
		}
		else{
			Toast.makeText(getApplicationContext(), "Fill the required credentials !!", Toast.LENGTH_LONG).show();
		}
	}

		class LoginAsync extends AsyncTask<Void,Void,Void>{
			
			String uname = etName.getText().toString();
			String passw = etPass.getText().toString();
			String name, email, contact;
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog = new ProgressDialog(VendorLogin.this);
				pDialog.setTitle("Login !! Please wait...");
				pDialog.setCancelable(false);
				pDialog.show();
			}
			
			@Override
			protected Void doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("uname", uname));
				params.add(new BasicNameValuePair("pwd", passw));
				JSONObject json = jParser.makeHttpRequest("http://10.0.2.2/android/OrderurTiffinDatabase/fetchVendor.php", "GET", params);
				Log.d("Response", json.toString());
				
				try{
					name = json.getString("name");
					email = json.getString("email");
					contact = json.getString("contact");
					vid = json.getString("id");
					
					success = json.getInt("success");
					
					
				}catch (JSONException e) {
					// TODO Auto-generated catch block
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
					Toast.makeText(getApplicationContext(), "Access Granted", Toast.LENGTH_LONG).show();
					Intent i = new Intent(getApplicationContext(), VendorMdetails.class);
					finish();
					i.putExtra("vname", name);
					i.putExtra("vemail", email);
					i.putExtra("vcontact", contact);
					i.putExtra("idv", vid);
					startActivity(i);
				}
				else
					Toast.makeText(getApplicationContext(), "Access Denied", Toast.LENGTH_LONG).show();
			}

		}

}
