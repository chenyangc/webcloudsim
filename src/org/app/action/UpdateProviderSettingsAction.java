package org.app.action;

import java.util.Map;

import org.app.dao.DatacenterRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.DatacenterRegistry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class UpdateProviderSettingsAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6678954163999189992L;
	
	private int statusCode;
	
	/** The datacenter's architecture. */
    private String architecture;
    
    /** The datacenter's operating system. */
    private String os;
    
    /** The datacenter's hypervisor. */
    private String vmm;
    
    /** The alias of the datacenter's allocation policy. */
    private String allocationPolicyAlias;
    
    /** Indicates whether virtual machines migrations are enabled or not. */
    private boolean vmMigration;
    
    /** The upper utilization threshold of the datacenter. */
    private double upperUtilizationThreshold;
    
    /** The lower utilization threshold of the datacenter. */
    private double lowerUtilizationThreshold;
    
    /** The datacenter's scheduling interval. */
    private double schedulingInterval;
    
    /** The datacenter's monitoring interval. */
    private double monitoringInterval;

	public String execute() throws Exception{
		try{
			ActionContext ctx = ActionContext.getContext();
			Map<String, Object> session = ctx.getSession();
			
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			
			int providerId = (int) session.get("providerId");
			
			DatacenterRegistryDAO dataRegDAO = new DatacenterRegistryDAO();
			DatacenterRegistry dataReg = dataRegDAO.getDatacenterRegistry(simReg, providerId);
			
			dataReg.setAllocationPolicyAlias(allocationPolicyAlias);
			dataReg.setArchitecture(architecture);
			dataReg.setOs(os);
			dataReg.setVmm(vmm);
			
			dataReg.setVmMigration(vmMigration);
			
			dataReg.setSchedulingInterval(schedulingInterval);
			dataReg.setLowerUtilizationThreshold(lowerUtilizationThreshold);
			dataReg.setMonitoringInterval(monitoringInterval);
			dataReg.setUpperUtilizationThreshold(upperUtilizationThreshold);
			System.out.println("传过来的值为" + os + vmm + "和" + vmMigration + "以及" + monitoringInterval);
			dataRegDAO.updateDatacenterRegistry(simReg, dataReg);
			simRegDAO.updateSimulationRegistry(simReg);
			System.out.println(providerId + "\n已改为----------" + dataReg.getVmm()+ "和" + dataReg.isVmMigration() + "------------\n");
			this.statusCode = 1;
		}
    	catch(Exception ex){
    		this.statusCode = 0;
    	}
		
    	return "success";
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setArchitecture(String architecture) {
		this.architecture = architecture;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public void setVmm(String vmm) {
		this.vmm = vmm;
	}

	public void setAllocationPolicyAlias(String allocationPolicyAlias) {
		this.allocationPolicyAlias = allocationPolicyAlias;
	}

	public void setVmMigration(boolean vmMigration) {
		this.vmMigration = vmMigration;
	}

	public void setUpperUtilizationThreshold(double upperUtilizationThreshold) {
		this.upperUtilizationThreshold = upperUtilizationThreshold;
	}

	public void setLowerUtilizationThreshold(double lowerUtilizationThreshold) {
		this.lowerUtilizationThreshold = lowerUtilizationThreshold;
	}

	public void setSchedulingInterval(double schedulingInterval) {
		this.schedulingInterval = schedulingInterval;
	}

	public void setMonitoringInterval(double monitoringInterval) {
		this.monitoringInterval = monitoringInterval;
	}
}
