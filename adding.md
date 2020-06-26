# Adding New Transformations to Java Ranger

When considering a new transformation to be added to Java Ranger, you should first ask yourself if this transformation needs to be done exactly once or multiple times as part of a fixed-point computation. If it is a former kind of transformation, then call it from `VeritestingListener.runVeritesting`. If it is the latter kind of transformation, then call it from `FixedPointWrapper.executeFixedPointTransformations`. In either case, you will want to add it to the `gov.nasa.jpf.symbc.veritesting.ast.transformations` package. If the transformation is a static one (changes only the static representation of a region in JR IR), you will want to ensure you call it inside the `StaticRegion` constructor. 

One important point to note for all JR transformations is that every region summary (represented as a `Stmt` inside `StaticRegion` or `DynamicRegion`) is immutable. Please try to maintain this design in new transformations because it helps examine the effect of each transformation on a region summary. 

The newly introduced transformation can choose to modify the summary's state or the summary's statement. The state refers to the fields contained in the `StaticRegion` or `DynamicRegion` whereas the statement refers to the `stmt` or `dynStmt` field in these two classes respectively. The state can include 
* the path-subscript map (stored in `psm`)
  - this map is used for handling field accesses
* the mapping of local inputs and outputs of the region to the stack slot of the local variables (stored in `inputTable` and `outputTable`)
* a mapping of variables in the summary's statement to the corresponding variable type (stored in `varTypeTable`)
* the offset of the instruction at which the multi-path region ends (stored in `endIns`)
* a summary of all the conditions used to explore the SPF cases in the summary. This can just be false in case there are no SPF cases in the summary (stored in `spfPredicateSummary`)
* a list of all the SPF cases in the summary (stored in `SPFCases`)
* a list of output expressions to be written into array entries (stored in `arrayOutputs`)
* a mapping of variables to their constant values for all constants in the summary (stored in `constantsTable`)
