package responsehandling.app.com.refrecdemo.xrecyclerview;

import android.view.View;

public interface CustomFooterViewCallBack {
    void onLoadingMore(View yourFooterView);
    void onLoadMoreComplete(View yourFooterView);
    void onSetNoMore(View yourFooterView,boolean noMore);
}
