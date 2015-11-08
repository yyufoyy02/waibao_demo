package com.property.activity;

import android.content.Intent;

import com.property.api.FaultApi;
import com.property.enumbase.MessageType;
import com.property.enumbase.UpdateType;
import com.property.http.MyJsonDataResponseCacheHandler;
import com.property.model.FaultModel;
import com.property.model.ImageModel;
import com.property.model.MessageModel;
import com.vk.simpleutil.library.XSimpleLogger;
import com.vk.simpleutil.view.pulltorefresh.lib.PullToRefreshBase;
import com.vk.simpleutil.view.pulltorefresh.lib.extras.IXListViewListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/8.
 */
public class FaultActivity extends MessageActivity implements IXListViewListener {

    List<FaultModel> faultModellist = new ArrayList<>();

    @Override
    public void initAllData() {
        super.initAllData();
        setTitle("抢修单");
        listView.setAdapter(mMessageAdapter);
        listView.setPullRefreshLoadEnable(true, true, PullToRefreshBase.Mode.BOTH);
        listView.setOnXListViewListener(this);
        getList(UpdateType.top);
    }

    List<MessageModel> initFault2Message(List<FaultModel> list) {
        List<MessageModel> messageModelList = new ArrayList<>();
        for (FaultModel faultModel : list) {
            MessageModel messageModel = new MessageModel();
            messageModel.setAddress(faultModel.getAddress());
            messageModel.setIcon(new ImageModel("drawable://" + R.drawable.icon_book));
            messageModel.setName("市政府电梯" + faultModel.getElevetor_number() + "号维保");
            messageModel.setTime(1444492800);
            messageModel.setStatus(faultModel.getStatus());
            messageModel.setMessage_type(1);
            messageModelList.add(messageModel);
        }
        return messageModelList;
    }

    void getList(final UpdateType updateType) {
        String id = null;
        if (updateType == UpdateType.top || faultModellist.isEmpty()) {
            id = null;
        } else {
            id = faultModellist.get(faultModellist.size() - 1).getId();
        }
        FaultApi.getInstance().getList(mContext, id, new MyJsonDataResponseCacheHandler<List<FaultModel>>(FaultModel.class, false) {
            @Override
            public void onJsonDataSuccess(List<FaultModel> object) {
                if (updateType == UpdateType.top)
                    faultModellist.clear();
                faultModellist.addAll(object);
                list.clear();
                list.addAll(initFault2Message(faultModellist));
                listView.notifyDataSetChanged();
            }

            @Override
            public void onHttpComplete() {
                super.onHttpComplete();
                listView.onRefreshComplete();
            }

            @Override
            public boolean onJsonCacheData(boolean has) {
                return false;
            }
        });
    }

    @Override
    public void onRefresh() {
        getList(UpdateType.top);
    }

    @Override
    public void onLoadMore() {
        getList(UpdateType.bottom);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCANLE && resultCode == RESULT_OK&&data!=null) {
            XSimpleLogger.Log().e("code:" + data.getStringExtra("code"));
            startActivity(new Intent(mContext, DetailActivity.class)
                    .putExtra("code",data.getStringExtra("code")).putExtra("messageType", MessageType.repair));
        }

    }
}
