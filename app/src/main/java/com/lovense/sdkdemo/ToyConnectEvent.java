package com.lovense.sdkdemo;

/**
 * Created  on 2019/5/16 13:58
 *
 * @author zyy
 */
public class ToyConnectEvent {
    private int connect;
    private String address;

    public ToyConnectEvent(int connect, String address) {
        this.connect = connect;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getConnect() {
        return connect;
    }

    public void setConnect(int connect) {
        this.connect = connect;
    }
}
