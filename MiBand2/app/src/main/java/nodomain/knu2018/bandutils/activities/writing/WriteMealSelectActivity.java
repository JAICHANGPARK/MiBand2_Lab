package nodomain.knu2018.bandutils.activities.writing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.mingle.entity.MenuEntity;
import com.mingle.sweetpick.BlurEffect;
import com.mingle.sweetpick.RecyclerViewDelegate;
import com.mingle.sweetpick.SweetSheet;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;
import nodomain.knu2018.bandutils.R;
import nodomain.knu2018.bandutils.activities.CameraActivity;

/**
 * The type Write meal select activity.
 */
public class WriteMealSelectActivity extends AppCompatActivity {

    private static final String TAG = "WriteMealSelectActivity";

    private static final int REQUEST_PERMISSION = 1000;
    private static final int PICK_FILE_REQUEST = 1001;

    /**
     * The Btn before breakfast.
     */
    @BindView(R.id.button_before_breakfast)
    Button btnBeforeBreakfast;
    /**
     * The Btn after breakfast.
     */
    @BindView(R.id.button_after_breakfast)
    Button btnAfterBreakfast;
    /**
     * The Btn before lunch.
     */
    @BindView(R.id.button_before_lunch)
    Button btnBeforeLunch;
    /**
     * The Btn after lunch.
     */
    @BindView(R.id.button_after_lunch)
    Button btnAfterLunch;
    /**
     * The Btn before dinner.
     */
    @BindView(R.id.button_before_dinner)
    Button btnBeforeDinner;
    /**
     * The Btn after dinner.
     */
    @BindView(R.id.button_after_dinner)
    Button btnAfterDinner;

    /**
     * The Btn snack.
     */
    @BindView(R.id.button_snack)
    Button btnSnack;


    /**
     * The Rl.
     */
    @BindView(R.id.r1)
    RelativeLayout rl;

    /**
     * The Type.
     */
    String[] type = new String[]{"아침식사전", "아침식사후", "점심식사전", "점심식사후", "저녁식사전", "저녁식사후", "간식"};

    private SweetSheet mSweetSheet;

    /**
     * The Uri.
     */
    Uri uri;
    /**
     * The Compressed image.
     */
    File compressedImage;
    /**
     * The Tmp file.
     */
    File tmpFile;
    /**
     * The Path.
     */
    File path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_meal_select);
        ButterKnife.bind(this);
        setupRecyclerView();

        Paper.init(this);


    }


    /**
     * On click before breakfast.
     */
    @OnClick(R.id.button_before_breakfast)
    public void onClickBeforeBreakfast() {

        Paper.book().write("mealType", type[0]);
        mSweetSheet.toggle();

    }

    /**
     * On click after breakfast.
     */
    @OnClick(R.id.button_after_breakfast)
    public void onClickAfterBreakfast() {
        Paper.book().write("mealType", type[1]);
        mSweetSheet.toggle();

    }

    /**
     * On click before lunch.
     */
    @OnClick(R.id.button_before_lunch)
    public void onClickBeforeLunch() {
        Paper.book().write("mealType", type[2]);
        mSweetSheet.toggle();

    }

    /**
     * On click after lunch.
     */
    @OnClick(R.id.button_after_lunch)
    public void onClickAfterLunch() {
        Paper.book().write("mealType", type[3]);
        mSweetSheet.toggle();

    }

    /**
     * On click before dinner.
     */
    @OnClick(R.id.button_before_dinner)
    public void onClickBeforeDinner() {
        Paper.book().write("mealType", type[4]);
        mSweetSheet.toggle();

    }

    /**
     * On click after dinner.
     */
    @OnClick(R.id.button_after_dinner)
    public void onClickAfterDinner() {
        Paper.book().write("mealType", type[5]);
        mSweetSheet.toggle();

    }

    @Override
    public void onBackPressed() {


        if (mSweetSheet.isShow()) {
            mSweetSheet.dismiss();
        } else {
            super.onBackPressed();
        }

    }

    private void setupRecyclerView() {

        final ArrayList<MenuEntity> list = new ArrayList<>();
        //添加假数据
        MenuEntity menuEntity = new MenuEntity();
        menuEntity.iconId = R.drawable.selfie_1f933;
        menuEntity.title = "Take a Photo";

        MenuEntity menuEntity1 = new MenuEntity();
        menuEntity1.iconId = R.drawable.camera_1f4f7;
        menuEntity1.title = "Choose Gallery";
        list.add(menuEntity);
        list.add(menuEntity1);

        // SweetSheet 控件,根据 rl 确认位置
        mSweetSheet = new SweetSheet(rl);

        //设置数据源 (数据源支持设置 list 数组,也支持从菜单中获取)
        mSweetSheet.setMenuList(list);
        //根据设置不同的 Delegate 来显示不同的风格.
        mSweetSheet.setDelegate(new RecyclerViewDelegate(true));
        //根据设置不同Effect 来显示背景效果BlurEffect:模糊效果.DimEffect 变暗效果
        mSweetSheet.setBackgroundEffect(new BlurEffect(14));
        //设置点击事件
        mSweetSheet.setOnMenuItemClickListener(new SweetSheet.OnMenuItemClickListener() {
            @Override
            public boolean onItemClick(int position, MenuEntity menuEntity1) {
                //即时改变当前项的颜色
                //((RecyclerViewDelegate) mSweetSheet.getClass()).notifyDataSetChanged();
                //根据返回值, true 会关闭 SweetSheet ,false 则不会.
                //Toast.makeText(WriteMealSelectActivity.this, menuEntity1.title + "  " + position, Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        startActivity(new Intent(WriteMealSelectActivity.this, CameraActivity.class));
                        break;
                    case 1:
                        chooseFile();
                        break;
                    default:
                        return false;

                }
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data != null) {

                    uri = data.getData();

                    //path = FileUtils.getPath(this, uri);
                    path = FileUtils.getFile(this, uri);
                    tmpFile = FileUtils.getFile(this, uri);
                    Log.e(TAG, "onActivityResult: path " + path);
                    Log.e(TAG, "onActivityResult: uri " + uri);
                    Log.e(TAG, "onActivityResult: getPath " + uri.getPath());
                    Log.e(TAG, "onActivityResult: getData " + data.getData());
                    if (uri != null && !uri.getPath().isEmpty()) {
                        // TODO: 2018-05-19 이미지 셋팅과 압축

                        Intent intent = new Intent(WriteMealSelectActivity.this, WriteMealPhotoActivity.class);
                        intent.putExtra("uri", uri.toString());
                        intent.putExtra("path", path.toString());
                        startActivity(intent);
                        finish();
                    }
                    //imageView.setImageURI(uri);
//                        new Compressor(this)
//                                .compressToFileAsFlowable(path)
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(new Consumer<File>() {
//                                    @Override
//                                    public void accept(File file) {
//                                        compressedImage = file;
//                                        setCompressedImage();
//                                        Log.e(TAG, "accept:  called");
//                                    }
//                                }, new Consumer<Throwable>() {
//                                    @Override
//                                    public void accept(Throwable throwable) {
//                                        throwable.printStackTrace();
//                                        //showError(throwable.getMessage());
//                                    }
//                                });

                }
            }


        }
    }


    private void chooseFile() {

        Intent getContentIntent = Intent.createChooser(FileUtils.createGetContentIntent(), "select file");
        startActivityForResult(getContentIntent, PICK_FILE_REQUEST);

    }
}
