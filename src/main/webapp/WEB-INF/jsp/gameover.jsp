<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
	<s:layout-component name="center">

		<div style="margin-bottom:20px">The has ended. See your inbox for the result.</div>
		
		<div style="margin-bottom:20px">
		
			<s:link beanclass="de.oglimmer.scg.web.action.SelectActionBean">
				<s:param name="gid">${actionBean.gid}</s:param>
				<s:param name="pid">${actionBean.pid}</s:param>
				To overview page.
			</s:link>
		
		</div>

	</s:layout-component>
</s:layout-render>