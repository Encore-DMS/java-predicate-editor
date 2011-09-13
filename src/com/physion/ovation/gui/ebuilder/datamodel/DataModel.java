package com.physion.ovation.gui.ebuilder.datamodel;

import java.util.ArrayList;

import com.physion.ovation.gui.ebuilder.datatypes.ClassDescription;
import com.physion.ovation.gui.ebuilder.datatypes.Attribute;
import com.physion.ovation.gui.ebuilder.datatypes.Type;
import com.physion.ovation.gui.ebuilder.datatypes.Cardinality;


/**
 * 
 */
public class DataModel {

    private static DataModel instance;

    /**
     * These are all the ClassDescriptions that exists in the system.
     */
    private static ArrayList<ClassDescription> allClassDescriptions;

    /**
     * These are the subset of all ClassDescriptions that are possible
     * choices in the very first/topmost row.  I.e. these are possible
     * Class Under Qualification choices.
     */
    private static ArrayList<ClassDescription> possibleCUQs;


    public static DataModel getInstance() {

        if (instance == null) {
            instance = new DataModel();
        }
        return(instance);
    }


    private DataModel() {

        allClassDescriptions = new ArrayList<ClassDescription>();
        possibleCUQs = new ArrayList<ClassDescription>();

        initialize();
    }



    /**
     * This method creates hard coded values at the moment.
     * Eventually, these values will come from a configuration file.
     */
    private static void initialize() {

        ClassDescription entityBaseCD =
            new ClassDescription("EntityBase", null);
        allClassDescriptions.add(entityBaseCD);

        ClassDescription taggableEntityBaseCD =
            new ClassDescription("TaggableEntityBase", entityBaseCD);
        allClassDescriptions.add(taggableEntityBaseCD);

        ClassDescription timelineElementCD =
            new ClassDescription("TimelineElement", taggableEntityBaseCD);
        allClassDescriptions.add(timelineElementCD);

        ClassDescription epochCD =
            new ClassDescription("Epoch", timelineElementCD);
        allClassDescriptions.add(epochCD);
        possibleCUQs.add(epochCD);

        ClassDescription sourceCD =
            new ClassDescription("Source", taggableEntityBaseCD);
        allClassDescriptions.add(sourceCD);
        possibleCUQs.add(sourceCD);

        ClassDescription userCD =
            new ClassDescription("User", taggableEntityBaseCD);
        allClassDescriptions.add(userCD);

        ClassDescription keywordTagCD =
            new ClassDescription("KeywordTag", entityBaseCD);
        allClassDescriptions.add(keywordTagCD);

        ClassDescription epochGroupCD =
            new ClassDescription("EpochGroup", timelineElementCD);
        allClassDescriptions.add(epochGroupCD);

        /**
         * Initialize values of the EntityBase class.
         */
        Attribute attribute = new Attribute("owner", Type.REFERENCE,
                                            userCD, Cardinality.TO_ONE);
        entityBaseCD.addAttribute(attribute);

        attribute = new Attribute("uuid", Type.UTF_8_STRING);
        entityBaseCD.addAttribute(attribute);

        attribute = new Attribute("incomplete", Type.BOOLEAN);
        entityBaseCD.addAttribute(attribute);

        /**
         * Initialize values of the Epoch class.
         */
        attribute = new Attribute("protocolID", Type.UTF_8_STRING);
        epochCD.addAttribute(attribute);

        attribute = new Attribute("excludeFromAnalysis", Type.BOOLEAN);
        epochCD.addAttribute(attribute);

        attribute = new Attribute("nextEpoch", Type.REFERENCE,
                                  epochCD, Cardinality.TO_ONE);
        epochCD.addAttribute(attribute);

        /**
         * Initialize values of the Source class.
         */
        attribute = new Attribute("label", Type.UTF_8_STRING);
        sourceCD.addAttribute(attribute);

        attribute = new Attribute("parent", Type.REFERENCE,
                                  sourceCD, Cardinality.TO_ONE);
        sourceCD.addAttribute(attribute);

        /**
         * Initialize values of the EpochGroup class.
         */
        attribute = new Attribute("label", Type.UTF_8_STRING);
        epochGroupCD.addAttribute(attribute);

        attribute = new Attribute("source", Type.REFERENCE,
                                  sourceCD, Cardinality.TO_ONE);
        epochGroupCD.addAttribute(attribute);
    }


    public static ArrayList<ClassDescription> getAllClassDescriptions() {
        return(allClassDescriptions);
    }


    public static ArrayList<ClassDescription> getPossibleCUQs() {
        return(possibleCUQs);
    }

    
    /**
     * This is a simple test program for this class.
     */
    public static void main(String[] args) {

        System.out.println("DataModel test is starting...");

        DataModel dataModel = DataModel.getInstance();

        System.out.println("DataModel test is ending.");
    }
}