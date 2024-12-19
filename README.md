# DePaul-Stock-Exchange
Object Oriented Project

### Overview
This project simulates a stock exchange system, focusing on the publication of current market data. It is built using object-oriented principles and showcases the implementation of key design patterns such as the Singleton and Observer patterns. The system handles the buy and sell sides of the market, calculating the top prices and volumes for each, and provides real-time updates to subscribers.

### Features
- **Market Data Publication:** Calculates and publishes top-of-book prices and volumes for both buy and sell sides.
- **Observer Pattern:** Allows users to subscribe to updates for specific stocks.
- **Singleton Pattern:** Ensures a single instance of critical classes such as the `CurrentMarketTracker` and `CurrentMarketPublisher`.
- **User Subscription:** Users can subscribe and unsubscribe to updates for specific stocks.
- **Market Width Calculation:** Dynamically computes the spread between buy and sell prices.

### Key Classes and Interfaces
- **CurrentMarketSide:** Represents the top price and volume for one market side.
- **CurrentMarketObserver Interface:** Defines the contract for receiving market updates.
- **CurrentMarketTracker:** Receives updates from the product book and forwards them to the publisher.
- **CurrentMarketPublisher:** Maintains subscriptions and publishes updates to observers.
- **User Class:** Implements `CurrentMarketObserver` to receive and store market updates.

### Example Output
```
*********** Current Market ***********
* WMT   $70.70x35 - $70.75x40 [$0.05]
**************************************
```

### Setup and Usage
1. Clone the repository:
   ```bash
   git clone https://github.com/gmadiyar00/stock-exchange-project.git
   cd stock-exchange-project
   ```
2. Compile and run the project using your preferred Java IDE or command line:
   ```bash
   javac *.java
   java Main
   ```

### Future Improvements
- Add support for multiple exchanges.
- Integrate real-time data feeds.
- Enhance the user interface for better visualization of market data.

### Acknowledgments
This project is part of the SE 350 Object-Oriented Software Development course, guided by Professor Christopher Hield. The concepts and requirements adhere to the principles taught in class.
