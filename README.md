# CS 220 – Data Structures & Software Engineering (Knox College)

This repository contains my projects for **CS 220**, focusing on data structures, software engineering practices, GUI development, and client–server communication.  
The course emphasized real-world problem solving, debugging, and writing clean, modular Java code.

---

# 1. DrawShape Project  
📁 Folder: `DrawShape/`

A graphics-based assignment applying object-oriented design in Java.

### Key Skills
- Interfaces, abstract classes, and inheritance  
- Polymorphism and shared behavior among shapes  
- Scene modeling (circles, rectangles, bounding boxes, selection rectangles)  
- Separation of logic (models) and rendering (panels)  
- Interactive movement and selection of shapes

### Concepts Used
- `IShape`, `AbstractShape`, `Circle`, `Rectangle`, `SelectionRectangle`, etc.  
- Handling shape positions and dimensions  
- Rendering logic separate from shape definitions

---

# 2. Blackjack – API Client & GUI  
📁 Folder: `Blackjack/`

A full GUI-based Blackjack client written in Java that communicates with the CS 220 Blackjack API (accessible only on the Knox campus network).

### Key Skills
- Making HTTP requests to a REST API  
- Handling and parsing API responses  
- Java GUI design and event-driven programming  
- Managing full game state on the client side  
- Strong error-handling (e.g., disabling buttons when actions are invalid)  
- Implementing betting, gameplay flow, and session management

### Notes
- API credentials were removed for security.  
- The server is not accessible outside the Knox network; this repository contains only the client-side implementation.

---

# 3. Final Project – Custom JavaFX Blackjack  
📁 Folder: `FinalProject/`

For the final project, I built a **standalone Blackjack game in JavaFX**, implementing **all game logic locally** without depending on the server.

### Key Skills
- Learning JavaFX independently  
- Model–View separation for cleaner architecture  
- Rendering cards, handling user input, updating UI in real time  
- State-dependent UI behavior (disabling/enabling buttons)  
- Managing full Blackjack logic: dealing, hitting, standing, scoring, dealer behavior

### Instructor Feedback (rewritten professionally)
- Successfully implemented game logic typically handled by the server  
- Strong UX decisions such as disabling invalid actions  
- Minor UI alignment issue with dealer card placement (logic unaffected)

---

# Tools & Concepts Used in This Course
- Java  
- OOP: inheritance, interfaces, abstract classes  
- Event-driven programming (Swing & JavaFX)  
- REST API communication  
- GUI design and layout management  
- State management  
- Incremental development and debugging  
- Clean code structure and modular design  

---

# Repository Structure

CS220/
│
├── DrawShape/
├── Blackjack/
└── FinalProject/


---

# What This Repository Demonstrates
- Ability to design and implement larger projects  
- Comfort with APIs, networking, and GUIs  
- Strong understanding of object-oriented programming  
- Ability to learn new frameworks (JavaFX) independently  
- Emphasis on UX, error handling, and robust logic  

