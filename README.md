# Java Ranger
Java Ranger is a path-merging extension of Symbolic PathFinder (SPF). In this tool, we've extended the `veritesting` technique by Avgerinos et al. (see paper [here](https://users.ece.cmu.edu/~aavgerin/papers/veritesting-icse-2014.pdf)) for symbolic execution of Java bytecode. 

The setup of Java Ranger is very similar to the setup used by SPF. The only difference  in  setup  is,  since Java Ranger is  simply  an  extension  of  SPF,  the Java Ranger directory can be specified as a valid `jpf-symbc` extension of JPF. The example configuration required by Java Ranger is exactly the same as the configuration that is required by SPF with the following additions.

`veritestingMode = <1-5>`

veritestingMode specifies the path-merging features to be enabled with each higher number adding a new feature to the set of features enabled by the previous number. Setting veritestingMode to 1 runs vanilla SPF. Setting it to 2 enables path-merging for multi-path regions with no method calls and a single exit point. Setting it to 3 adds path-merging for multi-path regions that make method calls where the method can be summarized by Java Ranger. Setting it to 4 adds path-merging  for  multi-path  regions  with  more  than  one  exit  point  caused  due  to exceptional behavior and unsummarized method calls. Setting it to 5 adds path-merging for summarizing  `return` instructions in multi-path regions by treating them as an additional exit point.

`performanceMode = <true or false>`

Setting `performanceMode` to true causes Java Ranger to minimize the number of solver calls to check the feasibility of the path condition when summarizing a multi-path region with multiple exit points.

`TARGET_CLASSPATH_WALA=<classpath of target code>`

Java Ranger needs this variable to be set up as environment variable. It is not part of the `.jpf` configuration file. This environment variable tells Java Ranger where it should be expecting to find code that needs to be statically summarized

## People
The following people have contributed to Java Ranger
1. Soha Hussein
2. Vaibhav Sharma
3. Michael Whalen
4. Stephen McCamant
5. Willem Visser
