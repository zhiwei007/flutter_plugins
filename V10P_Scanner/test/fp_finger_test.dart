import 'dart:ffi';
import 'dart:typed_data';
import 'package:flutter_test/flutter_test.dart';
import 'package:v10p_scanner/src/v10p_scanner_platform_interface.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFpFingerPlatform
    with MockPlatformInterfaceMixin
    implements V10PScannerPlatform {
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
  Future<String?> init() {
    // TODO: implement init
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

}
