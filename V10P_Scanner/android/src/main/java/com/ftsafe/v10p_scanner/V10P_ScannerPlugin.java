package com.ftsafe.v10p_scanner;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.service.scanner.FingerPrintModel;
import com.service.scanner.ScannerSeriveConnection;
import com.service.scanner.ServiceResponseModel;

import java.io.ByteArrayOutputStream;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** V10P_ScannerPlugin */
public class V10P_ScannerPlugin implements FlutterPlugin, MethodCallHandler {
  public     interface  V10ScannerApiErrorCode{
        int ERROR_SERVICE_OK = 0x00000000;
        int ERROR_SERVICE_DISCONNECTED = 0x00000001;
        int ERROR_SERVICE_RESPONSE_ERROR = 0x00000002;
        int ERROR_SERVICE_INVOKE_EXCP = 0x00000003;
  }
  private MethodChannel channel;
  private Context ctx;
  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "v10p_scanner");
    channel.setMethodCallHandler(this);
    ctx = flutterPluginBinding.getApplicationContext();
  }

  private Context getCtx(){
    return ctx;
  }
  private ScannerSeriveConnection scannerSeriveConnection ;
  private ServiceResponseModel serviceResponseModel;
  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {

    switch (call.method){
      case "init":
        new Thread(()->{
          Looper.prepare();
          scannerSeriveConnection = new ScannerSeriveConnection(getCtx());
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
             return ;
          }
          if(ScannerSeriveConnection.isServiceConnected() &&  ScannerSeriveConnection.getService()!= null){
            result.success(V10ScannerApiErrorCode.ERROR_SERVICE_OK+"");
          }else{
            result.error(V10ScannerApiErrorCode.ERROR_SERVICE_DISCONNECTED+"","init failed!",null);
          }
        }).start();

        break;
      case "hasInit":
        /*检查服务连接状态*/
        if(!ScannerSeriveConnection.isServiceConnected() ||    ScannerSeriveConnection.getService()== null){
          result.error(V10ScannerApiErrorCode.ERROR_SERVICE_DISCONNECTED+"","ScannerSerive disconnected",null);
          return ;
        }
        try {
          ServiceResponseModel serviceScannerInitialize =  ScannerSeriveConnection.getService().isScannerInitialize();
          if(serviceScannerInitialize.getErrorCode() == 0){
            result.success(V10ScannerApiErrorCode.ERROR_SERVICE_OK+"");
          }else{
            result.error(V10ScannerApiErrorCode.ERROR_SERVICE_RESPONSE_ERROR+"",serviceScannerInitialize.getErrorString(),null);
          }
        } catch (Exception e) {
          result.error(V10ScannerApiErrorCode.ERROR_SERVICE_INVOKE_EXCP+"","hasInit failed:"+e ,null);
        }
        break;
      case "openDevice":
        /*检查服务连接状态*/
        if(!ScannerSeriveConnection.isServiceConnected() ||    ScannerSeriveConnection.getService()== null){
          result.error(V10ScannerApiErrorCode.ERROR_SERVICE_DISCONNECTED+"","ScannerSerive disconnected",null);
          return ;
        }
        try {
           ScannerSeriveConnection.getService().openDevice();
          result.success(V10ScannerApiErrorCode.ERROR_SERVICE_OK+"");
        } catch (Exception e) {
          result.error(V10ScannerApiErrorCode.ERROR_SERVICE_INVOKE_EXCP+"","openDevice failed!:"+e,null);
        }
        break;
      case "closeDevice":
        /*检查服务连接状态*/
        if(!ScannerSeriveConnection.isServiceConnected() ||    ScannerSeriveConnection.getService()== null){
          result.error(V10ScannerApiErrorCode.ERROR_SERVICE_DISCONNECTED+"","ScannerSerive disconnected",null);
          return ;
        }
        try {
          ScannerSeriveConnection.getService().closeDevice();
          result.success(V10ScannerApiErrorCode.ERROR_SERVICE_OK+"");
        } catch (Exception e) {
          result.error(V10ScannerApiErrorCode.ERROR_SERVICE_INVOKE_EXCP+"","closeDevice failed!:"+e,null);
        }
        break;
      case "isDeviceReady":
        /*检查服务连接状态*/
        if(!ScannerSeriveConnection.isServiceConnected() ||    ScannerSeriveConnection.getService()== null){
          result.error(V10ScannerApiErrorCode.ERROR_SERVICE_DISCONNECTED+"","ScannerSerive disconnected",null);
          return ;
        }

        try {
          ServiceResponseModel serviceScanner = ScannerSeriveConnection.getService().isScannerInitialize();
          if(serviceScanner.getErrorCode() == 0){
            result.success(V10ScannerApiErrorCode.ERROR_SERVICE_OK+"");
          }else{
            result.success("device is not ready!");
          }
        } catch (Exception e) {
          result.error(V10ScannerApiErrorCode.ERROR_SERVICE_INVOKE_EXCP+"" ,"isDeviceReady failed!:"+e,null);
        }
        break;
      case "scan":
        /*检查服务连接状态*/
        if(!ScannerSeriveConnection.isServiceConnected() ||    ScannerSeriveConnection.getService()== null){
          result.error(V10ScannerApiErrorCode.ERROR_SERVICE_DISCONNECTED+"","ScannerSerive disconnected",null);
          return ;
        }
          try {
            serviceResponseModel = ScannerSeriveConnection.getService().scanFinger();
          } catch (RemoteException e) {
            result.error( V10ScannerApiErrorCode.ERROR_SERVICE_INVOKE_EXCP+"","scanFinger cause exception:"+e,null);
            return ;
          }
          int errorCode = serviceResponseModel.getErrorCode();
          if(errorCode == 0){
            FingerPrintModel  fingerPrintModel = serviceResponseModel.getFingerPrintModel();
            try{
              convertBimtap(fingerPrintModel,result);
            }catch (Exception e){
              showToastUI(  "scan  failed! \n"+e);
               result.error( V10ScannerApiErrorCode.ERROR_SERVICE_RESPONSE_ERROR+"","scan  failed!",null);
              return;
            }
          }else{
            showToastUI(errorCode + ","+errorCode+";error msg:"+serviceResponseModel.getErrorString());
            result.error(V10ScannerApiErrorCode.ERROR_SERVICE_RESPONSE_ERROR+""  , serviceResponseModel.getErrorString(),null);
          }

        break;

      default:
        result.notImplemented();
    }

  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  private    void convertBimtap(FingerPrintModel  fingerPrintModel,Result result){
    Bitmap  bitmap = fingerPrintModel.getFingerPrintImage();
    if (bitmap == null){
      result.error("-2","scan finger failed!\n getFingerPrintImage() return null!",null);
      return ;
    }
    int quality = fingerPrintModel.getQuality();
    ByteArrayOutputStream  bos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
    byte[] buf = bos.toByteArray();
    result.success(buf);
  }
  private  void showToastUI(String log){
      new Handler(Looper.getMainLooper()).post(()->{
          Toast.makeText(getCtx(),""+log,Toast.LENGTH_LONG).show();
      });
  }
}
