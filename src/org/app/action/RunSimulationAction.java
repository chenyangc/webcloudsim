package org.app.action;




import java.util.Map;

import org.app.dao.SimulationRegistryDAO;


import org.app.models.SimulationRegistry;
import org.app.simulation.NewSimulation;
import org.cloudbus.cloudsim.Log;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class RunSimulationAction  extends ActionSupport{
	
	private static Thread simulationThread;    
	private static final long serialVersionUID = -6944985926285449223L;
	private int statusCode;
	
	public String execute() throws Exception
    {
		
		try {
			Log.enable();									
			ActionContext ctx = ActionContext.getContext();
			Map<String, Object> session = ctx.getSession();

			SimulationRegistryDAO simRegDAO = new SimulationRegistryDAO();
			SimulationRegistry sr = simRegDAO.getSimulationRegistry((long) session.get("currentSimId"));
			         
			simulationThread = new Thread(new NewSimulation(sr));
			simulationThread.start();
			while(simulationThread.getState()!=Thread.State.TERMINATED){
				Thread.sleep(500);
			}
			System.out.println("###");
			this.statusCode=1;
			return "success";
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.statusCode=0;
			 
		}
		return "success";
       
    }

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
		
	
}
