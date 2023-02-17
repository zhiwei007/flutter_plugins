package com.service.scanner;

import com.service.scanner.ServiceResponseModel;

interface IRemoteScannerService {

	void initializeScanner();
	ServiceResponseModel isScannerInitialize();
	ServiceResponseModel getScannerSerial();
	void openDevice();
	void closeDevice();
	ServiceResponseModel isDeviceReady();
	ServiceResponseModel scanFinger();
}
