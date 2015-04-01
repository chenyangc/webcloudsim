package org.app.simulation;

import org.app.dao.CustomerRegistryDAO;
import org.app.dao.DrawDataDAO;

import org.app.dao.SimulationRegistryDAO;
import org.app.extensions.NetDatacenter;
import org.app.models.CustomerRegistry;
import org.app.models.DrawData;
import org.app.models.SimulationRegistry;
import org.app.reports.CustomerReport;
import org.app.reports.DatacenterReport;
import org.app.reports.LogReport;
import org.app.reports.NetDataCollector;
import org.app.utils.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.network.datacenter.NetDatacenterBroker;
import org.cloudbus.cloudsim.network.datacenter.NetworkDatacenter;

/**
 * The Simulation class is responsible for setting up the simulation
 * environments and running the all the simulations.
 * 
 * @author      Thiago T. Sá,chenyang
 * @since       1.0
 */
public class NewSimulation implements Runnable {

	private CustomerRegistryDAO crDAO=new CustomerRegistryDAO();

	private String log;
	
	/** Indicates whether the simulations are over or not. */
    private static boolean terminated;
    
    /** A data collector. */
    private static NetDataCollector dataCollector;
    
    private SimulationRegistry sr;
    

    /**
     * Gets a value that indicates whether the simulations are over or not.
     * 
     * @return  a boolean that indicates whether the simulations are over or not.
     */    
    public static boolean hasTerminated() {
        return terminated;
    }

    /**
     * Gets the data collector.
     * 
     * @return  the data collector.
     */    
    public static NetDataCollector getDataCollector() {
        return dataCollector;
    }
    
    public NewSimulation(SimulationRegistry sr){
    	this.sr=sr;
    	
    }
    /** 
     * Loads each simulation environment and run its simulations.
     * 
     * @since           1.0
     */        
    @Override
    public void run() {
        terminated = false;
        double startTime = Calendar.getInstance().getTimeInMillis();
        
        CloudSim.init(getNumberOfCustomers(sr), Calendar.getInstance(), false);
//        CloudSim.startSimulation();
           
        runAllSimulations(this.sr);
        

        double finishTime = Calendar.getInstance().getTimeInMillis();
        ElapsedTime elapsedTime = new ElapsedTime(finishTime - startTime);

    }

    /** 
     * Runs all simulations of a specific environment.
     * It creates all CloudSim entities, runs the simulation and generates the
     * report. This cycle is repeated the number of times indicated by the
     * NumberOfSimulations setting.
     * 
     * @since           1.0
     */       
    private void runAllSimulations(SimulationRegistry sr) {

    	
    	
        Log.setOutput(LogIO.getFileOutputStream());
        Log.printLine("WebCloudSim version 1.0");
        Log.print("Verifying available resources...");
        
        SimulationRegistryDAO srDAO=new SimulationRegistryDAO();
        srDAO.updateSimulationRegistry(sr);   
        ResetCustomersTimeToSend(sr);

        if (Verification.verifyVMsDeploymentViability(sr)) {
        	 
        	runSimulation(sr);
        } 
        else {
        	System.out.println("Simulation aborted:\nSome of the virtual "
        			+ "machines cannot be deployed by any available host.");
                
       }
            LogIO.removeTempLogFile();
        
    }
    
    /** 
     * Runs a round of a simulation.
     * 
     * @since           1.1
     */      
    private void runSimulation(SimulationRegistry  sr) {
        Log.print("OK\n");
      
        
        double currentSimulationStartTime = Calendar.getInstance().getTimeInMillis();
        CloudSim.init(getNumberOfCustomers(sr), Calendar.getInstance(), false);
//        CloudSim.terminateSimulation(SettingBusiness.getTimeToSimulate()*60);
 //���ý���ʱ��
 //       CloudSim.terminateSimulation(3600);
        NetEntityFactory eF=new NetEntityFactory();
     
        
        eF.reBuild(sr);
    	HashMap<String, NetDatacenter> datacenters=eF.getDatacenters(); 
    	HashMap<String, NetDatacenterBroker> brokers=eF.getBrokers();

    	HashMap<String, Datacenter> tmpdatacenters= new HashMap<String, Datacenter>();
    	for(Entry<String, NetDatacenter> datacenter:datacenters.entrySet()){
    		String tmp=datacenter.getKey();
    		Datacenter tmpdatacenter=datacenter.getValue();
    		tmpdatacenters.put(tmp, tmpdatacenter);
    	}
    	
        if (datacenters == null || brokers == null) return;
        

        try {
        	
            NewSimulation.dataCollector = new NetDataCollector(sr,tmpdatacenters, brokers);
            CloudSim.startSimulation();

            NewSimulation.dataCollector.flushData();
            double currentSimulationFinishTime = Calendar.getInstance().getTimeInMillis();
           
            ElapsedTime elapsedTime = new ElapsedTime(currentSimulationFinishTime - currentSimulationStartTime);
            
            List<NetDatacenterBroker> brokersList = Arrays.asList(brokers.values().toArray(new NetDatacenterBroker[0]));
            List<NetworkDatacenter> datacentersList = Arrays.asList(datacenters.values().toArray(new NetworkDatacenter[0]));
            LogReport logReport=new LogReport(brokersList,datacentersList,0);
            log=logReport.getHtml();
            DrawDataDAO ddDAO=new DrawDataDAO();
            DrawData drawData=new DrawData("log",log);
            ddDAO.insertDrawData(sr,drawData);

           SimulationRegistryDAO srDAO=new SimulationRegistryDAO();
           srDAO.updateSimulationRegistry(sr);
         
           sr=srDAO.getSimulationRegistry(this.sr.getId());
           
        CustomerReport cr=new CustomerReport(sr,brokersList.get(0));
           
          
        DatacenterReport dr=new DatacenterReport(sr,datacentersList.get(0),brokersList.get(0));
           
            
            if (hasTerminated()) {
                System.out.println("Simulation has been abrubtly terminated.");
            } else {
                // notification       
            	 System.out.println("Simulation has been finished.");
                
            }
            srDAO.updateSimulationRegistry(sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * Stops all simulations.
     * 
     * @since           1.0
     */      
    public static void stopSimulation() {
        CloudSim.init(1, Calendar.getInstance(), false);
        CloudSim.startSimulation();
        CloudSim.stopSimulation();
        terminated = true;
    }

    /** 
     * Gets the number of simulated customers.
     * 
     * @return  the number of simulated customers.
     * @since   1.0
     */      
    private int getNumberOfCustomers(SimulationRegistry sr) {
        CustomerRegistryDAO crDAO = new CustomerRegistryDAO();
        return crDAO.getNumOfCustomers(sr);
    }


    /** 
     * Resets the time to send cloudlets for all customers.
     * 
     * @since   1.1
     */  
    private void ResetCustomersTimeToSend(SimulationRegistry sr) {
        for (CustomerRegistry cr : crDAO.getListOfCustomers(sr)) {
            cr.getUtilizationProfile().setTimeToSend(0);
            crDAO.updateCustomerRegistry(sr,cr);
        }
    }
    
    
}
