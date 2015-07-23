package com.example.blank.cuhui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

import br.liveo.interfaces.NavigationLiveoListener;
import br.liveo.navigationliveo.NavigationLiveo;


public class MainActivity extends NavigationLiveo implements NavigationLiveoListener {
    public List<String> mListNameItem;

    @Override
    public void onUserInformation() {

        this.mUserName.setText("簇慧科技");
        this.mUserEmail.setText("昆明市盘龙区北京路延长线金江小区熙苑A20幢");
        this.mUserPhoto.setImageResource(R.drawable.ic_lee);
        this.mUserBackground.setImageResource(R.drawable.ic_user_background);

    }

    @Override
    public void onInt(Bundle savedInstanceState) {

        // set listener {required}
        this.setNavigationListener(this);

        //First item of the position selected from the list
        this.setDefaultStartPositionNavigation(0);

        // name of the list items
        mListNameItem = new ArrayList<>();
        mListNameItem.add(0, getString(R.string.inbox));
        mListNameItem.add(1, getString(R.string.starred));
        mListNameItem.add(2, getString(R.string.sent_mail));
        mListNameItem.add(3, getString(R.string.drafts));
        mListNameItem.add(4, getString(R.string.more_markers)); //This item will be a subHeader
        mListNameItem.add(5, getString(R.string.trash));
        mListNameItem.add(6, getString(R.string.spam));

        // icons list items
        List<Integer> mListIconItem = new ArrayList<>();
        mListIconItem.add(0, R.drawable.ic_home_black_24dp);
        mListIconItem.add(1, R.drawable.ic_drive_eta_black_24dp); //Item no icon set 0
        mListIconItem.add(2, R.drawable.ic_star_black_24dp); //Item no icon set 0
        mListIconItem.add(3, R.drawable.ic_perm_contact_cal_black_24dp);
        mListIconItem.add(4, 0); //When the item is a subHeader the value of the icon 0
        mListIconItem.add(5, R.drawable.ic_group_add_black_24dp);
        mListIconItem.add(6, R.drawable.ic_report_black_24dp);

        //{optional} - Among the names there is some subheader, you must indicate it here
        List<Integer> mListHeaderItem = new ArrayList<>();
        mListHeaderItem.add(4);

        //{optional} - Among the names there is any item counter, you must indicate it (position) and the value here
        SparseIntArray mSparseCounterItem = new SparseIntArray(); //indicate all items that have a counter
        mSparseCounterItem.put(0, 7);
        mSparseCounterItem.put(1, 123);
        mSparseCounterItem.put(6, 250);

        //If not please use the FooterDrawer use the setFooterVisible(boolean visible) method with value false
        this.setFooterInformationDrawer(R.string.settings, R.drawable.ic_settings_black_24dp);

        this.setNavigationAdapter(mListNameItem, mListIconItem, mListHeaderItem, mSparseCounterItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClickNavigation(int position, int layoutContainerId) {

        Fragment mFragment=null;
        String mTittle=null;
        FragmentManager mFragmentManager = getSupportFragmentManager();

        //Fragment mFragment = new FragmentMain().newInstance(mListNameItem.get(position));
        switch (position)
        {
            case  0:
                mFragment=new FragmentMain().newInstance(mListNameItem.get(position));
                mTittle=getResources().getString(R.string.app_name);
                break;

            case 1:mFragment=new FragmentCollect().newInstance(mListNameItem.get(position));
                 mTittle=getResources().getString(R.string.starred);
                break;
            case 2:mFragment=new Fragment2Collect().newInstance(mListNameItem.get(position));
                mTittle=getResources().getString(R.string.sent_mail);
                break;
            case 3:
                mFragment=new FragmentTrash().newInstance(mListNameItem.get(position));
                mTittle=getResources().getString(R.string.drafts);
                break;
            case 5:
                mFragment=new FragmentFeedback().newInstance(mListNameItem.get(position));
                 mTittle=getResources().getString(R.string.trash);
                break;

            case 6:

            mFragment=new FragmentSpam().newInstance(mListNameItem.get(position));
             mTittle=getResources().getString(R.string.spam);
            break;

        }

        if (mFragment != null){
            mFragmentManager.beginTransaction().replace(layoutContainerId, mFragment).commit();
            getSupportActionBar().setTitle(mTittle);
        }
    }

    @Override
    public void onPrepareOptionsMenuNavigation(Menu menu, int position, boolean visible) {
        //hide the menu when the navigation is opens
        switch (position) {
            case 0:
                menu.findItem(R.id.menu_add).setVisible(!visible);
                menu.findItem(R.id.menu_search).setVisible(!visible);
                break;

            case 1:
                menu.findItem(R.id.menu_add).setVisible(!visible);
                menu.findItem(R.id.menu_search).setVisible(!visible);
                break;
        }
    }

    @Override
    public void onClickFooterItemNavigation(View v) {


        startActivity(new Intent(this, Settings1Activity.class));

    }

    @Override
    public void onClickUserPhotoNavigation(View v) {
        Toast.makeText(this,"😊你竟然点了帅哥的头像😘",Toast.LENGTH_SHORT).show();


    }
}
