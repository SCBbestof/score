/*
 * Licensed to Hewlett-Packard Development Company, L.P. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/
package com.hp.score.lang.tests.runtime.builders;

import com.hp.score.api.ControlActionMetadata;
import com.hp.score.api.ExecutionPlan;
import com.hp.score.api.ExecutionStep;
import com.hp.score.lang.runtime.ActionType;
import com.hp.score.lang.runtime.Navigations;
import com.hp.score.lang.runtime.POCControlActions;
import com.hp.score.lang.tests.runtime.actions.LangActions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: stoneo
 * Date: 06/10/2014
 * Time: 09:34
 */
public class POCParentExecutionPlanActionsBuilder {

    public static final String CONTROL_ACTION_CLASS_NAME = POCControlActions.class.getName();
    public static final String NEXT_STEP_ID_KEY = "nextStepId";
    public static final String NAVIGATION_ACTIONS_CLASS = Navigations.class.getName();
    public static final String SIMPLE_NAVIGATION_METHOD = "navigate";

    public static final String ACTION_CLASS_KEY = "className";
    public static final String ACTION_METHOD_KEY = "methodName";

    ExecutionPlan executionPlan;

    private Long index = 1L;

    public POCParentExecutionPlanActionsBuilder() {
        createExecutionPlan();
    }

    private void createExecutionPlan() {
        executionPlan = new ExecutionPlan();
        executionPlan.setFlowUuid("parentFlow");
        executionPlan.setBeginStep(1L);
        executionPlan.addStep(createFlowStartStep());
        executionPlan.addStep(createFirstBeginTaskStep());
        executionPlan.addStep(createFirstFinishTaskStep());
        addSecondStep();
        executionPlan.addStep(createFlowEndStep());
    }

    private void addSecondStep() {
        executionPlan.addStep(createSecondBeginTaskStep());
        executionPlan.addStep(createSecondStartStep());
        executionPlan.addStep(createActionStep(LangActions.class.getName(), "print"));
        executionPlan.addStep(createSecondEndStep());
        executionPlan.addStep(createSecondFinishTaskStep());
    }

    private ExecutionStep createSecondFinishTaskStep() {
        Map<String, Serializable> actionData = new HashMap<>();
        HashMap<String, Serializable> taskPublishValues = createFirstTaskPublishValues();
        actionData.put("taskPublishValues", taskPublishValues);
        HashMap<String, Long> taskNavigationValues = createSecondTaskNavigationValues();
        actionData.put("taskNavigationValues", taskNavigationValues);
        ExecutionStep finishTask = createGeneralStep(index, CONTROL_ACTION_CLASS_NAME, "finishTask", ++index, actionData);
        finishTask.setNavigationData(null);
        return finishTask;
    }

    private ExecutionStep createSecondEndStep() {
        Map<String, Serializable> actionData = new HashMap<>();
        actionData.put("operationOutputs", new HashMap<>());
        HashMap<String, Serializable> operationAnswers = createOperationAnswers();
        actionData.put("operationAnswers", operationAnswers);
        return createGeneralStep(index, CONTROL_ACTION_CLASS_NAME, "end", ++index, actionData);
    }

    private HashMap<String, Serializable> createOperationAnswers() {
        LinkedHashMap<String, Serializable> operationAnswers = new LinkedHashMap<>();
        operationAnswers.put("SUCCESS", "retVal[isTrue]");
        operationAnswers.put("FAIL", "retVal[isFalse]");
        return operationAnswers;
    }

    private ExecutionStep createActionStep(String className, String methodName) {
        Map<String, Serializable> actionData = new HashMap<>();
        //put the actual action class name and method name
        actionData.put(ACTION_CLASS_KEY, className);
        actionData.put(ACTION_METHOD_KEY, methodName);
        actionData.put("actionType", ActionType.JAVA);
        return createGeneralStep(index, CONTROL_ACTION_CLASS_NAME, "doAction", ++index, actionData);
    }

    private ExecutionStep createSecondStartStep() {
        Map<String, Serializable> actionData = new HashMap<>();
        HashMap<String, Serializable> operationInputs = createOperationInputs();
        actionData.put("operationInputs", operationInputs);
        return createGeneralStep(index, CONTROL_ACTION_CLASS_NAME, "start", ++index, actionData);
    }

    private HashMap<String, Serializable> createOperationInputs() {
        LinkedHashMap<String, Serializable> operationInputs = new LinkedHashMap<>();
        operationInputs.put("first_name", null);
        operationInputs.put("id", null);
        operationInputs.put("emp_user", null);
        operationInputs.put("duration", null);
        operationInputs.put("string", "booooyahh!!");
        return operationInputs;
    }

    private ExecutionStep createSecondBeginTaskStep() {
        Map<String, Serializable> actionData = new HashMap<>();
        HashMap<String, Serializable> taskInputs = createSecondBeginTaskTaskInputs();
        actionData.put("taskInputs", taskInputs);
        return createGeneralStep(index, CONTROL_ACTION_CLASS_NAME, "beginTask", ++index, actionData);
    }

    private HashMap<String, Serializable> createSecondBeginTaskTaskInputs() {
        LinkedHashMap<String, Serializable> taskInputs = new LinkedHashMap<>();
        taskInputs.put("first_name", null);
        taskInputs.put("id", null);
        taskInputs.put("emp_user", "user");
        taskInputs.put("duration", "dur");
        return taskInputs;
    }

    public ExecutionPlan getExecutionPlan() {
        return executionPlan;
    }

    private ExecutionStep createFlowStartStep() {
        Map<String, Serializable> actionData = new HashMap<>();
        HashMap<String, Serializable> flowInputs = createFlowInputs();
        actionData.put("operationInputs", flowInputs);
        return createGeneralStep(index, CONTROL_ACTION_CLASS_NAME, "start", ++index, actionData);
    }

    private ExecutionStep createFirstBeginTaskStep() {
        Map<String, Serializable> actionData = new HashMap<>();
        HashMap<String, Serializable> taskInputs = createFirstBeginTaskTaskInputs();
        actionData.put("taskInputs", taskInputs);

        ExecutionStep beginTaskStep = createGeneralStep(index, CONTROL_ACTION_CLASS_NAME, "beginTask", ++index, actionData);
        HashMap<String, Object> navigationData = new HashMap<>(new HashMap<>(beginTaskStep.getNavigationData()));
        navigationData.put("subFlowId", "childFlow");
        beginTaskStep.setNavigationData(navigationData);

        return beginTaskStep;
    }

    private ExecutionStep createFirstFinishTaskStep() {
        Map<String, Serializable> actionData = new HashMap<>();
        HashMap<String, Serializable> taskPublishValues = createFirstTaskPublishValues();
        actionData.put("taskPublishValues", taskPublishValues);
        HashMap<String, Long> taskNavigationValues = createFirstTaskNavigationValues();
        actionData.put("taskNavigationValues", taskNavigationValues);
        ExecutionStep finishTask = createGeneralStep(index, CONTROL_ACTION_CLASS_NAME, "finishTask", ++index, actionData);
        finishTask.setNavigationData(null);
        return finishTask;
    }

    private ExecutionStep createFlowEndStep() {
        Map<String, Serializable> actionData = new HashMap<>();
        HashMap<String, Serializable> flowOutputs = createFlowOutputs();
        actionData.put("operationOutputs", flowOutputs);
        HashMap<String, Serializable> flowAnswers = createFlowAnswers();
        actionData.put("operationAnswers", flowAnswers);
        return createGeneralStep(index, CONTROL_ACTION_CLASS_NAME, "end", null, actionData);
    }

    private HashMap<String, Serializable> createFirstBeginTaskTaskInputs() {
        LinkedHashMap<String, Serializable> taskInputs = new LinkedHashMap<>();
        taskInputs.put("emp_first_name", "first_name");
        taskInputs.put("mail_server", null);
        taskInputs.put("admin_user", "admin");
        return taskInputs;
    }

    private HashMap<String, Serializable> createFlowInputs() {
        LinkedHashMap<String, Serializable> flowInputs = new LinkedHashMap<>();
        flowInputs.put("first_name", "name");
        flowInputs.put("id", null);
        flowInputs.put("mail_server", "mail.host");
        return flowInputs;
    }

    private HashMap<String, Serializable> createFirstTaskPublishValues() {
        LinkedHashMap<String, Serializable> taskPublishValues = new LinkedHashMap<>();
        taskPublishValues.put("user", null);
        taskPublishValues.put("dur", "duration");
        return taskPublishValues;
    }

    private HashMap<String, Long> createFirstTaskNavigationValues() {
        LinkedHashMap<String, Long> navigationValues = new LinkedHashMap<>();
        navigationValues.put("SUCCESS", index + 1);
        navigationValues.put("FAIL", null);
        return navigationValues;
    }

    private HashMap<String, Long> createSecondTaskNavigationValues() {
        LinkedHashMap<String, Long> navigationValues = new LinkedHashMap<>();
        navigationValues.put("SUCCESS", index + 1);
        navigationValues.put("FAIL", null);
        return navigationValues;
    }

    private HashMap<String, Serializable> createFlowOutputs() {
        LinkedHashMap<String, Serializable> flowOutputs = new LinkedHashMap<>();
        flowOutputs.put("flow_url", "$task_url");
        return flowOutputs;
    }

    private HashMap<String, Serializable> createFlowAnswers() {
        LinkedHashMap<String, Serializable> flowAnswers = new LinkedHashMap<>();
        //todo: how do I resolve the flow answer?
        flowAnswers.put("SUCCESS", "retVal[isTrue]");
        flowAnswers.put("FAIL", "retVal[isFalse]");
        return flowAnswers;
    }

    public ExecutionStep createGeneralStep(
            Long stepId,
            String actionClassName,
            String actionMethodName,
            Long nextStepId,
            Map<String, Serializable> actionData) {

        ExecutionStep step = new ExecutionStep(stepId);
        step.setAction(new ControlActionMetadata(actionClassName, actionMethodName));
        step.setActionData(actionData);

        step.setNavigation(new ControlActionMetadata(NAVIGATION_ACTIONS_CLASS, SIMPLE_NAVIGATION_METHOD));
        Map<String, Object> navigationData = new HashMap<>(2);
        navigationData.put(NEXT_STEP_ID_KEY, nextStepId);

        step.setNavigationData(navigationData);

//        step.setSplitStep(false);

        return step;
    }

}
