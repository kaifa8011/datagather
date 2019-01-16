package com.ciba.datagather.util.device;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.ciba.datagather.entity.CustomLocation;
import com.ciba.datagather.common.DataGatherManager;
import com.ciba.datagather.util.DataGatherLog;
import com.ciba.datasynchronize.manager.DataCacheManager;
import com.ciba.datasynchronize.manager.LoaderUploaderManager;

import java.util.List;
import java.util.Locale;

/**
 * @author : ciba
 * @date : 2018/7/24
 * @description : 地理位置信息工具类
 */

public class LocationUtil {
    private static final long LOCATION_INTERVALS = 7 * 24 * 60 * 60 * 1000;

    public static CustomLocation getCustomLocation(boolean geocoder) {
        String country = "CN";
        double lat = 0.0;
        double lng = 0.0;
        try {
            String latStr = DataCacheManager.getInstance().getLat();
            if (!TextUtils.isEmpty(latStr)) {
                String lngStr = DataCacheManager.getInstance().getLng();
                if (!TextUtils.isEmpty(lngStr)) {
                    lat = Double.parseDouble(latStr);
                    lng = Double.parseDouble(lngStr);
                }
            }
        } catch (Exception e) {
            DataGatherLog.innerI(e.getMessage());
        }
        CustomLocation customLocation = new CustomLocation(lat, lng, country, System.currentTimeMillis(), 0.0f);
        try {
            int per = ContextCompat.checkSelfPermission(DataGatherManager.getInstance().getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
            int per2 = ContextCompat.checkSelfPermission(DataGatherManager.getInstance().getContext(), Manifest.permission.ACCESS_FINE_LOCATION);

            if (per == PackageManager.PERMISSION_GRANTED) {
                LocationManager locationManager = (LocationManager) DataGatherManager.getInstance().getContext().getSystemService(Context.LOCATION_SERVICE);
                if (per2 == PackageManager.PERMISSION_GRANTED && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    // 从gps获取经纬度
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        changedLocationInfo(location, customLocation);
                    } else {
                        Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        changedLocationInfo(locationNet, customLocation);
                    }
                } else {
                    // 从网络获取经纬度
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    changedLocationInfo(location, customLocation);
                }
                if (0 != customLocation.getLat() && 0 != customLocation.getLng()) {
                    DataCacheManager.getInstance().saveLngAndLat(customLocation.getLng(), customLocation.getLat());
                } else {
                    customLocation.setCoordinateType(3);
                    customLocation.setTime(System.currentTimeMillis());
                }
            } else {
                customLocation.setCoordinateType(3);
                customLocation.setTime(System.currentTimeMillis());
            }
            // 获取当前地址信息，逆地理编码需要一些时间
            if (geocoder) {
                Geocoder gc = new Geocoder(DataGatherManager.getInstance().getContext(), Locale.getDefault());
                List<Address> locationList = gc.getFromLocation(customLocation.getLat(), customLocation.getLng(), 1);
                if (locationList != null && locationList.size() > 0) {
                    Address address = locationList.get(0);
                    country = address.getCountryName() + "_" + address.getCountryCode();
                    customLocation.setCountry(country);
                }
            }
        } catch (Exception e) {
            DataGatherLog.innerI(e.getMessage());
        }
        if (customLocation.getLat() == 0 || customLocation.getLat() == 0
                || System.currentTimeMillis() - DataCacheManager.getInstance().getLngLatTime() >= LOCATION_INTERVALS) {
            // 如果经纬度有一个为0或者本地经纬度已经超过间隔时间则需要自己想办法获取经纬度
            loaderLocation();
        }
        return customLocation;
    }

    /**
     * 将系统的的Location数据转换成自动以的Location数据
     */
    private static void changedLocationInfo(Location location, CustomLocation customLocation) {
        if (location != null) {
            if (location.getLatitude() != 0 && location.getLongitude() != 0) {
                customLocation.setLat(location.getLatitude());
                customLocation.setLng(location.getLongitude());
            }
            customLocation.setTime(location.getTime());
            customLocation.setAccuracy(location.getAccuracy());
        }
    }

    /**
     * 没有获取到位置信息，交给用户自行处理
     */
    private static void loaderLocation() {
        LoaderUploaderManager.getInstance().loaderLocation();
    }
}
