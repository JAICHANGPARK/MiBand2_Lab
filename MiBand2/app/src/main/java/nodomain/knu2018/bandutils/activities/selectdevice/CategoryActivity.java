package nodomain.knu2018.bandutils.activities.selectdevice;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import nodomain.knu2018.bandutils.R;

public class CategoryActivity extends AppCompatActivity {

    private static final String TOP_FILTER_COLOR = "#BDBDBD";
    private static final String IMAGE_FILTER_COLOR = "#858585";

    @BindView(R.id.bottom_image)
    ImageView bottomImageView;

    @BindView(R.id.top_image)
    ImageView topImageView;
    @BindView(R.id.first_image)
    ImageView firstImageView;
    @BindView(R.id.category_first_text_view)
    TextView categoryFirstText;

    @BindView(R.id.category_second_text_view)
    TextView categorySecondText;
    @BindView(R.id.category_third_text_view)
    TextView categoryThirdText;
    @BindView(R.id.category_fourth_text_view)
    TextView categoryFourthText;

    @BindView(R.id.second_image)
    ImageView secondImageView;
    @BindView(R.id.forth_image)
    ImageView forthImageView;
    @BindView(R.id.third_image)
    ImageView thirdImageView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_category);

        setStatusBar();
        bindView();
        setToolbar();
        setImageColorFilter();
        setImageClickListener();
        //Glide.with(this).load(R.drawable.exo).into(bottomImageView);
    }

    /**
     * 뷰 바인딩
     */
    private void bindView() {
        ButterKnife.bind(this);
    }

    /**
     * 툴바 선택
     */
    private void setToolbar() {
        setSupportActionBar(toolbar);
    }

    /**
     * 상단 상태바의 색상을 변경하는 메소드입니다.
     *
     * @author 박제창
     */
    private void setStatusBar() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.blue_5));
        }
    }

    /**
     * 이미지 뷰에 사진을 넣는 메소드입니다.
     * 작동이 잘 되지 않아 삭제 예정입니다.
     *
     * @author 박제창
     */
    @Deprecated
    private void setImage() {
        Glide.with(this).load(R.drawable.exo).into(topImageView);
        Glide.with(this).load(R.drawable.category_one).into(firstImageView);
        Glide.with(this).load(R.drawable.category_two).into(secondImageView);
        Glide.with(this).load(R.drawable.category_third).into(thirdImageView);
        Glide.with(this).load(R.drawable.category_fourth).into(forthImageView);
        Glide.with(this).load(R.drawable.exo).into(bottomImageView);
    }

    /**
     * 사진에 색 필터를 덮는 메소드 입니다.
     *
     * @author 박제창
     */
    private void setImageColorFilter() {
        firstImageView.setColorFilter(Color.parseColor(IMAGE_FILTER_COLOR), PorterDuff.Mode.MULTIPLY);
        secondImageView.setColorFilter(Color.parseColor(IMAGE_FILTER_COLOR), PorterDuff.Mode.MULTIPLY);
        // TODO: 2018-06-19 3번째 약물 디바이스 사진은 기본 어두워서 필터 삭제 했어요 - 박제창
        //thirdImageView.setColorFilter(Color.parseColor(IMAGE_FILTER_COLOR), PorterDuff.Mode.MULTIPLY);
        forthImageView.setColorFilter(Color.parseColor(IMAGE_FILTER_COLOR), PorterDuff.Mode.MULTIPLY);
        topImageView.setColorFilter(Color.parseColor(TOP_FILTER_COLOR), PorterDuff.Mode.MULTIPLY);
        bottomImageView.setColorFilter(Color.parseColor(TOP_FILTER_COLOR), PorterDuff.Mode.MULTIPLY);
    }

    /**
     * 각 이미지에 해당하는 리스너를 정의합니다.
     *
     * @author 박제창
     */
    private void setImageClickListener() {
        firstImageView.setOnClickListener(v -> {
//                Pair[] pairs = new Pair[1];
//                pairs[0]  = new Pair<View, String>(firstImageView, "category_first");
            Intent sharedIntent = new Intent(CategoryActivity.this, FitnessDeviceActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CategoryActivity.this,
                        Pair.create(v, "category_first"),
                        Pair.create(categoryFirstText, "category_first_text"));
                startActivity(sharedIntent, options.toBundle());
                finish();
            } else {
                startActivity(sharedIntent);
                finish();
            }
        });

        secondImageView.setOnClickListener(v -> {

            Intent sharedIntent = new Intent(CategoryActivity.this, FoodDeviceActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CategoryActivity.this,
                        Pair.create(v, "category_second"),
                        Pair.create(categorySecondText, "category_second_text"));
                startActivity(sharedIntent, options.toBundle());
                finish();
            } else {
                startActivity(sharedIntent);
                finish();
            }

        });

        thirdImageView.setOnClickListener(v -> {

            Intent sharedIntent = new Intent(CategoryActivity.this, DrugDeviceActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CategoryActivity.this,
                        Pair.create(v, "category_third"),
                        Pair.create(categoryThirdText, "category_third_text"));
                startActivity(sharedIntent, options.toBundle());
                finish();
            } else {
                startActivity(sharedIntent);
                finish();
            }

        });

        forthImageView.setOnClickListener(v -> {

            Intent sharedIntent = new Intent(CategoryActivity.this, BSMDeviceActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CategoryActivity.this,
                        Pair.create(v, "category_fourth"),
                        Pair.create(categoryFourthText, "category_fourth_text"));
                startActivity(sharedIntent, options.toBundle());
                finish();
            } else {
                startActivity(sharedIntent);
                finish();
            }

        });

    }
}
