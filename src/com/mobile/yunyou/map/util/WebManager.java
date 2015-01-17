package com.mobile.yunyou.map.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.location.LocationManager;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.mobile.yunyou.ServiceIPConfig;
import com.mobile.yunyou.model.BaseType;
import com.mobile.yunyou.util.CommonLog;
import com.mobile.yunyou.util.LogFactory;


public class WebManager {

	private static final CommonLog log = LogFactory.createLog();
	
	/*
	 * 通过google接口获取经纬度
	 */
	 public static Location callGear(List<CellIDInfo> cellID) {
	        if (cellID == null || cellID.size() == 0) 
	        {
	        	log.e("cellID == null || cellID.size() == 0 !!!");
	        	 return null;
	        }
	               
	        
	        DefaultHttpClient client = new DefaultHttpClient();
	            HttpPost post = new HttpPost("http://www.google.com/loc/json");
	            JSONObject holder = new JSONObject();

	            try {
	                    holder.put("version", "1.1.0");
	                    holder.put("host", "maps.google.com");
	          
//	                    holder.put("home_mobile_country_code", cellID.get(0).mobileCountryCode);
//	 	                holder.put("home_mobile_network_code", cellID.get(0).mobileNetworkCode);
	                    
	                   
	                    holder.put("radio_type", cellID.get(0).radioType);
	                    holder.put("request_address", true);
	                    if ("460".equals(cellID.get(0).mobileCountryCode)) 
	                            holder.put("address_language", "zh_CN");
	                    else
	                            holder.put("address_language", "en_US");
	                    
	                    JSONObject data,current_data;

	                    JSONArray array = new JSONArray();
	                    
	                    current_data = new JSONObject();
	                    current_data.put("cell_id", cellID.get(0).cellId);
	                    current_data.put("location_area_code", cellID.get(0).locationAreaCode);
	                    current_data.put("mobile_country_code",  Integer.valueOf(cellID.get(0).mobileCountryCode));
	                    current_data.put("mobile_network_code", Integer.valueOf(cellID.get(0).mobileNetworkCode));
	                    current_data.put("age", 0);
	                    current_data.put("signal_strength", -60);
	                    current_data.put("timing_advance", 5555);
	                    array.put(current_data);
	                    
//	                    if (cellID.size() > 2) {
//	                        for (int i = 1; i < cellID.size(); i++) {
//	                         data = new JSONObject();
//	                         data.put("cell_id", cellID.get(i).cellId);
//	                         data.put("location_area_code", cellID.get(i).locationAreaCode);
//	                         data.put("mobile_country_code", cellID.get(i).mobileCountryCode);
//	                         data.put("mobile_network_code", cellID.get(i).mobileNetworkCode);
//	                         data.put("age", 0);
//	                         array.put(data);
//	                        }
//	                       }

	                    
	                    
	                    
	                    holder.put("cell_towers", array);
	                                            
	                    StringEntity se = new StringEntity(holder.toString());
	                    log.e("Location send:" + holder.toString());
	                    post.setEntity(se);
	                    HttpResponse resp = client.execute(post);

	                    HttpEntity entity = resp.getEntity();

	                    BufferedReader br = new BufferedReader(
	                                    new InputStreamReader(entity.getContent()));
	                    StringBuffer sb = new StringBuffer();
	                    String result = br.readLine();
	                    while (result != null) {	                  
	                            sb.append(result);
	                            result = br.readLine();
	                    }
	                    
	                    log.e("Locaiton reseive-->" + sb.toString());
	                    data = new JSONObject(sb.toString());
	                  
	                    data = (JSONObject) data.get("location");

	                    Location loc = new Location(LocationManager.NETWORK_PROVIDER);
	                    loc.setLatitude((Double) data.get("latitude"));
	                    loc.setLongitude((Double) data.get("longitude"));
	                    loc.setAccuracy(Float.parseFloat(data.get("accuracy").toString()));
	                    loc.setTime( System.currentTimeMillis());//AppUtil.getUTCTime());
	                    return loc;
	            } catch (JSONException e) {
	                    e.printStackTrace();
	                    return null;
	            } catch (UnsupportedEncodingException e) {
	                    e.printStackTrace();
	            } catch (ClientProtocolException e) {
	                    e.printStackTrace();
	            } catch (IOException e) {
	                    e.printStackTrace();
	            }

	            return null;
	    }

	 
	 
	 	public static Location callGearByYunyou(List<CellIDInfo> cellID) throws IOException
	 	{
	 		    if (cellID == null || cellID.size() == 0) 
			    {
			    	log.e("cellID == null || cellID.size() == 0 !!!");
			    	 return null;
			    }
			    	
			    List<NameValuePair> params = new ArrayList<NameValuePair>();  	
				params.add(new BasicNameValuePair("lat", "0"));
				params.add(new BasicNameValuePair("lon", "0"));
				params.add(new BasicNameValuePair("lastLat", "0"));
				params.add(new BasicNameValuePair("lastLon", "0"));
				params.add(new BasicNameValuePair("rxlev1", "0"));
				
			    int size = cellID.size();
			    for(int i = 0; i < size; i++)
			    {
			    	CellIDInfo info = cellID.get(i);
			    	
			    	String arfcnString = "arfcn" + (i+1);
			    	String bsicString = "bsic" + (i+1);
			    	String ciString = "ci" + (i+1);
			    	String lacString = "lac" + (i+1);
			    	String mccString = "mcc" + (i+1);
			    	String mncString = "mnc" + (i+1);
			    	
			    	params.add(new BasicNameValuePair(arfcnString, "0"));
			    	params.add(new BasicNameValuePair(bsicString, "0"));
			    	params.add(new BasicNameValuePair(ciString, String.valueOf(info.cellId)));
			    	params.add(new BasicNameValuePair(lacString,  String.valueOf(info.locationAreaCode)));
			    	params.add(new BasicNameValuePair(mccString, info.mobileCountryCode));
			    	params.add(new BasicNameValuePair(mncString, info.mobileNetworkCode));
			    	
			    	break;
			    }
			    
			    
	    	String url = "http://www.360lbs.net:20002/yyservice/locate.do?" + URLEncodedUtils.format(params, HTTP.UTF_8);
	    	log.e("callGearByYunyou uri:" + url);
	    	
	    	
	    	DefaultHttpClient client = new DefaultHttpClient();
	        HttpGet get = new HttpGet(url);
	       
	                                        
	          
	        try {

	        	
		        	HttpResponse resp = client.execute(get);
		        	
		        	
	                HttpEntity entity = resp.getEntity();
	
	                BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
	                StringBuffer sb = new StringBuffer();
	                String result = br.readLine();
	                while (result != null) {	                  
	                        sb.append(result);
	                        result = br.readLine();
	                }
	                
	                log.e("Locaiton reseive-->" + sb.toString());
	                String results[] = sb.toString().split(",");
	                String lonString = results[1];
	                lonString.trim();
	                String latString = results[0];
	                latString.trim();
	                
	                Location loc = new Location(LocationManager.NETWORK_PROVIDER);
	                loc.setLatitude(Double.valueOf(latString));
	                loc.setLongitude(Double.valueOf(lonString));
	                loc.setTime( System.currentTimeMillis());//AppUtil.getUTCTime());
	                return loc;
				}  catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
	        
	                
	        
	        return null;
	
		} 
	 
	 
	/*
	 * 通过google接口获取地理位置
	 */
	 public static  String getAddressByGoogle(Location itude) throws Exception {
		 	if (itude == null)
		 	{
		 		return null;
		 	}
		 	
		 	
	        String resultString = "";
	 
	        /** 这里采用get方法，直接将参数加到URL上 */
	        String urlString = String.format("http://maps.google.cn/maps/geo?key=abcdefg&q=%s,%s", itude.getLatitude(), itude.getLongitude());
	    
	        /** 新建HttpClient */
	        HttpClient client = new DefaultHttpClient();
	        /** 采用GET方法 */
	        HttpGet get = new HttpGet(urlString);
	        try {
	            /** 发起GET请求并获得返回数据 */
	            HttpResponse response = client.execute(get);
	            HttpEntity entity = response.getEntity();
	            BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
	            StringBuffer strBuff = new StringBuffer();
	            String result = null;
	            while ((result = buffReader.readLine()) != null) {
	                strBuff.append(result);
	            }
	            resultString = strBuff.toString();
	 
	           
	            log.e("Address reseive-->" + resultString);
	            /** 解析JSON数据，获得物理地址 */
	            if (resultString != null && resultString.length() > 0) {
	                JSONObject jsonobject = new JSONObject(resultString);
	                JSONArray jsonArray = new JSONArray(jsonobject.get("Placemark").toString());
	                resultString = "";
	                for (int i = 0; i < jsonArray.length(); i++) {
	                    resultString = jsonArray.getJSONObject(i).getString("address");
	                }
	            }
	        } catch (Exception e) {
	            throw new Exception("获取物理位置出现错误:" + e.getMessage());
	        } finally {
	            get.abort();
	            client = null;
	        }
	        

	        log.e("resultString --> " + resultString);
	 
	        return resultString;
	}
	 

	 
	 private static long getAdressTime = 0;
	 
//	 public static synchronized String getAddressByGaoDe(Activity activity, Location itude) throws Exception {
//		 	if (itude == null)
//		 	{
//		 		return null;
//		 	}
//		 	
//		 	
////		 	
////		 	long timeMills = System.currentTimeMillis();
////		 	if (timeMills - getAdressTime < 3000)
////		 	{
////		 		
////		 		try {
////		 			Thread.sleep(3000);
////				} catch (Exception e) {
////					// TODO: handle exception
////					e.printStackTrace();
////				}
////		 	}
////		 
////		 	getAdressTime = System.currentTimeMillis();		 	
//		 	
//		 	
//
//		 	
//		 	StringBuffer addressString = new StringBuffer();
//		 	String addresCrossString = "";
//			String addresPOIString = "";
//			String addresStreetString = "";
//			String addresPOI2String = "";
//			String addresPOI3String = "";
//		 
//	 		Geocoder geocoder = new Geocoder(activity);
//	 		
//	 		List<Address> list = null;
//	 		try {			 			
//	 			list = geocoder.getFromLocation(itude.getLatitude(), itude.getLongitude(), 10);	 			
//		 		if (list.size() == 0)
//		 		{
//		 			return "can't get the adress";
//		 		}	 		
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//				log.e("com.amap.mapapi.core.AMapException");
//				return "com.amap.mapapi.core.AMapException";
//			}
//			
//	 		log.e("Address.size = " + list.size());
//	 		Address address = list.get(0);
//	 		
//	 		if (address.getAdminArea() != null)
//	 		{
//	 			addressString.append(address.getAdminArea());
//	 		}
//	 		
//	 		if (address.getLocality() != null)
//	 		{
//	 			addressString.append(address.getLocality());
//	 		}
//	 		
//	 		if(address.getSubLocality()!=null){
//	 			addressString.append(address.getSubLocality());
//			}
//	 	
//	 		
//	 		for(Address addres:list){
//				String type = addres.getPremises();//Address 类型
//			//	log.e("addres.getPremises() = " + type + ", address.getFeatureName() = " + addres.getFeatureName());	
//				
//				
//				if (type != null && type.equals(Geocoder.Cross))
//				{
//					addresCrossString = addres.getFeatureName();				
//			//		log.e("addresCrossString = " + addresCrossString);
//				}
//				
//				
//				if(type != null && type.equals(Geocoder.POI))
//				{
//					addresPOIString = addres.getFeatureName();
//			//		log.e("addresPOIString = " + addresPOIString);
//					if (addresPOI2String.length() == 0)
//					{
//						addresPOI2String = addres.getFeatureName();
//					}else if (addresPOI3String.length() == 0)
//					{
//						addresPOI3String =  addres.getFeatureName();
//					}
//				}
//		
//			
//				if(type != null && type.equals(Geocoder.Street_Road))
//				{				
//					addresStreetString = addres.getFeatureName();			
//			//		log.e("addresStreetString = " + addresStreetString);
//				}
//				
//			} 		
//
//	 		if (addresStreetString != null && addresStreetString.length() != 0)
//	 		{
//	 			addressString.append(";" + addresStreetString);
//	 		}else{
//	 			addressString.append(";" + addresPOI2String);
//	 		}
//	 		
//	 		if (addresPOIString != null && addresPOIString.length() != 0)
//	 		{
//	 			addressString.append(";" + addresPOIString);
//	 		}
//	 		
//	 		if (addresCrossString != null && addresCrossString.length() != 0)
//	 		{
//	 			addressString.append(";" + addresCrossString);
//	 		}else{
//	 			addressString.append(";" + addresPOI3String);
//	 		}
//	 	  
//	 	   log.e("addressSring --> " + addressString);
// 	   
//	 	   
//	 	   return addressString.toString();
//
//	 }
	 
	 public static String getAdressByGaodeEX(GeocodeSearch geocoderSearch, double lat, double lon){
		 String address = "";
		 
		 LatLonPoint point = new LatLonPoint(lat, lon);
		 RegeocodeQuery query = new RegeocodeQuery(point, 200, GeocodeSearch.AMAP);

			RegeocodeAddress posRegeocodeResult = null;
			try {
				posRegeocodeResult = geocoderSearch.getFromLocation(query);
				if (posRegeocodeResult != null){
					address = posRegeocodeResult.getFormatAddress();
				}
			} catch (com.amap.api.services.core.AMapException e) {
				e.printStackTrace();
			}
			
			log.e("getAdressByGaodeEX address = " + address);
		return (address != null ? address : "");
	 }
	 
		public static Location correctPosToMap(double lat, double lon) 
		{
			String urlString = ServiceIPConfig.correctIP + "?lat1=" + lat + "&lon1=" + lon;
	      //  String urlString = String.format("http://114.80.155.233:20002/yyservice/offset.do?lat1=%s&lon1=%s", lat, lon);
	        log.d("correctUrl -->" + urlString);
	        
	        /** 新建HttpClient */
	        HttpClient client = new DefaultHttpClient();
	        /** 采用GET方法 */
	        HttpGet get = new HttpGet(urlString);
	        try {
	            /** 发起GET请求并获得返回数据 */
	            HttpResponse response = client.execute(get);
	            HttpEntity entity = response.getEntity();
	            BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
	            StringBuffer strBuff = new StringBuffer();
	            String result = null;
	            while ((result = buffReader.readLine()) != null) {
	                strBuff.append(result);
	            }
	            String resultString = strBuff.toString();
	            
	            log.d("correctPos reseive-->" + resultString);
	            
	            
	            if (resultString != null && resultString.length() > 0) {
	            	  String array[] = resultString.split(",");
	            	  Location location = new Location(LocationManager.GPS_PROVIDER);
	            	  location.setLatitude(Double.valueOf(array[0]));
	            	  location.setLongitude(Double.valueOf(array[1]));
	            	  return location;
	            }
	        } catch (Exception e) {
	        	e.printStackTrace();
	            log.e("纠偏出现错误:" + e.getMessage());
	        } finally {
	            get.abort();
	            client = null;
	        }
	 
	        return null;
		}
		
		
		public static List<BaseType.BaseLocation> correctPosToMap(List<BaseType.BaseLocation> list) 
		{
			if (list.size() == 0)
			{
				return null;
			}

	    //   String urlString = String.format("http://www.360lbs.net:20002/yyservice/offset.do");
	        String urlString = ServiceIPConfig.correctIP;
	        
	        StringBuffer stringBuffer = new StringBuffer();
	        int size = list.size();
	        for(int i = 0; i < size; i++)
	        {
	        	stringBuffer.append("lat" + (i + 1) + "=" + list.get(i).lat + "&");
	        	stringBuffer.append("lon" + (i + 1) + "=" + list.get(i).lon + "&");
	        }
	        
	        String contentString =  stringBuffer.substring(0, stringBuffer.length() - 1);
	        log.d("list correctUrl -->" + urlString);
	        log.d("list contentString -->" + contentString);
	     
	        try {
	        	URL url = new URL(urlString);
	 	        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();		
	 			urlConn.setConnectTimeout(5000);
	 			urlConn.setRequestMethod("POST");
	 			urlConn.setDoInput(true);
	 			urlConn.setDoOutput(true);
	 			urlConn.setUseCaches(false);
	 			OutputStream out = urlConn.getOutputStream();
	 			out.write(contentString.getBytes("utf-8"));
	 			out.flush();
	 			out.close();

	 		
	 			ByteArrayOutputStream bos = null;
	 			try {
	 				bos = new ByteArrayOutputStream();
	 				InputStream in = urlConn.getInputStream();
	 				int i=-1;
	 				byte[] block = new byte[8192];
	 				while ((i = in.read(block)) != -1) {
	 					bos.write(block,0,i);
	 					block = new byte[8192];
	 				}
	 			} finally {
	 				if (bos != null) {
	 					bos.close();
	 				}
	 			}
	 			
	 			if (bos == null)
	 			{
	 				return null;
	 			}
	 			
	 			String resultString = bos.toString();
	 	            
	 	        log.d("correctPos reseive-->" + resultString);
	 	            
	             if (resultString.length() == 0)
	             {
	             	log.e("resultString.length() == 0");
	             	return null;
	             }
	 	          
	             List<BaseType.BaseLocation> resultList = new ArrayList<BaseType.BaseLocation>();
	             String locationArray[] = resultString.split(";");
	             int size1 = locationArray.length;
	             for(int i = 0; i < size; i++)
	             {
	             	  String array[] = locationArray[i].split(",");
	             	  BaseType.BaseLocation object = new BaseType.BaseLocation();
	             	  object.lat = Double.valueOf(array[0]);
	             	  object.lon = Double.valueOf(array[1]);
	             	  resultList.add(object);
	 	         }	
	             
	 	         return resultList;           
	             
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			     log.e("纠偏出现错误:" + e.getMessage());
			}
	 
	        return null;
		}
		
//	public static Location correctPosToMap(Location location,Context context) 
//	{
//		if (location == null)
//		{
//			return null;
//		}
//	
//        String urlString = String.format("http://huizhilian.gicp.net:20002/yyservice/offset.do?lat1=%s&lon1=%s", location.getLatitude(), location.getLongitude());
//        log.e("correctUrl -->" + urlString);
//        
//        /** 新建HttpClient */
//        HttpClient client = new DefaultHttpClient();
//        /** 采用GET方法 */
//        HttpGet get = new HttpGet(urlString);
//        try {
//            /** 发起GET请求并获得返回数据 */
//            HttpResponse response = client.execute(get);
//            HttpEntity entity = response.getEntity();
//            BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
//            StringBuffer strBuff = new StringBuffer();
//            String result = null;
//            while ((result = buffReader.readLine()) != null) {
//                strBuff.append(result);
//            }
//            String resultString = strBuff.toString();
//            
//            log.e("correctPos reseive-->" + resultString);
//            
//            
//            if (resultString != null && resultString.length() > 0) {
//            	  String array[] = resultString.split(",");
//            	
//            	  location.setLatitude(Double.valueOf(array[0]));
//            	  location.setLongitude(Double.valueOf(array[1]));
//            	  return location;
//            }
//        } catch (Exception e) {
//        	e.printStackTrace();
//            log.e("纠偏出现错误:" + e.getMessage());
//        } finally {
//            get.abort();
//            client = null;
//        }
// 
//        return null;
//	}
}
