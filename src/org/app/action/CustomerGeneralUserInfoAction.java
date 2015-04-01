package org.app.action;

import java.util.Map;

import org.app.dao.SimulationRegistryDAO;
import org.app.models.CustomerRegistry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class CustomerGeneralUserInfoAction  extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int numberOfService;
	
	private int numberOfCloudletEachService;
	
	private long averageLengthOfCloudlet;
	
	private long averageFileSizeOfCloudlet;
	
	private long averageOutputOfCloudlet;
	
	public String execute() throws Exception{
		ActionContext ctx = ActionContext.getContext();
		Map<String, Object> session = ctx.getSession();
		
		try{
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			
			this.numberOfService=simReg.getCustomerList().size();
			long amountLength=0;
			long amountFileSize=0;
			long amountOutputSize=0;
			for(CustomerRegistry cr:simReg.getCustomerList()){
					numberOfCloudletEachService=cr.getUtilizationProfile().getNumOfCloudlets();	
					amountLength+=cr.getUtilizationProfile().getLength();
					amountFileSize+=cr.getUtilizationProfile().getFileSize();
					amountOutputSize+=cr.getUtilizationProfile().getOutputSize();
				
			}
			this.averageFileSizeOfCloudlet=amountFileSize/this.numberOfService;
			this.averageLengthOfCloudlet=amountLength/this.numberOfService;
			this.averageOutputOfCloudlet=amountOutputSize/this.numberOfService;
		}
		catch(Exception ex){
			
		}
		
		return "success";
	}


	
	public int getNumberOfCloudletEachService() {
		return numberOfCloudletEachService;
	}



	public void setNumberOfCloudletEachService(int numberOfCloudletEachService) {
		this.numberOfCloudletEachService = numberOfCloudletEachService;
	}



	public int getNumberOfService() {
		return numberOfService;
	}



	public void setNumberOfService(int numberOfService) {
		this.numberOfService = numberOfService;
	}



	public long getAverageLengthOfCloudlet() {
		return averageLengthOfCloudlet;
	}

	public long getAverageFileSizeOfCloudlet() {
		return averageFileSizeOfCloudlet;
	}

	public long getAverageOutputOfCloudlet() {
		return averageOutputOfCloudlet;
	}

	public void setAverageLengthOfCloudlet(long averageLengthOfCloudlet) {
		this.averageLengthOfCloudlet = averageLengthOfCloudlet;
	}

	public void setAverageFileSizeOfCloudlet(long averageFileSizeOfCloudlet) {
		this.averageFileSizeOfCloudlet = averageFileSizeOfCloudlet;
	}

	public void setAverageOutputOfCloudlet(long averageOutputOfCloudlet) {
		this.averageOutputOfCloudlet = averageOutputOfCloudlet;
	}
	
	
}

