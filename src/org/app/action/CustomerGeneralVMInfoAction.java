package org.app.action;

import java.util.Map;

import org.app.dao.CustomerRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.CustomerRegistry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class CustomerGeneralVMInfoAction  extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int numberOfVM;
		
	private long averageSize;
	
	private long averageRAM;
	
	private long averageBandwidth;
	
	public String execute() throws Exception{
		ActionContext ctx = ActionContext.getContext();
		Map<String, Object> session = ctx.getSession();
		
		try{
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			CustomerRegistryDAO crDAO=new CustomerRegistryDAO();
			this.numberOfVM=(int) crDAO.getTotalNumOfVms(simReg);
			this.averageBandwidth= crDAO.getAvgBw(simReg);
			this.averageRAM=crDAO.getAvgRAM(simReg);
			this.averageSize=crDAO.getAvgImageSize(simReg);
		}
		catch(Exception ex){
			
		}
		
		return "success";
	}

	public int getNumberOfVM() {
		return numberOfVM;
	}

	public long getAverageSize() {
		return averageSize;
	}

	public long getAverageRAM() {
		return averageRAM;
	}

	public long getAverageBandwidth() {
		return averageBandwidth;
	}

	public void setNumberOfVM(int numberOfVM) {
		this.numberOfVM = numberOfVM;
	}

	public void setAverageSize(long averageSize) {
		this.averageSize = averageSize;
	}

	public void setAverageRAM(long averageRAM) {
		this.averageRAM = averageRAM;
	}

	public void setAverageBandwidth(long averageBandwidth) {
		this.averageBandwidth = averageBandwidth;
	}
	
	
	
}

