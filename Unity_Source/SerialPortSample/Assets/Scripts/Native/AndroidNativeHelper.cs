using UnityEngine;

public class AndroidNativeHelper
{
    protected AndroidJavaObject _androidInstanceJavaObject;
    
    public AndroidNativeHelper()
    {
        AndroidJavaObject androidUnityLibJavaClass = new AndroidJavaObject("com.ethanlin.serialportlib.UnitySerialPortDataLib");
        _androidInstanceJavaObject = androidUnityLibJavaClass.CallStatic<AndroidJavaObject>("getInstance");
    }
    
    /// <summary>
    /// init native lib and bluetooth manager
    /// </summary>
    public void InitNativeLib()
    {
        if (_androidInstanceJavaObject != null)
        {
            _androidInstanceJavaObject.Call("initSerialPortManagerAndReceiver");
        }
        else
        {
            Debug.LogError("Error, android native library Java object is null!!!");
        }
    }

    public void FindSerialPortDevice()
    {
        if (_androidInstanceJavaObject != null)
        {
            _androidInstanceJavaObject.Call("findSerialPortDevice");
        }
        else
        {
            Debug.LogError("Error, android native library Java object is null!!!");
        }
    }

    public void RequestUsbPermission()
    {
        if (_androidInstanceJavaObject != null)
        {
            _androidInstanceJavaObject.Call("requestUsbPermission");
        }
        else
        {
            Debug.LogError("Error, android native library Java object is null!!!");
        }
    }

    public void OpenSerialPort(int aBaudRate, int aDataBits, int aStopBits, int aParity)
    {
        if (_androidInstanceJavaObject != null)
        {
            _androidInstanceJavaObject.Call("openSerialPort", aBaudRate, aDataBits, aStopBits, aParity);
        }
        else
        {
            Debug.LogError("Error, android native library Java object is null!!!");
        }
    }
}
