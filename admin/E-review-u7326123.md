## Code Review

Reviewed by: Xinyun Zhou, u7326123

Reviewing code written by: Allen Lionel yen, u7435050

Component: [drawFromDeck](https://gitlab.cecs.anu.edu.au/u7326123/comp1110-ass2/-/blob/master/src/comp1110/ass2/Arboretum.java#L207-227)

### Comments 

##### What are the best features of this code?
The method drawFromDeck would allow us to find cards randomly in the deck. This is the start of the game. The best feature is he set a static integer to count. Codes are easy to follow and understand.
##### Is the code well-documented?
The code is not very good at well-documented. We need more comments to explain the steps to solve the problem.
##### Is the program decomposition (class and method structure) appropriate?
This program decomposition is appropriate most of the time. As we defined a static int called "min", we need to mention that we can't use "min" as a name of a variable in the future coding.
##### Does it follow Java code conventions (for example, are methods and variables properly named), and is the style consistent throughout?
The code is readable. But there does some points that can improve. The static integer name could be more specific and not use "min". The String result could define as "" to remove the warning.
##### If you suspect an error in the code, suggest a particular situation in which the program will not function correctly.
Because we want to find a card randomly, it is better to use Math.random to solve this problem. The limitation of this solution is that the method would only find and return the cards one by one in the deck. This randomness is not flexible and does not exactly what we want.
