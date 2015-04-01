$(function()
{
	$('#Customer_tree').tree({

        url:'customerList',
		animate:true,
		dnd:true,
		formatter:function(node){
			
			var s = node.text;
			if (node.children){
			    s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
			}
			return s;
		},
		onClick: function(node)
		{
		    var Customer_judge='Service_list';
            if(node.text == Customer_judge)
			{
			    window.location.href='index.jsp';
			}
			else
			{
				$.ajax({
					type:"post",
					url:"currentCustomer",
					data:{
						id:node.id
					},
					dataType:"json",
					success:function(data){
						for(var statusCode in data){
							if(data[statusCode] == 1){								
								window.location.href='Special_customer.jsp';
							}
							else{
								alert("系统异常");
							}
						}
					},
					error:function(){
						alert("未提交成功。");
					}
				});
			}
        }
    });
	
	
    $('#Provider_tree').tree({
        url:'providerList',
		animate:true,
		dnd:true,
		formatter:function(node){
			
			var s = node.text;
			if (node.children){
				s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
			}
			return s;
		},
		onClick: function(node)
		{
			var provider_judge='Datacenter_list';
            if(node.text == provider_judge)
			{
				window.location.href='Provider.jsp';
			}
			else
			{
				$.ajax({
					type:"post",
					url:"currentProvider",
					data:{
						id:node.id
					},
					dataType:"json",
					success:function(data){
						for(var statusCode in data){
							if(data[statusCode] == 1){
							
								window.location.href='Special_provider.jsp';
							}
							else{
								alert("系统异常");
							}
						}
					},
					error:function(){
						alert("未提交成功。");
					}
				});
			}
        }
    });

});
