BadNieces (The ShortestSuperstringProblem)
====

Problem Statement 
---
Write a program to reassemble a given set of text fragments into their original sequence. For this challenge your program should have a main method accepting one argument – the path to a well-formed UTF-8 encoded text file. Each line in the file represents a test case of the main functionality of your program: read it, process it and println to the console the corresponding defragmented output. 

Each line contains text fragments separated by a semicolon, ‘;’. You can assume that every fragment has length at least 2. 

Example input 1: 
O draconia;conian devil! Oh la;h lame sa;saint! 
 
Example output 1: 
O draconian devil! Oh lame saint! 
 
Example input 2: 
m quaerat voluptatem.;pora incidunt ut labore et d;, consectetur, adipisci velit;olore magnam aliqua;idunt ut labore et dolore magn;uptatem.;i dolorem ipsum qu;iquam quaerat vol;psum quia dolor sit amet, consectetur, a;ia dolor sit amet, conse;squam est, qui do;Neque porro quisquam est, qu;aerat voluptatem.;m eius modi tem;Neque porro qui;, sed quia non numquam ei;lorem ipsum quia dolor sit amet;ctetur, adipisci velit, sed quia non numq;unt ut labore et dolore magnam aliquam qu;dipisci velit, sed quia non numqua;us modi tempora incid;Neque porro quisquam est, qui dolorem i;uam eius modi tem;pora inc;am al

Example output 2: 
Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.
 
You may also assume: 
 
1.	Your niece’s attention to detail is impeccable; she has not misread any fragments or made any typos. 
2.	All test cases / input lines will reduce to one unambiguous, final, reassembled document. 

Implementation 
---

For each input line, search the collection of fragments to locate the pair with the maximal overlap match then merge those two fragments. This operation will decrease the total number of fragments by one. Repeat until there is only one fragment remaining in the collection. This is the defragmented line / reassembled document. 
If there is more than one pair of maximally overlapping fragments in any iteration then just merge one of them. So long as you merge one maximally overlapping pair per iteration the test inputs are guaranteed to result in good and deterministic output. 
When comparing for overlaps, compare case sensitively. 

How to run
---

Compile the source code and execute the program as a Java application passing the input file as an absolute path to the main method found in the class BadNiecesIO (at the ```badnieces``` project package).
Make sure that LOGS_DIR configuration in the BadNiecesIO class points to a valid directory (ideally an empty folder).

Definitions.
---
The working definitions of this application are the following...
* A fragment is defined a mergable string.
* A line is defined as a set of text fragments separated by semi-colons.
* A fragment thus contains no semi-colons (denoting a new line).

Solving the algorithm
---

There were two main challenges to solving the algorithm.
The first challenge was identifying the maximally overlapping pairs, the second challenge was identifying how to then merge the maximally overlapping pairs.

Identifying Maximally Overlapping Pairs
---

Consider the following strings
```
String 1 =  ABCDEF
String 2 = DEFG
String 3 = BCDE
Sting 4 = XCDEZ
String 5 = 123DEF
String 6 = ABCDEF
```

Strings 1 and 2 overlap because DEF overlaps with a length of 3.
Strings 1 and 3 overlap because CDE overlaps with a length of 3.
Strings 1 and 4 do not overlap because the overlaps do not occur at the start or end of the string.
For String 5 and String 6 it was not specified whether or not they constitute an overlap (so an assumption was made it does not).

Since there is no need to consider other than merges for the start or end string cases, there are then thus two cases where overlap is possible.
Case 1 will describe an overlap of strings from the beginning of a string i.e. ABCDEF and 123ABC123.
Case 2 will describe an overlap of strings from the end of a string i.e. ABCDEF DEF123 (where its important to note ABCDEF and 123DEF123 would be invalid).

Each are considered in turn...

*Case 1*

```
String 1: ABCDEF
String 2:    DEFGHI
```

Recall a string is just a character representation at indexes.
This means if we can know the indexes of the characters of the head of a string that are contained within the superstring, we can know precisely where that overlap occurs.
Our search becomes.
```
D E F [GHI]
3,4,5 at ABCDEF
```
The length of the index is thus the number of overlapping characters.

*Case 2*

```
String 1: ABCDEF
String 2: DEF123
```

If we look carefully this is just Case 1 in reverse. By flipping the strings we get...

```
String 1:    DEF123
String 2: ABCDEF
```
Thus our index (from Case 1) becomes.
```
D E F [123]
3,4,5 at ABCDEF
```

There is however a third case...


```
String mergeFrom = "ABCDEF";
String mergeInto = "DEFABC";
```

Do we merge ABCDEFABC or DEFABCDEF? Only the order of the merge can determine which of the two results to use. So whatever algorithm we implement should take this into consideration. The merge operation is not then just an overlap, but an overlap pair.

Merging Strings
---

If we know the indexes of where the merge occurs then the merging process is trivial substringing.

Consider...

```
String 1: DEF123
String 2: ABCDEF
```

We know DEF occurs at index 3,4,5 in the second string.
To then perform a merge operation we can apply sub-stringing to the following merge operation...

```
Create a new string equal to the second string [ABC until the index at position 3 (where the head of the first string occurs).
Continue building the range of that index [DEF].
At the end of that index continue building the second string until termination [ABCDEF].
```

Solution Design Considerations
---

* The scenario is to be designed as an application, and not as a script. The problem could be solved using Python providing an implementation of the ShortestSuperStringProblem. But where the programming challenge resembles a test of programming competency, it should best feature a solution approach by simplifying the problem and implementing a solution using object oriented programming concepts.
* The implementation should be production ready. This means it must be documented, tested, achieve its goal, and be easy to understand. The application should be safe to misuse, and not cause damage to an existing production server. More importantly the application should produce logs. Can you imagine a production application that has no logs?!
* There might be many different kinds of documents we want to search. We might want to search different documents with different encoding kinds. But we also can't know what the encoding is until we read the document. A feature should be provided to meaningfully allow us to extend the supported Unicode formats.
* However it is possible (but not known) that different file encodings may impact implementation details. A design decision was made to constrain encoding (UTF16 encoded files, but provide at least a framework which will allow the application to read files in different encoding formats.
* In the future we might want to search documents in many different ways. Additionally, in the future we might want to merge documents in many different ways. The implementation should thus not strongly couple the algorithm to search the document or merge the document with the algorithm used to solve the problem statement (it should be flexible to allow us to search many fragments in many different ways).
* The application should be scalable. To illustrate, what if we want to support many documents being processed concurrently.
* The application should focus on object oriented architecture (and not a glorified call and return strategy). Each object should be responsibly driven, have its own function and purpose.

Implementation
---
 * The merging process is handled using recursion over strategies via the Strategy Design Pattern (which implement the above two algorithms). Therein there exists a strategy to search for the next fragment, and a strategy to merge the fragments. Choosing different strategies allows an object responsible to compile documents together, the document compositor, to produce different results.
 * In this case we can trivially choose the strategies of choice to produce the desired output. Strategies are designed to cleanly fail, and if something really bad happens for whatever reason during the merging process you can always choose to restore from a backup as part of the implementation (a feature in the spirit of production ready design).


Greatest Challenge
---
 * A first the algorithm used to solve the problem was strongly coupled one way or another to the document itself (iterating through arrays of characters to resolve the issue). Even the strategies somewhat depended on each other as they were passing around strings and arrays of characters. The algorithms went through multiple iterations not to solve the problem as such, but to find the right choice of generified objects which can be used to search and merge our fragments.
 * Taking to heart what you said. "Write production level code" and "You wouldn't write a script in Java." We are writing an application. Not a tool. I focused more on making more modular the elements of the application so they can be redrafted and re-purposed. What I found is once I had the structure right (what the combinations of functions needs to be and identified I can just use recursion to follow the instructions of the challenge) the application was very easy to debug and refactor (as long as each function was meeting its purpose, in particular the components to get the next overlapping pair and merge a pair, it didn't matter as much how it did it as long as it didn't involve other objects as the theory was sound). 
 * Settled on the Document class which could be changed to better support multi-threading (although since all the data is contained within the object this should be fine).
