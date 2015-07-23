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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentMain extends Fragment {

    private boolean mSearchCheck;
    private static final String TEXT_FRAGMENT = "TEXT_FRAGMENT";

    int imgIDs[]={
            R.drawable.car1,R.drawable.car2,R.drawable.car3,R.drawable.car4,R.drawable.car5,R.drawable.car6,
            R.drawable.car7,R.drawable.car8,R.drawable.car9,R.drawable.car10,R.drawable.car11,R.drawable.car12,
            R.drawable.car13,
    };

    public String[] carName= {"东风本田新款CR-V实车","起亚发布Novo概念","吉利博瑞全系车型","日产SUV","Davis Divan II","奥迪推A8 5.5特别版","菲斯克新款Karma","大众","雪佛兰全新Spark",
            "法拉利推加强版","海马新1.8TSUV","奔驰GLE","捷豹全新一代XF",

    };
    private ListView mListView;

    public FragmentMain newInstance(String text){
		FragmentMain mFragment = new FragmentMain();
		Bundle mBundle = new Bundle();
		mBundle.putString(TEXT_FRAGMENT, text);
		mFragment.setArguments(mBundle);
		return mFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
        for (int k=0;k<13;k++){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("image", imgIDs[k]);
            map.put("textview", carName[k]);
            data.add(map);

        }

		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        String[] from={"image","textview"};
        int[] to={R.id.imageView_fragment_main_listitem,R.id.textView_fragment_main_listitem};
        mListView = (ListView) rootView.findViewById(R.id.listView_main);
        mListView.setAdapter(new SimpleAdapter(getActivity(), data, R.layout.fragment_main_listitem, from, to));


		//rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT ));
		return rootView;		
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
