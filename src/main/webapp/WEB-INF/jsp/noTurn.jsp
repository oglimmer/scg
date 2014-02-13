<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
	<s:layout-component name="center">

		<h3>
			Players:
		</h3>
		<c:forEach var="player" items="${actionBean.players }">
			<li>${player.displayName } ${player.status } ${player.turn }</li>
		</c:forEach>
	
		<h3>
			Your cards:
		</h3>
		<table>
			<tr>
				<c:forEach var="card" items="${actionBean.currentPlayersCards }">
					<td><img src="images/card${card.no }.png"/></td>
				</c:forEach>
			</tr>
		</table>
	
		<h3>
			Stacks:
		</h3> 
		Undisclosed cards: ${actionBean.undisclosedCards }<br/>
		Used cards: 
		<c:forEach var="usedCard" items="${actionBean.usedCards }" varStatus="loopStatus">
			${usedCard.key }(${usedCard.value }x)
			<c:if test="${!loopStatus.last}">, </c:if>
		</c:forEach>
	
		<h3>
			Last actions:
		</h3>	
		<ol>
			${actionBean.messages.allHtml }
		</ol> 
		
		
		<h3>Cards Help</h3>
			
		<%@include file="/WEB-INF/jsp/common/card_overview.jsp"%>
		

	</s:layout-component>
</s:layout-render>