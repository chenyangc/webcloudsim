package org.app.action;

import java.util.Map;

import org.app.dao.SimulationRegistryDAO;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ProviderGeneralInfoAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5137030665881761985L;
	private int numberDatacenters = 0;
	private int numberHosts = 0;
	private double numberProcessingUnits = 0;
	private double processingCapacity = 0;
	private long storageCapacity = 0;
	private int totalAmountRam = 0;
	
	public String execute() throws Exception{
		try{
			ActionContext ctx = ActionContext.getContext();
			Map<String, Object> session = ctx.getSession();
			
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			
			this.numberDatacenters = simReg.getDatacenterList().size();
			
			int number1 = 0;
			int number2 = 0;
			int number3 = 0;
			int number4 = 0;
			int number5 = 0;
			
			for(int i = 0; i < simReg.getDatacenterList().size(); ++i){
				
				number1 += simReg.getDatacenterList().get(i).getHostList().size();
				
				for(int j = 0; j < simReg.getDatacenterList().get(i).getHostList().size(); ++j){
					number2 += simReg.getDatacenterList().get(i).getHostList().get(j).getNumOfPes();
					
					number3 += (simReg.getDatacenterList().get(i).getHostList().get(j).getNumOfPes()) * (simReg.getDatacenterList().get(i).getHostList().get(j).getMipsPerPe());
					
					number4 += simReg.getDatacenterList().get(i).getHostList().get(j).getStorage();
					
					number5 += simReg.getDatacenterList().get(i).getHostList().get(j).getRam();
				}
			}
			
			this.numberHosts = number1;
			this.numberProcessingUnits = number2;
			this.processingCapacity = number3;
			this.storageCapacity = number4;
			this.totalAmountRam = number5;
		}
		catch(Exception ex){
			
		}
		
		return "success";
	}

	public int getNumberDatacenters() {
		return numberDatacenters;
	}

	public int getNumberHosts() {
		return numberHosts;
	}

	public double getNumberProcessingUnits() {
		return numberProcessingUnits;
	}

	public double getProcessingCapacity() {
		return processingCapacity;
	}

	public long getStorageCapacity() {
		return storageCapacity;
	}

	public int getTotalAmountRam() {
		return totalAmountRam;
	}
	
}
