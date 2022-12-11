package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, SeekBar.OnSeekBarChangeListener {

    GoogleMap map;
    SeekBar seekWidth, seekRed, seekBlue, seekGreen;
    Button btDraw, btClear;
    Polyline polyline = null;
    List<LatLng> latLngList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();

    private EditText mInputMessageView;
    private Button button;


    int red = 0, blue = 0, green = 0;


    private void attemptSend() {
        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }

        mInputMessageView.setText("");
        mSocket.emit("message", message);
    }


    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://192.168.99.152:3001");
        } catch (URISyntaxException e) {
        }
    }

    public Activity getActivity(Context context)
    {
        if (context == null)
        {
            return null;
        }
        else if (context instanceof ContextWrapper)
        {
            if (context instanceof Activity)
            {
                return (Activity) context;
            }
            else
            {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }


    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {



                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
                    addMessage(username, message);


        }
    };

    private void addMessage(String username, String message) {
        Log.d("IMPORTANT", message);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSocket.on("new message", onNewMessage);
        mSocket.connect();
        // Log.d("Test", new Boolean(mSocket.isActive()).toString());

        button= findViewById(R.id.button);
        mInputMessageView = findViewById(R.id.MInputMessageView);
        seekWidth = findViewById(R.id.seek_width);
        seekBlue = findViewById(R.id.seek_blue);
        seekRed = findViewById(R.id.seek_red);
        seekGreen = findViewById(R.id.seek_green);

        btClear = findViewById(R.id.bt_clear);
        btDraw = findViewById(R.id.bt_clear);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSend();

            }
        });


        //maps
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        // .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);

        //draw

        btDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ligne sur la map

                if (polyline != null) polyline.remove();

                PolylineOptions polylineOptions = new PolylineOptions()
                        .addAll(latLngList).clickable(true);
                polyline = map.addPolyline(polylineOptions);
                polyline.setColor(Color.rgb(red, green, blue));
                setWidth();


            }
        });


        //clear all
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (polyline != null)
                    polyline.remove();
                for (Marker marker : markerList) marker.remove();
                latLngList.clear();
                markerList.clear();
                seekWidth.setProgress(3);
                seekBlue.setProgress(0);
                seekRed.setProgress(0);
                seekGreen.setProgress(0);


            }
        });


        seekRed.setOnSeekBarChangeListener(this);
        seekBlue.setOnSeekBarChangeListener(this);
        seekGreen.setOnSeekBarChangeListener(this);

    }

    private void setWidth() {
        seekWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int width = seekWidth.getProgress();
                if (polyline != null)
                    polyline.setWidth(width);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //marker option
                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                // je cree mon marker
                Marker marker = map.addMarker(markerOptions);
                latLngList.add(latLng);
                markerList.add(marker);

            }
        });


        LatLng Resto1 = new LatLng(48.731772, 2.067787);
        map.addMarker(new MarkerOptions().position(Resto1).title("Resto1"));
        map.moveCamera(CameraUpdateFactory.newLatLng(Resto1));

        MarkerOptions options = new MarkerOptions().position(Resto1).title("Resto1");
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.seek_red:
                red = i;
                break;
            case R.id.seek_blue:
                blue = i;
                break;
            case R.id.seek_green:
                green = i;
                break;


        }
        polyline.setColor(Color.rgb(red, green, blue));

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}