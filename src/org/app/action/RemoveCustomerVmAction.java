package org.app.action;

import java.util.List;
import java.util.Map;

import org.app.dao.CustomerRegistryDAO;
import org.app.dao.SimulationRegistryDAO;
import org.app.models.CustomerRegistry;
import org.app.models.SimulationRegistry;
import org.app.models.VirtualMachineRegistry;




import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class RemoveCustomerVmAction extends ActionSupport{

	private static final long serialVersionUID = 5888940230895013745L;
	
	private VirtualMachineRegistry removeVm=new VirtualMachineRegistry();
	private int statusCode;
	
	public String execute() throws Exception{
		
		
		ActionContext ctx = ActionContext.getContext();
		Map<String, Object> session = ctx.getSession();
		
		try{
			System.out.println(removeVm);
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			long currentCustomerId= (long) session.get("customerId");
			CustomerRegistryDAO cusRegDAO = new CustomerRegistryDAO();	
			CustomerRegistry cr=cusRegDAO.getCustomerRegistry(simReg, currentCustomerId);
						
			List<VirtualMachineRegistry> vmlist=cr.getVmList();
			for(VirtualMachineRegistry vm:vmlist){
				if(vm.getId()==removeVm.getId()){
					vmlist.remove(vm);
					break;
				}
			}
			simRegDAO.updateSimulationRegistry(simReg);
			this.statusCode = 1;	
			return "success";
		}
		catch(Exception ex){
			this.statusCode = 2;//系统异常
		}
		return "success";
	}


	public VirtualMachineRegistry getRemoveVm() {
		return removeVm;
	}

	public void setRemoveVm(VirtualMachineRegistry removeVm) {
		this.removeVm = removeVm;
	}

	public int getStatusCode() {
		return statusCode;
	}


	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	
	
}
