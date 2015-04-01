function Customer_add_customer()
{
     $('#Customer_add_customer_dialog').dialog
	 ({
           title: 'Add Service',
           width: 400,
           height: 120,
           closed: false,
           cache: false,
           href:'dialog/Customer_add_customer.html',
           modal: true,
           buttons:
		   [{
               text:'Save',
               handler:function()
			   {
				   var Customer_judgeadd = $('#Customer_add_customer_name').textbox('getValue');
				   if(Customer_judgeadd)
				   {
					   //alert(Customer_judgeadd);
					   $.ajax({
						   type:"post",
						   url:"addCustomer",
						   data:{
							   customerName:Customer_judgeadd
						   },
						   dataType:"json",
						   success:function(data){
							   for(var statusCode in data){
								  if(data[statusCode] == 1){
									   $('#Customer_tree').tree('reload');
									   $('#Customer_add_customer_dialog').window('close');
									   //成功生成customer
								   }
								   else if(data[statusCode] == 2){
									   $('#Customer_add_customer_dialog').window('close');
									   //系统异常
								   }
								   else if(data[statusCode] == 0){
									   alert("name same");
									   //同名碰撞
								   }
								   else{
									   alert("程序错误");
								   }
							   }
							   $('#Customer_network_table').datagrid('reload');
//							   $('#Customer_users_form').form('reset');
//							   $('#Customer_virtual_machines_form').form('reload');
							   loadCustomerInfo();
							   
						   },
						   error:function(){
							   alert("系统异常，请稍后再试！");
						   }
					   });
				   }
			   }
            },
			{
               text:'Close',
               handler:function()
			   {
				   $('#Customer_add_customer_dialog').window('close');
			   }
			}
		   ]
	 });
}

function Customer_delete_customer()
{
    $('#Customer_delete_customer_dialog').dialog
	({
		title: 'Delete Service',
		width: 400,
		height: 160,
		closed: false,
		cache: false,
		href:'dialog/Customer_delete_customer.html',
		modal: true,
		buttons:
		[{
			text:'Delete',
			handler:function()
			{
				   var Customer_judgedelete = $('#Customer_delete_customer_name').combobox('getText');
				   if(Customer_judgedelete)
				   {
					   //alert(Customer_judgedelete);
					   $.ajax({
						   type:"post",
						   url:"deleteCustomer",
						   data:{
							   customerName:Customer_judgedelete
						   },
						   dataType:"json",
						   success:function(data){
							   
							   for(var statusCode in data){
								   if(data[statusCode] == 1){
									   $('#Customer_delete_customer_name').combobox('reload');
									   $('#Customer_tree').tree('reload');
									   $('#Customer_delete_customer_dialog').window('close');
									   //成功删除customer
								   }
								   else if(data[statusCode] == 2){
									   //系统异常
								   }
								   else if(data[statusCode] == 0){
									   alert("delete fail");
									   //无该名称
								   }
								   else{
									   $('#Customer_delete_customer_dialog').window('close');
									   alert("程序错误");
								   }
							   }
							   
							   $('#Customer_network_table').datagrid('reload');
							   $('#Customer_delete_customer_dialog').window('close');							 
//							   $('#Customer_users_form').form('reload');
//							   $('#Customer_virtual_machines_form').form('reload');
							   loadCustomerInfo();	
						   },
						   error:function(){
							   alert("不好意思，服务器挂了。");
						   }
					   });
				   }
			}
		 },
		 {
			 text:'Close',
			 handler:function(){$('#Customer_delete_customer_dialog').window('close');}
		 }],
		 onLoad : function()
		 {
			$.ajax({
					   type:"get",
					   url:"customerList",
					   dataType:"json",
					   success:function(data){						  
						   		$('#Customer_delete_customer_name').combobox({	
								data:data[0].children,
							    valueField:'id',
							    textField:'text',
							    editable:false,
							    required:true,
							    panelHeight:'50'
							});			
						   										   					   								   							 							 
							  
						}
						   
				   });			
		 }
	});
}

function Provider_add_datacenter()
{
    $('#Provider_add_datacenter_dialog').dialog
	({
        title: 'Add Datacenter',
		width: 400,
		height: 120,
		closed: false,
		cache: false,
		href:'dialog/Provider_add_datacenter.html',
		modal: true,
		buttons:
		[{
			text:'Save',
			handler:function()
			{
				   var Provider_judgeadd = $('#Provider_add_provider_name').textbox('getValue');
				   if(Provider_judgeadd)
				   {
					   $.ajax({
						   type:"post",
						   url:"addProvider",
						   data:{
							   providerName:Provider_judgeadd
						   },
						   dataType:"json",
						   success:function(data){
							   for(var statusCode in data){
								   if(data[statusCode] == 1){
									  
									   $('#Provider_add_datacenter_dialog').window('close');
									   $('#Provider_tree').tree('reload');
								   }
								   else if(data[statusCode] == 2){
									   //系统异常
									   alert("系统异常,可能未添加仿真环境，请重试");
								   }
								   else if(data[statusCode] == 0){
									   //同名碰撞
									   alert("同名碰撞");
								   }
								   else{
									   alert("程序错误");
								   }
							   }
							   
							   $("#Provider_network_table").datagrid('reload');
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
							   
						   },
						   error:function(){
							   alert("系统异常，请稍后再试！");
						   }
					   });
				   }
			}
		},{
			text:'Close',
			handler:function(){$('#Provider_add_datacenter_dialog').window('close');}
	    }]
	});
}

function Provider_delete_datacenter()
{
    $('#Provider_delete_datacenter_dialog').dialog
	({
		title: 'Delete Datacenter',
		width: 400,
		height: 160,
		closed: false,
		cache: false,
		href:'dialog/Provider_delete_datacenter.html',
		modal: true,
		buttons:
		[{
			text:'delete',
			handler:function()
			{
				var provider_judgedelete = $("#Provider_delete_customer_name").combobox('getText');
				if(provider_judgedelete)
				{
					alert(provider_judgedelete);
					$.ajax({
						   type:"post",
						   url:"removeDatacenter",
						   data:{
							   providerName:provider_judgedelete
						   },
						   dataType:"json",
						   success:function(data){
							   for(var statusCode in data){
								   if(data[statusCode] == 1){
									   console.log("remove success");
									   //成功删除provider
									   
								   }
								   else if(data[statusCode] == 2){
									   //系统异常
									   alert("系统异常,无法删除，请通知管理员");
								   }
								   else if(data[statusCode]== 0){
									   //无该名称
									   alert("无该名称");
								   }
								   else{
									   alert("程序错误");
								   }
							   }
							   $('#Provider_tree').tree('reload');
							   $('#Provider_delete_datacenter_dialog').window('close');
							   $("#Provider_network_table").datagrid('reload');
							   
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
						   },
						   error:function(){
							   alert("不好意思，服务器挂了。");
						   }
					   });
				}
			}
		 },
		 {
			 text:'Close',
			 handler:function(){$('#Provider_delete_datacenter_dialog').window('close');}
		}],
		onLoad : function()
		{
			$('#Provider_delete_customer_name').combobox({
                url:'datacenterName',
                editable:false,
                required:true,
                valueField:'id',
                textField:'datacenter_name',
                panelHeight:'50'
		    });
        }
	});
}

function Environment_add_environment()
{
    $('#Environment_add_environment_dialog').dialog
	({
        title: 'Add Environment',
		width: 450,
		height: 120,
		closed: false,
		cache: false,
		href:'dialog/Environment_add_environment.html',
		modal: true,
		buttons:
		[{
			text:'Save',
			handler:function()
			{
				   var Environment_judgeadd = $('#Environment_add_environment_name').textbox('getValue');
				   if(Environment_judgeadd)
				   {
					   //向后台传输这个名字，获得后台的状态码来决定下一步行为
					   $.ajax({
						   type:"post",
						   url:"addEnvironment",
						   data:{
							   environmentName:Environment_judgeadd
						   },
						   dataType:"json",
						   success:function(data){
							   for(var statusCode in data){
								   if(data[statusCode] == 1){
									   //成功生成了一个Environment
									   $('#simulationName_form').form('load',{simulationName:Environment_judgeadd});
									   $('#Environment_add_environment_dialog').window('close');
									   $.cookie("environmentName",Environment_judgeadd);																											
									   $('#Provider_tree').tree('reload');
									  
									   $('#Customer_tree').tree('reload');
									   $('#Provider_network_table').datagrid('reload');
									   $('#Customer_network_table').datagrid('reload');
								   }
								   else if(data[statusCode] == 2){
									   //系统异常
									   alert("系统异常");
								   }
								   else if(data[statusCode] == 0){
									   //同名碰撞
									   alert("同名碰撞");
								   }
								   else{
									   alert("程序错误");
								   }
							   }
							   
						   },
						   error:function(){
							   alert("不好意思，服务器挂了。");
						   }
					   });
				   }
			}
		},{
			text:'Close',
			handler:function(){$('#Environment_add_environment_dialog').window('close');}
	    }]
	});
}
function Run_simulation(){
	 $('#Wait_Html').window
	 ({
           title: 'Wait',
           width: 490,
           height: 390,
           closable: false,
           collapsible:false,
           minimizable:false,
           maximizable:false,
           href:'dialog/Wait_Html.html'
	
	 });		
	
	$.ajax({
		type:"get",
		url:"runSimulation",
		dataType:"json",
		success:function(data){
			   for(var statusCode in data){
				   if(data[statusCode] == 1){
					   console.log("run success");					  
					   $('#Wait_Html').window('close');
					   alert("The Simulation has finished ,please check out result in the Result View");
					   location.href ="Provider_simulation_report.jsp";
				   }
				   else {
					   console.log("run fail");		
				   }				 
			   }
			
			
		},
		error:function(){
			alert("系统异常");
		}
	});
	return false;
	
}