import 'package:flutter/material.dart';
import 'dart:async';
import 'package:flutter/services.dart';
/*step:1 import the the api*/
import 'package:v10p_scanner/v10p_scanner.dart';
void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _result = '';
  final _fpFingerPlugin = V10PScanner();

  @override
  void initState() {
    super.initState();
    /*step:2  init() */
    init();
  }

  @override
  void dispose() {
    super.dispose();
    /*You should call 'closeDevice' when you are not using another interface*/
    closeDevice();
  }


  Future<void> init() async {
    String? result;
    try {
      result = await _fpFingerPlugin.init()   ;
    } on PlatformException catch (e) {
      result = "invoke init failed!\nerror code:" +
          e.code +
          ", error msg:" +
          e.message!;
    }
    if (!mounted) return;
    setState(() {
      _result = result!;
    });
  }

  Future<void> hasInit() async {
    String? result;
    try {
      result = await _fpFingerPlugin.hasInit()   ;
    } on PlatformException catch (e) {
      result = "invoke hasInit failed!\nerror code:" +
          e.code +
          ", error msg:" +
          e.message!;
    }
    if (!mounted) return;
    setState(() {
      _result = result!;
    });
  }

  Future<void> isDeviceReady() async {
    String? result;
    try {
      result = await _fpFingerPlugin.isDeviceReady()   ;
    } on PlatformException catch (e) {
      result = "invoke isDeviceReady failed!\nerror code:" +
          e.code +
          ", error msg:" +
          e.message!;
    }
    if (!mounted) return;
    setState(() {
      _result = result!;
    });
  }

  Future<void> openDevice() async {
    String? result;
    try {
      result = await _fpFingerPlugin.openDevice()   ;
    } on PlatformException catch (e) {
      result = "openDevice failed!\nerror code:" +
          e.code +
          ", error msg:" +
          e.message!;
    }
    if (!mounted) return;
    setState(() {
      _result = result!;
    });
  }

  Future<void> closeDevice() async {
    String? result;
    try {
      result = await _fpFingerPlugin.closeDevice()  ;
    } on PlatformException catch (e) {
      result = " closeDevice failed!\nerror code:" +
          e.code +
          ", error msg:" +
          e.message!;
    }
    if (!mounted) return;
    setState(() {
      _result = result!;
    });
  }

  late Uint8List _imageByte = Uint8List(0);

  Future<void> fingerScan() async {
    String error = "";
    Uint8List? imageByte;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      imageByte = await _fpFingerPlugin.scan();
    } on PlatformException catch (e) {
      error =
          "scan failed!\nerror code:" + e.code + ", error msg:" + e.message!;
      setState(() {
        _result = error;
      });
      return;
    }
    if (!mounted) return;
    // imageByte =    File(result).readAsBytesSync();
    setState(() {
      _imageByte = imageByte!;
      _result = error;
    });
  }

  List<ButtonSegment> _segments = [
    const ButtonSegment(
        value: 1,
        icon: Icon(Icons.health_and_safety_sharp),
        label: Text(
          "openDevice",
          style: TextStyle(color: Colors.white),
        )),
    const ButtonSegment(
        value: 2,
        icon: Icon(Icons.close_sharp),
        label: Text("closeDevice", style: TextStyle(color: Colors.white))),
    const ButtonSegment(
        value: 3,
        icon: Icon(Icons.bakery_dining_outlined),
        label: Text("isDeviceReady", style: TextStyle(color: Colors.white))),
    const ButtonSegment(
        value: 4,
        icon: Icon(Icons.read_more),
        label: Text("hasInit", style: TextStyle(color: Colors.white))),
  ];
  int _index = 0;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('V10P Scanner'),
          centerTitle: true,
          actions: [
            Padding(
                padding: const EdgeInsets.only(right: 40),
                child: InkWell(
                    onTap: init,
                    child: const Icon(Icons.read_more_sharp, size: 40)))
          ],
        ),
        body: Center(
            child: Column(
          children: [
            Padding(
                padding: const EdgeInsets.all(10),
                child: Text('invoke result: $_result\n')),
            Visibility(
                visible: _imageByte.isNotEmpty,
                child: InkWell(
                    onTap: fingerScan,
                    child: Image.memory(_imageByte,
                        width: 300,
                        height: 300,
                        fit: BoxFit.fill,
                        filterQuality: FilterQuality.high))),
            Padding(
                padding: const EdgeInsets.all(10),
                child: SegmentedButton(
                  segments: _segments,
                  emptySelectionAllowed: true,
                  selected: {_index},
                  onSelectionChanged: (index) {
                    setState(() {
                      try {
                        _index = index.first as int;
                      } catch (e) {
                        print("onSelectionChanged error :$e");
                      }
                    });

                    switch (_index) {
                      case 1:
                        openDevice();
                        break;
                      case 2:
                        closeDevice();
                        break;
                      case 3:
                        isDeviceReady();
                        break;
                      case 4:
                        hasInit();
                        break;
                    }
                  },
                  showSelectedIcon: true,
                  style: ButtonStyle(
                    backgroundColor: MaterialStatePropertyAll(
                        Theme.of(context).primaryColor),
                    iconColor: MaterialStateProperty.all(Colors.white),
                  ),
                ))
          ],
        )),
        floatingActionButton: FloatingActionButton(
          onPressed: fingerScan,
          backgroundColor: Colors.white,
          child:
              const Icon(Icons.fingerprint, color: Colors.pinkAccent, size: 35),
        ),
      ),
    );
  }
}
