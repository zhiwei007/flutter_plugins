import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'v10p_scanner_platform_interface.dart';

/// An implementation of [V10PScannerPlatform] that uses method channels.
class MethodChannelV10PScannerFinger extends V10PScannerPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('v10p_scanner');

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
