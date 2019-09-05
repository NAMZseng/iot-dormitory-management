<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/pages/common/head.jsp"%>

<div id="content" class="span10">
	<div>
		<ul class="breadcrumb">
			<li><a href="main.html">DormitoryManagement</a> <span class="divider">/</span></li>
			<li><a href="javascript:void(0);">职工信息概览</a></li>
		</ul>
	</div>

	<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2><i class="icon-th"></i> 职工信息</h2>
				  <div class="box-icon">
					  <span class="btn btn-small btn-primary addStaff" >
						  <i class="icon-plus icon-white"></i> 添加 </span>
				  </div>
			  	
			 </div>
			<div class="box-content">
				<table class="table table-striped table-bordered bootstrap-datatable datatable">
					<thead>
						<tr>
							<th><input type="button" value="删除" id="myDelStaffBtn" ></th>
							<th>工号</th>
							<th>姓名</th>
							<th>电话</th>
							<th>工作楼号</th>
							<th>职称</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${staffList}" var="staff">
							<tr>
								<td><input type="checkbox" class="checkbox" name="delStaff" value="${staff.num}"></td>
								<td>${staff.num}</td>
								<td>${staff.name}</td>
								<td>${staff.tel}</td>
								<td>${staff.buildingNum}</td>
								<td>${staff.title}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>

<!-- 添加信息的表单 -->
<div class="modal hide fade" id="addStaffDiv" >
 <form action="staff/add"  method="post" onsubmit="return addStaffFunction();" >
	<div class="modal-header">
		<button type="button" class="close  addStaffcancel" data-dismiss="modal">×</button>
		<h3>添加职工</h3>
	</div>
	<div class="modal-body">
			<ul id="add_formtip"></ul>
               <ul class="topul">
                  <li><label>工号：</label><input type="text" id="a_staffNum" name="num" value="" />
                    				<span style="color:red;font-weight: bold;">*</span></li>
                  <li><label>姓名：</label><input type="text" id="a_staffName" name="name" value="">
                  					<span style="color:red;font-weight: bold;">*</span></li>
                  <li><label>电话：</label><input type="text" id="a_staffTel" name="tel" value="">
                  					<span style="color:red;font-weight: bold;">*</span></li>
                  <li><label>工作楼号：</label><input type="text" id="a_staffBuildingNum" name="buildingNum" value="">
                  					<span style="color:red;font-weight: bold;">*</span></li>
                  <li><label>职称：</label><input type="text" id="a_staffTitle" name="title" value="">
                  					<span style="color:red;font-weight: bold;">*</span></li>
               </ul>
	</div>
	<div class="modal-footer">
		<a href="#" class="btn addStaffcancel" data-dismiss="modal">关闭</a>
		<input type="submit"  class="btn btn-primary" value="保存" />
	</div>
	</form>
</div>
<%@include file="/WEB-INF/pages/common/foot.jsp"%>
