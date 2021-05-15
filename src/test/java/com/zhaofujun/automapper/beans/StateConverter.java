package com.zhaofujun.automapper.beans;

import com.zhaofujun.automapper.core.Converter;

public class StateConverter implements Converter<State, String> {
    @Override
    public Class<State> getSourceClass() {
        return State.class;
    }

    @Override
    public Class<String> getTargetClass() {
        return String.class;
    }

    @Override
    public String toTarget(State source) {
        if (source == State.False)
            return "否";
        return "是";
    }

    @Override
    public State toSource(String target) {
        if (target.equals("否"))
            return State.False;
        return State.True;
    }
}
