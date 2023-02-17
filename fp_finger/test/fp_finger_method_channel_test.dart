import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:fp_finger/fp_finger_method_channel.dart';

void main() {
  MethodChannelFpFinger platform = MethodChannelFpFinger();
  const MethodChannel channel = MethodChannel('fp_finger');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.init(), '42');
  });
}
