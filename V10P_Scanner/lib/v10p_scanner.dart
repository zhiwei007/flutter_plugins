import 'dart:typed_data';
import 'v10p_scanner_platform_interface.dart';
class V10PScanner {
/*
*This method must be called before any other method is called.
* If '0' is returned, the execution succeeds.
*othervise:You should try... catch Catches error messages
* */
  Future<String?> init() {
    return V10PScannerPlatform.instance.init();
  }

  /*Check that device is ready to scan before calling scan  method,
  because it take few seconds to open device.
* If '0' is returned, the execution succeeds.
*othervise:You should try... catch Catches error messages
  */
  Future<String?> hasInit() {
    return V10PScannerPlatform.instance.hasInit();
  }

  /*
  * It will open device for scanner.
Scanner will not scan until device is open.
* If '0' is returned, the execution succeeds.
*othervise:You should try... catch Catches error messages
  * */
  Future<String?> openDevice() {
    return V10PScannerPlatform.instance.openDevice();
  }

/*
* It will close device for scanner. It
will close device and not scan until you reopen device.
* If '0' is returned, the execution succeeds.
*othervise:You should try... catch Catches error messages
* */
  Future<String?> closeDevice() {
    return V10PScannerPlatform.instance.closeDevice();
  }

  /*
  *  If executed successfully, it will return a Uint8List data type which you can convert to an Image object
  *  see details in example
  * example/lib/main.dart:
  * Image.memory(_imageByte,
                        width: 300,
                        height: 300,
                        fit: BoxFit.fill,
                        filterQuality: FilterQuality.high)]
 *othervise:You should try... catch Catches error messages
  * */
  Future<Uint8List?> scan() {
    return V10PScannerPlatform.instance.scan();
  }

/*
* Check that device is ready to scan before calling scan
bacuase it will take few seconds to open device.
* If '0' is returned, the execution succeeds.
*othervise:You should try... catch Catches error messages
* */
  Future<String?> isDeviceReady() {
    return V10PScannerPlatform.instance.isDeviceReady();
  }
}
