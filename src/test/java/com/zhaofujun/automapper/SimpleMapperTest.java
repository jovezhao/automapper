package com.zhaofujun.automapper;


import org.junit.Assert;
import org.junit.Test;

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
}
