package responsehandling.app.com.refrecdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import responsehandling.app.com.civ.BitmapDrawer;
import responsehandling.app.com.civ.CircularDrawable;
import responsehandling.app.com.civ.IconDrawer;
import responsehandling.app.com.civ.OverlayArcDrawer;
import responsehandling.app.com.civ.TextDrawer;
import responsehandling.app.com.civ.notification.CircularNotificationDrawer;
import responsehandling.app.com.civ.notification.RectangularNotificationDrawer;
import responsehandling.app.com.customizespinner.AutoLabelUI;
import responsehandling.app.com.customizespinner.AutoLabelUISettings;
import responsehandling.app.com.refrecdemo.adapter.FoodCategoryAdatper;
import responsehandling.app.com.refrecdemo.retrofit.ApiClient;
import responsehandling.app.com.refrecdemo.retrofit.ApiInterface;
import responsehandling.app.com.refrecdemo.xrecyclerview.ProgressStyle;
import responsehandling.app.com.refrecdemo.xrecyclerview.XRecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private XRecyclerView rv_CompletedProjects;
    public static final String TAG=MainActivity.class.getSimpleName();
    String name="";
    FoodCategoryAdatper foodCategoryAdatper;
    ArrayList<HashMap<String,String >> arrayList=new ArrayList<>();
    private int count=0;

    //Spinner Custom
    private AutoLabelUI label_Sector;
    List<String> categories = new ArrayList<String>();
    private Spinner spinnerSector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findIDs();
    }

    private void findIDs() {
        rv_CompletedProjects=(XRecyclerView)findViewById(R.id.rv_CompletedProjects);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_CompletedProjects.setLayoutManager(layoutManager);
        rv_CompletedProjects.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        rv_CompletedProjects.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        rv_CompletedProjects.setPullRefreshEnabled(true);
        rv_CompletedProjects.setItemAnimator(new DefaultItemAnimator());
        findViewById(R.id.btn_CompletedProjects).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*hitTheAPI("1");
                count=1;*/
            }
        });
        rv_CompletedProjects.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
              /*  count++;
                Log.e(TAG, "onRefresh: "+count );
                hitTheAPI(String.valueOf(count));*/
            }

            @Override
            public void onLoadMore() {
               /* count++;
                Log.e(TAG, "onLoadMore: "+count );
                hitTheAPI(String.valueOf(count));*/
            }
        });
        label_Sector = (AutoLabelUI) findViewById(R.id.label_Sector);
        spinnerSector = (Spinner) findViewById(R.id.spinnerSector);
        categories.add("(*)(*)");
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");
        spinnerSector.setOnItemSelectedListener(this);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSector.setAdapter(dataAdapter);

        // attaching data adapter to spinner
        AutoLabelUISettings autoLabelUISettings = new AutoLabelUISettings.Builder()
                .withMaxLabels(100)
                .withBackgroundResource(R.drawable.label_background)
                .withLabelsClickables(false)
                .withShowCross(true)
                .withIconCross(R.drawable.ic_close_black_24dp)
                .withTextColor(android.R.color.white)
                .withTextSize(R.dimen.label_title_size)
                .withLabelPadding(R.dimen.five_dp)
                .build();
        label_Sector.setSettings(autoLabelUISettings);

        //CircularImageView
        new ImageLoader().execute();
    }


    private void hitTheAPI(final String perpage) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = apiService.getTopRatedMovies(perpage);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {


                try {
//                    arrayList.clear();
                    JSONArray object = new JSONArray(new Gson().toJson(response.body()));
                    Log.e("TAG", "onResponse: " + object);
                    for (int i = 0; i < object.length(); i++) {
                        JSONObject jsonObject=object.optJSONObject(i);
                        HashMap<String,String> hashMap=new HashMap<>();
                        hashMap.put("name",jsonObject.optString("name"));
                        arrayList.add(hashMap);
                    }
                    foodCategoryAdatper=new FoodCategoryAdatper(MainActivity.this,arrayList);
                    rv_CompletedProjects.setAdapter(foodCategoryAdatper);
                    foodCategoryAdatper.notifyDataSetChanged();
                    if(Integer.parseInt(perpage)!=1){
                        rv_CompletedProjects.refreshComplete();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(TAG, "onFailure: ",t );
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        if (position>0) {
            label_Sector.removeLabel(categories.get(position));
            label_Sector.addLabel(categories.get(position));
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    //Circle Image View

    private class ImageLoader extends AsyncTask<Void, Void, Void> {
        private CircularDrawable circularDrawable1;
        private CircularDrawable circularDrawable2;
        private CircularDrawable circularDrawable3;
        private CircularDrawable circularDrawable4;
        private CircularDrawable circularDrawable5;
        private CircularDrawable circularDrawable6;
        private CircularDrawable circularDrawable7;
        private CircularDrawable circularDrawable8;
        private CircularDrawable circularDrawable9;
        private CircularDrawable circularDrawable10;
        private CircularDrawable circularDrawable11;
        private ProgressDialog loadingDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = ProgressDialog.show(MainActivity.this, null, "Loading");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.jennifer_aniston);
            Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.katrina_kaif);
            Bitmap shopFrontBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.seller_icon);
            Bitmap badgeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.flipkart_round);
            Log.v(MainActivity.class.getName(), "Height:" + bitmap1.getHeight() + " Width:" + bitmap1.getWidth());

            //Test Image 1
            circularDrawable1 = new CircularDrawable();
            BitmapDrawer bitmapDrawer = new BitmapDrawer();
            bitmapDrawer.bitmap = bitmap1;
            bitmapDrawer.scaleType = ImageView.ScaleType.CENTER_CROP;
            circularDrawable1.setBitmapOrTextOrIcon(bitmapDrawer);
            circularDrawable1.setNotificationDrawer(new CircularNotificationDrawer().setNotificationSize(36, 52).setNotificationText("5")
                    .setNotificationAngle(45).setNotificationColor(Color.BLACK, Color.parseColor("#FDC301")));
            circularDrawable1.setBadge(badgeIcon);
//            circularDrawable1.setNotificationSize(50, 50, 50, 3);
            circularDrawable1.setBorder(Color.TRANSPARENT, 12);

            //Test Image 2
            circularDrawable2 = new CircularDrawable();
            BitmapDrawer bitmapDrawer2 = new BitmapDrawer();
            bitmapDrawer2.bitmap = bitmap2;
            bitmapDrawer2.scaleType = ImageView.ScaleType.FIT_CENTER;
            circularDrawable2.setBitmapOrTextOrIcon(bitmapDrawer2);
            circularDrawable2.setNotificationDrawer(new RectangularNotificationDrawer().setNotificationText("5").setNotificationAngle(45));
//          circularDrawable2.setNotificationStyle(CircularDrawable.NotificationStyle.Circle);
//          circularDrawable2.setNotificationColor(Color.BLACK, Color.parseColor("#FDC301"));
            circularDrawable2.setBadge(badgeIcon);
            circularDrawable2.setBorder(Color.BLACK, 4);

            //Test Image 10
            circularDrawable10 = new CircularDrawable();
            circularDrawable10.setBitmapOrTextOrIcon(bitmap2);
            circularDrawable10.setNotificationDrawer(new RectangularNotificationDrawer().setNotificationText("5").setNotificationAngle(45));
//            circularDrawable10.setBadge(badgeIcon);
            circularDrawable10.setBorder(Color.BLACK, 4);
            circularDrawable10.setOverlayArcDrawer(new OverlayArcDrawer(Color.parseColor("#BB333333"), 30, 120));

            //Test Image 3
            circularDrawable3 = new CircularDrawable();
            TextDrawer vsTextDrawer = new TextDrawer().setText("VS").setBackgroundColor(Color.BLUE).setTextColor(Color.WHITE);
            circularDrawable3.setBitmapOrTextOrIcon(vsTextDrawer);
            circularDrawable3.setOverlayArcDrawer(new OverlayArcDrawer(shopFrontBitmap, 210, 120));
            circularDrawable3.setBadge(badgeIcon);
//          circularDrawable3.setBorder(Color.BLACK, 4);

            //Test Image 11
            circularDrawable11 = new CircularDrawable();
            TextDrawer vsTextDrawer1 = new TextDrawer().setText("VS").setBackgroundColor(Color.BLUE).setTextColor(Color.WHITE);
            circularDrawable11.setBitmapOrTextOrIcon(vsTextDrawer1);
            circularDrawable11.setBadge(badgeIcon);
            circularDrawable11.setOverlayArcDrawer(new OverlayArcDrawer(shopFrontBitmap, 210, 120));
//          circularDrawable3.setBorder(Color.BLACK, 4);

            //Test Image 4
            circularDrawable4 = new CircularDrawable();
            circularDrawable4.setBitmapOrTextOrIcon(bitmap1, bitmap2);
            circularDrawable4.setNotificationDrawer(new CircularNotificationDrawer().setNotificationText("51").setNotificationAngle(135).setNotificationColor
                    (Color.WHITE, Color.RED));
            circularDrawable4.setDivider(4, Color.WHITE);
//          circularDrawable4.setBadge(badgeIcon);
//          circularDrawable4.setBorder(Color.BLACK, 4);

            //Test Image 5
            circularDrawable5 = new CircularDrawable();
            circularDrawable5.setBitmapOrTextOrIcon(vsTextDrawer, bitmap2, bitmap1);
            circularDrawable5.setDivider(4, Color.WHITE);
//          circularDrawable5.setBadge(badgeIcon);
            circularDrawable5.setBorder(Color.MAGENTA, 8);

            //Test Image 6
            circularDrawable6 = new CircularDrawable();
            TextDrawer abTextDrawer = new TextDrawer().setText("AB").setBackgroundColor(Color.RED).setTextColor(Color.WHITE);
            circularDrawable6.setBitmapOrTextOrIcon(vsTextDrawer, bitmap2, bitmap1, abTextDrawer);
            circularDrawable6.setDivider(4, Color.WHITE);
//          circularDrawable6.setNotificationText("5");
//          circularDrawable6.setNotificationAngle(135);
//          circularDrawable6.setNotificationStyle(CircularDrawable.NotificationStyle.Circle);
//          circularDrawable6.setNotificationColor(Color.WHITE, Color.RED);
//          circularDrawable6.setBadge(badgeIcon);
//          circularDrawable6.setBorder(Color.BLUE, 8);

            circularDrawable7 = new CircularDrawable();
            IconDrawer iconDrawer = new IconDrawer(BitmapFactory.decodeResource(getResources(), R.drawable.profile_placeholder), Color.BLACK).setMargin(pxFromDp(15));
            circularDrawable7.setBitmapOrTextOrIcon(iconDrawer);

            circularDrawable8 = new CircularDrawable();
            iconDrawer = new IconDrawer(BitmapFactory.decodeResource(getResources(), R.drawable.profile_placeholder), Color.BLUE).setMargin(pxFromDp(6));
            circularDrawable8.setBitmapOrTextOrIcon(bitmap1, iconDrawer);
            circularDrawable8.setDivider(4, Color.WHITE);

            circularDrawable9 = new CircularDrawable();
            iconDrawer = new IconDrawer(BitmapFactory.decodeResource(getResources(), R.drawable.profile_placeholder), Color.BLUE).setMargin(pxFromDp(4));
            circularDrawable9.setBitmapOrTextOrIcon(iconDrawer, iconDrawer, iconDrawer, iconDrawer);
            circularDrawable9.setDivider(4, Color.WHITE);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            loadingDialog.dismiss();

            ImageView testIcon = (ImageView) findViewById(R.id.iv_test_icon_1);
            testIcon.setImageDrawable(circularDrawable1);

            testIcon = (ImageView) findViewById(R.id.iv_test_icon_2);
            testIcon.setImageDrawable(circularDrawable2);

            testIcon = (ImageView) findViewById(R.id.iv_test_icon_3);
            testIcon.setImageDrawable(circularDrawable3);

            testIcon = (ImageView) findViewById(R.id.iv_test_icon_4);
            testIcon.setImageDrawable(circularDrawable4);

            testIcon = (ImageView) findViewById(R.id.iv_test_icon_5);
            testIcon.setImageDrawable(circularDrawable5);

            testIcon = (ImageView) findViewById(R.id.iv_test_icon_6);
            testIcon.setImageDrawable(circularDrawable6);

            testIcon = (ImageView) findViewById(R.id.iv_test_icon_7);
            testIcon.setImageDrawable(circularDrawable7);

            testIcon = (ImageView) findViewById(R.id.iv_test_icon_8);
            testIcon.setImageDrawable(circularDrawable8);

            testIcon = (ImageView) findViewById(R.id.iv_test_icon_9);
            testIcon.setImageDrawable(circularDrawable9);

            testIcon = (ImageView) findViewById(R.id.iv_test_icon_10);
            testIcon.setImageDrawable(circularDrawable10);

            testIcon = (ImageView) findViewById(R.id.iv_test_icon_11);
            testIcon.setImageDrawable(shadowify(circularDrawable11));
        }
    }

    private float pxFromDp(int dpValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

    private Drawable shadowify(Drawable drawable) {
        Drawable shadow = getResources().getDrawable(R.drawable.chat_bubble_shadow);
        InsetDrawable insetDrawable = new InsetDrawable(drawable, (int) dpToPx(20));
        return new LayerDrawable(new Drawable[]{shadow, insetDrawable});
    }

    private float dpToPx(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
