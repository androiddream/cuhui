/*
 * Copyright 2015 Rudson Lima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.blank.cuhui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.CarManage;
import widgets.AnimatedExpandableListView;


public class FragmentCollect extends Fragment {
    private AnimatedExpandableListView listView;
    private ExampleAdapter adapter;
    private List<CarManage> list;
    private ProgressBar pbar;
    String nameSpace = "http://tempuri.org/";
    // 调用方法的名称
    String methodName = "SelectCarInfoList";
    // EndPoint
    String serviceURL = "http://www.yncuhui.com/RentCarSTD/webserver/RentCarServer.asmx";
    // SOAP Action
    String soapAction = "http://tempuri.org/SelectCarInfoList";
    private SoapObject result;
    private boolean mSearchCheck;
    private static final String TEXT_FRAGMENT = "TEXT_FRAGMENT";

	public FragmentCollect newInstance(String text){
		FragmentCollect mFragment = new FragmentCollect();
		Bundle mBundle = new Bundle();
		mBundle.putString(TEXT_FRAGMENT, text);
		mFragment.setArguments(mBundle);
		return mFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated metho


        List<GroupItem> items = new ArrayList<GroupItem>();
        //List<Blackinfo> list=new ArrayList<Blackinfo>();
        RequesetAndParse();
		View rootView = inflater.inflate(R.layout.fragment_collect, container, false);
        pbar= (ProgressBar) rootView.findViewById(R.id.progressBar);
        listView= (AnimatedExpandableListView) rootView.findViewById(R.id.listView);
       // TextView mTxtTitle = (TextView) rootView.findViewById(R.id.txtTitle);
        //mTxtTitle.setText(getArguments().getString(TEXT_FRAGMENT));




        // listView.setAdapter(adapter);

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });
        return rootView;
	}
    public Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            // 将WebService得到的结果返回给TextView
            List<GroupItem> items = new ArrayList<GroupItem>();
            //ArrayList<Blackinfo> list1= (ArrayList<Blackinfo>) msg.obj;
            ArrayList<CarManage>  list= (ArrayList<CarManage>) msg.obj;
            for(int i = 0; i < list.size(); i++) {
                GroupItem item = new GroupItem();
                //Blackinfo blackinfo=new Blackinfo();

                item.title = list.get(i).getCarbrand()
                        .toString();


                ChildItem child = new ChildItem();
                //child.title = "证件类型：" +list.get(i).getIdtype().toString();

                // child.hint = "证件号："+list.get(i).getIdnum().toString();


                item.items.add(child);


                items.add(item);
            }

            adapter = new ExampleAdapter(getActivity());

            adapter.setData(items);
            listView.setAdapter(adapter);
            pbar.setVisibility(View.INVISIBLE);

        };
    };
    private void RequesetAndParse() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CarManage carmanage = new CarManage();
                String str = "";
                SoapObject soapObject = new SoapObject(nameSpace, methodName);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                envelope.bodyOut = soapObject;
                new MarshalBase64().register(envelope);
                envelope.setOutputSoapObject(soapObject);

                // SoapObject soapChild= (SoapObject) soapObject.getProperty(1);
                HttpTransportSE transport = new HttpTransportSE(serviceURL);
                transport.debug = true;
                try {

                    transport.call(nameSpace + methodName, envelope);
                    SoapObject result = (SoapObject) envelope.getResponse();
                    //下面对结果进行解析，结构类似json对象
                    //str= result.getProperty(6).toString();

                    list = parseCarmanage(result);

                    Message msg=handler.obtainMessage();
                    msg.obj=list;
                    handler.sendMessage(msg);





                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public List<CarManage> parseCarmanage(SoapObject result)
    {
        list=new ArrayList<CarManage>();
        CarManage carmanage=null;
        SoapObject soapChild= (SoapObject) result.getProperty(1);
        SoapObject soapFuckingchild= (SoapObject) soapChild.getProperty(0);
        SoapObject soaplittleObject= (SoapObject) soapFuckingchild.getProperty(0);
        for (int j=0;j<soapFuckingchild.getPropertyCount();j++)
        {
            SoapObject soapRootchild= (SoapObject) soapFuckingchild.getProperty(j);
            carmanage=new CarManage();

            carmanage.setCarnum(soapRootchild.getProperty(1).toString());
            carmanage.setCarbrand(soapRootchild.getProperty(2).toString());
            carmanage.setCartype(soapRootchild.getProperty(3).toString());
            carmanage.setCarcolor(soapRootchild.getProperty(4).toString());
            carmanage.setCarstate(soapRootchild.getProperty(5).toString());
            carmanage.setCarrent( soapRootchild.getProperty(6).toString());
            carmanage.setCarmargin(soapRootchild.getProperty(7).toString());
            carmanage.setMotonum(soapRootchild.getProperty(8).toString());
            carmanage.setVim(soapRootchild.getProperty(9).toString());
            list.add(carmanage);

        }
        /*for (int i=0;i<soaplittleObject.getPropertyCount();i++)
        {
            blackinfo=new Blackinfo();
            blackinfo.setId(soaplittleObject.getProperty(0).toString());
            blackinfo.setName(soaplittleObject.getProperty(1).toString());
            blackinfo.setSex(soaplittleObject.getProperty(2).toString());
            blackinfo.setIdtype(soaplittleObject.getProperty(3).toString());
            blackinfo.setIdnum(soaplittleObject.getProperty(4).toString());
            blackinfo.setDate( soaplittleObject.getProperty(5).toString());
            blackinfo.setCompany(soaplittleObject.getProperty(6).toString());
            blackinfo.setReason(soaplittleObject.getProperty(7).toString());
            blackinfo.setEtc(soaplittleObject.getProperty(8).toString());
            list.add(blackinfo);
            //list.add(1,blackinfo);






        }*/

        return list;
    }

    private static class GroupItem {
        String title;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private static class ChildItem {
        String title;
        String hint;
    }

    private static class ChildHolder {
        TextView title;
        TextView hint;
        TextView textsex;
        TextView textdate;
        TextView textcompany;
        TextView textreason;
        TextView textetc;
    }

    private static class GroupHolder {
        TextView title;
    }

    /**
     * Adapter for our list of {@link GroupItem}s.
     */
    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                holder.textsex = (TextView) convertView.findViewById(R.id.textsex);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                holder.hint = (TextView) convertView.findViewById(R.id.textHint);
                holder.textdate = (TextView) convertView.findViewById(R.id.textdate);
                holder.textcompany = (TextView) convertView.findViewById(R.id.textcompany);
                holder.textreason = (TextView) convertView.findViewById(R.id.textreason);
                holder.textetc = (TextView) convertView.findViewById(R.id.textetc);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }
            holder.textsex.setText("车型:"+list.get(groupPosition).getCartype().toString());
            holder.title.setText( "品牌:"+list.get(groupPosition).getCarbrand().toString());
            //holder.title.setText(item.title);
            holder.hint.setText("车牌号:" +list.get(groupPosition).getCarnum().toString());
            holder.textdate.setText("车颜色:"+ list.get(groupPosition).getCarcolor().toString());
            holder.textcompany.setText("此车状态:"+ list.get(groupPosition).getCarstate().toString());
            holder.textreason.setText("租金:"+ list.get(groupPosition).getCarrent().toString());
            holder.textetc.setText("押金:"+ list.get(groupPosition).getCarmargin().toString());
            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.group_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.title);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu, menu);
        
        //Select search item
        final MenuItem menuItem = menu.findItem(R.id.menu_search);
        menuItem.setVisible(true);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(this.getString(R.string.search));

        ((EditText) searchView.findViewById(R.id.search_src_text))
                .setHintTextColor(getResources().getColor(R.color.nliveo_white));
        searchView.setOnQueryTextListener(onQuerySearchView);

		menu.findItem(R.id.menu_add).setVisible(true);

		mSearchCheck = false;	
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch (item.getItemId()) {

		case R.id.menu_add:
            Toast.makeText(getActivity(), R.string.add, Toast.LENGTH_SHORT).show();
			break;

		case R.id.menu_search:
			mSearchCheck = true;
            Toast.makeText(getActivity(), R.string.search, Toast.LENGTH_SHORT).show();
			break;
		}
		return true;
	}	

   private SearchView.OnQueryTextListener onQuerySearchView = new SearchView.OnQueryTextListener() {
       @Override
       public boolean onQueryTextSubmit(String s) {
           return false;
       }

       @Override
       public boolean onQueryTextChange(String s) {
           if (mSearchCheck){
               // implement your search here
           }
           return false;
       }
   };
}
