package com.property.activity;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.property.activity.fault.FaultActivity;
import com.property.activity.maintenance.MaintenancePlanActivity;
import com.property.api.UserApi;
import com.property.base.BaseActivity;
import com.property.http.MyJsonDataResponseCacheHandler;
import com.property.model.MessageCountModel;
import com.property.model.MessageModel;
import com.property.utils.UserDataUtil;

import butterknife.InjectView;
import butterknife.annotation.event.OnClick;

public class IndexActivity extends BaseActivity {

    @InjectView(R.id.tv_index_username)
    TextView tvIndexUsername;
    @InjectView(R.id.tv_index_desc)
    TextView tvIndexDesc;
    @InjectView(R.id.iv_index_message)
    ImageView ivMessage;


    @Override
    public int onCreateViewLayouId() {
        return R.layout.index_activity;
    }

    @Override
    public void initAllData() {
        tvIndexUsername.setText(UserDataUtil.getInstance().getUserData().getName());
        tvIndexDesc.setText(UserDataUtil.getInstance().getUserData().getDepartment());

    }

    @OnClick({R.id.iv_index_settring, R.id.iv_index_message, R.id.iv_index_maintenance, R.id.iv_index_repair, R.id.iv_index_statistics, R.id.iv_index_user})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_index_settring:
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.iv_index_message:
//                MessagePopupWindow messagePopupWindow = new MessagePopupWindow(mContext);
//                messagePopupWindow.showAtLocation(ivMessage, Gravity.BOTTOM,0,0);
//                startActivity(new Intent(mContext, MessageActivity.class).putExtra("type", MessageType.all));
                break;
            case R.id.iv_index_maintenance:
                startActivity(new Intent(mContext, MaintenancePlanActivity.class));
                break;
            case R.id.iv_index_repair:
                startActivity(new Intent(mContext, FaultActivity.class));
                break;
            case R.id.iv_index_statistics:
                startActivity(new Intent(mContext, StatisticsActivity.class));
                break;
            case R.id.iv_index_user:
                startActivity(new Intent(mContext, UserActivity.class));
                break;
            default:
                break;
        }
    }

    void initMessageView(MessageCountModel messageCountModel) {

    }

    void getMessage() {
        UserApi.getInstance().getMessages(mContext, new MyJsonDataResponseCacheHandler<MessageCountModel>(MessageModel.class, false) {
            @Override
            public void onJsonDataSuccess(MessageCountModel object) {
                initMessageView(object);
            }

            @Override
            public boolean onJsonCacheData(boolean has) {
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMessage();
    }

    @Override
    public void initListener() {

    }
}
