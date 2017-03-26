package ru.alexandrkutashov.translatetestapp.view;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Window;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.alexandrkutashov.translatetestapp.R;
import ru.alexandrkutashov.translatetestapp.view.base.TabHolder;

public class MainActivity extends AppCompatActivity implements TabHolder {

    private SectionsPagerAdapter sectionsPagerAdapter;

    @BindView(R.id.container)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(sectionsPagerAdapter);

    }

    @Override
    public void moveToNextTab() {
        viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % sectionsPagerAdapter.getCount(), true);
    }

    class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return TranslationFragment.newInstance();
                default:
                    return DictionaryFragment.newInstance();
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
