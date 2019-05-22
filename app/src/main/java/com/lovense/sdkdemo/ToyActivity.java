package com.lovense.sdkdemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lovense.sdklibrary.Toy;
import com.lovense.sdklibrary.callBack.LovenseCommandCallBack;
import com.lovense.sdklibrary.callBack.LovenseConnectCallBack;
import com.lovense.sdklibrary.callBack.LovenseDisConnectCallBack;

import org.greenrobot.eventbus.EventBus;

import static com.lovense.sdkdemo.MyApplication.lovenseSDK;

/**
 * Created  on 2019/5/14 09:17
 *
 * @author zyy
 */
public class ToyActivity extends AppCompatActivity implements View.OnClickListener {

    protected TextView tvType, tvAddress, tvVersion, tvBattery,tvMovement;

    private String address;
    private View back,stopConnect;
    private LovenseConnectCallBack lovenseConnectCallBack;
    private EditText etProgram;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toy);
        initUI();
    }

    protected void initUI() {
        Intent intent = getIntent();

        back = findViewById(R.id.back);
        stopConnect = findViewById(R.id.stop_connect);

        tvType = findViewById(R.id.tv_type);
        tvAddress = findViewById(R.id.tv_address);
        tvVersion = findViewById(R.id.tv_version);
        tvBattery = findViewById(R.id.tv_battery);
        tvMovement = findViewById(R.id.tv_movement);
        address = intent.getStringExtra("address");

        if (!lovenseSDK.isConnected(address)){
            lovenseConnectCallBack = new LovenseConnectCallBack() {

                @Override
                public void onConnected(String address) {
                    EventBus.getDefault().post(new ToyConnectEvent(1, address));
                }


                @Override
                public void onError(String msg) {
                    try {
                        Toast.makeText(ToyActivity.this, msg, Toast.LENGTH_SHORT).show();
                        final AlertDialog.Builder normalDialog =
                                new AlertDialog.Builder(ToyActivity.this);
                        normalDialog.setTitle("notice");
                        normalDialog.setMessage(msg);
                        normalDialog.setPositiveButton("back",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //...To-do
                                        finish();
                                    }
                                });
                        normalDialog.setNegativeButton("AGAIN",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //...To-do
                                        lovenseSDK.requestConnect(address, lovenseConnectCallBack);
                                    }
                                });
                        // 显示
                        normalDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onServiceDiscover(String address) {
                    lovenseSDK.getDeviceType(address);
                    lovenseSDK.getBattery(address);
                    lovenseSDK.getBattery(address);
                }
            };
            lovenseSDK.requestConnect(address, lovenseConnectCallBack);
        }else {
            lovenseSDK.getDeviceType(address);
            lovenseSDK.getBattery(address);
            lovenseSDK.getBattery(address);
        }

        lovenseSDK.setBtCharacteristicListener(address, new LovenseCommandCallBack() {
            @Override
            public void notify(String address, String uuid, boolean started) {

            }


            @Override
            public void writeResult(String address, int status) {

            }

            @Override
            public void requestFailed(String address, int ordinal) {
                Toast.makeText(ToyActivity.this, "request failed!", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onConnectionStateChange(String address, int status, int newState) {
                EventBus.getDefault().post(new ToyConnectEvent(newState,address));
            }

            @Override
            public void onResultToyData(String address, Toy toy) {
                tvType.setText("Device Info："+toy.getType());
                tvAddress.setText("MAC Address："+toy.getMacAddress());
                tvVersion.setText("Version："+toy.getVersion());
            }

            @Override
            public void onResultBattery(String address, int battery) {
                tvBattery.setText("Battery："+battery+"%");
            }

            @Override
            public void onResultAidLightStatus(String address, Integer status) {
                Toast.makeText(ToyActivity.this, status==1?"AID Light on":"AID Light off", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResultLightStatus(String address, Integer status) {
                Toast.makeText(ToyActivity.this, status==1?"Light on":"Light off", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void notifityToyCharacteristic(String address, String value) {

            }

            @Override
            public void onOrderNotificationSuccess(String address) {
//                Toast.makeText(ToyActivity.this, address, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOrderNotificationError(String error) {
                Toast.makeText(ToyActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onGetAllExistedProgramSuccess(String programs) {
                Toast.makeText(ToyActivity.this, "Program is : "+programs, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResultMoveWaggleSuccess(String msg) {

                tvMovement.setText("Movement:"+msg);
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(ToyActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        stopConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lovenseSDK.disconnect(address, new LovenseDisConnectCallBack() {
                    @Override
                    public void disConnected(String address, int status) {
                        EventBus.getDefault().post(new ToyConnectEvent(status,address));
                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        SeekBar commVibrate = findViewById(R.id.comm_vibrate);
        commVibrate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lovenseSDK.commVibrate(address, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        findViewById(R.id.tv_flash).setOnClickListener(this);

        SeekBar noraRotate = findViewById(R.id.nora_rotate);
        noraRotate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lovenseSDK.rotate(address, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar noraRotateTrue = findViewById(R.id.nora_rotate_true);
        noraRotateTrue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lovenseSDK.rotateTrue(address, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar noraRotateFalse = findViewById(R.id.nora_rotate_false);
        noraRotateFalse.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lovenseSDK.rotateFalse(address, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        findViewById(R.id.tv_rotate_change).setOnClickListener(this);



        SeekBar edgeVibrate1 = findViewById(R.id.edge_vibrate1);
        edgeVibrate1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lovenseSDK.vibrate1(address, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar edgeVibrate2 = findViewById(R.id.edge_vibrate2);
        edgeVibrate2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lovenseSDK.vibrate2(address, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.tv_alight_off).setOnClickListener(this);
        findViewById(R.id.tv_alight_on).setOnClickListener(this);
        findViewById(R.id.tv_get_alight).setOnClickListener(this);

        findViewById(R.id.tv_light_off).setOnClickListener(this);
        findViewById(R.id.tv_light_on).setOnClickListener(this);



        findViewById(R.id.tv_start_move_waggle).setOnClickListener(this);
        findViewById(R.id.tv_stop_move_waggle).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_flash:
                lovenseSDK.flash(address);
                lovenseSDK.getDeviceType(address);
                break;
            case R.id.tv_rotate_change:
                lovenseSDK.rotateChange(address);
                break;
            case R.id.tv_alight_off:
                lovenseSDK.aLightOff(address);
                break;
            case R.id.tv_alight_on:
                lovenseSDK.aLightOn(address);
                break;
            case R.id.tv_get_alight:
                lovenseSDK.getAlight(address);
                break;
            case R.id.tv_light_off:
                lovenseSDK.lightOff(address);
                break;
            case R.id.tv_light_on:
                lovenseSDK.lightOn(address);
                break;
            case R.id.tv_start_move_waggle:
                tvMovement.setText("Movement:0");
                lovenseSDK.startMoveWaggle(address);
                break;
            case R.id.tv_stop_move_waggle:
                tvMovement.setText("");
                lovenseSDK.endMoveWaggle(address);
                break;
        }
    }





}
