<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
	<s:layout-component name="center">
	
	<div style="margin-bottom:20px">
		Turn accepted.
	</div>
	
	<div style="margin-bottom:20px">
		${actionBean.response }
	</div>

	<div style="margin-bottom:20px">
		Next email sent.
	</div>
	
	<div style="margin-bottom:20px">
		<a href="Select.action?gid=${actionBean.gid}&pid=${actionBean.pid}">To overview page.</a>
	</div>
	
	
	</s:layout-component>
</s:layout-render>