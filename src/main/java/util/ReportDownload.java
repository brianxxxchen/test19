package util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.engine.util.JRLoader;

import java.io.FileOutputStream;
import java.io.StringReader;



import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

// import com.google.gson.Gson;
import java.math.BigDecimal;
import java.util.Arrays;

@WebServlet("/ReportDownload")
public class ReportDownload extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String startDate = "2020/01/02";
		String endDate = "2020/01/03";
        
        try {
            JRDataSource dataSource = null;
            
            List<Map<String, Object>> personList = new ArrayList<Map<String, Object>>();
    		
    		Map<String, Object> map1 = new HashMap<String, Object>();
    		map1.put("id", "4");
    		map1.put("name", "aa木");
    		map1.put("age", new Integer(1));
    		map1.put("salary", new Double("200.22"));
    		
    		Map<String, Object> map2 = new HashMap<String, Object>();
    		map2.put("id", "5");
    		map2.put("name", "bb木");
    		map2.put("age", new Integer(11));
    		map2.put("salary", new Double("300.22"));
    		
    		Map<String, Object> map3 = new HashMap<String, Object>();
    		map3.put("id", "6");
    		map3.put("name", "cc木");
    		map3.put("age", new Integer(12));
    		map3.put("salary", new Double("400.22"));
    		
    		Map<String, Object> map4 = new HashMap<String, Object>();
    		map4.put("id", "7");
    		map4.put("name", "aa木生");
    		map4.put("age", new Integer(1));
    		map4.put("salary", new Double("11.33"));
    		
    		Map<String, Object> map5 = new HashMap<String, Object>();
    		map5.put("id", "8");
    		map5.put("name", "bb木生");
    		map5.put("age", new Integer(11));
    		map5.put("salary", new Double("12.44"));
    		
    		Map<String, Object> map6 = new HashMap<String, Object>();
    		map6.put("id", "9");
    		map6.put("name", "cc木生");
    		map6.put("age", new Integer(12));
    		map6.put("salary", new Double("13.55"));
    		
    		Map<String, Object> map7 = new HashMap<String, Object>();
    		map7.put("id", "15");
    		map7.put("name", "dd木生");
    		map7.put("age", new Integer(1));
    		map7.put("salary", new Double("18.66"));
    		
    		personList.add(map1);
    		personList.add(map2);
    		personList.add(map3);
    		personList.add(map4);
    		personList.add(map5);
    		personList.add(map6);
    		personList.add(map7);
    		
            /*
    		Collections.sort(personList, new Comparator<Map<String, Object>>() {
    	        @Override
    	        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
    	            if(null != o1.get("age") && null != o1.get("age")){
    	            	Integer al = (Integer)o1.get("age");
    	            	Integer a2 = (Integer)o2.get("age");
    	            	return al.compareTo(a2);
    	                //return o2.get("age").toString().compareTo(o1.get("age").toString());
    	            }else if(null != o1.get("age")){
    	                return 1;
    	            }else{
    	                return -1;
    	            }
    	        }
    	    });
    	    */
    		
    		int size = personList.size();
    		if(size > 0) {
    			dataSource = new JRBeanCollectionDataSource(personList);
    		}else {
    			dataSource = new JREmptyDataSource();
    		}

            Map parametros = new HashMap();
            parametros.put ("searchDate", startDate + "~" + endDate);
            //parametros.put("ot_entidad_id", request.getSession().getAttribute("ot_entidad_id"));
            
            List<JRSortField> sortList = new ArrayList<JRSortField>();
            JRDesignSortField sortField = new JRDesignSortField();
            sortField.setName("age");
            sortField.setOrder(SortOrderEnum.ASCENDING);
            sortField.setType(SortFieldTypeEnum.FIELD);
            sortList.add(sortField);
            //add other sortfields here
            parametros.put(JRParameter.SORT_FIELDS, sortList);
            
            //JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile("D:/AA/ss/IReport/pr2/report1.jasper");
            JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile("D:/AA/workspace/wk_D/jsaperReport11/src/main/webapp/report1.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parametros, dataSource);
            
            JRExporter exporter = new JRPdfExporter();
            ServletOutputStream out = response.getOutputStream();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
            exporter.exportReport();
			
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/pdf");

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	

}
