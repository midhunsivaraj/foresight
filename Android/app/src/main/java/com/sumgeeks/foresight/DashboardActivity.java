package com.sumgeeks.foresight;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.sumgeeks.foresight.menu.DrawerAdapter;
import com.sumgeeks.foresight.menu.DrawerItem;
import com.sumgeeks.foresight.menu.SimpleItem;
import com.sumgeeks.foresight.menu.SpaceItem;
import com.sumgeeks.foresight.fragment.CenteredTextFragment;

import java.util.Arrays;

/**
 * Created by yarolegovich on 25.03.2017.
 */

public class DashboardActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private String[] screenTitles;

    private SlidingRootNav slidingRootNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(0).setChecked(true),
                createItemFor(1),
                createItemFor(2),
                createItemFor(3),
                createItemFor(4),
                createItemFor(5),
                createItemFor(6),
                new SpaceItem(48),
                createItemFor(8)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(0);
    }

    @Override
    public void onItemSelected(int position) {
        if (position == 8) {
            finish();
        }
        else if(position == 2){
            Intent intent = new Intent(getApplicationContext(), AddListingP1.class);
            //intent.putExtra("role_intent",selected_role);
            //intent.putExtra("username_intent",username);
            //intent.putExtra("password_intent",password);
            //intent.putExtra("sname_intent",sc_name);
            startActivity(intent);
        }
        getSupportActionBar().setTitle(screenTitles[position]);
        slidingRootNav.closeMenu();
        //Fragment selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);
        //showFragment(selectedScreen);
    }

    private void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenTitles[position]);
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }


    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }
}
