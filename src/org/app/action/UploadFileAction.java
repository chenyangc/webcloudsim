package org.app.action;

import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;  
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport; 

import org.apache.commons.io.IOUtils;  
import org.apache.struts2.ServletActionContext;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.Setting;
import org.app.models.SimulationRegistry;
import org.app.utils.FileIO;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public  class UploadFileAction extends ActionSupport   {
	
		
		
		private static final long serialVersionUID = -1896915260152387341L;  
		private String Filename;
		private String Upload;
	    private File uploadify;			//这里的"fileName"一定要与表单中的文件域名相同  
	    private String uploadifyContentType;//格式同上"fileName"+ContentType  
	    private String uploadifyFileName; //格式同上"fileName"+FileName  
	    private String savePath;//文件上传后保存的路径  
	    
	    public String execute() throws Exception{  
	    	try {	    		    		
				savePath = FileIO.getPathOfExecutable() + "extensions";//上传完后文件存放位置  	    		
				System.out.println(savePath);  
				HttpServletResponse response  = ServletActionContext.getResponse(); 
			    response.setCharacterEncoding("utf-8");
				
				String newsuffix = "";  
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				String time = df.format(new Date());      
				if((uploadifyFileName != null)&&(uploadifyFileName.length()>0))  
				{  
				    int dot = uploadifyFileName.lastIndexOf(".");  
				    if((dot >-1) && (dot < (uploadifyFileName.length() - 1)))  
				    {  
				         newsuffix = uploadifyFileName.substring(dot + 1);  
				    }  
				}  
				String tmpfilename=time+"."+newsuffix;
				String file=savePath+"/"+ tmpfilename ;
				File output=new File(file);
				FileInputStream fis = new FileInputStream(uploadify);  
		        FileOutputStream fos = new FileOutputStream(output);  
		        IOUtils.copy(fis, fos);  
		        fos.flush();  
		        fos.close();  
		        fis.close();  											
				System.out.println(output.getName()+"submit success");	
				String xmlpath=FileIO.getPathOfExecutable() + "extensions/classnames.xml";
				File xmlfile=new File(xmlpath);
//				String encoding = "UTF-8";//设置文件的编码！！和format不是一回事
//				OutputStreamWriter outstream = new OutputStreamWriter(new FileOutputStream(xmlfile), encoding);												
//创建dom对象
				SAXReader saxReader=new SAXReader();
				Document document=null;
				Element root=null;
				if(!xmlfile.exists()){
					document=DocumentHelper.createDocument();
					root=document.addElement("root");
				}else{
					 document=saxReader.read(xmlfile);
					 root=document.getRootElement();
				}	           	            	            		          	            	
				Element addroot = root.addElement("class");
				Element type=addroot.addElement("type");				
				Element alias=addroot.addElement("alias");
				Element name=addroot.addElement("name");
				Element filename=addroot.addElement("filename");
//				type.addAttribute("name","type");
//				alias.addAttribute("name", "alias");
//				name.addAttribute("name", "name");
//				filename.addAttribute("name","filename");
				type.setText("VmAllocationPolicy");
				alias.setText("SimpleTest");
				name.setText("VmAllocationPolicyTest");
				filename.setText(tmpfilename);
//写入xml 文件		
				Writer writer = new FileWriter(xmlfile);
				OutputFormat format =OutputFormat.createPrettyPrint();						
				format.setEncoding("UTF-8");
				XMLWriter xmlwriter = new XMLWriter(writer,format);
				xmlwriter.write(document);
				xmlwriter.flush();
				xmlwriter.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	        return null;  
	    }  
	      
	    public File getUploadify() {  
	        return uploadify;  
	    }  
	    public void setUploadify(File uploadify) {  
	        this.uploadify = uploadify;  
	    }  
	    public String getUploadifyFileName() {  
	        return uploadifyFileName;  
	    }  
	    public void setUploadifyFileName(String uploadifyFileName) {  
	        this.uploadifyFileName = uploadifyFileName;  
	    }

		public String getFilename() {
			return Filename;
		}

		public String getUpload() {
			return Upload;
		}

		public String getUploadifyContentType() {
			return uploadifyContentType;
		}

		public void setFilename(String filename) {
			Filename = filename;
		}

		public void setUpload(String upload) {
			Upload = upload;
		}

		public void setUploadifyContentType(String uploadifyContentType) {
			this.uploadifyContentType = uploadifyContentType;
		}

		public String getSavePath() {
			return savePath;
		}

		public void setSavePath(String savePath) {
			this.savePath = savePath;
		}

		
	    

} 

