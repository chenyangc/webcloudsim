package org.app.action;

import java.util.Map;

import org.app.dao.CustomerRegistryDAO;
import org.app.dao.DatacenterRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.CustomerRegistry;
import org.app.models.DatacenterRegistry;
import org.app.models.SimulationRegistry;
import org.app.models.UtilizationProfile;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class UpdateCustomerUtilizationAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6678954163999189992L;
	
	private int statusCode;
	
	private String datacenterBroker;
	
	private String customerRely;
	
	private int cloudletsNumber;
	
	private long maxLength;
	
	private long fileSize;
	
	private long outputSize;
	        
    private String cpuModel;
    
   
    private String ramModel;
    
 
    private String bwModel;
    
    private String location;

	public String execute() throws Exception{
		try{
			ActionContext ctx = ActionContext.getContext();
			Map<String, Object> session = ctx.getSession();
			
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			
			int customerId = (int) session.get("customerId");			
			CustomerRegistryDAO crDAO=new CustomerRegistryDAO();
			CustomerRegistry cr=crDAO.getCustomerRegistry(simReg, customerId);
			cr.setReliedCustomerName(customerRely);
			UtilizationProfile up=new UtilizationProfile(datacenterBroker,cloudletsNumber,maxLength,fileSize,outputSize,cpuModel,ramModel,bwModel);
	
			cr.setUtilizationProfile(up);
			cr.setLocation(location);
			System.out.println("!!!"+location);
			simRegDAO.updateSimulationRegistry(simReg);
			
			this.statusCode = 1;
		}
    	catch(Exception ex){
    		this.statusCode = 0;
    	}
		
    	return "success";
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getDatacenterBroker() {
		return datacenterBroker;
	}

	public String getCustomerRely() {
		return customerRely;
	}

	public int getCloudletsNumber() {
		return cloudletsNumber;
	}

	public long getMaxLength() {
		return maxLength;
	}

	public long getFileSize() {
		return fileSize;
	}

	public long getOutputSize() {
		return outputSize;
	}

	public String getCpuModel() {
		return cpuModel;
	}

	public String getRamModel() {
		return ramModel;
	}

	public String getBwModel() {
		return bwModel;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public void setDatacenterBroker(String datacenterBroker) {
		this.datacenterBroker = datacenterBroker;
	}

	public void setCustomerRely(String customerRely) {
		this.customerRely = customerRely;
	}

	public void setCloudletsNumber(int cloudletsNumber) {
		this.cloudletsNumber = cloudletsNumber;
	}

	public void setMaxLength(long maxLength) {
		this.maxLength = maxLength;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public void setOutputSize(long outputSize) {
		this.outputSize = outputSize;
	}

	public void setCpuModel(String cpuModel) {
		this.cpuModel = cpuModel;
	}

	public void setRamModel(String ramModel) {
		this.ramModel = ramModel;
	}

	public void setBwModel(String bwModel) {
		this.bwModel = bwModel;
	}
	
	
}
