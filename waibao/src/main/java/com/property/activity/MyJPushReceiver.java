package com.property.activity;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.property.BaseApplication;
import com.property.utils.AppRoute;
import com.vk.simpleutil.library.XSimpleLogger;
import com.vk.simpleutil.library.XSimpleText;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class MyJPushReceiver extends BroadcastReceiver {

    private NotificationManager nm;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
        // Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: "
        // + AndroidUtil.printBundle(bundle));
        XSimpleLogger.Log().e("JPushID:" + JPushInterface.getRegistrationID(BaseApplication.mContext));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            XSimpleLogger.Log().e("JPush用户注册成功");
            XSimpleLogger.Log().e("JPushID:" + JPushInterface.getRegistrationID(BaseApplication.mContext));

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
            XSimpleLogger.Log().e("接受到推送下来的自定义消息");
//            receivingMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                .getAction())) {
            XSimpleLogger.Log().e("接受到推送下来的通知");

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
                .getAction())) {
            XSimpleLogger.Log().e("用户点击打开了通知");

            openNotification(context, bundle);

        } else {
            XSimpleLogger.Log().d("Unhandled intent - " + intent.getAction());
        }
    }

//    private void receivingMessage(Context context, Bundle bundle) {
//        // TODO Auto-generated method stub
//        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//        XSimpleLogger.Log().e("message : " + message);
//        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//        XSimpleLogger.Log().e("extras : " + extras);
//        if (!MyTextUtil.isEmpty(message) && message.equals("event")) {
//            String myValue;
////            int count;
//            try {
//                JSONObject extrasJson = new JSONObject(extras);
//                myValue = extrasJson.optString("event");
////                JSONObject parameters = extrasJson.optJSONObject("parameters");
////                count = parameters.optInt("count");
//            } catch (Exception e) {
//                XSimpleLogger.Log().e("Unexpected: extras is not a valid json",
//                        e);
//                return;
//            }
//            if (!MyTextUtil.isEmpty(myValue)) {
//                MessageFactory.getInstance().updataCount(myValue);
//
//            }
//        }
//    }

    private void openNotification(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

        String myValue = "";
        try {

            JSONObject extrasJson = new JSONObject(extras);
            XSimpleLogger.Log().e(extrasJson.toString());
            myValue = extrasJson.optString("app_route");
            if (XSimpleText.isEmpty(myValue))
                myValue = extrasJson.optString("txt");
        } catch (Exception e) {
            XSimpleLogger.Log().e("Unexpected: extras is not a valid json", e);
            return;
        }

        AppRoute.setActivityNewTask(true);
        AppRoute.goActivity(context, myValue);
    }

}
