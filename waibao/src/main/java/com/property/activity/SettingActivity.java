package com.property.activity;

import android.content.Intent;
import android.view.View;

import com.property.base.BaseActivity;
import com.property.utils.UserDataUtil;

import butterknife.annotation.event.OnClick;

public class SettingActivity extends BaseActivity {


    @OnClick(R.id.rl_setting_modify)
    void mpdify(View v) {
        startActivity(new Intent(mContext, SettingModifyActivity.class));
    }

    @OnClick(R.id.tv_setting_submit)
    void submit(View v) {
        UserDataUtil.getInstance().loginOut();
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public int onCreateViewLayouId() {
        return R.layout.setting_activity;
    }

    @Override
    public void initAllData() {
        setTitle("设置");
    }

    @Override
    public void initListener() {

    }

}
