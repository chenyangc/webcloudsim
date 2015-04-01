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

package org.app.reports;


import org.app.dao.DatacenterRegistryDAO;
import org.app.dao.DrawDataDAO;
import org.app.dao.MigrationDAO;
import org.app.dao.ReportDataDAO;
import org.app.models.DatacenterRegistry;
import org.app.models.DrawData;
import org.app.models.Migration;
import org.app.models.SimulationRegistry;

import java.text.DecimalFormat;
import java.util.*;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.network.datacenter.NetDatacenterBroker;
import org.cloudbus.cloudsim.network.datacenter.NetworkDatacenter;


/**
 * Provides methods to generate simulation reports with information about 
 * usage of resources by datacenters.
 * 
 * @author      Thiago T. Sá,chenyang
 * @since       1.0
 */
public class DatacenterReport {
	DecimalFormat df = new DecimalFormat("#.00");
    /** The name of the datacenter. */
    private String name;
    
	/** Number of executed cloudlets per host. */
    private TreeMap<String,Double> executedCloudlets;
    
    /** Number of deployed virtual machines per host. */
    private TreeMap<String,Double> deployedVms;
    
    /** Costs generated per customer. */
    private TreeMap<String,Double> costs;
    
    /** RAM utilization per host. */
    private HashMap<Integer, TreeMap<Double, Double>> hostsUsedRam;
    
    /** CPU utilization per host. */
    private HashMap<Integer, TreeMap<Double, Double>> hostsUsedCpu;
    
    /** Bandwidth utilization per host. */
    private HashMap<Integer, TreeMap<Double, Double>> hostsUsedBandwidth;
    
    /** Power consumption per host. */
    private HashMap<Integer, TreeMap<Double, Double>> hostsUsedPower;
    
    /** Overall usage of RAM by the datacenter. */
    private TreeMap<Double, Double> overallUsedRam;
    
    /** Overall usage of CPU by the datacenter. */
    private TreeMap<Double, Double> overallUsedCpu;
    
    /** Overall usage of bandwidth by the datacenter. */
    private TreeMap<Double, Double> overallUsedBandwidth;
    
    /** Overall power consumption by the datacenter. */
    private TreeMap<Double, Double> overallUsedPower;
    

    
    /** 
     * Creates a datacenter report.
     * 
     * @param   datacenter      the datacenter.
     * @param   brokersList     a list of all brokers.
     * @since                   1.0
     */           
    public DatacenterReport(SimulationRegistry sr,Datacenter datacenter, List<DatacenterBroker> brokersList)  {        
        this.name = datacenter.getName();
        DatacenterRegistryDAO drDAO=new DatacenterRegistryDAO();
        DrawDataDAO ddDAO=new DrawDataDAO();
        DatacenterRegistry dr=drDAO.getDatacenterRegistry(sr,this.name);
        // Get all hosts resource utilization data from the database
        this.hostsUsedRam =  new HashMap<Integer, TreeMap<Double, Double>>();
        this.hostsUsedCpu = new HashMap<Integer, TreeMap<Double, Double>>();
        this.hostsUsedBandwidth = new HashMap<Integer, TreeMap<Double, Double>>();
        this.hostsUsedPower = new HashMap<Integer, TreeMap<Double, Double>>();
        List<Host> hostsList = datacenter.getHostList();
        ReportDataDAO rdDAO = new ReportDataDAO();
        for(Host host : hostsList) {
            int hostId = host.getId();
            hostsUsedRam.put(hostId, rdDAO.getHostUsedResources(sr,"RAM", this.name, hostId));
            hostsUsedCpu.put(hostId, rdDAO.getHostUsedResources(sr,"CPU", this.name, hostId));
            hostsUsedBandwidth.put(hostId, rdDAO.getHostUsedResources(sr,"BANDWIDTH", this.name, hostId));
            hostsUsedPower.put(hostId, rdDAO.getHostUsedResources(sr,"POWER", this.name, hostId));
        }
        
        //Get the overall resource utilization data from the database
        this.overallUsedRam = rdDAO.getDatacenterOverallData(sr,"RAM", this.name);
        this.overallUsedCpu = rdDAO.getDatacenterOverallData(sr,"CPU", this.name);
        this.overallUsedBandwidth = rdDAO.getDatacenterOverallData(sr,"BANDWIDTH", this.name);
        this.overallUsedPower = rdDAO.getDatacenterOverallData(sr,"POWER", this.name);
        
        
        executedCloudlets = new TreeMap<String, Double>();
        deployedVms = new TreeMap<String, Double>();
        costs = new TreeMap<String, Double>();        
        for(DatacenterBroker broker : brokersList) {
            //Set of IDs from virtual machines deployed by this datacenter
            Set<Integer> vmIds = new HashSet<Integer>();
            
            // Compute all cloudlets executed in this datacenter
            executedCloudlets.put(broker.getName(), 0D);            
            List<Cloudlet> cloudletsList = broker.getCloudletSubmittedList();
            for(Cloudlet cloudlet : cloudletsList) {
                if(cloudlet.getResourceId() == datacenter.getId()) {
                    executedCloudlets.put(broker.getName(), executedCloudlets.get(broker.getName()) + 1);
                    vmIds.add(cloudlet.getVmId());
                } 
            }
            
            //Set the number of virtual machines from this customer deployed on this datacenter
            deployedVms.put(broker.getName(), Double.valueOf(String.valueOf(vmIds.size())));
            
            //Get the customer's debt on this datacenter
 //           costs.put(broker.getName(), datacenter.getDebts().get(broker.getId()));            
        }
        
        int host_number=drDAO.getNumOfHosts(sr,dr.getId());
     	String tmp=createResourceUtilization(-1);
       	DrawData drawData=new DrawData("host_resource_-1",tmp);
       	ddDAO.insertDrawData(sr,drawData);
       	tmp=createPowerConsumption(-1);
       	DrawData drawData1=new DrawData("host_power_-1",tmp);
       	ddDAO.insertDrawData(sr,drawData1);
       	
        for(Host host:hostsList){
         	 tmp=createResourceUtilization(host.getId());
           	 drawData=new DrawData("host_resource_"+host.getId(),tmp);
           	 ddDAO.insertDrawData(sr,drawData);
           	 tmp=createPowerConsumption(host.getId());
           	 drawData1=new DrawData("host_power_"+host.getId(),tmp);
           	 ddDAO.insertDrawData(sr,drawData1);
        }
        
      
        tmp=createVmNumber();
        DrawData drawData2=new DrawData("vm_number",tmp);
        ddDAO.insertDrawData(sr,drawData2);
        
        tmp=createCloudletsNumber();
        DrawData drawData3=new DrawData("cloudlet_number",tmp);
        ddDAO.insertDrawData(sr,drawData3);
        
      
    }

    public DatacenterReport(SimulationRegistry sr,
			NetworkDatacenter datacenter, NetDatacenterBroker broker) {
        this.name = datacenter.getName();
        DatacenterRegistryDAO drDAO=new DatacenterRegistryDAO();
        DrawDataDAO ddDAO=new DrawDataDAO();
      
        // Get all hosts resource utilization data from the database
        this.hostsUsedRam =  new HashMap<Integer, TreeMap<Double, Double>>();
        this.hostsUsedCpu = new HashMap<Integer, TreeMap<Double, Double>>();
        this.hostsUsedBandwidth = new HashMap<Integer, TreeMap<Double, Double>>();
        this.hostsUsedPower = new HashMap<Integer, TreeMap<Double, Double>>();
        List<Host> hostsList = datacenter.getHostList();
        ReportDataDAO rdDAO = new ReportDataDAO();
        for(Host host : hostsList) {
            int hostId = host.getId();
            hostsUsedRam.put(hostId, rdDAO.getHostUsedResources(sr,"RAM", this.name, hostId));
            hostsUsedCpu.put(hostId, rdDAO.getHostUsedResources(sr,"CPU", this.name, hostId));
            hostsUsedBandwidth.put(hostId, rdDAO.getHostUsedResources(sr,"BANDWIDTH", this.name, hostId));
            hostsUsedPower.put(hostId, rdDAO.getHostUsedResources(sr,"POWER", this.name, hostId));
        }
        
        //Get the overall resource utilization data from the database
        this.overallUsedRam = rdDAO.getDatacenterOverallData(sr,"RAM", this.name);
        this.overallUsedCpu = rdDAO.getDatacenterOverallData(sr,"CPU", this.name);
        this.overallUsedBandwidth = rdDAO.getDatacenterOverallData(sr,"BANDWIDTH", this.name);
        this.overallUsedPower = rdDAO.getDatacenterOverallData(sr,"POWER", this.name);
        
        
        executedCloudlets = new TreeMap<String, Double>();
        deployedVms = new TreeMap<String, Double>();
        costs = new TreeMap<String, Double>();        
    
           //Set of IDs from virtual machines deployed by this datacenter
           Set<Integer> vmIds = new HashSet<Integer>();
            
            // Compute all cloudlets executed in this datacenter
            executedCloudlets.put(broker.getName(), 0D);            
            List<Cloudlet> cloudletsList = broker.getCloudletSubmittedList();
            for(Cloudlet cloudlet : cloudletsList) {
                if(cloudlet.getResourceId() == datacenter.getId()) {
                    executedCloudlets.put(broker.getName(), executedCloudlets.get(broker.getName()) + 1);
                    vmIds.add(cloudlet.getVmId());
                } 
            }
            
            //Set the number of virtual machines from this customer deployed on this datacenter
            deployedVms.put(broker.getName(), Double.valueOf(String.valueOf(vmIds.size())));
            
            //Get the customer's debt on this datacenter
 //           costs.put(broker.getName(), datacenter.getDebts().get(broker.getId()));            
        
        
     	String tmp=createResourceUtilization(-1);
       	DrawData drawData=new DrawData("host_resource_-1",tmp);
       	ddDAO.insertDrawData(sr,drawData);
       	tmp=createPowerConsumption(-1);
       	DrawData drawData1=new DrawData("host_power_-1",tmp);
       	ddDAO.insertDrawData(sr,drawData1);
       	
        for(Host host:hostsList){
         	 tmp=createResourceUtilization(host.getId());
           	 drawData=new DrawData("host_resource_"+host.getId(),tmp);
           	 ddDAO.insertDrawData(sr,drawData);
           	 tmp=createPowerConsumption(host.getId());
           	 drawData1=new DrawData("host_power_"+host.getId(),tmp);
           	 ddDAO.insertDrawData(sr,drawData1);
        }
        
      
        tmp=createVmNumber();
        DrawData drawData2=new DrawData("vm_number",tmp);
        ddDAO.insertDrawData(sr,drawData2);
        
        tmp=createCloudletsNumber();
        DrawData drawData3=new DrawData("cloudlet_number",tmp);
        ddDAO.insertDrawData(sr,drawData3);
	}

	/**
     * Gets the name of the datacenter.
     * 
     * @return  a string that contains the name of the datacenter.
     */
    public String getName() {
		return name;
	}
    
    private String createResourceUtilization(int hostid){
    	StringBuilder dataStringBuilder = new StringBuilder("{  title : {  text: 'Resource utilization' },  "
    			+ " tooltip : { trigger: 'axis' }, "
    			+ "legend: { data:['CPU(MIPS)','RAM(MB)','Bandwidth(Mbps)'] }, "
    			+ "xAxis : [  {type : 'category',  boundaryGap : false, data :");
    	if(hostid==-1){
    		dataStringBuilder.append(getFirstDoubleData(overallUsedRam));
    		dataStringBuilder.append(" } ], yAxis : [ { type : 'value', axisLabel : { formatter: '{value} '   } } ], "
    				+ "series : [ {  name:'CPU(MIPS)', type:'line', data:");
    		dataStringBuilder.append(getSecondDataString(overallUsedCpu));
    		dataStringBuilder.append(", markPoint : { data : [  {type : 'max', name: 'max'}, {type : 'min', name: 'min'} ]  },"
    				+ " markLine : { data : [  {type : 'average', name: 'aver'} ]} },{  name:'RAM(MB)', type:'line', data:");
    		dataStringBuilder.append(getSecondDataString(overallUsedRam));
    		dataStringBuilder.append(", markPoint : {  data : [ {type : 'max', name: 'max'}, {type : 'min', name: 'min'}]  }, "
    				+ " markLine : { data : [ {type : 'average', name : 'aver'} ]  } }, { name:'Bandwidth(Mbps)', type:'line',  data:");
    		dataStringBuilder.append(getSecondDataString(overallUsedBandwidth));
    		dataStringBuilder.append(", markPoint : {  data : [{type : 'max', name: 'max'},  {type : 'min', name: 'min'}  ] }, "
    				+ "markLine : { data : [ {type : 'average', name : 'aver'} ]} } ]}");
    	}
    	else{    		
    		TreeMap<Double, Double> nowHostCpuUsed=hostsUsedCpu.get(hostid);
    		TreeMap<Double, Double> nowHostRamUsed=hostsUsedRam.get(hostid);
    		TreeMap<Double, Double> nowHostBandUsed=hostsUsedBandwidth.get(hostid);
    		dataStringBuilder.append(getFirstDoubleData(nowHostCpuUsed));
    		dataStringBuilder.append(" } ], yAxis : [ { type : 'value', axisLabel : { formatter: '{value} '   } } ], "
    				+ "series : [ {  name:'CPU(MIPS)', type:'line', data:");
    		dataStringBuilder.append(getSecondDataString(nowHostCpuUsed));
    		dataStringBuilder.append(", markPoint : { data : [  {type : 'max', name: 'max'}, {type : 'min', name: 'min'} ]  },"
    				+ " markLine : { data : [  {type : 'average', name: 'aver'} ]} },{  name:'RAM(MB)', type:'line', data:");
    		dataStringBuilder.append(getSecondDataString(nowHostRamUsed));
    		dataStringBuilder.append(", markPoint : {  data : [ {type : 'max', name: 'max'}, {type : 'min', name: 'min'}]  }, "
    				+ " markLine : { data : [ {type : 'average', name : 'aver'} ]  } }, { name:'Bandwidth(Mbps)', type:'line',  data:");
    		dataStringBuilder.append(getSecondDataString(nowHostBandUsed));
    		dataStringBuilder.append(", markPoint : {  data : [{type : 'max', name: 'max'},  {type : 'min', name: 'min'}  ] }, "
    				+ "markLine : { data : [ {type : 'average', name : 'aver'} ]} } ]}");
    		
    	}
    	return dataStringBuilder.toString();
    }
    
    private String createPowerConsumption(int hostid){
    	StringBuilder dataStringBuilder = new StringBuilder("{  title : {  text: 'Power Consumption' },  "
    			+ " tooltip : { trigger: 'axis' }, "
    			+ "legend: { data:['Power(KW)'] }, "
    			+ "xAxis : [  {type : 'category',  boundaryGap : false, data :");
    	if(hostid==-1){
    		dataStringBuilder.append(getFirstDoubleData(overallUsedPower));
    		dataStringBuilder.append(" } ], yAxis : [ { type : 'value', axisLabel : { formatter: '{value} '   } } ], "
    				+ "series : [ {  name:'Power(KW)', type:'line', data:");
    		dataStringBuilder.append(getSecondDataString(overallUsedPower));
    		dataStringBuilder.append(", markPoint : {  data : [{type : 'max', name: 'max'},  {type : 'min', name: 'min'}  ] }, "
    				+ "markLine : { data : [ {type : 'average', name : 'aver'} ]} } ]}");
    	}
    	else{    		
    		TreeMap<Double, Double> nowHostPowerUsed=hostsUsedPower.get(hostid);
    		dataStringBuilder.append(getFirstDoubleData(nowHostPowerUsed));
    		dataStringBuilder.append(" } ], yAxis : [ { type : 'value', axisLabel : { formatter: '{value} '   } } ], "
    				+ "series : [ {  name:'Power(KW)', type:'line', data:");
    		dataStringBuilder.append(getSecondDataString(nowHostPowerUsed));
    		dataStringBuilder.append(", markPoint : {  data : [{type : 'max', name: 'max'},  {type : 'min', name: 'min'}  ] }, "
    				+ "markLine : { data : [ {type : 'average', name : 'aver'} ]} } ]}");
    		
    	}
    	return dataStringBuilder.toString();
    }
    
    private String createVmNumber(){
    	StringBuilder dataStringBuilder = new StringBuilder("{  title : {  text: 'VM Number' },  "
    			+ " tooltip : { trigger: 'axis' }, "
    			+ "legend: { data:['VM-Number'] }, "
    			+ "xAxis : [  {type : 'category',  boundaryGap : true, data :");

    	dataStringBuilder.append(getFirstStringData(deployedVms));
    	dataStringBuilder.append(" } ], yAxis : [ { type : 'value', axisLabel : { formatter: '{value} '   } } ], "
    				+ "series : [ {  name:'VM-Number', type:'bar', data:");
    	dataStringBuilder.append(getSecondDataString(deployedVms));
    	dataStringBuilder.append(", markPoint : {  data : [{type : 'max', name: 'max'},  {type : 'min', name: 'min'}  ] }, "
    				+ "markLine : { data : [ {type : 'average', name : 'aver'} ]} } ]}");
    		
    	
    	return dataStringBuilder.toString();
    }
    
    private String createCloudletsNumber(){
    	StringBuilder dataStringBuilder = new StringBuilder("{  title : {  text: 'Cloudlets Number' },  "
    			+ " tooltip : { trigger: 'axis' }, "
    			+ "legend: { data:['Cloudlets-Number'] }, "
    			+ "xAxis : [  {type : 'category',  boundaryGap : true, data :");

    	dataStringBuilder.append(getFirstStringData(executedCloudlets));
    	dataStringBuilder.append(" } ], yAxis : [ { type : 'value', axisLabel : { formatter: '{value} '   } } ], "
    				+ "series : [ {  name:'Cloudlets-Number', type:'bar', data:");
    	dataStringBuilder.append(getSecondDataString(executedCloudlets));
    	dataStringBuilder.append(", markPoint : {  data : [{type : 'max', name: 'max'},  {type : 'min', name: 'min'}  ] }, "
    				+ "markLine : { data : [ {type : 'average', name : 'aver'} ]} } ]}");
    		
    	
    	return dataStringBuilder.toString();
    }
    
    private String getFirstStringData(TreeMap<String, Double> dataMap) {
        StringBuilder dataStringBuilder = new StringBuilder("[");
        Iterator<String> iterator = dataMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            dataStringBuilder.append("'");
            dataStringBuilder.append(key);
            dataStringBuilder.append("',");
        }
        dataStringBuilder.deleteCharAt(dataStringBuilder.lastIndexOf(","));
        dataStringBuilder.append("]");
        
        return dataStringBuilder.toString();
	}


	private String getSecondDataString(TreeMap<?, ?> dataMap) {
       StringBuilder dataStringBuilder = new StringBuilder("[");
       Iterator<?> iterator = dataMap.keySet().iterator();
       while (iterator.hasNext()) {
           Object key = iterator.next();
           dataStringBuilder.append("'");
           dataStringBuilder.append(df.format(dataMap.get(key)));
           dataStringBuilder.append("',");
       }
       dataStringBuilder.deleteCharAt(dataStringBuilder.lastIndexOf(","));
       dataStringBuilder.append("]");
       
       return dataStringBuilder.toString();
	}


    
	private String getFirstDoubleData(TreeMap<Double, Double> dataMap) {
       StringBuilder dataStringBuilder = new StringBuilder("[");
       Iterator<Double> iterator = dataMap.keySet().iterator();        
       while (iterator.hasNext()) {
           Double key = iterator.next();
           dataStringBuilder.append("'");
           dataStringBuilder.append(df.format(key));
           dataStringBuilder.append("',");
       }
       dataStringBuilder.deleteCharAt(dataStringBuilder.lastIndexOf(","));
       dataStringBuilder.append("]");
       
       return dataStringBuilder.toString();
   }
    

    
  
    private String getMigrationsString(SimulationRegistry sr) {        
        MigrationDAO mDAO = new MigrationDAO();
        List<Migration> migrationList = mDAO.getMigrationList(sr,this.name);
        StringBuilder migrationStringBuilder = new StringBuilder("<br/>Number of migrations: ");
        migrationStringBuilder.append(migrationList.size()).append("<br/>");
        
        for(Migration migration : migrationList) {
            migrationStringBuilder.append("<br/><hr>")
                           .append("<strong>Migration ")
                           .append(migrationList.indexOf(migration))
                           .append(":</strong><br/>")
                           .append("Description: ").append(migration.getDescription())
                           .append("<br/><strong>").append(migration.getVmLabel())
                           .append("</strong> from <strong>").append(migration.getSourceHostLabel())
                           .append("</strong> to <strong>").append(migration.getTargetHostLabel())
                           .append("</strong> at <strong>").append(migration.getTime()/60)
                           .append("</strong> minutes.<br/>")
                           .append("Source host was consuming ").append(migration.getSourceHostCpuUtilization()*100)
                           .append("% of CPU, ").append(migration.getSourceHostRamUtilization()*100)
                           .append("% of RAM and ").append(migration.getSourceHostPowerConsumption()*100)
                           .append("% of power.<br/>")
                           .append("Target host was consuming ").append(migration.getTargetHostCpuUtilization()*100)
                           .append("% of CPU, ").append(migration.getTargetHostRamUtilization()*100)
                           .append("% of RAM and ").append(migration.getTargetHostPowerConsumption()*100)
                           .append("% of power.<br/>");
        }
        migrationStringBuilder.append("<br/><br/>");
        
        return migrationStringBuilder.toString();
    }
    
}
