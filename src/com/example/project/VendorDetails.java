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
import android.widget.Toast;

public class VendorDetails extends Activity implements OnClickListener {

	String name, email, pass, contact;
	int success;
	EditText etName, etFName, etEmail, etFirm, etContact, etPass, etLocality;
	String url = "http://10.0.2.2/android/OrderurTiffinDatabase/addVendor.php" ;
	Button bMenu;
	JSONParser jParser = new JSONParser();
	private ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vendor_details);
		
		etName = (EditText)findViewById(R.id.eName);
		etFName = (EditText)findViewById(R.id.eFirm);
		etEmail = (EditText)findViewById(R.id.eEmail);
		etFirm = (EditText)findViewById(R.id.eAdd);
		etContact = (EditText)findViewById(R.id.eContact);
		etPass = (EditText)findViewById(R.id.epass);
		etLocality = (EditText)findViewById(R.id.eLoc);
		bMenu = (Button)findViewById(R.id.bMenu);
		
		name = getIntent().getExtras().getString("vname");
		email = getIntent().getExtras().getString("vemail");
		contact = getIntent().getExtras().getString("vcontact");
		
		etName.setText(name);
		etFName.setText("");
		etEmail.setText(email);
		etFirm.setText("");
		etContact.setText(contact);
		
		String vname = etName.getText().toString();
		String vFirm = etFName.getText().toString();
		String vEmail = etEmail.getText().toString();
		String vadd = etFirm.getText().toString();
		String vpass = etPass.getText().toString();
		String vcontact = etContact.getText().toString();
		
		if(vname.isEmpty() && vFirm.isEmpty() && vEmail.isEmpty() && vadd.isEmpty() && vcontact.isEmpty() && vpass.isEmpty()){
			
			bMenu.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					new InsertVendor().execute();
				
				}
				
				class InsertVendor extends AsyncTask<Void,Void,Void>{

					@Override
					protected void onPreExecute() {
						// TODO Auto-generated method stub
						super.onPreExecute();
						
						pDialog = new ProgressDialog(VendorDetails.this);
						pDialog.setTitle("Adding Product !! Please wait...");
						pDialog.setCancelable(false);
						pDialog.show();
					}
					
					@Override
					protected Void doInBackground(Void... arg0) {
						// TODO Auto-generated method stub
						
						String vname = etName.getText().toString();
						String vpass = etPass.getText().toString();
						String vFirm = etFName.getText().toString();
						String vEmail = etEmail.getText().toString();
						String vadd = etFirm.getText().toString();
						String vcontact = etContact.getText().toString();
						String vlocality = etLocality.getText().toString();
						

						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("name", vname ));
						params.add(new BasicNameValuePair("pass", vpass ));
						params.add(new BasicNameValuePair("firm",vFirm));
						params.add(new BasicNameValuePair("mail",vEmail));
						params.add(new BasicNameValuePair("address",vadd));
						params.add(new BasicNameValuePair("contact",vcontact));
						params.add(new BasicNameValuePair("locality",vlocality));
						
						try{
							JSONObject json = jParser.makeHttpRequest(url, "GET", params);
							Log.d("Response:", json.toString());
							success = json.getInt("success");
							
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
						
						if(success==1){
							Toast.makeText(getApplicationContext(), "Vendor Added !!!", Toast.LENGTH_LONG).show();
							
							Intent i = new Intent(VendorDetails.this, VendorMdetails.class);
							i.putExtra("vname", "");
							startActivity(i);
						}
					}
					
				}
			});
			
		}
		else
			bMenu.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.vendor_details, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		Intent i = new Intent(VendorDetails.this, VendorMdetails.class);
		i.putExtra("vname", name);
		startActivity(i);
	}

}
