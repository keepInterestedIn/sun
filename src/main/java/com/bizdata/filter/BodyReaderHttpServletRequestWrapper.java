package com.bizdata.filter;
import java.io.BufferedReader;  
import java.io.ByteArrayInputStream;  
import java.io.IOException;  
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletRequestWrapper;  
  
import jodd.JoddDefault;  
import jodd.io.StreamUtil;  
  
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {  
	private final byte[] body;  
    
    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request)   
throws IOException {  
        super(request);  
        body = StreamUtil.readBytes(request.getReader(), JoddDefault.encoding);  
    }  
  
    @Override  
    public BufferedReader getReader() throws IOException {  
        return new BufferedReader(new InputStreamReader(getInputStream()));  
    }  
  
    @Override  
    public ServletInputStream getInputStream() throws IOException {  
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);  
        return new ServletInputStream() {  
  
            @Override  
            public int read() throws IOException {  
                return bais.read();  
            }

			@Override
			public boolean isFinished() {
				// TODO 自动生成的方法存根
				return false;
			}

			@Override
			public boolean isReady() {
				// TODO 自动生成的方法存根
				return false;
			}

			@Override
			public void setReadListener(ReadListener listener) {
				// TODO 自动生成的方法存根
				
			}  
        };  
    }  
  
}  