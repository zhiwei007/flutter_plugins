package com.service.scanner;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

public class ScannerSeriveConnection {

    private static IRemoteScannerService service;
    private RemoteServiceConnection serviceConnection;

    Context context;
    private static boolean serviceConnected;

    public ScannerSeriveConnection(Context context) {
        this.context = context;
        connectService();
    }

    private void  connectService() {
        serviceConnection = new RemoteServiceConnection();
        Intent i = new Intent("com.service.scanner.ScannerManager");
        i.setPackage("com.service.scanner");
        setServiceConnected(context.bindService(i, serviceConnection,
                Context.BIND_AUTO_CREATE));
    }

    class RemoteServiceConnection implements ServiceConnection {

        public void onServiceConnected(ComponentName name, IBinder boundService) {
            service = IRemoteScannerService.Stub
                    .asInterface((IBinder) boundService);
//			Toast.makeText(context, "Service connected ", Toast.LENGTH_SHORT)
//					.show();
            try {
                service.initializeScanner();
            } catch (RemoteException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG)
                        .show();

            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            service = null;
            setServiceConnected(false);
            Toast.makeText(context, "Service disconnected", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public static IRemoteScannerService getService() {
        return service;
    }

    public static void setService(IRemoteScannerService service) {
        ScannerSeriveConnection.service = service;
    }

    public static boolean isServiceConnected() {
        return serviceConnected;
    }

    public static void setServiceConnected(boolean serviceConnected) {
        ScannerSeriveConnection.serviceConnected = serviceConnected;
    }
}
