package com.property.activity.maintenance;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.property.ActivityForResult;
import com.property.activity.R;
import com.property.api.MaintenanceApi;
import com.property.base.BaseActivity;
import com.property.http.MyJsonDataResponseCacheHandler;
import com.property.http.MySimpleJsonDataResponseCacheHandler;
import com.property.model.SignModel;
import com.property.ui.codeScan.CaptureActivity;
import com.property.utils.BaiDuMapUtilInit;
import com.property.utils.ImageUploadUtils;
import com.vk.simpleutil.library.XSimpleAlertDialog;
import com.vk.simpleutil.library.XSimpleImage;
import com.vk.simpleutil.library.XSimplePhotoChoose;
import com.vk.simpleutil.library.XSimpleResources;
import com.vk.simpleutil.library.XSimpleText;
import com.vk.simpleutil.library.XSimpleToast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import butterknife.annotation.event.OnClick;

public class MaintenancePolicyActivity extends BaseActivity implements ImageUploadUtils.ImageUpLoadListener {
    Map<Integer, String> codeMap = new ArrayMap<>();
    List<String> ruleList = new ArrayList<>();
    SignModel signModel;
    @InjectView(R.id.iv_maintenancepolicy_upload)
    ImageView ivMaintenancepolicyUpload;
    @InjectView(R.id.iv_maintenancepolicy_bottom)
    ImageView ivMaintenancepolicyBottom;
    @InjectView(R.id.iv_maintenancepolicy_top)
    ImageView ivMaintenancepolicyTop;
    @InjectView(R.id.iv_maintenancepolicy_side)
    ImageView ivMaintenancepolicySide;
    @InjectView(R.id.ll_maintenancepolicy_criterion)
    RelativeLayout llMaintenancepolicyCriterion;
    @InjectView(R.id.iv_maintenancepolicy_paper)
    ImageView ivMaintenancepolicyPaper;
    @InjectView(R.id.ll_maintenancepolicy_paper)
    LinearLayout llMaintenancepolicyPaper;
    @InjectView(R.id.edt_maintenancepolicy_other)
    EditText edtOther;
    @InjectView(R.id.tv_maintenancepolicy_submit)
    TextView submit;
    Map<String, String> map = new ArrayMap<>();
    int codePostion = -1;
    String maintenanceID;
    List<String> ruleIDs;
    BDLocation locations;

    @Override
    public int onCreateViewLayouId() {
        return R.layout.maintenancepolicyactivity_main;
    }

    @Override
    public void initAllData() {
        maintenanceID = getIntent().getStringExtra("maintenanceID");
        if (!XSimpleText.isEmpty(maintenanceID)) {
            initMaintenanceData(maintenanceID);
            submit.setVisibility(View.GONE);
            edtOther.setFocusable(false);
            edtOther.setEnabled(false);
            ivMaintenancepolicyUpload.setVisibility(View.GONE);
        } else {
            signModel = getIntent().getParcelableExtra("signModel");
            initRuleData(signModel);
            ivMaintenancepolicyUpload.setVisibility(View.VISIBLE);
            BaiDuMapUtilInit.getInstance().startBaiDuMapReceiveLocation(new BaiDuMapUtilInit.LocationCallback() {
                @Override
                public void getLocation(BDLocation location) {
                    locations = location;
                }

                @Override
                public void getLocationFail() {

                }
            });
        }
    }

    void initRuleData(final SignModel signModel) {
        if (signModel.getType() == 1) {
            setTitle("电子维保单");
            llMaintenancepolicyPaper.setVisibility(View.GONE);
            llMaintenancepolicyCriterion.setVisibility(View.VISIBLE);
        } else if (signModel.getType() == 2) {
            setTitle("纸质维保单");
            llMaintenancepolicyPaper.setVisibility(View.VISIBLE);
            llMaintenancepolicyCriterion.setVisibility(View.GONE);
        }
        if (!XSimpleText.isEmpty(signModel.getImg1()))
            XSimpleImage.getInstance().displayImage(signModel.getImg1(), ivMaintenancepolicyTop);
        if (!XSimpleText.isEmpty(signModel.getImg2()))
            XSimpleImage.getInstance().displayImage(signModel.getImg2(), ivMaintenancepolicyBottom);
        if (!XSimpleText.isEmpty(signModel.getImg3()))
            XSimpleImage.getInstance().displayImage(signModel.getImg3(), ivMaintenancepolicySide);
        if (!XSimpleText.isEmpty(signModel.getImg4()))
            XSimpleImage.getInstance().displayImage(signModel.getImg4(), ivMaintenancepolicyPaper);

        edtOther.setText(signModel.getRemarks());
        if (!XSimpleText.isEmpty(maintenanceID)) {
            if (signModel.getW_rule() != null)
                llMaintenancepolicyCriterion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext, MaintenancePolicyRuleActivity.class).putExtra("rules"
                                , (Serializable) signModel.getW_rule()));
                    }
                });

        }
    }

    @Override
    public void initListener() {

    }

    void initMaintenanceData(String id) {
        showProgressDialog();
        MaintenanceApi.getInstance().getDeal(mContext, id, new MyJsonDataResponseCacheHandler<SignModel>(SignModel.class, false) {
            @Override
            public void onHttpComplete() {
                super.onHttpComplete();
                dismissProgressDialog();
            }

            @Override
            public void onJsonDataSuccess(SignModel object) {
                initRuleData(object);
            }

            @Override
            public boolean onJsonCacheData(boolean has) {
                return false;
            }
        });
    }

    @OnClick({R.id.iv_maintenancepolicy_bottom, R.id.iv_maintenancepolicy_top
            , R.id.iv_maintenancepolicy_side, R.id.iv_maintenancepolicy_paper, R.id.ll_maintenancepolicy_criterion, R.id.tv_maintenancepolicy_submit})
    void onClick(View v) {
        if (!XSimpleText.isEmpty(maintenanceID))
            return;
        switch (v.getId()) {
            case R.id.iv_maintenancepolicy_bottom:
                pohoto(0);
                break;
            case R.id.iv_maintenancepolicy_top:
                pohoto(1);
                break;
            case R.id.iv_maintenancepolicy_side:
                pohoto(2);
                break;
            case R.id.iv_maintenancepolicy_paper:
                pohoto(3);
                break;
            case R.id.ll_maintenancepolicy_criterion:
                startActivityForResult(new Intent(mContext, MaintenancePolicyRuleActivity.class).putExtra("ruleID"
                        , signModel.getNew_rule_id()), ActivityForResult.CRITERIONFORRESULT);
                break;
            case R.id.tv_maintenancepolicy_submit:
                submit();
                break;
        }
    }

    void pohoto(int codePostion) {
        this.codePostion = codePostion;
        XSimpleAlertDialog.simpleBaseDialog(mContext, null, "拍照", new XSimpleAlertDialog.Callback() {
            @Override
            public void getCallback() {
                XSimplePhotoChoose.open(mActivity, XSimplePhotoChoose.PhotoChooseType.PHOTO_GRAPH);
            }
        }, "相册", new XSimpleAlertDialog.Callback() {
            @Override
            public void getCallback() {
                XSimplePhotoChoose.open(mActivity, XSimplePhotoChoose.PhotoChooseType.PHOTO_ALBUM);
            }
        });
    }

    void submit() {
        map.clear();
        if (codeMap.containsKey(1))
            map.put("code[0]", codeMap.get(1));
        if (codeMap.containsKey(0))
            map.put("code[1]", codeMap.get(0));
        if (codeMap.containsKey(2))
            map.put("code[2]", codeMap.get(2));
        if (codeMap.containsKey(3))
            map.put("code[3]", codeMap.get(3));
        map.put("type", signModel.getType() + "");
        if (ruleIDs != null)
            for (int i = 0; i < ruleIDs.size(); i++)
                map.put("w_rule[" + i + "]", ruleIDs.get(i));
        map.put("remarks", edtOther.getText().toString());
        MaintenanceApi.getInstance().putMaintenance(mContext, signModel.getMaintenance_id(), map, new MySimpleJsonDataResponseCacheHandler(new MySimpleJsonDataResponseCacheHandler.OnJsonCallBack() {
            @Override
            public void success() {
                XSimpleToast.showToast("提交成功！");
                ActivityForResult.MaintenanceListRefresh = true;
                if (signModel.getType2() != 2) {
                    finish();
                } else {
                    CaptureActivity.launchActivity((Activity) mContext, ActivityForResult.POLICY_REQUEST_CODE_SCANLE);
                }
            }

            @Override
            public void fail() {

            }
        }));
    }

    void scanConfirm(String code) {
        MaintenanceApi.getInstance().scanConfirm(mContext, signModel.getMaintenance_id(), code, locations.getLatitude(), locations.getLongitude(),
                new MySimpleJsonDataResponseCacheHandler(new MySimpleJsonDataResponseCacheHandler.OnJsonCallBack() {
                    @Override
                    public void success() {
                        finish();
                    }

                    @Override
                    public void fail() {

                    }
                }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityForResult.CRITERIONFORRESULT) {
            if (resultCode == RESULT_OK && data != null) {
                ruleIDs = (List<String>) data.getSerializableExtra("ruleIDs");
            }
        } else if (requestCode == ActivityForResult.POLICY_REQUEST_CODE_SCANLE) {
            if (resultCode == RESULT_OK && data != null)
                scanConfirm(data.getStringExtra("code"));
        } else {
            String src = "file://" + XSimplePhotoChoose.onActivityResult(mActivity, requestCode, resultCode, data);
            if (XSimpleText.isEmpty(XSimplePhotoChoose.onActivityResult(mActivity, requestCode, resultCode, data)))
                return;
            ImageUploadUtils.getInstance().initImageCode(mContext, src, "", codePostion, this);
            if (codePostion == 0) {
                XSimpleImage.getInstance().displayImage(src, ivMaintenancepolicyBottom);
            } else if (codePostion == 1) {
                XSimpleImage.getInstance().displayImage(src, ivMaintenancepolicyTop);
            } else if (codePostion == 2) {
                XSimpleImage.getInstance().displayImage(src, ivMaintenancepolicySide);
            } else if (codePostion == 3) {
                XSimpleImage.getInstance().displayImage(src, ivMaintenancepolicyPaper);
            }
        }
    }

    @Override
    public void imageUploadSuccess(String base64, int tag) {
        codeMap.put(tag, base64);
    }

    @Override
    public void imageUploadFail(int statusFail, String error, int tag) {
        if (tag == 0) {
            XSimpleToast.showToast("底部图片上传失败！");
            ivMaintenancepolicyBottom.setImageDrawable(XSimpleResources.getDrawable(R.drawable.btn_photo));
        } else if (tag == 1) {
            XSimpleToast.showToast("顶部图片上传失败！");
            ivMaintenancepolicyTop.setImageDrawable(XSimpleResources.getDrawable(R.drawable.btn_photo));
        } else if (tag == 2) {
            XSimpleToast.showToast("侧面图片上传失败！");
            ivMaintenancepolicySide.setImageDrawable(XSimpleResources.getDrawable(R.drawable.btn_photo));
        } else if (tag == 3) {
            XSimpleToast.showToast("纸质维单图片上传失败！");
            ivMaintenancepolicyPaper.setImageDrawable(XSimpleResources.getDrawable(R.drawable.btn_photo));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageUploadUtils.getInstance().onDestroy();
    }
}
