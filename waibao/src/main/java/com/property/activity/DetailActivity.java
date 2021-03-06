package com.property.activity;


import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.property.activity.fault.FaultDetailEditCompleteActivity;
import com.property.activity.maintenance.MaintenancePolicyActivity;
import com.property.api.FaultApi;
import com.property.api.MaintenanceApi;
import com.property.base.BaseActivity;
import com.property.enumbase.MessageType;
import com.property.http.MyJsonDataResponseCacheHandler;
import com.property.http.MySimpleJsonDataResponseCacheHandler;
import com.property.model.LiftModel;
import com.property.model.SignModel;
import com.property.utils.BaiDuMapUtilInit;
import com.vk.simpleutil.library.XSimpleTime;

import butterknife.InjectView;
import butterknife.annotation.event.OnClick;

public class DetailActivity extends BaseActivity {

    @InjectView(R.id.edt_detail_id)
    TextView edtDetailId;
    @InjectView(R.id.edt_detail_address)
    TextView edtDetailAddress;
    @InjectView(R.id.edt_detail_brand)
    TextView edtDetailBrand;
    @InjectView(R.id.edt_detail_property_company)
    TextView edtDetailPropertyCompany;
    @InjectView(R.id.edt_detail_maintenance_company)
    TextView edtDetailMaintenanceCompany;
    @InjectView(R.id.edt_detail_phone)
    TextView edtDetailPhone;
    @InjectView(R.id.edt_detail_lasttime)
    TextView edtDetailLasttime;
    @InjectView(R.id.edt_detail_thistime)
    TextView edtDetailThistime;
    @InjectView(R.id.tv_detail_reason)
    TextView tvReason;
    @InjectView(R.id.tv_detail_reasontitle)
    TextView tvTitleReason;

    @InjectView(R.id.tv_detail_submit)
    TextView submit;
    @InjectView(R.id.rl_detail_thistime)
    RelativeLayout rlThistime;
    @InjectView(R.id.rl_detail_lasttime)
    RelativeLayout rlLasttime;
    MessageType messageType;
    String id;
    LiftModel liftModel;
    BDLocation bdLocation;

    @Override
    public int onCreateViewLayouId() {
        return R.layout.detail_activity;
    }

    @Override
    public void initAllData() {
        setTitle("电梯信息");
        submit.setClickable(false);
        id = getIntent().getStringExtra("id");
        messageType = (MessageType) getIntent().getSerializableExtra("messageType");
        getScan(id, getIntent().getStringExtra("code"));
        BaiDuMapUtilInit.getInstance().startBaiDuMapReceiveLocation(new BaiDuMapUtilInit.LocationCallback() {
            @Override
            public void getLocation(BDLocation location) {
                bdLocation = location;
            }

            @Override
            public void getLocationFail() {

            }
        });
    }

    void initData(LiftModel liftModel) {
        if (liftModel == null)
            return;
        this.liftModel = liftModel;
        edtDetailId.setText(liftModel.getLift_code());
        edtDetailAddress.setText(liftModel.getLift_address());
        edtDetailBrand.setText(liftModel.getLift_brand());
        edtDetailPropertyCompany.setText(liftModel.getCustomer());
        edtDetailMaintenanceCompany.setText(liftModel.getCompany());
        edtDetailPhone.setText(liftModel.getPhone());
        edtDetailLasttime.setText(XSimpleTime.getFormatTimeFromTimestamp((long) liftModel.getLast_time(), "yyyy-MM-dd"));
        if (liftModel.getLast_time() == 0 && messageType == MessageType.maintenance)
            edtDetailLasttime.setText("未有过维保");
        edtDetailThistime.setText(XSimpleTime.getFormatTimeFromTimestamp((long) liftModel.getStart_time(), "yyyy-MM-dd"));
        if (liftModel.getStart_time() == 0 && messageType == MessageType.maintenance)
            edtDetailThistime.setText("未有过维保");
        tvReason.setText(liftModel.getFault_type());
        submit.setClickable(true);
    }

    void getScan(String id, String code) {
        showProgressDialog();
        MyJsonDataResponseCacheHandler myJsonDataResponseCacheHandler = new MyJsonDataResponseCacheHandler<LiftModel>(LiftModel.class, false) {
            @Override
            public void onJsonDataSuccess(LiftModel object) {
                initData(object);
            }

            @Override
            public boolean onJsonCacheData(boolean has) {
                return false;
            }

            @Override
            public void onHttpComplete() {
                super.onHttpComplete();
                dismissProgressDialog();
            }
        };
        if (messageType == MessageType.repair) {
            FaultApi.getInstance().scan(mContext, id, code, myJsonDataResponseCacheHandler);
            rlThistime.setVisibility(View.GONE);
            rlLasttime.setVisibility(View.GONE);
        } else if (messageType == MessageType.maintenance) {
            MaintenanceApi.getInstance().scan(mContext, id, code, myJsonDataResponseCacheHandler);
            tvReason.setVisibility(View.GONE);
            tvTitleReason.setVisibility(View.GONE);
        }
    }

    @Override
    public void initListener() {

    }


    @OnClick(R.id.tv_detail_submit)
    void submit(View view) {
        if (messageType == MessageType.repair) {
            FaultApi.getInstance().sign(mContext, id, bdLocation.getLatitude(), bdLocation.getLongitude(), new MySimpleJsonDataResponseCacheHandler(new MySimpleJsonDataResponseCacheHandler.OnJsonCallBack() {
                @Override
                public void success() {
                    startActivity(new Intent(mContext, FaultDetailEditCompleteActivity.class)
                            .putExtra("messageType", messageType).putExtra("id", id));
                    finish();
                }

                @Override
                public void fail() {

                }
            }));

        } else if (messageType == MessageType.maintenance) {
            if (liftModel == null)
                return;
            MaintenanceApi.getInstance().sign(mContext, liftModel.getLift_id(), liftModel.getPlan_id()
                    , bdLocation.getLatitude(), bdLocation.getLongitude(), new MyJsonDataResponseCacheHandler<SignModel>(SignModel.class, false) {
                @Override
                public void onJsonDataSuccess(SignModel object) {
                    startActivity(new Intent(mContext, MaintenancePolicyActivity.class)
                            .putExtra("signModel", (Parcelable) object));
                    finish();
                }

                @Override
                public boolean onJsonCacheData(boolean has) {
                    return false;
                }
            });
        }

    }

}
