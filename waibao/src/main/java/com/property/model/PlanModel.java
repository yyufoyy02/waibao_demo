package com.property.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.property.base.BaseModel;
import com.vk.simpleutil.library.XSimpleText;

public class PlanModel extends BaseModel implements Parcelable {
    String id;
    String customer_name;
    String customer_id;
    String customer_address;
    String plan_name;
    int lifts_count;
    double plan_time;
    int status;
    int ok_count;

    /**
     * 电梯完成数
     */
    public int getOk_count() {
        return ok_count;
    }

    public void setOk_count(int ok_count) {
        this.ok_count = ok_count;
    }

    /**
     * 计划id,
     */
    public String getId() {
        return XSimpleText.isEmpty(id, "");
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 物业公司名字
     */
    public String getCustomer_name() {
        return XSimpleText.isEmpty(customer_name, "");
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    /**
     * 物业公司
     */
    public String getCustomer_id() {
        return XSimpleText.isEmpty(customer_id, "");
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    /**
     * 地址,
     */
    public String getCustomer_address() {
        return XSimpleText.isEmpty(customer_address, "");
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
    }

    /**
     * 第5期半月维保计划
     */
    public String getPlan_name() {
        return XSimpleText.isEmpty(plan_name, "");
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    /**
     * 计划内电梯数
     */
    public int getLifts_count() {
        return lifts_count;
    }

    public void setLifts_count(int lifts_count) {
        this.lifts_count = lifts_count;
    }

    /**
     * 计划时间
     */
    public double getPlan_time() {
        return plan_time;
    }

    public void setPlan_time(double plan_time) {
        this.plan_time = plan_time;
    }

    /**
     * 计划状态 1：进行中，2：完成
     */
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.customer_name);
        dest.writeString(this.customer_id);
        dest.writeString(this.customer_address);
        dest.writeString(this.plan_name);
        dest.writeInt(this.lifts_count);
        dest.writeDouble(this.plan_time);
        dest.writeInt(this.status);
        dest.writeInt(this.ok_count);
    }

    public PlanModel() {
    }

    protected PlanModel(Parcel in) {
        this.id = in.readString();
        this.customer_name = in.readString();
        this.customer_id = in.readString();
        this.customer_address = in.readString();
        this.plan_name = in.readString();
        this.lifts_count = in.readInt();
        this.plan_time = in.readDouble();
        this.status = in.readInt();
        this.ok_count = in.readInt();
    }

    public static final Parcelable.Creator<PlanModel> CREATOR = new Parcelable.Creator<PlanModel>() {
        public PlanModel createFromParcel(Parcel source) {
            return new PlanModel(source);
        }

        public PlanModel[] newArray(int size) {
            return new PlanModel[size];
        }
    };
}
