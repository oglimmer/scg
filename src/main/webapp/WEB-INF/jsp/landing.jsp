<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
	<s:layout-component name="center">

		<div class="page-title-area">
			<h1>Simple Card Game</h1>
			<div class="subtitle">A game of deduction, deception &amp; daring for 4 players</div>
		</div>

		<div class="panel">
			<div class="panel-header">
				<span class="panel-icon">&#9830;</span>
				<h3>Rules</h3>
			</div>
			<ul class="rules-list">
				<li>You have 1 card in your hand</li>
				<li>At your turn you draw one new card</li>
				<li>You must always play one out of two cards</li>
				<li>When you play a card, its text takes effect</li>
				<li>In the beginning the undisclosed card stack has 10 cards</li>
				<li>The game ends when all 10 cards are drawn or only one player survived</li>
				<li>If more than 1 player survived the player with the highest card in his/her hand wins</li>
			</ul>
		</div>

		<div class="panel">
			<div class="panel-header">
				<span class="panel-icon">&#9827;</span>
				<h3>How to Play</h3>
			</div>
			<p>There are 3 ways to play the game:</p>
			<ul class="rules-list">
				<li>Play within the Email-Client: if your email client supports html-forms you can just press one of the two "play" buttons below the cards.</li>
				<li>Play in the browser: click the link "Play via Browser" at the bottom of the email.</li>
				<li>Play by reply: press reply and use the follow answer (keep the original email in your reply): x-y-z. x = card 1 or 2, y = (optional) target player number, z = (optional) target card no</li>
			</ul>
		</div>

		<div class="panel">
			<div class="panel-header">
				<span class="panel-icon">&#9824;</span>
				<h3>Cards</h3>
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
