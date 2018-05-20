package nodomain.knu2018.bandutils.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;
import nodomain.knu2018.bandutils.R;

public class WriteMealPhotoActivity extends AppCompatActivity {

    private static final String TAG = "WriteMealPhotoActivity";

    @BindView(R.id.imageView)
    ImageView imageView;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_meal_photo);
        ButterKnife.bind(this);
        Paper.init(this);

        String getString = getIntent().getStringExtra("uri");
        Uri uri = Uri.parse(getString);
        Glide.with(this).load(uri).into(imageView);

        type = Paper.book().read("mealType");

        Bitmap bitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("path"));
        Log.e(TAG, "onCreate: "  + bitmap.getWidth() +  ", " + bitmap.getHeight());
        Log.e(TAG, "onCreate: " + type );
    }


}
