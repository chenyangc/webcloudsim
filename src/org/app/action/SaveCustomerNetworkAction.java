package org.app.action;

import java.util.List;
import java.util.Map;

import org.app.dao.SimulationRegistryDAO;
import org.app.models.NetworkMapEntry;
import org.app.models.SimulationRegistry;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class SaveCustomerNetworkAction extends ActionSupport{

	private static final long serialVersionUID = 5888940230895013745L;

	private NetworkMapEntry saveNetwork=new NetworkMapEntry();
	private int statusCode;
	
	public String execute() throws Exception{
		
		
		ActionContext ctx = ActionContext.getContext();
		Map<String, Object> session = ctx.getSession();
		
		try{			
			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry simReg = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
										
			List<NetworkMapEntry> networkList=simReg.getNetworkMapEntrys();
			for(NetworkMapEntry nm:networkList){
				if(nm.getId()==saveNetwork.getId()){
					nm.clone(saveNetwork);
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






	public NetworkMapEntry getSaveNetwork() {
		return saveNetwork;
	}

	public void setSaveNetwork(NetworkMapEntry saveNetwork) {
		this.saveNetwork = saveNetwork;
	}

	public int getStatusCode() {
		return statusCode;
	}


	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	
	
}
