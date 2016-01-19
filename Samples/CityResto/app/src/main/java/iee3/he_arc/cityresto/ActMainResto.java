package iee3.he_arc.cityresto;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.renderscript.ScriptGroup;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class ActMainResto extends AppCompatActivity {


    public static ClassDiskLruImageCache mDiskLruImageCache;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    private static final String DISK_CACHE_SUBDIR = "thumbnails";
    public static final String TABSELECT = "TABSELECT";


    private static final String TAG = "ActMainResto" ;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private static ViewPager viewPager;
    private static ViewPagerAdapter adapter;
    private int[] tabIcons = {
            R.drawable.ic_tab_map,
            R.drawable.ic_tab_setting,
            R.drawable.ic_tab_favourite
    };


    //Service
    private boolean mBound=false;
    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            Log.i(TAG,"Service Connected");
            ClassMainStorageManager.gps = ((ServiceGoogleHelper.LocalBinder) service).getService();
            mBound=true;

        }

        /**
         * Called when a connection to the Service has been lost.  This typically
         * happens when the process hosting the service has crashed or been killed.
         * This does <em>not</em> remove the ServiceConnection itself -- this
         * binding to the service will remain active, and you will receive a call
         * to {@link #onServiceConnected} when the Service is next running.
         *
         * @param name The concrete component name of the service whose
         *             connection has been lost.
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG,"Service Disconnected");
            mBound=false;
            ClassMainStorageManager.gps = null;
        }

    };
        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_main_resto);
        //Binding du service
        getApplicationContext().bindService(new Intent(ActMainResto.this, ServiceGoogleHelper.class), this.mConnection, Context.BIND_AUTO_CREATE);
            mDiskLruImageCache = new ClassDiskLruImageCache(getApplicationContext(),
                    DISK_CACHE_SUBDIR,
                    DISK_CACHE_SIZE,
                    Bitmap.CompressFormat.JPEG,
                    100);
    }

    @Override
    protected void onStart() {
        super.onStart();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        setupViewPager(viewPager, adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setPage(this, ClassMainStorageManager.lPositionTab); // At the begining, position = 0. Sometimes, = 2
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    //LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(TABSELECT));
                }
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
        setupTabIcons();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //Binding du service
        getApplicationContext().bindService(new Intent(ActMainResto.this, ServiceGoogleHelper.class), this.mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mBound)
        {
            mBound=false;
            getApplicationContext().unbindService(mConnection);
        }
    }
    @Override
   public void onPause() {
        super.onPause();
        if(mBound)
        {
            mBound=false;
            getApplicationContext().unbindService(mConnection);

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBound) {
            mBound = false;
            getApplicationContext().unbindService(mConnection);

        }
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager, ViewPagerAdapter adapter) {
        adapter.addFragment(new FragMapList(), "");
        adapter.addFragment(new FragParameters(), "");
        adapter.addFragment(new FragFavourites(), "");
        viewPager.setAdapter(adapter);
    }

    // Going to the Map/List fragment
    public static void setPage(Context c, int pos){
        viewPager.setCurrentItem(pos);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    //Partie options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_main_resto, menu);
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
    
}
