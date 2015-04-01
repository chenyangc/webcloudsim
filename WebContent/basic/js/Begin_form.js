function loadCustomerInfo(){
	$.ajax({
		type:"get",
		url:"customerGeneralUserInfo",
		dataType:"json",
		success:function(data){

			$('#Customer_users_form').form('load',{
				Number_customers:data.numberOfService,
				Cloudlets_minute:data.numberOfCloudletEachService,
				Average_cloudlets:data.averageLengthOfCloudlet,
				Average_file_size:data.averageFileSizeOfCloudlet,
				Average_output_size:data.averageOutputOfCloudlet
			});
			
			
		},
		error:function(){
			alert("系统异常");
		}
	});
	$.ajax({
		type:"get",
		url:"customerGeneralVMInfo",
		dataType:"json",
		success:function(data){		
			$('#Customer_virtual_machines_form').form('load',{
			    Number_virtual_machine:data.numberOfVM,
			    Average_image_size:data.averageSize,
			    Average_RAM:data.averageRAM,
			    Average_bandwidth:data.averageBandwidth
			});
					
			
		},
		error:function(){
			alert("系统异常");
		}
	});
}

function saveCosts(){
	var costPerSec = $("#Processing_cost").val();
	var costPerMem = $("#Memory_cost").val();
	var costPerStorage = $("#Storage").val();
	var costPerBw = $("#Bandwidth").val();
	
	$.ajax({
		type:"post",
		url:"updateCosts",
		data:{
			costPerSec:costPerSec,
			costPerMem:costPerMem,
			costPerStorage:costPerStorage,
			costPerBw:costPerBw
		},
		dataType:"json",
		success:function(data){
			if(data.statusCode == 1){
				//更新成功
				alert("update successfully");
				$.ajax({
					type:"get",
					url:"providerCosts",
					dataType:"json",
					success:function(data){
						$('#Special_provider_costs_form').form('load',{
							Processing_cost:data.costPerSec,
							Memory_cost: data.costPerMem,
							Storage: data.costPerStorage,
							Bandwidth: data.costPerBw
						});
						
					},
					error:function(){
						alert("系统异常");
					}
				});
			}
			else{
				//更新失败
				alert("更新异常");
			}
			
		},
		error:function(){
			alert("系统异常");
		}
	});
	
}

function saveSettings(){
	var allocationPolicyAlias = $("#Allocation_Policy").combobox('getText');
	var architecture = $("#Architecture").combobox('getText');
	var upperUtilizationThreshold = $("#Upper_Threshold").val();
	var os = $("#Operating_System").combobox('getText');
	var lowerUtilizationThreshold = $("#Lower_Threshold").val();
	var vmm = $("#Hypervisor").combobox('getText');
	var vmMigration = $("#VM_Migrations").combobox('getText');
	var schedulingInterval = $("#Scheduling_interval").val();
	var monitoringInterval = $("#Monitoring_interval").val();
//	alert(vmMigration);
	if(vmMigration == 'true'){
		vmMigration = true;
	}
	else{
		vmMigration = false;
	}
	
	$.ajax({
		type:"post",
		url:"updateProviderSettings",
		data:{
			allocationPolicyAlias:allocationPolicyAlias,
			architecture:architecture,
			upperUtilizationThreshold:upperUtilizationThreshold,
			os:os,
			lowerUtilizationThreshold:lowerUtilizationThreshold,
			vmm:vmm,
			vmMigration:vmMigration,
			schedulingInterval:schedulingInterval,
			monitoringInterval:monitoringInterval		
		},
		dataType:"json",
		success:function(data){
			if(data.statusCode == 1){
				//更新成功
				alert("update successfully");
				$.ajax({
					type:"get",
					url:"providerSettings",
					dataType:"json",
					success:function(data){
						$('#Speial_Provider_settings_form').form('load',{
							Allocation_Policy: data.allocationPolicyAlias,
							Architecture: data.architecture,
							Upper_Threshold: data.upperUtilizationThreshold,
							Operating_System: data.os,
							Lower_Threshold: data.lowerUtilizationThreshold,
							Hypervisor: data.vmm,
							VM_Migrations: data.vmMigration,
							Scheduling_interval: data.schedulingInterval,
							Monitoring_interval: data.monitoringInterval
						});
						
						$("#Allocation_Policy").attr("value" ,data.allocationPolicyAlias);
						$("#Operating_System").attr("value" ,data.os);
						$("#Architecture").attr("value" ,data.architecture);
						$("#VM_Migrations").attr("value" ,data.vmMigration);
						$("#Hypervisor").attr("value" ,data.vmm);					
					},
					error:function(){
						alert("系统异常");
					}
				});
			}
			else{
				//更新失败
				alert("后台系统异常");
			}
			
		},
		error:function(){
			//异常
			alert("系统异常");
		}
	});
}
function saveCustomerUtilization(){	
	var datacenterBroker=$("#datacenter_broker").combobox('getText');
	var customerRely=$("#Special_customer_rely_combobox").combobox('getText');
	var maxLength=$("#Maximum_length").val();
	var cloudletsNumber=$("#Processing_elements").val();
	var fileSize=$("#File_size").val();
	var outputSize=$("#Output_size").val();
	var cpuModel=$("#cpu_model").combobox('getText');
	var ramModel=$("#ram_model").combobox('getText');
	var bwModel=$("#bw_model").combobox('getText');
	var location=$("#location").combobox('getValue');
	$.ajax({
		type:"post",
		url:"updateCustomerUtilization",
		data:{
			datacenterBroker:datacenterBroker,
			customerRely:customerRely,
			maxLength:maxLength,
			cloudletsNumber:cloudletsNumber,
			fileSize:fileSize,
			outputSize:outputSize,
			cpuModel:cpuModel,
			ramModel:ramModel,
			bwModel:bwModel,
			location:location
			
		},
		dataType:"json",
		success:function(data){
			if(data.statusCode == 1){
//				$.cookie("location",location);
//				$.cookie("customerRely",customerRely);
//				console.log($.cookie("location"));
//			
//				$("#Special_customer_rely_combobox").attr("value" ,$.cookie("customerRely"));
				//更新成功
				alert("update successfully");				
			}
			else{
				//更新失败
				alert("后台系统异常");
			}
			
		},
		error:function(){
			//异常
			alert("系统异常");
		}
	});
	
}

$(function()
{
	loadCustomerInfo();		
	$.ajax({
		type:"get",
		url:"providerGeneralInfo",
		dataType:"json",
		success:function(data){
			$('#Provider_general_information_form').form('load',{
				Number_datacenters:data.numberDatacenters,
				Number_hosts: data.numberHosts,
				Number_processing_units: data.numberProcessingUnits,
				Processing_capacity: data.processingCapacity,
				Storage_capacity: data.storageCapacity,
				Total_amount_RAM: data.totalAmountRam
			});
			
		},
		error:function(){
			alert("系统异常");
		}
	});
	
	$.ajax({
		type:"get",
		url:"providerSettings",
		dataType:"json",
		success:function(data){
			$('#Speial_Provider_settings_form').form('load',{
				Allocation_Policy: data.allocationPolicyAlias,
				Architecture: data.architecture,
				Upper_Threshold: data.upperUtilizationThreshold,
				Operating_System: data.os,
				Lower_Threshold: data.lowerUtilizationThreshold,
				Hypervisor: data.vmm,
				VM_Migrations: data.vmMigration,
				Scheduling_interval: data.schedulingInterval,
				Monitoring_interval: data.monitoringInterval
			});
			
			$("#Allocation_Policy").attr("value" ,data.allocationPolicyAlias);
			$("#Operating_System").attr("value" ,data.os);
			$("#Architecture").attr("value" ,data.architecture);
			$("#VM_Migrations").attr("value" ,data.vmMigration);
			$("#Hypervisor").attr("value" ,data.vmm);
		},
		error:function(){
			alert("系统异常");
		}
	});
	
	$.ajax({
		type:"get",
		url:"providerCosts",
		dataType:"json",
		success:function(data){
			$('#Special_provider_costs_form').form('load',{
				Processing_cost:data.costPerSec,
				Memory_cost: data.costPerMem,
				Storage: data.costPerStorage,
				Bandwidth: data.costPerBw
			});
			
		},
		error:function(){
			alert("系统异常");
		}
	});
	
	$('#Special_customer_rely_combobox').combobox({
        url:'fetchRelyList',
        editable:false,
        required:true,
        valueField:'id',
        textField:'customerName',
        panelHeight:'50'
    });


})


