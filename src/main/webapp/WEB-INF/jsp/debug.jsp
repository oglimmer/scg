<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>
<%
	pageContext.setAttribute("newLineChar", "\n");
%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
	<s:layout-component name="center">

		<c:forEach var="game" items="${actionBean.gameManager.allGames }">
			${fn:replace(game.debugPrint(), newLineChar, '<br/>')}
			<s:link beanclass="de.oglimmer.scg.web.action.DebugActionBean"
				event="delete">
				<s:param name="gameId">${game.id }</s:param>
				Del
			</s:link>
			<s:link beanclass="de.oglimmer.scg.web.action.DebugActionBean"
				event="resendEmail">
				<s:param name="gameId">${game.id }</s:param>
				Resend
			</s:link>
			<hr />
		</c:forEach>

	</s:layout-component>
</s:layout-render>