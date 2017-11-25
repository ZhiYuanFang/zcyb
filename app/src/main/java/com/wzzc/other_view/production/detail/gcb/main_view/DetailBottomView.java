package com.wzzc.other_view.production.detail.gcb.main_view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.wzzc.base.BaseView;
import com.wzzc.base.Default;
import com.wzzc.base.annotation.ViewInject;
import com.wzzc.other_view.production.detail.gcb.DetailGcbActivity;
import com.wzzc.other_view.extendView.scrollView.UIScrollListener;
import com.wzzc.other_view.extendView.scrollView.UIScrollView;
import com.wzzc.other_activity.web.main_view.WebBrowser;
import com.wzzc.zcyb365.R;

/**
 * Created by zcyb365 on 2016/10/18.
 *
 */
public class DetailBottomView extends BaseView {

    @ViewInject(R.id.main_view)
    public UIScrollView main_view;
    @ViewInject(R.id.main_webview)
    public WebBrowser main_webview;
    @ViewInject(R.id.top_view)
    public RelativeLayout top_view;

    public DetailBottomView(Context context) {
        super(context);
    }

    public DetailBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void viewFirstLoad() {
        if (isInEditMode()) {
            return;
        }
        final int cheight = Default.dip2px(40, this.getContext());
        main_view.setScrollChange(new UIScrollListener() {
            @Override
            public void onScrollChanged(int top) {
            }

            @Override
            public boolean onScrollEnd(int top) {
                if (top < -cheight) {
                    RelativeLayout.LayoutParams params = (LayoutParams) top_view.getLayoutParams();
                    params.height = 0;
                    top_view.setLayoutParams(params);

                    DetailGcbActivity activity = (DetailGcbActivity) GetBaseActivity();
                    activity.RestoreView();
                    return true;
                }
                return false;
            }
        });
        main_webview.LoadURL("http://www.zcyb365.com/ecmobile/goods_desc.php?id="+GetBaseActivity().GetIntentData(DetailGcbActivity.GOODSID).toString());
    }
}
