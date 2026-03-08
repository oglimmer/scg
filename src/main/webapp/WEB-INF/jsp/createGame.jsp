<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
	<s:layout-component name="center">

		<div class="page-title-area">
			<h1>New Game</h1>
			<div class="subtitle">Invite four players to the table</div>
		</div>

		<div class="panel">
			<s:form beanclass="de.oglimmer.scg.web.action.CreateGameActionBean" focus="">
				<div class="form-group">
					<label for="email1">Player 1</label>
					<s:text name="email1" />
				</div>
				<div class="form-group">
					<label for="email2">Player 2</label>
					<s:text name="email2" />
				</div>
				<div class="form-group">
					<label for="email3">Player 3</label>
					<s:text name="email3" />
				</div>
				<div class="form-group">
					<label for="email4">Player 4</label>
					<s:text name="email4" />
				</div>
				<div class="mt-2">
					<s:submit name="create" value="Deal the Cards" />
				</div>
			</s:form>
		</div>

	</s:layout-component>
</s:layout-render>
