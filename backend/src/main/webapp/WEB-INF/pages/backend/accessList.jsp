<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/pages/common/head.jsp"%>

<div id="content" class="span10">
    <div>
        <ul class="breadcrumb">
            <li><a href="main.html">DormitoryManagement</a> <span class="divider">/</span></li>
            <li><a href="javascript:void(0);">人流数据概览</a></li>
        </ul>
    </div>

    <div class="row-fluid sortable">
        <div class="box span12">
            <div class="box-header well" data-original-title>
                <h2><i class="icon-th"></i> 人流数据</h2>
            </div>
            <div class="box-content">
                <table class="table table-striped table-bordered bootstrap-datatable datatable">
                    <thead>
                    <tr>
                        <th><input type="button" value="删除" id="myDelAccessBtn" ></th>
                        <th>学号</th>
                        <th>姓名</th>
                        <th>宿舍楼号</th>
                        <th>时间</th>
                        <th>进/出</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${accessList}" var="access">
                        <tr>
                            <td><input type="checkbox" class="checkbox" name="delAccess" value="<fmt:formatDate value="${access.accessTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"></td>
                            <td>${access.studentNum}</td>
                            <td>${access.name}</td>
                            <td>${access.buildingNum}</td>
                            <td><fmt:formatDate value="${access.accessTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td>${access.accessStatus}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<%@include file="/WEB-INF/pages/common/foot.jsp"%>