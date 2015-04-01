/* 
 * Copyright (c) 2010-2012 Thiago T. Sá
 * 
 * This file is part of org.app.
 *
 * CloudReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CloudReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * For more information about your rights as a user of CloudReports,
 * refer to the LICENSE file or see <http://www.gnu.org/licenses/>.
 */

package org.app.dao;


import org.app.models.ReportData;
import org.app.models.SimulationRegistry;

import com.opensymphony.xwork2.ActionSupport;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;


/**
 * ReportDataDAO provides basic CRUD operations related to the 
 * {@link ReportData} class.
 * 
 * @see         ReportData
 * @author      ChenYang
 * @since       1.0
 */
public class ReportDataDAO   {
    
    /** 
     * Inserts report data related to resource usage of a given host.
     *
     * @param   type            the type of the used resource.
     * @param   datacenterName  the name of the datacenter that owns the host.
     * @param   hostId          the id of the host.
     * @param   time            the instant of time which the data being inserted refers to.
     * @param   amount          the amount of resources being used by the host at <code>time</code>.
     * @param   simulationId    the identification number of the simulation.
     * 
     * @see                     ReportData
     * @since                   1.0
     */    
	
	
    public void insertHostData(SimulationRegistry sr,String type,String datacenterName, int hostId,
                               double time, double amount, int simulationId) {
     
    	Set<ReportData> reportDatas=sr.getReportDatas();
    	ReportData rd = new ReportData(type, datacenterName, null, hostId, null, time, amount, simulationId);
    	reportDatas.add(rd);
          
                 
    }
    
    /** 
     * Gets a report of resources usage of a given host.
     *
     * @param   type            the type of the used resource.
     * @param   datacenterName  the name of the datacenter that owns the host.
     * @param   hostId          the id of the host.
     * @return                  a map with values of time as keys and values of
     *                          used resources as values.
     * @see                     ReportData
     * @since                   1.0
     */    

	public TreeMap<Double, Double> getHostUsedResources(SimulationRegistry sr,String type, String datacenterName, int hostId) {
        Set<ReportData> dataSet = new HashSet<ReportData>();
     
    	Set<ReportData> reportDatas=sr.getReportDatas();
        for(ReportData rrd:reportDatas){
        	if(rrd.getType()==null||rrd.getDatacenterName()==null||rrd.getHostId()==null)
        		continue;
        	if(rrd.getType().equals(type)&&rrd.getDatacenterName().equals(datacenterName)&&rrd.getHostId()==hostId){
        		dataSet.add(rrd);
        	}
        }
    	
        
        TreeMap<Double, Double> hostUsedResources = new TreeMap<Double, Double>();
        if(dataSet.size()==0)  return hostUsedResources;
        for(ReportData rd : dataSet) {
            if(type.equals("BANDWIDTH")) {
                //If bandwidth, convert from kbps to Mbps
                hostUsedResources.put(rd.getTime()/60, rd.getAmount() );
            }
            else hostUsedResources.put(rd.getTime()/60, rd.getAmount());
        }
        
        return hostUsedResources;
    }  
    
    /**
     * Inserts report data related to resource usage of a given virtual machine.
     *
     * @param type          the type of the used resource.
     * @param customerName  the name of the customer that owns the virtual machine.
     * @param vmId          the id of the virtual machine.
     * @param time          the instant of time which the data being inserted refers to.
     * @param amount        the amount of resources being used by the virtual machine at
     *                      <code>time</code>.
     * @param simulationId  the identification number of the simulation.
     *
     * @see                 ReportData
     * @since               1.0
     */    
    public void insertVmData(SimulationRegistry sr,String type,String customerName, int vmId, 
                             double time, double amount, int simulationId) {
        
        ReportData rd = new ReportData(type, null, customerName, null, vmId, time, amount, simulationId);
        
      
    	Set<ReportData> reportDatas=sr.getReportDatas();
    	
    	reportDatas.add(rd);
    }
    
    /** 
     * Gets a report of resources usage of a given virtual machine.
     *
     * @param   type            the type of the used resource.
     * @param   customerName    the name of the customer that owns the virtual machine.
     * @param   vmId            the id of the virtual machine.
     * @return                  a map with values of time as keys and values of
     *                          used resources as values.
     * @see                     ReportData
     * @since                   1.0
     */        

	public TreeMap<Double, Double> getVmUsedResources(SimulationRegistry sr,String type, String customerName, int vmId) {
        Set<ReportData> dataSet = new HashSet<ReportData>();
       
    	Set<ReportData> reportDatas=sr.getReportDatas();
    	
        for(ReportData rrd:reportDatas){
        	if(rrd.getType()==null||rrd.getVmId()==null||rrd.getCustomerName()==null)
        		continue;
        	if(rrd.getType().equals(type)&&rrd.getVmId()==vmId&&rrd.getCustomerName().equals(customerName)){
        		dataSet.add(rrd);
        	}
        }
        
        
        TreeMap<Double, Double> vmUsedResources = new TreeMap<Double, Double>();
        if(dataSet.size()==0) return vmUsedResources;
        for(ReportData rd : dataSet) {
            if (type.equals("BANDWIDTH")) {
                //If bandwidth, convert from kbps to Mbps
                vmUsedResources.put(rd.getTime()/60, rd.getAmount() );
            } else {
                vmUsedResources.put(rd.getTime()/60, rd.getAmount());
            }
        }
        
        return vmUsedResources;
    }    
    
    /** 
     * Inserts report data related to overall resource usage of a given datacenter.
     *
     * @param   type            the type of the used resource.
     * @param   datacenterName  the name of the datacenter.
     * @param   time            the instant of time which the data being inserted refers to.
     * @param   amount          the overall amount of resources being used by 
     *                          the datacenter at <code>time</code>.
     * @param   simulationId    the identification number of the simulation.
     * 
     * @see                     ReportData
     * @since                   1.0
     */        
    public void insertDatacenterOverallData(SimulationRegistry sr,String type, String datacenterName, double time,
                                            double amount, int simulationId) {
        
        ReportData rd = new ReportData(type, datacenterName, null, time, amount, simulationId);
        
//       	SimulationRegistry sr=srDAO.getSimulationRegistry(simuid);
    	Set<ReportData> reportDatas=sr.getReportDatas();
    	
    	reportDatas.add(rd);
    } 
    
    /** 
     * Gets a report of overall resources usage of a given datacenter.
     *
     * @param   type            the type of the used resource.
     * @param   datacenterName  the name of the datacenter.
     * @return                  a map with values of time as keys and values of
     *                          used resources as values.
     * @see                     ReportData
     * @since                   1.0
     */      

	public TreeMap<Double, Double> getDatacenterOverallData(SimulationRegistry sr,String type, String datacenterName) {
        Set<ReportData> dataSet = new HashSet<ReportData>();
 //       SimulationRegistry sr=srDAO.getSimulationRegistry(simuid);
    	Set<ReportData> reportDatas=sr.getReportDatas();
        for(ReportData rrd:reportDatas){
        	
        	if(rrd.getType()==null||rrd.getDatacenterName()==null||rrd.getHostId()!=null)
        		continue;
        	if(rrd.getType().equals(type)&&rrd.getDatacenterName().equals(datacenterName)){
        		dataSet.add(rrd);
        	}
        }
        
        
        TreeMap<Double, Double> overallUsedResources = new TreeMap<Double, Double>();
        if(dataSet.size()==0) return overallUsedResources;
        for(ReportData rd : dataSet) {
            if (type.equals("BANDWIDTH")) {
                //If bandwidth, convert from kbps to Mbps
                overallUsedResources.put(rd.getTime()/60, rd.getAmount() );
            } else {
                overallUsedResources.put(rd.getTime()/60, rd.getAmount());
            }
        }
        
        return overallUsedResources;
    }    
    
    /** 
     * Inserts report data related to overall resource usage of a given customer.
     *
     * @param   type            the type of the used resource.
     * @param   customerName    the name of the customer.
     * @param   time            the instant of time which the data being inserted refers to.
     * @param   amount          the overall amount of resources being used by 
     *                          the customer at <code>time</code>.
     * @param   simulationId    the identification number of the simulation.
     * 
     * @see                     ReportData
     * @since                   1.0
     */       
    public void insertCustomerOverallData(SimulationRegistry sr ,String type, String customerName, double time,
                                          double amount, int simulationId) {
        
        ReportData rd = new ReportData(type, null, customerName, time, amount, simulationId);
        
       
    	Set<ReportData> reportDatas=sr.getReportDatas();
    	
    	reportDatas.add(rd);
    }        
    
    /**
     * Gets a report of overall resources usage of a given customer.
     *
     * @param type          the type of the used resource.
     * @param customerName  the name of the customer.
     * @return              a map with values of time as keys and values of
     *                      used resources as values.
     * @see                 ReportData
     * @since               1.0
     */    
 
	public TreeMap<Double, Double> getCustomerOverallData(SimulationRegistry sr,String type, String customerName) {
        Set<ReportData> dataSet = new HashSet<ReportData>();
        
    	Set<ReportData> reportDatas=sr.getReportDatas();
        for(ReportData rrd:reportDatas){
        	if(rrd.getType()==null||rrd.getCustomerName()==null||rrd.getVmId()!=null)
        		continue;
        	if(rrd.getType().equals(type)&&rrd.getCustomerName().equals(customerName)){
        		dataSet.add(rrd);
        	}
        }
       
        
        TreeMap<Double, Double> overallUsedResources = new TreeMap<Double, Double>();
        if(dataSet.size()==0) return overallUsedResources;
        for(ReportData rd : dataSet) {
            if (type.equals("BANDWIDTH")) {
                //If bandwidth, convert from kbps to Mbps
                overallUsedResources.put(rd.getTime()/60, rd.getAmount() );
            } else {
                overallUsedResources.put(rd.getTime()/60, rd.getAmount());
            }
        }
        
        return overallUsedResources;
    }

    /**
     * Inserts a Set of report data into the database.
     *
     * @param   dataSet    the Set of report data to be inserted.
     * @see                 ReportData
     * @since               1.0
     */      
    public void insertDataSet (SimulationRegistry sr,Set<ReportData> dataSet)  {
    	SimulationRegistryDAO ssrDAO=new SimulationRegistryDAO();
//    	GlobalAttriDAO gaDAO=new GlobalAttriDAO();
//    	SimulationRegistry sr=ssrDAO.getSimulationRegistry(Long.valueOf(gaDAO.getGlobalAttri("nowSimulationId").getValue()));
 
    	Set<ReportData> reportDatas=sr.getReportDatas();    	
    	reportDatas.addAll(dataSet);
    	ssrDAO.updateSimulationRegistry(sr);
    	
    }

}
