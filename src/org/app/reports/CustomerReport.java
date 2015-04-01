

package org.app.reports;


import org.app.dao.CustomerRegistryDAO;
import org.app.dao.DrawDataDAO;
import org.app.dao.ReportDataDAO;
import org.app.models.CustomerRegistry;
import org.app.models.DrawData;
import org.app.models.SimulationRegistry;
import org.app.models.VirtualMachineRegistry;
import org.app.utils.FileIO;

import java.text.DecimalFormat;
import java.util.*;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.network.datacenter.NetDatacenterBroker;

/**
 * Provides methods to generate simulation reports with information about 
 * usage of resources by customers.
 * 
 * @author      Thiago T. Sá
 * @since       1.0
 */
public class CustomerReport {
    
	DecimalFormat df = new DecimalFormat("#.00");
    /** The name of the customer. */
    private String name;
    
	/** Number of cloudlets executed per virtual machine. */
    private TreeMap<String, Integer> cloudletsPerVm;
    
    /** Cloudlets' start and finish time of execution. */
    private TreeMap<Integer, List<Double>> cloudletsTimesOfExecution;
    
    /** RAM utilization per virtual machine. */
    private HashMap<Integer, TreeMap<Double, Double>> vmsUsedRam;
    
    /** CPU utilization per virtual machine. */
    private HashMap<Integer, TreeMap<Double, Double>> vmsUsedCpu;
    
    /** Bandwidth utilization per virtual machine. */
    private HashMap<Integer, TreeMap<Double, Double>> vmsUsedBandwidth;
    
    /** Overall usage of RAM by the customer. */
    private TreeMap<Double, Double> overallUsedRam;
    
    /** Overall usage of CPU by the customer. */
    private TreeMap<Double, Double> overallUsedCpu;
    
    /** Overall usage of bandwidth by the customer. */
    private TreeMap<Double, Double> overallUsedBandwidth;
    
    
    /** 
     * Creates a customer report for a given broker.
     * 
     * @param   broker  the customer's broker.
     * @since           1.0
     */    
    public CustomerReport(SimulationRegistry sr,DatacenterBroker broker)  {
    	
    	DrawDataDAO ddDAO=new DrawDataDAO();	    	
    	for(CustomerRegistry cr:sr.getCustomerList()){    			    	    	
    	        this.name = cr.getName();    	       
    	        List<Vm> vmsList = broker.getVmList();
    	        List<Cloudlet> cloudletsList = broker.getCloudletSubmittedList();
    	        //Get all virtual machines resource utilization data from the database
    	        this.vmsUsedRam = new HashMap<Integer, TreeMap<Double, Double>>();
    	        this.vmsUsedCpu = new HashMap<Integer, TreeMap<Double, Double>>();
    	        this.vmsUsedBandwidth = new HashMap<Integer, TreeMap<Double, Double>>();
    	        cloudletsPerVm = new TreeMap<String, Integer>();
    	        
    	        ReportDataDAO rdDAO = new ReportDataDAO();
    	        for(Vm vm : vmsList) {
    	            int vmId = (int) vm.getId();
    	            vmsUsedRam.put(vmId, rdDAO.getVmUsedResources(sr,"RAM", this.name, vmId));
    	            vmsUsedCpu.put(vmId, rdDAO.getVmUsedResources(sr,"CPU", this.name, vmId));
    	            vmsUsedBandwidth.put(vmId, rdDAO.getVmUsedResources(sr,"BANDWIDTH", this.name, vmId));
    	            cloudletsPerVm.put("VM"+vmId, 0);
    	        }
    	        
    	        //Get the overall resource utilization from this customer
    	        this.overallUsedRam = rdDAO.getCustomerOverallData(sr,"RAM", this.name);
    	        this.overallUsedCpu = rdDAO.getCustomerOverallData(sr,"CPU", this.name);
    	        this.overallUsedBandwidth = rdDAO.getCustomerOverallData(sr,"BANDWIDTH", this.name);

    	        
    	        cloudletsTimesOfExecution = new TreeMap<Integer, List<Double>>();
    	        System.out.println("!!!!!!!"+cloudletsList.size());
    	        
    	        for(Cloudlet cloudlet : cloudletsList) {
    	            List<Double> timesOfExecution = new ArrayList<Double>();
    	            timesOfExecution.add(cloudlet.getExecStartTime());
    	            timesOfExecution.add(cloudlet.getFinishTime());
    	            cloudletsTimesOfExecution.put(cloudlet.getCloudletId(), timesOfExecution);
    	            cloudletsPerVm.put("VM"+cloudlet.getVmId(), cloudletsPerVm.get("VM"+cloudlet.getVmId()) + 1);
    	        }
    	       
    	        String tmp=createResourceUtilization(-1);
    	        DrawData drawData=new DrawData("vm-1",tmp);
    	        ddDAO.insertDrawData(sr,drawData);
    	        
    	       
    	        for(Vm vm:vmsList){
    	        	 tmp=createResourceUtilization(vm.getId());
    	             drawData=new DrawData("vm"+vm.getId(),tmp);
    	             ddDAO.insertDrawData(sr,drawData);
    	        }
    	        
    	      
    	         String tmp1=createCloudletsNum();
    	         DrawData drawData1=new DrawData("cloudletsNum",tmp1);
    	         ddDAO.insertDrawData(sr,drawData1);
    	     
    	       
    	         tmp=createCloudletTime();
    	         DrawData drawData2=new DrawData("cloudletsTime",tmp);
    	         ddDAO.insertDrawData(sr,drawData2);
    		}
       
         
    }
    
    public CustomerReport(SimulationRegistry sr,
			NetDatacenterBroker netDatacenterBroker) {
    	DrawDataDAO ddDAO=new DrawDataDAO();
      for(CustomerRegistry cr:sr.getCustomerList()){    
    	
    	
        this.name = cr.getName();        
        List<Vm> vmsList = netDatacenterBroker.getVmList();
        List<Cloudlet> cloudletsList = netDatacenterBroker.getCloudletSubmittedList();
        //Get all virtual machines resource utilization data from the database
        this.vmsUsedRam = new HashMap<Integer, TreeMap<Double, Double>>();
        this.vmsUsedCpu = new HashMap<Integer, TreeMap<Double, Double>>();
        this.vmsUsedBandwidth = new HashMap<Integer, TreeMap<Double, Double>>();
        cloudletsPerVm = new TreeMap<String, Integer>();
        
        ReportDataDAO rdDAO = new ReportDataDAO();
        for(Vm vm : vmsList) {
            int vmId = (int) vm.getId();
            vmsUsedRam.put(vmId, rdDAO.getVmUsedResources(sr,"RAM", this.name, vmId));
            vmsUsedCpu.put(vmId, rdDAO.getVmUsedResources(sr,"CPU", this.name, vmId));
            vmsUsedBandwidth.put(vmId, rdDAO.getVmUsedResources(sr,"BANDWIDTH", this.name, vmId));
            cloudletsPerVm.put("VM"+vmId, 0);
        }
        
        //Get the overall resource utilization from this customer
        this.overallUsedRam = rdDAO.getCustomerOverallData(sr,"RAM", this.name);
        this.overallUsedCpu = rdDAO.getCustomerOverallData(sr,"CPU", this.name);
        this.overallUsedBandwidth = rdDAO.getCustomerOverallData(sr,"BANDWIDTH", this.name);
        if(this.overallUsedBandwidth==null||this.overallUsedCpu==null||this.overallUsedRam==null){
        	System.out.println( "Error overallUsed is null.");
            return ;
        }
      
        cloudletsTimesOfExecution = new TreeMap<Integer, List<Double>>();
        System.out.println("!!!!!!!"+cloudletsList.size());
        
        for(Cloudlet cloudlet : cloudletsList) {
            List<Double> timesOfExecution = new ArrayList<Double>();
            timesOfExecution.add(cloudlet.getExecStartTime());
            timesOfExecution.add(cloudlet.getFinishTime());
            cloudletsTimesOfExecution.put(cloudlet.getCloudletId(), timesOfExecution);
            cloudletsPerVm.put("VM"+cloudlet.getVmId(), cloudletsPerVm.get("VM"+cloudlet.getVmId()) + 1);
        }
       
        String tmp=createResourceUtilization(-1);
        DrawData drawData=new DrawData("vm-1",tmp);
        ddDAO.insertDrawData(sr,drawData);
        
       
        for(Vm vm:vmsList){
        	 tmp=createResourceUtilization(vm.getId());
             drawData=new DrawData("vm"+vm.getId(),tmp);
             ddDAO.insertDrawData(sr,drawData);
        }
        
      
         String tmp1=createCloudletsNum();
         DrawData drawData1=new DrawData("cloudletsNum",tmp1);
         ddDAO.insertDrawData(sr,drawData1);
     
       
         tmp=createCloudletTime();
         DrawData drawData2=new DrawData("cloudletsTime",tmp);
         ddDAO.insertDrawData(sr,drawData2);
      }
	}

	/**
     * Gets the name of the customer.
     * 
     * @return  a string that contains the name of the customer.
     */
    public String getName() {
		return name;
	}
    
    private String createResourceUtilization(int vmid){
    	StringBuilder dataStringBuilder = new StringBuilder("{  title : {  text: 'Resource utilization' },  "
    			+ " tooltip : { trigger: 'axis' }, "
    			+ "legend: { data:['CPU(MIPS)','RAM(MB)','Bandwidth(Mbps)'] }, "
    			+ "xAxis : [  {type : 'category',  boundaryGap : false, data :");
    	if(vmid==-1){
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
    		TreeMap<Double, Double> nowVmCpuUsed=vmsUsedCpu.get(vmid);
    		TreeMap<Double, Double> nowVmRamUsed=vmsUsedRam.get(vmid);
    		TreeMap<Double, Double> nowVmBandUsed=vmsUsedBandwidth.get(vmid);
    		dataStringBuilder.append(getFirstDoubleData(overallUsedRam));
    		dataStringBuilder.append(" } ], yAxis : [ { type : 'value', axisLabel : { formatter: '{value} '   } } ], "
    				+ "series : [ {  name:'CPU(MIPS)', type:'line', data:");
    		dataStringBuilder.append(getSecondDataString(nowVmCpuUsed));
    		dataStringBuilder.append(", markPoint : { data : [  {type : 'max', name: 'max'}, {type : 'min', name: 'min'} ]  },"
    				+ " markLine : { data : [  {type : 'average', name: 'aver'} ]} },{  name:'RAM(MB)', type:'line', data:");
    		dataStringBuilder.append(getSecondDataString(nowVmRamUsed));
    		dataStringBuilder.append(", markPoint : {  data : [ {type : 'max', name: 'max'}, {type : 'min', name: 'min'}]  }, "
    				+ " markLine : { data : [ {type : 'average', name : 'aver'} ]  } }, { name:'Bandwidth(Mbps)', type:'line',  data:");
    		dataStringBuilder.append(getSecondDataString(nowVmBandUsed));
    		dataStringBuilder.append(", markPoint : {  data : [{type : 'max', name: 'max'},  {type : 'min', name: 'min'}  ] }, "
    				+ "markLine : { data : [ {type : 'average', name : 'aver'} ]} } ]}");
    		
    	}
    	return dataStringBuilder.toString();
    }
    
    private String createCloudletsNum(){
    	StringBuilder dataStringBuilder = new StringBuilder("{  title : {  text: 'Cloudlets Number' }, "
    			+ " tooltip : { trigger: 'axis' }, "
    			+ "legend: { data:['cloudlets'] }, "
    			+ "xAxis : [  {type : 'category',  boundaryGap : true, data :");
    	dataStringBuilder.append(getFirstDataString(cloudletsPerVm));
		dataStringBuilder.append(" } ], yAxis : [ { type : 'value', axisLabel : { formatter: '{value} '   } } ], "
				+ "series : [ {  name:'cloudlets', type:'bar', data:");
		dataStringBuilder.append(getSecondIntegerData(cloudletsPerVm));
		dataStringBuilder.append(", markPoint : {  data : [{type : 'max', name: 'max'},  {type : 'min', name: 'min'}  ] }, "
				+ "markLine : { data : [ {type : 'average', name : 'aver'} ]} } ]}");
		
		return dataStringBuilder.toString();
    }
 
    private String createCloudletTime(){
    	StringBuilder dataStringBuilder = new StringBuilder("{  title : {  text: 'Cloudlets Execution Time' }, "
    			+ " tooltip : { trigger: 'axis' }, "
    			+ "legend: { data:['start_time','finish_time'] }, "
    			+ "xAxis : [  {type : 'category',  boundaryGap : false, data :");
    	dataStringBuilder.append(getFirstDataString(cloudletsTimesOfExecution));
    	dataStringBuilder.append(" } ], yAxis : [ { type : 'value', axisLabel : { formatter: '{value} '   } } ], "
				+ "series : [ {  name:'start_time', type:'line', data:");
		dataStringBuilder.append(getIndexTime(0,cloudletsTimesOfExecution));
		dataStringBuilder.append(", markPoint : { data : [  {type : 'max', name: 'max'}, {type : 'min', name: 'min'} ]  },"
				+ " markLine : { data : [  {type : 'average', name: 'aver'} ]} },{  name:'finish_time', type:'line', data:");
		dataStringBuilder.append(getIndexTime(1,cloudletsTimesOfExecution));
		dataStringBuilder.append(", markPoint : {  data : [{type : 'max', name: 'max'},  {type : 'min', name: 'min'}  ] }, "
				+ "markLine : { data : [ {type : 'average', name : 'aver'} ]} } ]}");
		return dataStringBuilder.toString();
    }

    
    private String  getIndexTime(int i, TreeMap<Integer, List<Double>> cloudletmap) {
    	 StringBuilder dataStringBuilder = new StringBuilder("[");
         Iterator<Integer> iterator = cloudletmap.keySet().iterator();
         while (iterator.hasNext()) {
             Integer key = iterator.next();
             dataStringBuilder.append("'");
             dataStringBuilder.append(df.format(cloudletmap.get(key).get(i)));
             dataStringBuilder.append("',");
         }
         dataStringBuilder.deleteCharAt(dataStringBuilder.lastIndexOf(","));
         dataStringBuilder.append("]");
         
         return dataStringBuilder.toString();
	}

	private String getSecondDataString(TreeMap<Double, Double> dataMap) {
        StringBuilder dataStringBuilder = new StringBuilder("[");
        Iterator<Double> iterator = dataMap.keySet().iterator();
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
	
	private String getSecondIntegerData(TreeMap<String, Integer> dataMap) {
        StringBuilder dataStringBuilder = new StringBuilder("[");
        Iterator<?> iterator = dataMap.keySet().iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            dataStringBuilder.append("'");
            dataStringBuilder.append(dataMap.get(key));
            dataStringBuilder.append("',");
        }
        dataStringBuilder.deleteCharAt(dataStringBuilder.lastIndexOf(","));
        dataStringBuilder.append("]");
        
        return dataStringBuilder.toString();
	}

	private String getFirstDataString(TreeMap<?, ?> dataMap) {
        StringBuilder dataStringBuilder = new StringBuilder("[");
        Iterator<?> iterator = dataMap.keySet().iterator();
        
        while (iterator.hasNext()) {
            Object key = iterator.next();
            dataStringBuilder.append("'");
            dataStringBuilder.append(key);
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
            

}
