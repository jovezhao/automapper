package com.zhaofujun.automapper;


import com.zhaofujun.automapper.beans.UserDo;
import com.zhaofujun.automapper.beans.UserDto;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleMapperTest {

    @Test
    public  void defaultMap() {
        UserDo userDo=new UserDo();
        userDo.setAge(100);

        IMapper mapper=new AutoMapper();
        UserDto userDto = mapper.map(userDo, UserDto.class);
        Assert.assertEquals(userDo.getAge(),userDto.getAge());

    }

    @Test
    public void noSetterMap(){
        UserDto userDto=new UserDto();
        userDto.setId("idid");

        IMapper mapper=new AutoMapper();
        //允许映射包括没有setter的字段
        mapper.mapping(UserDto.class,UserDo.class,true);
        UserDo userDo = mapper.map(userDto, UserDo.class);
        Assert.assertEquals(userDo.getId(),userDto.getId());

        UserDto userDto1 = mapper.map(userDo, UserDto.class);
        Assert.assertEquals(userDo.getId(),userDto1.getId());


    }

    @Test
    public void differentFieldMap(){
        UserDto userDto=new UserDto();
        userDto.setRealName("name");

        IMapper mapper=new AutoMapper();
        //允许映射包括没有setter的字段
        mapper.mapping(UserDto.class,UserDo.class,true)
                .field("realName","name");

        UserDo userDo = mapper.map(userDto, UserDo.class);
        Assert.assertEquals(userDo.getName(),userDto.getRealName());
    }
    @Test
    public void excludeField(){
        UserDto userDto=new UserDto();
        userDto.setRealName("name");
        userDto.setAge(10);

        IMapper mapper=new AutoMapper();
        //允许映射包括没有setter的字段
        mapper.mapping(UserDto.class,UserDo.class,true)
                .field("realName","name")
                .excludes("age");

        UserDo userDo = mapper.map(userDto, UserDo.class);
        Assert.assertEquals(userDo.getAge(),0);
    }


    @Test
    public void testHashmap(){
        List<UserDto> us=new ArrayList<>();
        UserDto e1 = new UserDto();
        e1.setId("11");
        e1.setName1("name1");
        us.add(e1);


        UserDto e2 = new UserDto();
        e2.setId("22");
        e2.setName1("name2");
        us.add(e2);

        Map<String, String> collect = us.stream().collect(Collectors.toMap(UserDto::getId, UserDto::getName1));

    }
}
