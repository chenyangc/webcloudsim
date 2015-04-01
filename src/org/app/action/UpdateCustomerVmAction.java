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

public class UpdateCustomerVmAction extends ActionSupport{

	private static final long serialVersionUID = 5888940230895013745L;
	private VirtualMachineRegistry updateVm=new VirtualMachineRegistry();
	private int statusCode;
	
	public String execute() throws Exception{
		
		
		ActionContext ctx = ActionContext.getContext();
		Map<String, Object> session = ctx.getSession();
		
		try{
			System.out.println("!!!"+updateVm);
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			
			CustomerRegistryDAO cusRegDAO = new CustomerRegistryDAO();	
			int currentCustomerId= (int) session.get("customerId");
			CustomerRegistry cr=cusRegDAO.getCustomerRegistry(simReg, currentCustomerId);
			System.out.println("!!!update vm "+cr.getVmList().size());			
			List<VirtualMachineRegistry> vmlist=cr.getVmList();
			
			for(VirtualMachineRegistry vm:vmlist){
				if(vm.getId()==updateVm.getId()){
					System.out.println(vm.getId());
					vm.clone(updateVm);
					System.out.println("!!!update vm success");
					break;
				}
			}
			simRegDAO.updateSimulationRegistry(simReg);
			this.statusCode = 1;	
			return "success";
		}
		catch(Exception ex){
			ex.printStackTrace();
			this.statusCode = 2;//系统异常
		}
		return "success";
	}



	public VirtualMachineRegistry getUpdateVm() {
		return updateVm;
	}

	public void setUpdateVm(VirtualMachineRegistry updateVm) {
		this.updateVm = updateVm;
	}

	public int getStatusCode() {
		return statusCode;
	}


	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	
	
}
