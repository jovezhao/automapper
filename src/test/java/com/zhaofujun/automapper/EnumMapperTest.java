package com.zhaofujun.automapper;

import com.zhaofujun.automapper.beans.UserDo;
import com.zhaofujun.automapper.beans.UserDto;
import org.junit.Assert;
import org.junit.Test;

public class EnumMapperTest {
    @Test
    public void enumToString() {


        IMapper mapper = new AutoMapper();

        UserDo userDo = new UserDo();
        userDo.setSex(UserDo.Sex.male);
        UserDto userDto = mapper.map(userDo, UserDto.class);

        Assert.assertEquals(userDto.getSex(), "male");
    }

    @Test
    public void stringToEnum() {
        IMapper mapper = new AutoMapper();

        UserDto userDto = new UserDto();
        userDto.setSex("male");
        UserDo userDo = mapper.map(userDto, UserDo.class);

        Assert.assertEquals(userDo.getSex(), UserDo.Sex.male);
    }

}

