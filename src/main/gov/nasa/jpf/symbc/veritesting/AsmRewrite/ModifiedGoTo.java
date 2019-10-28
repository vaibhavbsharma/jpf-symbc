package GoToTransformation;

import javafx.util.Pair;
import org.objectweb.asm.Label;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * This class collects all GoTo instruction numbers whose target is going to change, and also the number of the last
 * GOTO where we need to add a label. Also it assigns a new label.
 */
public class ModifiedGoTo {
    public Integer goToPosition;
    public Label jumpLabel;
    public boolean isLastGoTo = false;


    public ModifiedGoTo(Integer goToPosition, Label jumpLabel, boolean isLastGoTo) {
        this.goToPosition = goToPosition;
        this.jumpLabel = jumpLabel;
        this.isLastGoTo = isLastGoTo;
    }

    /**
     * This method iterates over all methods for which we have collected a backedge and tries to create the right
     * label set for it.
     */
    public static HashMap<String, Pair<ArrayList<Label>, HashMap<Integer, ModifiedGoTo>>> createAll(HashMap<String, ArrayList<Pair<Integer, Label>>>
                                                           methodCollectedJmpInstMap,
                                                                                                    HashMap<String, ArrayList<Label>>  methodBackEdgeTargetLabels) {


        HashMap<String, Pair<ArrayList<Label>, HashMap<Integer, ModifiedGoTo>>> newMethodGoToMap = new HashMap<>();

        Iterator<Map.Entry<String, ArrayList<Pair<Integer, Label>>>> methodCollectedJmpInstMapItr = methodCollectedJmpInstMap.entrySet().iterator();

        while(methodCollectedJmpInstMapItr.hasNext()){
            Map.Entry<String, ArrayList<Pair<Integer, Label>>> entry = methodCollectedJmpInstMapItr.next();

            String methodName = entry.getKey();

            ArrayList<Pair<Integer, Label>> collectedJmpInst = entry.getValue();

            ArrayList<Label> backEdgeLabel = methodBackEdgeTargetLabels.get(methodName);

            Pair newGoToPair = createForMethod(collectedJmpInst, backEdgeLabel);

            newMethodGoToMap.put(methodName, newGoToPair);
        }

        return newMethodGoToMap;
    }


    public static Pair<ArrayList<Label>, HashMap<Integer, ModifiedGoTo>> createForMethod(ArrayList<Pair<Integer, Label>> collectedJumpInstructions, ArrayList<Label>
            backEdgeTargetLabels) {
        HashMap<Integer, ModifiedGoTo> goToInsHashMap = new HashMap<>();
        ArrayList<Label> newLabels = new ArrayList<>();

        for (Label backLabel : backEdgeTargetLabels) { // for every identified backedge label we try to get the
            // corrsponding goTo instructions, check if there are more than 2 goTo jumping to the same backedge
            // label, if so then we need to modify those, except for the last one.

            ArrayList<Pair<Integer, Label>> relatedGoToIns = getRelatedGoToInst(backLabel, collectedJumpInstructions);
            if (relatedGoToIns.size() >= 2) {// three or more goTos are going to the same back edge label, then this is where we want to change
                Label newLabel = new Label();
                newLabels.add(newLabel);
                for (int i = 0; i <= relatedGoToIns.size() - 2; i++) { //change all of them to a new label except for
                    // the last one.
                    Pair<Integer, Label> myGoToPosition = relatedGoToIns.get(i);
                    goToInsHashMap.put(myGoToPosition.getKey(), new ModifiedGoTo(myGoToPosition.getKey(), newLabel, false));
                }
                //setting the last goTo statement to have its old label but a new flag that it is the last go to.
                Pair<Integer, Label> lastGoToPosition = relatedGoToIns.get(relatedGoToIns.size() - 1);
                goToInsHashMap.put(lastGoToPosition.getKey(), new ModifiedGoTo(lastGoToPosition.getKey(),
                        newLabel,
                        true));
            }

        }
        return new Pair<ArrayList<Label>, HashMap<Integer, ModifiedGoTo>>(newLabels, goToInsHashMap);
    }


    private static ArrayList<Pair<Integer, Label>> getRelatedGoToInst(Label backLabel, ArrayList<Pair<Integer, Label>> collectedJumpInstructions) {
        ArrayList<Pair<Integer, Label>> filteredGoToLabels = new ArrayList<Pair<Integer, Label>>();

        for (Pair<Integer, Label> goToLabelPair : collectedJumpInstructions) {
            if (goToLabelPair.getValue().equals(backLabel))
                filteredGoToLabels.add(goToLabelPair);
        }
        return filteredGoToLabels;
    }
}
