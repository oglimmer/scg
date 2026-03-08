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
