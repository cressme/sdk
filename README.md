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


       @Override
       public void onCreate() {
          super.onCreate();
          Lovense.getInstance(this).setDeveloperToken("YOU TOKEN");
        }
      }
    ```
* add scan success notification
  ```java
    Lovense.getInstance(this).setSearchToyListener(new OnSearchToyListener() {
            @Override
            public void onSearchToy(LovenseToy toy) {  // 找到设备回调

            }

            @Override
            public void finishScaning() {  // 扫描结束回调

            }

            @Override
            public void onError(LovenseError lovenseError) { // 错误回调

            }
        });
     ```
 * add connect success notification
   ```java
    Lovense.getInstance(this).connectToy(toyId, new OnConnectListener() {
            @Override
            public void onConnected(String toyId) { // 连接成功回调

            }

            @Override
            public void onError(LovenseError lovenseError) { // 连接错误回调

            }

            @Override
            public void onServiceDiscover(String toyId) { // 服务准备完成回调

            }
        });
      ```
 
* add toy message or command Callback
     ```java
        Lovense.getInstance(this).setSendCommandListener(toyId, new OnSendCommandListener() {
       
            // Notification state changed!   true:Start Notify state  false:Stop Notify state
            @Override
            public void notify(String toyId, String uuid, boolean started) { } 
            
            @Override
            public void writeResult(String toyId, int status) { }  // 命令发送完成回调

            @Override
            public void requestFailed(String toyId, int ordinal) { } // 命令发送请求失败
            
             // 连接状态变更回调 status:0 成功执行连接操作 newState 当前设备的连接状态，0 设备已断开 1:设备正在连接 2：设备已连接 3：设备正在断开
            @Override
            public void onConnectionStateChange(String toyId, int status, int newState) { }

            @Override
            public void onResultToyData(String toyId, Toy toy) { }  // 玩具参数回调

            @Override
            public void onResultBattery(String toyId, int battery) { }  //电量回调

            @Override
            public void onResultAidLightStatus(String toyId, Integer status) { }  // 辅助灯状态回调

            @Override
            public void onResultLightStatus(String toyId, Integer status) { } // 指示灯状态回调

            @Override
            public void notifityToyCharacteristic(String toyId, String value) { }  // 其他信息回调

            @Override
            public void onOrderNotificationSuccess(String toyId) { }  // 指令发送成功回调

            @Override
            public void onOrderNotificationError(String error) { }  // 指令错误（无法识别）回调

            @Override
            public void onGetAllExistedProgramSuccess(String programs) { }  // 获取指令列表成功回调

            @Override
            public void onResultMoveWaggleSuccess(String msg) { }  // 震动信息回调

            @Override
            public void onError(LovenseError error);  { }  // 错误信息回调
        });

   ```
 * Search the toys over Bluetooth
    ```java
        Lovense.getInstance(getApplication()).searchToys(true);
    ```
 * stop Search the toys 
    ```java
        Lovense.getInstance(getApplication()).searchToys(false);
    ```
 * Save the toys
    ```java
        Lovense.getInstance(getApplication()).saveToys(lovenseToys, new OnErrorListener());
    ```
 * Retrieve the saved toys
   ```java
        Lovense.getInstance(getApplication()).listToys(new OnErrorListener());
    ```
 * Connect the toy
   ```java
         Lovense.getInstance(getApplication()).connectToy(toyId,new OnConnectListener());
    ```
 * Disconnect the toy
    ```java
         Lovense.getInstance(getApplication()).disconnect(toyId);
    ```
 *  Send a command to the toy
    ```java
         Lovense.getInstance(getApplication()).sendCommand(toyId,LovenseToy.COMMAND_VIBRATE,vibrateLevel);
    ```
    
 ### 5、 ** command method list **
  `COMMAND_VIBRATE`
  `-Vibrate the toy .The parameter must be between 0 and 20!`

  `COMMAND_ROTATE`
  `-Rotate the toy .The parameter must be between 0 and 20!`

  `COMMAND_ROTATE_CLOCKWISE`
  `-Rotate clockwise .The parameter must be between 0 and 20!`

  `COMMAND_ROTATE_ANTI_CLOCKWISE`
  `-Rotate anti-clockwise .The parameter must be between 0 and 20!`

  `COMMAND_ROTATE_CHANGE`
  `-Change the rotation direction`

  `COMMAND_VIBRATE1`
  `-Activate the first vibrator at level n .The parameter must be between 0 and 20!`

  `COMMAND_VIBRATE2`
  `-Activate the second vibrator at level n .The parameter must be between 0 and 20!`

  `COMMAND_VIBRATE_FLASH`
  `-Vibrate the toy at level n, and flash the light at the same time .The parameter must be between 0 and 20!`

  `COMMAND_FLASH`
  `-Flash the light 3 times`

  `COMMAND_LIGHT_OFF`
  `-Turn off the light (saved permanently)`

  `COMMAND_LIGHT_ON`
  `-Turn on the light (saved permanently)`

  `COMMAND_GET_LIGHT_STATUS`
  `-Get the light status (1: on, 0:off)`

  `COMMAND_ALIGHT_OFF`
  `-Turn off the AID light (saved permanently)`

  `COMMAND_ALIGHT_ON`
  `-Turn on the AID light (saved permanently)`

  `COMMAND_GET_ALIGHT_STATUS`
  `-Get the AID light status (1: on, 0:off)`

  `COMMAND_GET_BATTERY`
  `-Get battery status`

  `COMMAND_GET_DEVICE_TYPE`
  `-Get device/toy information`

  `COMMAND_START_MOVE`
  `-Start tracking the toy movement (0-4)`

  `COMMAND_STOP_MOVE`
  `-Stop tracking the toy movement`
  
 ### 7、 ** entity class **
   * LovnseToy 玩具类，记录玩具所有属性（如玩具名称，玩具类型等）
     ```java
      private String toyId; // toy id

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
      
