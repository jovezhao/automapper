package com.zhaofujun.automapper;

import com.zhaofujun.automapper.beans.Contact;
import com.zhaofujun.automapper.beans.UserDo;
import com.zhaofujun.automapper.beans.UserDto;
import com.zhaofujun.automapper.beans.UserDtoSubClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PerformanceTest {

    private IMapper mapper;
    private UserDo userDo;

    @Before
    public void init() {
        mapper = new AutoMapper();
        mapper.mapping(UserDo.class, UserDto.class)
                .field("contact.address", "contactAddress")
                .field("contact.tel", "contactTel");

        Contact contact = new Contact();
        contact.setAddress("address");
        contact.setTel(11);

        userDo = new UserDo();
        userDo.setContact(contact);
        userDo.setAge(11);
        userDo.setName("asd");
        userDo.setSex(UserDo.Sex.male);
        userDo.setName1("功盖三分国，名成八阵图。江流石不转，遗恨失吞吴。1");
        userDo.setName2("功盖三分国，名成八阵图。江流石不转，遗恨失吞吴。2");
        userDo.setName3("功盖三分国，名成八阵图。江流石不转，遗恨失吞吴。3");
        userDo.setName4("功盖三分国，名成八阵图。江流石不转，遗恨失吞吴。4");
        userDo.setName5("功盖三分国，名成八阵图。江流石不转，遗恨失吞吴。5");
        userDo.setName6("功盖三分国，名成八阵图。江流石不转，遗恨失吞吴。6");
        userDo.setName7("功盖三分国，名成八阵图。江流石不转，遗恨失吞吴。7");
        userDo.setName8("功盖三分国，名成八阵图。江流石不转，遗恨失吞吴。8");
        userDo.setName9("功盖三分国，名成八阵图。江流石不转，遗恨失吞吴。9");
        userDo.setName10("功盖三分国，名成八阵图。江流石不转，遗恨失吞吴。10");
        userDo.setName11("功盖三分国，名成八阵图。江流石不转，遗恨失吞吴。11");
        userDo.setName12("功盖三分国，名成八阵图。江流石不转，遗恨失吞吴。12");
        userDo.setName13("功盖三分国，名成八阵图。江流石不转，遗恨失吞吴。13");
        userDo.setName14("功盖三分国，名成八阵图。江流石不转，遗恨失吞吴。14");
        userDo.setName14("功盖三分国，名成八阵图。江流石不转，遗恨失吞吴。15");
        userDo.setName15("功盖三分国，名成八阵图。江流石不转，遗恨失吞吴。16");
        userDo.setName16("功盖三分国，名成八阵图。江流石不转，遗恨失吞吴。17");
        userDo.setName17("功盖三分国，名成八阵图。江流石不转，遗恨失吞吴。18");
        userDo.setName18("功盖三分国，名成八阵图。江流石不转，遗恨失吞吴。19");
        userDo.setName19("功盖三分国，名成八阵图。江流石不转，遗恨失吞吴。20");

        preheat();
    }


    public void preheat() {
        for (int i = 0; i < 1000; i++) {
            mapper.map(userDo, UserDto.class,true);
        }
    }

    @Test
    public void complexToSimple() {
        Long times = 0L;
        for (int y = 0; y < 100; y++) {
            long runtime_start = System.currentTimeMillis();
            UserDto userDto = new UserDto();
            for (int i = 0; i < 100000; i++) {
//                UserDto userDto = mapper.map(userDo, UserDto.class);
                mapper.map(userDo, userDto,true);
            }
            long runtime_end = System.currentTimeMillis();
            Long time = runtime_end - runtime_start;
            System.out.println("complexToSimple 100,000次映射" + y + ": 耗时" + time);
            times += time;
        }
        System.out.println("complexToSimple 100,000*100次映射" + times);
        System.out.println("平均100，1000次耗时:" + times / 100);
    }

    @Test
    public void SimpleToComplex() {
        IMapper mapper = new AutoMapper();
        mapper.mapping(UserDto.class, UserDo.class)
                .field("contactTel", "contact.tel")
                .field("contactAddress", "contact.address");

        UserDto userDto = new UserDto();
        userDto.setContactTel("123");
        userDto.setContactAddress("address");

        long runtime_start = System.currentTimeMillis();
        UserDo userDo = mapper.map(userDto, UserDo.class,true);
        long runtime_end = System.currentTimeMillis();
        System.out.println(runtime_end - runtime_start);

        Assert.assertEquals(userDo.getContact().getAddress(), userDto.getContactAddress());
    }

    @Test
    public void subClassTest() {
        IMapper mapper = new AutoMapper();
        mapper.mapping(UserDto.class, UserDo.class)
                .field("contactTel", "contact.tel")
                .field("contactAddress", "contact.address");

        UserDtoSubClass userDto = new UserDtoSubClass();
        userDto.setContactTel("123");
        userDto.setContactAddress("address");

        long runtime_start = System.currentTimeMillis();
        UserDo userDo = mapper.map(userDto, UserDo.class,true);
        long runtime_end = System.currentTimeMillis();
        System.out.println(runtime_end - runtime_start);

        Assert.assertEquals(userDo.getContact().getAddress(), userDto.getContactAddress());
    }

    @Test
    public void copySimple() {
        UserDto userDto = new UserDto();
        long start = System.nanoTime();
        //UserDto userDto1 = mapper.map(userDo, UserDto.class);
        mapper.map(userDo, userDto,true);
        long end = System.nanoTime() - start;
        System.out.println(new BigDecimal(end).divide(new BigDecimal(1000000), 6, RoundingMode.HALF_UP) + "[ms]");
    }

    @Test
    public void copyList() {
        int times = 1000 * 1000;
        UserDto userDto = new UserDto();
        long start = System.nanoTime();
        for (int i = 0; i < times; i++) {
            //mapper.map(userDo, UserDto.class);
            mapper.map(userDo, userDto,true);
        }
        long end = System.nanoTime() - start;
        System.out.println(new BigDecimal(end).divide(new BigDecimal(1000000), 6, RoundingMode.HALF_UP) + "[ms]");
        System.out.println(new BigDecimal(end / times).divide(new BigDecimal(1000000), 6, RoundingMode.HALF_UP) + "[ms]");
    }
}

