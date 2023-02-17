package com.ftsafe.fp_finger;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.service.scanner.FingerPrintModel;
import com.service.scanner.IRemoteScannerService;
import com.service.scanner.ScannerSeriveConnection;
import com.service.scanner.ServiceResponseModel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** FpFingerPlugin */
public class FpFingerPlugin implements FlutterPlugin, MethodCallHandler {
  private MethodChannel channel;
  private Context ctx;
  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "fp_finger");
    channel.setMethodCallHandler(this);
    ctx = flutterPluginBinding.getApplicationContext();
  }

  private Context getCtx(){
    return ctx;
  }
  private ScannerSeriveConnection scannerSeriveConnection ;
  private IRemoteScannerService remoteScannerService;
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
            throw new RuntimeException(e);
          }
          if(ScannerSeriveConnection.isServiceConnected()){
            showToastUI("ScannerSerive connected...");
            remoteScannerService =  ScannerSeriveConnection.getService();
          }

          if(remoteScannerService!=null){
            showToastUI("init success");
            result.success(  " init success");
          }else{
            showToastUI("init failed");
            result.success(  " init failed");
          }
        }).start();

        break;
      case "hasInit":
        /*检查服务连接状态*/
        if(!ScannerSeriveConnection.isServiceConnected() || remoteScannerService==null){
          showToastUI("ScannerSerive disconnected...");
          remoteScannerService =  ScannerSeriveConnection.getService();
          result.success("ScannerSerive disconnected");
          return ;
        }

        try {
          ServiceResponseModel serviceScannerInitialize = remoteScannerService.isScannerInitialize();
          if(serviceScannerInitialize.getErrorCode() == 0){
            result.success("hasInit   success");
          }else{
            result.success("hasInit failed: "+serviceScannerInitialize.getErrorString());
          }
        } catch (Exception e) {
          result.success("hasInit failed: "+e);
        }
        break;
      case "openDevice":
        /*检查服务连接状态*/
        if(!ScannerSeriveConnection.isServiceConnected() || remoteScannerService==null){
          showToastUI("ScannerSerive disconnected...");
          remoteScannerService =  ScannerSeriveConnection.getService();
          result.success("ScannerSerive disconnected");
          return ;
        }
        try {
           remoteScannerService.openDevice();
           result.success("openDevice success!");
        } catch (Exception e) {
          result.success("openDevice failed!");
        }
        break;
      case "closeDevice":
        /*检查服务连接状态*/
        if(!ScannerSeriveConnection.isServiceConnected() || remoteScannerService==null){
          showToastUI("ScannerSerive disconnected...");
          remoteScannerService =  ScannerSeriveConnection.getService();
          result.success("ScannerSerive disconnected");
          return ;
        }
        try {
          remoteScannerService.closeDevice();
          result.success("closeDevice success!");
        } catch (Exception e) {
          result.success("closeDevice failed!");
        }
        break;
      case "isDeviceReady":
        /*检查服务连接状态*/
        if(!ScannerSeriveConnection.isServiceConnected() || remoteScannerService==null){
          showToastUI("ScannerSerive disconnected...");
          remoteScannerService =  ScannerSeriveConnection.getService();
          result.success("ScannerSerive disconnected");
          return ;
        }

        try {
          ServiceResponseModel serviceScanner = remoteScannerService.isScannerInitialize();
          if(serviceScanner.getErrorCode() == 0){
            result.success("device is  ready!");
          }else{
            result.success("device is not ready!");
          }
        } catch (Exception e) {
          result.success("device is not ready!\n"+e);
        }
        break;
      case "scan":
        if(ScannerSeriveConnection.isServiceConnected() && remoteScannerService!=null){
          try {
            serviceResponseModel = remoteScannerService.scanFinger();
          } catch (RemoteException e) {
            showToastUI("invoke method<scanFinger> cause exception:"+e);
            result.success(  "invoke method<scanFinger> cause exception:"+e);
            return ;
          }
          int errorCode = serviceResponseModel.getErrorCode();
          if(errorCode == 0){
            FingerPrintModel  fingerPrintModel = serviceResponseModel.getFingerPrintModel();
            try{
              convertBimtap(fingerPrintModel,result);
            }catch (Exception e){
              showToastUI(  "scan finger failed! \n"+e);
              result.success( "scan finger failed!");
              return;
            }
          }else{
            showToastUI(errorCode + ","+errorCode+";error msg:"+serviceResponseModel.getErrorString());
            result.success(errorCode + ","+errorCode+";error msg:"+serviceResponseModel.getErrorString());
          }
        }else{
          showToastUI(  "scan finger failed!");
          result.success( "scan finger failed!");
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
//    String fingerPicPath  = ctx.getFilesDir()+File.separator+"finger.png";
//    File file  = new File(fingerPicPath);
//    if(file.exists()){
//      file.delete();
//    }
//    Log.e("ft","fingerPicPath:"+fingerPicPath);
    Bitmap  bitmap = fingerPrintModel.getFingerPrintImage();
    if (bitmap == null){
//        throw  new NullPointerException("getFingerPrintImage() return null!");
      result.success("scan finger failed!\n getFingerPrintImage() return null!");
      return ;
    }
    int quality = fingerPrintModel.getQuality();
    Log.e("ft","convertBimtap quality:"+quality);
//    BufferedOutputStream bos ;
//    try {
//      bos = new BufferedOutputStream(new FileOutputStream(fingerPicPath));
//    } catch (FileNotFoundException e) {
//      result.success("scan finger failed!");
//      return ;
//    }
    ByteArrayOutputStream  bos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
//    try {
//      bos.flush();
//    } catch (Exception e) {
//      Log.e("ft","convertBimtap:"+e);
//    }finally {
//      try {
//        bos.close();
//
//      } catch (IOException e) {
//
//      }
//    }
    byte[] buf = bos.toByteArray();
    result.success(buf);
  }
  private  void showToastUI(String log){
      new Handler(Looper.getMainLooper()).post(()->{
          Toast.makeText(getCtx(),""+log,Toast.LENGTH_LONG).show();
      });
  }
}
