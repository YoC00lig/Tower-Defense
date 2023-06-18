<h1> ⭐️ Tower defense ⭐️ </h1>
<p> A simple game whose goal is to defend the castle from the incoming waves of enemies. The player can defend the castle by placing towers that deal damage to enemies and building walls that temporarily distract the
enemy from the target (castle). Unfortunately, enemies can destroy any object.</p>

<p> There are two types of towers in the game. They differ primarily in attack range and durability and therefore  in price. During the game, you can increase the tower's attack
range or sell it. Its price will of course depend on its condition - a tower that has been attacked by an enemy will no longer have its maximum value.</p>

<p> The player can also build a wall. The wall does not attack the enemy, but temporarily stops them. The enemy stands against the wall that stands in his way and destroys it until he
can go through it. This gives you extra time to think about your game strategy.</p>

<p> You can see different types of enemies. They differ in the maximum health and strength. If the tower kills an enemy, you will receive money - the amount depends on the type 
of enemy. Enemies are a bit lazy - they always look for the nearest object and choose the shortest path to it.</p>

<p> The castle is the most important object in the game. It stands in the center of the map and is the target of enemy attacks. The castle has a health
bar, and when it is empty, the game is over. You can recharge its health bar to the maximum at any time - but it's not cheap.</p>

<h2> 🗺 You can choose different variants of the map: </h2>
<p> ⚙️ Basic - you can only place towers and walls. </p>
<p> 🌊 Flood - in addition, there are inaccessible places flooded by water on the map. </p>
<p> ✨ Extended - you can increase castle health bar, sell and upgrade towers. </p>

<h1> 🛠 Core technology stack: </h1>
<ul>
<li> Java 17 </li>
<li> Gradle </li>
<li> JavaFX </li>
<li> The icons have been sourced from <a href="https://www.flaticon.com">here</a>. </li>
</ul>

<h1> 🎮 Let's begin! </h1>
<p> After starting the game, the following window will be displayed: </p>
<img src="/readme/start.gif">
<p> After pressing START GAME button you can choose a map variant (don't forget to enter your nickname - without it the game won't start). </p>
<img src="/readme/input.gif">

<h1>❓ How to place objects? </h1>
<h3> Tower </h3>
<p> It's easy. You have to click anywhere on the map (it will be the upper left corner of the tower). Then the shop window will be displayed. Click the BUY button under the selected tower. If you do not have enough money, you will see the appropriate message.</p>
<h3> Wall </h3>
<p> It's a little more complicated. You also need to click anywhere on the map - this will be the beginning of the wall. In the shop window, select the wall and click BUY. When the shop window disappears, click anywhere on the map again - this time it will be the end of the wall. When you don't have enough money, only part of the wall will be built.</p>

<h3> First objects </h3>

<p> At the beginning, you have a certain amount of money to spend. Place your first objects, then press the PLAY button to start the game. Remember that objects cannot overlap and cannot extend beyond map bounds. </p>

<img src="/readme/shop.gif">

<h1> ⬆️ Upgrade objects </h1>
<p> You can upgrade your objects during the game. To do this, click on the object you want to improve. </p>
<img src="/readme/upgrade.gif">

<h1> 🏁 End of the game </h1>
<p> The game ends when the castle is destroyed or the player defeats all waves of enemies. At the end of the game, a window with information and statistics is displayed. You can see your score and top three scores in the game, quit game or play again.</p>
<img src="/readme/end.gif">
