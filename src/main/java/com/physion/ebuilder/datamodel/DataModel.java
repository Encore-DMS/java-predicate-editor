/**
 * Copyright (c) 2011. Physion Consulting LLC
 * All rights reserved.
 */
package com.physion.ebuilder.datamodel;

import com.physion.ebuilder.datatypes.Attribute;
import com.physion.ebuilder.datatypes.Cardinality;
import com.physion.ebuilder.datatypes.ClassDescription;
import com.physion.ebuilder.datatypes.Type;

import java.util.ArrayList;


/**
 * This class manages all the data types/classes that are part
 * of the system.  It is a singleton.  (I.e. only one instance
 * of this class should exist.)
 *
 * At some point in the future, Physion will probably replace
 * this class with something else that more closely matches
 * their existing C++ code.
 */
public class DataModel {

    /**
     * Possible types for a "properties" keyed value.
     * I.e. A user can declare that a keyed property
     * is one of these types.
     */
    public static final Type[] PROP_TYPES = {Type.INT_32,
                                             Type.FLOAT_64,
                                             Type.UTF_8_STRING,
                                             Type.DATE_TIME,
                                             Type.BOOLEAN};

    /**
     * The one an only instance of this DataModel.
     * We are a singleton.
     */
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


    /**
     * Get the one and only instance of the DataModel object.
     * @return Singleton DataModel instance
     */
    public static DataModel getInstance() {

        if (instance == null) {
            instance = new DataModel();
        }
        return(instance);
    }


    /**
     * Create the one and only DataModel object.
     * We are a singleton, so this constructor is private.
     */
    private DataModel() {

        allClassDescriptions = new ArrayList<ClassDescription>();
        possibleCUQs = new ArrayList<ClassDescription>();

        initialize();
    }


    /**
     * This method creates hard coded values at the moment.
     * Eventually, these values could come from a configuration file.
     * But, as of October 2011, we have decided that there is
     * nothing to be gained by writing a parser to do that.
     * Changing the code in this method is pretty simple.
     */
    private static void initialize() {

        ClassDescription entityBaseCD =
            new ClassDescription("EntityBase", null);
        allClassDescriptions.add(entityBaseCD);

        ClassDescription taggableEntityBaseCD =
            new ClassDescription("TaggableEntityBase", entityBaseCD);
        allClassDescriptions.add(taggableEntityBaseCD);

        ClassDescription annotatableEntityBaseCD =
                new ClassDescription("AnnotatableEntityBase", taggableEntityBaseCD);
        allClassDescriptions.add(annotatableEntityBaseCD);

        ClassDescription timelineElementCD =
            new ClassDescription("TimelineElement", annotatableEntityBaseCD);
        allClassDescriptions.add(timelineElementCD);


        ClassDescription purposeAndNotesEntityCD =
            new ClassDescription("PurposeAndNotesEntity", taggableEntityBaseCD);
        allClassDescriptions.add(purposeAndNotesEntityCD);

        ClassDescription iOBaseCD =
            new ClassDescription("IOBase", taggableEntityBaseCD);
        allClassDescriptions.add(iOBaseCD);

        ClassDescription responseBaseCD =
            new ClassDescription("ResponseBase", iOBaseCD);
        allClassDescriptions.add(responseBaseCD);

        ClassDescription projectCD =
            new ClassDescription("Project", purposeAndNotesEntityCD);
        allClassDescriptions.add(projectCD);

        ClassDescription experimentCD =
            new ClassDescription("Experiment", purposeAndNotesEntityCD);
        allClassDescriptions.add(experimentCD);

        ClassDescription externalDeviceCD =
            new ClassDescription("ExternalDevice", taggableEntityBaseCD);
        allClassDescriptions.add(externalDeviceCD);

        ClassDescription sourceCD =
            new ClassDescription("Source", taggableEntityBaseCD);
        allClassDescriptions.add(sourceCD);

        ClassDescription epochGroupCD =
            new ClassDescription("EpochGroup", timelineElementCD);
        allClassDescriptions.add(epochGroupCD);

        ClassDescription epochCD =
            new ClassDescription("Epoch", timelineElementCD);
        allClassDescriptions.add(epochCD);

        ClassDescription stimulusCD =
            new ClassDescription("Stimulus", iOBaseCD);
        allClassDescriptions.add(stimulusCD);

        ClassDescription responseCD =
            new ClassDescription("Response", responseBaseCD);
        allClassDescriptions.add(responseCD);

        ClassDescription derivedResponseCD =
            new ClassDescription("DerivedResponse", responseBaseCD);
        allClassDescriptions.add(derivedResponseCD);

        ClassDescription keywordTagCD =
            new ClassDescription("KeywordTag", entityBaseCD);
        allClassDescriptions.add(keywordTagCD);

        ClassDescription resourceCD =
            new ClassDescription("Resource", taggableEntityBaseCD);
        allClassDescriptions.add(resourceCD);

        ClassDescription userCD =
            new ClassDescription("User", taggableEntityBaseCD);
        allClassDescriptions.add(userCD);

        ClassDescription analysisRecordCD =
            new ClassDescription("AnalysisRecord", taggableEntityBaseCD);
        allClassDescriptions.add(analysisRecordCD);

        ClassDescription annotationCD =
                new ClassDescription("Note", taggableEntityBaseCD);
        allClassDescriptions.add(annotationCD);

        ClassDescription noteCD =
                new ClassDescription("Note", annotationCD);
        allClassDescriptions.add(noteCD);

        ClassDescription timelineAnnotationCD =
                new ClassDescription("TimelineAnnotation", annotationCD);
        allClassDescriptions.add(timelineAnnotationCD);

        /**
         * Of the ClassDescriptions we created above,
         * we want to have a subset of them available
         * as choices in the "Class Under Qualification"
         * comboBox at the top of the GUI.
         * Create the list of possible CUQ's now.
         */
        possibleCUQs.add(epochCD);
        possibleCUQs.add(sourceCD);
        possibleCUQs.add(analysisRecordCD);
        possibleCUQs.add(projectCD);
        possibleCUQs.add(epochGroupCD);

        /**
         * The ClassDescription objects we created above are
         * empty at this point.  Now we will set what Attributes
         * each ClassDescription contains.
         *
         * Each block of code below follows the same form:
         * Create an Attribute object, and add it to the
         * ClassDescription.
         *
         * There are a few Attribute object constructors
         * that you can use.  If the Attribute is a simple
         * primitive such as int, float, string, time,
         * you only need to provide a name and a type to
         * the Attribute constructor.
         *
         * If an Attribute is a reference to a class,
         * you also need to pass the ClassDescription
         * to which it is a reference, and its
         * cardinality:  to-one or to-many.
         *
         * The parameters-map, per-user, and per-user-parameters-map
         * Attributes are created in a similar way, but
         * have a "displayName" that is different than their "queryName".
         * An Attribute like "protocolID" appears in both the
         * GUI's comboBox as "protocolID" as well as in the Expression
         * that is generated from the Attribute.  But, an Attribute
         * like "keywords" or "mykeywords" appears in the comboBox
         * as "All Keywords" or "My Keywords".
         *
         * Just look at an example of the desired Attribute
         * type below and cut and paste the code, and change
         * the name of the Attribute.
         */

        Attribute attribute;

        /**
         * Initialize values of the EntityBase class.
         */
        attribute = new Attribute("owner", "Owner",
                                  Type.REFERENCE,
                                  userCD, Cardinality.TO_ONE);
        entityBaseCD.addAttribute(attribute);

        attribute = new Attribute("uuid", "UUID",
                                  Type.UTF_8_STRING);
        entityBaseCD.addAttribute(attribute);

        attribute = new Attribute("incomplete", "Incomplete",
                                  Type.BOOLEAN);
        entityBaseCD.addAttribute(attribute);

        attribute = new Attribute("myproperties", "My Property",
                                  Type.PER_USER_PARAMETERS_MAP,
                                  null, Cardinality.TO_MANY);
        entityBaseCD.addAttribute(attribute);

        attribute = new Attribute("properties", "Any Property",
                                  Type.PER_USER_PARAMETERS_MAP,
                                  null, Cardinality.TO_MANY);
        entityBaseCD.addAttribute(attribute);

        attribute = new Attribute("resources", "Resources",
                                  Type.REFERENCE,
                                  resourceCD, Cardinality.TO_MANY);
        entityBaseCD.addAttribute(attribute);

        /**
         * Initialize values of the TaggableEntityBase class.
         */
        attribute = new Attribute("mykeywords", "My Keywords",
                                  Type.PER_USER_OR_CUSTOM_REFERENCE_OPERATOR,
                                  keywordTagCD,
                                  Cardinality.TO_MANY);
        taggableEntityBaseCD.addAttribute(attribute);

        attribute = new Attribute("keywords", "All Keywords",
                                  Type.PER_USER_OR_CUSTOM_REFERENCE_OPERATOR,
                                  keywordTagCD,
                                  Cardinality.TO_MANY);
        taggableEntityBaseCD.addAttribute(attribute);


        /**
         * Initialize values of the AnnotatableEntityBase class
         */

        attribute = new Attribute("notes", "All Notes", Type.PER_USER_OR_CUSTOM_REFERENCE_OPERATOR,
                noteCD, Cardinality.TO_MANY);
        annotatableEntityBaseCD.addAttribute(attribute);

        attribute = new Attribute("mynotes", "My Notes", Type.PER_USER_OR_CUSTOM_REFERENCE_OPERATOR,
                noteCD, Cardinality.TO_MANY);
        annotatableEntityBaseCD.addAttribute(attribute);

        /**
         * Initialize values for the Annotation class
         */

        attribute = new Attribute("text", "Text",
                                  Type.UTF_8_STRING);
        annotationCD.addAttribute(attribute);

        /**
         * Initialize values for the TimelineAnnotation class
         */

        attribute = new Attribute("startTime", "Start Time",
                                  Type.DATE_TIME);
        timelineAnnotationCD.addAttribute(attribute);

        attribute = new Attribute("endTime", "End Time",
                                  Type.DATE_TIME);
        timelineAnnotationCD.addAttribute(attribute);

        attribute = new Attribute("startTimeZone", "Start Timezone",
                                  Type.UTF_8_STRING);
        timelineAnnotationCD.addAttribute(attribute);

        attribute = new Attribute("endTimeZone", "End Timezone",
                                  Type.UTF_8_STRING);
        timelineAnnotationCD.addAttribute(attribute);

        /**
         * Initialize values of the TimelineElement class.
         */
        attribute = new Attribute("startTime", "Start Time",
                                  Type.DATE_TIME);
        timelineElementCD.addAttribute(attribute);

        attribute = new Attribute("endTime", "End Time",
                                  Type.DATE_TIME);
        timelineElementCD.addAttribute(attribute);

        attribute = new Attribute("startTimeZone", "Start Timezone",
                                  Type.UTF_8_STRING);
        timelineElementCD.addAttribute(attribute);

        attribute = new Attribute("endTimeZone", "End Timezone",
                                  Type.UTF_8_STRING);
        timelineElementCD.addAttribute(attribute);

        attribute = new Attribute("timelineannotations", "All Timeline Annotations", Type.PER_USER_OR_CUSTOM_REFERENCE_OPERATOR,
                timelineAnnotationCD, Cardinality.TO_MANY);
        timelineElementCD.addAttribute(attribute);

        attribute = new Attribute("mytimelineannotations", "My Timeline Annotations", Type.PER_USER_OR_CUSTOM_REFERENCE_OPERATOR,
                timelineAnnotationCD, Cardinality.TO_MANY);
        timelineElementCD.addAttribute(attribute);

        /**
         * Initialize values of the PurposeAndNotesEntity class.
         */
        attribute = new Attribute("purpose", "Purpose",
                                  Type.UTF_8_STRING);
        purposeAndNotesEntityCD.addAttribute(attribute);

        attribute = new Attribute("notes", "Notes",
                                  Type.UTF_8_STRING);
        purposeAndNotesEntityCD.addAttribute(attribute);

        /**
         * Initialize values of the IOBase class.
         */
        attribute = new Attribute("units", "Units",
                                  Type.UTF_8_STRING);
        iOBaseCD.addAttribute(attribute);

        attribute = new Attribute("externalDevice", "External Device",
                                  Type.REFERENCE,
            externalDeviceCD, Cardinality.TO_ONE);
        iOBaseCD.addAttribute(attribute);

        attribute = new Attribute("externalDeviceParameters", "External Device Parameters",
                                  Type.PARAMETERS_MAP, null, Cardinality.N_A);
        iOBaseCD.addAttribute(attribute);

        /**
         * Initialize values of the ResponseBase class.
         */
        attribute = new Attribute("dateType", "Data Type",
                                  Type.INT_16);
        responseBaseCD.addAttribute(attribute);

        attribute = new Attribute("byteOrder", "Byte Order",
                                  Type.INT_16);
        responseBaseCD.addAttribute(attribute);

        attribute = new Attribute("sampleBytes", "Sample Size (bytes)",
                                  Type.INT_16);
        responseBaseCD.addAttribute(attribute);

        /**
         * Initialize values of the Project class.
         */
        attribute = new Attribute("name", "Name",
                                  Type.UTF_8_STRING);
        projectCD.addAttribute(attribute);

        attribute = new Attribute("experiments", "Experiments",
                                  Type.REFERENCE,
            experimentCD, Cardinality.TO_MANY);
        projectCD.addAttribute(attribute);

        attribute = new Attribute("myanalysisRecords", "My AnalysisRecords",
                                  Type.PER_USER_OR_CUSTOM_REFERENCE_OPERATOR,
                                  analysisRecordCD, Cardinality.TO_MANY);
        projectCD.addAttribute(attribute);

        attribute = new Attribute("analysisRecords", "All AnalysisRecords",
                                  Type.PER_USER_OR_CUSTOM_REFERENCE_OPERATOR,
                                  analysisRecordCD, Cardinality.TO_MANY);
        projectCD.addAttribute(attribute);

        /**
         * Initialize values of the Experiment class.
         */
        attribute = new Attribute("project", "Project",
                                  Type.REFERENCE,
            projectCD, Cardinality.TO_MANY);
        experimentCD.addAttribute(attribute);

        attribute = new Attribute("sources", "Sources",
                                  Type.REFERENCE,
            sourceCD, Cardinality.TO_MANY);
        experimentCD.addAttribute(attribute);

        attribute = new Attribute("externalDevices", "External Devices",
                                  Type.REFERENCE,
            externalDeviceCD, Cardinality.TO_MANY);
        experimentCD.addAttribute(attribute);

        attribute = new Attribute("epochGroups", "Epoch Groups",
                                  Type.REFERENCE,
            epochGroupCD, Cardinality.TO_MANY);
        experimentCD.addAttribute(attribute);

        attribute = new Attribute("curated", "Curated",
                                  Type.BOOLEAN);
        experimentCD.addAttribute(attribute);

        /**
         * Initialize values of the ExternalDevice class.
         */
        attribute = new Attribute("name", "Name",
                                  Type.UTF_8_STRING);
        externalDeviceCD.addAttribute(attribute);

        attribute = new Attribute("manufacturer", "Manufacturer",
                                  Type.UTF_8_STRING);
        externalDeviceCD.addAttribute(attribute);

        attribute = new Attribute("experiments", "Experiments",
                                  Type.REFERENCE,
            experimentCD, Cardinality.TO_MANY);
        externalDeviceCD.addAttribute(attribute);

        /**
         * Initialize values of the Source class.
         */
        attribute = new Attribute("label", "Label",
                                  Type.UTF_8_STRING);
        sourceCD.addAttribute(attribute);

        attribute = new Attribute("experiments", "Experiments",
                                  Type.REFERENCE,
            experimentCD, Cardinality.TO_MANY);
        sourceCD.addAttribute(attribute);

        attribute = new Attribute("parent", "Parent",
                                  Type.REFERENCE,
                                  sourceCD, Cardinality.TO_ONE);
        sourceCD.addAttribute(attribute);

        attribute = new Attribute("children", "Children",
                                  Type.REFERENCE,
                                  sourceCD, Cardinality.TO_MANY);
        sourceCD.addAttribute(attribute);

        attribute = new Attribute("epochGroups", "Epoch Groups",
                                  Type.REFERENCE,
            epochGroupCD, Cardinality.TO_MANY);
        sourceCD.addAttribute(attribute);

        attribute = new Attribute("containing_experiments", "Containing Experiments",
                                  Type.PER_USER_OR_CUSTOM_REFERENCE_OPERATOR,
                                  experimentCD,
                                  Cardinality.TO_MANY);
        sourceCD.addAttribute(attribute);

        /**
         * Initialize values of the EpochGroup class.
         */
        attribute = new Attribute("label", "Label",
                                  Type.UTF_8_STRING);
        epochGroupCD.addAttribute(attribute);

        attribute = new Attribute("experiment", "Experiment",
                                  Type.REFERENCE,
                                  experimentCD, Cardinality.TO_ONE);
        epochGroupCD.addAttribute(attribute);

        attribute = new Attribute("source", "Source",
                                  Type.REFERENCE,
                                  sourceCD, Cardinality.TO_ONE);
        epochGroupCD.addAttribute(attribute);

        attribute = new Attribute("epochs", "Epochs",
                                  Type.REFERENCE,
                                  epochCD, Cardinality.TO_MANY);
        epochGroupCD.addAttribute(attribute);

        attribute = new Attribute("parent", "Parent",
                                  Type.REFERENCE,
                                  epochGroupCD, Cardinality.TO_ONE);
        epochGroupCD.addAttribute(attribute);

        attribute = new Attribute("children", "Children",
                                  Type.REFERENCE,
                                  epochGroupCD, Cardinality.TO_MANY);
        epochGroupCD.addAttribute(attribute);

        attribute = new Attribute("eg_containing_experiments", "Containing Experiments",
                                  Type.PER_USER_OR_CUSTOM_REFERENCE_OPERATOR,
                                  experimentCD,
                                  Cardinality.TO_MANY);
        epochGroupCD.addAttribute(attribute);

        /**
         * Initialize values of the Epoch class.
         */
        attribute = new Attribute("protocolID", "Protocol ID",
                                  Type.UTF_8_STRING);
        epochCD.addAttribute(attribute);

        attribute = new Attribute("protocolParameters", "Protocol Parameters",
                                  Type.PARAMETERS_MAP,
                                  null, Cardinality.N_A);
        epochCD.addAttribute(attribute);

        attribute = new Attribute("excludeFromAnalysis", "Exclude from Analysis",
                                  Type.BOOLEAN);
        epochCD.addAttribute(attribute);

        attribute = new Attribute("stimuli", "Stimuli",
                                  Type.REFERENCE,
                                  stimulusCD, Cardinality.TO_MANY);
        epochCD.addAttribute(attribute);

        attribute = new Attribute("responses", "Responses",
                                  Type.REFERENCE,
                                  responseCD, Cardinality.TO_MANY);
        epochCD.addAttribute(attribute);

        attribute = new Attribute("myderivedResponses", "My DerivedResponses",
                                  Type.PER_USER_OR_CUSTOM_REFERENCE_OPERATOR,
                                  derivedResponseCD, Cardinality.TO_MANY);
        epochCD.addAttribute(attribute);

        attribute = new Attribute("derivedResponses", "All DerivedResponses",
                                  Type.PER_USER_OR_CUSTOM_REFERENCE_OPERATOR,
                                  derivedResponseCD, Cardinality.TO_MANY);
        epochCD.addAttribute(attribute);

        attribute = new Attribute("epochGroup", "Epoch Group",
                                  Type.REFERENCE,
                                  epochGroupCD, Cardinality.TO_ONE);
        epochCD.addAttribute(attribute);

        attribute = new Attribute("analysisRecords", "Analysis Records",
                                  Type.REFERENCE,
                                  analysisRecordCD, Cardinality.TO_MANY);
        epochCD.addAttribute(attribute);

        attribute = new Attribute("nextEpoch", "Next Epoch",
                                  Type.REFERENCE,
                                  epochCD, Cardinality.TO_ONE);
        epochCD.addAttribute(attribute);

        attribute = new Attribute("previousEpoch", "Previous Epoch",
                                  Type.REFERENCE,
                                  epochCD, Cardinality.TO_ONE);
        epochCD.addAttribute(attribute);

        /**
         * Initialize values of the Stimulus class.
         */
        attribute = new Attribute("epoch", "Epoch",
                                  Type.REFERENCE,
                                  epochCD, Cardinality.TO_ONE);
        stimulusCD.addAttribute(attribute);

        attribute = new Attribute("pluginID", "Plugin ID",
                                  Type.UTF_8_STRING);
        stimulusCD.addAttribute(attribute);

        attribute = new Attribute("stimulusParameters", "Stimulus Parameters",
                                  Type.PARAMETERS_MAP, null, Cardinality.N_A);
        stimulusCD.addAttribute(attribute);

        /**
         * Initialize values of the Response class.
         */
        attribute = new Attribute("epoch", "Epoch",
                                  Type.REFERENCE,
                                  epochCD, Cardinality.TO_ONE);
        responseCD.addAttribute(attribute);

        attribute = new Attribute("samplingRate", "Sampling Rate",
                                  Type.FLOAT_64);
        responseCD.addAttribute(attribute);

        attribute = new Attribute("samplingUnits", "Sampling Rate Units",
                                  Type.UTF_8_STRING);
        responseCD.addAttribute(attribute);

        /**
         * Initialize values of the DerivedResponse class.
         */
        attribute = new Attribute("epoch", "Epoch",
                                  Type.REFERENCE,
                                  epochCD, Cardinality.TO_ONE);
        derivedResponseCD.addAttribute(attribute);

        attribute = new Attribute("description", "Description",
                                  Type.UTF_8_STRING);
        derivedResponseCD.addAttribute(attribute);

        attribute = new Attribute("name", "Name",
                                  Type.UTF_8_STRING);
        derivedResponseCD.addAttribute(attribute);

        attribute = new Attribute("derivationParameters", "Derivation Parameters",
                                  Type.PARAMETERS_MAP, null, Cardinality.N_A);
        derivedResponseCD.addAttribute(attribute);

        /**
         * Initialize values of the KeywordTag class.
         */
        attribute = new Attribute("tag", "Keyword Tag",
                                  Type.UTF_8_STRING);
        keywordTagCD.addAttribute(attribute);

        /**
         * Initialize values of the Resource class.
         */
        attribute = new Attribute("uti", "UTI",
                                  Type.UTF_8_STRING);
        resourceCD.addAttribute(attribute);

        attribute = new Attribute("notes", "Notes",
                                  Type.UTF_8_STRING);
        resourceCD.addAttribute(attribute);

        attribute = new Attribute("name", "Name",
                                  Type.UTF_8_STRING);
        resourceCD.addAttribute(attribute);

        /**
         * Initialize values of the User class.
         */
        attribute = new Attribute("userName", "User Name",
                                  Type.UTF_8_STRING);
        userCD.addAttribute(attribute);

        /**
         * Initialize values of the AnalysisRecord class.
         */
        attribute = new Attribute("name", "Name",
                                  Type.UTF_8_STRING);
        analysisRecordCD.addAttribute(attribute);

        attribute = new Attribute("notes", "Notes",
                                  Type.UTF_8_STRING);
        analysisRecordCD.addAttribute(attribute);

        attribute = new Attribute("scmRevision", "Source Code Revision",
                                  Type.INT_32);
        analysisRecordCD.addAttribute(attribute);

        attribute = new Attribute("scmURL", "Source Code Repository URL",
                                  Type.UTF_8_STRING);
        analysisRecordCD.addAttribute(attribute);

        attribute = new Attribute("entryFunctionName", "Entry Function Name",
                                  Type.UTF_8_STRING);
        analysisRecordCD.addAttribute(attribute);

        attribute = new Attribute("analysisParameters", "Analysis Parameters",
                                  Type.PARAMETERS_MAP, null, Cardinality.N_A);
        analysisRecordCD.addAttribute(attribute);

        attribute = new Attribute("epochs", "Epochs",
                                  Type.REFERENCE,
                                  epochCD, Cardinality.TO_MANY);
        analysisRecordCD.addAttribute(attribute);

        attribute = new Attribute("project", "Project",
                                  Type.REFERENCE,
                                  projectCD, Cardinality.TO_ONE);
        analysisRecordCD.addAttribute(attribute);
    }


    /**
     * Get a ClassDescription object using its name member data.
     *
     * If a ClassDescription with the passed in name does not
     * exist, this method prints an error message and returns
     * null.  We could instead throw an IllegalArgumentException.
     * What would the caller prefer?
     *
     * @param name The name of the ClassDescription.  For example, "Epoch", "Source", "TaggableEntityBase".
     * @return ClassDescription for class name.
     */
    public static ClassDescription getClassDescription(String name) {

        /**
         * Be sure the ClassDescription singleton has been initialized.
         */
        getInstance();

        for (ClassDescription classDescription : allClassDescriptions)
            if (classDescription.getName().equals(name))
                return(classDescription);

        System.err.println("ERROR:  In getClassDescription().  "+
            "Caller asked for unknown class with name \""+name+"\".");
        /*
        throw(new IllegalArgumentException("Caller asked for unknown
                                           class with name \""+name+"\"."));
        */
        return(null);
    }


    /**
     * Get a list of all the ClassDescription objects.
     *
     * Please note, this method does NOT return a copy of the
     * list, so don't mess with it.
     * @return List of all known class descriptions
     */
//    public static ArrayList<ClassDescription> getAllClassDescriptions() {
//
//        /**
//         * Be sure the ClassDescription singleton has been initialized.
//         */
//        getInstance();
//
//        return(allClassDescriptions);
//    }


    /**
     * Get a list of the ClassDescription objects that can
     * be used as a CUQ (Class Under Qualification).
     *
     * Please note, this method does NOT return a copy of the
     * list, so don't mess with it.
     * @return List of desired CUQs
     */
    public static ArrayList<ClassDescription> getPossibleCUQs() {

        /**
         * Be sure the ClassDescription singleton has been initialized.
         */
        getInstance();

        return(possibleCUQs);
    }


    /**
     * Get a COPY of the Attribute with the passed in name
     * from ANY ClassDescription that contains it.
     *
     * @param queryName The name used in the PQL expressions.
     * Not necessarily the same as the string used in the GUI.
     * E.g. "mykeywords" instead of "My Keywords".
     */
    public static Attribute getAttribute(String queryName) {

        for (ClassDescription cd : allClassDescriptions) {
            Attribute attribute = cd.getAttribute(queryName);
            if (attribute != null)
                return(attribute);
        }

        /**
         * Didn't find this attribute in any class.
         */
        return(null);
    }


    /**
     * This is a simple test program for this class.
     * @param args ignored
     */
    public static void main(String[] args) {

        System.out.println("DataModel test is starting...");

        DataModel.getInstance();

        System.out.println("DataModel test is ending.");
    }
}
