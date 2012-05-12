package ru.external;

import ru.hse.se.nodes.Node;

public class Person extends Node {

    public Person() {
        mother = father = null;
        name = null;
        age = 0;
    }

    
    public void setName(String str) {
        name = str;
    }
    
    public void setAge(int i) {
        age = i;
    }
    
    public void setMother(Person p) {
        mother = p;
    }

    public void setFather(Person p) {
        father = p;
    }
    
    
    public String getName() {
        return name;
    }
    
    public int getAge() {
        return age;
    }
    
    public Person getMother() {
        return mother;
    }
    
    public Person getFather() {
        return father;
    }

    
    public String containerField() {
        return "mother";
    }
    
    
    private Person mother, father;
    private String name;
    private int age;
    
    private static final long serialVersionUID = 1L;
}
