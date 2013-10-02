package org.mopcon.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.mopcon.R;

public class FragmentGMap extends Fragment implements LocationListener
{
  static final LatLng STU = new LatLng(22.763321,120.376292);
  private LatLng self_location;
  private GoogleMap map;
  private LocationManager locationManager;
  private boolean getService = false;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.map_fragment,container,false);
    locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

    setTargetLocation(STU,"樹德科技大學","Mopcon APP!!");



    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    {
      getService = true;
      locationServiceInital();
    }
    else
    {
      Toast.makeText(getActivity(),"please turn on the location service", Toast.LENGTH_LONG).show();
      startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }
    return view;
  }

  private LocationManager lms;
  private String bestProvider = LocationManager.GPS_PROVIDER;

  private void locationServiceInital()
  {
    lms = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
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
      Toast.makeText(getActivity(),"out of service", Toast.LENGTH_LONG).show();
    }
  }

  @Override
  public void onLocationChanged(Location location)
  {
    getLocation(location);
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras)
  {

  }

  @Override
  public void onProviderEnabled(String provider)
  {

  }

  @Override
  public void onProviderDisabled(String provider)
  {

  }

  public void setTargetLocation(LatLng target, String title, String context)
  {
    map =  ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.GMap)).getMap();
    Marker nkut = map.addMarker(new MarkerOptions().position(target).title(title).snippet(context));

    // Move the camera instantly to Target  with a zoom of 16.
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(target, 12));
  }

  @Override
  public void onStart() {
    super.onStart();
    if (getService)
    {
      lms.requestLocationUpdates(bestProvider, 10000, 1, this);
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (getService)
    {
      lms.removeUpdates(this);
    }
  }

  @Override
  public void onStop() {
    super.onStop();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

}