# Lovense SDK for android 1.0
### 1、把以下文件复制到工程lib目录下
 * sdklibrary.aar <br>  ![](https://github.com/zyelement/sdk/blob/master/imgs/image1.png)
 
### 2、在app build.gradle引用，program build.gradle中配置
 * app  build.gradle<br>  ![](https://github.com/zyelement/sdk/blob/master/imgs/image2.png)
 * program build.gradle <br>  ![](https://github.com/zyelement/sdk/blob/master/imgs/image3.png)
 
### 3、AndroidManifest.xml 中配置权限和service注册
  * 权限列表<br> 
  ```java
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```
* 注册service<br> 
  ```java
    <service
            android:name="com.xtremeprog.sdk.ble.BleService"
            android:enabled="true" />
    ```
    
### 4、Connect Lovense Toys
* initialization sdk in you application and pass your token into Lovense framework
 ```java
 public class MyApplication extends Application {

    public  static LovenseSDK lovenseSDK;

    @Override
    public void onCreate() {
        super.onCreate();
        lovenseSDK = LovenseSDK.getInstance();
        lovenseSDK.init(this);
        // Pass your token into Lovense framework
        lovenseSDK.setDeveloperToken("YOU TOKEN");
    }
}
   ```
* add scan success notification
 ```java
 MyApplication.lovenseSDK.setLovenseScaListener(new LovenseScanCallBack() {
            @Override
            public void foundDevice(Toy device) {

            }

            @Override
            public void finishScaning() {

            }

            @Override
            public void onError(String msg) {

            }
        });
   ```
  * add connect success notification
 ```java
  lovenseSDK.requestConnect(address, new LovenseConnectCallBack() {
            @Override
            public void onConnected(String address) {

            }

            @Override
            public void onError(String msg) {

            }

            @Override
            public void onServiceDiscover(String address) {

            }
        });
   ```
     * add connect success notification
  * add disconnect success notification
  ```java
    lovenseSDK.disconnect(address, new LovenseDisConnectCallBack() {
                    @Override
                    public void disConnected(String address, int status) {
                       
                    }
                });
  ```
   * add toy message or command Callback
  ```java

        lovenseSDK.setBtCharacteristicListener(address, new LovenseCommandCallBack() {
            @Override
            public void notify(String address, String uuid, boolean started) { }
            
            @Override
            public void writeResult(String address, int status) { }

            @Override
            public void requestFailed(String address, int ordinal) { }
            
            @Override
            public void onConnectionStateChange(String address, int status, int newState) { }

            @Override
            public void onResultToyData(String address, Toy toy) { }

            @Override
            public void onResultBattery(String address, int battery) { }

            @Override
            public void onResultAidLightStatus(String address, Integer status) { }

            @Override
            public void onResultLightStatus(String address, Integer status) { }

            @Override
            public void notifityToyCharacteristic(String address, String value) { }

            @Override
            public void onOrderNotificationSuccess(String address) { }

            @Override
            public void onOrderNotificationError(String error) { }

            @Override
            public void onGetAllExistedProgramSuccess(String programs) { }

            @Override
            public void onResultMoveWaggleSuccess(String msg) { }

            @Override
            public void onError(String msg) { }
        });

  ```
  * Search the toys over Bluetooth
    ```java
     MyApplication.lovenseSDK.scanDevice(true);
    ```
 * stop scan the toys 
    ```java
       MyApplication.lovenseSDK.scanDevice(false);
    ```
 * Connect the toy
   ```java
        lovenseSDK.requestConnect(address,new LovenseConnectCallBack());
    ```
 * Disconnect the toy
    ```java
       lovenseSDK.disconnect(address, new LovenseDisConnectCallBack());
    ```
    
 ### 5、 ** command method list **
    * getDeviceType(String address); Get device/toy information
    * getBattery(String address) ; Get battery status
    * commVibrate(String address, int parameter); Vibrate the toy .The parameter must be between 0 and 20!
    * flash(String address); Flash the light 3 times
    * rotate(String address, int n); Rotate the toy .The parameter must be between 0 and 20!
    * rotateTrue(String address, int n); Rotate clockwise .The parameter must be between 0 and 20!
    * rotateFalse(String address, int n); Rotate anti-clockwise .The parameter must be between 0 and 20!
    * rotateChange(String address); Change the rotation direction!
    * vibrate1(String address, int parameter); Activate the first vibrator at level n .The parameter must be between 0 and 20!
    * vibrate2(String address, int parameter); Activate the second vibrator at level n .The parameter must be between 0 and 20!
    * aLightOff(String address); Turn off the AID light (saved permanently)
    * aLightOn(String address);  Turn on the AID light (saved permanently)
    * getAlight(String address); Get the AID light status (1: on, 0:off)
    * lightOff(String address);  Turn off the light (saved permanently)
    * lightOn(String address);   Turn on the light (saved permanently)
    * getLight(String address);  Get the light status (1: on, 0:off)
    * startMoveWaggle(String address); Start tracking the toy movement (0-4)
    * endMoveWaggle(String address); Stop tracking the toy movement
  
 ### 6、 ** callback list**
  * LovenseScanCallBack(): scan callback
    ```java
      void foundDevice(Toy device);
      void finishScaning();
      void onError(String msg);
    ```
    <br/>
  * LovenseConnectCallBack(): conect callback
    ```java
      void onConnected(String address); // 连接成功回调
      void onError(String msg); // 连接错误回调
      void onServiceDiscover(String address); // 服务准备完成回调
    ```
     <br/>
  * LovenseDisConnectCallBack(): disConect callback
     ```java
      void disConnected(String address, int status); // 断开连接回调
     ```
    <br/>
  * LovenseCommandCallBack(): command callback
     ```java
      void notify(String address, String uuid, boolean started); // Notification state changed!   true:Start Notify state  false:Stop Notify state
      void writeResult(String address, int status); // 命令发送完成回调
      void requestFailed(String address, int ordinal);// 命令发送请求失败
      void onConnectionStateChange(String address, int status, int newState); // 连接状态变更回调  status:0 成功执行连接操作  newState 当前设备的连接状态，0 设备已断开 1:设备正在连接 2：设备已连接 3：设备正在断开
      void onResultToyData(String address,Toy toy); // 玩具参数回调
      void onResultBattery(String address, int battery); //电量回调
      void onResultAidLightStatus(String address, Integer status); // 辅助灯状态回调
      void onResultLightStatus(String address, Integer status); // 指示灯状态回调
      void notifityToyCharacteristic(String address, String value); // 其他信息回调
      void onOrderNotificationSuccess(String success); // 指令发送成功回调
      void onOrderNotificationError(String error); // 指令错误（无法识别）回调
      void onGetAllExistedProgramSuccess(String programs); // 获取指令列表成功回调
      void onResultMoveWaggleSuccess(String msg); // 震动信息回调
      void onError(String msg); // 错误信息回调
    ```
 ### 7、 ** entity class **
   * Toy 玩具类，记录玩具所有属性（如玩具名称，玩具类型等）
     ```java
      private String address; // toy address

      private Integer version; // toy version

      private String type;// toy type

      private int status = -1; //toy connect status    // 1:connect -1：disconnect
   
      private int battery = -1;// toy battery

      private int rssi = 50;//

      private String deviceName; // toy name

      private String uuid; //

      private String deviceType; // 
      
      private String macAddress; // mac address
     ```
      
