$(function()
{
	$('#Customer_network_table').datagrid({
	title:'Network',
    url:'fetchCustomersNetwork',
    columns:[[
            {field:'id',title:'Id',width:120},
            {field:'source',title:'Source',width:120},
            {field:'destination',title:'Destination',width:120},
            {field:'bandwidth',title:'Bandwidth',width:120},
            {field:'latency',title:'Latency',width:120}
            ]],
	singleSelect:true
    });
	
	$('#Provider_network_table').datagrid({
	title:'Network',
    url:'fetchCustomersNetwork',
    columns:[[
            {field:'id',title:'Id',width:120},
            {field:'source',title:'Source',width:120},
            {field:'destination',title:'Destination',width:120},
            {field:'bandwidth',title:'Bandwidth',width:120},
            {field:'latency',title:'Latency',width:120}
            ]],
	singleSelect:true
    });
	
	
	
	var Special_customer_virtual_machine_editIndex = undefined;
    function Special_customer_virtual_machine_endEditing()
    {
        if (Special_customer_virtual_machine_editIndex == undefined){return true}
        if ($('#Special_customer_virtual_machine_table').datagrid('validateRow', Special_customer_virtual_machine_editIndex))
	    {
            $('#Special_customer_virtual_machine_table').datagrid('endEdit', Special_customer_virtual_machine_editIndex);
            Special_customer_virtual_machine_editIndex = undefined;
	        return true;
        } 
	    else 
	    {
	        return false;
        }
    };
   
	$('#Special_customer_virtual_machine_table').datagrid({
	title:'Virtual Machines',
    url:'fetchSpecialCustomerVM',
//    queryParams: {
//		customerName: 'customer1'		
//	},   
	onClickRow:function(index,row)
	{
        if (Special_customer_virtual_machine_editIndex != index)
	    {
	        if (Special_customer_virtual_machine_endEditing())
	        {
	            $('#Special_customer_virtual_machine_table').datagrid('selectRow', index).datagrid('beginEdit', index);
		        Special_customer_virtual_machine_editIndex = index;
	        }
		    else
		    {
	           $('#Special_customer_virtual_machine_table').datagrid('selectRow', Special_customer_virtual_machine_editIndex);
            }
        }
	},
    columns:[[
            {field:'id',title:'ID',width:50,align:'center'},
            {field:'amount',title:'Amount',width:100,align:'center',editor:{type:'numberbox',options:{min:0,required:true}}},
			{field:'hostId',title:'HostId',width:100,align:'center',editor:{type:'combobox',
				options:{
					url:'fetchHostId',						
					valueField:'id',
					textField:'hostid',
					editable:false,
					required:true,
					panelHeight:'50'}}},	
            {field:'size',title:'Image size',width:100,align:'center',editor:{type:'numberbox',options:{min:0,required:true}}},
            {field:'pesNumber',title:'Processing elements',width:150,align:'center',editor:{type:'numberbox',options:{min:0,required:true}}},
            {field:'mips',title:'MIPS',width:100,align:'center',editor:{type:'numberbox',options:{min:0,required:true}}},
			{field:'ram',title:'RAM',width:100,align:'center',editor:{type:'numberbox',options:{min:0,required:true}}},
			{field:'bw',title:'Bandwidth',width:100,align:'center',editor:{type:'numberbox',options:{min:0,required:true}}},
			{field:'priority',title:'Priority',width:100,align:'center',editor:{type:'numberbox',options:{min:0,required:true}}},
			{field:'vmm',title:'hypervisor',width:100,align:'center',editor:{type:'combobox',options:{
								valueField:'value',
								textField:'text',
								editable:false,
								required:true,
								data:[{value:'Xen',text:'Xen'},{value:'KVM',text:'KVM'}]}}},
			{field:'schedulingPolicyAlias',title:'Scheduling Policy',width:150,align:'center',editor:{
							type:'combobox',options:{
								valueField:'value',
								textField:'text',
								editable:false,
								required:true,
								data:[{value:'Time shared',text:'Time shared'},{value:'Space shared',text:'Space shared'},{value:'Dynamic workload',text:'Dynamic workload'}]}}}
								
		]],
	    singleSelect:true,
		toolbar:[{
			text:'Remove',
			iconCls:'icon-remove',
		    handler:function()
			{
		    	var row =  $('#Special_customer_virtual_machine_table').datagrid('getSelected');
				if (Special_customer_virtual_machine_editIndex == undefined){return}
				var removed_vm =  $('#Special_customer_virtual_machine_table').datagrid('selectRow', Special_customer_virtual_machine_editIndex);                
				$('#Special_customer_virtual_machine_table').datagrid('cancelEdit', Special_customer_virtual_machine_editIndex).datagrid('deleteRow', Special_customer_virtual_machine_editIndex);
                Special_customer_virtual_machine_editIndex = undefined;
                if(row)
					{					
						$.ajax({
							   type:"post",
							   url:"removeCustomerVm",
							   data:{
								   "removeVm.id":row.id,
								   "removeVm.amount":row.amount,								 
								   "removeVm.bw":row.bw,
								   "removeVm.hostId":row.hostId,
								   "removeVm.mips":row.mips,
								   "removeVm.pesNumber":row.pesNumber,
								   "removeVm.priority":row.priority,
								   "removeVm.ram":row.ram,
								   "removeVm.schedulingPolicyAlias":row.schedulingPolicyAlias,
								   "removeVm.size":row.size,
								   "removeVm.vmm":row.vmm   								  
							   },
							   dataType:"json",
							   success:function(data){									   	  
									   if(data.statusCode == 1){										
										   $('#Special_customer_virtual_machine_table').datagrid('reload');
										   //成功删除customer_vm
									   }
									   else if(data.statusCode == 2){
										   //系统异常
									   }
									   else if(data.statusCode== 0){
										   //无该名称
									   }
									   else{
										   	alert("程序错误");
									   }
								  
							   },
							   error:function(){
								   alert("不好意思，服务器挂了。");
							   }
						   });
					
	            }
                
			}
		},{
			text:'Save',
			iconCls:'icon-save',
			handler:function()
			{
				 var row =  $('#Special_customer_virtual_machine_table').datagrid('getSelected');
                if (Special_customer_virtual_machine_endEditing()){                	
                	$('#Special_customer_virtual_machine_table').datagrid('acceptChanges');                	                
  	             
                	if(row)
  					{					
  						$.ajax({
  							   type:"post",
  							   url:"updateCustomerVm",
  							   data:{  								 
  								   "updateVm.id":row.id,
  								   "updateVm.amount":row.amount,								 
  								   "updateVm.bw":row.bw,
  								   "updateVm.hostId":row.hostId,
  								   "updateVm.mips":row.mips,
  								   "updateVm.pesNumber":row.pesNumber,
  								   "updateVm.priority":row.priority,
  								   "updateVm.ram":row.ram,
  								   "updateVm.schedulingPolicyAlias":row.schedulingPolicyAlias,
  								   "updateVm.size":row.size,
  								   "updateVm.vmm":row.vmm   								  
  							   },
  							   dataType:"json",
  							   success:function(data){									   	  
  									   if(data.statusCode == 1){
  										
  										   $('#Special_customer_virtual_machine_table').datagrid('reload');
  										   //成功删除customer_vm
  									   }
  									   else if(data.statusCode == 2){
  										   //系统异常
  									   }
  									   else if(data.statusCode== 0){
  										   //无该名称
  									   }
  									   else{
  										   	alert("程序错误");
  									   }
  								  
  							   },
  							   error:function(){
  								   alert("不好意思，服务器挂了。");
  							   }
  						   });
  					
  	            }
               	 
                }
                   
			}
		},{
			text:'Reject',
			iconCls:'icon-undo',
			handler:function()
			{
				$('#Special_customer_virtual_machine_table').datagrid('rejectChanges');
	            Special_customer_virtual_machine_editIndex = undefined;
			}
		},{
			text:'Append',
			iconCls:'icon-add',
			handler:function()
			{
 		         	    	   $.ajax({
							   type:"get",
							   url:"addCustomersVm",						  
							   dataType:"json",
							   success:function(data){									   	  
									   if(data.statusCode == 1){										
										   $('#Special_customer_virtual_machine_table').datagrid('reload');
										   //成功删除customer_vm
									   }
									   else if(data.statusCode == 2){
										   //系统异常
									   }
									   else if(data.statusCode== 0){
										   //无该名称
									   }
									   else{
										   	alert("程序错误");
									   }
								  
							   },
							   error:function(){
								   alert("不好意思，服务器挂了。");
							   }
						   });
	            
            }
		
		}]
    });
	
	
	
    var Special_customer_network_editIndex = undefined;
    function Special_customer_network_endEditing()
    {
        if (Special_customer_network_editIndex == undefined){return true}
        if ($('#Special_customer_network_table').datagrid('validateRow', Special_customer_network_editIndex))
	    {
            $('#Special_customer_network_table').datagrid('endEdit', Special_customer_network_editIndex);
            Special_customer_network_editIndex = undefined;
	        return true;
        } 
	    else 
	    {
	        return false;
        }
    }
	$('#Special_customer_network_table').datagrid({
	title:'Network',
    url:'fetchSpecialCustomerNetwork',
	onClickRow:function(index,row)
	{
		if (Special_customer_network_editIndex != index)
	    {
	        if (Special_customer_network_endEditing())
		    {
	            $('#Special_customer_network_table').datagrid('selectRow', index).datagrid('beginEdit', index);
		        Special_customer_network_editIndex = index;
	        }
		    else
		    {
	            $('#Special_customer_network_table').datagrid('selectRow', Special_customer_network_editIndex);
            }
        }
	},
    columns:[[
            {field:'id',title:'Id',width:120,align:'center'},
            {field:'source',title:'Source',width:120,align:'center'},
            {field:'destination',title:'Destination',width:120,align:'center'},
            {field:'bandwidth',title:'Bandwidth',width:120,align:'center',editor:{type:'numberbox',options:{precision:1,min:0,required:true}}},
            {field:'latency',title:'Latency',width:120,align:'center',editor:{type:'numberbox',options:{precision:1,min:0,required:true}}}	     		
            ]],
	singleSelect:true,
	toolbar:[{
			text:'Save',
			iconCls:'icon-save',
			handler:function()
			{
				var row =  $('#Special_customer_network_table').datagrid('getSelected'); 
				if (Special_customer_network_endEditing())
	            {
                   $('#Special_customer_network_table').datagrid('acceptChanges');  				                           	                                	                  	               
                 	if(row)
   					{					
   						$.ajax({
   							   type:"post",
   							   url:"saveCustomerNetwork",
   							   data:{
   							
   								   "saveNetwork.destination":row.destination,
   								   "saveNetwork.bandwidth":row.bandwidth,								 
   								   "saveNetwork.latency":row.latency,
   								  "saveNetwork.id":row.id,
   								  "saveNetwork.source":row.source
   							   },
   							   dataType:"json",
   							   success:function(data){									   	  
   									   if(data.statusCode == 1){										
   										   $('#Special_customer_network_table').datagrid('reload');
   										   //成功删除customer_vm
   									   }
   									   else if(data.statusCode == 2){
   										   //系统异常
   									   }
   									   else if(data.statusCode== 0){
   										   //无该名称
   									   }
   									   else{
   										   	alert("程序错误");
   									   }
   								  
   							   },
   							   error:function(){
   								   alert("不好意思，服务器挂了。");
   							   }
   						   });
   					
   	            }
                	                                 
              }
			}
		},{
			text:'Reject',
			iconCls:'icon-undo',
			handler:function()
			{
				$('#Special_customer_network_table').datagrid('rejectChanges');
	            Special_customer_network_editIndex = undefined;
			}
		}]
    });
	
	
	
	
    var Special_provider_hosts_editIndex = undefined;
    function Special_provider_hosts_endEditing()
    {
        if (Special_provider_hosts_editIndex == undefined){return true}
        if ($('#Special_provider_hosts_table').datagrid('validateRow', Special_provider_hosts_editIndex))
	    {
            $('#Special_provider_hosts_table').datagrid('endEdit', Special_provider_hosts_editIndex);
            Special_provider_hosts_editIndex = undefined;
	        return true;
        } 
	    else 
	    {
	        return false;
        }
    };
	$('#Special_provider_hosts_table').datagrid({
	title:'Hosts',
    url:'fetchProviderHosts',
	onClickRow:function(index,row)
	{
        if (Special_provider_hosts_editIndex != index)
	    {
	        if (Special_provider_hosts_endEditing())
		    {
	           $('#Special_provider_hosts_table').datagrid('selectRow', index).datagrid('beginEdit', index);
		       Special_provider_hosts_editIndex = index;
	        }
		    else
		    {
	           $('#Special_provider_hosts_table').datagrid('selectRow', Special_provider_hosts_editIndex);
            }
        }
	},
	frozenColumns:[[{field:'id',title:'ID',width:50,align:'center'}]],
    columns:[[
            {field:'schedulingPolicyAlias',title:'VM scheduling',width:150,align:'center',editor:{
				type:'combobox',options:{
				valueField:'value',
				textField:'text',
				editable:false,
				data:[{value:'Time shared',text:'Time shared'},{value:'Space shared',text:'Space shared'}]}}},
            {field:'numOfPes',title:'Processing Elements',width:150,align:'center',editor:{type:'numberbox',options:{min:0,required:true}}},
            {field:'mipsPerPe',title:'MIPS',width:100,align:'center',editor:{type:'numberbox',options:{min:0,required:true}}},
			{field:'maxPower',title:'Maximum Power',width:150,align:'center',editor:{type:'numberbox',options:{required:true,min:0,precision:1}}},
			{field:'staticPowerPercent',title:'Static Power Percent',width:150,align:'center',editor:{type:'numberbox',options:{required:true,min:0,precision:3}}},
			{field:'powerModelAlias',title:'Power model',width:150,align:'center',editor:{
				type:'combobox',options:{
				valueField:'value',
				textField:'text',
				editable:false,
				data:[{value:'Linear',text:'Linear'},{value:'Square root',text:'Square root'}]}}},
			{field:'ram',title:'RAM',width:100,align:'center',editor:{type:'numberbox',options:{required:true,min:0,precision:1}}},
			{field:'ramProvisionerAlias',title:'RAM Provisioner',width:150,align:'center',editor:{
				type:'combobox',options:{
				valueField:'value',
				textField:'text',
				editable:false,
				data:[{value:'Simple',text:'Simple'}]}}},
			{field:'bw',title:'Bandwidth',width:100,align:'center',editor:{type:'numberbox',options:{required:true,min:0,precision:1}}},
			{field:'bwProvisionerAlias',title:'Bandwidth Provisioner',width:150,align:'center',editor:{
							type:'combobox',options:{
							valueField:'value',
							textField:'text',
							editable:false,
							data:[{value:'Simple',text:'Simple'}]}}},
            {field:'amount',title:'Amount',width:100,align:'center',editor:{type:'numberbox',options:{min:0,required:true}}},
			{field:'storage',title:'Storage',width:100,align:'center',editor:{type:'numberbox',options:{required:true,min:0,precision:1}}},
			{field:'peProvisionerAlias',title:'PE Provisioner',width:150,align:'center',editor:{
						type:'combobox',options:{
						valueField:'value',
						textField:'text',
						editable:false,
						data:[{value:'Simple',text:'Simple'}]}}}		
            ]],
	    singleSelect:true,
		toolbar:[{
			text:'Remove',
			iconCls:'icon-remove',
		    handler:function()
			{
		    	var row = $('#Special_provider_hosts_table').datagrid('getSelected');
                if (Special_provider_hosts_editIndex == undefined){return}
                $('#Special_provider_hosts_table').datagrid('cancelEdit', Special_provider_hosts_editIndex).datagrid('deleteRow', Special_provider_hosts_editIndex);
                Special_provider_hosts_editIndex = undefined;
                
                if(row){
                	$.ajax({
                		type:"post",
                		url:"removeProviderHost",
                		data:{
                			id:row.id
                		},
                		dataType:"json",
                		success:function(data){
                			if(data.statusCode == 1){
                				console.log("delete successfully!");
                			}
                			else{
                				alert("fail");
                			}
                			$('#Special_provider_hosts_table').datagrid('reload');
                		},
                		error:function(){
                			alert("系统异常");
                		}
                	});
                }
			}
		},{
			text:'Save',
			iconCls:'icon-save',
			handler:function()
			{
                if (Special_provider_hosts_endEditing())
	            {
                    $('#Special_provider_hosts_table').datagrid('acceptChanges');
                    var row = $('#Special_provider_hosts_table').datagrid('getSelected');
                    
                    if(row){
                    	$.ajax({
                    		type:"post",
                    		url:"updateProviderHosts",
                			data:{
                				id:row.id,
                				schedulingPolicyAlias:row.schedulingPolicyAlias,
                				numOfPes:row.numOfPes,
                				mipsPerPe:row.mipsPerPe,
                				maxPower:row.maxPower,
                				staticPowerPercent:row.staticPowerPercent,
                				powerModelAlias:row.powerModelAlias,
                				ram:row.ram,
                				ramProvisionerAlias:row.ramProvisionerAlias,
                				bw:row.bw,
                				bwProvisionerAlias:row.bwProvisionerAlias,
                				amount:row.amount,
                				storage:row.storage,
                				peProvisionerAlias:row.peProvisionerAlias
                			},
                			success:function(data){
                				if(data.statusCode == 1){
                					alert("save successfully!");
                				}
                				else{
                //					alert("fail");
                				}
                				$('#Special_provider_hosts_table').datagrid('reload');
                			},
                			error:function(){
                				alert("系统异常");
                			}
                    	});
                    }
                }
			}
		},{
			text:'Reject',
			iconCls:'icon-undo',
			handler:function()
			{
                $('#Special_provider_hosts_table').datagrid('rejectChanges');
	            Special_provider_hosts_editIndex = undefined;
			}
		},{
			text:'Append',
			iconCls:'icon-add',
			handler:function()
			{  
				$.ajax({
            		type:"get",
            		url:"addProviderHosts",
            		dataType:"json",
            		success:function(data){
            			if(data.statusCode == 1){
        //    				alert("append successfully!");
            		
            			}
            			else{
            				alert("fail!");
            			}
            			$('#Special_provider_hosts_table').datagrid('reload');
            		},
            		error:function(){
            			alert("系统异常");
            		}
            	});
            }
		
		}]
    });
	
	
    var Special_provider_san_editIndex = undefined;
    function Special_provider_san_endEditing()
    {
        if (Special_provider_san_editIndex == undefined){return true}
        if ($('#Special_provider_san_table').datagrid('validateRow', Special_provider_san_editIndex))
	    {
            $('#Special_provider_san_table').datagrid('endEdit', Special_provider_san_editIndex);
            Special_provider_san_editIndex = undefined;
	        return true;
        } 
	    else 
	    {
	      return false;
        }
    }
	$('#Special_provider_san_table').datagrid({
	title:'SAN',
    url:'fetchProviderSAN',
	onClickRow:function(index,row)
	{
        if (Special_provider_san_editIndex != index)
	    { 
	        if (Special_provider_san_endEditing())
		    {
	           $('#Special_provider_san_table').datagrid('selectRow', index).datagrid('beginEdit', index);
		       Special_provider_san_editIndex = index;
	        }
		    else
		    {
	           $('#Special_provider_san_table').datagrid('selectRow', Special_provider_san_editIndex);
            }
        }
	},
    columns:[[
            {field:'id',title:'Id',width:125,align:'center',editor:{type:'textbox',options:{required:true}}},
	        {field:'name',title:'Name',width:125,align:'center',editor:{type:'textbox',options:{required:true}}},
			{field:'capacity',title:'Capacity',width:125,align:'center',editor:{type:'numberbox',options:{min:0,required:true}}},
			{field:'bandwidth',title:'Bandwidth',width:125,align:'center',editor:{type:'numberbox',options:{min:0,required:true}}},
			{field:'networkLatency',title:'Latency',width:125,align:'center',editor:{type:'numberbox',options:{min:0,required:true}}}
            ]],
	    singleSelect:true,
		toolbar:[{
			text:'Remove',
			iconCls:'icon-remove',
		    handler:function()
			{
		    	var row = $("#Special_provider_san_table").datagrid('getSelected');
//                if (Special_provider_san_editIndex == undefined){return}
//                $('#Special_provider_san_table').datagrid('cancelEdit', Special_provider_san_editIndex);
//                Special_provider_san_editIndex = undefined;
                
                if(row){
                	$.ajax({
                		type:"post",
						url:"removeProviderSAN",
						data:{
							id:row.id
						},
						dataType:"json",
						success:function(data){
							if(data.statusCode == 1){
								//保存成功
								console.log("save successfully");
							}
							else{
								//失败
								alert("系统异常");
							}
							$("#Special_provider_san_table").datagrid('reload');
						},
						error:function(){
							alert("系统异常");
						}
                	});
                }
			}
		},{
			text:'Save',
			iconCls:'icon-save',
			handler:function()
			{
				var row = $("#Special_provider_san_table").datagrid('getSelected');
				if (Special_provider_san_endEditing()){
					$('#Special_provider_san_table').datagrid('acceptChanges');
					if(row){
						$.ajax({
							type:"post",
							url:"saveProviderSAN",
							data:{
								id:row.id,//注意ID应设置为不可更改
								name:row.name,
								capacity:row.capacity,
								bandwidth:row.bandwidth,
								networkLatency:row.networkLatency
							},
							dataType:"json",
							success:function(data){
								if(data.statusCode == 1){
									//保存成功
									alert("save successfully");
								}
								else{
									//失败
								}
								$("#Special_provider_san_table").datagrid('reload');
							},
							error:function(){
								alert("系统异常");
							}
						});
					}
				}
					
			}
		},{
			text:'Reject',
			iconCls:'icon-undo',
			handler:function()
			{
				$('#Special_provider_san_table').datagrid('rejectChanges');
	            Special_provider_san_editIndex = undefined;
	            $("#Special_provider_san_table").datagrid('reload');
			}
		},{
			text:'Append',
			iconCls:'icon-add',
			handler:function()
			{
				$.ajax({
            		type:"post",
            		url:"addProviderSAN",
            		data:{
            			name:"SAN"
            		},
            		dataType:"json",
            		success:function(data){
            			if(data.statusCode == 1){
            				alert("append successfully!");
            			}
            			else{
            				alert("fail");
            			}
            			$("#Special_provider_san_table").datagrid('reload');
            		},
            		error:function(){
            			alert("系统异常");
            		}	                		
            	});
            }
		
		}]
    });	
		
    var Special_provider_network_editIndex = undefined;
    function Special_provider_network_endEditing()
    {
        if (Special_provider_network_editIndex == undefined){return true}
        if ($('#Special_provider_network_table').datagrid('validateRow', Special_provider_network_editIndex))
	    {
            $('#Special_provider_network_table').datagrid('endEdit', Special_provider_network_editIndex);
            Special_provider_network_editIndex = undefined;
	        return true;
        } 
	    else 
	    {
	        return false;
        }
    }
	$('#Special_provider_network_table').datagrid({
	title:'Network',
    url:'fetchProviderNetwork',
	onClickRow:function(index,row)
	{
        if (Special_provider_network_editIndex != index)
	    {
	        if (Special_provider_network_endEditing())
		    {
	           $('#Special_provider_network_table').datagrid('selectRow', index).datagrid('beginEdit', index);
		       Special_provider_network_editIndex = index;
	        }
		    else
		    {
	           $('#Special_provider_network_table').datagrid('selectRow', Special_provider_network_editIndex);
            }
        }
	},
    columns:[[
            {field:'id',title:'Id',width:120,align:'center'},
            {field:'source',title:'Source',width:120,align:'center'},
	        {field:'destination',title:'Destination',width:120,align:'center'},
			{field:'bandwidth',title:'Bandwidth',width:120,align:'center',editor:{type:'numberbox',options:{precision:1,min:0,required:true}}},
			{field:'latency',title:'Latency',width:120,align:'center',editor:{type:'numberbox',options:{precision:1,min:0,required:true}}}
            ]],
	singleSelect:true,
	toolbar:[{
			text:'Save',
			iconCls:'icon-save',
			handler:function()
			{
                if (Special_provider_network_endEditing())
	            {
                   $('#Special_provider_network_table').datagrid('acceptChanges');
                   var row = $('#Special_provider_network_table').datagrid('getSelected');
                   if(row){
                	   $.ajax({
                		   type:"post",
                		   url:"updateProviderNetwork",
                		   data:{
                			   id:row.id,
                			   source:row.source,
                			   destination:row.destination,
                			   bandwidth:row.bandwidth,
                			   latency:row.latency
                		   },
                		   dataType:"json",
                		   success:function(data){
	                			if(data.statusCode == 1){
	                				alert("save successfully!");
	                			}
	                			else{
	                				alert("fail");
	                			}
	                			$("#Special_provider_network_table").datagrid('reload');
	                		},
	                		error:function(){
	                			alert("系统异常");
	                		}
                	   });
                   }
                }
			}
		},{
			text:'Reject',
			iconCls:'icon-undo',
			handler:function()
			{
                $('#Special_provider_network_table').datagrid('rejectChanges');  
				Special_provider_network_editIndex = undefined;
			}
		}]
    });
	
	
	
});
