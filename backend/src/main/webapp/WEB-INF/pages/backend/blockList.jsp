<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/pages/common/head.jsp"%>

<div id="content" class="span10">
    <div>
        <ul class="breadcrumb">
            <li><a href="main.html">DormitoryManagement</a> <span class="divider">/</span></li>
            <li><a href="javascript:void(0);">被阻访问数据概览</a></li>
        </ul>
    </div>

    <div class="row-fluid sortable">
        <div class="box span12">
            <div class="box-header well" data-original-title>
                <h2><i class="icon-th"></i> 被阻访问数据</h2>
            </div>
            <div class="box-content">
                <table class="table table-striped table-bordered bootstrap-datatable datatable">
                    <thead>
                    <tr>
                        <th><input type="button" value="删除" id="myDelBlockBtn" ></th>
                        <th>学号</th>
                        <th>姓名</th>
                        <th>班主任电话</th>
                        <th>宿舍楼号</th>
                        <th>时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${blockList}" var="block">
                        <tr>
                            <td><input type="checkbox" class="checkbox" name="delBlock" value="<fmt:formatDate value="${block.accessTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"></td>
                            <td>${block.num}</td>
                            <td>${block.studentName}</td>
                            <td>${block.teacherTel}</td>
                            <td>${block.buildingNum}</td>
                            <td><fmt:formatDate value="${block.accessTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<%@include file="/WEB-INF/pages/common/foot.jsp"%>