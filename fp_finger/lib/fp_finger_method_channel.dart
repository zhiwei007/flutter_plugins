import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'fp_finger_platform_interface.dart';

/// An implementation of [FpFingerPlatform] that uses method channels.
class MethodChannelFpFinger extends FpFingerPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('fp_finger');

  @override
  Future<String?> init() async {
    final result = await methodChannel.invokeMethod<String>('init');
    return result;
  }
  @override
  Future<String?> hasInit() async {
    final result = await methodChannel.invokeMethod<String>('hasInit');
    return result;
  }

  @override
  Future<String?> openDevice() async {
    final result = await methodChannel.invokeMethod<String>('openDevice');
    return result;
  }

  @override
  Future<String?> closeDevice() async {
    final result = await methodChannel.invokeMethod<String>('closeDevice');
    return result;
  }
  @override
  Future<String?> isDeviceReady() async {
    final result = await methodChannel.invokeMethod<String>('isDeviceReady');
    return result;
  }

  @override
  Future<Uint8List?> scan() async {
    final result = await methodChannel.invokeMethod('scan');
    return result;
  }
}
