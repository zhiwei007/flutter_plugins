import 'dart:ffi';
import 'dart:typed_data';
import 'v10p_scanner_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';


abstract class V10PScannerPlatform extends PlatformInterface {
  /// Constructs a FpFingerPlatform.
  V10PScannerPlatform() : super(token: _token);

  static final Object _token = Object();

  static V10PScannerPlatform _instance = MethodChannelV10PScannerFinger();
  static  MethodChannelV10PScannerFinger methodChannelFpFinger =   MethodChannelV10PScannerFinger();
  /// The default instance of [FpFingerPlatform] to use.
  ///
  /// Defaults to [MethodChannelFpFinger].
  static V10PScannerPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FpFingerPlatform] when
  /// they register themselves.
  static set instance(V10PScannerPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> init() {
    return methodChannelFpFinger.init();
  }
  Future<String?> hasInit() {
    return methodChannelFpFinger.hasInit();
  }
  Future<String?> closeDevice() {
    return methodChannelFpFinger.closeDevice();
  }
  Future<String?> openDevice() {
    return methodChannelFpFinger.openDevice();
  }
  Future<String?> isDeviceReady() {
    return methodChannelFpFinger.isDeviceReady();
  }
  Future<Uint8List?> scan() {
    return methodChannelFpFinger.scan();
  }
}
