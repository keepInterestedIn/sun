package com.bizdata.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;
@WebFilter(urlPatterns = "/*", filterName = "filter")
public class filter implements Filter{


	private FilterConfig config;
		private static String errorPath="/";// 出错跳转的目的地
		private static String[] excludePaths={".js",".css"};// 不进行拦截的url
		private static String[] safeless = {
				"<script", // 需要拦截的JS字符关键字
				"</script", "<iframe", "</iframe", "<frame", "</frame",
				"set-cookie", "%3cscript", "%3c/script", "%3ciframe", "%3c/iframe",
				"%3cframe", "%3c/frame", "src=\"javascript:", "<body", "</body",
				"%3cbody", "%3c/body",
		// "<",
		// ">",
		// "</",
		// "/>",
		// "%3c",
		// "%3e",
		// "%3c/",
		// "/%3e"
		};

		public void doFilter(ServletRequest req, ServletResponse resp,
				FilterChain filterChain) throws IOException, ServletException {
			String contentType = req.getContentType();
			System.out.println("------"+req.getContentType());
			String requestUrl = ((HttpServletRequest)req).getRequestURI();
			Map<String,String[]> map = new HashMap<String,String[]>(req.getParameterMap()); 
			for (Map.Entry<String,String[]> entry : map.entrySet()) {  
				  
			    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  
			  
			    
			} 
			if(excludeUrl(requestUrl)) {
				filterChain.doFilter(req, resp);
			} else {
				
				filterChain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest)req),resp);
			}
			/*String method = "GET";  
	        ServletRequest requestWrapper = null;    
			HttpServletRequest request = (HttpServletRequest) req;
			Enumeration params = request.getParameterNames();
			HttpServletResponse response = (HttpServletResponse) resp;
			boolean isSafe = true;
			String requestUrl = request.getRequestURI();
				requestUrl = requestUrl.substring(requestUrl.indexOf("/"));
				if(request.getContentType() != null &&request.getContentType().contains("multipart/form-data;")){
					filterChain.doFilter(request, response);
				}else{
				//if (!excludeUrl(requestUrl)) {
						if (!params.hasMoreElements()){
						    if(req instanceof HttpServletRequest) {    
				                method = ((HttpServletRequest) req).getMethod();  
				                requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) req);  //替换  
				            }    
						    String body = this.getBodyString(requestWrapper.getReader());
						    if (!isSafe(body)) {
								isSafe = false;
							}
						    if (!isSafe) {
						    	requestWrapper.setAttribute("err", "您输入的参数有非法字符，请输入正确的参数！");				
						    	//requestWrapper.getRequestDispatcher(errorPath).forward(requestWrapper, response);
								return;
							}
							filterChain.doFilter(requestWrapper, response);
					 }else{
						 
							while (params.hasMoreElements()) {
								String cache = req.getParameter((String) params.nextElement());
								if (null != cache && cache.length() > 0) {
									if (!isSafe(cache)) {
										isSafe = false;
										break;
									}
								}
							}
							if (!isSafe) {
								request.setAttribute("err", "您输入的参数有非法字符，请输入正确的参数！");				
								request.getRequestDispatcher(errorPath).forward(request, response);
								return;
							}
							filterChain.doFilter(request, response);
					 //}
					 }
				}
			*/
			
		}

		private static boolean isSafe(String str) {
			if (null != str && str.length() > 0) {
				for (String s : safeless) {
					if (str.toLowerCase().contains(s)) {
						return false;
					}
				}
			}
			return true;
		}

		private boolean excludeUrl(String url) {
			if (excludePaths != null && excludePaths.length > 0) {
				for (String path : excludePaths) {
					if (url.toLowerCase().indexOf(path)!=-1) {
						return true;
					}
				}
			}
			return false;
		}

		public void destroy() {
		}

		public void init(FilterConfig config) throws ServletException {
			this.config = config;
			errorPath = config.getInitParameter("errorPath");
			String excludePath = config.getInitParameter("excludePaths");
			if (null != excludePath && excludePath.length() > 0) {
				excludePaths = excludePath.split(",");
			}
		}
		
		private String getBody(HttpServletRequest request) throws IOException {
	        String body = null;
	        StringBuilder stringBuilder = new StringBuilder();
	        BufferedReader bufferedReader = null;
	    
	        try {
	            InputStream inputStream = request.getInputStream();
	            if (inputStream != null) {
	                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	                char[] charBuffer = new char[128];
	                int bytesRead = -1;
	                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                    stringBuilder.append(charBuffer, 0, bytesRead);
	                }
	            } else {
	                stringBuilder.append("");
	            }
	        } catch (IOException ex) {
	            throw ex;
	        }finally {
	            if(null != bufferedReader){
	                bufferedReader.close();
	            }
	        }
	        body = stringBuilder.toString();
	        return body;
	    }
		public  static String getBodyString(BufferedReader br) {  
			String inputLine;  
		     String str = "";  
		   try {  
		     while ((inputLine = br.readLine()) != null) {  
		      str += inputLine;  
		     }  
		     br.close();  
		   } catch (IOException e) {  
		     System.out.println("IOException: " + e);  
		   }  
		   return str;  
	}
}
