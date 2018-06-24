package nodomain.knu2018.bandutils.activities.userprofile;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.activities.selectdrug.SelectDrugActivity;
import nodomain.knu2018.bandutils.fragments.profile.UserBaseInfoFragment;
import nodomain.knu2018.bandutils.fragments.profile.UserDeviceFragment;
import nodomain.knu2018.bandutils.fragments.profile.UserDrugFragment;

import static nodomain.knu2018.bandutils.Const.DataKeys.USER_NAME;
import static nodomain.knu2018.bandutils.Const.DataKeys.USER_UUID;
import static nodomain.knu2018.bandutils.Const.DataKeys.USER_UUID_V2;

/**
 * @author Dreamwaler(박제창)
 */

public class HomeProfileActivity extends AppCompatActivity {

    private static final String TAG = "HomeProfileActivity";

    @BindView(R.id.imageView)
    KenBurnsView imageView;
    @BindView(R.id.launchTwitterAnimation)
    ImageButton imageButton;
    @BindView(R.id.textView)
    TextView userNameTextView;
    @BindView(R.id.textView2)
    TextView userUUIDTextView;
    @BindView(R.id.tabLayout3)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.linearView)
    LinearLayout revealView;
    @BindView(R.id.layoutButtons)
    LinearLayout layoutButtons;

    @BindView(R.id.edit_info_button)
    Button editInfoButton;
    @BindView(R.id.edit_photo_button)
    Button editPhotoButton;
    @BindView(R.id.edit_drug_button)
    Button editDrugButton;

    Animation alphaAnimation;

    float pixelDensity;
    boolean flag = true;

    Map<String, String> userInfo = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_profile);
        ButterKnife.bind(this);
        Paper.init(this);

        userInfo.put(USER_NAME, Paper.book().read(USER_NAME));
        userInfo.put(USER_UUID, Paper.book().read(USER_UUID_V2));

        userNameTextView.setText(userInfo.get(USER_NAME));
        userUUIDTextView.setText(userInfo.get(USER_UUID));

        pixelDensity = getPixelDensity();
        // Adding the tabs using addTab() method
        setTabLayout();
        setViewPager();
        setImageFilter();

        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);

    }

    @OnClick(R.id.edit_info_button)
    public void onEditUserInfoButtonClicked(){
        Intent intent = new Intent(HomeProfileActivity.this, UserInformationActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.edit_drug_button)
    public void onEditUserDrugButtonClicked(){
        Intent intent = new Intent(HomeProfileActivity.this, SelectDrugActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 상단 상태바의 색상을 변경하는 메소드입니다.
     *
     * @author 박제창
     */
    private void setOpenStatusBar() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));
        }
    }

    /**
     * 상단 상태바의 색상을 변경하는 메소드입니다.
     *
     * @author 박제창
     */
    private void setCloseStatusBar() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.primary_custom_light));
        }
    }

    private float getPixelDensity() {
        float density = getResources().getDisplayMetrics().density;
        return density;
    }

    private void setImageFilter() {
        imageView.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);
    }

    private void setViewPager() {
        // Create an adapter that knows which fragment should be shown on each page
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);
        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setTabLayout() {

        tabLayout.addTab(tabLayout.newTab().setText("Section 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Section 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Section 3"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        TextView tv1 = (TextView) (((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(0)).getChildAt(1));
        tv1.setScaleY(-1);
        TextView tv2 = (TextView) (((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(1)).getChildAt(1));
        tv2.setScaleY(-1);
        TextView tv3 = (TextView) (((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(2)).getChildAt(1));
        tv3.setScaleY(-1);

    }

    public void launchTwitter(View view) {
        int x = imageView.getRight();
        int y = imageView.getBottom();

        x -= ((28 * pixelDensity) + (16 * pixelDensity));

        int hypotenuse = (int) Math.hypot(imageView.getWidth(), imageView.getHeight());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (flag) {
                setOpenStatusBar();
                imageButton.setBackgroundResource(R.drawable.rounded_cancel_button);
                imageButton.setImageResource(R.drawable.ic_clear_black_24dp);

                FrameLayout.LayoutParams parameters = (FrameLayout.LayoutParams) revealView.getLayoutParams();
                parameters.height = imageView.getHeight();
                revealView.setLayoutParams(parameters);

                Animator anim = null;
                anim = ViewAnimationUtils.createCircularReveal(revealView, x, y, 0, hypotenuse);

                anim.setDuration(700);

                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        userNameTextView.setVisibility(View.GONE);
                        userUUIDTextView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                        layoutButtons.setVisibility(View.VISIBLE);
                        layoutButtons.startAnimation(alphaAnimation);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

                revealView.setVisibility(View.VISIBLE);
                anim.start();

                flag = false;

            } else {
                setCloseStatusBar();
                imageButton.setBackgroundResource(R.drawable.rounded_button);
                imageButton.setImageResource(R.drawable.ic_eject_black_24dp);

                Animator anim = ViewAnimationUtils.createCircularReveal(revealView, x, y, hypotenuse, 0);
                anim.setDuration(400);

                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                        userNameTextView.setVisibility(View.VISIBLE);
                        userUUIDTextView.setVisibility(View.VISIBLE);
                        revealView.setVisibility(View.GONE);
                        layoutButtons.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

                anim.start();
                flag = true;
            }
        } else {

            // TODO: 2018-06-22 롤리팝 이하의 버전에서의 처리는 여기서 합니다. - 박제창 (Dreamwalker)

            if (flag) {

                imageButton.setBackgroundResource(R.drawable.rounded_cancel_button);
                imageButton.setImageResource(R.drawable.ic_clear_black_24dp);

                FrameLayout.LayoutParams parameters = (FrameLayout.LayoutParams) revealView.getLayoutParams();
                parameters.height = imageView.getHeight();
                revealView.setLayoutParams(parameters);

                userNameTextView.setVisibility(View.GONE);
                userUUIDTextView.setVisibility(View.GONE);
                layoutButtons.setVisibility(View.VISIBLE);
                revealView.setVisibility(View.VISIBLE);

                flag = false;

            } else {

                imageButton.setBackgroundResource(R.drawable.rounded_button);
                imageButton.setImageResource(R.drawable.ic_eject_black_24dp);


                userNameTextView.setVisibility(View.VISIBLE);
                userUUIDTextView.setVisibility(View.VISIBLE);
                revealView.setVisibility(View.GONE);
                layoutButtons.setVisibility(View.GONE);


                flag = true;
            }

        }

    }


    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!flag) {
                setCloseStatusBar();
                int x = imageView.getRight();
                int y = imageView.getBottom();

                x -= ((28 * pixelDensity) + (16 * pixelDensity));

                int hypotenuse = (int) Math.hypot(imageView.getWidth(), imageView.getHeight());

                imageButton.setBackgroundResource(R.drawable.rounded_button);
                imageButton.setImageResource(R.drawable.ic_eject_black_24dp);

                Animator anim = ViewAnimationUtils.createCircularReveal(revealView, x, y, hypotenuse, 0);
                anim.setDuration(400);

                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                        userNameTextView.setVisibility(View.VISIBLE);
                        userUUIDTextView.setVisibility(View.VISIBLE);

                        revealView.setVisibility(View.GONE);
                        layoutButtons.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

                anim.start();
                flag = true;

            } else {
                super.onBackPressed();
            }
        } else {

            if (!flag) {

                int x = imageView.getRight();
                int y = imageView.getBottom();

                x -= ((28 * pixelDensity) + (16 * pixelDensity));

                int hypotenuse = (int) Math.hypot(imageView.getWidth(), imageView.getHeight());

                imageButton.setBackgroundResource(R.drawable.rounded_button);
                imageButton.setImageResource(R.drawable.ic_eject_black_24dp);

                userNameTextView.setVisibility(View.VISIBLE);
                userUUIDTextView.setVisibility(View.VISIBLE);

                revealView.setVisibility(View.GONE);
                layoutButtons.setVisibility(View.GONE);

                flag = true;

            } else {
                super.onBackPressed();
            }

        }

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//            TabLayout.Tab tab = tabLayout.getTabAt(position);
//            tab.setCustomView(R.layout.layout_tab_view);
            switch (position) {
                case 0:
                    return new UserBaseInfoFragment();
                case 1:
                    return new UserDrugFragment();
                default:
                    return new UserDeviceFragment();

            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "내정보";
                case 1:
                    return "투약정보";
                default:
                    return "액세서리";
            }
            //return super.getPageTitle(position);
        }
    }
}
