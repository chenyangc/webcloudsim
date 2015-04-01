package org.app.action;

import java.util.Map;

import org.app.dao.DatacenterRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.DatacenterRegistry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ProviderSettingsAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7399780890870791124L;
	
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
			
			int id = (int)session.get("providerId");
			DatacenterRegistryDAO dataRegDAO = new DatacenterRegistryDAO();
			DatacenterRegistry dataReg = dataRegDAO.getDatacenterRegistry(simReg, id);
			
			this.allocationPolicyAlias = dataReg.getAllocationPolicyAlias();
			this.architecture = dataReg.getArchitecture();
			this.lowerUtilizationThreshold = dataReg.getLowerUtilizationThreshold();
			this.monitoringInterval = dataReg.getMonitoringInterval();
			this.os = dataReg.getOs();
			this.schedulingInterval = dataReg.getSchedulingInterval();
			this.upperUtilizationThreshold = dataReg.getUpperUtilizationThreshold();
			this.vmm = dataReg.getVmm();
			this.vmMigration = dataReg.isVmMigration();
			
		}
		catch(Exception ex){
			
		}
		
		return "success";
	}

	public String getArchitecture() {
		return architecture;
	}

	public String getOs() {
		return os;
	}

	public String getVmm() {
		return vmm;
	}

	public String getAllocationPolicyAlias() {
		return allocationPolicyAlias;
	}

	public boolean isVmMigration() {
		return vmMigration;
	}

	public double getUpperUtilizationThreshold() {
		return upperUtilizationThreshold;
	}

	public double getLowerUtilizationThreshold() {
		return lowerUtilizationThreshold;
	}

	public double getSchedulingInterval() {
		return schedulingInterval;
	}

	public double getMonitoringInterval() {
		return monitoringInterval;
	}
}
