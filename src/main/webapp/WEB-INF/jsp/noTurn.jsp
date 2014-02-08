<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
	<s:layout-component name="center">

		<div>It's not your turn.</div>

		<div>Reload in 15 secs.</div>

		<script>
			function reloadPage() {
				location.reload(true);
			}
			window.setTimeout(reloadPage, 15 * 1000);
		</script>

	</s:layout-component>
</s:layout-render>