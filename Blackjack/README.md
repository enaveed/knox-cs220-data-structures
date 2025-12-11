
# Blackjack – API Client & GUI (CS 220 Project)

This project is a client-side Blackjack game built for **CS 220** at **Knox College**.  
The game communicates with a Blackjack server hosted internally on the Knox campus and lets players:

- Start a new game session  
- Place bets  
- Hit / Stand  
- View round outcomes  
- Play multiple hands in a session  
- Interact with a GUI built in Java  

---

## 🎮 Features

### ✔ Remote API Communication  
The client sends HTTP requests to the Blackjack server located at:
http://euclid.knox.edu:8080/api/blackjack


(Only accessible on the campus network.)

### ✔ GUI Using Java  
- Buttons for *Hit*, *Stand*, *Bet*, *New Game*  
- Cards displayed with images  
- Score and balance displayed clearly  
- Buttons automatically disable when the action is invalid  
  (for example, betting in the middle of a hand)

### ✔ Error Handling  
A major part of the project:

- Illegal bet amounts → show error  
- Betting at the wrong time → greyed-out buttons  
- Preventing invalid states  
- Resetting UI cleanly after each game  


### ✔ Code Structure (example classes)
- `BlackjackClient.java`  
- `ClientConnector.java` 
- `GameState.java` 
- `Card.java` – Represents a card  
- `CardPanel.java` – GUI drawing for cards  
- `SessionSummary.java` – Tracks gameplay summary  
- `JsonUtil.java` – Helps parse JSON responses  

---

## 🔐 Credentials Note
The username/password for the API were provided privately by the instructor.  
**They are NOT included in this repository.**  
All credential variables have been replaced with placeholders for security.

---

## 🧪 Testing
This project was tested on the Knox campus network:

- Verified correct connection to the Blackjack server  
- Tested all game actions (hit, stand, bet, new game)  
- Verified the logic around illegal actions  
- Ensured buttons are disabled at correct times  
- Played multiple rounds to check game state consistency  

---

## 📁 Files Included

- Java source files for both the API client and GUI  
- Image assets for cards  

This project demonstrates API integration, state management, GUI programming, and error handling — all essential skills for real-world software.

