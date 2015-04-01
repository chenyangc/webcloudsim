package org.app.action;

import java.util.List;
import java.util.Map;

import org.app.dao.DatacenterRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.DatacenterRegistry;
import org.app.models.HostRegistry;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class UpdateProviderHostsAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2629711753005881697L;
	
	/** The host's id. */
    private long id;
    
    /** The host's scheduling policy. */
    private String schedulingPolicyAlias;
    
    /** The host's number of processing elements. */
    private int numOfPes;
    
    /** The amount of mips per processing units. */
    private double mipsPerPe;
    
    /** The maximum power consumption. */
    private double maxPower;
    
    /** The static power consumption percent. */
    private double staticPowerPercent;
    
    /** The host's power model. */
    private String powerModelAlias;
    
    /** The amount of RAM. */
    private int ram;
    
    /** The host's RAM provisioner. */
    private String ramProvisionerAlias;
    
    /** The amount of bandwidth. */
    private int bw;
    
    /** The host's bandwidth provisioner. */
    private String bwProvisionerAlias;
    
    /** The amount of hosts with this registry's specification
     * owned by the datacenter. */
    private int amount;
    
    /** The storage capacity. */
    private long storage;
    
    /** The processing elements provisioner. */
    private String peProvisionerAlias;
    

	private int statusCode;

	public String execute() throws Exception{
		try{
			ActionContext ctx = ActionContext.getContext();
			Map<String, Object> session = ctx.getSession();
			
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			
			int providerId = (int) session.get("providerId");
			
			DatacenterRegistryDAO dataRegDAO = new DatacenterRegistryDAO();
			DatacenterRegistry dataReg = dataRegDAO.getDatacenterRegistry(simReg, providerId);
			
			List<HostRegistry> listOfHost = dataReg.getHostList();
			System.out.println("-----------传过来的id为" + id);
			for(HostRegistry i:listOfHost){
				if(i.getId() == this.id){
					i.setAmount(amount);
					i.setBw(bw);
					i.setBwProvisionerAlias(bwProvisionerAlias);
					i.setMaxPower(maxPower);
					i.setMipsPerPe(mipsPerPe);
					i.setNumOfPes(numOfPes);
					i.setPeProvisionerAlias(peProvisionerAlias);
					i.setPowerModelAlias(powerModelAlias);
					i.setRam(ram);
					i.setRamProvisionerAlias(ramProvisionerAlias);
					i.setSchedulingPolicyAlias(schedulingPolicyAlias);
					i.setStaticPowerPercent(staticPowerPercent);
					i.setStorage(storage);
					dataRegDAO.updateDatacenterRegistry(simReg, dataReg);
					simRegDAO.updateSimulationRegistry(simReg);
					this.statusCode = 1;
					System.out.println("-------------保存host成功------------");
				}
			}
			
		}
    	catch(Exception ex){
    		this.statusCode = 0;
    	}
		
    	return "success";
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setSchedulingPolicyAlias(String schedulingPolicyAlias) {
		this.schedulingPolicyAlias = schedulingPolicyAlias;
	}

	public void setNumOfPes(int numOfPes) {
		this.numOfPes = numOfPes;
	}

	public void setMipsPerPe(double mipsPerPe) {
		this.mipsPerPe = mipsPerPe;
	}

	public void setMaxPower(double maxPower) {
		this.maxPower = maxPower;
	}

	public void setStaticPowerPercent(double staticPowerPercent) {
		this.staticPowerPercent = staticPowerPercent;
	}

	public void setPowerModelAlias(String powerModelAlias) {
		this.powerModelAlias = powerModelAlias;
	}

	public void setRam(int ram) {
		this.ram = ram;
	}

	public void setRamProvisionerAlias(String ramProvisionerAlias) {
		this.ramProvisionerAlias = ramProvisionerAlias;
	}

	public void setBw(int bw) {
		this.bw = bw;
	}

	public void setBwProvisionerAlias(String bwProvisionerAlias) {
		this.bwProvisionerAlias = bwProvisionerAlias;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setStorage(long storage) {
		this.storage = storage;
	}

	public void setPeProvisionerAlias(String peProvisionerAlias) {
		this.peProvisionerAlias = peProvisionerAlias;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
