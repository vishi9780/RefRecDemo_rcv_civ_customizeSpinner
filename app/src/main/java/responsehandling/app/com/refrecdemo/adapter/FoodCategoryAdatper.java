package responsehandling.app.com.refrecdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import responsehandling.app.com.refrecdemo.R;


public class FoodCategoryAdatper extends RecyclerView.Adapter<FoodCategoryAdatper.MyViewHolder> {
    private Context context;

    HashMap<String,String> hashMap=new HashMap<>();
    ArrayList<HashMap<String,String>> hashMapArrayList;

    public FoodCategoryAdatper(Context applicationContext, ArrayList<HashMap<String, String>> strings) {

        this.context=applicationContext;
        this.hashMapArrayList=strings;
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.adapter_category, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        hashMap=hashMapArrayList.get(position);

        holder.tv_name.setText(hashMap.get("name"));


    }


    @Override
    public long getItemId(int position) {
        return (position);

    }

    @Override
    public int getItemCount() {

        return hashMapArrayList==null ? 0 :hashMapArrayList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        public MyViewHolder(View view) {
            super(view);
            tv_name=(TextView)view.findViewById(R.id.tv_name);
            context = view.getContext();
        }


    }

}