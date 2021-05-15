package com.zhaofujun.automapper;

import com.zhaofujun.automapper.beans.State;
import com.zhaofujun.automapper.beans.StateConverter;
import com.zhaofujun.automapper.beans.Teacher;
import com.zhaofujun.automapper.beans.TeacherPO;
import com.zhaofujun.automapper.core.ClassMapping;
import com.zhaofujun.automapper.core.IMapper;
import org.junit.Assert;
import org.junit.Test;

public class BaseTest {

    @Test
    public void baseMap() {
        Teacher teacher = new Teacher();
        teacher.setName("name");

        IMapper mapper = new AutoMapper();
        TeacherPO teacherPO = mapper.map(teacher, TeacherPO.class);

        Assert.assertEquals(teacher.getName(), teacherPO.getName());
    }

    @Test
    public void int2StringMap() {
        Teacher teacher = new Teacher();
        teacher.setName("name");
        teacher.setAge(100);

        IMapper mapper = new AutoMapper();
        TeacherPO teacherPO = mapper.map(teacher, TeacherPO.class);
        Teacher teacher1 = mapper.map(teacherPO, Teacher.class);

        Assert.assertEquals(teacher.getName(), teacher1.getName());
        Assert.assertEquals(teacher.getAge(), teacher1.getAge());
    }

    @Test
    public void subMap() {
        Teacher teacher = new Teacher();
        teacher.setName("name");
        teacher.setAge(100);
        Teacher parent = new Teacher();
        parent.setName("parent name");
        parent.setAge(300);
        teacher.setParent(parent);

        AutoMapper mapper = new AutoMapper();
        mapper.registerClassMapping(new ClassMapping(Teacher.class, TeacherPO.class)
                .field("parent.name", "parentName")
                .field("parent.age", "parentAge"), true);

        TeacherPO teacherPO = mapper.map(teacher, TeacherPO.class);
        Teacher teacher1 = mapper.map(teacherPO, Teacher.class);

        Assert.assertEquals(teacher.getParent().getName(), teacher1.getParent().getName());

    }

    @Test
    public void excludeByConfig() {
        Teacher teacher = new Teacher();
        teacher.setName("name");
        teacher.setAge(100);

        AutoMapper autoMapper = new AutoMapper();
        autoMapper.registerClassMapping(
                new ClassMapping(Teacher.class, TeacherPO.class)
                        .exclude("age"), true);

        TeacherPO teacherPO = autoMapper.map(teacher, TeacherPO.class);

        Assert.assertEquals(teacher.getName(), teacherPO.getName());
        Assert.assertEquals(teacherPO.getAge(), null);
        teacherPO.setAge("200");
        Teacher teacher1 = autoMapper.map(teacherPO, Teacher.class);
        Assert.assertEquals(teacher.getName(), teacher1.getName());
        Assert.assertEquals(teacher1.getAge(), 0);

    }

    @Test
    public void excludeByOnce() {
        Teacher teacher = new Teacher();
        teacher.setName("name");
        teacher.setAge(100);

        AutoMapper autoMapper = new AutoMapper();

        TeacherPO teacherPO = autoMapper.map(teacher, TeacherPO.class, "age");
        TeacherPO teacherPO1 = autoMapper.map(teacher, TeacherPO.class);
        Assert.assertEquals(teacher.getName(), teacherPO.getName());
        Assert.assertEquals(teacherPO.getAge(), null);
        Assert.assertEquals(teacherPO1.getAge(), String.valueOf(teacher.getAge()));
    }

    @Test
    public void converterByGlobal() {
        Teacher teacher = new Teacher();
        teacher.setName("name");
        teacher.setAge(100);
        teacher.setState(State.False);

        AutoMapper autoMapper = new AutoMapper();
        autoMapper.registerConverter(new StateConverter())
                .registerClassMapping(new ClassMapping(Teacher.class, TeacherPO.class)
                                .field("state", "stateLabel")
                        , false);

        TeacherPO teacherPO = autoMapper.map(teacher, TeacherPO.class);

        Assert.assertEquals(teacherPO.getStateLabel(), "否");

    }

    @Test
    public void converterByConfig() {
        Teacher teacher = new Teacher();
        teacher.setName("name");
        teacher.setAge(100);
        teacher.setState(State.False);

        AutoMapper autoMapper = new AutoMapper();
        autoMapper.registerClassMapping(new ClassMapping(Teacher.class, TeacherPO.class)
                        .field("state", "stateLabel", new StateConverter())
                , true);

        TeacherPO teacherPO = autoMapper.map(teacher, TeacherPO.class);
        Teacher teacher1 = autoMapper.map(teacherPO, Teacher.class);

        Assert.assertEquals(teacherPO.getStateLabel(), "否");
        Assert.assertEquals(teacher.getState(), teacher1.getState());

    }
}
