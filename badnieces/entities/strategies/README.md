Strategies
---

Defines a family of algorithms, encapsulate each one, and makes them interchangeable. 
A strategy lets the algorithm vary independently from clients that use it. 

* Strategies are implemented to provide a flexible algorithmic solution which are implementable to objects.
* Objects can thus be composed by many strategies which might be changed and re-used to provide flexible or chosen functionality.
* By keeping our strategies responsibly designed to provide a single function over Java library data-types we can achieve functionality which is flexible and re-usable in many different objects and classes.
* Strategies should be open to extension, but closed for modification. You should be able to extend strategies and override the search method to provide tweaks to existing functionality, but any changes made must respect the isA property of inheritence.
* The main advantage of strategies is it gives objects the power to decide how they want to perform operations at runtime (rather than at compile time). We can thus pick and choose what algorithms-per-object we want to execute. 

Future Work
---

Future work may involve...
 * Extension to provide support for Lambda expressions (to provide Merge functions by implementing objects that can be chained together). 
 * We may be able to avoid re-inventing the wheel and extend the BiFunction interface. This means giving an implementing object control of the expression they want to pass to the strategy object, and letting the strategy object determine whether that can be considered good or bad output. This would require further research.

Search Strategies
---

A Search strategy entity follows the strategy design pattern to implement the operations to perform object searching.

The implementations provide a selection of algorithms which arbitrate a ```search()``` operation (see documentation).

To add a new ```search()``` operation create a new class implementing the appropriate SearchStrategy interface overriding to provide your chosen functionality.

Merge Strategies
---

A Mergable strategy entity follows the strategy design pattern to implement the operations to perform object merging.

The implementations provide a selection of algorithms which arbitrate a ```merge()``` operation (see documentation).

To add a new ```merge()``` operation create a new class implementing the appropriate MergeStrategy interface overriding to provide your chosen functionality.