<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
	<s:layout-component name="center">

	<div>
		Turn accepted.
	</div>
	
	<div>
		${actionBean.response }
	</div>

	<div>
		Next email sent.
	</div>

	</s:layout-component>
</s:layout-render>