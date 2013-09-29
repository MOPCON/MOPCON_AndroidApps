package org.mopcon.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import android.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.mopcon.R;

public class GMap extends Activity implements LocationListener
{
    static final LatLng STU = new LatLng(22.763321,120.376292);
    private LatLng self_location;
    private GoogleMap map;
    private LocationManager locationManager;
    private boolean getService = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.GMap)).getMap();
        Marker nkut = map.addMarker(new MarkerOptions().position(STU).title("樹德科技大學").snippet("Mopcon"));

        // Move the camera instantly to NKUT with a zoom of 16.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(STU, 16));


        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            getService = true;
            locationServiceInital();
        }
        else
        {
            Toast.makeText(this,"please turn on the location service", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }


    }

    private LocationManager lms;
    private String bestProvider = LocationManager.GPS_PROVIDER;

    private void locationServiceInital()
    {
        lms = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        bestProvider = lms.getBestProvider(criteria, true);
        Location location = lms.getLastKnownLocation(bestProvider);
        getLocation(location);
    }

    private void getLocation(Location location)
    {
        if (location != null)
        {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            self_location = new LatLng(latitude,longitude);
        }
        else
        {
            Toast.makeText(this,"out of service", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
       getLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (getService)
        {
            lms.requestLocationUpdates(bestProvider, 1000, 1, this);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (getService)
        {
            lms.removeUpdates(this);
        }

    }


}