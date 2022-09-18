package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;


import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.UUID;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

import android.util.Log;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{

    private TextView _txtViewHello;

    // 获取到蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter;
    // 用来保存搜索到的设备信息
    private List<String> bluetoothDevices = new ArrayList<String>();
    // ListView组件
    private ListView lvDevices;
    // ListView的字符串数组适配器
    private ArrayAdapter<String> arrayAdapter;
    // UUID，蓝牙建立链接需要的
    private final UUID MY_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    // 为其链接创建一个名称
    private final String NAME = "Bluetooth_Socket";
    // 选中发送数据的蓝牙设备，全局变量，否则连接在方法执行完就结束了
    private BluetoothDevice _selectDevice;
    // 获取到选中设备的客户端串口，全局变量，否则连接在方法执行完就结束了
    private BluetoothSocket _clientSocket;
    // 获取到向设备写的输出流，全局变量，否则连接在方法执行完就结束了
    private OutputStream _os;
    private InputStream _is;
    // 服务端利用线程不断接受客户端信息
    private AcceptThread thread;
    //定义按钮
    //定义按钮
    private Button close_all_led;
    private Button red1 = null;
    private Button green1 = null;
    private Button blue1 = null;
    private Button breath = null;
    private TextView receive1;
    private SeekBar seekBar;
    private String LED_STATE = "hello";
    private TextView re_msg;
    private TextView _textViewV1;

//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        _txtViewHello = findViewById(R.id.textViewHello);
//        Button button1 = findViewById(R.id.button1);
//
//    }

    public void button1_click(View view)
    {
        String str = "Hello, 雯琪";
        _txtViewHello.setText(str);

        //搜索蓝牙设备
        setTitle("正在扫描...");

        _txtViewHello.setText("正在扫描...");
        // 点击搜索周边设备，如果正在搜索，则暂停搜索
        if (mBluetoothAdapter.isDiscovering())
        {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _txtViewHello = findViewById(R.id.textViewHello);
        Button button1 = findViewById(R.id.button1);
        _textViewV1 = findViewById(R.id.textViewV1);

//        red1 = (Button) findViewById(R.id.red);
//        green1 = (Button) findViewById(R.id.green);
//        blue1 = (Button) findViewById(R.id.blue);
//        receive1 = (TextView) findViewById(R.id.receive_text);
//        close_all_led = (Button) findViewById(R.id.close_all_led);
//        breath = (Button) findViewById(R.id.breath);
//        seekBar = (SeekBar) findViewById(R.id.seekBar);
//        re_msg = (TextView) findViewById(R.id.msg);

//        red1.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                LED_STATE = "R";
//                receive1.setText(LED_STATE);
//            }
//        });
//        green1.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                LED_STATE = "G";
//                receive1.setText(LED_STATE);
//            }
//        });
//        blue1.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                LED_STATE = "B";
//                receive1.setText(LED_STATE);
//            }
//        });
//        close_all_led.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                LED_STATE = "E";
//                receive1.setText(LED_STATE);
//            }
//        });
//        breath.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                LED_STATE = "H";
//                receive1.setText(LED_STATE);
//            }
//        });
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
//        {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
//            {
//                if (b)
//                {
//                    LED_STATE = "{0:" + i + "}";
//                    receive1.setText(LED_STATE);
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar)
//            {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar)
//            {
//
//            }
//        });

        //申请蓝牙权限, 同时Manifest中还需要添加这两行:
        //    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
        //    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
        if (Build.VERSION.SDK_INT >= 6.0)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }


        // 获取到蓝牙默认的适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // 获取到ListView组件
        lvDevices = (ListView) findViewById(R.id.lvDevices);
        // 为listview设置字符换数组适配器
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, bluetoothDevices);
        // 为listView绑定适配器
        lvDevices.setAdapter(arrayAdapter);
        // 为listView设置item点击事件侦听
        lvDevices.setOnItemClickListener(this);

        // 用Set集合保持已绑定的设备   将绑定的设备添加到Set集合。
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        if (devices.size() > 0)
        {
            for (BluetoothDevice bluetoothDevice : devices)
            {
                // 保存到arrayList集合中
                bluetoothDevices.add(bluetoothDevice.getName() + ":" + bluetoothDevice.getAddress() + "\n");
            }
        }

        // 因为蓝牙搜索到设备和完成搜索都是通过广播来告诉其他应用的
        // 这里注册找到设备和完成搜索广播
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        // 实例接收客户端传过来的数据线程
        thread = new AcceptThread();
        // 线程开始
        thread.start();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
            {
                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.i(TAG, "获取蓝牙权限成功");
            }
            return;
        }
    }


    //搜索蓝牙设备
    public void onClick_Search(View view)
    {
        //搜索蓝牙设备
        setTitle("正在扫描...");
        // 点击搜索周边设备，如果正在搜索，则暂停搜索
        if (mBluetoothAdapter.isDiscovering())
        {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }

    public void onClick_buttonA1On(View view)
    {
        //需要发送的信息
        String text = "a1on\r\n";

        try
        {
            if (_os == null)
            {
                Toast.makeText(this, "Not Connected",  Toast.LENGTH_SHORT).show();
                return;
            }
            _os.write(text.getBytes("UTF-8"));
        } catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(this, "发送a1on失败",  Toast.LENGTH_SHORT).show();
        }

    }

    public void onClick_buttonB1On(View view)
    {
        String text = "b1on\r\n";

        try
        {
            if (_os == null)
            {
                Toast.makeText(this, "Not Connected",  Toast.LENGTH_SHORT).show();
                return;
            }
            _os.write(text.getBytes("UTF-8"));
        } catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(this, "发送a1on失败",  Toast.LENGTH_SHORT).show();
        }
    }



    public void onClick_buttonOff1(View view)
    {
        //Toggle switch1
        String text = "off\r\n";

        try
        {
            if (_os == null)
            {
                Toast.makeText(this, "Not Connected",  Toast.LENGTH_SHORT).show();
                return;
            }

            _os.write(text.getBytes("UTF-8"));
        } catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(this, "发送f1失败",  Toast.LENGTH_SHORT).show();
        }

    }





    // 注册广播接收者
    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // 获取到广播的action
            String action = intent.getAction();
            // 判断广播是搜索到设备还是搜索完成
            if (action.equals(BluetoothDevice.ACTION_FOUND))
            {
                // 找到设备后获取其设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 判断这个设备是否是之前已经绑定过了，如果是则不需要添加，在程序初始化的时候已经添加了
                if (device.getBondState() != BluetoothDevice.BOND_BONDED)
                {
                    // 设备没有绑定过，则将其保持到arrayList集合中
                    bluetoothDevices.add(device.getName() + ":" + device.getAddress() + "\n");
                    // 更新字符串数组适配器，将内容显示在listView中
                    arrayAdapter.notifyDataSetChanged();
                }
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED))
            {
                setTitle("搜索完成");
            }
        }

//        @Override
//        public void onReceive(Context arg0, Intent intent)
//        {
//
//        }
    };

    // 点击listView中的设备，传送数据
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        //_clientSocket, 断开按钮点击后设置为空.
        if (_clientSocket != null)
            return;

        // 获取到这个设备的信息
        String s = arrayAdapter.getItem(position);
        // 对其进行分割，获取到这个设备的地址
        String address = s.substring(s.indexOf(":") + 1).trim();
        Log.d("TAG", address);
        // 判断当前是否还是正在搜索周边设备，如果是则暂停搜索
        if (mBluetoothAdapter.isDiscovering())
        {
            mBluetoothAdapter.cancelDiscovery();
        }
        // 如果选择设备为空则代表还没有选择设备
        if (_selectDevice == null)
        {
            //通过地址获取到该设备
            _selectDevice = mBluetoothAdapter.getRemoteDevice(address);
        }
        // 这里需要try catch一下，以防异常抛出
        try
        {
            // 判断客户端接口是否为空
            if (_clientSocket == null)
            {
                // 获取到客户端接口   注:如果还未配对, 则系统会弹出配对对话框进行配对.
                _clientSocket = _selectDevice.createRfcommSocketToServiceRecord(MY_UUID);
                // 向服务端发送连接
                _clientSocket.connect();
                // 获取到输出流，向外写数据
                _os = _clientSocket.getOutputStream();
                _is = _clientSocket.getInputStream();

            }
            // 判断是否拿到输出流
            if (_is != null)
            {
                // 需要发送的信息
                //String text = "我传过去了";
                // 以utf-8的格式发送出去
                //_os.write(LED_STATE.getBytes("UTF-8"));

                //启动数据接收线程
                receiveTask.execute();

                Toast.makeText(this, "连接成功", Toast.LENGTH_LONG).show();

            }

        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            // 如果发生异常则告诉用户发送失败
            Toast.makeText(this, "连接失败", Toast.LENGTH_LONG).show();
        }

    }

    // 创建handler，因为我们接收是采用线程来接收的，在线程中无法操作UI，所以需要handler
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            // 通过msg传递过来的信息，吐司一下收到的信息
            // Toast.makeText(BuletoothClientActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
            _textViewV1.setText((String) msg.obj);
        }
    };


    AsyncTask receiveTask = new AsyncTask()
    {

        @Override
        protected String doInBackground(Object[] params)
        {
            byte[] buffer = new byte[100];
            int count = 0;
            while (true)
            {
                try
                {
                    count = _is.read(buffer);
                    int a = _is.available();

                    Message msg = new Message();
                    // 发送一个String的数据，让他向上转型为obj类型
                    msg.obj = new String(buffer, 0, count, "utf-8");
                    // 发送数据
                    handler.sendMessage(msg);

                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o)
        {
            super.onPostExecute(o);
            Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
        }
    };


    // 服务端接收信息线程
    private class AcceptThread extends Thread
    {
        private BluetoothServerSocket serverSocket;// 服务端接口
        private BluetoothSocket socket;// 获取到客户端的接口
        private InputStream is;// 获取到输入流
        private OutputStream os;// 获取到输出流

        public AcceptThread()
        {
            try
            {
                // 通过UUID监听请求，然后获取到对应的服务端接口
                serverSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public void run()
        {
            try
            {
                // 接收其客户端的接口
                socket = serverSocket.accept();
                // 获取到输入流
                is = socket.getInputStream();
                // 获取到输出流
                os = socket.getOutputStream();

                // 无线循环来接收数据
                while (true)
                {
                    // 创建一个128字节的缓冲
                    byte[] buffer = new byte[128];
                    // 每次读取128字节，并保存其读取的角标
                    int count = is.read(buffer);
                    // 创建Message类，向handler发送数据
                    Message msg = new Message();
                    // 发送一个String的数据，让他向上转型为obj类型
                    msg.obj = new String(buffer, 0, count, "utf-8");
                    // 发送数据
                    handler.sendMessage(msg);
                }
            } catch (Exception e)
            {
                // TODO: handle exception
                e.printStackTrace();
            }

        }
    }//AcceptThread
}// class main activity