package com.nao20010128nao.MCPE.SLE;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import java.io.*;
import android.view.View.*;
import android.util.*;
import android.widget.TextView.*;
import android.net.*;
import android.text.*;
import android.content.*;
import java.lang.reflect.*;

public class MainActivity extends ListActivity
{
    /** Called when the activity is first created. */
	static Method finishMethod=findFinish();
	static List<String[]> servers=null;
	boolean save=false;
	InternalListAdapter ila=null;
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setListAdapter(ila=new InternalListAdapter());
    }
	class InternalListAdapter extends ArrayAdapter<String[]>
	{
		public InternalListAdapter(){
			super(MainActivity.this,0,servers==null?servers=loadFile():servers);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			// TODO: Implement this method
			Log.d("InternalListAdapter","offset:"+position);
			if(convertView==null)
				convertView=getLayoutInflater().inflate(R.layout.compedit,null);
			String[] data=getItem(position);
			data[0]=Integer.toString(position);
			convertView.setTag(data);
			TextView name=(TextView)convertView.findViewById(R.id.serverName);
			TextView ip=(TextView)convertView.findViewById(R.id.serverIp);
			TextView port=(TextView)convertView.findViewById(R.id.serverPort);
			ImageButton delete=(ImageButton)convertView.findViewById(R.id.serverDelete);
			ImageButton edit=(ImageButton)convertView.findViewById(R.id.serverEdit);
			name.setText(data[1]);
			ip.setText(data[2]);
			port.setText(data[3]);
			
			delete.setTag(data);
			edit.setTag(data);
			
			delete.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					final String[] data=(String[])v.getTag();
					new AlertDialog.Builder(MainActivity.this).
						setMessage(getResources().getString(R.string.deleteConfirm).replace("@server@",data[1])).
						setPositiveButton(android.R.string.yes,new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface d,int sel){
								servers.remove(data);
								refreshListView();
							}
						}).
						setNegativeButton(android.R.string.no,new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface d,int sel){}
						}).
						show();
				}
			});
			
			edit.setOnClickListener(new OnClickListener(){
					public void onClick(View v){
						final String[] prevData=(String[])v.getTag();
						final String[] data=((String[])v.getTag()).clone();
						View dialog=getLayoutInflater().inflate(R.layout.servereditdialog,null);
						final EditText name=(EditText)dialog.findViewById(R.id.serverName);
						final EditText ip=(EditText)dialog.findViewById(R.id.serverIp);
						final EditText port=(EditText)dialog.findViewById(R.id.serverPort);
						
						name.setTag(data);
						ip.setTag(data);
						port.setTag(data);
						
						name.setText(data[1]);
						ip.setText(data[2]);
						port.setText(data[3]);
						
						name.addTextChangedListener(new TextWatcher(){
								@Override  
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override  
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									String[] data=(String[])name.getTag();
									data[1]=s.toString();
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
						ip.addTextChangedListener(new TextWatcher(){
								@Override  
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override  
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									String[] data=(String[])ip.getTag();
									data[2]=s.toString();
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
						port.addTextChangedListener(new TextWatcher(){
								@Override  
								public void beforeTextChanged(CharSequence s, int start, int count, int after) {

								}

								@Override  
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									String[] data=(String[])port.getTag();
									data[3]=s.toString();
								}

								@Override
								public void afterTextChanged(Editable s) {

								}
							});
						new AlertDialog.Builder(MainActivity.this).
							setView(dialog).
							setPositiveButton(android.R.string.yes,new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface d,int sel){
									servers.set(servers.indexOf(prevData),data);
									refreshListView();
								}
							}).
							setNegativeButton(android.R.string.no,new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface d,int sel){
									
								}
							}).
							show();
					}
				});
			return convertView;
		}
	}
	List<String[]> loadFile(){
		ArrayList<String[]> al=new ArrayList<String[]>();
		BufferedReader br=null;
		try{
			br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(Environment.getExternalStorageDirectory(),"/games/com.mojang/minecraftpe/external_servers.txt"))));
			while(true){
				String s=br.readLine();
				if(s==null)break;
				Log.d("readLine",s);
				al.add(s.split("\\:"));
			}
		}catch(Throwable ex){
			ex.printStackTrace();
		}finally{
			try{
				if (br != null)
					br.close();
			}catch (IOException e){
				
			}
		}
		return al;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO: Implement this method
		menu.add(0,1,0,R.string.apply).setIcon(android.R.drawable.ic_menu_save);
		menu.add(0,2,0,android.R.string.cancel).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		menu.add(0,3,0,R.string.add).setIcon(android.R.drawable.ic_menu_add);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(/*int featureId, */MenuItem item)
	{
		// TODO: Implement this method
		switch(item.getItemId()){
			case 1:
				save=true;
			case 2:
				try{
					finishMethod.invoke(this);
				}catch(Throwable ex){
					//unreachable
					ex.printStackTrace();
				}
				break;
			case 3:
				servers.add(new String[]{"0","A Minecraft:PE server","localhost","19132"});
				refreshListView();
				break;
		}
		return true;
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		if(!save)
			return;
		FileWriter fw=null;
		try{
			fw=new FileWriter(new File(Environment.getExternalStorageDirectory(),"/games/com.mojang/minecraftpe/external_servers.txt"));
			for(String[] s:servers){
				fw.append(join(s)+"\n");
			}
		}catch(Throwable ex){
			ex.printStackTrace();
		}finally{
			try{
				if (fw != null)
					fw.close();
			}catch (IOException e){

			}
		}
	}
	String join(String[] ary){
		StringBuilder builder = new StringBuilder();

		for(int i=0; i<ary.length-1; i++) {
			builder.append(ary[i]).append(":");
		}

		return builder.append(ary[ary.length-1]).toString();
	}
	static Method findFinish(){
		Class actClas=Activity.class;
		try{
			return actClas.getDeclaredMethod("finishAndRemoveTask");
		}catch(Throwable ex){
			ex.printStackTrace();
		}
		try{
			//finish() must be found
			return actClas.getDeclaredMethod("finish");
		}catch(Throwable ex){
			//unreachable
			ex.printStackTrace();
		}
		return null;//unreachable
	}
	public void refreshListView(){
		ila.notifyDataSetChanged();
	}
}
