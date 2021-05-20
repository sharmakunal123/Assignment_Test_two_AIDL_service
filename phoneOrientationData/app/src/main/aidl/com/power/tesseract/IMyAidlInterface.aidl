// IMyAidlInterface.aidl
package com.power.tesseract;

// Declare any non-default types here with import statements

interface IMyAidlInterface {

    String requestValues();

    // TODO Should not return int or Long
    String requestXAxis();
    String requestYAxis();
    String requestZAxis();
    String requestAccuracy();
}