# V10P_Scanner

The plugin is used for fingerprint input of   V10P devices.

## Getting Started

This project is a starting point for a Flutter
[plug-in package](https://flutter.dev/developing-packages/),
a specialized package that includes platform-specific implementation code for
Android.

For help getting started with Flutter development, view the
[online documentation](https://flutter.dev/docs), which offers tutorials,
samples, guidance on mobile development, and a full API reference.

As so far,the plugin has been tested in the FP V10P POS(android 11)

## NOTE:
1.Make sure you have the 'ftrAnsiSDKService-release_1.0.0.0.apk' service installed on your device

2.When build release apk,you must add the code in your project
~~~
-keepclasseswithmembernames class com.service.scanner.FingerPrintModel{
   public *;
}

-keepclasseswithmembernames class com.service.scanner.ServiceResponseModel{
   public *;
}
~~~
## Integration in flutter:
1.config in your yaml,like this:\
~~~
v10p_scanner:
path: ../
~~~

2.import 'package:v10p_scanner/v10p_scanner.dart';

3.use the api ,eg:
~~~
Future<void> init() async {
    String? result;
    try {
    result = await _fpFingerPlugin.init()   ;
    } on PlatformException catch (e) {
    result = "invoke init failed!\nerror code:" + e.code + ", error msg:" + e.message!;
    }
   if (!mounted) return;
    setState(() {
    _result = result!;
   });
}
~~~   
     
    




