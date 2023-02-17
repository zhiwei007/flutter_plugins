import 'dart:ffi';
import 'dart:typed_data';
import 'v10p_scanner_platform_interface.dart';
class V10PScanner {
  Future<String?> init() {
    return V10PScannerPlatform.instance.init();
  }
  Future<String?> hasInit() {
    return V10PScannerPlatform.instance.hasInit();
  }

  Future<String?> openDevice() {
    return V10PScannerPlatform.instance.openDevice();
  }
  Future<String?> closeDevice() {
    return V10PScannerPlatform.instance.closeDevice();
  }

  Future<Uint8List?>  scan() {
    return V10PScannerPlatform.instance.scan();
  }

  Future<String?> isDeviceReady() {
    return V10PScannerPlatform.instance.isDeviceReady();
  }
}
