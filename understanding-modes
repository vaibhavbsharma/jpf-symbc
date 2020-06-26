Java Ranger can be run in 5 different modes as set up the [veritestingMode](https://vaibhavbsharma.github.io/java-ranger/docs/gov/nasa/jpf/symbc/VeritestingListener.VeritestingMode.html). `veritestingMode` specifies the path-merging features to be enabled with each higher number adding a new feature to the set of features enabled by the previous number. 
* Setting `veritestingMode` to 1 runs vanilla SPF. 
* Setting it to 2 enables path-merging for multi-path regions with no method calls and a single exit point. 
* Setting it to 3 adds path-merging for multi-path regions that make method calls where the method can be summarized by Java Ranger. 
* Setting it to 4 adds path-merging for multi-path regions with more than one exit point caused due to exceptional behavior and unsummarized method calls. 
* Setting it to 5 adds path-merging for summarizing return instructions in multi-path regions by treating them as an additional exit point. This is the most advanced mode of Java Ranger that turns on all of its capabilities. 

We've broken Java Ranger's abilities into these five different modes because our evaluation has shown that path-merging is not always beneficial. Java Ranger's performance with the `replace` benchmark is a striking evidence of this phenomenon. Limiting the path-merging capability can sometimes lead to better performance. While we do not currently have a way to find the best mode to set Java Ranger in, running it in all five modes in parallel can potentially lead to the best performance for the time being. 
