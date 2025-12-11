
# CS 220 Final Project – Custom Blackjack (Client-Side Logic in JavaFX)

For my CS 220 final project, I built a **standalone Blackjack game in JavaFX**.

In the original CS 220 assignment, the Blackjack client talked to a server that handled all of the game logic.  
For this project, I instead **re-implemented the game rules on the client side**, so my program:

- Deals cards
- Tracks hands and scores
- Applies Blackjack rules
- Manages bets and balances

All of this logic is now handled locally in my code instead of by the remote server.

---

## 🎮 Features

- Full Blackjack game implemented in Java
- Game state stored and updated on the client side
- JavaFX GUI with buttons for betting, hitting, standing, and starting new rounds
- Buttons are **disabled when an action is not valid** (for example, no hitting after the hand is finished)
- Visual feedback for wins/losses and current balance
- Uses separate classes for game logic vs. UI

My professor commented that it was:

> “cool that you did blackjack but basically had to implement all of the logic that the server handled,”  

and also specifically liked that:

> “the buttons seem to get disabled when they are not usable.”
>
> ---

## 🐛 Known Issue

- When new cards are drawn for the dealer, they sometimes appear slightly out of place in the GUI layout.  
  This is a layout/positioning bug in the JavaFX view code, not in the game logic itself.

I chose to leave this visible as a reminder of what I would refine next (and as a realistic example that UI alignment bugs can be tricky in new frameworks).

