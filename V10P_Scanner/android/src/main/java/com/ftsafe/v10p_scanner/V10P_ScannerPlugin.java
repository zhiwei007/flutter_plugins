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

/**
 * V10P_ScannerPlugin
 */
public class V10P_ScannerPlugin implements FlutterPlugin, MethodCallHandler {
    public interface V10ScannerApiErrorCode {
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

    private Context getCtx() {
        return ctx;
    }

    private ScannerSeriveConnection scannerSeriveConnection;
    private ServiceResponseModel serviceResponseModel;

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        switch (call.method) {
      /*
      You can call this method intializeScanner. Its return type is void. After
calling this method you need to wait for a broadcast. Broadcast filter action is
‘com.service.scanner.initialize’. You will get a Model of ServiceResponseModel.java in
Broadcast intent with the key ‘ServiceResponseModel’. In this model you can get error code and
error message.
      * */
            case "init":
                new Thread(() -> {
                    Looper.prepare();
                    scannerSeriveConnection = new ScannerSeriveConnection(getCtx());
                    while (true) {
                        if (ScannerSeriveConnection.isServiceConnected() && ScannerSeriveConnection.getService() != null) {
                            break;
                        }
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (ScannerSeriveConnection.isServiceConnected() && ScannerSeriveConnection.getService() != null) {
                        result.success(V10ScannerApiErrorCode.ERROR_SERVICE_OK + "");
                    } else {
                        result.error(V10ScannerApiErrorCode.ERROR_SERVICE_DISCONNECTED + "", "init failed!", null);
                    }
                }).start();

                break;
        /*
        *In this case you can check whether the scanner is initialized by using the method
isScannerInitialize() of the model already provided in reponse and the scanner serial number can
be obtained (if available) by invoking the method getSerialNumber() of current model.
        * */
            case "hasInit":
                if (!ScannerSeriveConnection.isServiceConnected() || ScannerSeriveConnection.getService() == null) {
                    result.error(V10ScannerApiErrorCode.ERROR_SERVICE_DISCONNECTED + "", "ScannerSerive disconnected", null);
                    return;
                }
                try {
                    ServiceResponseModel serviceScannerInitialize = ScannerSeriveConnection.getService().isScannerInitialize();
                    if (serviceScannerInitialize.getErrorCode() == 0) {
                        result.success(V10ScannerApiErrorCode.ERROR_SERVICE_OK + "");
                    } else {
                        result.error(V10ScannerApiErrorCode.ERROR_SERVICE_RESPONSE_ERROR + "", serviceScannerInitialize.getErrorString(), null);
                    }
                } catch (Exception e) {
                    result.error(V10ScannerApiErrorCode.ERROR_SERVICE_INVOKE_EXCP + "", "hasInit failed:" + e, null);
                }
                break;
        /*
        * This method will not return any data. It will open device for scanner.
Scanner will not scan until device is open
        * */
            case "openDevice":
                if (!ScannerSeriveConnection.isServiceConnected() || ScannerSeriveConnection.getService() == null) {
                    result.error(V10ScannerApiErrorCode.ERROR_SERVICE_DISCONNECTED + "", "ScannerSerive disconnected", null);
                    return;
                }
                try {
                    ScannerSeriveConnection.getService().openDevice();
                    result.success(V10ScannerApiErrorCode.ERROR_SERVICE_OK + "");
                } catch (Exception e) {
                    result.error(V10ScannerApiErrorCode.ERROR_SERVICE_INVOKE_EXCP + "", "openDevice failed!:" + e, null);
                }
                break;
        /*
        * This method will not return any data. It will close device for scanner. It
will close device and not scan until you reopen device.
        * */
            case "closeDevice":
                if (!ScannerSeriveConnection.isServiceConnected() || ScannerSeriveConnection.getService() == null) {
                    result.error(V10ScannerApiErrorCode.ERROR_SERVICE_DISCONNECTED + "", "ScannerSerive disconnected", null);
                    return;
                }
                try {
                    ScannerSeriveConnection.getService().closeDevice();
                    result.success(V10ScannerApiErrorCode.ERROR_SERVICE_OK + "");
                } catch (Exception e) {
                    result.error(V10ScannerApiErrorCode.ERROR_SERVICE_INVOKE_EXCP + "", "closeDevice failed!:" + e, null);
                }
                break;

        /*
        * This method will give you a Model of ServiceResponseModel.java. You
have to check that device is ready to scan before calling scanFinger method because some model
take few seconds to open device
        * */
            case "isDeviceReady":
                if (!ScannerSeriveConnection.isServiceConnected() || ScannerSeriveConnection.getService() == null) {
                    result.error(V10ScannerApiErrorCode.ERROR_SERVICE_DISCONNECTED + "", "ScannerSerive disconnected", null);
                    return;
                }

                try {
                    ServiceResponseModel serviceScanner = ScannerSeriveConnection.getService().isScannerInitialize();
                    if (serviceScanner.getErrorCode() == 0) {
                        result.success(V10ScannerApiErrorCode.ERROR_SERVICE_OK + "");
                    } else {
                        result.success("device is not ready!");
                    }
                } catch (Exception e) {
                    result.error(V10ScannerApiErrorCode.ERROR_SERVICE_INVOKE_EXCP + "", "isDeviceReady failed!:" + e, null);
                }
                break;
      /*The response will contain FingerPrintModel model. You can use its method to get
      Template Type, Template Size, Image Quality, Finger Print Template and Image of Scanned
      Finger.*/
            case "scan":
                if (!ScannerSeriveConnection.isServiceConnected() || ScannerSeriveConnection.getService() == null) {
                    result.error(V10ScannerApiErrorCode.ERROR_SERVICE_DISCONNECTED + "", "ScannerSerive disconnected", null);
                    return;
                }
                try {
                    serviceResponseModel = ScannerSeriveConnection.getService().scanFinger();
                } catch (RemoteException e) {
                    result.error(V10ScannerApiErrorCode.ERROR_SERVICE_INVOKE_EXCP + "", "scanFinger cause exception:" + e, null);
                    return;
                }
                int errorCode = serviceResponseModel.getErrorCode();
                if (errorCode == 0) {
                    FingerPrintModel fingerPrintModel = serviceResponseModel.getFingerPrintModel();
                    try {
                        convertBimtap(fingerPrintModel, result);
                    } catch (Exception e) {
//                        showToastUI("scan  failed! \n" + e);
                        result.error(V10ScannerApiErrorCode.ERROR_SERVICE_RESPONSE_ERROR + "", "scan  failed!", null);
                        return;
                    }
                } else {
//                    showToastUI(errorCode + "," + errorCode + ";error msg:" + serviceResponseModel.getErrorString());
                    result.error(V10ScannerApiErrorCode.ERROR_SERVICE_RESPONSE_ERROR + "", serviceResponseModel.getErrorString(), null);
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

    private void convertBimtap(FingerPrintModel fingerPrintModel, Result result) {
        Bitmap bitmap = fingerPrintModel.getFingerPrintImage();
        if (bitmap == null) {
            result.error(V10ScannerApiErrorCode.ERROR_SERVICE_RESPONSE_ERROR+"", "scan finger failed!\n getFingerPrintImage() return null!", null);
            return;
        }
        int quality = fingerPrintModel.getQuality();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] buf = bos.toByteArray();
        result.success(buf);
    }

    private void showToastUI(String log) {
        new Handler(Looper.getMainLooper()).post(() -> {
            Toast.makeText(getCtx(), "" + log, Toast.LENGTH_LONG).show();
        });
    }
}
