	<table>
		<c:forEach var="row" begin="0" end="3">
			<tr>
				<c:forEach var="col" begin="1" end="2">
					<td>
						${actionBean.getCard(row*2+col).name } - ${actionBean.getCardCount(row*2+col) }x 
						<img src="images/card${row*2+col }.png"/>
					</td>
				</c:forEach>
			</tr>
		</c:forEach>				
	</table>