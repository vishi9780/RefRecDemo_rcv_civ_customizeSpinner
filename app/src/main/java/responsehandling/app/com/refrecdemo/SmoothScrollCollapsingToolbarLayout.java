package responsehandling.app.com.refrecdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import responsehandling.app.com.refrecdemo.adapter.FoodCategoryAdatper;

public class SmoothScrollCollapsingToolbarLayout extends AppCompatActivity {
    RecyclerView recycler_view_target;
    FoodCategoryAdatper foodCategoryAdatper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_smooth_scroll_parallax);
        recycler_view_target=(RecyclerView)findViewById(R.id.recycler_view_target);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_target.setLayoutManager(mLayoutManager);
        recycler_view_target.setItemAnimator(new DefaultItemAnimator());
        ArrayList<HashMap<String,String>> hashMapArrayList=new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            HashMap<String,String>  hashMap=new HashMap<>();
            hashMap.put("name","name"+i);
            hashMapArrayList.add(hashMap);
        }
        foodCategoryAdatper=new FoodCategoryAdatper(getApplicationContext(),hashMapArrayList);
        recycler_view_target.setAdapter(foodCategoryAdatper);*/
    }
}