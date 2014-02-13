<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
	<s:layout-component name="center">

		<h1>Rules</h1>
		
			<ul>
				<li>You have 1 card in your hand</li>
				<li>At your turn you draw one new card</li>
				<li>You must always play one out of two cards</li>
				<li>When you play a card, its text takes effect</li>
				<li>In the beginning the undisclosed card stack has 10 cards</li>
				<li>The game ends when all 10 cards are drawn or only one player survived</li>
				<li>If more than 1 player survived the player with the highest card in his/her hand wins</li>
			</ul>
			
		<h1>How to play</h1>
		
		<p>
		There are 3 ways to play the game:
		</p>
		
		<ul>
			<li>Play within the Email-Client: if your email client supports html-forms you can just press one of the two "play" buttons below the cards. Unfortunately some email clients think html-forms are evil and don't support them.</li>
			<li>Play in the browser: click the link "Play via Browser" at the bottom of the email.</li>
			<li>Play by reply: press reply and use the follow answer (keep the original email in your reply): x-y-z. x = card 1 or 2, y = (optional) target player number, z = (optional) target card no</li>
		</ul>

		<h1>Cards</h1>
		
		<%@include file="/WEB-INF/jsp/common/card_overview.jsp"%>
		
		<div>
			&nbsp;
		</div>

	</s:layout-component>
</s:layout-render>