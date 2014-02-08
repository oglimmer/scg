<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
	<s:layout-component name="center">

		<s:form beanclass="de.oglimmer.scg.web.action.CreateGameActionBean"
			focus="">
			<div>
				<label for="email1">Email 1</label>
				<s:text name="email1" />
			</div>
			<div>
				<label for="email2">Email 2</label>
				<s:text name="email2" />
			</div>
			<div>
				<label for="email3">Email 3</label>
				<s:text name="email3" />
			</div>
			<div>
				<label for="email4">Email 4</label>
				<s:text name="email4" />
			</div>
			<s:submit name="create" value="create" />
		</s:form>

	</s:layout-component>
</s:layout-render>