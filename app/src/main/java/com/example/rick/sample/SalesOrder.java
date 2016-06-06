package com.example.rick.sample;

import com.orm.dsl.Ignore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rick on 6/5/16.
 */
public class SalesOrder extends Model {

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public String getOrderDateString() {
        return order_date.toString();
    }

    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }

    public Boolean getSync_id() {
        return sync_id;
    }

    public void setSync_id(Boolean sync_id) {
        this.sync_id = sync_id;
    }
    @Ignore
    static Customer TEMP_CUSTOMER;

    String order_id;
    String user_id;
    String customer_id;
    Date order_date;
    Boolean sync_id;
    int order_status_id;

    List<OrderDetail> getLineItems() {
        return OrderDetail.find(OrderDetail.class, "orderid = ?", getId().toString());
    }

    static List<SalesOrder> getUnprocessedOrders()
    {
        return SalesOrder.find(SalesOrder.class,"orderstatusid = ?","1");
    }

    static ArrayList<SalesOrder> getUnprocessedOrdersArray()
    {
        List<SalesOrder> list = SalesOrder.find(SalesOrder.class,"orderstatusid = ?","1");

        ArrayList<SalesOrder> arrayList = new ArrayList<>();
        for(SalesOrder order : list)
        {
            arrayList.add(order);
        }

        return arrayList;
    }

    public SalesOrder generateOrderNumber() {

        this.order_id = "OD5400" + SalesOrder.listAll(SalesOrder.class).size() + 1;

        return this;
    }
}
