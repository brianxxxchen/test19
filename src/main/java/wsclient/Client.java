package wsclient;

import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;

import net.mahtabalam.ws.Country;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class Client {
       
   public static void main(String[] args) throws Exception {
	   String wsdl = "http://localhost:8080/SOAPWithJaxWS/discoverIndia?wsdl"; 
		int timeout = 10000; 
		StringBuffer sb = new StringBuffer("");
	   
	   sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"); 
		sb.append("<soap:Envelope " + 
		"xmlns:api='http://ws.mahtabalam.net/' " + 
		"xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " + 
		"xmlns:xsd='http://www.w3.org/2001/XMLSchema' " + 
		"xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>"); 
		sb.append("<soap:Body>"); 
		//sb.append("<api:sayHello>");
		sb.append("<api:getCapital>"); 
		sb.append("<arg0>Kerala</arg0>"); 
		sb.append("</api:getCapital>"); 
		sb.append("</soap:Body>"); 
		sb.append("</soap:Envelope>");
		
		System.out.println("請求xml:" + sb.toString());
		
		/*
		<?xml version="1.0" encoding="UTF-8"?>
		<soap:Envelope xmlns:api='http://ws.mahtabalam.net/' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>
			<soap:Body>
				<api:getCapital><arg0>Kerala</arg0></api:getCapital>
			</soap:Body>
		</soap:Envelope>
		
		<?xml version='1.0' encoding='UTF-8'?>
		<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
			<S:Body>
				<ns2:getCapitalResponse xmlns:ns2="http://ws.mahtabalam.net/">
					<return>Thiruvananthapuram_陳牧生</return>
				</ns2:getCapitalResponse>
			</S:Body>
		</S:Envelope>
		 */
		
		// HttpClient發送SOAP請求 
		System.out.println("HttpClient發送SOAP請求"); 
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(wsdl); 
		//  設置連接超時 
		client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout); 
		// 設置讀取時間超時 
		client.getHttpConnectionManager().getParams().setSoTimeout(timeout); 
		// 然後把Soap請求數據添加到PostMethod中 
		RequestEntity requestEntity = new StringRequestEntity(sb.toString(), "text/xml", "UTF-8"); 
		//設置請求頭部，否則可能會報 「no SOAPAction header」 的錯誤 
		postMethod.setRequestHeader("SOAPAction",""); 
		// 設置請求體 
		postMethod.setRequestEntity(requestEntity);
		int status = client.executeMethod(postMethod); 
		// 列印請求狀態碼 
		System.out.println("status:" + status); 
		// 獲取響應體輸入流 
		InputStream is = postMethod.getResponseBodyAsStream(); 
		// 獲取請求結果字符串 
		String result = IOUtils.toString(is).trim();
		System.out.println("result:" + result);
		
		URL wsdlUrl = new URL("http://localhost:8080/SOAPWithJaxWS/discoverIndia?wsdl");
		QName qname = new QName("http://ws.mahtabalam.net/", "CountryImplService");
		
		Service service = Service.create(wsdlUrl, qname);
		Country countryInterface = service.getPort(Country.class);
		
		System.out.println("Capital of Kerala:" + countryInterface.getCapital("Kerala"));
	   
	   /*
       CountryImplService service=new CountryImplService();          
       CountryImpl country=service.getCountryImplPort();          
       System.out.println("Capital of Kerala : "+country.getCapital("Kerala"));
       System.out.println("No. of districts in Telangna : "+country.getDistricts("Telangna"));
       System.out.println("Local languages of Gujrat : "+country.getLanguages("Gujrat"));
       System.out.println("Airports in Uttrakhand : "+country.getAirports("Uttrakhand"));
       System.out.println("Places to visit in Rajasthan : "+country.getPlacesToVisit("Rajasthan"));
       System.out.println("Interesting Facts about Meghalaya : "+country.getInterestingFacts("Meghalaya"));
       */        
   }
}
