<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/pages/common/head.jsp"%>

<div id="content" class="span10">
    <div>
        <ul class="breadcrumb">
            <li><a href="main.html">DormitoryManagement</a> <span class="divider">/</span></li>
            <li><a href="javascript:void(0);">学生信息概览</a></li>
        </ul>
    </div>

    <div class="row-fluid sortable">
        <div class="box span12">
            <div class="box-header well" data-original-title>
                <h2><i class="icon-th"></i> 学生信息</h2>
                <div class="box-icon">
					  <span class="btn btn-small btn-primary addStudent" >
						  <i class="icon-plus icon-white"></i> 添加 </span>
                </div>
            </div>
            <div class="box-content">
                <table class="table table-striped table-bordered bootstrap-datatable datatable">
                    <thead>
                    <tr>
                        <th><input type="button" value="删除" id="myDelStudentBtn" ></th>
                        <th>学号</th>
                        <th>姓名</th>
                        <th>学院</th>
                        <th>专业</th>
                        <th>班主任姓名</th>
                        <th>班主任电话</th>
                        <th>宿舍楼号</th>
                        <th>房间号</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${studentList}" var="student">
                        <tr>
                            <td><input type="checkbox" class="checkbox" name="delStudent" value="${student.num}"></td>
                            <td>${student.num}</td>
                            <td>${student.name}</td>
                            <td>${student.school}</td>
                            <td>${student.major}</td>
                            <td>${student.teacherName}</td>
                            <td>${student.teacherTel}</td>
                            <td>${student.buildingNum}</td>
                            <td>${student.roomNum}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- 添加信息的表单 -->
<div class="modal hide fade" id="addStudentDiv" >
    <form action="student/add"  method="post" onsubmit="return addStudentfFunction();" >
        <div class="modal-header">
            <button type="button" class="close  addStudentcancel" data-dismiss="modal">×</button>
            <h3>添加学生</h3>
        </div>
        <div class="modal-body">
            <ul id="add_formtip"></ul>
            <ul class="topul">
                <li><label>学号：</label><input type="text" id="a_studentNum" name="num" value="" />
                    <span style="color:red;font-weight: bold;">*</span></li>
                <li><label>姓名：</label><input type="text" id="a_studentName" name="name" value="">
                    <span style="color:red;font-weight: bold;">*</span></li>
                <li><label>学院：</label><input type="text" id="a_studentSchool" name="school" value="">
                    <span style="color:red;font-weight: bold;">*</span></li>
                <li><label>专业：</label><input type="text" id="a_studentMajor" name="major" value="">
                    <span style="color:red;font-weight: bold;">*</span></li>
                <li><label>班主任姓名：</label><input type="text" id="a_studenTeacherName" name="teacherName" value="">
                    <span style="color:red;font-weight: bold;">*</span></li>
                <li><label>班主任电话：</label><input type="text" id="a_studenTeacherTel" name="teacherTel" value="">
                    <span style="color:red;font-weight: bold;">*</span></li>
                <li><label>宿舍楼号：</label><input type="text" id="a_studenBuildingNum" name="buildingNum" value="">
                    <span style="color:red;font-weight: bold;">*</span></li>
                <li><label>房间号：</label><input type="text" id="a_studenRoomNum" name="roomNum" value="">
                    <span style="color:red;font-weight: bold;">*</span></li>
            </ul>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn addStudentcancel" data-dismiss="modal">关闭</a>
            <input type="submit"  class="btn btn-primary" value="保存" />
        </div>
    </form>
</div>

<%@include file="/WEB-INF/pages/common/foot.jsp"%>
