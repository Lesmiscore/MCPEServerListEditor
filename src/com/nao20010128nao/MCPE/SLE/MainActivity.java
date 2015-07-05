package com.nao20010128nao.MCPE.SLE;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import java.io.*;
import android.view.View.*;

public class MainActivity extends ListActivity
{
    /** Called when the activity is first created. */
	List<String[]> servers=null;
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setListAdapter(new InternalListAdapter());
    }
	class InternalListAdapter extends ArrayAdapter<String[]>
	{
		public InternalListAdapter(){
			super(MainActivity.this,0,servers=loadFile());
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			// TODO: Implement this method
			if(convertView!=null)return convertView;
			String[] data=getItem(position);
			data[0]=Integer.toString(position);
			convertView=getLayoutInflater().inflate(R.layout.compedit,null);
			convertView.setTag(data);
			EditText name=(EditText)convertView.findViewById(R.id.serverName);
			EditText ip=(EditText)convertView.findViewById(R.id.serverIp);
			EditText port=(EditText)convertView.findViewById(R.id.serverPort);
			ImageButton delete=(ImageButton)convertView.findViewById(R.id.serverDelete);
			name.setText(data[1]);
			ip.setText(data[2]);
			port.setText(data[3]);
			
			name.setTag(data);
			ip.setTag(data);
			port.setTag(data);
			delete.setTag(data);
			
			delete.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					String[] data=(String[])v.getTag();
					
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
}
