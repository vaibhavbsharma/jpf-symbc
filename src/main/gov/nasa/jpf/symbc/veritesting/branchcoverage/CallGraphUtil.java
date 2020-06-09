package gov.nasa.jpf.symbc.veritesting.branchcoverage;

import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.collections.CollectionFilter;
import com.ibm.wala.util.graph.Graph;
import com.ibm.wala.util.graph.GraphSlicer;
import java.util.function.Predicate;

import java.util.Collection;

public class CallGraphUtil {

    /**
     * Restrict g to nodes from the Application loader
     */

    public static Graph<CGNode> pruneForAppLoader(CallGraph g) throws WalaException {
        Predicate<CGNode> f = new Predicate<CGNode>() {
            @Override
            public boolean test(CGNode cgNode) {
                return (cgNode.getMethod().getDeclaringClass().getClassLoader().getReference().equals(ClassLoaderReference.Application));
            }
        };
        return pruneGraph(g, f);
    }

    public static <T> Graph<T> pruneGraph(Graph<T> g, Predicate<T> f) throws WalaException {
        Collection<T> slice = GraphSlicer.slice(g, f);
        return GraphSlicer.prune(g, new CollectionFilter<>(slice));
    }

}
