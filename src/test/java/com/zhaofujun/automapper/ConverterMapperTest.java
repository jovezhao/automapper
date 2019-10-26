package com.zhaofujun.automapper;

import com.zhaofujun.automapper.map.Converter;
import org.junit.Assert;
import org.junit.Test;

public class ConverterMapperTest {

    @Test
    public void EnumToString() {
        IMapper mapper = new AutoMapper();
        mapper.registerConverter(new SexAndStringConverter());
        UserDo userDo = new UserDo();
        userDo.setSex(UserDo.Sex.female);

        UserDto userDto = mapper.map(userDo, UserDto.class);

        Assert.assertEquals(userDto.getSex(), "女");

    }

    @Test
    public void StringToEnum() {
        IMapper mapper = new AutoMapper();
        mapper.registerConverter(new SexAndStringConverter());
        UserDto userDto = new UserDto();
        userDto.setSex("女");

        UserDo userDo = mapper.map(userDto, UserDo.class);

        Assert.assertEquals(userDo.getSex(), UserDo.Sex.female);

    }
}

class SexAndStringConverter extends Converter<String, UserDo.Sex> {

    @Override
    protected Class<String> getSourceClass() {
        return String.class;
    }

    @Override
    protected Class<UserDo.Sex> getTargetClass() {
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
