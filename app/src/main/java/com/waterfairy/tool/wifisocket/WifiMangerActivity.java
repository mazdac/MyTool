package com.waterfairy.tool.wifisocket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.waterfairy.tool.R;
import com.waterfairy.wifi.manger.WifiManager;
import com.waterfairy.wifi.listener.WifiServerListener;
import com.waterfairy.wifi.listener.WifiUserListener;

public class WifiMangerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "wifiMangerActivity";
    private TextView mTVIp, mTVInfo;
    private EditText mETIp, mETInput;
    private Button mBTSetAsServer, mBTConnect, mBTSend, mBTSearchWifiDevice;
    private WifiManager wifiManager;
    private int type;
    private String connectIp, serverIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_manger);
        findView();
        initView();
        initData();

    }

    private void initData() {
        wifiManager = WifiManager.getInstance();
    }

    private void initView() {
        mBTConnect.setOnClickListener(this);
        mBTSetAsServer.setOnClickListener(this);
        mBTSend.setOnClickListener(this);
        mBTSearchWifiDevice.setOnClickListener(this);
    }

    private void findView() {
        mTVInfo = (TextView) findViewById(R.id.info);
        mBTSend = (Button) findViewById(R.id.send);
        mETIp = (EditText) findViewById(R.id.ip);
        mTVIp = (TextView) findViewById(R.id.server_ip);
        mBTSetAsServer = (Button) findViewById(R.id.set_as_server);
        mBTConnect = (Button) findViewById(R.id.connect_to_wifi);
        mETInput = (EditText) findViewById(R.id.input);
        mBTSearchWifiDevice = (Button) findViewById(R.id.search_wifi_device);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.set_as_server:

                setAsServer();
                break;
            case R.id.connect_to_wifi:
                connect();
                break;
            case R.id.send:
                send();
                break;
            case R.id.search_wifi_device:
                startActivityForResult(new Intent(this, SearchWifiDeviceActivity.class), 11);
                break;
            case R.id.disconnect_user:
                wifiManager.disconnect();
                break;
            case R.id.disconnect_user_from_server:
                wifiManager.disconnectFromServer();
                break;
            case R.id.close_server:
                wifiManager.closeServer();
                break;
        }
    }

    private void send() {
        String input = mETInput.getText().toString();
        if (!TextUtils.isEmpty(input)) {
            byte[] bytes = input.getBytes();
            if (type == 2) {
                wifiManager.writeMsgFromUser(connectIp, bytes);
            } else if (type == 1) {
                wifiManager.writeMsgFromServer(bytes);
            }
        }
    }

    private void connect() {
        type = 2;
        connectIp = mETIp.getText().toString();
        if (!TextUtils.isEmpty(connectIp)) {
            wifiManager.connect(connectIp, new WifiUserListener() {
                @Override
                public void onConnecting() {
                    Log.i(TAG, "onConnecting: ");

                }

                @Override
                public void onConnectSuccess() {
                    Log.i(TAG, "onConnectSuccess: ");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBTConnect.setText("连接wifi(已连接)" + connectIp);
                        }
                    });
                }

                @Override
                public void onConnectError() {
                    Log.i(TAG, "onConnectError: ");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBTConnect.setText("连接wifi(连接失败)" + connectIp);
                        }
                    });
                }

                @Override
                public void onRead(byte[] readBytes) {
                    String read = new String(readBytes);
                    Log.i(TAG, "onRead: " + new String(readBytes));

                    read = mTVInfo.getText().toString() + read;
                    final String finalRead = read + "\n";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTVInfo.setText(finalRead);
                        }
                    });

                }

                @Override
                public void onWrite(byte[] writeBytes) {
                    Log.i(TAG, "onWrite: " + new String(writeBytes));
                }

                @Override
                public void onWriteError() {
                    Log.i(TAG, "onWriteError: ");
                }

                @Override
                public void onDisconnect() {
                    Log.i(TAG, "onDisconnect: ");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBTConnect.setText("连接wifi(连接断开)" + connectIp);
                        }
                    });
                }

                @Override
                public void isRightServer(boolean isRightServer) {

                }
            });
        }
    }

    private void setAsServer() {
        type = 1;
        serverIp = wifiManager.getLocalIP(this);
        mTVIp.setText(serverIp);
        wifiManager.setAsServer(true, "sport_request", "sport_return", new WifiServerListener() {
            @Override
            public void onStarting() {
                Log.i(TAG, "onStarting: ");
            }

            @Override
            public void onStartServerSuccess() {
                Log.i(TAG, "onStartServerSuccess: ");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBTSetAsServer.setText("作为服务器(已打开)(未连接)");
                    }
                });
            }

            @Override
            public void onStartServerError() {
                Log.i(TAG, "onStartServerError: ");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBTSetAsServer.setText("作为服务器");
                    }
                });
            }

            @Override
            public void onConnectSuccess(final String ipAddress) {
                Log.i(TAG, "onConnectSuccess: ");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBTSetAsServer.setText("作为服务器(已打开)(已连接)" + ipAddress);
                    }
                });
            }

            @Override
            public void onRead(byte[] readBytes) {
                String read = new String(readBytes);
//                Log.i(TAG, "onRead: " + new String(readBytes));

                read = mTVInfo.getText().toString() + read;
                final String finalRead = read + "\n";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTVInfo.setText(finalRead);

//                        wifiManager.writeMsgFromServer("hha".getBytes());
                    }
                });
            }

            @Override
            public void onWrite(String ipAddress, byte[] writeBytes) {
                Log.i(TAG, "onWrite: " + new String(writeBytes));
            }

            @Override
            public void onWriteError(String ipAddress) {
                Log.i(TAG, "onWriteError: ");
            }

            @Override
            public void onDisconnect(final boolean isServerOpen, String address) {
                Log.i(TAG, "onDisconnect: ");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String connect = null;
                        if (isServerOpen) {
                            mBTSetAsServer.setText("作为服务器(已打开)(未连接)");
                        } else {
                            mBTSetAsServer.setText("作为服务器(已关闭)(未连接)");
                        }

                    }
                });
            }

            @Override
            public void onServerClose() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBTSetAsServer.setText("作为服务器(已关闭)");
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == RESULT_OK) {
            String ipAddress = data.getStringExtra("ipAddress");
            mETIp.setText(ipAddress);
        }
    }
}
