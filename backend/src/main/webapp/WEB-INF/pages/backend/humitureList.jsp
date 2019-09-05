<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/pages/common/head.jsp"%>

<div id="content" class="span10">
    <div>
        <ul class="breadcrumb">
            <li><a href="main.html">DormitoryManagement</a> <span class="divider">/</span></li>
            <li><a href="javascript:void(0);">温湿度数据概览</a></li>
        </ul>
    </div>

    <div class="row-fluid sortable">
        <div class="box span12">
            <div class="box-header well" data-original-title>
                <h2><i class="icon-th"></i> 温湿度数据</h2>
            </div>
            <div class="box-content">
                <table class="table table-striped table-bordered bootstrap-datatable datatable">
                    <thead>
                    <tr>
                        <th><input type="button" value="删除" id="myDelHumitureBtn" ></th>
                        <th>物理地址</th>
                        <th>宿舍楼号</th>
                        <th>位置</th>
                        <th>时间</th>
                        <th>温度/摄氏度</th>
                        <th>相对湿度</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${humitureList}" var="humiture">
                        <tr>
                            <td><input type="checkbox" class="checkbox" name="delHumiture" value="<fmt:formatDate value="${humiture.collectTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"></td>
                            <td>${humiture.macAddress}</td>
                            <td>${humiture.buildingNum}</td>
                            <td>${humiture.location}</td>
                            <td><fmt:formatDate value="${humiture.collectTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td>${humiture.temperature}</td>
                            <td>${humiture.humidity}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<%@include file="/WEB-INF/pages/common/foot.jsp"%>