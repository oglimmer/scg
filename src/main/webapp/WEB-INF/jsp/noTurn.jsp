<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
	<s:layout-component name="center">

		<div class="page-title-area">
			<h1>Game Overview</h1>
			<div class="subtitle">Waiting for your turn</div>
		</div>

		<c:if test="${actionBean.game.turn.gameEnded}">
			<div class="panel">
				<div class="status-message status-gameover">
					<h2>Game Over</h2>
					<p>${actionBean.gameWinner}</p>
				</div>
			</div>
		</c:if>

		<div class="panel">
			<div class="panel-header">
				<span class="panel-icon">&#9829;</span>
				<h3>Players</h3>
			</div>
			<ul class="player-list">
				<c:forEach var="player" items="${actionBean.players}">
					<li>${player.displayName} ${player.status} ${player.turn}</li>
				</c:forEach>
			</ul>
		</div>

		<div class="panel">
			<div class="panel-header">
				<span class="panel-icon">&#9824;</span>
				<h3>Your Hand</h3>
			</div>
			<div class="player-hand">
				<c:forEach var="card" items="${actionBean.callingPlayersCards}">
					<img src="images/card${card.no}.png" alt="Card ${card.no}"/>
				</c:forEach>
			</div>
		</div>

		<div class="panel">
			<div class="panel-header">
				<span class="panel-icon">&#9830;</span>
				<h3>Stacks</h3>
			</div>
			<div class="stacks-info">
				<div class="stack-item">
					<div class="stack-label">Undisclosed Cards</div>
					<div class="stack-value">${actionBean.undisclosedCards}</div>
				</div>
				<div class="stack-item">
					<div class="stack-label">Used Cards</div>
					<div class="stack-value">
						<c:forEach var="usedCard" items="${actionBean.usedCards}" varStatus="loopStatus">
							${usedCard.key}(${usedCard.value}x)<c:if test="${!loopStatus.last}">, </c:if>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>

		<div class="panel">
			<div class="panel-header">
				<span class="panel-icon">&#9827;</span>
				<h3>Last Actions</h3>
			</div>
			<ol class="action-log">
				${actionBean.callingPlayersMessages.allHtml}
			</ol>
		</div>

		<div class="panel">
			<div class="panel-header">
				<span class="panel-icon">&#9824;</span>
				<h3>Cards Reference</h3>
			</div>
			<div class="game-cards-grid">
				<c:forEach var="row" begin="0" end="3">
					<c:forEach var="col" begin="1" end="2">
						<div class="game-card-item">
							<img src="images/card${row*2+col}.png" alt="${actionBean.getCard(row*2+col).name}"/>
							<div class="game-card-info">
								<div class="card-name">${actionBean.getCard(row*2+col).name}</div>
								<div class="card-count">${actionBean.getCardCount(row*2+col)}x in deck</div>
							</div>
						</div>
					</c:forEach>
				</c:forEach>
			</div>
		</div>

	</s:layout-component>
</s:layout-render>
