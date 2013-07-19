/* Copyright 2009, 2012 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */


package com.predic8.membrane.core.interceptor;

import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import com.googlecode.jatl.Html;
import com.predic8.membrane.annot.MCAttribute;
import com.predic8.membrane.annot.MCElement;
import com.predic8.membrane.core.exchange.Exchange;
import com.predic8.membrane.core.http.Header;
import com.predic8.membrane.core.http.MimeType;
import com.predic8.membrane.core.http.Response;

@MCElement(name="counter")
public class CountInterceptor extends AbstractInterceptor {

	private static Log log = LogFactory.getLog(CountInterceptor.class.getName());

	private int counter;
	
	public CountInterceptor() {
		name = "Count Interceptor";		
	}
	
	@Override
	public Outcome handleRequest(Exchange exc) throws Exception {
		log.debug(""+ (++counter) +". request received.");
		exc.setResponse(Response.ok().header(Header.CONTENT_TYPE, MimeType.TEXT_HTML_UTF8).body(getPage()).build());
		return Outcome.RETURN;
	}

	private String getPage() throws UnknownHostException {
		StringWriter writer = new StringWriter();
		new Html(writer) {{
				html();
					head();
						title().text(name).end();
					end();
					body();
						h1().text(name+"("+InetAddress.getLocalHost().getHostAddress()+")").end();
						p().text("This site is generated by a simple interceptor that counts how often this site was requested.").end();
						p().text("Request count: "+counter).end();
				endAll(); 
				done();	
			}
		};
		return writer.toString();
	}
	
	@Override
	public String getHelpId() {
		return "counter";
	}
	
	@Override
	public String getDisplayName() {
		return "Counter: " + super.getDisplayName();
	}
	
	/**
	 * @description Sets the name that will be displayed on the web page.
	 * @example Mock Node 1
	 */
	@Required
	@MCAttribute(attributeName="name")
	@Override
	public void setDisplayName(String name) {
		super.setDisplayName(name);
	}

}
