<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
	<s:layout-component name="center">

		<div class="panel">
			<div class="status-message status-success">
				<h2>Turn Accepted</h2>
				<p>${actionBean.response}</p>
				<p class="text-muted">Next email sent.</p>
				<div class="mt-2">
					<s:link beanclass="de.oglimmer.scg.web.action.SelectActionBean" class="btn btn-secondary">
						<s:param name="gid">${actionBean.gid}</s:param>
						<s:param name="pid">${actionBean.pid}</s:param>
						View Game Overview
					</s:link>
				</div>
			</div>
		</div>

	</s:layout-component>
</s:layout-render>
