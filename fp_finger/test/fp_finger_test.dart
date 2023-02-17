import 'dart:typed_data';

import 'package:flutter_test/flutter_test.dart';
import 'package:fp_finger/fp_finger.dart';
import 'package:fp_finger/fp_finger_platform_interface.dart';
import 'package:fp_finger/fp_finger_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFpFingerPlatform
    with MockPlatformInterfaceMixin
    implements FpFingerPlatform {

  @override
  Future<String?> init() {
    // TODO: implement init
    throw UnimplementedError();
  }


  @override
  Future<String?> closeDevice() {
    // TODO: implement closeDevice
    throw UnimplementedError();
  }

  @override
  Future<String?> hasInit() {
    // TODO: implement hasInit
    throw UnimplementedError();
  }

  @override
  Future<String?> isDeviceReady() {
    // TODO: implement isDeviceReady
    throw UnimplementedError();
  }

  @override
  Future<String?> openDevice() {
    // TODO: implement openDevice
    throw UnimplementedError();
  }

  @override
  Future<Uint8List?> scan() {
    // TODO: implement scan
    throw UnimplementedError();
  }
}

void main() {
  final FpFingerPlatform initialPlatform = FpFingerPlatform.instance;

  test('$MethodChannelFpFinger is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFpFinger>());
  });

  test('getPlatformVersion', () async {
    FpFinger fpFingerPlugin = FpFinger();
    MockFpFingerPlatform fakePlatform = MockFpFingerPlatform();
    FpFingerPlatform.instance = fakePlatform;

    expect(await fpFingerPlugin.init(), '42');
  });
}
