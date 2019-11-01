package com.zhaofujun.automapper;

import com.zhaofujun.automapper.beans.UserDo;
import com.zhaofujun.automapper.converter.Converter;

public class SexAndStringConverter extends Converter<String, UserDo.Sex> {

    @Override
    public Class<String> getSourceClass() {
        return String.class;
    }

    @Override
    public Class<UserDo.Sex> getTargetClass() {
        return UserDo.Sex.class;
    }

    @Override
    public UserDo.Sex toTarget(String source) {
        if (source.equals("男"))
            return UserDo.Sex.male;
        else
            return UserDo.Sex.female;
    }

    @Override
    public String toSource(UserDo.Sex target) {
        if (target.equals(UserDo.Sex.male))
            return "男";
        else
            return "女";
    }
}
