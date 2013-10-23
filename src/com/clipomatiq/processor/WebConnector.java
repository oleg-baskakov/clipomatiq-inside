package com.clipomatiq.processor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.net.URLEncoder;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class WebConnector {

	protected final static String PARAM_METHOD_KEY="_method";
	protected final static String PARAM_METHOD_VAL_POST="post";
	protected final static String PARAM_METHOD_VAL_GET="get";
	protected HttpClient client;

	public WebConnector() {
		// TODO Auto-generated constructor stub
	}



	protected  String connect(String url,HashMap params) {
			// TODO Auto-generated method stub
			String result="";
			if(params==null)return "";
			String method=""+params.get(PARAM_METHOD_KEY);
			if(method==null||method.length()==0)return "";
			params.remove(PARAM_METHOD_KEY);
			HttpClientParams hcp=new HttpClientParams();
			hcp.setParameter("Content-Type", "text/txt");
			if(PARAM_METHOD_VAL_GET.equalsIgnoreCase(method)){
				result=methodGet(url, params);
			}else if(PARAM_METHOD_VAL_POST.equalsIgnoreCase(method)){
				result=methodPost(url, params);
			}
			FileOutputStream fos = null;
			//System.out.println("");
/*			try {
				fos = new FileOutputStream("c:\\\\out.txt");
				PrintWriter outf = new PrintWriter(new OutputStreamWriter(fos, "cp1251"), true);

				outf.write(result);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
*/
				return result;
			
			
		}
		private String methodPost(String url, HashMap params) {
			String result="";
			PostMethod neoboxGet=new PostMethod(url);
			//neoboxGet.setParams(hcp);
			neoboxGet.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
			    		new DefaultHttpMethodRetryHandler(3, false));
			String key=new String();
			NameValuePair[] vals=new NameValuePair[params.size()];
			
			Iterator it=params.keySet().iterator();
			int j=0;
			
			while(it.hasNext()){
				key=""+it.next();
				NameValuePair pair=new NameValuePair(key,""+URLEncoder.encode(""+params.get(key)));
				vals[j++]=pair;
	//			System.out.println("parameter added:"+key+":"+params.get(key));
			}
			neoboxGet.setRequestBody(vals);

			//hcp.setParameter(HttpClientParams.HTTP_ELEMENT_CHARSET, "");
			//System.out.println("!"+neobox.getParams().getProxyHost());
			Header[] headers=neoboxGet.getRequestHeaders();
			//System.out.println("query= "+neoboxGet.getQueryString());
//			for(int i=0;i<headers.length;i++){
//				System.out.println("request:"+headers[i].getName()+":"+headers[i].getValue());
//			}
			try {
				client.executeMethod(neoboxGet);
				 headers=neoboxGet.getResponseHeaders();
//				for(int i=0;i<headers.length;i++){
//					System.out.println(""+headers[i].getName()+":"+headers[i].getValue());
//				}
				result=neoboxGet.getResponseBodyAsString();
				//System.out.println(result);
				//System.out.println();
				
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				neoboxGet.releaseConnection();
			}
			return result;
	}



		private String methodGet(String url, HashMap params) {
			
			String result="";
			GetMethod neoboxGet=new GetMethod(url);
			neoboxGet.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
			    		new DefaultHttpMethodRetryHandler(3, false));
			//neoboxGet.setParams(hcp);
			//neoboxGet.setQueryString(arg0);
			String key;
			NameValuePair[] vals=new NameValuePair[params.size()];
			
			Iterator it=params.keySet().iterator();
			int j=0;
			
			while(it.hasNext()){
				key=""+it.next();
				NameValuePair pair;
					pair = new NameValuePair(key,""+params.get(key));
//					pair = new NameValuePair(key,URLEncoder.encode(""+(params.get(key)),"UTF-8"));
				vals[j++]=pair;
	//			System.out.println("parameter added:"+key+":"+pair.getValue());
			}
			neoboxGet.setQueryString(vals);
//			System.out.println(neoboxGet.getQueryString());
			
//			System.out.println(neoboxGet.getPath());
			//hcp.setParameter(HttpClientParams.HTTP_ELEMENT_CHARSET, "");
			//System.out.println("!"+neobox.getParams().getProxyHost());
			Header[] headers=neoboxGet.getRequestHeaders();
//			for(int i=0;i<headers.length;i++){
//				System.out.println("request:"+headers[i].getName()+":"+headers[i].getValue());
//			}
			try {
				client.executeMethod(neoboxGet);
				headers=neoboxGet.getResponseHeaders();
//				for(int i=0;i<headers.length;i++){
//					System.out.println(""+headers[i].getName()+":"+headers[i].getValue());
//				}
				result=neoboxGet.getResponseBodyAsString();

				//System.out.println();
				
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				neoboxGet.releaseConnection();
			}
			return result;
	}



		protected void init(){
			client=new HttpClient();
		/*	Credentials defaultcreds = new UsernamePasswordCredentials("440796", "cbcntvfYbgtkm3");
			ProxyHost ph=new ProxyHost("ncproxy1", 8080);
			client.getState().setProxyCredentials(new AuthScope("ncproxy1", 8080, AuthScope.ANY_HOST), defaultcreds);
			client.getHostConfiguration().setProxyHost(ph);
	*/		
		}
	

}
