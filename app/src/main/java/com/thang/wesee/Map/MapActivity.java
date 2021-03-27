package com.thang.wesee.Map;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.thang.wesee.Config.PreFerenceManager;
import com.thang.wesee.R;

public class MapActivity  extends FragmentActivity
implements OnMapReadyCallback {
    private String La;
    private String Lo;
    private Intent intent;
    private GoogleMap map;
    private PreFerenceManager preFerenceManager;
    private double la,lo;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        preFerenceManager=new PreFerenceManager(getApplication());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toast.makeText(getApplicationContext(), ""+preFerenceManager.getLa()+"-"+preFerenceManager.getLo(), Toast.LENGTH_SHORT).show();
    if(preFerenceManager.getLa().length()>5 && preFerenceManager.getLo().length()>5){
        la=Double.parseDouble(preFerenceManager.getLa().trim());
        lo=Double.parseDouble(preFerenceManager.getLo().trim());
    }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        LatLng latLng=new LatLng(lo,la);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
        map.addMarker(new MarkerOptions()
                .title("Person")
                .snippet("This is person need to help")
                .position(latLng));

    }

}
