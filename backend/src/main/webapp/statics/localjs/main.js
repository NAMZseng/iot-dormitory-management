$("#myDelStaffBtn").click(function(){
	 var trs = $("table").find("tr"); //获取表格每一行
	 var selectIds = "";
	    trs.each(function() {  // 遍历
	        var isChecked = $(this).find(".checkbox").prop("checked");  // 获取当前行checkbox选择状态；
	        if(isChecked == true || isChecked == "true") { // 如果选中
	        	selectIds += $(this).find(".checkbox").val()+" "; // checkbox的value
	        }
	    })
    	if(selectIds != "" && selectIds != null){
    	  	$.ajax({
    			type: "POST",
    			url: "staff/delete",
    			data: {"staffNum":selectIds},
    			dataType: "html",
    	  		timeout:1000,
    			error: function () {
    			   alert("删除失败error");
    				window.location.href = "staff/list";
    	        },
    	        success: function(result){
    	        	if(result == "success"){
    	 			   window.location.href = "staff/list";
    	 			  alert("职工"+selectIds+"删除成功了");
    	        	}else if(result == "failed"){
    	        		window.location.href = "staff/list";
    	        		 alert("删除失败");
    	        	}
    	        },
    		});
    	}else{
	    	alert("请选择职工");
	    }
});

$('.addStaff').click(function(e){
	$("#add_formtip").html('');
	e.preventDefault();
	$('#addStaffDiv').modal('show');
});

function addStaffFunction(){
	$("#add_formtip").html("");
	var result = true;
	if($("#a_staffNum").val() == ""){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>工号不能为空</li>");
		result = false;
	}
	if($("#a_staffName").val() == ""){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>姓名不能为空</li>");
		result = false;
	}
	if($("#a_staffTel").val() == ""){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>电话不能为空</li>");
		result = false;
	}
	if($("#a_staffBuildingNum").val() == ""){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>工作楼号不能为空</li>");
		result = false;
	}
	if($("#a_staffTitle").val() == ""){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>职称不能为空</li>");
		result = false;
	}
	if(result == true) alert("添加成功 ^_^");
	return result;
}

$('.addStaffcancel').click(function(e) {
	$('#a_staffNum').val('');
	$('#a_staffName').val('');
	$('#a_staffTel').val('');
	$('#a_staffBuildingNum').val('');
	$('#a_staffTitle').val('');
});