using UnityEngine;
using UnityEngine.UI;

public class Sample : MonoBehaviour
{
    private AndroidNativeHelper _androidNativeHelper;
    
    [SerializeField] private int _baudRate;
    [SerializeField] private int _dataBits;
    [SerializeField] private int _stopBits;
    [SerializeField] private int _parity;

    [SerializeField] private Text _messageText;

    private void OnEnable()
    {
        _androidNativeHelper = new AndroidNativeHelper();
        _androidNativeHelper.InitNativeLib();
    }

    private void Start()
    {
        
    }

    public void Find()
    {
        _androidNativeHelper.FindSerialPortDevice();
    }

    public void Open()
    {
        _androidNativeHelper.OpenSerialPort(_baudRate, _dataBits, _stopBits, _parity);
    }

    #region message from native

    public void getScanResult(string aMessage)
    {
        _messageText.text = aMessage;
    }

    #endregion
}
