package com.lovense.sdkdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lovense.sdklibrary.Toy;
import com.lovense.sdklibrary.callBack.LovenseScanCallBack;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private View start;
    private TextView stop,title;
    private RecyclerView recyclerView;

    private RxPermissions rxPermissions;

    List<Toy> toys = new ArrayList<>();
    private ToyAdapter toyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        start= findViewById(R.id.start_scan);
        stop= findViewById(R.id.stop_scan);
        title= findViewById(R.id.title);
        recyclerView = ((RecyclerView) findViewById(R.id.recyler_view));

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        toyAdapter = new ToyAdapter(this, toys);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(toyAdapter);

        MyApplication.lovenseSDK.setLovenseScaListener(new LovenseScanCallBack() {
            @Override
            public void foundDevice(Toy device) {
                addDevice(device);
            }

            @Override
            public void finishScaning() {
//                Toast.makeText(MainActivity.this, "扫描结束！", Toast.LENGTH_SHORT).show();
                title.setText("search toy(stop scan)");
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        rxPermissions = new RxPermissions(this);
        start.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               rx();
           }
       });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.lovenseSDK.scanDevice(false);
            }
        });
    }

    private void scanDev() {
        toys.clear();
        toyAdapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "start scan！", Toast.LENGTH_SHORT).show();
        title.setText("search toy(scaning)");
        MyApplication.lovenseSDK.scanDevice(true);
    }


    private void rx(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH)
                        .subscribe(new io.reactivex.functions.Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean b) throws Exception {
                                if (b) {
                                    scanDev();
                                } else {
                                    Toast.makeText(MainActivity.this, "If you\\'re using Android 6.0+, your GPS must be enabled to connect to Bluetooth devices.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                scanDev();
            }
        } else {
            scanDev();
        }
    }

    private void addDevice(Toy toy) {
        if (toy != null) {
            if (!isAdded(toy)) {
                toys.add(toy);
                toyAdapter.notifyDataSetChanged();
                //lovenseSDK.requestConnect(device.getAddress());
            }
        }
    }

    protected boolean isAdded(Toy toy) {
        for (Toy t:toys) {
            String address = t.getAddress();
            String toyAddress = toy.getAddress();
            Log.e("scan 7", "scanDevice: " + address+"  2:"+toyAddress  );
            if (!TextUtils.isEmpty(address) && address.equals(toyAddress)) {
                return true;
            }
        }
        return false;
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ToyConnectEvent mToyConnectEvent) {
        String address = mToyConnectEvent.getAddress();
        int connect = mToyConnectEvent.getConnect();
        for (int i = 0; i < toys.size(); i++) {
            Toy toy = toys.get(i);
            if (toy.getAddress().equals(address)){
                toy.setStatus(connect);
                toyAdapter.notifyItemChanged(i);
                break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

