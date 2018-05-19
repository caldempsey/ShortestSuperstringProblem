Utils
---

Utils are project agnostic classes which are external to the application. Utils provide static methods and operations which are applicable to the entire Java 8 library. 

A decision to move a responsibly designed function (function designed to fulfill one purpose or goal) should take the following into consideration... 

 * A decision to move functions to helper classes is based on re-usable functional operations which apply to primitives (Bloch Item 47). 
 * Where functions operate on and only return primitives, functions to perform one task with those primitives need not apply OOP design practices, such as SOLID.
 * A String type in the Java library may feature as a member of the Static helper methods as it is closed for modification (the class cannot be extended or changed). Nulls must be carefully considered if a decision is made to implement a String static helper, but use caution and apply judiciously. 
 * Utils must feature function oriented design (given the same input always return the same output). This is achieved by writing each Util as adhering to a contract. Do not implement a Util which does not specify a contract. 
 * Evaluating from the above, any function oriented method which operates only on primitives in your code base could be a good candidate for featuring in Utils.
 


