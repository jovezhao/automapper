package com.zhaofujun.automapper;

import com.zhaofujun.automapper.beans.UserDo;
import com.zhaofujun.automapper.beans.UserDto;
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
//        mapper.registerConverter(new SexAndStringConverter());

        mapper.mapping(UserDto.class,UserDo.class)
                .field("sex","sex",new SexAndStringConverter());

        UserDto userDto = new UserDto();
        userDto.setSex("女");

        UserDo userDo = mapper.map(userDto, UserDo.class);

        Assert.assertEquals(userDo.getSex(), UserDo.Sex.female);

    }
}

