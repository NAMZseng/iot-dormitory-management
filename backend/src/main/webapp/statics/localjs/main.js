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

$("#myDelStudentBtn").click(function(){
	var trs = $("table").find("tr");
	var selectIds = "";
	trs.each(function() {
		var isChecked = $(this).find(".checkbox").prop("checked");
		if(isChecked == true || isChecked == "true") {
			selectIds += $(this).find(".checkbox").val()+" ";
		}
	})
	if(selectIds != "" && selectIds != null){
		$.ajax({
			type: "POST",
			url: "student/delete",
			data: {"studentNum":selectIds},
			dataType: "html",
			timeout:1000,
			error: function () {
				alert("删除失败error");
				window.location.href = "student/list";
			},
			success: function(result){
				if(result == "success"){
					window.location.href = "student/list";
					alert("学生"+selectIds+"删除成功了");
				}else if(result == "failed"){
					window.location.href = "student/list";
					alert("删除失败");
				}
			},
		});
	}else{
		alert("请选择学生");
	}
});

$('.addStudent').click(function(e){
	$("#add_formtip").html('');
	e.preventDefault();
	$('#addStudentDiv').modal('show');
});

function addStudentfFunction(){
	$("#add_formtip").html("");
	var result = true;
	if($("#a_studentNum").val() == ""){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>学号不能为空</li>");
		result = false;
	}
	if($("#a_studentName").val() == ""){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>姓名不能为空</li>");
		result = false;
	}
	if($("#a_studentSchool").val() == ""){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>学院不能为空</li>");
		result = false;
	}
	if($("#a_studentMajor").val() == ""){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>专业不能为空</li>");
		result = false;
	}
	if($("#a_studenTeacherName").val() == ""){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>班主任姓名不能为空</li>");
		result = false;
	}
	if($("#a_studenTeacherTel").val() == ""){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>班主任电话不能为空</li>");
		result = false;
	}
	if($("#a_studenBuildingNum").val() == ""){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>宿舍楼号不能为空</li>");
		result = false;
	}
	if($("#a_studenRoomNum").val() == ""){
		$("#add_formtip").css("color","red");
		$("#add_formtip").append("<li>房间号不能为空</li>");
		result = false;
	}
	if(result == true) alert("添加成功 ^_^");
	return result;
}

$('.addStudentcancel').click(function(e) {
	$('#a_studentNum').val('');
	$('#a_studentName').val('');
	$('#a_studentSchool').val('');
	$('#a_studentMajor').val('');
	$('#a_studenTeacherName').val('');
	$('#a_studenTeacherTel').val('');
	$('#a_studenBuildingNum').val('');
	$('#a_studenRoomNum').val('');
});

$("#myDelHumitureBtn").click(function(){
	var trs = $("table").find("tr"); //获取表格每一行
	var selectIds = "";
	trs.each(function() {  // 遍历
		var isChecked = $(this).find(".checkbox").prop("checked");  // 获取当前行checkbox选择状态；
		if(isChecked == true || isChecked == "true") { // 如果选中
			selectIds += $(this).find(".checkbox").val()+"/"; // checkbox的value
		}
	})
	if(selectIds != "" && selectIds != null){
		$.ajax({
			type: "POST",
			url: "humiture/delete",
			data: {"collectTime":selectIds},
			dataType: "html",
			timeout:1000,
			error: function () {
				alert("删除失败error");
				window.location.href = "humiture/list";
			},
			success: function(result){
				if(result == "success"){
					window.location.href = "humiture/list";
					alert("记录"+selectIds+"删除成功了");
				}else if(result == "failed"){
					window.location.href = "humiture/list";
					alert("删除失败");
				}
			},
		});
	}else{
		alert("请选择记录");
	}
});

$("#myDelAccessBtn").click(function(){
	var trs = $("table").find("tr");
	var selectIds = "";
	trs.each(function() {
		var isChecked = $(this).find(".checkbox").prop("checked");
		if(isChecked == true || isChecked == "true") {
			selectIds += $(this).find(".checkbox").val()+"/";
		}
	})
	if(selectIds != "" && selectIds != null){
		$.ajax({
			type: "POST",
			url: "access/deleteAccess",
			data: {"accessTime":selectIds},
			dataType: "html",
			timeout:1000,
			error: function () {
				alert("删除失败error");
				window.location.href = "access/listAccess";
			},
			success: function(result){
				if(result == "success"){
					window.location.href = "access/listAccess";
					alert("记录"+selectIds+"删除成功了");
				}else if(result == "failed"){
					window.location.href = "access/listAccess";
					alert("删除失败");
				}
			},
		});
	}else{
		alert("请选择记录");
	}
});

$("#myDelBlockBtn").click(function(){
	var trs = $("table").find("tr");
	var selectIds = "";
	trs.each(function() {
		var isChecked = $(this).find(".checkbox").prop("checked");
		if(isChecked == true || isChecked == "true") {
			selectIds += $(this).find(".checkbox").val()+"/";
		}
	})
	if(selectIds != "" && selectIds != null){
		$.ajax({
			type: "POST",
			url: "access/deleteBlock",
			data: {"accessTime":selectIds},
			dataType: "html",
			timeout:1000,
			error: function () {
				alert("删除失败error");
				window.location.href = "access/listBlock";
			},
			success: function(result){
				if(result == "success"){
					window.location.href = "access/listBlock";
					alert("记录"+selectIds+"删除成功了");
				}else if(result == "failed"){
					window.location.href = "access/listBlock";
					alert("删除失败");
				}
			},
		});
	}else{
		alert("请选择记录");
	}
});