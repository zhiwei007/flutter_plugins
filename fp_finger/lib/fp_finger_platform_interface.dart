import 'dart:typed_data';

import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'fp_finger_method_channel.dart';

abstract class FpFingerPlatform extends PlatformInterface {
  /// Constructs a FpFingerPlatform.
  FpFingerPlatform() : super(token: _token);

  static final Object _token = Object();

  static FpFingerPlatform _instance = MethodChannelFpFinger();
  static  MethodChannelFpFinger methodChannelFpFinger =   MethodChannelFpFinger();
  /// The default instance of [FpFingerPlatform] to use.
  ///
  /// Defaults to [MethodChannelFpFinger].
  static FpFingerPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FpFingerPlatform] when
  /// they register themselves.
  static set instance(FpFingerPlatform instance) {
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
