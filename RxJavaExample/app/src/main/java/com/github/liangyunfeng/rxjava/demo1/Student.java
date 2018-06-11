package com.github.liangyunfeng.rxjava.demo1;

import java.util.List;

/**
 * Created by yunfeng.l on 2018/6/8.
 */

public class Student {
    String name;
    List<Course> courses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
