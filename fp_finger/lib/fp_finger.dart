import 'dart:typed_data';
import 'fp_finger_platform_interface.dart';

class FpFinger {
  Future<String?> init() {
    return FpFingerPlatform.instance.init();
  }
  Future<String?> hasInit() {
    return FpFingerPlatform.instance.hasInit();
  }

  Future<String?> openDevice() {
    return FpFingerPlatform.instance.openDevice();
  }
  Future<String?> closeDevice() {
    return FpFingerPlatform.instance.closeDevice();
  }

  Future<Uint8List?>  scan() {
    return FpFingerPlatform.instance.scan();
  }

  Future<String?> isDeviceReady() {
    return FpFingerPlatform.instance.isDeviceReady();
  }
}
